package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.types.EventNotificationType;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

public class EventNotificationServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IEventNotificationService eventNotificationService;
	@Mock 	   private IPartyDAO partyDAO;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(eventNotificationService, "partyDAO", partyDAO);
	}
	
	@Test public void testGetCreatePartyEventNotification(){
		Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	
    	EventType ev = eventNotificationService.getCreatePartyEventNotification(party);
    	assertNotNull(ev.getCreatePartyEvent());
	}
	
    @Test public void testGetUpdatePartyEventNotification(){
    	Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	
    	EventType ev = eventNotificationService.getUpdatePartyEventNotification(party, party);
    	assertNotNull(ev.getUpdatePartyEvent());
    }       
    
    @Test public void testGetDeletePartyEventNotification(){
    	Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	
    	EventType ev = eventNotificationService.getDeletePartyEventNotification(party);
    	assertNotNull(ev.getDeletePartyEvent());
    }    
    
    @Test public void testGetCreateInterchangeAgreementyEventNotification(){
    	InterchangeAgreement ica = new InterchangeAgreement();
    	
    	Profile p = new Profile();
    	p.setName("name");
    	ica.setProfile(p);
    	
    	CIALevel cia = new CIALevel();
    	cia.setAvailabilityLevel(1);
    	cia.setConfidentialityLevel(2);
    	cia.setIntegrityLevel(3);
    	ica.setCiaLevel(cia);
    	
    	PartyRole pr = new PartyRole();
    	Role role = new Role();
    	role.setCode("code");
    	pr.setRole(role);
    	Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	pr.setParty(party);
    	ica.addPartyRole(pr);
    	
    	EventType ev = eventNotificationService.getCreateInterchangeAgreementyEventNotification(ica);
    	assertNotNull(ev.getCreateInterchangeAgreementEvent());
    }
    
    @Test public void testGetUpdateInterchangeAgreementyEventNotification(){
    	InterchangeAgreement ica = new InterchangeAgreement();
    	
    	Profile p = new Profile();
    	p.setName("name");
    	ica.setProfile(p);
    	
    	CIALevel cia = new CIALevel();
    	cia.setAvailabilityLevel(1);
    	cia.setConfidentialityLevel(2);
    	cia.setIntegrityLevel(3);
    	ica.setCiaLevel(cia);
    	
    	PartyRole pr = new PartyRole();
    	Role role = new Role();
    	role.setCode("code");
    	pr.setRole(role);
    	Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	pr.setParty(party);
    	ica.addPartyRole(pr);
    	
    	EventType ev = eventNotificationService.getUpdateInterchangeAgreementyEventNotification(ica, ica);
    	assertNotNull(ev.getUpdateInterchangeAgreementEvent());
    }
   
    @Test public void testGetDeleteInterchangeAgreementyEventNotification(){
    	InterchangeAgreement ica = new InterchangeAgreement();
    	
    	Profile p = new Profile();
    	p.setName("name");
    	ica.setProfile(p);
    	
    	CIALevel cia = new CIALevel();
    	cia.setAvailabilityLevel(1);
    	cia.setConfidentialityLevel(2);
    	cia.setIntegrityLevel(3);
    	ica.setCiaLevel(cia);
    	
    	PartyRole pr = new PartyRole();
    	Role role = new Role();
    	role.setCode("code");
    	pr.setRole(role);
    	Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	pr.setParty(party);
    	ica.addPartyRole(pr);
    	
    	EventType ev = eventNotificationService.getDeleteInterchangeAgreementyEventNotification(ica);
    	assertNotNull(ev.getDeleteInterchangeAgreementEvent());
    }
    
    @Test public void testGetDeleteDocumentEventNotification(){
    	Message dbMessage = new Message();
    	dbMessage.setDocumentId("ID");
    	dbMessage.setMessageDocumentTypeCode("DTC");
    	
    	Party party = new Party();
    	party.setName("name");
    	party.setNaturalPersonFlag(true);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setValue("value");
    	pi.setSchemeId(IdentifierIssuingAgency.AD_VAT);
    	party.addIdentifier(pi);
    	
    	dbMessage.setSender(party);
    	dbMessage.setReceiver(party);
    	
    	EventType ev = eventNotificationService.getDeleteDocumentEventNotification(dbMessage);
    	assertNotNull(ev.getDeleteDocumentEvent());
    }
    
    @Test public void testGetSenderId(){
    	Party senderParty = new Party();
    	senderParty.setId(1L);
    	PartyIdentifier pi = new PartyIdentifier();
    	pi.setId(3L);
    	senderParty.addIdentifier(pi);
    	
    	Party receiverParty = new Party();
    	receiverParty.setId(2L);
    	
    	when(partyDAO.getEventPublisherParty(receiverParty)).thenReturn(null);
    	try {
    		eventNotificationService.getSenderId(receiverParty);
    		fail("Should throw an exception");
    	}catch (Exception e){}
    	
    	when(partyDAO.getEventPublisherParty(receiverParty)).thenReturn(senderParty);
    	assertEquals(pi.getId(), eventNotificationService.getSenderId(receiverParty).getId());
    }
    
    @Test public void testGetPartiesConfiguredToReceiveEventNotification(){
    	EventNotificationType eventType = EventNotificationType.CFG;
    	Long businessDomainId = 1L;
    	Long profileId = 2L;
    	
    	eventNotificationService.getPartiesConfiguredToReceiveEventNotification(eventType, businessDomainId, profileId);
    	verify(partyDAO, times(1)).getPartiesConfiguredToReceiveEventNotification(eventType, businessDomainId, profileId); 
    }
}
