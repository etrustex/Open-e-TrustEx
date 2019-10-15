package eu.europa.ec.etrustex.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IDataExtractionDAO;
import eu.europa.ec.etrustex.domain.query.DataExtractionConfig;

@Repository
public class DataExtractionConfigDAO extends TrustExDAO<DataExtractionConfig, Long> implements IDataExtractionDAO {

	@Override
	public List<DataExtractionConfig> getXpathsForDocument(Long documentId) {
		return entityManager.createQuery("from DataExtractionConfig dec where  dec.document.id = :docId",DataExtractionConfig.class)
				.setParameter("docId", documentId).getResultList();
	}
}
