package eu.europa.ec.etrustex.services;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IMessageBinaryDAO;
import eu.europa.ec.etrustex.dao.IMessageDAO;
import eu.europa.ec.etrustex.dao.IPolicyDAO;
import eu.europa.ec.etrustex.dao.dto.CreateMessageBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.CreateMessageDTO;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.exception.BinaryStreamLimitExceededException;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.dao.exception.MessageRetrieveException;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.VolumeSlaPolicy;
import eu.europa.ec.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.FrequencyType;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import eu.europa.ec.etrustex.types.MetaDataItemType;

//TODO Revisit after refactoring & externalising FileSystem Handlers
public class MessageServiceTest extends AbstractEtrustExTest{
	
	private InputStream file1;
	private InputStream file2;
	
	@Autowired 	private IMessageService messageService;
	@Mock 		private IMessageDAO messageDAO;
	@Mock 		private IMessageBinaryDAO messageBinaryDAO;
	@Mock 		private IMetadataService metadataService;
	@Mock 		private ILogService logService;
	@Mock 		private IAuthorisationService authorisationService;
	@Mock 		private IPartyService partyService;
	@Mock 		private ITransactionService transactionService;
    @Mock 		private IPolicyDAO policyDAO;
    
    @Before public void testinit(){
		//Injection doesn't work with transactional service through @InjectMocks so we have to do it through reflection 
		MockitoAnnotations.initMocks(this);
		
		ReflectionTestUtils.setField(messageService, "metadataService", metadataService);
		ReflectionTestUtils.setField(messageService, "logService", logService);
		ReflectionTestUtils.setField(messageService, "authorisationService", authorisationService);
		ReflectionTestUtils.setField(messageService, "partyService", partyService);
		ReflectionTestUtils.setField(messageService, "transactionService", transactionService);
		
		ReflectionTestUtils.setField(messageService, "messageDAO", messageDAO);
		ReflectionTestUtils.setField(messageService, "messageBinaryDAO", messageBinaryDAO);		
		ReflectionTestUtils.setField(messageService, "policyDAO", policyDAO);
		
		file1 = getClass().getClassLoader().getResourceAsStream("dumbFile1.txt");
		file2 = getClass().getClassLoader().getResourceAsStream("dumbFile2.txt");
	}
        
    @Test public void testRetrieveMessages(){
    	String messageDocumentId 	= "myId";
    	String documentTypeCode	 	= "myDTC"; 
    	Long senderId			 	= 1L; 
    	Long receiverId			 	= 2L;
    	Boolean biDirectional	 	= false; 
    	Set<String> statesToExclude = null;
    	
    	Long mId = 45L;   	
    	Message m = new Message();
    	m.setId(mId);
    	
    	when(messageDAO.retrieveMessages(messageDocumentId, documentTypeCode, senderId, receiverId, biDirectional, statesToExclude)).thenReturn(new ArrayList<Message>(Arrays.asList(m, m)));  	
    	assertEquals(m.getId(), messageService.retrieveMessage(messageDocumentId, documentTypeCode, senderId, receiverId, biDirectional, statesToExclude).getId());
    	 
    	when(messageDAO.retrieveMessages(messageDocumentId, documentTypeCode, senderId, receiverId, biDirectional, statesToExclude)).thenReturn(null);
    	assertNull(messageService.retrieveMessage(messageDocumentId, documentTypeCode, senderId, receiverId, biDirectional, statesToExclude));
    };
    
    @Test public void testRetrieveMessage(){
    	String messageDocumentId 	= "myId";
    	String documentTypeCode	 	= "myDTC"; 
    	Long senderId			 	= 1L; 
    	Long receiverId			 	= 2L;
    	
    	Long mId = 45L;   	
    	Message m = new Message();
    	m.setId(mId);
    	
    	when(messageDAO.retrieveMessage(messageDocumentId, documentTypeCode, senderId, receiverId)).thenReturn(m);  	
    	assertEquals(m.getId(), messageService.retrieveMessage(messageDocumentId, documentTypeCode, senderId, receiverId).getId());
    	 
    	when(messageDAO.retrieveMessage(messageDocumentId, documentTypeCode, senderId, receiverId)).thenReturn(null);
    	assertNull(messageService.retrieveMessage(messageDocumentId, documentTypeCode, senderId, receiverId));
    };
    
