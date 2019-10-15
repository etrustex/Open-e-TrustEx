package eu.europa.ec.etrustex.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;

public class MessageResponseCodeDAOTest extends AbstractEtrustExTest {

	@Autowired private IMessageResponseCodeDAO 	mrcDao;
	@Autowired private IProfileDAO 			   	profileDao;
	@Autowired private ITransactionDAO			transactionDao;
	@Autowired private IDocumentDAO 			documentDAO;
	@Autowired private IInterchangeAgreementDAO icaDao;
	
	
	@Test public void testGetTransactionMessageResponseCode(){
		Transaction t = new Transaction();
		t.setName("tx1");
		t = transactionDao.create(t);
		
		Transaction t2 = new Transaction();
		t2.setName("tx2");
		t2 = transactionDao.create(t2);
		
		List<MessageResponseCode> mrcList = mrcDao.getTransactionMessageResponseCode(t.getId());
		Assert.assertEquals(0, mrcList.size());
		
		mrcDao.create(buildMRC("utest-key1","utest-value1",null,t,null,null));
		mrcDao.create(buildMRC("utest-key2","utest-value2",null,t,null,null));
		
		mrcDao.create(buildMRC("utest-key3","utest-value3",null,t2,null,null));
		mrcDao.create(buildMRC("utest-key4","utest-value4",null,t2,null,null));
		mrcDao.create(buildMRC("utest-key5","utest-value5",null,t2,null,null));
		mrcDao.create(buildMRC("utest-key6","utest-value6",null,t2,null,null));
		
		mrcList = mrcDao.getTransactionMessageResponseCode(t.getId());
		Assert.assertEquals(2, mrcList.size());
		
		mrcList = mrcDao.getTransactionMessageResponseCode(t2.getId());
		Assert.assertEquals(4, mrcList.size());
	} 
	
	@Test public void testGetDocumentMessageResponseCode(){
		Document document1 = new Document();
		document1.setName("DOC_NAME_1");
		document1.setLocalName("DOC_LNAME_1");
		document1.setDocumentTypeCode("DOC_DTC_1");
		documentDAO.create(document1);
		
		Document document2 = new Document();
		document2.setName("DOC_NAME_2");
		document2.setLocalName("DOC_LNAME_2");
		document2.setDocumentTypeCode("DOC_DTC_2");
		documentDAO.create(document2);
		
		List<MessageResponseCode> mrcList = mrcDao.getDocumentMessageResponseCode(document1.getId());
		Assert.assertEquals(0, mrcList.size());
		
		mrcDao.create(buildMRC("utest-key1","utest-value1",null,null,document1,null));
		mrcDao.create(buildMRC("utest-key2","utest-value2",null,null,document1,null));
		
		mrcDao.create(buildMRC("utest-key3","utest-value3",null,null,document2,null));
		mrcDao.create(buildMRC("utest-key4","utest-value4",null,null,document2,null));
		mrcDao.create(buildMRC("utest-key5","utest-value5",null,null,document2,null));
		mrcDao.create(buildMRC("utest-key6","utest-value6",null,null,document2,null));
		
		mrcList = mrcDao.getDocumentMessageResponseCode(document1.getId());
		Assert.assertEquals(2, mrcList.size());
		
		mrcList = mrcDao.getDocumentMessageResponseCode(document2.getId());
		Assert.assertEquals(4, mrcList.size());
	} 
	
	@Test public void testGetInterchangeAgreementMessageResponseCode(){
		Profile profile1 = new Profile();
		profile1.setName("P"+UUID.randomUUID().toString());
		profileDao.create(profile1);
		
		InterchangeAgreement ica1 = new InterchangeAgreement();
		ica1.setProfile(profile1);
		icaDao.create(ica1);
		
		InterchangeAgreement ica2 = new InterchangeAgreement();
		ica2.setProfile(profile1);
		icaDao.create(ica2);
		
		List<MessageResponseCode> mrcList = mrcDao.getInterchangeAgreementMessageResponseCode(ica1.getId());
		Assert.assertEquals(0, mrcList.size());
		
		mrcDao.create(buildMRC("utest-key1","utest-value1",null,null,null,ica1));
		mrcDao.create(buildMRC("utest-key2","utest-value2",null,null,null,ica1));
		
		mrcDao.create(buildMRC("utest-key3","utest-value3",null,null,null,ica2));
		mrcDao.create(buildMRC("utest-key4","utest-value4",null,null,null,ica2));
		mrcDao.create(buildMRC("utest-key5","utest-value5",null,null,null,ica2));
		mrcDao.create(buildMRC("utest-key6","utest-value6",null,null,null,ica2));
		
		mrcList = mrcDao.getInterchangeAgreementMessageResponseCode(ica1.getId());
		Assert.assertEquals(2, mrcList.size());
		
		mrcList = mrcDao.getInterchangeAgreementMessageResponseCode(ica2.getId());
		Assert.assertEquals(4, mrcList.size());
	} 
	
