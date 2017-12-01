/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;
import java.util.Date;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;

public class LogsSearchForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id_search;
	private String logType_search;
	private String operation_search;
	private String description_search;
	private String value_search;
	private String largeValue_search;
	private String urlContext_search;
	private String correlationId_search;
	private String documentId_search;
	private String documentTypeCode_search;
	private String businessCorrelationId_search;
	private Transaction transaction_search;
	private Long messageId_search;
	private Long messageBinaryId_search;
	private Long messageSize_search;
	private Party issuerParty_search;
	private Party senderParty_search;
	private Party receiverParty_search;
	private BusinessDomain businessDomain_search;
	private String module_search;
	private String username_search;
	private String userRole_search;
	private String authIpAddress_search;
	private String entity_search;
	private Long entityId_search;
	private boolean reinitializeCorrelationId_search;
	private Date creationDateFrom;
	private Date creationDateTo;
	
	public LogsSearchForm() { }


	public Long getId_search() {
		return id_search;
	}


	public void setId_search(Long id_search) {
		this.id_search = id_search;
	}


	public String getLogType_search() {
		return logType_search;
	}


	public void setLogType_search(String logType_search) {
		this.logType_search = logType_search;
	}


	public String getOperation_search() {
		return operation_search;
	}


	public void setOperation_search(String operation_search) {
		this.operation_search = operation_search;
	}


	public String getDescription_search() {
		return description_search;
	}


	public void setDescription_search(String description_search) {
		this.description_search = description_search;
	}


	public String getValue_search() {
		return value_search;
	}


	public void setValue_search(String value_search) {
		this.value_search = value_search;
	}


	public String getLargeValue_search() {
		return largeValue_search;
	}


	public void setLargeValue_search(String largeValue_search) {
		this.largeValue_search = largeValue_search;
	}


	public String getUrlContext_search() {
		return urlContext_search;
	}


	public void setUrlContext_search(String urlContext_search) {
		this.urlContext_search = urlContext_search;
	}


	public String getCorrelationId_search() {
		return correlationId_search;
	}


	public void setCorrelationId_search(String correlationId_search) {
		this.correlationId_search = correlationId_search;
	}


	public String getDocumentId_search() {
		return documentId_search;
	}


	public void setDocumentId_search(String documentId_search) {
		this.documentId_search = documentId_search;
	}


	public String getDocumentTypeCode_search() {
		return documentTypeCode_search;
	}


	public void setDocumentTypeCode_search(String documentTypeCode_search) {
		this.documentTypeCode_search = documentTypeCode_search;
	}


	public String getBusinessCorrelationId_search() {
		return businessCorrelationId_search;
	}


	public void setBusinessCorrelationId_search(String businessCorrelationId_search) {
		this.businessCorrelationId_search = businessCorrelationId_search;
	}


	public Transaction getTransaction_search() {
		return transaction_search;
	}


	public void setTransaction_search(Transaction transaction_search) {
		this.transaction_search = transaction_search;
	}


	public Long getMessageId_search() {
		return messageId_search;
	}


	public void setMessageId_search(Long messageId_search) {
		this.messageId_search = messageId_search;
	}


	public Long getMessageBinaryId_search() {
		return messageBinaryId_search;
	}


	public void setMessageBinaryId_search(Long messageBinaryId_search) {
		this.messageBinaryId_search = messageBinaryId_search;
	}


	public Long getMessageSize_search() {
		return messageSize_search;
	}


	public void setMessageSize_search(Long messageSize_search) {
		this.messageSize_search = messageSize_search;
	}


	public Party getIssuerParty_search() {
		return issuerParty_search;
	}


	public void setIssuerParty_search(Party issuerParty_search) {
		this.issuerParty_search = issuerParty_search;
	}


	public Party getSenderParty_search() {
		return senderParty_search;
	}


	public void setSenderParty_search(Party senderParty_search) {
		this.senderParty_search = senderParty_search;
	}


	public Party getReceiverParty_search() {
		return receiverParty_search;
	}


	public void setReceiverParty_search(Party receiverParty_search) {
		this.receiverParty_search = receiverParty_search;
	}


	public BusinessDomain getBusinessDomain_search() {
		return businessDomain_search;
	}


	public void setBusinessDomain_search(BusinessDomain businessDomain_search) {
		this.businessDomain_search = businessDomain_search;
	}


	public String getModule_search() {
		return module_search;
	}


	public void setModule_search(String module_search) {
		this.module_search = module_search;
	}


	public String getUsername_search() {
		return username_search;
	}


	public void setUsername_search(String username_search) {
		this.username_search = username_search;
	}


	public String getUserRole_search() {
		return userRole_search;
	}


	public void setUserRole_search(String userRole_search) {
		this.userRole_search = userRole_search;
	}


	public String getAuthIpAddress_search() {
		return authIpAddress_search;
	}


	public void setAuthIpAddress_search(String authIpAddress_search) {
		this.authIpAddress_search = authIpAddress_search;
	}


	public String getEntity_search() {
		return entity_search;
	}


	public void setEntity_search(String entity_search) {
		this.entity_search = entity_search;
	}


	public Long getEntityId_search() {
		return entityId_search;
	}


	public void setEntityId_search(Long entityId_search) {
		this.entityId_search = entityId_search;
	}


	public boolean isReinitializeCorrelationId_search() {
		return reinitializeCorrelationId_search;
	}


	public void setReinitializeCorrelationId_search(
			boolean reinitializeCorrelationId_search) {
		this.reinitializeCorrelationId_search = reinitializeCorrelationId_search;
	}


	public Date getCreationDateFrom() {
		return creationDateFrom;
	}


	public void setCreationDateFrom(Date creationDateFrom) {
		this.creationDateFrom = creationDateFrom;
	}


	public Date getCreationDateTo() {
		return creationDateTo;
	}


	public void setCreationDateTo(Date creationDateTo) {
		this.creationDateTo = creationDateTo;
	}
}