package eu.europa.ec.etrustex.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.query.QueryResult;
import eu.europa.ec.etrustex.types.DataExtractionTypes;

public class QueryResultDaoTest extends AbstractEtrustExTest {

	@Autowired private IQueryResultDAO qrDao;
	@Autowired private IPartyDAO 	   partyDao;
	@Autowired private IMessageDAO 	   msgDao;
	@Autowired private ITransactionDAO transactionDao;
	
	
	@Test public void testGetQRByMessageId(){
		Party partySender = new Party();
		partySender.setName("UTEST_PARTY_S");
		partySender = partyDao.create(partySender);
		
		Transaction t1 = new Transaction();
		t1.setName("T1");
		transactionDao.create(t1);
		
		Message m1 = new Message();
		m1.setDocumentId("M1");
		m1.setSender(partySender);
		m1.setIssuer(partySender);
		m1.setMessageDocumentTypeCode("DTC");
		m1.setTransaction(t1);
		msgDao.create(m1);
		
		Message m2 = new Message();
		m2.setDocumentId("M2");
		m2.setSender(partySender);
		m2.setIssuer(partySender);
		m2.setMessageDocumentTypeCode("DTC");
		m2.setTransaction(t1);
		m2.getAdditionalInfo().addAll(buildQR("myUser", "BUSINESS_DOCUMENT", "Title",m2));
		msgDao.create(m2);
		
		Assert.assertEquals(0, qrDao.getQRByMessageId(m1.getId()).size());
		Assert.assertEquals(3, qrDao.getQRByMessageId(m2.getId()).size());
	}
	
	private List<QueryResult> buildQR(String user, String bd, String title, Message message){
		List<QueryResult> list = new ArrayList<QueryResult>();
		
		QueryResult qr_user = new QueryResult();
		qr_user.setKey(DataExtractionTypes.JMS_USER_ID.name());
		qr_user.setValue(user);
		qr_user.setMessage(message);
		qr_user = qrDao.create(qr_user);
		list.add(qr_user);		
		
		QueryResult qr_bd = new QueryResult();
		qr_bd.setKey(DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name());
		qr_bd.setValue(bd);
		qr_bd.setMessage(message);
		qr_bd = qrDao.create(qr_bd);
		list.add(qr_bd);
		
		QueryResult qr_title = new QueryResult();
		qr_title.setKey(DataExtractionTypes.JMS_TITLE.name());
		qr_title.setValue(title);
		qr_title.setMessage(message);
		qr_bd = qrDao.create(qr_title);
		list.add(qr_title);
		
		return list;
	}
}
