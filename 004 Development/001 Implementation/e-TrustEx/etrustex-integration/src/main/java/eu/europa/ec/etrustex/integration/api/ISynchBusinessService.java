package eu.europa.ec.etrustex.integration.api;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;

public interface ISynchBusinessService {
	
	public TrustExMessage processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException;

}
