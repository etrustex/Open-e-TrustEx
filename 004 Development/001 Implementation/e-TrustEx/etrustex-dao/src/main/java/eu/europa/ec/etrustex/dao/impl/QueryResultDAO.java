package eu.europa.ec.etrustex.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IQueryResultDAO;
import eu.europa.ec.etrustex.domain.query.QueryResult;

@Repository
public class QueryResultDAO extends TrustExDAO<QueryResult, Long> implements IQueryResultDAO {
	
	
	
	public List<QueryResult> getQRByMessageId(Long msgId){
		return entityManager.createQuery("from QueryResult qr where qr.message.id =:msgId",QueryResult.class)
				.setParameter("msgId", msgId).getResultList();
	}
	
}
