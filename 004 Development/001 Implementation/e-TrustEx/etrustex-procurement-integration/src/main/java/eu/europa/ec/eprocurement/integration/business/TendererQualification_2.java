package eu.europa.ec.eprocurement.integration.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;

@Component("SubmitTendererQualificationResponse-2.0")
public class TendererQualification_2 implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(TendererQualification_2.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		

		
		
		
		return message;
	}
}
