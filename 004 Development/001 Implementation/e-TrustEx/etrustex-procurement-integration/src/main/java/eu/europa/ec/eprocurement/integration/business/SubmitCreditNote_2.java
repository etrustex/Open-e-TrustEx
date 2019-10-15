package eu.europa.ec.eprocurement.integration.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;

@Component("SubmitCreditNote-2.0")
public class SubmitCreditNote_2 extends SubmitCreditNoteBase implements IASynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(SubmitCreditNote_2.class);

}