    @Test public void testRetrieveMessageBinary(){
    	MessageBinary mb = new MessageBinary();
    	mb.setId(45L);
    	
    	when(messageBinaryDAO.read(45L)).thenReturn(mb);
    	
    	messageService.retrieveMessageBinary(45L);
    	
    	verify(messageBinaryDAO, times(1)).read(45L);
    };

    @Test public void testUpdateMessageBinary() throws MessageUpdateException{
    	MessageBinary mb = new MessageBinary();
    	mb.setId(45L);
    	
    	when(messageBinaryDAO.update(mb)).thenReturn(mb);
    	
    	messageService.updateMessageBinary(mb);
    	
    	verify(messageBinaryDAO, times(1)).update(mb);
    };	
    
    @Test public void testRetrieveMessageWithInitializedProperties(){
    	Long messageId = 45L;
    	boolean initCollections = true;
    	
    	Party sender = new Party();
    	sender.setBusinessDomain(new BusinessDomain());
    	Message parent = new Message();
    	parent.setSender(sender);
    	
    	Message msg = new Message();
    	msg.getParentMessages().add(parent);
    	msg.getChildMessages().add(parent);
    	
    	when(messageDAO.read(messageId)).thenReturn(msg);
    	
    	messageService.retrieveMessageWithInitializedProperties(messageId, initCollections);
    	
    	verify(messageDAO, times(1)).read(messageId);
    };
    
    @Test public void testCountMessagesByCriteria(){
    	Message message = new Message();
    	Date createdFrom = Calendar.getInstance().getTime();
    	Date createdTo = Calendar.getInstance().getTime();
    	List<Long> businessDomainIds = null;
    	DispatchEnum dispatched = DispatchEnum.PARTIAL;
    	
    	when(messageDAO.countMessagesByCriteria(message, createdFrom, createdTo, businessDomainIds, dispatched)).thenReturn(5L);
    	
    	Long value = messageService.countMessagesByCriteria(message, createdFrom, createdTo, businessDomainIds, dispatched);
    	assertEquals((Long)5L, value);
    };
    
    @Test public void testFindMessagesByCriteria(){
    	Message message = new Message();
    	Date createdFrom = Calendar.getInstance().getTime();
    	Date createdTo = Calendar.getInstance().getTime();
    	List<Long> businessDomainIds = null;
    	DispatchEnum dispatched = DispatchEnum.PARTIAL;
    	
    	List<Message> list = new ArrayList<Message>();
    	list.add(new Message());
    	list.add(new Message());
    	list.add(new Message());
    	
    	when(messageDAO.findMessagesByCriteria(message, createdFrom, createdTo, 1, 5, businessDomainIds, dispatched)).thenReturn(list);
    	
    	List<Message> newList = messageService.findMessagesByCriteria(message, createdFrom, createdTo, 1, 5, businessDomainIds, dispatched);
    	
    	assertEquals(3, newList.size());
    };
    
    @Test public void testFindMessagesByDocumentId(){
    	String messageDocumentId = "Doc";
    	
    	when(messageDAO.findMessagesByDocumentId(messageDocumentId)).thenReturn(5L);
    	
    	assertEquals(5, messageService.findMessagesByDocumentId(messageDocumentId));
    };
    
    @Test public void testGetMessageBinary(){
    	Long messageId 	  = 45L;
    	String binaryType = "BType"; 
    	
    	MessageBinary mb = new MessageBinary();
    	mb.setId(messageId);
    	
    	when(messageBinaryDAO.getMessageBinary(messageId, binaryType)).thenReturn(mb);
    	
    	assertEquals(mb, messageService.getMessageBinary(messageId, binaryType));
    }; 
    
    @Test public void testDetachParent(){
    	Message parent = new Message();
    	parent.setId(45L);
    	
    	Message child = new Message();
    	child.setId(46L);
    	child.getParentMessages().add(parent);
    	
    	when(messageDAO.read(46L)).thenReturn(child);
    	when(messageDAO.update(child)).thenReturn(child);
    	
    	messageService.detachParent(46L);
    	
    	assertNull(child.getParentMessages());
    };
    
