package eu.europa.ec.etrustex.integration.api;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;

import eu.europa.ec.etrustex.integration.config.ChannelsConfig;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;

@MessagingGateway(name = "messageProcessingGateway", defaultReplyChannel=ChannelsConfig.MESSAGE_PROCESSING_GW_OUT)
public interface IMessageProcessingGateway {
	
	/**
	 * This method is used by client to trigger the processing of synchronouse messages (Inbox, status Request, delete docuemtn wrapper) by the etrust
	 * ex integration layer
	 * @param message
	 * @return
	 */
	@Gateway(requestChannel=ChannelsConfig.READ_IN, headers= @GatewayHeader(name = "replyTO", value="MSG_GATEWAY"))
	public TrustExMessage processSynchMessage(TrustExMessage<String> message);
	
	/**
	 * This method is used by client to trigger the processing of submit type messages (SubmitInvoice, SubmitBundle ... ) by the etrust
	 * ex integration layer
	 * @param message
	 * @return
	 */
	@Gateway(requestChannel=ChannelsConfig.SYNC_SUBMIT_IN, headers= @GatewayHeader(name = "replyTO", value="MSG_GATEWAY"))
	public TrustExMessage<String> processSubmitMessage(TrustExMessage<String> message);
	
	
	
}
