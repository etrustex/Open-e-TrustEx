package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.domain.query.DataExtractionConfig;
import eu.europa.ec.etrustex.domain.query.QueryResult;

public interface IQueryService {
	
	public List<DataExtractionConfig> getXpathsForDocument(String transactionRequestLocalName, String transactionLocalName);
	
	public void saveQueryResult(String key, String value, Long messageId);
	
	public List<QueryResult> retieveQueryResultsForMessage(Long msgId);
}