    @Test public void testIsInterchangeAgreementUsedToSendMessages(){
    	InterchangeAgreement interchangeAgreement = new InterchangeAgreement();
    	
    	when(messageDAO.isInterchangeAgreementUsedToSendMessages(interchangeAgreement)).thenReturn(true);
    	assertTrue(messageService.isInterchangeAgreementUsedToSendMessages(interchangeAgreement));
    	
    	when(messageDAO.isInterchangeAgreementUsedToSendMessages(interchangeAgreement)).thenReturn(false);
    	assertFalse(messageService.isInterchangeAgreementUsedToSendMessages(interchangeAgreement));
    };

    @Test public void testGetMessageBinaryAsStream1(){
    	Long messageBinaryId = 5L;
    	
    	InputStream is = file1;
    	
    	when(messageBinaryDAO.getMessageBinaryAsStream(messageBinaryId)).thenReturn(is);
    	
    	InputStream is2 = messageService.getMessageBinaryAsStream(messageBinaryId);
    	
    	assertEquals(is, is2);
    	verify(messageBinaryDAO, times(1)).getMessageBinaryAsStream(messageBinaryId);
    };
   
    @Test public void testGetMessageBinaryAsStream2(){
    	Long messageBinaryId = 5L;
    	MessageBinaryType binaryType = MessageBinaryType.BINARY_ATTACHMENT;
    	
    	InputStream is = file1;
		
    	when(messageBinaryDAO.getMessageBinaryAsStream(messageBinaryId, binaryType)).thenReturn(is);
    	
    	InputStream is2 = messageService.getMessageBinaryAsStream(messageBinaryId, binaryType);
    	
    	assertEquals(is, is2);
    	verify(messageBinaryDAO, times(1)).getMessageBinaryAsStream(messageBinaryId, binaryType);
    };
    
    @Test public void testGetMessageBinaryAsString(){
    	Long messageId = 5L;
    	MessageBinaryType binaryType = MessageBinaryType.BINARY_ATTACHMENT;
    	
    	String bin = "myTest";
    	
    	when(messageBinaryDAO.getMessageBinaryAsString(messageId, binaryType)).thenReturn(bin);
    	
    	String bin2 = messageService.getMessageBinaryAsString(messageId, binaryType);
    	
    	assertTrue(bin2.equals(bin));
    	verify(messageBinaryDAO, times(1)).getMessageBinaryAsString( messageId, binaryType);
    };
    
    @Test public void testGetDecryptedStream(){
    	byte[] iv = {000};
    	InputStream encryptedStream = file1;
    	InputStream decStream = file2;
    	
    	try {
			when(messageBinaryDAO.getDecryptedStream(encryptedStream, iv)).thenReturn(decStream);
		} catch (IOException e) {fail("");}
    	
    	InputStream resIS = null;
		try {
			resIS = messageService.getDecryptedStream(encryptedStream, iv);
		} catch (MessageRetrieveException e) {fail("");}
    	assertEquals(decStream, resIS);
    	
    	try {
			when(messageBinaryDAO.getDecryptedStream(encryptedStream, iv)).thenThrow(new IOException());
		} catch (IOException e) {fail("");}
    	
    	try {
			resIS = messageService.getDecryptedStream(encryptedStream, iv);
			fail("");
		} catch (MessageRetrieveException e) {}
    };
    
    @Test public void testRetrieveMessages2(){
    	MessageQueryDTO messageDTO = new MessageQueryDTO();
    	
    	List<Message> list = new ArrayList<Message>(Arrays.asList(new Message(), new Message(), new Message()));
    	when(messageDAO.retrieveMessages(messageDTO)).thenReturn(list);
    	
    	assertEquals(3, messageService.retrieveMessages(messageDTO).size());
    	verify(messageDAO, times(1)).retrieveMessages(messageDTO);
    };
    
    @Test public void testgetDocumentTypeCodes(){
    	when(messageDAO.getDocumentTypeCodes()).thenReturn(new ArrayList<String>(Arrays.asList("A","B","C")));
    	assertEquals(3, messageService.getDocumentTypeCodes().size());
    	verify(messageDAO, times(1)).getDocumentTypeCodes();
    };
    
