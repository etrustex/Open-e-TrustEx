package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commoncomponents_3.EmbeddedDocumentBinaryObjectType;
import ec.schema.xsd.viewrequest_3.ViewRequestType;
import ec.schema.xsd.viewresponse_3.ObjectFactory;
import ec.schema.xsd.viewresponse_3.ViewResponseType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class ViewRequestService_3 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(ViewRequestService_3.class);
	
	private static JAXBContext jaxbContext = null;
	
	private static String MIME_UTF8 = "UTF8";
	
	private static final String TEXT_HTML = "text/html";
	private static final Object HTML_CDATA_START = "<![CDATA[";
	private static final Object HTML_CDATA_STOP = "]]>";

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(TrustExMessage<String> message) throws BusinessException {
		try {
			logger.debug("Start processMessage");
			TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
			responseMessage.setHeader(message.getHeader());
			logger.debug("Sender id with scheme" + message.getHeader().getSenderIdWithScheme());
			
			Source s = new StreamSource(new StringReader(message.getPayload()));
			JAXBElement<ViewRequestType> requestElement = getJaxBContext().createUnmarshaller().unmarshal(s, ViewRequestType.class);
			ViewRequestType request = requestElement.getValue();

			String documentId = request.getDocumentReference().getInstanceIdentifier();
			if (documentId != null) {
				int sepIndex = documentId.indexOf(ID_SEPARATOR);
				if (sepIndex != -1) {
					documentId = documentId.substring(0, sepIndex).trim().concat(ID_SEPARATOR)
							.concat(documentId.substring(sepIndex + ID_SEPARATOR.length(), documentId.length()).trim());
				} else {
					documentId = documentId.trim();
				}
			}
			logger.debug("Document id:" + documentId);
			

			String documentTypeCode = request.getDocumentReference().getType();
			logger.debug("Document type code:" + documentTypeCode);
			
			Party senderP = null;
			Party receiverP = null;
			String partyIdentifier = "";
			try {
				// Check if the Sender and REceiverParty are present in the message  (see 2.4.4 of use case)
				if (request.getSenderParty() == null || request.getSenderParty().getIdentifier().getValue() == null || request.getReceiverParty() == null
						|| request.getReceiverParty().getIdentifier().getValue() == null) {
					throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Sender- ReceiverPartyEndpoint not present in the viewRequest");
				}
				
				List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
				String schemeIdSeparator = metadata.get(0).getValue();
				
				partyIdentifier = StringUtils.isNotBlank(request.getSenderParty().getIdentifier().getAuthority()) ? request.getSenderParty()
						.getIdentifier().getAuthority()
						+ schemeIdSeparator + request.getSenderParty().getIdentifier().getValue() : request.getSenderParty().getIdentifier().getValue();
				senderP = authorisationService.getParty(partyIdentifier, message.getHeader().getIssuer().getBusinessDomain());

				partyIdentifier = StringUtils.isNotBlank(request.getReceiverParty().getIdentifier().getAuthority()) ? request.getReceiverParty()
						.getIdentifier().getAuthority()
						+ schemeIdSeparator + request.getReceiverParty().getIdentifier().getValue() : request.getReceiverParty().getIdentifier()
						.getValue();
				receiverP = authorisationService.getParty(partyIdentifier, message.getHeader().getIssuer().getBusinessDomain());

			} catch (UndefinedIdentifierException e) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
			}
			if (!message.getHeader().getSenderPartyId().equals(senderP.getId()) && !message.getHeader().getSenderPartyId().equals(receiverP.getId())) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "SenderParty not equal to the sender- or receiverParty of the viewRequest");
			}
			
			ObjectFactory fact = new ObjectFactory();
			ViewResponseType response = fact.createViewResponseType();
			Transaction t = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			
			List<Message> messagesReceived = messageService.retrieveMessages(receiverP.getId(), senderP.getId(), null, null, null, null, documentId,
					documentTypeCode, null, false, false, false);
			
			Message mainDoc = null;
			if (messagesReceived != null && messagesReceived.size() > 0) {
				logger.debug("Message found as sender");
				mainDoc = messagesReceived.get(0);
			} else {
				logger.debug("No message found as sender");
			}
			
			EmbeddedDocumentBinaryObjectType emb = null;

			if (mainDoc == null) {
				emb = new EmbeddedDocumentBinaryObjectType();
				emb.setMimeCode(MIME_UTF8);
				String encoded = new String(Base64.encode(new String("").getBytes()));
				emb.setValue(encoded.getBytes());
			} else {
				boolean found = false;
				for (MessageBinary messageBinary : mainDoc.getBinaries()) {
					if (MessageBinaryType.HUMAN_READABLE_MESSAGE.name().equals(messageBinary.getBinaryType())) {
						logger.debug("Message XML content found");
						if (TEXT_HTML.equals(messageBinary.getMimeCode())) {
							emb = new EmbeddedDocumentBinaryObjectType();
							String doc = new String(messageBinary.getBinary(), "UTF-8");
							emb.setValue((HTML_CDATA_START+doc+HTML_CDATA_STOP).getBytes());
						} else {
							emb = new EmbeddedDocumentBinaryObjectType();
							emb.setMimeCode(messageBinary.getMimeCode());
							String encoded = new String(Base64.encode(messageBinary.getBinary()));
							emb.setValue(encoded.getBytes());
						}
						found = true;
						break;
					}
				}
				if (!found) {
					emb = new EmbeddedDocumentBinaryObjectType();
					emb.setMimeCode(MIME_UTF8);
					String encoded = new String(Base64.encode(new String("").getBytes()));
					emb.setValue(encoded.getBytes());
				}
			}		
			//END
			response.setEmbeddedDocumentBinaryObject(emb);
			String responseElementLocalName = fact.createViewResponse(response).getName().getLocalPart();

			JAXBElement<ViewResponseType> responseElement = new JAXBElement<ViewResponseType>(new QName(t.getNamespace(), responseElementLocalName, "ec"), ViewResponseType.class, response);
			
			logger.debug("Response:" + responseElement);
			responseMessage.setPayload(responseElement);
			logger.debug("Stop processMessage");
			return responseMessage;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}

	}
	
	private static JAXBContext getJaxBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(ViewRequestType.class);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}
}
