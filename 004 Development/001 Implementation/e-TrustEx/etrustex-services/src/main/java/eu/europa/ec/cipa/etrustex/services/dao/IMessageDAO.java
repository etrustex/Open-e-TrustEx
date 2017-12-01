package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.cipa.etrustex.services.util.DispatchEnum;
import eu.europa.ec.cipa.etrustex.types.SortFieldTypeCode;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IMessageDAO extends ITrustExDAO<Message,Long> {
	
	/**
	 * this method retrieve a single message using the msg document id, the id of the message sender and the tansactionTypeId 
	 * @param messageDocumentId the external document id
	 * @param messageIcaId the ica id 
	 * @return the requested message
	 */
	public Message retrieveMessage(String messageDocumentId, Long senderId,Long transactionTypeId) ;
	 
	/**
	 * 
	 * @param message
	 * @return
	 * @throws SQLException
	 */
	public Message createMessage(Message message)  throws SQLException;
	
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
	 * @param filterOutMessagesInError bollean to indicate wheter the service should return messages in error state.
	 * @return
	 */
	public List<Message> retrieveMessages(Long receiverPartyId,Long senderPartyId,
			Long issuerPartyId, Long icaId, Set<Transaction> transactions,
			Integer maxResult,String messageDocumentId,String documentTypeCode,Boolean retrievedInd, Boolean fetchParents,Boolean fetchBinaries, Boolean filterOutMessagesInError);
	
	/**
	 * 
	 * This method queries the Message entity using the parameters as criteria. parameters can be null, is so they will not be included in the query. The method generates an and where clause from the secified parameters  
	 * @param receiverPartyIds a set of receiverPartyIds (in clause generated)
	 * @param senderPartyIds a set of senderPartyIds (in clause generated)
	 * @param issuerPartyId issuerPartyId the issuer party id
	 * @param transactions a set of transaction (in clause generated)
	 * @param documentTypeCodes a set of documentTypeCodes (in clause generated)
	 * @param retrievedInd the value of the retrieved indication flag use null if you want to retrieve messages regardless the value of the flag
	 * @param startDate start date and end date defines a period of time during wich the messages were recived
	 * @param endDate start date and end date defines a period of time during wich the messages were recived
	 * @param correlationId the message correlation Id
	 * @param maxResult the max number of results returned by the query
	 * @param fetchParents prefetch of the parents
	 * @param fetchBinaries prefetch of the binaries
	 * @param isSenderAlsoReceiver this flag is used to tell the method to query for messages where the message's sender is in the list of senderPartyIds or the message receiver is in the list of senderPartyIds
	 * @return
	 */
	public MessagesListVO retrieveMessagesJustice(Set<Long> receiverPartyIds,Set<Long> senderPartyIds, Set<Long> receiverPartyIds2,Set<Long> senderPartyIds2,
			Long issuerPartyId, Set<Transaction> transactions,Set<String> documentTypeCodes,String documentId,Boolean retrievedInd,Date startDate,Date endDate,Set<String> correlationIds,Integer maxResult, Boolean fetchParents,Boolean fetchBinaries,Boolean  isSenderAlsoReceiver, String userId, Set<String> businessDocumentType, BigDecimal paginationFrom, BigDecimal paginationTo, SortFieldTypeCode sortField,Boolean filterOutMessagesInError);
	
	public List<Message> retrieveMessages(Set<Long> receiverPartyIds,Set<Long> senderPartyIds,
			Set<Long> receiverPartyIds2, Set<Long> senderPartyIds2, Long issuerPartyId,
			Set<Transaction> transactions, Set<String> documentTypeCodes,String documentId,
			Boolean retrievedInd,Date startDate,Date endDate,Set<String> correlationIds, Integer maxResult, Boolean fetchParents,
			Boolean fetchBinaries, Boolean isSenderAlsoReceiver, Boolean filterOutMessagesInError, Boolean hasParents);
	
	/**
	 * 
	 * @param businessDomainId
	 * @param transaction
	 * @param startDate
	 * @param endDate
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
	public void deleteMessages(List<Long> messages);

	/**
	 * 
	 * @param messages
	 */
	public List<Message> findMessagesByTransaction(Long transactionId);
	
//	public Set<String> retrieveConversationIds(Set<Long> receiverPartyIds,Set<Long> senderPartyIds, Set<Long> receiverPartyIds2,Set<Long> senderPartyIds2,
//			Long issuerPartyId, Set<Transaction> transactions,Set<String> documentTypeCodes,String documentId,Boolean retrievedInd,Date startDate,Date endDate,Set<String> correlationIds,Integer maxResult, Boolean fetchParents,Boolean fetchBinaries,Boolean  isSenderAlsoReceiver, String userId, Set<String> businessDocumentType, BigDecimal paginationFrom, BigDecimal paginationTo, SortFieldTypeCode sortField,Boolean filterOutMessagesInError);

    public List<String> getDocumentTypeCodes();
    public List<String> getStatusCodes();
    public List<Message> findMessagesByCriteria(Message message, Date createdFrom, Date createdTo, int firstResult, int maxResults, List<Long> businessDomainIds, DispatchEnum dispatched);
    public long countMessagesByCriteria(Message message, Date createdFrom, Date createdTo, List<Long> businessDomainIds, DispatchEnum dispatched);
}
