package eu.europa.ec.etrustex.integration.business;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

public class ViewRequestService_2 extends TrustExBusinessService implements ISynchBusinessService {

	private static final String RECEIVER_PARTY_SCHEME_XPATH = "//*:ViewRequest/*:ReceiverParty/*:EndpointID/@schemeID";
	private static final String RECEIVER_PARTY_ID_XPATH = "//*:ViewRequest/*:ReceiverParty/*:EndpointID";
	private static final String SENDER_PARTY_SCHEME_XPATH = "//*:ViewRequest/*:SenderParty/*:EndpointID/@schemeID";
	private static final String SENDER_PARTY_ID_XPATH = "//*:ViewRequest/*:SenderParty/*:EndpointID";
	private static final Logger logger = LoggerFactory.getLogger(ViewRequestService_2.class);
	private static final String TEXT_HTML = "text/html";
	private static final Object HTML_CDATA_START = "<![CDATA[";
	private static final Object HTML_CDATA_STOP = "]]>";
	private static final String RESP_START_TAG = "<ec:ViewResponse>";
	private static final String RESP_END_TAG = "</ec:ViewResponse>";
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {
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

		String documentId = extractDocumentId(docInfo, config);
		

		String documentTypeCode = null;
		try {
			documentTypeCode = queryForSingle("//*:ViewRequest/*:DocumentReferenceRequest/*:DocumentTypeCode", docInfo, config);
			logger.debug("Document type code:" + documentTypeCode);
		} catch (XPathException e) {
			logger.debug("Not able to find document type code");
			throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
		}
		if (StringUtils.isBlank(documentTypeCode)) {
			throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docTypeCode"));	
		}
		Party senderP = null;
		Party receiverP = null;
		String partyIdentifier = "";
		String schemeId = "";
		try {
			//extract sender ID
			partyIdentifier = queryForSingle(SENDER_PARTY_ID_XPATH, docInfo, config);
			if (StringUtils.isBlank(partyIdentifier)) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.sender.missing"));
			}
			
			schemeId = queryForSingle(SENDER_PARTY_SCHEME_XPATH, docInfo, config);
			if (StringUtils.isNotEmpty(schemeId)) {
				partyIdentifier = schemeId + "#" + partyIdentifier;
			}
			try{
				senderP = authorisationService.getParty(partyIdentifier, message.getHeader().getIssuer().getBusinessDomain());
			}catch (UndefinedIdentifierException e) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.senderParty.unknown"));
			}
			//extract receiver ID
			partyIdentifier = queryForSingle(RECEIVER_PARTY_ID_XPATH, docInfo, config);
			if (StringUtils.isBlank(partyIdentifier)) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.receiver.missing"));
				
			}
			schemeId = queryForSingle(RECEIVER_PARTY_SCHEME_XPATH, docInfo, config);
			if (StringUtils.isNotEmpty(schemeId)) {
				partyIdentifier = schemeId + "#" + partyIdentifier;
			}
			try{
				receiverP = authorisationService.getParty(partyIdentifier, message.getHeader().getIssuer().getBusinessDomain());
			}catch (UndefinedIdentifierException e) {
				throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
						ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.receiverParty.unknown"));
			}

		} catch (XPathException e) {
			logger.debug("Not able to find the Sender Party");
			throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.senderParty.missing"));
		}
		
		if (!message.getHeader().getSenderPartyId().equals(senderP.getId()) && !message.getHeader().getSenderPartyId().equals(receiverP.getId())) {
			throw new BusinessException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.hardrule.businessHdr.sender"));
		}

		StringBuffer response = new StringBuffer();
		response.append(RESP_START_TAG);
		Message mainDoc = null;
		
		MessageQueryDTO mq = new MessageQueryDTO();
		mq.setReceiverPartyId(receiverP.getId());
		mq.setSenderPartyId(senderP.getId());
		mq.setMessageDocumentId(documentId);
		mq.setDocumentTypeCode(documentTypeCode);
		mq.setFilterOutMessagesInError(Boolean.FALSE);
		List<Message> messagesReceived = messageService.retrieveMessages(mq);

		if (messagesReceived != null && messagesReceived.size() > 0) {
			logger.debug("Message found as sender");
			mainDoc = messagesReceived.get(0);
		} else {
			logger.debug("No message found as sender");
		}

		if (mainDoc == null) {
			String startTag = "<urn:EmbeddedDocumentBinaryObject xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' mimeCode='UTF8'>";
			String endTag = "</urn:EmbeddedDocumentBinaryObject>";
			String encoded = "";
			response.append(startTag);
			response.append(encoded);
			response.append(endTag);
		} else {
			MessageBinary messageBinary = messageService.getMessageBinary(mainDoc.getId(), MessageBinaryType.HUMAN_READABLE_MESSAGE.name());
			
			if (messageBinary != null) {
				logger.debug("Message XML content found");

				if (TEXT_HTML.equals(messageBinary.getMimeCode())) {
					String doc = null;
					try {
						doc = new String(messageBinary.getValue().getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
								ErrorResponseCode.TECHNICAL_ERROR, null, null);
					}
					response.append(HTML_CDATA_START);
					response.append(doc);
					response.append(HTML_CDATA_STOP);
				} else {
					String startTag = "<urn:EmbeddedDocumentBinaryObject xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' mimeCode='"
							+ messageBinary.getMimeCode() + "'>";
					String endTag = "</urn:EmbeddedDocumentBinaryObject>";
					String encoded = new String(Base64.encode(messageBinary.getValue().getValue()));
					response.append(startTag);
					response.append(encoded);
					response.append(endTag);
				}
				messageService.detachMessageBinary(messageBinary);					
			} else {
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
	}

	private String extractDocumentId(DocumentInfo docInfo, final Configuration config) throws BusinessException {
		String documentId = null;
		try {
			documentId = queryForSingle("//*:ViewRequest/*:DocumentReferenceRequest/*:ID", docInfo, config);
		} catch (XPathException e) {
			logger.debug("Not able to find ID");
			throw new BusinessException("soapenv:Client", ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null,
					ErrorResponseCode.INVALID_MESSAGE_ID, null, null);
		}				
		if (StringUtils.isBlank(documentId)) {
			throw new BusinessException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docId"));			
		}
		int sepIndex = documentId.indexOf(ID_SEPARATOR);
		if (sepIndex != -1) {
			documentId = documentId
					.substring(0, sepIndex)
					.trim()
					.concat(ID_SEPARATOR)
					.concat(documentId.substring(sepIndex + ID_SEPARATOR.length(), documentId.length()).trim());
		} else {
			documentId = documentId.trim();
		}
		logger.debug("Document id:" + documentId);
		return documentId;
	}
}
