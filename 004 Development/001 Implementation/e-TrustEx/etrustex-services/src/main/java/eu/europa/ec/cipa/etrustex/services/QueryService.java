package eu.europa.ec.cipa.etrustex.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.query.DataExtractionConfig;
import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;
import eu.europa.ec.cipa.etrustex.services.dao.IDataExtractionDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IMessageDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IQueryResultDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ITransactionDAO;

public class QueryService implements IQueryService {
	
	private IDataExtractionDAO decDAO;

	private ITransactionDAO txDAO;
	
	private IMessageDAO messageDAO;
	
	private IQueryResultDAO queryResultDAO;

	@Override
	public List<DataExtractionConfig> getXpathsForDocument(String transactionRequestLocalName, String transactionLocalName) {		
		Transaction tx = null;		
		try {
			tx = txDAO.getTransactionByNameSpace(transactionLocalName, transactionRequestLocalName);
			if(tx != null){
				return decDAO.getXpathsForDocument(tx.getDocument().getId());
			}
		} catch (Exception e) {

		}		
		return new ArrayList<DataExtractionConfig>();
	}
	
	public void saveQueryResult(String key, String value, Long messageId){
		QueryResult qr = new QueryResult();
		qr.setKey(key);
		qr.setValue(value);
		qr.setMessage(messageDAO.read(messageId));
		queryResultDAO.create(qr);
	}
	
	public List<QueryResult> retieveQueryResultsForMessage(Long msgId){
		return queryResultDAO.getQRByMessageId(msgId);
	}
	
	
	
	
	public ITransactionDAO getTxDAO() {
		return txDAO;
	}

	public void setTxDAO(ITransactionDAO txDAO) {
		this.txDAO = txDAO;
	}

	public IDataExtractionDAO getDecDAO() {
		return decDAO;
	}

	public void setDecDAO(IDataExtractionDAO decDAO) {
		this.decDAO = decDAO;
	}
	
	public IMessageDAO getMessageDAO() {
		return messageDAO;
	}

	public void setMessageDAO(IMessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}
	
	public IQueryResultDAO getQueryResultDAO() {
		return queryResultDAO;
	}

	public void setQueryResultDAO(IQueryResultDAO queryResultDAO) {
		this.queryResultDAO = queryResultDAO;
	}
}