    @Test public void testFindMessagesByTransaction(){
    	Long txId = 5L;
    	when(messageDAO.findMessagesByTransaction(txId)).thenReturn(6L);
    	assertEquals(6L, messageService.findMessagesByTransaction(txId));
    	verify(messageDAO, times(1)).findMessagesByTransaction(txId);
    };
    
    @Test public void testRetrieveMessagesWithConversation(){
    	RetrieveMessagesForQueryRequestDTO queryDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
				.setFetchParents(false)
				.setFetchBinaries(false)
				.setIsSenderAlsoReceiver(false)
				.setFilterOutMessagesInError(true)
				.build();	
    	
    	MessagesListVO list = new MessagesListVO(new ArrayList<Message>(), 5);
    	
    	when(messageDAO.retrieveMessagesJustice(queryDTO)).thenReturn(list);
    	assertEquals(list, messageService.retrieveMessagesWithConversation(queryDTO));
    	verify(messageDAO, times(1)).retrieveMessagesJustice(queryDTO);
    };
    
    @Test public void testretrieveLeaves(){
    	BusinessDomain businessDomain = new BusinessDomain(); 
    	Transaction transaction = new Transaction(); 
    	Date startDate = Calendar.getInstance().getTime(); 
    	Date endDate = Calendar.getInstance().getTime(); 
    	Set<Transaction> childTransactionsToIgnore = null;
    	
    	when(messageDAO.retrieveLeaves(businessDomain, transaction, startDate, endDate, childTransactionsToIgnore)).thenReturn(new ArrayList<Message>(Arrays.asList(new Message(), new Message(), new Message())));
    	assertEquals(3, messageService.retrieveLeaves(businessDomain, transaction, startDate, endDate, childTransactionsToIgnore).size());
    	messageService.retrieveLeaves(businessDomain, transaction, startDate, endDate, childTransactionsToIgnore);
    	verify(messageDAO, times(2)).retrieveLeaves(businessDomain, transaction, startDate, endDate, childTransactionsToIgnore);
    };
    
    @Test public void testRetrieveOrphans(){
    	BusinessDomain businessDomain = new BusinessDomain(); 
    	Transaction transaction = new Transaction(); 
    	Date startDate = Calendar.getInstance().getTime(); 
    	Date endDate = Calendar.getInstance().getTime(); 
    	
    	when(messageDAO.retrieveOrphans(businessDomain, transaction, startDate, endDate)).thenReturn(new ArrayList<Message>(Arrays.asList(new Message(), new Message(), new Message())));
    	assertEquals(3, messageService.retrieveOrphans(businessDomain, transaction, startDate, endDate).size());
    	messageService.retrieveOrphans(businessDomain, transaction, startDate, endDate);
    	verify(messageDAO, times(2)).retrieveOrphans(businessDomain, transaction, startDate, endDate);
    };
    
    @Test public void testGetStatusCodes(){
    	when(messageDAO.getStatusCodes()).thenReturn(new ArrayList<String>(Arrays.asList("A","B","C")));
    	assertEquals(3, messageService.getStatusCodes().size());
    	verify(messageDAO, times(1)).getStatusCodes();
    };
    
