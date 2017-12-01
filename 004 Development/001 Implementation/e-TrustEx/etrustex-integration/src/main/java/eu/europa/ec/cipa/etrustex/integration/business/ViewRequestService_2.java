package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

public class ViewRequestService_2 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(ViewRequestService_2.class);
	private static final String NOT_FOUND = "&lt;!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.0 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>&lt;html>&lt;body>No matches were found&lt;/body>&lt;/html>";
	private static final String TEXT_HTML = "text/html";
	private static final Object HTML_CDATA_START = "<![CDATA[";
	private static final Object HTML_CDATA_STOP = "]]>";
	private static final String RESP_START_TAG = "<ec:ViewResponse>";
	private static final String RESP_END_TAG = "</ec:ViewResponse>";

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException {
		try {
			logger.debug("Start processMessage");
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(null);
			responseMessage.setHeader(message.getHeader());
			logger.debug("Sender id with scheme" + message.getHeader().getSenderIdWithScheme());

			DocumentInfo docInfo = null;
			final Configuration config = new Configuration();
			try {

				docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			} catch (XPathException e) {
				logger.error("Not able to parse XML");
				throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
			}

			String documentId = null;
			try {
				documentId = queryForSingle("//*:ViewRequest/*:DocumentReferenceRequest/*:ID", docInfo, config);
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
			} catch (XPathException e) {
				logger.debug("Not able to find ID");
				throw new BusinessException("soapenv:Client", ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null,
						ErrorResponseCode.INVALID_MESSAGE_ID, null, null);
			}

			String documentTypeCode = null;
			try {
				documentTypeCode = queryForSingle("//*:ViewRequest/*:DocumentReferenceRequest/*:DocumentTypeCode", docInfo, config);
				logger.debug("Document type code:" + documentTypeCode);
			} catch (XPathException e) {
				logger.debug("Not able to find document type code");
				throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}

			Party senderP = null;
			Party receiverP = null;
			String partyIdentifier = "";
			try {
				// Check if the Sender and REceiverParty are present in the message  (see 2.4.4 of use case)
				if (StringUtils.isEmpty(queryForSingle("//*:ViewRequest/*:SenderParty/*:EndpointID", docInfo, config))
						|| StringUtils.isEmpty(queryForSingle("//*:ViewRequest/*:ReceiverParty/*:EndpointID", docInfo, config))) {
					throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Sender- ReceiverPartyEndpoint not present in the viewRequest");
				}
				
				if (StringUtils.isNotEmpty(queryForSingle("//*:ViewRequest/*:SenderParty/*:EndpointID/@schemeID", docInfo, config))) {
					partyIdentifier += queryForSingle("//*:ViewRequest/*:SenderParty/*:EndpointID/@schemeID", docInfo, config) + "#";
				}
				partyIdentifier += queryForSingle("//*:ViewRequest/*:SenderParty/*:EndpointID", docInfo, config);
				senderP = authorisationService.getParty(partyIdentifier, message.getHeader().getIssuer().getBusinessDomain());

				partyIdentifier = "";
				if (StringUtils.isNotEmpty(queryForSingle("//*:ViewRequest/*:ReceiverParty/*:EndpointID/@schemeID", docInfo, config))) {
					partyIdentifier += queryForSingle("//*:ViewRequest/*:ReceiverParty/*:EndpointID/@schemeID", docInfo, config) + "#";
				}
				partyIdentifier += queryForSingle("//*:ViewRequest/*:ReceiverParty/*:EndpointID", docInfo, config);
				receiverP = authorisationService.getParty(partyIdentifier, message.getHeader().getIssuer().getBusinessDomain());

			} catch (XPathException e) {
				logger.debug("Not able to find the Sender Party");
				throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
			} catch (UndefinedIdentifierException e) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
			}
			if (!message.getHeader().getSenderPartyId().equals(senderP.getId()) && !message.getHeader().getSenderPartyId().equals(receiverP.getId())) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "SenderParty not equal to the sender- or receiverParty of the viewRequest");
			}

			StringBuffer response = new StringBuffer();
			response.append(RESP_START_TAG);
			Message mainDoc = null;
			List<Message> messagesReceived = messageService.retrieveMessages(receiverP.getId(), senderP.getId(), null, null, null, null, documentId,
					documentTypeCode, null, false, false, false);

			if (messagesReceived != null && messagesReceived.size() > 0) {
				logger.debug("Message found as sender");
				mainDoc = messagesReceived.get(0);
			} else {
				logger.debug("No message found as sender");
			}

			if (mainDoc == null) {
				String startTag = "<urn:EmbeddedDocumentBinaryObject xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' mimeCode='UTF8'>";
				String endTag = "</urn:EmbeddedDocumentBinaryObject>";
				String encoded = new String(Base64.encode(new String("").getBytes()));
				response.append(startTag);
				response.append(encoded);
				response.append(endTag);
			} else {
				boolean found = false;
				for (MessageBinary messageBinary : mainDoc.getBinaries()) {
					if (MessageBinaryType.HUMAN_READABLE_MESSAGE.name().equals(messageBinary.getBinaryType())) {
						logger.debug("Message XML content found");

						if (TEXT_HTML.equals(messageBinary.getMimeCode())) {
							String doc = new String(messageBinary.getBinary(), "UTF-8");
							response.append(HTML_CDATA_START);
							response.append(doc);
							response.append(HTML_CDATA_STOP);
						} else {
							String startTag = "<urn:EmbeddedDocumentBinaryObject xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' mimeCode='"
									+ messageBinary.getMimeCode() + "'>";
							String endTag = "</urn:EmbeddedDocumentBinaryObject>";
							String encoded = new String(Base64.encode(messageBinary.getBinary()));
							response.append(startTag);
							response.append(encoded);
							response.append(endTag);
						}
						found = true;
						break;
					}
				}
				if (!found) {
					String startTag = "<urn:EmbeddedDocumentBinaryObject xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' mimeCode='UTF8'>";
					String endTag = "</urn:EmbeddedDocumentBinaryObject>";
					String encoded = new String(Base64.encode(new String("").getBytes()));
					response.append(startTag);
					response.append(encoded);
					response.append(endTag);
				}
			}
			response.append(RESP_END_TAG);
			logger.debug("Response:" + response.toString());
			responseMessage.setPayload(response.toString());
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
}
