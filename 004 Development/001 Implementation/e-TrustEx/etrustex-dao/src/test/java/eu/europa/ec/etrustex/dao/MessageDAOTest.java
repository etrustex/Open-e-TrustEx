package eu.europa.ec.etrustex.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.query.QueryResult;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.etrustex.types.DataExtractionTypes;
import eu.europa.ec.etrustex.types.SortFieldTypeCode;

public class MessageDAOTest extends AbstractEtrustExTest {

	@Autowired private IMessageDAO 				msgDao;
	@Autowired private IPartyDAO 				partyDao;
	@Autowired private ITransactionDAO 			transactionDao;
	@Autowired private IInterchangeAgreementDAO icaDao;
	@Autowired private IBusinessDomainDAO 		bdDao;
	@Autowired private IQueryResultDAO 			qrDao;
	@Autowired private IMessageRoutingDAO		mrDao;
	@Autowired private IEndpointDAO 			epDao;
	
	private static final String STATUS_CODE_RECEIVED   	= "RECEIVED";
	private static final String STATUS_CODE_ERROR   	= "ERROR";
	private static final String DOCUMENT_TYPE_CODE 		= "TDTC";
	private static final String DOCUMENT_ID		   		= "DOCUMENT_ID_1"; 
	private static final String CORRELATION_ID		   	= "CORRELATION_ID_1";
	private static final String USER_ID		   			= "USER_U_TEST";
	private static final String BUSINESS_DOCUMENT		= "B_DOCUMENT_UTEST";
	private static final String CREATION_USER 			= "user";
	private static final int MAX_RESULTS				= 20;
	private Date startDate;
	private Date endDate;
	
	private Message	    	message1;
	private Party 			partySender;
	private Party 			partyReceiver;
	private Transaction 	transaction1;
	private BusinessDomain 	bd1;
	
	
	@Before 
	public void setupTests(){
		startDate = new Date(System.currentTimeMillis() - (1 * 60 * 60 * 1000));
		endDate = new Date(System.currentTimeMillis());
		
		bd1 = new BusinessDomain();
		bd1.setName("BD1");
		bd1.setResponseSignatureRequired(Boolean.TRUE);
		bdDao.create(bd1);
		
		partySender = new Party();
		partySender.setName("UTEST_PARTY_S");
		partySender.setBusinessDomain(bd1);
		partySender = partyDao.create(partySender);
		
		partyReceiver = new Party();
		partyReceiver.setName("UTEST_PARTY_R");
		partyReceiver = partyDao.create(partyReceiver);
		
		transaction1 = new Transaction();
		transaction1.setName("UTEST_TRANSACTION");
		transaction1 = transactionDao.create(transaction1);
		
		message1 = msgDao.create(buildMessage(DOCUMENT_ID, STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate));
	}
	
	@Test(expected=DataIntegrityViolationException.class) 
	public void testCreateMessageDataIntegrityException(){
		Message m = new Message();
        msgDao.createMessage(m);
		Assert.fail("Exception Not Caught");
	}
	
	@Test 
	public void testCreateMessage(){
        Message m = buildMessage("MY_MESSAGE", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
        msgDao.createMessage(m);
		Assert.assertNotNull(m.getId());
	}
	
	@Test 
	public void testRetrieveMessage4Params(){
		Message m = msgDao.retrieveMessage(DOCUMENT_ID,DOCUMENT_TYPE_CODE,partySender.getId(),partyReceiver.getId());
		Assert.assertNotNull(m.getId());
		
		m = msgDao.retrieveMessage(DOCUMENT_ID,"Fake",partySender.getId(),partyReceiver.getId());
		Assert.assertNull(m);
	}
	
	@Test 
	public void testFindMessagesByDocumentId(){
		String uuid = UUID.randomUUID().toString();
		msgDao.create(buildMessage(uuid, STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate));		
		Assert.assertEquals(1, msgDao.findMessagesByDocumentId(uuid));
	}
		
	@Test 
	public void testRetrieveMessages6Params(){
		Set<String> statesToExclude = new HashSet<String>();
		statesToExclude.add("RECEIVED");
		List<Message> list = msgDao.retrieveMessages(DOCUMENT_ID, DOCUMENT_TYPE_CODE, partySender.getId(), null, Boolean.FALSE, null);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0).getId());
		