    @Test public void testCreateMessageBinary() throws UndefinedIdentifierException{
    	//Sender Party
    	Party sender = new Party();
    	sender.setId(1L);
    	//Issuer Party
    	Party issuer = new Party();
    	issuer.setId(2L);
    	issuer.setBusinessDomain(new BusinessDomain());
    	//Policy DTO
    	PolicySearchDTO policySearchDTO = new PolicySearchDTO();
		policySearchDTO.setSender(sender);
		policySearchDTO.setPolicyType(VolumeSlaPolicy.class);
    	//Policy
		VolumeSlaPolicy vPolicy = new VolumeSlaPolicy();
		vPolicy.setFrequency(FrequencyType.DAILY);
		vPolicy.setValue((int)100);

		CreateMessageBinaryDTO createMessageBinaryDTO = new CreateMessageBinaryDTO.Builder()
    			.senderIdWithScheme("senderIdWithScheme")
    			.issuer(issuer)
    			.inputStream(getClass().getClassLoader().getResourceAsStream("dumbFile2.txt"))
    			.encryptBinary(false)
    			.useFileStore(false)
    			.build();
    	
    	when(authorisationService.getParty(createMessageBinaryDTO.getSenderIdWithScheme(), createMessageBinaryDTO.getIssuer().getBusinessDomain())).thenReturn(sender);
    	when(policyDAO.getPoliciesByCriteria(any(PolicySearchDTO.class))).thenReturn(new ArrayList<Policy>(Arrays.asList(vPolicy)));
    	when(logService.getVolumeCountForParty(any(Long.class), any(FrequencyType.class))).thenReturn(20000000000000L);
    	
    	try {
			messageService.createMessageBinary(createMessageBinaryDTO);
		} catch (MessageCreationException e) {
			assertEquals(ErrorResponseCode.SLA_VOLUME_EXCEEDED, e.getResponseCode());
		}
    	
    	when(policyDAO.getPoliciesByCriteria(any(PolicySearchDTO.class))).thenReturn(null);
    	
    	//when(logService.getVolumeCountForParty(policySearchDTO.getSender().getId(), vPolicy.getFrequency())).thenReturn(102L);
    	Map<MetaDataItemType, MetaDataItem> metadata = new HashMap<MetaDataItemType, MetaDataItem>();
    	MetaDataItem useFS = new MetaDataItem();
    	useFS.setValue("true");
    	metadata.put(MetaDataItemType.DEFAULT_USE_FILE_STORE, useFS);
    	MetaDataItem pathFS = new MetaDataItem();
    	pathFS.setValue("//");
    	metadata.put(MetaDataItemType.FILE_STORE_PATH, pathFS);
    	when(metadataService.retrieveMetaData((Long) null, null, null, null, null)).thenReturn(metadata);
    	
    	MessageBinary mb = new MessageBinary();
    	mb.setId(1L);
    	try {
			when(messageBinaryDAO.createMessageBinary(createMessageBinaryDTO)).thenReturn(mb);
		} catch (SQLException | BinaryStreamLimitExceededException | IOException e1) {}
    	try {
			messageService.createMessageBinary(createMessageBinaryDTO);
		} catch (MessageCreationException e) {e.printStackTrace();}
    };
    
    @Test public void testCreateMessage(){
    	CreateMessageDTO createMessageDTO = new CreateMessageDTO.Builder()
    			.transactionTypeId(1L)
    			.senderPartyId(1L)
    			.receiverPartyId(1L)
    			.issuerId(2L)
    			.icaId(1L)
    			.binaries(new ArrayList<MessageBinary>())
    			.parentMessageId(10L)
    			.build();
    	
    	Document doc = new Document();
    	doc.setDocumentTypeCode("DTC");
    	Transaction tx = new Transaction();
    	tx.setId(1L);
    	tx.setDocument(doc);
    	
    	when(authorisationService.getTransactionById(createMessageDTO.getTransactionTypeId())).thenReturn(tx);
    	
    	//Sender Party
    	Party sender = new Party();
    	sender.setId(1L);
    	//Issuer Party
    	Party issuer = new Party();
    	issuer.setId(2L);
    	issuer.setBusinessDomain(new BusinessDomain());
    	
    	//Map<MetaDataItemType, MetaDataItem> metadata = new Hash<MetaDataItemType, MetaDataItem>();
    	MetaDataItem md = new MetaDataItem();
    	md.setRawItemType(MetaDataItemType.OLD_FAULT_SUPPORT.name());
    	md.setValue("true");
    	
    	Message message = new Message();
    	message.setId(1L);
    	message.setBinaries(new HashSet<MessageBinary>());
    	
    	when(partyService.getParty(createMessageDTO.getIssuerId())).thenReturn(issuer);
    	when(partyService.getParty(createMessageDTO.getSenderPartyId())).thenReturn(sender);
    	when(metadataService.retrieveMetaData(createMessageDTO.getIcaId(), null, null, null, null)).thenReturn(new HashMap<MetaDataItemType, MetaDataItem>());
    	when(metadataService.getMetaDataItemsByCriteria(any(MetaDataItem.class))).thenReturn(new ArrayList<MetaDataItem>(Arrays.asList(md))); 
    	when(messageDAO.createMessage(any(Message.class))).thenReturn(message);
    	when(messageDAO.read(createMessageDTO.getParentMessageId())).thenReturn(new Message());
    	
    	try {
			messageService.createMessage(createMessageDTO);
		} catch (MessageCreationException e) {fail("The positive flow should work");}
    };
    
