package eu.europa.ec.cipa.etrustex.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.ack.GenerateAckService;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

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
		} catch (Exception e) {
			e.printStackTrace();
			throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
		}
		
	}

	

}
