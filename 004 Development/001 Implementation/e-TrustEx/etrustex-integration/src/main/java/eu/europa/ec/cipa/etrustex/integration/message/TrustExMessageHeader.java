package eu.europa.ec.cipa.etrustex.integration.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPMessage;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.util.JmsHeaders;
import eu.europa.ec.cipa.etrustex.integration.util.MessageHeaders;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import eu.europa.ec.cipa.etrustex.types.RedeliveryReasonType;

public class TrustExMessageHeader implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String authenticatedUser;

	private String messageDocumentId;

	private String messageDocumentVersion;

	private Long parentMessageId;// internal Id
	
	private String parentStatusCode;
	
	private String messageStatusCode;
	
	private String messageParentDocumentId;

	private String messageParentDocumentTypeCode;
	
	private String correlationId;

	private String senderIdWithScheme;

	private String receiverIdWithScheme;

	private String transactionNamespace;

	private String transactionRequestLocalName;

	private Date receivedDate;

	private Date issueDate;

	private Long messageId;

	private Long senderPartyId;

	private Long receiverPartyId;

	private Long transactionTypeId;

	private Long documentTypeId;

	private Long issuerPartyId;

	private Long interchangeAgreementId;

	private Map<MetaDataItemType, MetaDataItem> metadata;

	private String schematronResult;
	
	private Boolean messageIsRedelivered= Boolean.FALSE;
	
	private RedeliveryReasonType redeliveryReason;
	
	private String replyTo;
	
	private String instanceIdentifier;
	
	private String responseCode;
	
	private String availableNotification;
	
	private String logCorrelationId;
	
	private String messageRoutingId;
	
	private String rawHeader;
		
	private transient MessageHeaders messageHeaders;
	
	private transient BusinessException businessException;
	
	private Party issuer;
	private Party sender;
	//private Party receiver;
	
	private boolean multicastSupported = false;
	private List<String> receiverIdWithSchemeList = new ArrayList<String>();
	private Map<String,Long> receiverIdByreceiverIdWithScheme = new HashMap<String, Long>();
	private Map<String, Long> interchangeAgreementIdByreceiverIdWithScheme = new HashMap<String, Long>();

	private SOAPMessage soapMessage;
	
	public Map<String,Long> getReceiverIdByreceiverIdWithScheme() {
		return receiverIdByreceiverIdWithScheme;
	}

	public Map<String, Long> getInterchangeAgreementIdByreceiverIdWithScheme() {
		return interchangeAgreementIdByreceiverIdWithScheme;
	}

	public List<String> getReceiverIdWithSchemeList() {
		return receiverIdWithSchemeList;
	}

	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	public void setInstanceIdentifier(String instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public RedeliveryReasonType getRedeliveryReason() {
		return redeliveryReason;
	}

	public void setRedeliveryReason(RedeliveryReasonType redeliveryReason) {
		this.redeliveryReason = redeliveryReason;
	}

	public Boolean getMessageIsRedelivered() {
		return messageIsRedelivered;
	}

	public void setMessageIsRedelivered(Boolean messageIsRedelivered) {
		this.messageIsRedelivered = messageIsRedelivered;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getSchematronResult() {
		return schematronResult;
	}

	public void setSchematronResult(String schematronResult) {
		this.schematronResult = schematronResult;
	}

	public Map<MetaDataItemType, MetaDataItem> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<MetaDataItemType, MetaDataItem> metadata) {
		this.metadata = metadata;
	}

	public Long getInterchangeAgreementId() {
		return interchangeAgreementId;
	}

	public void setInterchangeAgreementId(Long interchangeAgreementId) {
		this.interchangeAgreementId = interchangeAgreementId;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getAuthenticatedUser() {
		return authenticatedUser;
	}

	public void setAuthenticatedUser(String authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}

	public String getMessageDocumentId() {
		return messageDocumentId;
	}

	public void setMessageDocumentId(String messageDocumentId) {
		this.messageDocumentId = messageDocumentId;
	}

	public String getSenderIdWithScheme() {
		return senderIdWithScheme;
	}

	public void setSenderIdWithScheme(String senderIdWithScheme) {
		this.senderIdWithScheme = senderIdWithScheme;
	}

	public String getReceiverIdWithScheme() {
		return receiverIdWithScheme;
	}

	public void setReceiverIdWithScheme(String receiverIdWithScheme) {
		this.receiverIdWithScheme = receiverIdWithScheme;
	}

	public Long getIssuerPartyId() {
		return issuerPartyId;
	}

	public void setIssuerPartyId(Long issuerPartyId) {
		this.issuerPartyId = issuerPartyId;
	}

	public Long getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Long tansactionTypeId) {
		this.transactionTypeId = tansactionTypeId;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getMessageParentDocumentId() {
		return messageParentDocumentId;
	}

	public void setMessageParentDocumentId(String messageParentDocumentId) {
		this.messageParentDocumentId = messageParentDocumentId;
	}

	public String getMessageDocumentVersion() {
		return messageDocumentVersion;
	}

	public void setMessageDocumentVersion(String messageDocumentVersion) {
		this.messageDocumentVersion = messageDocumentVersion;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getSenderPartyId() {
		return senderPartyId;
	}

	public void setSenderPartyId(Long senderPartyId) {
		this.senderPartyId = senderPartyId;
	}

	public Long getReceiverPartyId() {
		return receiverPartyId;
	}

	public void setReceiverPartyId(Long receiverPartyId) {
		this.receiverPartyId = receiverPartyId;
	}

	public String getTransactionNamespace() {
		return transactionNamespace;
	}

	public void setTransactionNamespace(String transactionNamespace) {
		this.transactionNamespace = transactionNamespace;
	}

	public String getTransactionRequestLocalName() {
		return transactionRequestLocalName;
	}

	public void setTransactionRequestLocalName(
			String transactionRequestLocalName) {
		this.transactionRequestLocalName = transactionRequestLocalName;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessageParentDocumentTypeCode() {
		return messageParentDocumentTypeCode;
	}

	public void setMessageParentDocumentTypeCode(
			String messageParentDocumentTypeCode) {
		this.messageParentDocumentTypeCode = messageParentDocumentTypeCode;
	}

	public String getAvailableNotification() {
		return availableNotification;
	}

	public void setAvailableNotification(String availableNotification) {
		this.availableNotification = availableNotification;
	}

	public Long getParentMessageId() {
		return parentMessageId;
	}

	public void setParentMessageId(Long parentMessageId) {
		this.parentMessageId = parentMessageId;
	}

	public String getParentStatusCode() {
		return parentStatusCode;
	}

	public void setParentStatusCode(String parentStatusCode) {
		this.parentStatusCode = parentStatusCode;
	}

	public String getMessageStatusCode() {
		return messageStatusCode;
	}

	public void setMessageStatusCode(String messageStatusCode) {
		this.messageStatusCode = messageStatusCode;
	}

	public String getLogCorrelationId() {
		return logCorrelationId;
	}

	public void setLogCorrelationId(String logCorrelationId) {
		this.logCorrelationId = logCorrelationId;
	}

	public String getMessageRoutingId() {
		return messageRoutingId;
	}

	public void setMessageRoutingId(String messageRoutingId) {
		this.messageRoutingId = messageRoutingId;
	}

	public MessageHeaders getMessageHeaders() {
		if (messageHeaders == null) {
			messageHeaders = new JmsHeaders();
		}	
		return messageHeaders;
	}

	public void setMessageHeaders(MessageHeaders jmsHeaders) {
		this.messageHeaders = jmsHeaders;
	}

	public BusinessException getBusinessException() {
		return businessException;
	}

	public void setBusinessException(BusinessException businessException) {
		this.businessException = businessException;
	}

	public Party getIssuer() {
		return issuer;
	}

	public void setIssuer(Party issuer) {
		this.issuer = issuer;
	}

	public Party getSender() {
		return sender;
	}

	public void setSender(Party sender) {
		this.sender = sender;
	}

//	public Party getReceiver() {
//		return receiver;
//	}

//	public void setReceiver(Party receiver) {
//		this.receiver = receiver;
//	}

	public String getRawHeader() {
		return rawHeader;
	}

	public void setRawHeader(String rawHeader) {
		this.rawHeader = rawHeader;
	}

	public boolean isMulticastSupported() {
		return multicastSupported;
	}

	public void setMulticastSupported(boolean multicast) {
		this.multicastSupported = multicast;
	}
	
	public SOAPMessage getSoapMessage() {
		return soapMessage;
	}
	public void setSoapMessage(SOAPMessage soapMessage) {
		this.soapMessage = soapMessage;
	}
	
}
