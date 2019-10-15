/**
 * 
 */
package eu.europa.ec.etrustex.integration.service;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.lang.StringUtils;
import org.springframework.messaging.Message;

import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;

/**
 * @author chiricr
 *
 */
public class MessageIdValidatorServiceActivator extends TrustExServiceActivator {
	
	private boolean readService;
	private static final String STORE_DOC_WRAPPER_REQUEST_LOCAL_NAME = "StoreDocumentWrapperRequest";
	private static final String DELETE_DOC_WRAPPER_REQUEST_LOCAL_NAME = "DeleteDocumentWrapperRequestRequest";
	
	
	public Message<TrustExMessage<String>> validateMessageId(Message<TrustExMessage<String>> message) {
		
		if (readService 
				&& !STORE_DOC_WRAPPER_REQUEST_LOCAL_NAME.equals(message.getPayload().getHeader().getTransactionRequestLocalName())
				&& !DELETE_DOC_WRAPPER_REQUEST_LOCAL_NAME.equals(message.getPayload().getHeader().getTransactionRequestLocalName())) {
			//validate message id for store and delete wrapper
			return message;
		}
		
		String messageDocId = message.getPayload().getHeader().getMessageDocumentId();
		// we check for the ID length
		if (StringUtils.isBlank(messageDocId)) {
			throw new MessageProcessingException("soapenv:client", ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null, 
					ErrorResponseCode.INVALID_MESSAGE_ID, null, soapErrorMessages.getProperty("error.hardrule.docId"));
		}
		
		if (messageDocId.length() > 250) {
			throw new MessageProcessingException("soapenv:client", ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null, 
					ErrorResponseCode.INVALID_MESSAGE_ID, null, soapErrorMessages.getProperty("error.hardrule.docId.length"));			
		}
		
		CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();		
		// we check for special characters 
		if (!asciiEncoder.canEncode(messageDocId)){
			throw new MessageProcessingException(  "soapenv:client",ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null, 
					ErrorResponseCode.INVALID_MESSAGE_ID, null, soapErrorMessages.getProperty("error.hardrule.docId.ASCII"));
		}
		
		return message;
	}


	public void setReadService(boolean readService) {
		this.readService = readService;
	}
	
}
