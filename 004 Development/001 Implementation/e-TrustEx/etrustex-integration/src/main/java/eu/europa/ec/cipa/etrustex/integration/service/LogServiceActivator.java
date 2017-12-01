/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.service;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;

import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;

/**
 * @author chiricr
 *
 */
public class LogServiceActivator extends TrustExServiceActivator {
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private LogServiceHelper logServiceHelper;
	
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
