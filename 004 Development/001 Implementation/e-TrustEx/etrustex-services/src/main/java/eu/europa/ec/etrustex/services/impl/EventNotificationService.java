package eu.europa.ec.etrustex.services.impl;

import ec.schema.xsd.commonaggregatecomponents_2_1.*;
import ec.schema.xsd.commonbasiccomponents_1.AvailabilityLevelCodeType;
import ec.schema.xsd.commonbasiccomponents_1.ConfidentialityLevelCodeType;
import ec.schema.xsd.commonbasiccomponents_1.IntegrityLevelCodeType;
import ec.schema.xsd.deletedocument_1.DeleteDocumentType;
import ec.schema.xsd.eventnotification_1.*;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.services.IEventNotificationService;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PersonType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;

/**
 * Created by guerrpa on 18/09/2015.
 */
@Service
public class EventNotificationService implements IEventNotificationService {

    @Autowired
    private IPartyDAO partyDAO;

    @Override
    public EventType getCreatePartyEventNotification(Party party) {
        EventType eventType = new EventType();
        CreatePartyEventType createPartyEventType = new CreatePartyEventType();

        PartyType partyType = getPartyType(party);

        createPartyEventType.setParty(partyType);
        eventType.setCreatePartyEvent(createPartyEventType);

        return eventType;
    }

    @Override
    public EventType getUpdatePartyEventNotification(Party fromParty, Party toParty) {
        EventType eventType = new EventType();
        UpdatePartyEventType updatePartyEventType = new UpdatePartyEventType();

        FromPartyType fromPartyType = new FromPartyType();
        fromPartyType.setParty(getPartyType(fromParty));

        ToPartyType toPartyType = new ToPartyType();
        toPartyType.setParty(getPartyType(toParty));

        updatePartyEventType.setFromParty(fromPartyType);
        updatePartyEventType.setToParty(toPartyType);
        eventType.setUpdatePartyEvent(updatePartyEventType);

        return eventType;
    }

    @Override
    public EventType getDeletePartyEventNotification(Party party) {
        DeletePartyEventType deletePartyEventType = new DeletePartyEventType();
        EventType eventType = new EventType();

        PartyType partyType = getPartyType(party);

        deletePartyEventType.setParty(partyType);
        eventType.setDeletePartyEvent(deletePartyEventType);

        return eventType;
    }

    @Override
    public EventType getCreateInterchangeAgreementyEventNotification(InterchangeAgreement ica) {
        CreateInterchangeAgreementEventType createInterchangeAgreementEventType = new CreateInterchangeAgreementEventType();
        EventType eventType = new EventType();

        InterchangeAgreementType interchangeAgreementType = getInterchangeAgreementType(ica);

        createInterchangeAgreementEventType.setInterchangeAgreement(interchangeAgreementType);
        eventType.setCreateInterchangeAgreementEvent(createInterchangeAgreementEventType);

        return eventType;
    }

    @Override
    public EventType getUpdateInterchangeAgreementyEventNotification(InterchangeAgreement fromIca, InterchangeAgreement toIca) {
        UpdateInterchangeAgreementEventType updateInterchangeAgreementEventType = new UpdateInterchangeAgreementEventType();
        EventType eventType = new EventType();

        InterchangeAgreementType interchangeAgreementTypeFrom = getInterchangeAgreementType(fromIca);
        InterchangeAgreementType interchangeAgreementTypeTo = getInterchangeAgreementType(toIca);

        updateInterchangeAgreementEventType.setFromInterchangeAgreement(interchangeAgreementTypeFrom);
        updateInterchangeAgreementEventType.setToInterchangeAgreement(interchangeAgreementTypeTo);
        eventType.setUpdateInterchangeAgreementEvent(updateInterchangeAgreementEventType);

        return eventType;
    }

    @Override
    public EventType getDeleteInterchangeAgreementyEventNotification(InterchangeAgreement ica) {
        EventType eventType = new EventType();
        DeleteInterchangeAgreementEventType deleteInterchangeAgreementEventType = new DeleteInterchangeAgreementEventType();
        deleteInterchangeAgreementEventType.setInterchangeAgreement(getInterchangeAgreementType(ica));

        eventType.setDeleteInterchangeAgreementEvent(deleteInterchangeAgreementEventType);
        return eventType;
    }

