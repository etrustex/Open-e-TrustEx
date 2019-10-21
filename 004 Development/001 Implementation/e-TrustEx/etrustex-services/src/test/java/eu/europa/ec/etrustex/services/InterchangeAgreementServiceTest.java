package eu.europa.ec.etrustex.services;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.ICIALevelDAO;
import eu.europa.ec.etrustex.dao.IInterchangeAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyRoleDao;
import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.services.impl.InterchangeAgreementService;

public class InterchangeAgreementServiceTest {

	@InjectMocks private InterchangeAgreementService interchangeService;
	@Mock private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Mock private IPartyRoleDao partyRoleDAO;
	@Mock private IPartyAgreementDAO partyAgreementDAO;
	@Mock private ICIALevelDAO ciaLevelDAO;

	@Before public void setup() {
		MockitoAnnotations.initMocks(this);
		
		ReflectionTestUtils.setField(interchangeService, "interchangeAgreementDAO", interchangeAgreementDAO);
		ReflectionTestUtils.setField(interchangeService, "partyRoleDAO", partyRoleDAO);
		ReflectionTestUtils.setField(interchangeService, "partyAgreementDAO", partyAgreementDAO);
		ReflectionTestUtils.setField(interchangeService, "ciaLevelDAO", ciaLevelDAO);
	}
	
	@Test public void testGetInterchangeAgreements(){
		PartyRole a = new PartyRole();
		PartyRole b = new PartyRole();
		Profile p = new Profile();
		CIALevel l = new CIALevel();
		interchangeService.getInterchangeAgreements(a, b, p, l);
		verify(interchangeAgreementDAO, times(1)).getInterchangeAgreements(a, b, p, l);	
	}
	
	@Test public void testgetInterchangeAgreements2(){
		Party requester = new Party();
		requester.setId(5L);
		Set<Party> senders = new HashSet<Party>();
		Set<Party> receivers = new HashSet<Party>();
		List<String> documentTypeIds = new ArrayList<String>();
		
		interchangeService.getInterchangeAgreements(requester, (Set<Party>)null, (Set<Party>)null, documentTypeIds);
		verify(interchangeAgreementDAO, times(2)).getInterchangeAgreements(same(documentTypeIds), any(Set.class), any(Set.class));
		
		senders.clear();
		receivers.clear();
		receivers.add(new Party());
		interchangeService.getInterchangeAgreements(requester, senders, receivers, documentTypeIds);
		verify(interchangeAgreementDAO, times(3)).getInterchangeAgreements(same(documentTypeIds), any(Set.class), any(Set.class));
		
		senders.clear();
		receivers.clear();
		senders.add(new Party());
		interchangeService.getInterchangeAgreements(requester, senders, receivers, documentTypeIds);
		verify(interchangeAgreementDAO, times(4)).getInterchangeAgreements(same(documentTypeIds), any(Set.class), any(Set.class));
	}

	@Test public void testGetInterchangeAgreements3(){
		Party requester = new Party();
		requester.setId(1L);
		Party party1 = new Party();
		party1.setId(2L);
		Party party2  = new Party();
		party2.setId(3L);
		List<String> documentTypeIds = new ArrayList<String>();
		
		when(interchangeAgreementDAO.getInterchangeAgreements(any(List.class), any(Set.class), any(Set.class))).thenReturn(Arrays.asList(new InterchangeAgreement()));
		
		assertTrue(interchangeService.getInterchangeAgreements(requester, (Party)null, (Party)null, documentTypeIds).isEmpty());
		verify(interchangeAgreementDAO, times(0)).getInterchangeAgreements(any(List.class), any(Set.class), any(Set.class));
		
		assertTrue(!interchangeService.getInterchangeAgreements(requester, requester, party2, documentTypeIds).isEmpty());
		verify(interchangeAgreementDAO, times(1)).getInterchangeAgreements(any(List.class), any(Set.class), any(Set.class));
		
		//assertTrue(interchangeService.getInterchangeAgreements(requester, party1, party2, documentTypeIds).isEmpty());
	}
	