		list = msgDao.retrieveMessages(DOCUMENT_ID, DOCUMENT_TYPE_CODE, null, partyReceiver.getId(), Boolean.FALSE, null);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0).getId());
		
		list = msgDao.retrieveMessages(DOCUMENT_ID, DOCUMENT_TYPE_CODE, null, partySender.getId(), Boolean.TRUE, null);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0).getId());
		
		list = msgDao.retrieveMessages(DOCUMENT_ID, DOCUMENT_TYPE_CODE, partyReceiver.getId(), partySender.getId(), Boolean.TRUE, statesToExclude);
		Assert.assertEquals(0, list.size());
		
		list = msgDao.retrieveMessages(DOCUMENT_ID+"_FAKE", DOCUMENT_TYPE_CODE, null, partyReceiver.getId(), Boolean.TRUE, null);
		Assert.assertEquals(0, list.size());
	}
	
	@Test 
	public void testRetrieveMsgWithEproFilterAppResponseNoParent(){		
		MessageQueryDTO mq = new MessageQueryDTO();
		mq.setReceiverPartyId(partySender.getId());
		mq.setTransactions(new HashSet<Transaction>());
		mq.setRetrievedInd(Boolean.FALSE);
		mq.setFetchParents(Boolean.TRUE);
		mq.setInboxServiceFilter(Boolean.TRUE);
		mq.setMaxResult(20);
		
		Message message301 = buildMessage("DOC_301_WithoutParent", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION_ID, false, endDate);
		message301.setMessageDocumentTypeCode("301");
		message301 = msgDao.create(message301);
		
		List<Message> inbox = msgDao.retrieveMessages(mq); 
		Assert.assertEquals("Parent is null so a the doc should be returned", 1, inbox.size());
		
		message301.setMessageDocumentTypeCode("501");
		message301 = msgDao.update(message301);
		
		inbox = msgDao.retrieveMessages(mq);
		Assert.assertEquals("SHouldn't be filtered out", 1, inbox.size());
	}
	
	@Test 
	public void testRetrieveMsgWithEproFilteAppResponseWithParent(){
		List<Message> inbox;
		Set<Transaction> transactions = new HashSet<Transaction>();
		
		Transaction transaction2 = new Transaction();
		transaction2.setName("UTEST_TRANSACTION_2");
		transaction2 = transactionDao.create(transaction2);		
		transactions.add(transaction2);
		
		Transaction transaction3 = new Transaction();
		transaction3.setName("UTEST_TRANSACTION_3");
		transaction3 = transactionDao.create(transaction3);		
			
		MessageQueryDTO mq = new MessageQueryDTO();
		mq.setReceiverPartyId(partySender.getId());
		mq.setTransactions(transactions);
		mq.setRetrievedInd(Boolean.FALSE);
		mq.setFetchParents(Boolean.TRUE);
		mq.setInboxServiceFilter(Boolean.TRUE);
		mq.setMaxResult(20);
		
		Message parentOfMessage301 = buildMessage("DOC_301_Parent", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		parentOfMessage301.setTransaction(transaction3);
		parentOfMessage301 = msgDao.create(parentOfMessage301);
		
		Message message301 = buildMessage("DOC_301", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION_ID, false, endDate);
		message301.setMessageDocumentTypeCode("301");
		message301.setTransaction(transaction2);
		message301.getParentMessages().add(parentOfMessage301);
		message301 = msgDao.create(message301);
		
		inbox = msgDao.retrieveMessages(mq);
		Assert.assertEquals("Should not return a result because the TX-2 should be filtered out", 0, inbox.size());
		
		transactions.add(transaction3);
		inbox = msgDao.retrieveMessages(mq); 
		Assert.assertEquals("Should return a result because the parent is part of the Transactions", 1, inbox.size());
		
		message301.setMessageDocumentTypeCode("916");
		msgDao.update(message301);
		inbox = msgDao.retrieveMessages(mq); 
		Assert.assertEquals("Attached Document should work as well", 1, inbox.size());
	}
	
	@Test 
	public void testRetrieveMsgWithEproFilterAttachement(){
		List<Message> inbox;
		Set<Transaction> transactions = new HashSet<Transaction>();
		
		Transaction transaction2 = new Transaction();
		transaction2.setName("UTEST_TRANSACTION_2");
		transaction2 = transactionDao.create(transaction2);		
		transactions.add(transaction2);
		
		Transaction transaction3 = new Transaction();
		transaction3.setName("UTEST_TRANSACTION_3");
		transaction3 = transactionDao.create(transaction3);		
		//transactions.add(transaction3);
			
		MessageQueryDTO mq = new MessageQueryDTO();
		mq.setReceiverPartyId(partySender.getId());
		mq.setTransactions(transactions);
		mq.setRetrievedInd(Boolean.FALSE);
		mq.setFetchParents(Boolean.TRUE);
		mq.setInboxServiceFilter(Boolean.TRUE);
		mq.setMaxResult(20);
		
		Message grandParent916 = buildMessage("DOC_916_GrandParent", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		grandParent916.setTransaction(transaction3);
		grandParent916 = msgDao.create(grandParent916);
		
		Message parent916 = buildMessage("DOC_916_Parent", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		parent916.setMessageDocumentTypeCode("916");
		parent916.getParentMessages().add(grandParent916);
		parent916 = msgDao.create(parent916);
		
		Message message916 = buildMessage("DOC_916", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION_ID, false, endDate);
		message916.setMessageDocumentTypeCode("916");
		message916.setTransaction(transaction2);
		message916.getParentMessages().add(parent916);
		message916 = msgDao.create(message916);
		inbox = msgDao.retrieveMessages(mq); 
		Assert.assertEquals("Filtering by transaction", 0, inbox.size());	
		
		transactions.add(transaction3);
		inbox = msgDao.retrieveMessages(mq); 
		Assert.assertEquals("Filtering by transaction - Should return 1 result", 1, inbox.size());
	}
	
	
	@Test 
	public void testRetrieveMessages14ParamsFilteredRead_NoParent(){
		List<Message> list;
		
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, partySender.getId()));
		Assert.assertEquals("Only the main message should be returned", 1, list.size());
	}
	
	@Test
	public void testRetrieveMessages14ParamsFilteredRead_WrongParent(){
		List<Message> list;
		
		Message parent = buildMessage(DOCUMENT_ID+"P3", STATUS_CODE_RECEIVED, partySender, partySender, partySender, CORRELATION_ID, false, endDate);
		message1.addParentMessage(parent);
		parent.addChildMessage(message1);
		msgDao.create(parent);
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, partyReceiver.getId()));
		Assert.assertEquals("With Filtering disabled it should return 1 result", 1, list.size());
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, partyReceiver.getId()));
		Assert.assertEquals("With wrong parent should return a result", 1, list.size());
	}
	
	@Test 
	public void testRetrieveMessages14ParamsFilteredRead_RightSenderAndIssuer(){
		List<Message> list;
		
		Message parent = buildMessage(DOCUMENT_ID+"P2", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION_ID, false, endDate);
		message1.addParentMessage(parent);
		parent.addChildMessage(message1);
		msgDao.create(parent);
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, partyReceiver.getId()));
		Assert.assertEquals("With Filtering disabled it should return one result", 1, list.size());
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE,partyReceiver.getId()));
		Assert.assertEquals("With Filtering enabled we need to have 1 result as we have a valid parent", 1, list.size());
	}
	
	@Test 
	public void testRetrieveMessages14ParamsFilteredRead_RightSenderWrongIssuer(){
		List<Message> list;
		
		Message parent1 = buildMessage(DOCUMENT_ID+"P1", STATUS_CODE_RECEIVED, partyReceiver, partySender, partySender, CORRELATION_ID, false, endDate);
		
		message1.addParentMessage(parent1);
		parent1.addChildMessage(message1);
		msgDao.create(parent1);
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, partyReceiver.getId()));
		Assert.assertEquals("With Filtering disabled it should return one result", 1, list.size());
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), null, null, null, null, 20, null, null, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, partyReceiver.getId()));
		Assert.assertEquals("With Filtering enabled no message should be returned, wrong Issuer", 0, list.size());
	}

	
	@Test 
	public void testRetrieveMessages14Params(){
		List<Message> list;
		
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), null, new HashSet<>(Arrays.asList(transaction1)), 20, DOCUMENT_ID, DOCUMENT_TYPE_CODE, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null));
		Assert.assertEquals("Only the main message should be returned", 1, list.size());
				
		Message parentM = buildMessage("Parent_Msg", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION_ID, false, endDate);
		parentM = msgDao.create(parentM);
		
		message1.setParentMessages(new HashSet<>(Arrays.asList(parentM)));
		message1.addMessageBinary(new MessageBinary());
		message1 = msgDao.update(message1);
		
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), null, new HashSet<>(Arrays.asList(transaction1)), 20, DOCUMENT_ID, DOCUMENT_TYPE_CODE, null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
		Assert.assertEquals("1 result returned after fetching the binaries & parents", 1, list.size());
		
		message1.setRetrieveIndicator(Boolean.TRUE);
		message1 = msgDao.update(message1);
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), null, new HashSet<>(Arrays.asList(transaction1)), 20, DOCUMENT_ID, DOCUMENT_TYPE_CODE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
		Assert.assertEquals("Retrieve Indicator should filter out the reult", 0, list.size());
				
		Message errorM = buildMessage("Error_Msg", STATUS_CODE_ERROR, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		errorM = msgDao.create(errorM);
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), null, new HashSet<>(Arrays.asList(transaction1)), 20, null, DOCUMENT_TYPE_CODE, null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null));
		Assert.assertEquals("Error Message is filtered out", 1, list.size());
		
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), null, new HashSet<>(Arrays.asList(transaction1)), 20, null, DOCUMENT_TYPE_CODE, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null));
		Assert.assertEquals("Main & Error Message should be retrieved", 2, list.size());
		
		Profile p = new Profile();
		p.setName("P1");
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setProfile(p);
		ica = icaDao.create(ica);
		
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), ica.getId(), new HashSet<>(Arrays.asList(transaction1)), 20, null, DOCUMENT_TYPE_CODE, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null));
		Assert.assertEquals("No message related to the new ICA", 0, list.size());
		
		message1.setAgreement(ica);
		message1 = msgDao.update(message1);
		
		list = msgDao.retrieveMessages(new MessageQueryDTO(partyReceiver.getId(), partySender.getId(), partySender.getId(), ica.getId(), new HashSet<>(Arrays.asList(transaction1)), 20, null, DOCUMENT_TYPE_CODE, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null));
		Assert.assertEquals("Should return 1 result linked to the new ICA", 1, list.size());
	}
	
	/**
	 * no parents, no binaries, messages in error included
	 */
	@Test 
	public void testRetrieveMessagesForQueryRequest(){		
		Set<Long> senderSet 	= new HashSet<Long>(Arrays.asList(partySender.getId()));
		Set<Long> receiverSet 	= new HashSet<Long>(Arrays.asList(partyReceiver.getId()));
		
		// START OF TEST 1
		//chiricr: issuerPartyId, documentId and hasParent were removed from the method signature because the code calling the method was never sending a non-null parameter
		RetrieveMessagesForQueryRequestDTO queryDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setReceiverPartyIds(receiverSet)
				.setSenderPartyIds(senderSet)
				.setTransactions(new HashSet<>(Arrays.asList(transaction1)))
				.setDocumentTypeCodes(new HashSet<>(Arrays.asList(DOCUMENT_TYPE_CODE)))
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(CORRELATION_ID)
				.setMaxResult(MAX_RESULTS)
				.setFetchParents(false)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(false)
				.setFilterOutMessagesInError(true)
				.build();				
		
		List<Message> list = msgDao.retrieveMessagesForQueryRequest(queryDTO);
		Assert.assertEquals("Should return 1 result", 1, list.size());
		// END OF TEST 1
			
		// START OF TEST 2
		Message errorM = buildMessage("Error_Msg", STATUS_CODE_ERROR, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		errorM = msgDao.create(errorM);
		queryDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setReceiverPartyIds(receiverSet)
				.setSenderPartyIds(senderSet)
				.setTransactions(new HashSet<>(Arrays.asList(transaction1)))
				.setDocumentTypeCodes(new HashSet<>(Arrays.asList(DOCUMENT_TYPE_CODE)))
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(CORRELATION_ID)
				.setMaxResult(MAX_RESULTS)
				.setFetchParents(false)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(false)
				.setFilterOutMessagesInError(false)
				.build();				
		
		list = msgDao.retrieveMessagesForQueryRequest(queryDTO);
		Assert.assertEquals("Should return 1 result + 1 Error", 2, list.size());
		// END OF TEST 2
		
		// START OF TEST 3
		Message parentM = buildMessage("Parent_Msg", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION_ID, false, endDate);
		parentM = msgDao.create(parentM);
		
		message1.setParentMessages(new HashSet<>(Arrays.asList(parentM)));
		message1.addMessageBinary(new MessageBinary());
		message1.setRetrieveIndicator(Boolean.TRUE);
		message1 = msgDao.update(message1);
		
		queryDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setReceiverPartyIds(receiverSet)
				.setSenderPartyIds(senderSet)
				.setTransactions(new HashSet<>(Arrays.asList(transaction1)))
				.setDocumentTypeCodes(new HashSet<>(Arrays.asList(DOCUMENT_TYPE_CODE)))
				.setRetrievedInd(true)
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(CORRELATION_ID)
				.setMaxResult(MAX_RESULTS)
				.setFetchParents(true)
				.setFetchBinaries(true)
				.setIsSenderAlsoReceiver(false)
				.setFilterOutMessagesInError(false)
				.build();
		list = msgDao.retrieveMessagesForQueryRequest(queryDTO);
		Assert.assertEquals("Should return 1 result after adding parent & message Binary", 1, list.size());
		// END OF TEST 3
		
		// START OF TEST 4
		//TODO Is this realistic?
		Message senderReceiver = buildMessage("senderReceiver", STATUS_CODE_RECEIVED, partySender, partySender, partySender, CORRELATION_ID, false, endDate);
		senderReceiver = msgDao.create(senderReceiver);
		
		queryDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setSenderPartyIds(senderSet)
				.setTransactions(new HashSet<>(Arrays.asList(transaction1)))
				.setDocumentTypeCodes(new HashSet<>(Arrays.asList(DOCUMENT_TYPE_CODE)))
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(CORRELATION_ID)
				.setMaxResult(MAX_RESULTS)
				.setFetchParents(false)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(true)
				.setFilterOutMessagesInError(true)
				.build();		
		list = msgDao.retrieveMessagesForQueryRequest(queryDTO);
		//ETRUSTEX-3859 by removing the parameter hasParents, which was never used aside from the unit tests, this test now returns 2 records instead of 1
		Assert.assertEquals("Should return 2 results: SenderReceiver and Parent_Msg", 2, list.size());
		// END OF TEST 4
		
		// START OF TEST 5
		queryDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setReceiverPartyIds(receiverSet)
				.setSenderPartyIds(senderSet)
				.setTransactions(new HashSet<>(Arrays.asList(transaction1)))
				.setDocumentTypeCodes(new HashSet<>(Arrays.asList(DOCUMENT_TYPE_CODE)))
				.setRetrievedInd(true)
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(CORRELATION_ID)
				.setFetchParents(true)
				.setFetchBinaries(true)
				.setIsSenderAlsoReceiver(false)
				.setFilterOutMessagesInError(false)
				.build();				
		
		list = msgDao.retrieveMessagesForQueryRequest(queryDTO);
		Assert.assertEquals("Should return 1 result ", 1, list.size());
		// END OF TEST 5
	}
	
	@Test 
	public void testGetStatusCodes(){
		int scCount = msgDao.getStatusCodes().size();
		String SC = "FAKE_SC";
		msgDao.create(buildMessage("01", SC, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate));
		List<String> sc2 = msgDao.getStatusCodes();
		
		Assert.assertEquals("There should be 1 new Status Code", 1 ,sc2.size()-scCount);
		
		boolean newStatusFound = false;
		for (String string : sc2) {
			if(SC.equals(string)){
				newStatusFound = true;
			}
		}
		if(!newStatusFound){
			Assert.fail("New Status Code Not Returned");
		}
	}
	
	@Test 
	public void testGetDocumentTypeCodes(){
		boolean newDTCFound = false;
		for (String string : msgDao.getDocumentTypeCodes()) {
			if(DOCUMENT_TYPE_CODE.equals(string)){
				newDTCFound = true;
			}
		}
		if(!newDTCFound){
			Assert.fail("New Document Type Code Not Returned");
		}
	}
	
	@Test 
	public void testFindMessagesByTransaction(){
		msgDao.create(buildMessage("01", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate));
		Assert.assertEquals("Should return 2 ", 2,msgDao.findMessagesByTransaction(transaction1.getId()));
		
		Transaction transaction2 = new Transaction();
		transaction2.setName("UTEST_TRANSACTION_2");
		transaction2 = transactionDao.create(transaction2);
		Assert.assertEquals("Should return 0", 0,msgDao.findMessagesByTransaction(transaction2.getId()));
	}
	
	@Test 
	public void testRetrieveOrphans(){
		List<Message> list;

		Transaction transaction2 = new Transaction();
		transaction2.setName("UTEST_TRANSACTION2");
		transaction2 = transactionDao.create(transaction2);
		
		Transaction transaction3 = new Transaction();
		transaction3.setName("UTEST_TRANSACTION3");
		transaction3 = transactionDao.create(transaction3);
		
		Message message2 = buildMessage("Message2", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		msgDao.create(message2);
		
		Message message3 = buildMessage("Message3", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		message3.setTransaction(transaction2);
		msgDao.create(message3);
		
		list = msgDao.retrieveOrphans(bd1, transaction1, startDate, endDate);
		Assert.assertEquals("Should return 2", 2, list.size());
		
		list = msgDao.retrieveOrphans(bd1, null, startDate, endDate);
		Assert.assertEquals("Should return 3 as TX is not taken into account", 3, list.size());
		
		message1.getChildMessages().add(message2);
		message2.addParentMessage(message1);
		msgDao.update(message1);
		
		list = msgDao.retrieveOrphans(bd1, transaction1, startDate, endDate);
		Assert.assertEquals("Should only return the parent", 1, list.size());
		Assert.assertEquals("Should only return the parent", message1.getId(), list.get(0).getId());
		
		list = msgDao.retrieveOrphans(bd1, null, startDate, endDate);
		Assert.assertEquals("Should return message1 & 3", 2, list.size());
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("BD2");
		bd2.setResponseSignatureRequired(Boolean.TRUE);
		bd2 = bdDao.create(bd2);
		
		list = msgDao.retrieveOrphans(bd2, transaction1, startDate, endDate);
		Assert.assertEquals("Should return 0 as bd2 is not related to any message", 0, list.size());
	}
	
	@Test 
	public void testRetrieveLeaves(){
		List<Message> list;
		
		list = msgDao.retrieveLeaves(bd1, transaction1, startDate, endDate, null);
		Assert.assertEquals("One message available for this transaction", 1, list.size());
		Assert.assertEquals(message1.getId(), list.get(0).getId());
		
		Message message2 = buildMessage("Message2", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION_ID, false, endDate);
		message2.addParentMessage(message1);
		msgDao.create(message2);
		
		list = msgDao.retrieveLeaves(bd1, transaction1, startDate, endDate, null);
		Assert.assertEquals("One leave available for this transaction", 1, list.size());
		Assert.assertEquals(message2.getId(), list.get(0).getId());
		
		Transaction transaction2 = new Transaction();
		transaction2.setName("UTEST_TRANSACTION2");
		transaction2 = transactionDao.create(transaction2);
		message2.setTransaction(transaction2);
		msgDao.update(message2);
		
		list = msgDao.retrieveLeaves(bd1, transaction1, startDate, endDate, new HashSet<Transaction>(Arrays.asList(transaction2)));
		Assert.assertEquals("Parent SHould be returned", 1, list.size());
		Assert.assertEquals(message1.getId(), list.get(0).getId());		
	}
	
	@Test 
	public void testRetrieveMessagesJustice(){
		MessagesListVO listVO;
		
		Set<Long> senderSet 	= new HashSet<Long>(Arrays.asList(partySender.getId()));
		Set<Long> receiverSet 	= new HashSet<Long>(Arrays.asList(partyReceiver.getId()));
		Set<Transaction> tx1Set = new HashSet<Transaction>(Arrays.asList(transaction1));
		Set<String> dtcSet 		= new HashSet<String>(Arrays.asList(DOCUMENT_TYPE_CODE));

		message1.getAdditionalInfo().addAll(buildQR(USER_ID, BUSINESS_DOCUMENT, "Title",message1));
		message1.setRetrieveIndicator(Boolean.TRUE);
		msgDao.update(message1);
		
		RetrieveMessagesForQueryRequestDTO.Builder queryDTOBuilder = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setReceiverPartyIds(receiverSet)
				.setSenderPartyIds(senderSet)
				.setTransactions(tx1Set)
				.setDocumentTypeCodes(dtcSet)
				.setRetrievedInd(true)
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setCorrelationId(CORRELATION_ID)
				.setMaxResult(MAX_RESULTS)
				.setFetchParents(false)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(false)
				.setUserId(USER_ID)
				.setBusinessDocumentTypes(new HashSet<String>(Arrays.asList(BUSINESS_DOCUMENT)))
				.setPaginationFrom(BigDecimal.valueOf(0))
				.setPaginationTo(BigDecimal.valueOf(1))
				.setSortField(SortFieldTypeCode.TITLE)
				.setFilterOutMessagesInError(true);
		RetrieveMessagesForQueryRequestDTO queryDTO = queryDTOBuilder.build(); 
		listVO = msgDao.retrieveMessagesJustice(queryDTO);
		Assert.assertEquals(1, listVO.getMessages().size());
		
		queryDTOBuilder.setFilterOutMessagesInError(false);
		queryDTO = queryDTOBuilder.build();
		listVO = msgDao.retrieveMessagesJustice(queryDTO);
		Assert.assertEquals(1, listVO.getMessages().size());
	}
	
	@Test 
	public void testRetrieveMessagesJusticeSorting(){
		String USER = "gradmin1";
		
		String CORRELATION1 = "Corr1";
		String CORRELATION2 = "Corr2";
		//START of TEST 1
		Set<Long> senderSet = new HashSet<Long>(Arrays.asList(partySender.getId()));
		RetrieveMessagesForQueryRequestDTO.Builder queryDTOBuilder = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setSenderPartyIds(senderSet)
				.setDocumentTypeCodes(new HashSet<String>())
				.setRetrievedInd(true)
				.setMaxResult(1000)
				.setFetchParents(true)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(false)
				.setUserId(USER)
				.setBusinessDocumentTypes(new HashSet<String>())
				.setPaginationFrom(BigDecimal.valueOf(0))
				.setPaginationTo(BigDecimal.valueOf(10))
				.setSortField(SortFieldTypeCode.SUBMISSION_DATE)
				.setFilterOutMessagesInError(false);
		RetrieveMessagesForQueryRequestDTO queryDTO = queryDTOBuilder.build(); 		
		MessagesListVO listVO = msgDao.retrieveMessagesJustice(queryDTO);				
		Assert.assertEquals(0, listVO.getSize().longValue());
		//END of TEST 1
		
		//START of TEST 2
		Message tempM;
		Date issueDate;
		//First Conversation
		issueDate = new GregorianCalendar(2003, 01, 01).getTime();
		tempM =  msgDao.create(buildMessage(DOCUMENT_ID+"-FormA-01", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION1, true, issueDate));
		tempM.getAdditionalInfo().addAll(buildQR(USER, BUSINESS_DOCUMENT, "Title 1",tempM));
		tempM = msgDao.update(tempM);
		
		issueDate = new GregorianCalendar(2013, 01, 01).getTime();
		tempM =  msgDao.create(buildMessage(DOCUMENT_ID+"-FormB-01", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION1, false, issueDate));
		tempM.setIssueDate(issueDate);
		tempM.getAdditionalInfo().addAll(buildQR(USER, BUSINESS_DOCUMENT, "Title 1",tempM));
		tempM = msgDao.update(tempM);

		//Second Conversation
		issueDate = new GregorianCalendar(2004, 01, 01).getTime();
		tempM =  msgDao.create(buildMessage(DOCUMENT_ID+"-FormA-02", STATUS_CODE_RECEIVED, partySender, partyReceiver, partySender, CORRELATION2, true, issueDate));
		tempM.getAdditionalInfo().addAll(buildQR(USER, BUSINESS_DOCUMENT, "Title 2",tempM));
		tempM = msgDao.update(tempM);
		
		issueDate = new GregorianCalendar(2010, 01, 01).getTime();
		tempM =  msgDao.create(buildMessage(DOCUMENT_ID+"-FormB-02", STATUS_CODE_RECEIVED, partyReceiver, partySender, partyReceiver, CORRELATION2, false,issueDate));
		tempM.getAdditionalInfo().addAll(buildQR(USER, BUSINESS_DOCUMENT, "Title 2",tempM));
		tempM = msgDao.update(tempM);
		
		queryDTOBuilder.setRetrievedInd(null);
		queryDTO = queryDTOBuilder.build();		
		listVO = msgDao.retrieveMessagesJustice(queryDTO);		
		Assert.assertEquals(2, listVO.getSize().longValue());
		//END of TEST 2
		
	}
	
	@Test 
	public void testFindMessagesByCriteria(){
		List<Message> list;
		
		Profile p = new Profile();
		p.setName("P1");
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setProfile(p);
		ica = icaDao.create(ica);
		p= ica.getProfile();
		
		flush();
		
		JMSEndpoint ep = new JMSEndpoint();
		ep.setDestinationJndiName("D");
		ep.setIsSupportingReplyTo(false);
		ep.setIsActive(true);
		ep.setProfile(p);
		ep = (JMSEndpoint)epDao.create(ep);
		
		flush();
		
		message1.setAgreement(ica);
		msgDao.update(message1);
		
		Message messageExample = new Message();
		messageExample.getAccessInfo().setCreationId(CREATION_USER);;
		messageExample.setSender(partySender);
		messageExample.setIssuer(partySender);
		messageExample.setCorrelationId(CORRELATION_ID);
		messageExample.setDocumentId(DOCUMENT_ID);
		messageExample.setMessageDocumentTypeCode(DOCUMENT_TYPE_CODE);
		messageExample.setRetrieveIndicator(Boolean.FALSE);
		messageExample.setReceiver(partyReceiver);
		messageExample.setTransaction(transaction1);
		messageExample.setStatusCode(STATUS_CODE_RECEIVED);
		messageExample.setAgreement(ica);
	
		list = msgDao.findMessagesByCriteria(messageExample, startDate, endDate, 0, 20, Arrays.asList(bd1.getId()), null);
		Assert.assertEquals(1, list.size());
		
		MessageRouting mr1 = new MessageRouting();
		mr1.setMessage(message1);
		mr1.setProcessed(Boolean.TRUE);
		mr1.setEndpoint(ep);
		mrDao.create(mr1);
		
		list = msgDao.findMessagesByCriteria(messageExample, startDate, endDate, 0, 20, Arrays.asList(bd1.getId()), DispatchEnum.YES);
		Assert.assertEquals(1, list.size());
		
		MessageRouting mr2 = new MessageRouting();
		mr2.setMessage(message1);
		mr2.setProcessed(Boolean.FALSE);
		mr2.setEndpoint(ep);
		mrDao.create(mr2);
		
		list = msgDao.findMessagesByCriteria(messageExample, startDate, endDate, 0, 20, Arrays.asList(bd1.getId()), DispatchEnum.PARTIAL);
		Assert.assertEquals(1, list.size());
	}
	
	@Test 
	public void testCountMessagesByCriteria(){
		Profile p = new Profile();
		p.setName("P1");
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.setProfile(p);
		ica = icaDao.create(ica);
		p= ica.getProfile();
		
		flush();
		
		JMSEndpoint ep = new JMSEndpoint();
		ep.setDestinationJndiName("D");
		ep.setIsSupportingReplyTo(false);
		ep.setIsActive(true);
		ep.setProfile(p);
		ep = (JMSEndpoint)epDao.create(ep);
		
		flush();
		
		message1.setAgreement(ica);
		msgDao.update(message1);
		
		Message messageExample = new Message();
		messageExample.getAccessInfo().setCreationId(CREATION_USER);;
		messageExample.setSender(partySender);
		messageExample.setIssuer(partySender);
		messageExample.setCorrelationId(CORRELATION_ID);
		messageExample.setDocumentId(DOCUMENT_ID);
		messageExample.setMessageDocumentTypeCode(DOCUMENT_TYPE_CODE);
		messageExample.setRetrieveIndicator(Boolean.FALSE);
		messageExample.setReceiver(partyReceiver);
		messageExample.setTransaction(transaction1);
		messageExample.setStatusCode(STATUS_CODE_RECEIVED);
		messageExample.setAgreement(ica);
	
		Assert.assertEquals(1, msgDao.countMessagesByCriteria(messageExample, startDate, endDate, Arrays.asList(bd1.getId()), null));
		
		MessageRouting mr1 = new MessageRouting();
		mr1.setMessage(message1);
		mr1.setProcessed(Boolean.TRUE);
		mr1.setEndpoint(ep);
		mrDao.create(mr1);
		
		Assert.assertEquals(1, msgDao.countMessagesByCriteria(messageExample, startDate, endDate, Arrays.asList(bd1.getId()), DispatchEnum.YES));
		
		MessageRouting mr2 = new MessageRouting();
		mr2.setMessage(message1);
		mr2.setProcessed(Boolean.FALSE);
		mr2.setEndpoint(ep);
		mrDao.create(mr2);
		
		Assert.assertEquals(1, msgDao.countMessagesByCriteria(messageExample, startDate, endDate, Arrays.asList(bd1.getId()), DispatchEnum.PARTIAL));
	}
	
	private Message buildMessage(String docId, String statusCode, Party sender, Party receiver, Party issuer, String corrId, Boolean retIndicator, Date issueDate){
		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationDate(endDate);
		eai.setCreationId(CREATION_USER);
		eai.setModificationDate(endDate);
		
		Message m = new Message();
		m.setAccessInfo(eai);
		m.setDocumentId(docId);
		m.setStatusCode(statusCode);
		m.setMessageDocumentTypeCode(DOCUMENT_TYPE_CODE);
		m.setTransaction(transaction1);
		m.setSender(sender);
		m.setIssuer(issuer);
		m.setReceiver(receiver);
		m.setReceptionDate(endDate);
		m.setIssueDate(issueDate);
		m.setCorrelationId(corrId);
		m.setRetrieveIndicator(retIndicator);
		return m;
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
		qr_bd.setValue(BUSINESS_DOCUMENT);
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
