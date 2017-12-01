package eu.europa.ec.cipa.eprocurement.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

public abstract class ViewRequestServiceBase extends TrustExBusinessService implements ISynchBusinessService {

	public static final Logger logger = LoggerFactory.getLogger(ViewRequestServiceBase.class);
	private static final String NOT_FOUND = "&lt;!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.0 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>&lt;html>&lt;body>No matches were found&lt;/body>&lt;/html>";
	private static final String TEXT_HTML = "text/html";
	private static final Object HTML_CDATA_START = "<![CDATA[";
	private static final Object HTML_CDATA_STOP = "]]>";

	public ViewRequestServiceBase() {
		super();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {
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
				documentId = null;
			}
			String documentTypeCode = null;
			try {
				documentTypeCode = queryForSingle("//*:ViewRequest/*:DocumentReferenceRequest/*:DocumentTypeCode", docInfo, config);
				logger.debug("Document type code:" + documentTypeCode);
			} catch (XPathException e) {
				logger.debug("Not able to find document type code");
				documentTypeCode = null;
			}
	
			StringBuffer response = new StringBuffer();
	
			Message mainDoc = null;
			List<Message> messagesReceived = messageService.retrieveMessages(message.getHeader().getSenderPartyId(), null, null, null, null, null,
					documentId, documentTypeCode, null, false, false, false);
			if (messagesReceived != null && messagesReceived.size() > 0) {
				logger.debug("Message found as sender");
				mainDoc = messagesReceived.get(0);
			} else {
				logger.debug("No message found as sender");
				List<Message> messagesSent = messageService.retrieveMessages(null, message.getHeader().getSenderPartyId(), null, null, null, null,
						documentId, documentTypeCode, null, false, false, false);
				if (messagesSent != null && messagesSent.size() > 0) {
					logger.debug("Message found as receiver");
					mainDoc = messagesSent.get(0);
				} else {
					logger.debug("No message found as sender");
				}
			}
			if (mainDoc == null) {
				response.append(NOT_FOUND);
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
							String startTag = "<ec:EmbeddedDocumentBinaryObject xmlns:ec='ec:services:wsdl:ViewRequest-0.1' mimeCode='"
									+ messageBinary.getMimeCode() + "'>";
							String endTag = "</ec:EmbeddedDocumentBinaryObject>";
							String encoded = new String(Base64.encode(messageBinary.getBinary()));
							response.append(startTag);
							response.append(encoded);
							response.append(endTag);
						}
						found = true;
						break;
					}
				}
				if (!found)
					response.append(NOT_FOUND);
			}
	
			logger.debug("Response:" + response.toString());
			responseMessage.setPayload(response.toString());
			logger.debug("Stop processMessage");
			return responseMessage;
		} catch (TechnicalErrorException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}

}