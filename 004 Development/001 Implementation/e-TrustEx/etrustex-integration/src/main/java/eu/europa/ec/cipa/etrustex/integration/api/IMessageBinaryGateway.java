package eu.europa.ec.cipa.etrustex.integration.api;

import org.springframework.integration.annotation.Header;

import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;

public interface IMessageBinaryGateway {
	
	/**
	 * This method is used by client to stream the large Binaries to the gateway
	 * @param the message binary
	 * @return the Id of the created message binary
	 */
	public Long storeMessageBinary(TrustExMessageBinary largeBinary)  throws MessageProcessingException;
	
	/**
	 * This method is used by client to delete a message binary  
	 * @param message
	 * @return
	 */
	public Long deleteMessageBinary(Long largeBinaryId)  throws MessageProcessingException;

}
