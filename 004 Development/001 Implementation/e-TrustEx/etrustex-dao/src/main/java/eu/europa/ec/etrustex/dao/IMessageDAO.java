package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MessagesListVO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IMessageDAO extends ITrustExDAO<Message,Long> {

    long findMessagesByDocumentId(String messageDocumentId);

	/**
	 * 
	 * @param message
	 * @return
	 * @throws SQLException
	 */
    public Message createMessage(Message message);
	
	/**
	 * Method used to retrieve Messages based on docId, doc TypeCode, SenderId, Receiver Id
	 * @param messageDocumentId
	 * @param documentTypeCode
	 * @param senderId
	 * @param receiverId
	 * @return one single message since the given parameters are the content of a unique index
	 */
	public Message retrieveMessage(String messageDocumentId, String documentTypeCode, Long senderId, Long receiverId);
	
	/**
	 * Method used to retrieve Messages based on docId, doc TypeCode, SenderId, Receiver Id
	 * @param messageDocumentId
	 * @param documentTypeCode
	 * @param senderId
	 * @param receiverId
	 * @param biDirectional If false then Sender->Receiver, if true then Sender->Receiver and Receiver->Sender
	 * @param statesToExclude states that need to be excluded during the search
	 * @return
	 */
	public List<Message> retrieveMessages(String messageDocumentId, String documentTypeCode, Long senderId, Long receiverId, Boolean biDirectional, Set<String> statesToExclude);
	
	
	
	/**
	 * This method queries the Message entity using the parameters as criteria. parameters can be null, is so they will not be included in the query. The method generates an and where clause from the secified parameters
	 * @param receiverPartyId the receiver party id
	 * @param senderPartyId the sender party id
	 * @param issuerPartyId the issuer party id
	 * @param icaId the interchange agreement id
	 * @param transactions a set of transaction (in clause generated)
	 * @param maxResult the max number of results returned by the query
	 * @param messageDocumentId the message document id 
	 * @param documentTypeCode the document type code
	 * @param retrievedInd the value of the retrieved indication flag use null if you want to retrieve messages regardless the value of the flag
	 * @param fetchParents prefetch of the parents
	 * @param fetchBinaries prefetch of the binaries
	 * @param filterOutMessagesInError boolean to indicate wheter the service should return messages in error state.
	 * @param filterReadServices for read services this flag filter messages by issuer
	 * @return
	 */
	public List<Message> retrieveMessages(MessageQueryDTO messageDTO);
	
	/**
	 * 
	 * @param queryDTO
	 * @return
	 */
	public MessagesListVO retrieveMessagesJustice(RetrieveMessagesForQueryRequestDTO queryDTO);
	
	/**
	 * 
	 * @param retrieveMessagesDTO
	 * @return
	 */
	public List<Message> retrieveMessagesForQueryRequest(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO);
	
	/**
	 * 
	 * @param businessDomain
	 * @param transaction
	 * @param startDate
	 * @param endDate
	 * @param childTransactionsToIgnore
	 * @return
	 */
	public List<Message> retrieveLeaves(BusinessDomain businessDomain, Transaction transaction, Date startDate, Date endDate, Set<Transaction> childTransactionsToIgnore);
	
	/**
	 * 
	 * @param businessDomain
	 * @param transaction
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Message> retrieveOrphans(BusinessDomain businessDomain, Transaction transaction, Date startDate, Date endDate);

	/**
	 * 
	 * @param messages
	 */
	public long findMessagesByTransaction(Long transactionId);

	boolean isInterchangeAgreementUsedToSendMessages(InterchangeAgreement interchangeAgreement);
	
//	public Set<String> retrieveConversationIds(Set<Long> receiverPartyIds,Set<Long> senderPartyIds, Set<Long> receiverPartyIds2,Set<Long> senderPartyIds2,
//			Long issuerPartyId, Set<Transaction> transactions,Set<String> documentTypeCodes,String documentId,Boolean retrievedInd,Date startDate,Date endDate,Set<String> correlationIds,Integer maxResult, Boolean fetchParents,Boolean fetchBinaries,Boolean  isSenderAlsoReceiver, String userId, Set<String> businessDocumentType, BigDecimal paginationFrom, BigDecimal paginationTo, SortFieldTypeCode sortField,Boolean filterOutMessagesInError);

    public List<String> getDocumentTypeCodes();
    public List<String> getStatusCodes();
    public List<Message> findMessagesByCriteria(Message message, Date createdFrom, Date createdTo, int firstResult, int maxResults, List<Long> businessDomainIds, DispatchEnum dispatched);
    public long countMessagesByCriteria(Message message, Date createdFrom, Date createdTo, List<Long> businessDomainIds, DispatchEnum dispatched);

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