    @Test public void testDeleteMessage(){
    	Message message = new Message();
    	message.setId(1L);
    	
    	when(messageBinaryDAO.getFileSystemPath(message.getId())).thenReturn("//gfsfgdTEST");
    	try {
			messageService.deleteMessage(message);
		} catch (Exception e) {fail("Shouldn't Fail"+e.getMessage());}
    	
    	when(messageBinaryDAO.getFileSystemPath(message.getId())).thenThrow(new RuntimeException());
    	
    	try {
			messageService.deleteMessage(message);
			fail("Should not proceed");
		} catch (Exception e) {}
    };

    @Test public void testRetrieveMessagesForQueryRequest(){
    	RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO = new RetrieveMessagesForQueryRequestDTO.Builder()
    			.setEndDate(Calendar.getInstance().getTime())
    			.build();
    	Date initDate = retrieveMessagesDTO.getEndDate();
    	messageService.retrieveMessagesForQueryRequest(retrieveMessagesDTO);
    	assertNotEquals(initDate, retrieveMessagesDTO.getEndDate());
    	verify(messageDAO, times(1)).retrieveMessagesForQueryRequest(any(RetrieveMessagesForQueryRequestDTO.class));
    }
    
    @Test public void testUpdateMessageStatus(){
    	Long messageId = 1L;
    	String status = "RANDOM_STATUS";
    	Message m = new Message();
    	m.setId(messageId);
    	
    	when(messageDAO.read(messageId)).thenReturn(m);
    	try {
			messageService.updateMessageStatus(messageId, status);
		} catch (MessageUpdateException e) {}
    	
    	assertEquals(status, m.getStatusCode());
    	verify(messageDAO, times(1)).update(any(Message.class));
    }
    
    @Test public void testUpdateMessage(){
    	Long messageId = 1L;
    	String status = "RANDOM_STATUS";
    	String responseCode = "RANDOM_RESPONSE";
    	Message m = new Message();
    	m.setId(messageId);
    	
    	when(messageDAO.read(messageId)).thenReturn(m);
    	try {
			messageService.updateMessage(messageId, status, responseCode);
		} catch (MessageUpdateException e) {}
    	
    	assertEquals(status, m.getStatusCode());
    	assertEquals(responseCode, m.getResponseCode());
    	verify(messageDAO, times(1)).update(any(Message.class));
    }
    
    @Test public void testIsManaged(){
    	Message m = new Message();
    	m.setId(45L);
    	
    	when(messageDAO.isManaged(m)).thenReturn(true);
    	assertTrue(messageService.isManaged(m));
    	
    	when(messageDAO.isManaged(m)).thenReturn(false);
    	assertFalse(messageService.isManaged(m));
    };
    
    @Test public void testDetachMessageBinary(){
    	MessageBinary mb = new MessageBinary();
    	mb.setId(45L);
    	
    	messageService.detachMessageBinary(mb);
    	verify(messageBinaryDAO, times(1)).detachObject(mb);
    };
    
    @Test public void testCleanPhantomBinary(){
    	Long binId = 45L;
    	String corrId = "46";
    	
    	when(logService.getLogCorrelationId()).thenReturn(corrId);
    	messageService.cleanPhantomBinary(binId);
    	verify(logService, times(1)).removeBinarySizeInLogs(corrId);
    	verify(messageBinaryDAO, times(1)).deleteMessageBinary(binId);
    };
    
    @Test public void testCleanPhantomBinaryNullId(){
    	String corrId = "46";
    	when(logService.getLogCorrelationId()).thenReturn(corrId);
    	
    	messageService.cleanPhantomBinary(null);
    	verify(logService, times(1)).removeBinarySizeInLogs(corrId);
    	verify(messageBinaryDAO, times(0)).deleteMessageBinary(null);
    }
}
