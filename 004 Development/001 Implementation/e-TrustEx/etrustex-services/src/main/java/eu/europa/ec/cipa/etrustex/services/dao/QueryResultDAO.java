package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;

public class QueryResultDAO extends TrustExDAO<QueryResult, Long> implements IQueryResultDAO {
	
	
	
	public List<QueryResult> getQRByMessageId(Long msgId){
		return entityManager.createQuery("from QueryResult qr where qr.message.id =:msgId",QueryResult.class)
				.setParameter("msgId", msgId).getResultList();
	}
	
}
