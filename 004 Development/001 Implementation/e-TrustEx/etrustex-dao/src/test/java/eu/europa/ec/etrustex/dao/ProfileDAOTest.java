package eu.europa.ec.etrustex.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;

public class ProfileDAOTest extends AbstractEtrustExTest {

	@Autowired private IProfileDAO profileDao;
	@Autowired private IBusinessDomainDAO bdDao;
	@Autowired private IInterchangeAgreementDAO icaDao;
	@Autowired private IMetadataDAO mdDao;
	@Autowired private IEndpointDAO epDao;
	@Autowired private IMessageResponseCodeDAO mrcDao;
	@Autowired private ITransactionDAO txDao;
	
	@Test public void testCreateWithoutBD(){
		Profile p = new Profile();
		p.setName("P1");
		
		p = profileDao.create(p);
		Assert.assertNotNull(p.getId());
		
		Assert.assertNotNull(profileDao.getProfile(p.getId()).getId());
	}
	
	@Test public void testCreateWitBD(){
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("BD1");
		bd1.setResponseSignatureRequired(true);
		bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("BD2");
		bd2.setResponseSignatureRequired(true);
		bdDao.create(bd2);
		
		Profile p1 = new Profile();
		p1.setName("P1");
		p1.getBusinessDomains().add(bd1);
		p1.getBusinessDomains().add(bd2);
		
		p1 = profileDao.create(p1);
		
		Assert.assertNotNull(profileDao.read(p1.getId()));
		
		Assert.assertEquals(2, profileDao.read(p1.getId()).getBusinessDomains().size());
	}
	
	@Test public void testUpdate(){
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("BD1");
		bd1.setResponseSignatureRequired(true);
		bd1 = bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("BD2");
		bd2.setResponseSignatureRequired(true);
		bd2 = bdDao.create(bd2);
		
		BusinessDomain bd3 = new BusinessDomain();
		bd3.setName("BD3");
		bd3.setResponseSignatureRequired(true);
		bd3 = bdDao.create(bd3);
		
		Profile p1 = new Profile();
		p1.setName("P1");
		p1.getBusinessDomains().add(bd1);
		p1.getBusinessDomains().add(bd3);
		
		p1 = profileDao.create(p1);
		
		Assert.assertNotNull(p1.getId());
		Assert.assertEquals(2, p1.getBusinessDomains().size());
		Assert.assertEquals(1, bdDao.read(bd1.getId()).getProfiles().size());
		Assert.assertEquals(1, bdDao.read(bd3.getId()).getProfiles().size());
		Assert.assertEquals(0, bdDao.read(bd2.getId()).getProfiles().size());
		
		flush();
		
		p1.getBusinessDomains().clear();
		p1.getBusinessDomains().add(bd2);

		p1 = profileDao.update(p1);
		
		flush();
		
		Assert.assertEquals(1, p1.getBusinessDomains().size());
		Assert.assertEquals("BD2", profileDao.read(p1.getId()).getBusinessDomains().toArray(new BusinessDomain[1])[0].getName());	
	}
	
	@Test public void testFindByName(){
		Profile p = new Profile();
		p.setName("P1");
		
		p = profileDao.create(p);
		Assert.assertNotNull(profileDao.findByName("P1"));
		Assert.assertNotNull(profileDao.findByName("p1"));
		Assert.assertNull(profileDao.findByName("P2"));
	}
	
	@Test public void testIsInUse(){		
		Profile profileFree = new Profile();
		profileFree.setName("P1");
		profileFree = profileDao.create(profileFree);
		
		InterchangeAgreement ica = new InterchangeAgreement();
		Profile profileICA = new Profile();
		profileICA.setName("P2");
		profileICA = profileDao.create(profileICA);
		ica.setProfile(profileICA);
		icaDao.create(ica);
		
		MetaDataItem md1 = new MetaDataItem();
		md1.setRawItemType("TYPE_1");
		md1.setValue("VALUE_1");
		Profile profileMD = new Profile();
		profileMD.setName("P3");
		profileMD = profileDao.create(profileMD);
		md1.setProfile(profileMD);
		mdDao.create(md1);
		
		JMSEndpoint ep = new JMSEndpoint();
		ep.setDestinationJndiName("D");
		ep.setIsSupportingReplyTo(false);
		ep.setIsActive(true);
		Profile profileEP = new Profile();
		profileEP.setName("P4");
		profileEP = profileDao.create(profileEP);
		ep.setProfile(profileEP);
		epDao.create(ep);
		
		MessageResponseCode mrc = new MessageResponseCode();
		mrc.setResponseCodeKey("k");
		mrc.setResponseCodeValue("V");
		Profile profileMRC = new Profile();
		profileMRC.setName("P5");
		profileMRC = profileDao.create(profileMRC);
		mrc.setProfile(profileMRC);
		mrcDao.create(mrc);
		
		Assert.assertFalse(profileDao.isInUse(profileFree));
		Assert.assertTrue(profileDao.isInUse(profileICA));
		Assert.assertTrue(profileDao.isInUse(profileMD));
		Assert.assertTrue(profileDao.isInUse(profileEP));
		Assert.assertTrue(profileDao.isInUse(profileMRC));		
	}
	
	@Test public void testFindProfilesByCriteria(){
		Profile p1 = new Profile();
		p1.setName("noCriteriaProfile");
		p1 = profileDao.create(p1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("BD2");
		bd2.setResponseSignatureRequired(true);
		bd2 = bdDao.create(bd2);
		Profile p2 = new Profile();
		p2.setName("profileWithBusinessDomain");
		p2.getBusinessDomains().add(bd2);
		p2 = profileDao.create(p2);
		
		Transaction tx = new Transaction();
		tx.setName("ImATransaction");
		txDao.create(tx);
		Profile p3 = new Profile();
		p3.setName("profileWithTransaction");
		p3.getTransactions().add(tx);
		p3 = profileDao.create(p3);
		
		Assert.assertEquals(p1.getId(), profileDao.findProfilesByCriteria("noCriteriaProfile", null, null).get(0).getId());
		Assert.assertEquals("profileWithBusinessDomain", profileDao.findProfilesByCriteria(null, null, bd2).get(0).getName());
		Assert.assertEquals(p3.getId(), profileDao.findProfilesByCriteria("profileWithTransaction", "ImATransaction", null).get(0).getId());
	}
}
