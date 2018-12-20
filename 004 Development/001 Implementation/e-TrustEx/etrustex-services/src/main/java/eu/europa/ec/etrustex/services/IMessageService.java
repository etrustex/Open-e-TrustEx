package eu.europa.ec.etrustex.services;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import eu.europa.ec.etrustex.dao.dto.CreateMessageBinaryDTO;
import eu.europa.ec.etrustex.dao.dto.CreateMessageDTO;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.dao.exception.MessageRetrieveException;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.etrustex.types.MessageBinaryType;

public interface IMessageService {
	
	//TODO ode add Javadoc
	public void reloadFileStoreParameters(boolean forceReload) throws MessageCreationException;
	
	/**
	 *  This method is used to store binaries that are streame as inpus stream to the platform, This is used to store potnetially large binary files
	 *  this method can be configured to store binarie into the file system or into the database (Not recommended) 
	 * @param binaryType the type of the binary ("Business type of the binary" set by the binary submitter)
	 * @param mimeType the miem type of the binary
	 * @param inputStream the stream itself
	 * @param encryptBinary boolean indicating wether to encrypt the binary or not
	 * @param senderIsdWithScheme the binary submitter id this is use for creating folder on the file store to help sorting binaries by sender. this will help
	 * 		  to compute statistics on the storage useage by party	 
	 * * @return
	 * @throws MessageCreationException
	 */
	public Long createMessageBinary(CreateMessageBinaryDTO createMessageBinaryDTO) throws MessageCreationException;
	/**
	 * method used to delete a message binary
	 * @param binaryId
	 * @throws MessageUpdateException
	 */
	public void deleteMessageBinary(Long binaryId);
	/**
	 * Method used to delete one specific message
	 * @param message
	 * @throws MessageUpdateException
	 */
	public void deleteMessage(Message message)throws MessageUpdateException;
	

	public Long createMessage(CreateMessageDTO createMessageDTO) throws MessageCreationException;
	
	/**
	 * This method retrieves a {@link Message} using it's primary key, the message id
	 * @param messageId
	 * @return
	 */
	public Message retrieveMessage(Long messageId) ;
	
	/**
	 * Retrieves the MessageBinary with the given id 
	 * @param messageBinaryId
	 * @return
	 */
	public MessageBinary retrieveMessageBinary(Long messageBinaryId);
	
	/**
	 * This method retrieves a {@link Message} using from the message document id, sender Id & Receiver Id and documentTypeCode
	 * 
	 * @param messageDocumentId
	 * @param documentTypeCode
	 * @param senderId
	 * @param receiverId
	 * @param biDirectional
	 * @param statesToExclude
	 * @return
	 */
	public Message retrieveMessage(String messageDocumentId, String documentTypeCode,Long senderId, Long receiverId, Boolean biDirectional, Set<String> statesToExclude);
	
	/**
	 * This method retrieves a {@link Message} using from the message document id, it's documentTypeCode, it's messageSenderId and it's messageReceiverId
	 * @param messageDocumentId
	 * @param documentTypeCode
	 * @param messageSenderId
	 * @param messagereceiverId
	 * @return
	 */
	public Message retrieveMessage(String messageDocumentId, String documentTypeCode, Long messageSenderId, Long messagereceiverId);
	
	/**
	 * Thuis methods builds a query on the message entity from the passed parametres, the method generates an "and" clause from the parametres. Passing null value to one of the parm
	 * 
	 * @param receiverPartyId
	 * @param senderPartyId
	 * @param issuerPartyId
	 * @param icaId
	 * @param transactions
	 * @param maxResult
	 * @param messageDocumentId
	 * @param documentTypeCode
	 * @param retrievedInd
	 * @param fetchParents
	 * @param fetchBinaries
	 * @param filterOutMessagesInError
	 * @return
	 */
	public List<Message> retrieveMessages(MessageQueryDTO messageDTO);
	
