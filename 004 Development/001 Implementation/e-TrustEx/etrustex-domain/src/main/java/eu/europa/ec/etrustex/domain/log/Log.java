/**
 * 
 */
package eu.europa.ec.etrustex.domain.log;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.types.LogModuleEnum;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author chiricr
 *
 */
@Entity
@Table(name="ETR_TB_LOG")
public class Log implements Serializable {
	
	private static final long serialVersionUID = -532513250873452109L;
	
	@Id
	@Column(name ="LOG_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name="LOG_TYPE", nullable = false)
	private String logType;
	
	@Column(name="LOG_OPERATION")
	private String operation;

	@Column(name="LOG_DESCRIPTION")
	private String description;

	@Column(name="LOG_VALUE")
	private String value;

	@Lob
	@Column(name="LOG_LARGE_VALUE")
	private String largeValue;

	@Column(name="LOG_URL_CONTEXT")
	private String urlContext;
	
	@Column(name="LOG_CORRELATION_ID")
	private String correlationId;
	
	@Column(name="LOG_DOCUMENT_ID")
	private String documentId;
	
	@Column(name="LOG_DOCUMENT_TYPE_CD")
	private String documentTypeCode;
	
	@Column(name="LOG_BUS_CORRELATION_ID")
	private String businessCorrelationId;	
	
	@Column(name="LOG_MODULE")
	@Enumerated(EnumType.STRING)
	private LogModuleEnum module;
	
	@Column(name="LOG_USRNAME")
	private String username;
	
	@Column(name="LOG_USR_ROLE")
	private String userRole;
	
	@Column(name="LOG_AUTH_IP_ADDRESS")
	private String authIpAddress;
	
	@Column(name="LOG_ENTITY")
	private String entity;
	
	@Column(name="LOG_ENITITY_ID")
	private Long entityId;
	
	@ManyToOne
	@JoinColumn(name="LOG_TRA_ID")
	private Transaction transaction;

	@Column(name="LOG_MSG_ID")
	private Long messageId;

	@Column(name="LOG_MSG_BIN_ID")
	private Long messageBinaryId;
	
	@Column(name="LOG_MSG_SIZE")
	private Long messageSize;
	
	@ManyToOne
	@JoinColumn(name="LOG_ISSUER_PTY_ID")
	private Party issuerParty;
	
	@ManyToOne
	@JoinColumn(name="LOG_SENDER_PTY_ID")
	private Party senderParty;
	
	@ManyToOne
	@JoinColumn(name="LOG_RECEIVER_PTY_ID")
	private Party receiverParty;
	
	@ManyToOne
	@JoinColumn(name="LOG_BUSINESS_DOMAIN")
	private BusinessDomain businessDomain;
	
	@Column(name="LOG_CLASS")
	private String logClass;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public Log() {
		accessInfo = new EntityAccessInfo();
	}
	
	private Log(LogBuilder builder) {
		this.logType = builder.logType;
		this.correlationId = builder.correlationId;
		this.operation = builder.operation;
		this.description = builder.description;
		this.value = builder.value;
		this.largeValue = builder.largeValue;
		this.urlContext = builder.urlContext;
		this.documentId = builder.documentId;
		this.documentTypeCode = builder.documentTypeCode;
		this.businessCorrelationId = builder.businessCorrelationId;
		this.transaction = builder.transaction;
		this.messageId = builder.messageId;
		this.messageBinaryId = builder.messageBinaryId;
		this.messageSize = builder.messageSize;
		this.issuerParty = builder.issuerParty;
		this.senderParty = builder.senderParty;
		this.receiverParty = builder.receiverParty;
		this.businessDomain = builder.businessDomain;
		accessInfo = new EntityAccessInfo();
		this.accessInfo.setCreationId("TRUSTEX");
		this.module = builder.module;
		this.username = builder.username;
		this.userRole = builder.userRole;
		this.entity = builder.entity;
		this.authIpAddress = builder.authIpAddress;
		this.entityId = builder.entityId;
		this.logClass = builder.logClass;
		if (module == null || StringUtils.isEmpty(module.name())) {
			module = LogModuleEnum.ETRUSTEX;
		}
	}
	
	public static class LogBuilder {
		private String logType;
		private String correlationId;
		private String operation;
		private String description;
		private String value;
		private String largeValue;
		private String urlContext;
		private String documentId;
		private String documentTypeCode;
		private String businessCorrelationId;
		private Transaction transaction;
		private Long messageId;
		private Long messageBinaryId;
		private Long messageSize;
		private Party issuerParty;
		private Party senderParty;
		private Party receiverParty;
		private BusinessDomain businessDomain;
		private LogModuleEnum module;
		private String username;
		private String userRole;
		private String authIpAddress;
		private String entity;
		private Long entityId;
		private String logClass;
		
		public LogBuilder(String logType, String correlationId) {
			this.logType = logType;
			this.correlationId = correlationId;			
		}
		
		public LogBuilder operation(String operation) {
			this.operation = operation;
			return this;
		}
		
		public LogBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public LogBuilder value(String value) {
			this.value = value;
			return this;
		}	
		
		public LogBuilder largeValue(String largeValue) {
			this.largeValue = largeValue;
			return this;
		}	
		
		public LogBuilder urlContext(String urlContext) {
			this.urlContext = urlContext;
			return this;
		}	
		
		public LogBuilder documentId(String documentId) {
			this.documentId = documentId;
			return this;
		}		
		
		public LogBuilder documentTypeCode(String documentTypeCode) {
			this.documentTypeCode = documentTypeCode;
			return this;
		}
		
		public LogBuilder businessCorrelationId(String businessCorrelationId) {
			this.businessCorrelationId = businessCorrelationId;
			return this;
		}		
		
		public LogBuilder transaction(Transaction transaction) {
			this.transaction = transaction;
			return this;
		}		
		
		public LogBuilder messageId(Long messageId) {
			this.messageId = messageId;
			return this;
		}
		
		public LogBuilder messageBinaryId(Long messageBinaryId) {
			this.messageBinaryId = messageBinaryId;
			return this;
		}
		
		public LogBuilder issuerParty(Party issuerParty) {
			this.issuerParty = issuerParty;
			return this;
		}
		
		public LogBuilder senderParty(Party senderParty) {
			this.senderParty = senderParty;
			return this;
		}		
		
		public LogBuilder receiverParty(Party receiverParty) {
			this.receiverParty = receiverParty;
			return this;
		}			
		
		public LogBuilder businessDomain(BusinessDomain businessDomain) {
			this.businessDomain = businessDomain;
			return this;
		}		
		
		public LogBuilder messageSize(Long messageSize) {
			this.messageSize = messageSize;
			return this;
		}
		
		public LogBuilder module(LogModuleEnum module) {
			this.module = module;
			return this;
		}
		
		public LogBuilder username(String username) {
			this.username = username;
			return this;
		}

		public LogBuilder userRole(String userRole) {
			this.userRole = userRole;
			return this;
		}

		public LogBuilder authIpAddress(String authIpAddress) {
			this.authIpAddress = authIpAddress;
			return this;
		}

		public LogBuilder entity(String entity) {
			this.entity = entity;
			return this;
		}

		public LogBuilder entityId(Long entityId) {
			this.entityId = entityId;
			return this;
		}
		
		public LogBuilder logClass(String logClass) {
			this.logClass = logClass;
			return this;
		}		
		
		
		public Log build() {
			return new Log(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLargeValue() {
		return largeValue;
	}

	public void setLargeValue(String largeValue) {
		this.largeValue = largeValue;
	}

	public String getUrlContext() {
		return urlContext;
	}

	public void setUrlContext(String urlContext) {
		this.urlContext = urlContext;
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

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getBusinessCorrelationId() {
		return businessCorrelationId;
	}

	public void setBusinessCorrelationId(String businessCorrelationId) {
		this.businessCorrelationId = businessCorrelationId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}


	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getMessageBinaryId() {
		return messageBinaryId;
	}

	public void setMessageBinaryId(Long messageBinaryId) {
		this.messageBinaryId = messageBinaryId;
	}

	public Party getIssuerParty() {
		return issuerParty;
	}

	public void setIssuerParty(Party issuerParty) {
		this.issuerParty = issuerParty;
	}

	public Party getSenderParty() {
		return senderParty;
	}

	public void setSenderParty(Party senderParty) {
		this.senderParty = senderParty;
	}

	public Party getReceiverParty() {
		return receiverParty;
	}

	public void setReceiverParty(Party receiverParty) {
		this.receiverParty = receiverParty;
	}

	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}

	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public LogModuleEnum getModule() {
		return module;
	}

	public void setModule(LogModuleEnum module) {
		this.module = module;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getAuthIpAddress() {
		return authIpAddress;
	}

	public void setAuthIpAddress(String authIpAddress) {
		this.authIpAddress = authIpAddress;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(Long messageSize) {
		this.messageSize = messageSize;
	}

	public String getLogClass() {
		return logClass;
	}

	public void setLogClass(String logClass) {
		this.logClass = logClass;
	}
		
}
