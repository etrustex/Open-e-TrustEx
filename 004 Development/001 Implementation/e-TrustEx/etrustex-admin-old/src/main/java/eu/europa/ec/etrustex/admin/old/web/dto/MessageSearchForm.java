/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

import java.io.Serializable;
import java.util.Date;

public class MessageSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private BusinessDomain businessDomain;
    private String creationId;
    private Date creationDateFrom;
    private Date creationDateTo;
    private String correlationId;
    private String documentId;
    private String messageDocumentTypeCode;
    private DispatchEnum dispatched; // Dispatched
    private Boolean retrieveIndicator;
    private Party issuer;
    private Party sender;
    private Party receiver;
    private InterchangeAgreement agreement;
    private Transaction transaction;
    private String statusCode;
    private Boolean searchOnLoad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusinessDomain getBusinessDomain() {
        return businessDomain;
    }

    public void setBusinessDomain(BusinessDomain businessDomain) {
        this.businessDomain = businessDomain;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMessageDocumentTypeCode() {
        return messageDocumentTypeCode;
    }

    public void setMessageDocumentTypeCode(String messageDocumentTypeCode) {
        this.messageDocumentTypeCode = messageDocumentTypeCode;
    }

    public DispatchEnum getDispatched() {
        return dispatched;
    }

    public void setDispatched(DispatchEnum dispatched) {
        this.dispatched = dispatched;
    }

    public Boolean getRetrieveIndicator() {
        return retrieveIndicator;
    }

    public void setRetrieveIndicator(Boolean retrieveIndicator) {
        this.retrieveIndicator = retrieveIndicator;
    }

    public Party getIssuer() {
        return issuer;
    }

    public void setIssuer(Party issuer) {
        this.issuer = issuer;
    }

    public String getCreationId() {
        return creationId;
    }

    public void setCreationId(String creationId) {
        this.creationId = creationId;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Party getSender() {
        return sender;
    }

    public void setSender(Party sender) {
        this.sender = sender;
    }

    public Party getReceiver() {
        return receiver;
    }

    public void setReceiver(Party receiver) {
        this.receiver = receiver;
    }

    public InterchangeAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(InterchangeAgreement agreement) {
        this.agreement = agreement;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }
}