    @Override
    public EventType getDeleteDocumentEventNotification(Message dbMessage) {
        EventType eventType = new EventType();
        DeleteDocumentEventType deleteDocumentEventType = new DeleteDocumentEventType();
        DeleteDocumentType deleteDocumentType = new DeleteDocumentType();
        ec.schema.xsd.commonaggregatecomponents_2.DocumentReceiverParty documentReceiverParty = new ec.schema.xsd.commonaggregatecomponents_2.DocumentReceiverParty();
        ec.schema.xsd.commonaggregatecomponents_2.DocumentSenderParty documentSenderParty = new ec.schema.xsd.commonaggregatecomponents_2.DocumentSenderParty();
        EndpointIDType senderEndpointIDType = new EndpointIDType();
        EndpointIDType receiverEndpointIDType = new EndpointIDType();
        IDType idType = new IDType();
        IDType idDocumentReferenceType = new IDType();
        DocumentReferenceType documentReferenceType = new DocumentReferenceType();
        DocumentTypeCodeType documentTypeCodeType = new DocumentTypeCodeType();

        String wrapperId = UUID.randomUUID().toString();
        idType.setValue(wrapperId);
        deleteDocumentType.setID(idType);

        senderEndpointIDType.setValue(dbMessage.getSender().getIdentifiers().iterator().next().getValue());
        documentSenderParty.setEndpointID(senderEndpointIDType);
        deleteDocumentType.setDocumentSenderParty(documentSenderParty);

        if(dbMessage.getReceiver() != null && CollectionUtils.isNotEmpty(dbMessage.getReceiver().getIdentifiers())) {
            receiverEndpointIDType.setValue(dbMessage.getReceiver().getIdentifiers().iterator().next().getValue());
            documentReceiverParty.setEndpointID(receiverEndpointIDType);
        }

        deleteDocumentType.setDocumentReceiverParty(documentReceiverParty);
        documentTypeCodeType.setValue(dbMessage.getMessageDocumentTypeCode());
        idDocumentReferenceType.setValue(dbMessage.getDocumentId());
        documentReferenceType.setID(idDocumentReferenceType);
        documentReferenceType.setDocumentTypeCode(documentTypeCodeType);
        deleteDocumentType.setDocumentReference(documentReferenceType);
        deleteDocumentEventType.setDeleteDocument(deleteDocumentType);
        eventType.setDeleteDocumentEvent(deleteDocumentEventType);

        return eventType;
    }

    @Override
    @Transactional(readOnly = true)
    public PartyIdentifier getSenderId(Party receiverParty) {
        Party senderParty = partyDAO.getEventPublisherParty(receiverParty);
        if (senderParty == null) {
            throw new RuntimeException("No publisher found for subscriber " + receiverParty.getName());
        }
        return senderParty.getIdentifiers().iterator().next();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Party> getPartiesConfiguredToReceiveEventNotification(eu.europa.ec.etrustex.types.EventNotificationType eventType, Long businessDomainId, Long profileId) {
        return partyDAO.getPartiesConfiguredToReceiveEventNotification(eventType, businessDomainId, profileId);
    }

    private PartyType getPartyType(Party party) {
        PartyType partyType = new PartyType();
        NameType nameType = new NameType();
        PartyNameType partyNameType = new PartyNameType();
        // PartyName
        nameType.setValue(party.getName());
        partyNameType.setName(nameType);
        partyType.getPartyName().add(partyNameType);

        // Party identifiers
        Set<PartyIdentifier> identifiers = new HashSet<>(party.getIdentifiers()); // avoid duplicates
        for(PartyIdentifier pi : identifiers) {
            PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
            IDType idT = new IDType();
            idT.setSchemeID(pi.getSchemeId().getSchemeID());
            idT.setValue(pi.getValue());
            partyIdentificationType.setID(idT);
            partyType.getPartyIdentification().add(partyIdentificationType);
        }
        
        //party natural person
        if (BooleanUtils.isTrue(party.getNaturalPersonFlag())) {
        	PersonType personType = new PersonType();
        	partyType.setPerson(personType);
        }
        return partyType;
    }


    private InterchangeAgreementType getInterchangeAgreementType(InterchangeAgreement interchangeAgreement) {
        InterchangeAgreementType interchangeAgreementType = new InterchangeAgreementType();

        // Profile
        ProfileIDType profileIDType = new ProfileIDType();
        profileIDType.setValue(interchangeAgreement.getProfile().getName());
        interchangeAgreementType.setProfileID(profileIDType);


        // Security information
        SecurityInformationType securityInformationType = new SecurityInformationType();
        AvailabilityLevelCodeType availabilityLevelCodeType = new AvailabilityLevelCodeType();
        ConfidentialityLevelCodeType confidentialityLevelCodeType = new ConfidentialityLevelCodeType();
        IntegrityLevelCodeType integrityLevelCodeType = new IntegrityLevelCodeType();

        availabilityLevelCodeType.setValue(String.valueOf(interchangeAgreement.getCiaLevel().getAvailabilityLevel()));
        confidentialityLevelCodeType.setValue(String.valueOf(interchangeAgreement.getCiaLevel().getConfidentialityLevel()));
        integrityLevelCodeType.setValue(String.valueOf(interchangeAgreement.getCiaLevel().getIntegrityLevel()));

        securityInformationType.setAvailabilityLevelCode(availabilityLevelCodeType);
        securityInformationType.setConfidentialityLevelCode(confidentialityLevelCodeType);
        securityInformationType.setIntegrityLevelCode(integrityLevelCodeType);

        interchangeAgreementType.setSecurityInformation(securityInformationType);

        //parties and roles
        for(PartyRole pr : interchangeAgreement.getPartyRoles()) {
            PartyType partyType = getPartyType(pr.getParty());
                        
            RoleCodeType roleCodeType = new RoleCodeType();
            roleCodeType.setValue(pr.getRole().getCode());
            
            PartyRoleType partyRoleType = new PartyRoleType();
            partyRoleType.setParty(partyType);
            partyRoleType.setRoleCode(roleCodeType);

            interchangeAgreementType.getPartyRole().add(partyRoleType);
        }

        return interchangeAgreementType;
    }
}