	@Test public void testgetInterchangeArgreement(){}
	
	@Test public void testCreateInterchangeAgreement(){
		InterchangeAgreement ica = new InterchangeAgreement();
		interchangeService.createInterchangeAgreement(ica);
		verify(interchangeAgreementDAO, times(1)).create(ica);
	}
	
	@Test public void testCreateInterchangeAgreementDelegation(){
		InterchangeAgreement ica = new InterchangeAgreement();
		
		PartyAgreement firstDelegation = new PartyAgreement(); 
		PartyAgreement secondDelegation = new PartyAgreement();
		
		CIALevel cia = new CIALevel();
		cia.setAvailabilityLevel(0);
		cia.setConfidentialityLevel(0);
		cia.setIntegrityLevel(0);
		ica.setCiaLevel(cia);
		
		PartyRole partyRole1 = new PartyRole();
		PartyRole partyRole2 = new PartyRole();
		Party p1 = new Party();
		p1.setId(1L);
		Party p2 = new Party();
		p2.setId(2L);
		Role r1 = new Role();
		r1.setId(1L);
		Role r2 = new Role();
		r2.setId(2L);
		partyRole1.setParty(p1);
		partyRole1.setRole(r1);
		partyRole2.setParty(p2);
		partyRole2.setRole(r2);
		ica.getPartyRoles().add(partyRole1);
		ica.getPartyRoles().add(partyRole2);
		
		//Happy Flow
		//--Party Agreements created
		//--Existing CIA level
		//--Existing PartyRoles
		when(partyRoleDAO.getPartyRoles(partyRole1.getParty().getId(), partyRole1.getRole().getId())).thenReturn(Arrays.asList(partyRole1));
		when(partyRoleDAO.getPartyRoles(partyRole2.getParty().getId(), partyRole2.getRole().getId())).thenReturn(Arrays.asList(partyRole2));
		when(ciaLevelDAO.retrieveCIALevel(0,0,0)).thenReturn(Arrays.asList(cia));
		when(partyAgreementDAO.existsPartyAgreement(any(Party.class), any(Party.class))).thenReturn(false);		
		interchangeService.createInterchangeAgreement(ica, firstDelegation, secondDelegation);
		verify(partyAgreementDAO, times(2)).create(any(PartyAgreement.class));
		verify(interchangeAgreementDAO, times(1)).create(ica);
		
		//--Party Agreements existing
		//--Creating a new CIA
		//--New PartyRoles
		ica.setCiaLevel(null);
		when(partyRoleDAO.getPartyRoles(partyRole1.getParty().getId(), partyRole1.getRole().getId())).thenReturn(new ArrayList<PartyRole>());
		when(partyRoleDAO.getPartyRoles(partyRole2.getParty().getId(), partyRole2.getRole().getId())).thenReturn(new ArrayList<PartyRole>());
		when(ciaLevelDAO.retrieveCIALevel(0,0,0)).thenReturn(new ArrayList<CIALevel>());
		when(partyAgreementDAO.existsPartyAgreement(any(Party.class), any(Party.class))).thenReturn(true);		
		interchangeService.createInterchangeAgreement(ica, firstDelegation, secondDelegation);
		verify(partyAgreementDAO, times(2)).create(any(PartyAgreement.class));
		verify(interchangeAgreementDAO, times(2)).create(ica);
	}
	
	@Test public void testUpdateInterchangeAgreement(){
		InterchangeAgreement ica = new InterchangeAgreement();
		interchangeService.updateInterchangeAgreement(ica);
		verify(interchangeAgreementDAO, times(1)).update(ica);
	}
	
