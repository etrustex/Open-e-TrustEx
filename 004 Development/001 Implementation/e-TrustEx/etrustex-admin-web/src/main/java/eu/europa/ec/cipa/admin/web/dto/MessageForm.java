/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.util.DispatchEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MessageForm implements Serializable {
	private static final long serialVersionUID = 1L;

    private Long id;
    private String documentId;
    private String correlationId;
    private Set<MessageBinary> binaries =new HashSet<MessageBinary>();
    private InterchangeAgreement agreement;
    private String statusCode;
    private Date receptionDate;
    private Boolean retrieveIndicator;
    private Date issueDate;
    private Transaction transaction;
    private Party issuer;
    private Party sender;
    private Set<QueryResult> additionalInfo = new HashSet<QueryResult>();
    private Party receiver;
    private Set<Message> parentMessages= new HashSet<Message>();
    private Set<Message> childMessages = new HashSet<Message>();
    private String messageDocumentTypeCode;
    private String responseCode;
    private EntityAccessInfo accessInfo;
    private DispatchEnum dispatched; // Dispatched

    public MessageForm() { }

    public MessageForm(Message message, DispatchEnum dispatched) {
        if (message != null) {
            this.id = message.getId();
            this.documentId = message.getDocumentId();
            this.correlationId = message.getCorrelationId();
            this.binaries = message.getBinaries();
            this.agreement = message.getAgreement();
            this.statusCode = message.getStatusCode();
            this.receptionDate = message.getReceptionDate();
            this.retrieveIndicator = message.getRetrieveIndicator();
            this.issueDate = message.getIssueDate();
            this.transaction = message.getTransaction();
            this.issuer = message.getIssuer();
            this.sender = message.getSender();
            this.additionalInfo = message.getAdditionalInfo();
            this.receiver = message.getReceiver();
            this.parentMessages = message.getParentMessages();
            this.childMessages = message.getChildMessages();
            this.messageDocumentTypeCode = message.getMessageDocumentTypeCode();
            this.responseCode = message.getResponseCode();
            this.accessInfo = message.getAccessInfo();
            this.dispatched = dispatched;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Set<MessageBinary> getBinaries() {
        return binaries;
    }

    public void setBinaries(Set<MessageBinary> binaries) {
        this.binaries = binaries;
    }

    public InterchangeAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(InterchangeAgreement agreement) {
        this.agreement = agreement;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    public Boolean getRetrieveIndicator() {
        return retrieveIndicator;
    }

    public void setRetrieveIndicator(Boolean retrieveIndicator) {
        this.retrieveIndicator = retrieveIndicator;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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

    public Set<QueryResult> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Set<QueryResult> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Party getReceiver() {
        return receiver;
    }

    public void setReceiver(Party receiver) {
        this.receiver = receiver;
    }

    public Set<Message> getParentMessages() {
        return parentMessages;
    }

    public void setParentMessages(Set<Message> parentMessages) {
        this.parentMessages = parentMessages;
    }

    public Set<Message> getChildMessages() {
        return childMessages;
    }

    public void setChildMessages(Set<Message> childMessages) {
        this.childMessages = childMessages;
    }

    public String getMessageDocumentTypeCode() {
        return messageDocumentTypeCode;
    }

    public void setMessageDocumentTypeCode(String messageDocumentTypeCode) {
        this.messageDocumentTypeCode = messageDocumentTypeCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public EntityAccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(EntityAccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }

    public DispatchEnum getDispatched() {
        return dispatched;
    }

    public void setDispatched(DispatchEnum dispatched) {
        this.dispatched = dispatched;
    }
}