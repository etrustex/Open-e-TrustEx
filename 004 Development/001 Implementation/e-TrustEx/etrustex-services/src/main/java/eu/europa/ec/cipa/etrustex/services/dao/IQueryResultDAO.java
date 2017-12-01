package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;

public interface IQueryResultDAO extends ITrustExDAO<QueryResult, Long> {
	
	List<QueryResult> getQRByMessageId(Long Id);
	
}