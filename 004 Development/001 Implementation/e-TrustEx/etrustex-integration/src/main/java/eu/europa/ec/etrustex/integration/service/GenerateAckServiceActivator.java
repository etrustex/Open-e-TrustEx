package eu.europa.ec.etrustex.integration.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.integration.ack.GenerateAckService;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import freemarker.template.TemplateException;

public class GenerateAckServiceActivator extends TrustExServiceActivator {
	
	@Autowired
	private GenerateAckService generateAckService;
	
	@Transactional
	public Message<TrustExMessage<String>> processMessage(Message<TrustExMessage<String>> message) throws TechnicalErrorException {
		//generate ack
		Transaction transaction = transactionService.getTransaction(message.getPayload().getHeader().getTransactionTypeId());
		try {
			String ackPayload = generateAckService.generateAndSignAck(transaction, message.getPayload().getHeader());
				
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(ackPayload);
			responseMessage.setHeader(message.getPayload().getHeader());
			MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(responseMessage).copyHeaders(message.getHeaders());
			
			return builder.build();
		} catch (ClassNotFoundException | TemplateException | IOException e) {
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
		
	}

	

}
