package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.*;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("metadataService")
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
    private IPartyDAO partyDAO;
    @Autowired
	private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Autowired
	private IProfileDAO profileDAO;
	@Autowired
	private ITransactionDAO transactionDAO;
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	@Cacheable(value="metadataItemCache")
	public Map<MetaDataItemType, MetaDataItem> retrieveMetaData(Transaction transaction, BusinessDomain bd) {
		Map<MetaDataItemType, MetaDataItem> metadataMap = new HashMap<>();

        if(transaction != null){
            Document document = transaction.getDocument();
            Set<Profile> profiles = transaction.getProfiles();

            // the multiple for loops are necessary to override metadata the most specific overriding the most generic.
            for (MetaDataItem metaDataItem : metadataDAO.getDefaultMetadata()) {
                metadataMap.put(metaDataItem.getItemType(), metaDataItem);               
            }
            
            if(bd != null){
            	 for (MetaDataItem metaDataItem : metadataDAO.getBusinessDomainMetadata(bd.getId())) {
                     metadataMap.put(metaDataItem.getItemType(), metaDataItem);
                 }
            }
            
            if(document != null){
                for (MetaDataItem metaDataItem : metadataDAO.getDocumentMetadata(document.getId())) {
                    metadataMap.put(metaDataItem.getItemType(), metaDataItem);
                }
            }

            for (MetaDataItem metaDataItem : metadataDAO.getTransactionMetadata(transaction.getId())) {
                metadataMap.put(metaDataItem.getItemType(), metaDataItem);
            }

            if(profiles != null && profiles.size() != 0){
                for (MetaDataItem metaDataItem : metadataDAO.getProfileMetadata(profiles)) {
                    metadataMap.put(metaDataItem.getItemType(), metaDataItem);
                }
            }
        }

		return metadataMap;
	}

    /*
     * ETRUSTEX-2180 Implementation - Metadata Retrieval and Application
     * Graniluarity level of Metadata definition from the most generic to the most specific
     * 1 Document
     * 2 Transaction
     * 3 Profile
     * 4 ICA
     * 5 Party
     */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	@Cacheable(value="metadataItemCache")
	public Map<MetaDataItemType, MetaDataItem> retrieveMetaData(Long agrId, Long traID, Long documentTypeID, Long profileID, Long partyId) {
		Map<MetaDataItemType, MetaDataItem> metadataMap = new HashMap<>();

		// the multiple for loops are necessary to override metadata the most specific overriding the most generic.
		for (MetaDataItem metaDataItem : metadataDAO.getDefaultMetadata()) {
			metadataMap.put(metaDataItem.getItemType(), metaDataItem);
		}

		if(profileID != null){
			for (MetaDataItem metaDataItem : metadataDAO.getProfileMetadata(profileID)) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}

		if(traID != null){
			for (MetaDataItem metaDataItem : metadataDAO.getTransactionMetadata(traID)) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}

		if(documentTypeID != null){
			for (MetaDataItem metaDataItem : metadataDAO.getDocumentMetadata(documentTypeID)) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}
		if(agrId != null){
			for (MetaDataItem metaDataItem : metadataDAO.getInterchangeAgreementMetadata(agrId)) {
				metadataMap.put(metaDataItem.getItemType(), metaDataItem);
			}
		}

        if(partyId != null){
            for (MetaDataItem metaDataItem : metadataDAO.getPartyMetadata(partyId)) {
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
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public InputStream getMetadataResourceAsStream(Long metadataItemId) {
		return metadataDAO.getMetadataResourceAsStream(metadataItemId);
	}
	
	@Override
    @Transactional(readOnly = true)
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

		if(metadataItem.getSender() != null && metadataItem.getSender().getId() != null) {
		    metadataItem.setSender(partyDAO.read(metadataItem.getSender().getId()));
        } else {
            metadataItem.setSender(null);
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
