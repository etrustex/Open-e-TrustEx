package eu.europa.ec.cipa.etrustex.integration.api;

import java.io.InputStream;

import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;

public interface IMessageProcessingGateway {
	/**
	 * This method is used by client to trigger the processing of synchronouse messages (Inbox, status Request, delete docuemtn wrapper) by the etrust
	 * ex integration layer
	 * @param message
	 * @return
	 */
	public TrustExMessage processSynchMessage(TrustExMessage<String> message) throws MessageProcessingException;
	/**
	 * This method is used by client to trigger the processing of submit type messages (SubmitInvoice, SubmitBundle ... ) by the etrust
	 * ex integration layer
	 * @param message
	 * @return
	 */
	public TrustExMessage<String> processSubmitMessage(TrustExMessage<String> message)  throws MessageProcessingException;
	
	
	
}