	@Test public void testGetProfileMessageResponseCodeById(){
		Profile p1 = new Profile();
		p1.setName("P1");
		profileDao.create(p1);
		
		Profile p2 = new Profile();
		p2.setName("P2");
		profileDao.create(p2);
		
		mrcDao.create(buildMRC("utest-key1","utest-value1",p1,null,null,null));
		mrcDao.create(buildMRC("utest-key2","utest-value2",p1,null,null,null));
		
		mrcDao.create(buildMRC("utest-key3","utest-value3",p2,null,null,null));
		mrcDao.create(buildMRC("utest-key4","utest-value4",p2,null,null,null));
		mrcDao.create(buildMRC("utest-key5","utest-value5",p2,null,null,null));
		mrcDao.create(buildMRC("utest-key6","utest-value6",p2,null,null,null));
		
		List<MessageResponseCode> mrcList = mrcDao.getProfileMessageResponseCode(p1.getId());
		
		Assert.assertEquals(2, mrcList.size());
		
		mrcList = mrcDao.getProfileMessageResponseCode(p2.getId());
		
		Assert.assertEquals(4, mrcList.size());
	} 
	
	@Test public void testGetProfileMessageResponseCodeBySet(){
		Set<Profile> profiles = new HashSet<Profile>();
		
		Profile p1 = new Profile();
		p1.setName("P1");
		p1 = profileDao.create(p1);
		
		Profile p2 = new Profile();
		p2.setName("P2");
		p2 = profileDao.create(p2);
		
		mrcDao.create(buildMRC("utest-key1","utest-value1",p1,null,null,null));
		mrcDao.create(buildMRC("utest-key2","utest-value2",p1,null,null,null));
		
		mrcDao.create(buildMRC("utest-key3","utest-value3",p2,null,null,null));
		mrcDao.create(buildMRC("utest-key4","utest-value4",p2,null,null,null));
		mrcDao.create(buildMRC("utest-key5","utest-value5",p2,null,null,null));
		mrcDao.create(buildMRC("utest-key6","utest-value6",p2,null,null,null));
		
		profiles.add(p1);
		
		List<MessageResponseCode> mrcList = mrcDao.getProfileMessageResponseCode(profiles);
		Assert.assertEquals(2, mrcList.size());
		
		profiles.add(p2);
		mrcList = mrcDao.getProfileMessageResponseCode(profiles);
		
		Assert.assertEquals(6, mrcList.size());
	} 
	
	@Test public void testGetDefaultMessageResponseCode(){
		List<MessageResponseCode> mrcList = mrcDao.getDefaultMessageResponseCode();
		int initialSize = mrcList.size();
		
		mrcDao.create(buildMRC("utest-key","utest-value",null,null,null,null));
		
		mrcList = mrcDao.getDefaultMessageResponseCode();
		
		Assert.assertEquals("New value not returned in the default list", 1, mrcList.size() - initialSize);
	}
	
	private MessageResponseCode buildMRC(String key, String value, Profile p, Transaction t, Document d, InterchangeAgreement ica){
		MessageResponseCode mrc = new MessageResponseCode();
		mrc.setResponseCodeKey(key);
		mrc.setResponseCodeValue(value);
		
		if(p != null){
			mrc.setProfile(p);
		}
		
		if(d != null){
			mrc.setDocument(d);
		}
		
		if(t != null){
			mrc.setTansaction(t);
		}
		
		if(ica != null){
			mrc.setInterchangeAgreement(ica);
		}
		
		
		return mrc;
	}

}
