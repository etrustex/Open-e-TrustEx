package eu.europa.ec.etrustex.admin.old.web.services;

import javax.xml.bind.JAXBException;

import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.PartyAgreement;

public interface IICANotificationService {
    InterchangeAgreement createAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception;

    InterchangeAgreement updateAndSendNotification(InterchangeAgreement ica, PartyAgreement firstDelegation, PartyAgreement secondDelegation) throws Exception;

    boolean deleteAndSendNotification(Long icaId) throws RecordInUseException, JAXBException;

}
