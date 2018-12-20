package eu.europa.ec.etrustex.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;


public class MetadataDAOTest extends AbstractEtrustExTest {
	
	@Autowired private IMetadataDAO 			metadataDAO;
	@Autowired private IBusinessDomainDAO		bdDao;
	@Autowired private IPartyDAO 				partyDao;
	@Autowired private IProfileDAO 				profileDao;
	@Autowired private ITransactionDAO			transactionDao;
	@Autowired private IDocumentDAO 			documentDAO;
	@Autowired private IInterchangeAgreementDAO icaDao;
	
	private MetaDataItem md1;
	
	@Before public void prepareMetadatas(){
		md1 = buildMetadata("TYPE_1", "VALUE_1", null, null, null, null, null, null);
		metadataDAO.create(md1);
	}
	
	@Test public void testDefaultMetadata(){
		List<MetaDataItem> defaultMD = metadataDAO.getDefaultMetadata();
		for (MetaDataItem metaDataItem : defaultMD) {
			if(metaDataItem.getDocument() != null || metaDataItem.getInterchangeAgreement() != null || metaDataItem.getProfile() != null || metaDataItem.getTansaction() != null || metaDataItem.getSender() != null || metaDataItem.getBusinessDomain() != null){
				Assert.fail("Retrieved non default property");
			}
		}
	}
	
	@Test public void testIsManaged(){
		Assert.assertEquals("Not Managed",false,metadataDAO.isManaged(new MetaDataItem()));		
		Assert.assertEquals("Managed",true,metadataDAO.isManaged(md1));	
	}
	
	@Test public void testGetMetaDataItemsByCriteria(){
		MetaDataItem criteria = null;
		List<MetaDataItem> mdList = null;
		
		metadataDAO.create(buildMetadata("ITEM_TYPE_1", "VALUE_1", null, null, null, null, null, null));
		criteria = new MetaDataItem();
		criteria.setRawItemType("ITEM_TYPE_1");
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals("ITEM_TYPE_1", mdList.get(0).getRawItemType());
		
		
		Document document1 = new Document();
		document1.setName("DOC_NAME_1");
		document1.setLocalName("DOC_LNAME_1");
		document1.setDocumentTypeCode("DOC_DTC_1");
		documentDAO.create(document1);
		metadataDAO.create(buildMetadata("ITEM_TYPE_2", "VALUE_2", null, document1, null, null, null, null));
		criteria = new MetaDataItem();
		criteria.setDocument(document1);
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals(document1.getId(), mdList.get(0).getDocument().getId());
		
		Profile p = new Profile();
		p.setName("P");
		profileDao.create(p);		
		InterchangeAgreement ica1 = new InterchangeAgreement();
		ica1.setProfile(p);
		icaDao.create(ica1);
		metadataDAO.create(buildMetadata("ITEM_TYPE_3", "VALUE_3", null, null, null, ica1, null, null));
		criteria = new MetaDataItem();
		criteria.setInterchangeAgreement(ica1);
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals(ica1.getId(), mdList.get(0).getInterchangeAgreement().getId());
				
		metadataDAO.create(buildMetadata("ITEM_TYPE_4", "VALUE_4", null, null, p, null, null, null));
		criteria = new MetaDataItem();
		criteria.setProfile(p);
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals(p.getId(), mdList.get(0).getProfile().getId());
		
		Transaction t1 = new Transaction();
		t1.setName("tx1");
		transactionDao.create(t1);
		metadataDAO.create(buildMetadata("ITEM_TYPE_5", "VALUE_5", t1, null, null, null, null, null));
		criteria = new MetaDataItem();
		criteria.setTansaction(t1);
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals(t1.getId(), mdList.get(0).getTansaction().getId());
		
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		metadataDAO.create(buildMetadata("ITEM_TYPE_6", "VALUE_6", null, null, null, null, party1, null));
		criteria = new MetaDataItem();
		criteria.setSender(party1);
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals(party1.getId(), mdList.get(0).getSender().getId());
		
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("bd-test1");
		bd1.setResponseSignatureRequired(false);
		bdDao.create(bd1);
		metadataDAO.create(buildMetadata("ITEM_TYPE_7", "VALUE_7", null, null, null, null, null, bd1));
		criteria = new MetaDataItem();
		criteria.setBusinessDomain(bd1);
		mdList = metadataDAO.getMetaDataItemsByCriteria(criteria);
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals(bd1.getId(), mdList.get(0).getBusinessDomain().getId());		
	} 
	
