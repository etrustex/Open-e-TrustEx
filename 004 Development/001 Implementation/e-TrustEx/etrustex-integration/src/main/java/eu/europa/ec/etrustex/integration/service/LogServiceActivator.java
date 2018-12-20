/**
 * 
 */
package eu.europa.ec.etrustex.integration.service;

import org.springframework.messaging.Message;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;

/**
 * @author chiricr
 *
 */
public class LogServiceActivator extends TrustExServiceActivator {
	
	private boolean returnMessage;
		
	/**
	 * logs the successful processing of a message
	 * @param message
	 * @return
	 */
	public Message<TrustExMessage<String>> logSuccessfulProcessing(Message<TrustExMessage<String>> message) {
		String description = "Message processing completed successfully";
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.SUCCESS, LogDTO.LOG_OPERATION.OTHER, 
				description, this.getClass().getName());			
							
		logService.saveLog(logDTO);
		
		//chiricr: the synch processing chain should receive the message as reply on the output channel, 
		//the asych processing chain has no output or reply channel, so it should receive null
		return returnMessage ? message : null;
	}

	public void setReturnMessage(boolean returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	

}
