/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.service;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.stereotype.Component;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IDocumentService;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

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
		if (StringUtils.isBlank(messageDocId) || messageDocId.length() > 250){
			throw new MessageProcessingException("soapenv:client", ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null, 
					ErrorResponseCode.INVALID_MESSAGE_ID, null, null);
		}
		
		CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();		
		// we check for special characters 
		if (!asciiEncoder.canEncode(messageDocId)){
			throw new MessageProcessingException(  "soapenv:client",ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null, 
					ErrorResponseCode.INVALID_MESSAGE_ID, null, null);
		}
		
		return message;
	}


	public void setReadService(boolean readService) {
		this.readService = readService;
	}
	
}
