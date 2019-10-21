package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface IMetadataDAO extends ITrustExDAO<MetaDataItem, Long> {
	
	public List<MetaDataItem> getTransactionMetadata(Long transactionId);
	public List<MetaDataItem> getDocumentMetadata(Long documentId);
	public List<MetaDataItem> getInterchangeAgreementMetadata(Long icaId);
	public List<MetaDataItem> getDefaultMetadata();
	public List<MetaDataItem> getProfileMetadata(Long profileId);
	public List<MetaDataItem> getProfileMetadata(Set<Profile> profiles);
	public List<MetaDataItem> getDefaultMetadataByType(String type);
	public List<MetaDataItem> getMetaDataItemsByCriteria(MetaDataItem metaDataItem);
    public List<MetaDataItem> getPartyMetadata(Long partyId);
    public List<MetaDataItem> getBusinessDomainMetadata(Long businessDomainId);
	public InputStream getMetadataResourceAsStream(Long metadataItemId);

}
