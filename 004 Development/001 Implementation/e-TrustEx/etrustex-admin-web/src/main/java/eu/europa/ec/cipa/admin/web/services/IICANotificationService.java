package eu.europa.ec.cipa.admin.web.services;

import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;

public interface IICANotificationService {
	InterchangeAgreement createAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception;
	InterchangeAgreement updateAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception;
	boolean deleteAndSendNotification(Long icaId) throws Exception;

}
