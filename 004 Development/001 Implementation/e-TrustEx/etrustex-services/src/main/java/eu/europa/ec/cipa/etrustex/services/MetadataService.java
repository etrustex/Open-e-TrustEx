package eu.europa.ec.cipa.etrustex.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.services.dao.IDocumentDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IInterchangeAgreementDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IMessageResponseCodeDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IMetadataDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IProfileDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ITransactionDAO;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class MetadataService implements IMetadataService {
	
	@Autowired
	private IMetadataDAO metadataDAO;
	@Autowired
	private IMessageResponseCodeDAO messageResponseCodeDAO;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private IDocumentDAO documentDAO;
	@Autowired
	private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Autowired
	private IProfileDAO profileDAO;
	@Autowired
	private ITransactionDAO transactionDAO;
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	@Cacheable(value="metadataItemCache")
	public Map<MetaDataItemType, MetaDataItem> retrieveMetaData(
			InterchangeAgreement agr, Transaction transaction, Document document, Set<Profile> profiles) {
		Map<MetaDataItemType, MetaDataItem> metadataMap = new HashMap<MetaDataItemType, MetaDataItem>();
	
		List<MetaDataItem> retrievedItems= null; 
		
		// the multiple for loops are necessary to override metadata the most specific overriding the most generic.
		retrievedItems = metadataDAO.getDefaultMetadata();
		for (MetaDataItem metaDataItem : retrievedItems) {
			metadataMap.put(metaDataItem.getItemType(), metaDataItem);
		}
		if(profiles != null && profiles.size() != 0){
			retrievedItems = metadataDAO.getProfileMetadata(profiles);
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(transaction != null){
			retrievedItems = metadataDAO.getTransactionMetadata(transaction.getId());
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(document != null){
			retrievedItems = metadataDAO.getDocumentMetadata(document.getId());
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(agr != null){
			retrievedItems = metadataDAO.getInterchangeAgreementMetadata(agr.getId());
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		return metadataMap;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	@Cacheable(value="metadataItemCache")
	public Map<MetaDataItemType, MetaDataItem> retrieveMetaData(
			Long agrId, Long traID, Long documentTypeID, Long profileID) {
		Map<MetaDataItemType, MetaDataItem> metadataMap = new HashMap<MetaDataItemType, MetaDataItem>();
		// we first get the md at doc level
		List<MetaDataItem> retrievedItems= null; 
		
		// the multiple for loops are necessary to override metadata the most specific overriding the most generic.
		retrievedItems = metadataDAO.getDefaultMetadata();
		for (MetaDataItem metaDataItem : retrievedItems) {
			metadataMap.put(metaDataItem.getItemType(), metaDataItem);
		}
		if(profileID != null){
			retrievedItems = metadataDAO.getProfileMetadata(profileID);
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(traID != null){
			retrievedItems = metadataDAO.getTransactionMetadata(traID);
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(documentTypeID != null){
			retrievedItems = metadataDAO.getDocumentMetadata(documentTypeID);
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(agrId != null){
			retrievedItems = metadataDAO.getInterchangeAgreementMetadata(agrId);
			for (MetaDataItem metaDataItem : retrievedItems) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		return metadataMap;
	}
	
	/**
	 * This method clears the metadata cache (metadataItemCache)
	 */
	@Override
	public void clearMetaDataCache(){
		cacheManager.getCache("metadataItemCache").clear();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	@Cacheable(value="responseCodeValueCache")
	public Map<String, String> retrieveResponseCodeValues(
			Long agrId, Long traID, Long documentTypeID, Long profileID) {
		Map<String, String> codesMap = new HashMap<String, String>();
		// we first get the md at doc level
		List<MessageResponseCode> codes= null; 
		
		// the multiple for loops are necessary to override metadata the most specific overriding the most generic.
		codes = messageResponseCodeDAO.getDefaultMessageResponseCode();
		for (MessageResponseCode item : codes) {
			codesMap.put(item.getResponseCodeKey(), item.getResponseCodeValue());
		}
		if(profileID != null){
			codes = messageResponseCodeDAO.getProfileMessageResponseCode(profileID);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item.getResponseCodeValue());
			}
		}
		if(traID != null){
			codes = messageResponseCodeDAO.getTransactionMessageResponseCode(traID);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item.getResponseCodeValue());
			}
		}
		if(documentTypeID != null){
			codes = messageResponseCodeDAO.getDocumentMessageResponseCode(documentTypeID);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item.getResponseCodeValue());
			}
		}
		if(agrId != null){
			codes = messageResponseCodeDAO.getInterchangeAgreementMessageResponseCode(agrId);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item.getResponseCodeValue());
			}
		}
		return codesMap;
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	@Cacheable(value="responseCodeCache")
	public Map<String, MessageResponseCode> retrieveMessageResponseCodes(
			Long agrId, Long traID, Long documentTypeID, Set<Profile> profiles) {
		Map<String, MessageResponseCode> codesMap = new HashMap<String, MessageResponseCode>();
		// we first get the md at doc level
		List<MessageResponseCode> codes= null; 
		
		// the multiple for loops are necessary to override metadata the most specific overriding the most generic.
		codes = messageResponseCodeDAO.getDefaultMessageResponseCode();
		for (MessageResponseCode item : codes) {
			codesMap.put(item.getResponseCodeKey(), item);
		}
		if(profiles != null){
			codes = messageResponseCodeDAO.getProfileMessageResponseCode(profiles);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item);
			}
		}
		if(traID != null){
			codes = messageResponseCodeDAO.getTransactionMessageResponseCode(traID);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item);
			}
		}
		if(documentTypeID != null){
			codes = messageResponseCodeDAO.getDocumentMessageResponseCode(documentTypeID);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item);
			}
		}
		if(agrId != null){
			codes = messageResponseCodeDAO.getInterchangeAgreementMessageResponseCode(agrId);
			for (MessageResponseCode item : codes) {
				codesMap.put(item.getResponseCodeKey(), item);
			}
		}
		return codesMap;
		
	}

	public IMessageResponseCodeDAO getMessageResponseCodeDAO() {
		return messageResponseCodeDAO;
	}

	public void setMessageResponseCodeDAO(
			IMessageResponseCodeDAO messageResponseCodeDAO) {
		this.messageResponseCodeDAO = messageResponseCodeDAO;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public InputStream getMetadataResourceAsStream(Long metadataItemId) {
		return metadataDAO.getMetadataResourceAsStream(metadataItemId);
	}
	
	@Override
	public List<MetaDataItem> getDefaultMetadataByType(String type) {
		return metadataDAO.getDefaultMetadataByType(type);
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<MetaDataItem> getMetaDataItemsByCriteria(MetaDataItem metaDataItem) {
		List<MetaDataItem> list = metadataDAO.getMetaDataItemsByCriteria(metaDataItem);
		
		for(MetaDataItem m : list) {
			initializeMetadataItem(m);
		}
		
		return list;
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public MetaDataItem getMetadata(Long metadataItemId) {
		MetaDataItem metadataItem = metadataDAO.read(metadataItemId);

		if(metadataItem != null) {
			initializeMetadataItem(metadataItem);
		}

		return metadataItem;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MetaDataItem create(MetaDataItem metadataItem) {
		mergeProperties(metadataItem);
		return metadataDAO.create(metadataItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MetaDataItem update(MetaDataItem metadataItem) {
		mergeProperties(metadataItem);
		return metadataDAO.update(metadataItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(MetaDataItem metadataItem) {
		MetaDataItem m = metadataDAO.read(metadataItem.getId());
		metadataDAO.delete(m);
	}

	public IMetadataDAO getMetadataDAO() {
		return metadataDAO;
	}
	public void setMetadataDAO(IMetadataDAO metadataDAO) {
		this.metadataDAO = metadataDAO;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	
	private void mergeProperties(MetaDataItem metadataItem) {
		if(metadataItem.getDocument() != null && metadataItem.getDocument().getId() != null) {
			metadataItem.setDocument(documentDAO.read(metadataItem.getDocument().getId()));
		} else {
			metadataItem.setDocument(null);
		}
		
		if(metadataItem.getInterchangeAgreement() != null && metadataItem.getInterchangeAgreement().getId() != null) {
			metadataItem.setInterchangeAgreement(interchangeAgreementDAO.read(metadataItem.getInterchangeAgreement().getId()));
		} else {
			metadataItem.setInterchangeAgreement(null);
		}
		
		if(metadataItem.getProfile() != null && metadataItem.getProfile().getId() != null) {
			metadataItem.setProfile(profileDAO.read(metadataItem.getProfile().getId()));
		} else {
			metadataItem.setProfile(null);
		}
		
		if(metadataItem.getTansaction() != null && metadataItem.getTansaction().getId() != null) {
			metadataItem.setTansaction(transactionDAO.read(metadataItem.getTansaction().getId()));
		} else {
			metadataItem.setTansaction(null);
		}
	}

	private void initializeMetadataItem(MetaDataItem metaDataItem) {
        Hibernate.initialize(metaDataItem.getDocument());
        Hibernate.initialize(metaDataItem.getInterchangeAgreement());
        Hibernate.initialize(metaDataItem.getProfile());
        Hibernate.initialize(metaDataItem.getTansaction());
        Hibernate.initialize(metaDataItem.getSender());
    }
}
