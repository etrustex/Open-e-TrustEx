package eu.europa.ec.etrustex.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

public class BusinessDomainServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IBusinessDomainService businessDomainService;
	@Mock private IBusinessDomainDAO businessDomainDAO;
	@Mock private IAuthorisationService authorisationService;
	
	@Before public void init() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(businessDomainService, "businessDomainDAO", businessDomainDAO);
		ReflectionTestUtils.setField(businessDomainService, "authorisationService", authorisationService);
	}
	
	@Test public void testGetBusinessDomain(){
		Long bdID = 1l;
		
		BusinessDomain bd = new BusinessDomain();
		bd.getProfiles().add(new Profile());
		
		when(businessDomainDAO.read(bdID)).thenReturn(bd);
		businessDomainService.getBusinessDomain(bdID);
		verify(businessDomainDAO,times(1)).read(bdID);
	}
	
	@Test public void testGetAllBusinessDomains(){
		businessDomainService.getAllBusinessDomains();
		verify(businessDomainDAO,times(1)).getAll();
	}
	
	@Test public void testRetrieveBusinessDomain(){
		String authenticatedUser = "usr";
		Party party = new Party();
		party.setBusinessDomain(new BusinessDomain());
		try {
			when(authorisationService.getMessageIssuer(authenticatedUser)).thenReturn(new Party());
		} catch (UndefinedIdentifierException e) {}		
		assertNull(businessDomainService.retrieveBusinessDomain(authenticatedUser));
		
		try {
			when(authorisationService.getMessageIssuer(authenticatedUser)).thenReturn(party);
		} catch (UndefinedIdentifierException e) {}
		assertNotNull(businessDomainService.retrieveBusinessDomain(authenticatedUser));
	}
	
	@Test public void testFindByName(){
		String name = "N";
		businessDomainService.findByName(name);
		verify(businessDomainDAO, times(1)).findByName(name);
	}
}
