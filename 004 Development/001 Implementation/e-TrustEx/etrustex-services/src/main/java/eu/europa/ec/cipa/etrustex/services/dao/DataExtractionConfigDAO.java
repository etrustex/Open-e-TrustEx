package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.cipa.etrustex.domain.query.DataExtractionConfig;

public class DataExtractionConfigDAO extends TrustExDAO<DataExtractionConfig, Long> implements IDataExtractionDAO {
	
	@Autowired
	private DataSource eTrustExDS;
	
	public DataSource geteTrustExDS() {
		return eTrustExDS;
	}

	public void seteTrustExDS(DataSource eTrustExDS) {
		this.eTrustExDS = eTrustExDS;
	}

	@Override
	public List<DataExtractionConfig> getXpathsForDocument(Long documentId) {
		return entityManager.createQuery("from DataExtractionConfig dec where  dec.document.id = :docId",DataExtractionConfig.class)
				.setParameter("docId", documentId).getResultList();
	}
}