	@Test public void testGetMetadataResourceAsStream(){
		//TODO to review. if not commited the DS injected in the DAO cannot see the new Object. 
		
		MetaDataItem md = metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", null, null, null, null, null, null));
		
		InputStream iStream = null;
		
		try {
			iStream = metadataDAO.getMetadataResourceAsStream(md.getId());
			Assert.assertNull(iStream);
		//Assert.assertEquals("VALUE_1", convertStreamToString(iStream));
		}finally{
			if(iStream != null){
				try {
					iStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test public void testGetTransactionMetadata(){
		Transaction t1 = new Transaction();
		t1.setName("tx1");
		transactionDao.create(t1);
		
		Transaction t2 = new Transaction();
		t2.setName("tx2");
		transactionDao.create(t2);
		
		Transaction t3 = new Transaction();
		t3.setName("tx3");
		transactionDao.create(t3);
		
		metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", t1, null, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_2", "VALUE_2", t1, null, null, null, null, null));
		
		metadataDAO.create(buildMetadata("TYPE_3", "VALUE_3", t2, null, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_4", "VALUE_4", t2, null, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_5", "VALUE_5", t2, null, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_6", "VALUE_6", t2, null, null, null, null, null));
		
		List<MetaDataItem> mdList = null;
		
		mdList = metadataDAO.getTransactionMetadata(t1.getId());
		Assert.assertEquals(2, mdList.size());
		Assert.assertEquals(t1.getId(), mdList.get(0).getTansaction().getId());
		
		mdList = metadataDAO.getTransactionMetadata(t2.getId());
		Assert.assertEquals(4, mdList.size());
		Assert.assertEquals(t2.getId(), mdList.get(0).getTansaction().getId());
		
		mdList = metadataDAO.getTransactionMetadata(t3.getId());
		Assert.assertEquals(0, mdList.size());
	}
	
	@Test public void testGetDocumentMetadata(){
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
		
		Document document3 = new Document();
		document3.setName("DOC_NAME_3");
		document3.setLocalName("DOC_LNAME_3");
		document3.setDocumentTypeCode("DOC_DTC_3");
		documentDAO.create(document3);
		
		metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", null, document1, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_2", "VALUE_2", null, document1, null, null, null, null));
		
		metadataDAO.create(buildMetadata("TYPE_3", "VALUE_3", null, document2, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_4", "VALUE_4", null, document2, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_5", "VALUE_5", null, document2, null, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_6", "VALUE_6", null, document2, null, null, null, null));
		
		List<MetaDataItem> mdList = null;
		
		mdList = metadataDAO.getDocumentMetadata(document1.getId());
		Assert.assertEquals(2, mdList.size());
		Assert.assertEquals(document1.getId(), mdList.get(0).getDocument().getId());
		
		mdList = metadataDAO.getDocumentMetadata(document2.getId());
		Assert.assertEquals(4, mdList.size());
		Assert.assertEquals(document2.getId(), mdList.get(0).getDocument().getId());
	} 
	
	@Test public void testGetProfileMetadata(){
		Profile profile1 = new Profile();
		profile1.setName("P1");
		profileDao.create(profile1);
		
		Profile profile2 = new Profile();
		profile2.setName("P2");
		profileDao.create(profile2);
		
		Profile profile3 = new Profile();
		profile3.setName("P3");
		profileDao.create(profile3);
		
		metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", null, null, profile1, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_2", "VALUE_2", null, null, profile1, null, null, null));
		
		metadataDAO.create(buildMetadata("TYPE_3", "VALUE_3", null, null, profile2, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_4", "VALUE_4", null, null, profile2, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_5", "VALUE_5", null, null, profile2, null, null, null));
		metadataDAO.create(buildMetadata("TYPE_6", "VALUE_6", null, null, profile2, null, null, null));
		
		List<MetaDataItem> mdList = null;
		
		mdList = metadataDAO.getProfileMetadata(profile1.getId());
		Assert.assertEquals(2, mdList.size());
		Assert.assertEquals(profile1.getId(), mdList.get(0).getProfile().getId());
		
		mdList = metadataDAO.getProfileMetadata(profile2.getId());
		Assert.assertEquals(4, mdList.size());
		Assert.assertEquals(profile2.getId(), mdList.get(0).getProfile().getId());
		
		mdList = metadataDAO.getProfileMetadata(profile3.getId());
		Assert.assertEquals(0, mdList.size());
		
		mdList = metadataDAO.getProfileMetadata(new HashSet<Profile>(Arrays.asList(profile1, profile2)));
		Assert.assertEquals(6, mdList.size());
	} 
	
	@Test public void testGetInterchangeAgreementMetadata(){
		Profile p = new Profile();
		p.setName("P");
		profileDao.create(p);
		
		InterchangeAgreement ica1 = new InterchangeAgreement();
		ica1.setProfile(p);
		icaDao.create(ica1);
		
		InterchangeAgreement ica2 = new InterchangeAgreement();
		ica2.setProfile(p);
		icaDao.create(ica2);
		
		InterchangeAgreement ica3 = new InterchangeAgreement();
		ica3.setProfile(p);
		icaDao.create(ica3);
		
		metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", null, null, null, ica1, null, null));
		metadataDAO.create(buildMetadata("TYPE_2", "VALUE_2", null, null, null, ica1, null, null));
		
		metadataDAO.create(buildMetadata("TYPE_3", "VALUE_3", null, null, null, ica2, null, null));
		metadataDAO.create(buildMetadata("TYPE_4", "VALUE_4", null, null, null, ica2, null, null));
		metadataDAO.create(buildMetadata("TYPE_5", "VALUE_5", null, null, null, ica2, null, null));
		metadataDAO.create(buildMetadata("TYPE_6", "VALUE_6", null, null, null, ica2, null, null));
		
		List<MetaDataItem> mdList = null;
		
		mdList = metadataDAO.getInterchangeAgreementMetadata(ica1.getId());
		Assert.assertEquals(2, mdList.size());
		Assert.assertEquals(ica1.getId(), mdList.get(0).getInterchangeAgreement().getId());
		
		mdList = metadataDAO.getInterchangeAgreementMetadata(ica2.getId());
		Assert.assertEquals(4, mdList.size());
		Assert.assertEquals(ica2.getId(), mdList.get(0).getInterchangeAgreement().getId());
		
		mdList = metadataDAO.getInterchangeAgreementMetadata(ica3.getId());
		Assert.assertEquals(0, mdList.size());
	}  
	
	@Test public void testGetBusinessDomainMetadata(){
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("bd-test1");
		bd1.setResponseSignatureRequired(false);
		bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("bd-test2");
		bd2.setResponseSignatureRequired(false);
		bdDao.create(bd2);
		
		BusinessDomain bd3 = new BusinessDomain();
		bd3.setName("bd-test3");
		bd3.setResponseSignatureRequired(false);
		bdDao.create(bd3);
		
		metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", null, null, null, null, null, bd1));
		metadataDAO.create(buildMetadata("TYPE_2", "VALUE_2", null, null, null, null, null, bd1));
		
		metadataDAO.create(buildMetadata("TYPE_3", "VALUE_3", null, null, null, null, null, bd2));
		metadataDAO.create(buildMetadata("TYPE_4", "VALUE_4", null, null, null, null, null, bd2));
		metadataDAO.create(buildMetadata("TYPE_5", "VALUE_5", null, null, null, null, null, bd2));
		metadataDAO.create(buildMetadata("TYPE_6", "VALUE_6", null, null, null, null, null, bd2));
		
		List<MetaDataItem> mdList = null;
		
		mdList = metadataDAO.getBusinessDomainMetadata(bd1.getId());
		Assert.assertEquals(2, mdList.size());
		Assert.assertEquals(bd1.getId(), mdList.get(0).getBusinessDomain().getId());
		
		mdList = metadataDAO.getBusinessDomainMetadata(bd2.getId());
		Assert.assertEquals(4, mdList.size());
		Assert.assertEquals(bd2.getId(), mdList.get(0).getBusinessDomain().getId());
		
		mdList = metadataDAO.getBusinessDomainMetadata(bd3.getId());
		Assert.assertEquals(0, mdList.size());
	}
	
	@Test public void testGetDefaultMetadataByType(){
		List<MetaDataItem> mdList = metadataDAO.getDefaultMetadataByType("TYPE_1");
		Assert.assertEquals(1, mdList.size());
		Assert.assertEquals("VALUE_1", mdList.get(0).getValue());
		
		mdList = metadataDAO.getDefaultMetadataByType("DUMMY_TYPE");
		Assert.assertEquals(0, mdList.size());
	}
	
	@Test public void testGetPartyMetadata(){
		Party party1 = new Party();
		party1.setName("P1");
		partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		partyDao.create(party2);
		
		Party party3 = new Party();
		party3.setName("P3");
		partyDao.create(party3);
		
		metadataDAO.create(buildMetadata("TYPE_1", "VALUE_1", null, null, null, null, party1, null));
		metadataDAO.create(buildMetadata("TYPE_2", "VALUE_2", null, null, null, null, party1, null));
		
		metadataDAO.create(buildMetadata("TYPE_3", "VALUE_3", null, null, null, null, party2, null));
		metadataDAO.create(buildMetadata("TYPE_4", "VALUE_4", null, null, null, null, party2, null));
		metadataDAO.create(buildMetadata("TYPE_5", "VALUE_5", null, null, null, null, party2, null));
		metadataDAO.create(buildMetadata("TYPE_6", "VALUE_6", null, null, null, null, party2, null));
		
		List<MetaDataItem> mdList = null;
		
		mdList = metadataDAO.getPartyMetadata(party1.getId());
		Assert.assertEquals(2, mdList.size());
		Assert.assertEquals(party1.getId(), mdList.get(0).getSender().getId());
		
		mdList = metadataDAO.getPartyMetadata(party2.getId());
		Assert.assertEquals(4, mdList.size());
		Assert.assertEquals(party2.getId(), mdList.get(0).getSender().getId());
		
		mdList = metadataDAO.getPartyMetadata(party3.getId());
		Assert.assertEquals(0, mdList.size());
	} 

	private MetaDataItem buildMetadata(String itemType, String value, Transaction tx, Document doc, Profile pro, InterchangeAgreement ica, Party sender, BusinessDomain bd){
		MetaDataItem md = new MetaDataItem();
		md.setRawItemType(itemType);
		md.setValue(value);
		md.setTansaction(tx);
		md.setDocument(doc);
		md.setProfile(pro);
		md.setInterchangeAgreement(ica);
		md.setSender(sender);
		md.setBusinessDomain(bd);
		
		return md;
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