	@Test public void testUpdateInterchangeAgreement2(){
		InterchangeAgreement ica = new InterchangeAgreement();
		PartyRole partyRole1 = new PartyRole();
		PartyRole partyRole2 = new PartyRole();
		Party p1 = new Party();
		p1.setId(1L);
		Party p2 = new Party();
		p2.setId(2L);
		Role r1 = new Role();
		r1.setId(1L);
		Role r2 = new Role();
		r2.setId(2L);
		partyRole1.setParty(p1);
		partyRole1.setRole(r1);
		partyRole2.setParty(p2);
		partyRole2.setRole(r2);
		ica.getPartyRoles().add(partyRole1);
		ica.getPartyRoles().add(partyRole2);
		Profile p = new Profile();
		p.setCiaLevel(new CIALevel());
		ica.setProfile(p);
		PartyAgreement firstDelegation = new PartyAgreement();
		PartyAgreement secondDelegation = new PartyAgreement();
		interchangeService.updateInterchangeAgreement(ica, firstDelegation, secondDelegation);
		verify(interchangeAgreementDAO, times(1)).update(any(InterchangeAgreement.class));
	}
	
	@Test public void testDeleteInterchangeAgreement(){
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setId(1L);
		when(interchangeAgreementDAO.isUsed(ica.getId())).thenReturn(false);
		interchangeService.deleteInterchangeAgreement(ica);
		verify(interchangeAgreementDAO, times(1)).delete(ica);
		
		when(interchangeAgreementDAO.isUsed(ica.getId())).thenReturn(true);
		try{
			interchangeService.deleteInterchangeAgreement(ica);
			fail("Should not proceed");
		} catch (RecordInUseException e) {};
	}
    
	@Test public void testfindInterchangeAgreementsByCriteria(){}
	
    @Test public void testgetInterchangeAgreementsByProfile(){
    	interchangeService.getInterchangeAgreementsByProfile(1L);
		verify(interchangeAgreementDAO, times(1)).getInterchangeAgreementsByProfile(1L);
    }
	
    @Test public void testgetPartiesInInterchangeAgreementFor(){
    	InterchangeAgreement ica = new InterchangeAgreement();
		PartyRole partyRole1 = new PartyRole();
		PartyRole partyRole2 = new PartyRole();
		Party p1 = new Party();
		p1.setId(1L);
		Party p2 = new Party();
		p2.setId(2L);
		Role r1 = new Role();
		r1.setId(1L);
		Role r2 = new Role();
		r2.setId(2L);
		partyRole1.setParty(p1);
		partyRole1.setRole(r1);
		partyRole2.setParty(p2);
		partyRole2.setRole(r2);
		ica.getPartyRoles().add(partyRole1);
		ica.getPartyRoles().add(partyRole2);
    	
    	when(interchangeAgreementDAO.getInterchangeAgreements(3L, 2L, 1L, 4L)).thenReturn(Arrays.asList(ica));
    	assertEquals(2, interchangeService.getPartiesInInterchangeAgreementFor(1L, 2L, 3L, 4L).size());
    	
    }
	@Test public void testdeleteInterchangeAgreementById(){}
	
	@Test public void testIsUnique(){
		interchangeService.isUnique(1l, 2l, 3l, 4l, 5l);
		verify(interchangeAgreementDAO, times(1)).isUniqueBy(1l, 2l, 3l, 4l, 5l);
	}
	
	@Test public void testcontainsParty(){}
	
	@Test public void testExistICAwithSameRoleForParties(){
		Role role = new Role();
		interchangeService.existICAwithSameRoleForParties(role);
		verify(interchangeAgreementDAO, times(1)).existICAwithSameRoleForParties(role);
	}
	
	@Test public void testIsUsed(){
		Long id = 1L;
		interchangeService.isUsed(id);
		verify(interchangeAgreementDAO, times(1)).isUsed(id);
	}
    
	@Test public void testFindByParty(){
		Party party = new Party();
		interchangeService.findByParty(party);
		verify(interchangeAgreementDAO, times(1)).findByParty(party);
	}
    
	@Test public void testGetPartiesInExistingAgreement(){
		Party party1 = new Party();
		party1.setId(1L);
		Party party2 = new Party();
		party2.setId(2L);
		when(interchangeAgreementDAO.getPartiesInExistingAgreement(1L,2L,3L,4L)).thenReturn(Arrays.asList(party1, party2));
		assertEquals(2, interchangeService.getPartiesInExistingAgreement(1L,2L,3L,4L).size());
	}
	
}
