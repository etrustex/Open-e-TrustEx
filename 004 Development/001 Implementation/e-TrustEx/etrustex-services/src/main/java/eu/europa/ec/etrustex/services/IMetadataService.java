package eu.europa.ec.etrustex.services;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public interface IMetadataService {
	
	public Map<MetaDataItemType,MetaDataItem> retrieveMetaData(Transaction transaction, BusinessDomain bd);
	public Map<MetaDataItemType, MetaDataItem> retrieveMetaData(Long agrId, Long traID, Long documentTypeID, Long profileId, Long partyId);
	public InputStream getMetadataResourceAsStream(Long metadataItemId);
	public Map<String, String> retrieveResponseCodeValues(Long agrId, Long traID, Long documentTypeID, Long profileID);
	public Map<String, MessageResponseCode> retrieveMessageResponseCodes(Long agrId, Long traID, Long documentTypeID, Set<Profile> profiles);
	public void clearMetaDataCache();
	public List<MetaDataItem> getDefaultMetadataByType(String type);
	public List<MetaDataItem> getMetaDataItemsByCriteria(MetaDataItem metaDataItem);
	public MetaDataItem getMetadata(Long metadataItemId);
	public MetaDataItem create(MetaDataItem metadataItem);
	public MetaDataItem update(MetaDataItem metadataItem);
	public void delete(MetaDataItem metadataItem);
}
