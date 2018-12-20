package eu.europa.ec.etrustex.dao;

import static org.junit.Assert.fail;

import javax.persistence.PersistenceException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.util.AssertionErrors;
import org.springframework.util.Assert;

import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

public class BusinessDomainDAOTest extends AbstractEtrustExTest {

	public static final String BD_NAME = "TEST_BUSINESS_DOMAIN";
	public static final String BD_NAME_2 = "TEST_BUSINESS_DOMAIN_2";
	
	@Autowired
	private IBusinessDomainDAO businessDomainDAO;

	@Test
	public void testCreate(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);		
		Assert.notNull(businessDomain.getId(),"The ID has not been generated");		
		AssertionErrors.assertEquals("It doesn't correspon to the created object", BD_NAME, businessDomain.getName());
		AssertionErrors.assertEquals("Should be empty", 0, businessDomain.getProfiles().size());
		AssertionErrors.assertEquals("Should be empty", 0, businessDomain.getParty().size());
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testCreateWithoutName(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain.setName(null);
		businessDomain = businessDomainDAO.create(businessDomain);
		flush();
	}
	
	@Test(expected=PersistenceException.class)
	public void testCreateNonUniqueName(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain.setName(BD_NAME);
		businessDomainDAO.create(businessDomain);		
		BusinessDomain businessDomain2 = new BusinessDomain();
		businessDomain2.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain2.setName(BD_NAME);				
		businessDomain = businessDomainDAO.create(businessDomain2);
		flush();
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testCreateWithoutASignature(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setResponseSignatureRequired(null);
		businessDomain.setName(BD_NAME);		
		businessDomain = businessDomainDAO.create(businessDomain);
		flush();
		fail( "Should not Persist a BD without a Signature" );
	}
	
	@Test
	public void testDelete(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		Long id = businessDomain.getId();
		businessDomainDAO.delete(businessDomain);
		BusinessDomain bd2 = businessDomainDAO.read(id);		
		Assert.isNull(bd2, "Object should have been deleted");
	}
	
	@Test
	public void testRead(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);
		BusinessDomain businessDomain2 = businessDomainDAO.read(businessDomain.getId());		
		Assert.notNull(businessDomain2, "Object should have been created");		
	}
	
	@Test
	public void testFindByName(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);		
		
		BusinessDomain businessDomain2 = null;
		try {
			businessDomain2 = businessDomainDAO.findByName(BD_NAME_2);
			fail( "My method didn't throw an Exception when I expected it to" );
		} catch (EmptyResultDataAccessException expectedException) {}				
		Assert.isNull(businessDomain2, "New Object not present in the DB yet");
		
		businessDomain2 = businessDomainDAO.findByName(BD_NAME);		
		Assert.notNull(businessDomain2, "Object should be present");
	}
	
	@Test
	public void testUpdate(){
		BusinessDomain businessDomain = new BusinessDomain();
		businessDomain.setName(BD_NAME);
		businessDomain.setResponseSignatureRequired(Boolean.TRUE);
		businessDomain = businessDomainDAO.create(businessDomain);				
		BusinessDomain businessDomain2 = null;
		try {
			businessDomain2 = businessDomainDAO.findByName(BD_NAME_2);
			fail( "My method didn't throw an Exception when I expected it to" );
		} catch (EmptyResultDataAccessException expectedException) {}				
		Assert.isNull(businessDomain2, "New Object not present in the DB yet");
		
		businessDomain.setName(BD_NAME_2);
		businessDomainDAO.update(businessDomain);
		
		businessDomain2 = businessDomainDAO.findByName(BD_NAME_2);		
		Assert.notNull(businessDomain2, "Object should be present");
	}
	

}
