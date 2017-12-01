package eu.europa.ec.cipa.etrustex.integration.api;

import javax.xml.bind.JAXBElement;

import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;

public interface ISynchBusinessService {
	
	public TrustExMessage processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException;

}
