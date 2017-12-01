package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
@Entity
@Table(name="ETR_TB_MESSAGE")
public class Message implements Serializable {

	
	@Override
	public String toString() {
		return "Message [id=" + id + ", documentId=" + documentId
				+ ", correlationId=" + correlationId + ", binaries=" + binaries
				+ ", agreement=" + agreement + ", statusCode=" + statusCode
				+ ", receptionDate=" + receptionDate + ", retrieveIndicator="
				+ retrieveIndicator + ", issueDate=" + issueDate
				+ ", transaction=" + transaction + ", issuer=" + issuer
				+ ", sender=" + sender + ", additionalInfo=" + additionalInfo
				+ ", receiver=" + receiver + ", parentMessages="
				+ parentMessages + ", childMessages=" + childMessages
				+ ", messageDocumentTypeCode=" + messageDocumentTypeCode 
				+ ", responseCode=" + responseCode
				+ ", accessInfo=" + accessInfo + "]";
	}

	@Id
	@Column(name ="MSG_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name ="MSG_DOCUMENT_ID", nullable = false)
	private String documentId;
	
	@Column(name="MSG_CORRELATION_ID", nullable= true)
	private String correlationId;
	
	
	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL , fetch = FetchType.LAZY, orphanRemoval=true)
	private Set<MessageBinary> binaries =new HashSet<MessageBinary>();
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MSG_AGR_ID", unique=false, nullable=true,updatable=true)
	private InterchangeAgreement agreement;
	
	@Column(name="MSG_STS_CD")
	private String statusCode;
	
	@Column(name = "MSG_RECEIPT_DT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date receptionDate;
	
	@Column(name = "MSG_RETRIEVED_FL", nullable = true)
	private Boolean retrieveIndicator;

	@Column(name = "MSG_ISSUE_DT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date issueDate;

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MSG_TRANS_ID", unique=false, nullable=false,updatable=true)
	private Transaction transaction;

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MSG_ISSUER_ID", unique=false, nullable=false,updatable=true)
	private Party issuer;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MSG_SENDER_ID", unique=false, nullable=false,updatable=true)
	private Party sender;
	
	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	private Set<QueryResult> additionalInfo = new HashSet<QueryResult>();

	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MSG_RECEIVER_ID", unique=false, nullable=true,updatable=true)
	private Party receiver;
	
	@ManyToMany( cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "ETR_TB_MSG_MSG", 
		joinColumns = { @JoinColumn(name = "MSG_CHILD_ID", referencedColumnName = "MSG_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "MSG_PAR_ID", referencedColumnName = "MSG_ID") })
	private Set<Message> parentMessages= new HashSet<Message>();
	
	
	@ManyToMany(mappedBy = "parentMessages", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<Message> childMessages = new HashSet<Message>();

	@Column(name = "MSG_DOC_TYPE_CD", nullable = false)
	private String messageDocumentTypeCode;

	@Column(name = "MSG_RSP_CD")
	private String responseCode;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public Message(){
		super();
	}
	
	public Message(Long icaId, String documentId,
			String correlationId, String statusCode, Long issuerId,
			Long transactionTypeId, 
			String autheticatedUser, Date receptionDate,Date issueDate,Long receiverPartyId,Long senderPartyId, String responseCode){
		super();
		InterchangeAgreement agr = new InterchangeAgreement();
		agr.setId(icaId);
		this.setAgreement(agr);
		this.setDocumentId(documentId);
		this.setCorrelationId(correlationId);
		this.setStatusCode(statusCode);
		Party issuer = new Party();
		issuer.setId(issuerId);
		this.setIssuer(issuer);
		Transaction t = new Transaction();
		t.setId(transactionTypeId);
		this.setTransaction(t);
		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationDate(receptionDate);
		eai.setCreationId(autheticatedUser);
		eai.setModificationDate(receptionDate);
		eai.setModificationId(autheticatedUser);
		this.setAccessInfo(eai);
		this.setReceptionDate(receptionDate);
		this.setIssueDate(issueDate);
		Party sender = new Party();
		sender.setId(senderPartyId);
		this.setSender(sender);
		Party receiver = new Party();
		receiver.setId(receiverPartyId);
		this.setReceiver(receiver);
		this.setResponseCode(responseCode);
	}
	
	public Set<Message> getParentMessages() {
		return parentMessages;
	}
	
	public void addParentMessage(Message parent) {
		parentMessages.add(parent);
	}
	
	public void addMessageBinary(MessageBinary binary) {
		getBinaries().add(binary);
		binary.setMessage(this);
	}



	public Boolean getRetrieveIndicator() {
		return retrieveIndicator;
	}

	public void setRetrieveIndicator(Boolean retrieveIndicator) {
		this.retrieveIndicator = retrieveIndicator;
	}

	public void setParentMessages(Set<Message> parentMessages) {
		this.parentMessages = parentMessages;
	}

	
	public void addChildMessage(Message childMessage) {
		childMessages.add(childMessage);
	}

	

	public Set<Message> getChildMessages() {
		return childMessages;
	}


	public void setChildMessages(Set<Message> childMessages) {
		this.childMessages = childMessages;
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
	public String getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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


	public Party getIssuer() {
		return issuer;
	}


	public void setIssuer(Party issuer) {
		this.issuer = issuer;
	}


	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}


	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public Date getReceptionDate() {
		return receptionDate;
	}

	public void setReceptionDate(Date receptionDate) {
		this.receptionDate = receptionDate;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
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
	public String getMessageDocumentTypeCode() {
		return messageDocumentTypeCode;
	}

	public void setMessageDocumentTypeCode(String messageDocumentTypeCode) {
		this.messageDocumentTypeCode = messageDocumentTypeCode;
	}
	
	public Set<QueryResult> getAdditionalInfo() {
		return additionalInfo;
	}
	
	public Map<String,String> getAdditionalInfoAsMap() {
		Map<String,String> map = new HashMap<String,String>(); 
		for (QueryResult qr : getAdditionalInfo()) {
			map.put(qr.getKey(), qr.getValue());
		}
		
		
		return map;
	}
	
	public void setAdditionalInfo(Set<QueryResult> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}	
	
	@Transient
	public MessageBinary getMessageBinaryByBinaryType(MessageBinaryType binaryType) {
		for (MessageBinary binary : binaries) {
			if (binaryType.name().equals(binary.getBinaryType())) {
				return binary;
			}
		}
		return null;
	}
	
	@Transient
	public Message getChildMessageByDocumentTypeCode(String documentTypeCode) {
		for (Message message : childMessages) {
			if (documentTypeCode.equals(message.getMessageDocumentTypeCode())) {
				return message;
			}
		}
		return null;
	}
	
}