	public List<Message> retrieveMessagesForQueryRequest(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO);
	
	
	/**
	 * Method used to retrieve bundle leaves 
	 */
	public List<Message> retrieveLeaves(BusinessDomain businessDomain, Transaction transaction, Date startDate,Date endDate, Set<Transaction> childTransactionsToIgnore);
	
	/**
	 * 
	 * @param businessDomain
	 * @param transaction
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Message> retrieveOrphans(BusinessDomain businessDomain, Transaction transaction, Date startDate,Date endDate);
	
	/**
	 * Method specific to query Justice that does group sorting (Correlation then criteria) and pulls the conversation in the case where 
	 * Date range is specified 
	 * 
	 * @return
	 */
	public MessagesListVO retrieveMessagesWithConversation(RetrieveMessagesForQueryRequestDTO queryDTO);
	
	public InputStream getDecryptedStream(InputStream encryptedStream, byte[] iv) throws MessageRetrieveException;

	public String getMessageBinaryAsString(Long messageId, MessageBinaryType type);
	public InputStream getMessageBinaryAsStream(Long messageBinaryId);
	
	public void updateMessageStatus(Long messageId, String statusCode) throws MessageUpdateException;
	
	public void updateMessage(Long messageId, String statusCode, String responseCode) throws MessageUpdateException;
		
	public Message updateMessage(Message message) throws MessageUpdateException;
	public MessageBinary updateMessageBinary(MessageBinary binary) throws MessageUpdateException;
	
	public Message retrieveMessageWithInitializedProperties(Long messageId, boolean initCollections);
	public long findMessagesByTransaction(Long transactionId);
    public List<String> getDocumentTypeCodes();
    public List<String> getStatusCodes();
    public List<Message> findMessagesByCriteria(Message message, Date createdFrom, Date createdTo, int firstResult, int maxResults, List<Long> businessDomainIds, DispatchEnum dispatched);
    public long countMessagesByCriteria(Message message, Date createdFrom, Date createdTo, List<Long> businessDomainIds, DispatchEnum dispatched);
    /**
     * detaches the message with the given id from all its parents
     * @param messageId
     */
    public void detachParent(Long messageId);
    
    /**
     * Checks if the argument is an entity managed by the current persistence context
     * @param object
     * @return
     */
    public boolean isManaged(Message message);
    
    /**
     * Returns the message count per document ID
     * @param messageDocumentId
     * @return
     */
    public long findMessagesByDocumentId(String messageDocumentId);

    public boolean isInterchangeAgreementUsedToSendMessages(InterchangeAgreement interchangeAgreement);
    
	/**
	 * retrieve the binary content as input stream
	 * @param messageId
	 * @param binaryType
	 * @return
	 */
    public InputStream getMessageBinaryAsStream(Long messageId, MessageBinaryType binaryType);
    
	/**
	 * retrieve message binary by message id and type
	 * @param messageId
	 * @param binaryType
	 * @return
	 */
	public MessageBinary getMessageBinary(Long messageId, String binaryType);    
    
    /**
     * detaches the message binary from the JPA persistence context
     * @param binary the binary
     */
    public void detachMessageBinary(MessageBinary binary);
    
    /**
     * This utility method is used to compensate any error occuring during the storing of the
	 * wrapper metadata. the system will store first the binary stream and the
	 * the metadata of the wrapper. The storing of the binary can be non
	 * transaction when the platform is configured with binary storage into
	 * files. so we need this method to delete the binary if an error occurs
	 * while storing the metadata.
	 * 
     * @param binaryId
     */
    public void cleanPhantomBinary(Long binaryId);
    
    /**
     * check that an eJustice message with the same correlation ID from a different user exists
     * @param retrieveMessagesDTO
     * @return true if such a message exists, false otherwise
     */
    public boolean existsEjusticeMessageForCorrelationId(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO);
    
    /**
     * get the number of messages matching the given criteria
     * @param messageDTO the bean containing the criteria
     * @return the number of messages found in the DB
     */
    public long getMessageCount(MessageQueryDTO messageDTO);    
}
