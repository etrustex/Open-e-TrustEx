package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IDocumentDAO;
import eu.europa.ec.etrustex.dao.IInterchangeAgreementDAO;
import eu.europa.ec.etrustex.dao.IMessageResponseCodeDAO;
import eu.europa.ec.etrustex.dao.IMetadataDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.IProfileDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.mock.CacheBase;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public class MetadataServiceTest extends AbstractEtrustExTest{
		
	@Autowired private IMetadataService metadataService;
	@Mock private IMetadataDAO metadataDAO;
	@Mock private IMessageResponseCodeDAO messageResponseCodeDAO;
	@Mock private CacheManager cacheManager;
	@Mock private IDocumentDAO documentDAO;
    @Mock private IPartyDAO partyDAO;
    @Mock private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Mock private IProfileDAO profileDAO;
	@Mock private ITransactionDAO transactionDAO;
	
	@Mock
    private Cache cache;
	private CacheBase<String, Object> cacheBase;
	
	
	@Before public void testinit(){
		//Injection doesn't work with transactional service through @InjectMocks so we have to do it through reflection 
		MockitoAnnotations.initMocks(this);
		
		ReflectionTestUtils.setField(metadataService, "metadataDAO", metadataDAO);
		ReflectionTestUtils.setField(metadataService, "transactionDAO", transactionDAO);
		ReflectionTestUtils.setField(metadataService, "profileDAO", profileDAO);
		ReflectionTestUtils.setField(metadataService, "interchangeAgreementDAO", interchangeAgreementDAO);
		ReflectionTestUtils.setField(metadataService, "partyDAO", partyDAO);
		ReflectionTestUtils.setField(metadataService, "documentDAO", documentDAO);
		ReflectionTestUtils.setField(metadataService, "cacheManager", cacheManager);
		ReflectionTestUtils.setField(metadataService, "messageResponseCodeDAO", messageResponseCodeDAO);
	}
	
	
	@Test public void testRetrieveMetaData(){
		Long agrId = 1l;
		Long traID = 2l;
		Long documentTypeID = 3l;
		Long profileID = 4l;
		Long partyId = 5l;
		
		when(metadataDAO.getDefaultMetadata()).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.ACK_GENERATOR_CLASS,""))));		
		when(metadataDAO.getProfileMetadata(profileID)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.ACK_SIGNATURE_CLASS,""))));
		when(metadataDAO.getTransactionMetadata(traID)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.AVAILABLE_NOTIFICATION_XPATH,""))));
		when(metadataDAO.getDocumentMetadata(documentTypeID)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.BUSINESS_SERVICE_BEAN,""))));
		when(metadataDAO.getInterchangeAgreementMetadata(agrId)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.BUSINESS_HEADER_XPATH,""))));
		when(metadataDAO.getPartyMetadata(partyId)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.BUSINESS_SCOPE_II_XPATH,""))));
		
		Map<MetaDataItemType, MetaDataItem> hm = metadataService.retrieveMetaData(agrId, traID, documentTypeID, profileID, partyId);
		assertEquals(6, hm.size());
		
		verify(metadataDAO, times(1)).getDefaultMetadata();
		verify(metadataDAO, times(1)).getInterchangeAgreementMetadata(agrId);
		verify(metadataDAO, times(1)).getTransactionMetadata(traID);
		verify(metadataDAO, times(1)).getDocumentMetadata(documentTypeID);
		verify(metadataDAO, times(1)).getProfileMetadata(profileID);
		verify(metadataDAO, times(1)).getPartyMetadata(partyId);
		
		hm = metadataService.retrieveMetaData(null, null, null, null, null);
		assertEquals(1, hm.size());
	}
	
	@Test public void testRetrieveMetaData2(){
		Long bdID = 1L;
		Long traID = 2l;
		Long documentTypeID = 3l;
		Long profileID = 4l;
		
		BusinessDomain bd = new BusinessDomain();
		bd.setId(bdID);
		
		Transaction tx = new Transaction();
		tx.setId(traID);
		
		Document doc = new Document();
		doc.setId(documentTypeID);
		tx.setDocument(doc);
		
		Profile pro = new Profile();
		pro.setId(profileID);
		tx.getProfiles().add(pro);
		
		when(metadataDAO.getBusinessDomainMetadata(bdID)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.SUPPORT_MULTICAST,""))));
		when(metadataDAO.getDefaultMetadata()).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.ACK_GENERATOR_CLASS,""))));		
		when(metadataDAO.getProfileMetadata(tx.getProfiles())).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.ACK_SIGNATURE_CLASS,""))));
		when(metadataDAO.getTransactionMetadata(traID)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.AVAILABLE_NOTIFICATION_XPATH,""))));
		when(metadataDAO.getDocumentMetadata(documentTypeID)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(buildMetadata(MetaDataItemType.BUSINESS_SERVICE_BEAN,""))));
		
		Map<MetaDataItemType, MetaDataItem> metadataMap = metadataService.retrieveMetaData(null, bd);
		assertEquals(0, metadataMap.size());
		
		metadataMap = metadataService.retrieveMetaData(tx, null);
		assertEquals(4, metadataMap.size());
		
		metadataMap = metadataService.retrieveMetaData(tx, bd);
		assertEquals(5, metadataMap.size());
		
		verify(metadataDAO, times(2)).getDefaultMetadata();
		verify(metadataDAO, times(2)).getTransactionMetadata(traID);
		verify(metadataDAO, times(2)).getDocumentMetadata(documentTypeID);
		verify(metadataDAO, times(2)).getProfileMetadata(tx.getProfiles());
		verify(metadataDAO, times(1)).getBusinessDomainMetadata(bdID);
	}
	
	@Test public void testRetrieveResponseCodeValues(){
		Long agrId = 1l;
		Long traID = 2l;
		Long documentTypeID = 3l;
		Long profileID = 4l;
		
		when(messageResponseCodeDAO.getDefaultMessageResponseCode()).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key1",""))));		
		when(messageResponseCodeDAO.getProfileMessageResponseCode(profileID)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key2",""))));
		when(messageResponseCodeDAO.getTransactionMessageResponseCode(traID)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key3",""))));
		when(messageResponseCodeDAO.getDocumentMessageResponseCode(documentTypeID)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key4",""))));
		when(messageResponseCodeDAO.getInterchangeAgreementMessageResponseCode(agrId)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key5",""))));
		
		Map<String, String> rc = metadataService.retrieveResponseCodeValues(agrId, traID, documentTypeID, profileID);
		assertEquals(5, rc.size());
		
		verify(messageResponseCodeDAO, times(1)).getDefaultMessageResponseCode();
		verify(messageResponseCodeDAO, times(1)).getProfileMessageResponseCode(profileID);
		verify(messageResponseCodeDAO, times(1)).getTransactionMessageResponseCode(traID);
		verify(messageResponseCodeDAO, times(1)).getDocumentMessageResponseCode(documentTypeID);
		verify(messageResponseCodeDAO, times(1)).getInterchangeAgreementMessageResponseCode(agrId);
		
		rc = metadataService.retrieveResponseCodeValues(null, null, null, null);
		assertEquals(1, rc.size());
	}
		
	@Test public void testRetrieveMessageResponseCodes(){
		Long agrId = 1l;
		Long traID = 2l;
		Long documentTypeID = 3l;
		Long profileID = 4l;
		
		Profile p = new Profile();
		p.setId(profileID);
		Set<Profile> pros = new HashSet<Profile>(Arrays.asList(p));
		
		when(messageResponseCodeDAO.getDefaultMessageResponseCode()).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key1",""))));		
		when(messageResponseCodeDAO.getProfileMessageResponseCode(pros)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key2",""))));
		when(messageResponseCodeDAO.getTransactionMessageResponseCode(traID)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key3",""))));
		when(messageResponseCodeDAO.getDocumentMessageResponseCode(documentTypeID)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key4",""))));
		when(messageResponseCodeDAO.getInterchangeAgreementMessageResponseCode(agrId)).thenReturn(new ArrayList<MessageResponseCode>(Arrays.asList(buildMessageResponseCode("key5",""))));
		
		Map<String, MessageResponseCode> map = metadataService.retrieveMessageResponseCodes(agrId, traID, documentTypeID, pros);
		assertEquals(5, map.size());
		
		verify(messageResponseCodeDAO, times(1)).getDefaultMessageResponseCode();
		verify(messageResponseCodeDAO, times(1)).getProfileMessageResponseCode(pros);
		verify(messageResponseCodeDAO, times(1)).getTransactionMessageResponseCode(traID);
		verify(messageResponseCodeDAO, times(1)).getDocumentMessageResponseCode(documentTypeID);
		verify(messageResponseCodeDAO, times(1)).getInterchangeAgreementMessageResponseCode(agrId);
	}
	
	@Test public void create(){
		MetaDataItem metadataItem = new MetaDataItem();
		
		Long partyId = 1L;
		Long traID = 2l;
		Long documentTypeID = 3l;
		Long profileID = 4l;
		Long icaID = 5l;
		
		Transaction tx = new Transaction();
		tx.setId(traID);
		metadataItem.setTansaction(tx);
		
		Document doc = new Document();
		doc.setId(documentTypeID);
		metadataItem.setDocument(doc);
		
		Profile pro = new Profile();
		pro.setId(profileID);
		metadataItem.setProfile(pro);
		
		Party party = new Party();
		party.setId(partyId);
		metadataItem.setSender(party);
		
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setId(icaID);
		metadataItem.setInterchangeAgreement(ica);

		metadataService.create(new MetaDataItem());
		verify(metadataDAO, times(1)).create(any(MetaDataItem.class));
		
		metadataService.create(metadataItem);
		verify(metadataDAO, times(2)).create(any(MetaDataItem.class));
		verify(documentDAO, times(1)).read(any(Long.class));
		verify(interchangeAgreementDAO, times(1)).read(any(Long.class));
		verify(partyDAO, times(1)).read(any(Long.class));
		verify(profileDAO, times(1)).read(any(Long.class));
		verify(transactionDAO, times(1)).read(any(Long.class));	
	}
	
	@Test public void getMetadataResourceAsStream(){
		metadataService.getMetadataResourceAsStream(1L);
		verify(metadataDAO, times(1)).getMetadataResourceAsStream(1L);
	}
	
	@Test public void clearMetaDataCache(){
		cacheBase = new CacheBase<>("metadataItemCache");
	    CacheBase.setCacheManager(cacheManager);
		
		when(cacheManager.getCache("metadataItemCache")).thenReturn(cache);
		metadataService.clearMetaDataCache();
		verify(cacheManager, times(1)).getCache("metadataItemCache");
		verify(cache, times(1)).clear();
	}
	
	@Test public void getDefaultMetadataByType(){
		metadataService.getDefaultMetadataByType("T");
		verify(metadataDAO, times(1)).getDefaultMetadataByType("T");
	}
	
	@Test public void getMetaDataItemsByCriteria(){
		MetaDataItem mdi = new MetaDataItem();
		when(metadataDAO.getMetaDataItemsByCriteria(mdi)).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(new MetaDataItem())));
		metadataService.getMetaDataItemsByCriteria(mdi);
		verify(metadataDAO, times(1)).getMetaDataItemsByCriteria(mdi);
	}
	
	@Test public void getMetadata(){
		Long metadataItemId = 1L;
		when(metadataDAO.read(metadataItemId)).thenReturn(new MetaDataItem());
		metadataService.getMetadata(metadataItemId);
		verify(metadataDAO, times(1)).read(metadataItemId);
	}
	
	@Test public void update(){
		MetaDataItem metadataItem = new MetaDataItem();
		metadataService.update(metadataItem);
		verify(metadataDAO, times(1)).update(metadataItem);
	}
	
	@Test public void delete(){
		MetaDataItem metadataItem = new MetaDataItem();
		metadataService.delete(metadataItem);
		verify(metadataDAO, times(1)).delete(any(MetaDataItem.class));
	}
	
	private MetaDataItem buildMetadata(MetaDataItemType type, String value){
		MetaDataItem md = new MetaDataItem();
		md.setValue(value);
		md.setRawItemType(type.toString());	
		return md;
	}
	
	private MessageResponseCode buildMessageResponseCode(String key, String value){
		MessageResponseCode rc = new MessageResponseCode();
		rc.setResponseCodeValue(value);
		rc.setResponseCodeKey(key);	
		return rc;
	}
	 
}
