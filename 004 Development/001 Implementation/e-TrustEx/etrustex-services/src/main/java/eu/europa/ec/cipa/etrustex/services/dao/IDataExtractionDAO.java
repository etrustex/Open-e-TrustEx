package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.query.DataExtractionConfig;

public interface IDataExtractionDAO extends ITrustExDAO<DataExtractionConfig, Long> {
	public List<DataExtractionConfig> getXpathsForDocument(Long documentId);
}
