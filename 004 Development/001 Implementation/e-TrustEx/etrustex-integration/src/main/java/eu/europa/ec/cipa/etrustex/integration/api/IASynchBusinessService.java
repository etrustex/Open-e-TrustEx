package eu.europa.ec.cipa.etrustex.integration.api;

import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;

public interface IASynchBusinessService {
	/**
	 * This interface is to be implemented for business specific processing of a document it must  return the processed
	 * message. Implementations should return the message or throw an exception if exeception is thrown an application response
	 * will be created during the asynch processing 
	 * 	
	 * @param message
	 * @return
	 * @throws BusinessServiceException 
	 */
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException,TechnicalErrorException;
}
