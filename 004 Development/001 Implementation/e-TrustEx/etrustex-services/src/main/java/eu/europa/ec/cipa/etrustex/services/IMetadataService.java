package eu.europa.ec.cipa.etrustex.services;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public interface IMetadataService {
	
	public Map<MetaDataItemType,MetaDataItem> retrieveMetaData(InterchangeAgreement agr, Transaction transaction , Document document,Set<Profile> profiles);
	public Map<MetaDataItemType, MetaDataItem> retrieveMetaData(Long agrId, Long traID, Long documentTypeID, Long profileId);
	public InputStream getMetadataResourceAsStream(Long metadataItemId);
	public Map<String, String> retrieveResponseCodeValues(Long agrId, Long traID, Long documentTypeID, Long profileID);
	public Map<String, MessageResponseCode> retrieveMessageResponseCodes(Long agrId, Long traID, Long documentTypeID, Set<Profile> profiles);
	public void clearMetaDataCache();
	public List<MetaDataItem> getDefaultMetadataByType(String type);
	public List<MetaDataItem> getMetaDataItemsByCriteria(MetaDataItem metaDataItem);
	MetaDataItem getMetadata(Long metadataItemId);
	MetaDataItem create(MetaDataItem metadataItem);
	MetaDataItem update(MetaDataItem metadataItem);
	void delete(MetaDataItem metadataItem);
}
