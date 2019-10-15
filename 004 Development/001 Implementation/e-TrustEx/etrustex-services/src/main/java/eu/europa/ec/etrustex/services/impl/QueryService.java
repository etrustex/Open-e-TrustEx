package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.IDataExtractionDAO;
import eu.europa.ec.etrustex.dao.IMessageDAO;
import eu.europa.ec.etrustex.dao.IQueryResultDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.query.DataExtractionConfig;
import eu.europa.ec.etrustex.domain.query.QueryResult;
import eu.europa.ec.etrustex.services.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("queryService")
public class QueryService implements IQueryService {
	
	@Autowired
	private IDataExtractionDAO decDAO;
	
	@Autowired
	private ITransactionDAO txDAO;
	
	@Autowired
	private IMessageDAO messageDAO;
	
	@Autowired
	private IQueryResultDAO queryResultDAO;

	@Override
    @Transactional(readOnly = true)
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

    @Transactional
    public void saveQueryResult(String key, String value, Long messageId) {
        QueryResult qr = new QueryResult();
        qr.setKey(key);
        qr.setValue(value);
        qr.setMessage(messageDAO.read(messageId));
        queryResultDAO.create(qr);
    }

    @Transactional(readOnly = true)
    public List<QueryResult> retieveQueryResultsForMessage(Long msgId) {
        return queryResultDAO.getQRByMessageId(msgId);
    }
}
