package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.domain.query.DataExtractionConfig;

public interface IDataExtractionDAO extends ITrustExDAO<DataExtractionConfig, Long> {
	public List<DataExtractionConfig> getXpathsForDocument(Long documentId);
}
