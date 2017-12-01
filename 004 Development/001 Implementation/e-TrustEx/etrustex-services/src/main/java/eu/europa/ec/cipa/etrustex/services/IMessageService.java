package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.cipa.etrustex.services.dto.CreateMessageBinaryDTO;
import eu.europa.ec.cipa.etrustex.services.dto.CreateMessageDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageCreationException;
import eu.europa.ec.cipa.etrustex.services.exception.MessageRetrieveException;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.services.util.DispatchEnum;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import eu.europa.ec.cipa.etrustex.types.SortFieldTypeCode;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
	public void deleteMessageBinary(Long binaryId)throws MessageUpdateException;
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
	 * This method retrieves a {@link Message} using from the message document id, it's messageSenderId and message transaction ID
	 * 
	 * @param messageDocumentId
	 * @param messageSenderId
	 * @param messageTransactionTypeId
	 * @return
	 */
	public Message retrieveMessage(String messageDocumentId, Long messageSenderId,Long messageTransactionTypeId);
	
	/**
	 * This method retrieves a {@link Message} using from the message document id, it's documentTypeCode, it's messageSenderId and it's messageReceiverId
	 * 
	 * @param messageDocumentId
	 * @param messageSenderId
	 * @param messageTransactionTypeId
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
	public List<Message> retrieveMessages(Long receiverPartyId,Long senderPartyId,
			Long issuerPartyId, Long icaId, Set<Transaction> transactions,
			Integer maxResult,String messageDocumentId,String documentTypeCode,Boolean retrievedInd, Boolean fetchParents,Boolean fetchBinaries,Boolean filterOutMessagesInError);
	
	public List<Message> retrieveMessages(Set<Long> receiverPartyIds,Set<Long> senderPartyId,
			Long issuerPartyId, Set<Transaction> transactions,Set<String> documentTypeCodes,String documentId,Boolean retrievedInd,Date startDate,Date endDate,String correlationId,Integer maxResult, Boolean fetchParents,Boolean fetchBinaries,Boolean isSenderAlsoReceiver, Boolean filterOutMessagesInError, Boolean hasParents);
	
	
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
	public MessagesListVO retrieveMessagesWithConversation(Set<Long> receiverPartyIds,Set<Long> senderPartyId, Set<Long> receiverPartyIds2, Set<Long> senderPartyIds2,
			Long issuerPartyId, Set<Transaction> transactions,Set<String> documentTypeCodes,String documentId,Boolean retrievedInd,Date startDate,Date endDate,String correlationId,Integer maxResult, Boolean fetchParents,Boolean fetchBinaries,Boolean isSenderAlsoReceiver, String userId, Set<String> businessDocumentTypes, BigDecimal paginationFrom, BigDecimal paginationTo, SortFieldTypeCode sortField,Boolean filterOutMessagesInError);
	
	public InputStream getDecryptedStream(InputStream encryptedStream, byte[] iv) throws MessageRetrieveException;
	
	public InputStream getDecryptedStream(Long messageBinayId) throws MessageRetrieveException;
	
	
	public String getMessageBinaryAsString(Long messageId, MessageBinaryType type);
	public InputStream getMessageBinaryAsStream(Long messageBinaryId);
	
	public void updateMessageStatus(Long messageId, String statusCode) throws MessageUpdateException;
	
	public void updateMessage(Long messageId, String statusCode, String responseCode) throws MessageUpdateException;
		
	public Message updateMessage(Message message) throws MessageUpdateException;
	public MessageBinary updateMessageBinary(MessageBinary binary) throws MessageUpdateException;
	public void deleteMessages(List<Long> messages);
	
	public Message retrieveMessageWithInitializedCollections(Long messageId);
    public Message retrieveMessageWithInitializedProperties(Long messageId);
	public List<Message> findMessagesByTransaction(Long transactionId);
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
    
    Message retrieveMessageWithBinaries(Long messageId); 
}
