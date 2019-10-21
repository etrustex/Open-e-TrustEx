package eu.europa.ec.etrustex.integration.transformers;

import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import eu.europa.ec.etrustex.integration.message.TrustExMessage;

public class MessageToMailTransformer {
	
	@Transformer
	public Message<String> transformIncomingMessage(Message<TrustExMessage<String>> incoming){
		//Message<String> mailMessage = null;
		
		MessageBuilder<String> builder = MessageBuilder.withPayload(incoming.getPayload().getPayload()).copyHeaders(incoming.getHeaders());
		return builder.build();
	}

}
