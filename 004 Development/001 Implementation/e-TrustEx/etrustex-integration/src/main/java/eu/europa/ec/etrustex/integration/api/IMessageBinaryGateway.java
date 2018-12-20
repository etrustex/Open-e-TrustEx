package eu.europa.ec.etrustex.integration.api;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;

@MessagingGateway(name = "messageBinaryGateway", defaultReplyChannel=ChannelsConfig.MESSAGE_BINARY_GW_OUT)
public interface IMessageBinaryGateway {
	
	/**
	 * This method is used by client to stream the large Binaries to the gateway
	 * @param the message binary
	 * @return the Id of the created message binary
	 */
	@Gateway(requestChannel=ChannelsConfig.MESSAGE_BINARY_GW_STORE_IN)
	public Long storeMessageBinary(TrustExMessageBinary largeBinary);
	
	/**
	 * This method is used by client to delete a message binary  
	 * @param message
	 * @return
	 */
	@Gateway(requestChannel=ChannelsConfig.MESSAGE_BINARY_GW_DELETE_IN)
	public Long deleteMessageBinary(Long largeBinaryId);

}
