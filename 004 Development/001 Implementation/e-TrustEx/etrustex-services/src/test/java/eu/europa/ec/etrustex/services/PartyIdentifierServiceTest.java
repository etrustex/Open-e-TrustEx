package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IPartyIdentifierDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

public class PartyIdentifierServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IPartyIdentifierService partyIdentifierService; 
	@Mock private IPartyIdentifierDAO partyIdentifierDAO;
	
	@Before public void testInit(){
		//Injection doesn't work with transactional service through @InjectMocks so we have to do it through reflection 
		MockitoAnnotations.initMocks(this);		
		ReflectionTestUtils.setField(partyIdentifierService, "partyIdentifierDAO", partyIdentifierDAO);
	}
	
	@Test public void testExistsPartyIdentifierInBusinessDomain(){
		Long bdID = 1L;
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setId(bdID);
		
		Party p = new Party();
		p.setBusinessDomain(businessDomain);
		
		PartyIdentifier pi = new PartyIdentifier();
		pi.setValue(" ");
		pi.setParty(p);
		
		when(partyIdentifierDAO.getBySchemeAndValue(any(PartyIdentifier.class))).thenReturn(null);
		assertFalse(partyIdentifierService.existsPartyIdentifierInBusinessDomain(null, bdID));
		assertFalse(partyIdentifierService.existsPartyIdentifierInBusinessDomain(pi, bdID));
		
		when(partyIdentifierDAO.getBySchemeAndValue(any(PartyIdentifier.class))).thenReturn(new ArrayList<PartyIdentifier>(Arrays.asList(pi)));
		assertTrue(partyIdentifierService.existsPartyIdentifierInBusinessDomain(pi, bdID));
		assertFalse(partyIdentifierService.existsPartyIdentifierInBusinessDomain(pi, 2L));
		
		pi.setParty(null);
		assertFalse(partyIdentifierService.existsPartyIdentifierInBusinessDomain(pi, bdID));
	}
	
	@Test public void testGetPartyIdentifier(){
		Long partyIdentifierId = 1L;
		partyIdentifierService.getPartyIdentifier(partyIdentifierId);
		verify(partyIdentifierDAO, times(1)).read(partyIdentifierId); 
	}
	
	@Test public void testDeletePartyIdentifier(){
		Long partyIdentifierId = 1L;
		partyIdentifierService.deletePartyIdentifier(partyIdentifierId);
		verify(partyIdentifierDAO, times(1)).delete(partyIdentifierId);
	}
	
	@Test public void testGetBySchemeAndValueInBusinessDomain(){
		assertNull(partyIdentifierService.getBySchemeAndValueInBusinessDomain(null, null));
		
		PartyIdentifier pi = new PartyIdentifier();
		pi.setValue("X");
		
		when(partyIdentifierDAO.getBySchemeAndValue(any(PartyIdentifier.class))).thenReturn(null);
		assertNull(partyIdentifierService.getBySchemeAndValueInBusinessDomain(pi, null));
		
		when(partyIdentifierDAO.getBySchemeAndValue(any(PartyIdentifier.class))).thenReturn(new ArrayList<PartyIdentifier>(Arrays.asList(pi)));
		assertNull(partyIdentifierService.getBySchemeAndValueInBusinessDomain(pi, null));
		
		Long bdID = 1L;
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setId(bdID);
		
		Party p = new Party();
		p.setBusinessDomain(businessDomain);
		pi.setParty(p);
		
		assertEquals(pi, partyIdentifierService.getBySchemeAndValueInBusinessDomain(pi, bdID));
	}
}
