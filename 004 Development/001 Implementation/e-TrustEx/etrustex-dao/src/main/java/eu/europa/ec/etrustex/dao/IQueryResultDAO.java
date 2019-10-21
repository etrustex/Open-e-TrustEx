package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.domain.query.QueryResult;

public interface IQueryResultDAO extends ITrustExDAO<QueryResult, Long> {
	
	List<QueryResult> getQRByMessageId(Long Id);
	
}