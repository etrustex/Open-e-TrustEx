/**
 * 
 */
package eu.europa.ec.etrustex.dao.dto;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.types.LogModuleEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author chiricr
 *
 */
public class LogDTO {
	
	private Long id;
	private LOG_TYPE logType;
	private LOG_OPERATION operation;
	private String description;
	private String value;
	private String largeValue;
	private String urlContext;
	private String correlationId;
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
	private Set<Long> businessDomains;
	private LogModuleEnum module;
	private String username;
	private String userRole;
	private String authIpAddress;
	private String entity;
	private List<String> entities;
	private Long entityId;
	private boolean reinitializeCorrelationId;
	private String logClass;
	
	public enum LOG_OPERATION {
        AUTHENTICATION, AUTHORIZATION, VALIDATION, DATA_EXTRACTION, BUSINESS, PROCESS_MSG, CRUD, DISPATCHING, CONNECTION, TRANSFORM_MSG,
		RECEIVE_JMS_MSG, APPLY_RETENTION_POLICY, SLA_POLICY_VALIDATION, OTHER, EVIDENCE_EMISSION,
        CREATE, UPDATE, DELETE, VIEW, SEARCH, REDISPATCH, BATCHREDISPATCH, RESUBMIT;

        /*
         * Returns etrustex log operations, filtering out operations specific to CIPADMIN.
         * To be used for searching etrustex logs (JIRA-1489).
         */
        public static List<LOG_OPERATION> eTrustexOperations() {
            List<LOG_OPERATION> ops = Arrays.asList(AUTHORIZATION, VALIDATION, DATA_EXTRACTION, BUSINESS, PROCESS_MSG, CRUD, DISPATCHING, CONNECTION, TRANSFORM_MSG,
                    RECEIVE_JMS_MSG, APPLY_RETENTION_POLICY, SLA_POLICY_VALIDATION, OTHER, EVIDENCE_EMISSION);

            Collections.sort(ops);

            return ops;
        }

        public static List<LOG_OPERATION> etxadminOperations() {
            List<LOG_OPERATION> ops = Arrays.asList(AUTHENTICATION, AUTHORIZATION, CREATE, UPDATE, DELETE, VIEW, SEARCH, REDISPATCH, BATCHREDISPATCH, RESUBMIT);

            Collections.sort(ops);

            return ops;
        }
	}
	
	public enum LOG_TYPE {
		INFO, WARN, ERROR, SUCCESS
	}
	
	private LogDTO(LogDTOBuilder builder) {
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
		this.issuerParty = builder.issuerParty;
		this.senderParty = builder.senderParty;
		this.receiverParty = builder.receiverParty;
		this.businessDomain = builder.businessDomain;
		this.businessDomains = builder.businessDomains;
		this.messageSize = builder.messageSize;
		this.reinitializeCorrelationId = builder.reinitializeCorrelationId;
		this.module = builder.module;
		this.username = builder.username;
		this.userRole = builder.userRole;
		this.entity = builder.entity;
		this.entities = builder.entities;
		this.authIpAddress = builder.authIpAddress;
		this.entityId = builder.entityId;
		this.logClass = builder.logClass;

	}
	
	public static class LogDTOBuilder {
		private LOG_TYPE logType;
		private String correlationId;
		private LOG_OPERATION operation;
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
		private Set<Long> businessDomains;
		private boolean reinitializeCorrelationId;
		private LogModuleEnum module;
		private String username;
		private String userRole;
		private String authIpAddress;
		private String entity;

		private List<String> entities;
		private Long entityId;
		private String logClass;

		public LogDTOBuilder(LOG_TYPE logType, LOG_OPERATION logOperation, String logClass) {
			this.logType = logType;		
			this.operation = logOperation;
			this.logClass = logClass;
		}
		
		public LogDTOBuilder correlationId(String correlationId) {
			this.correlationId = correlationId;
			return this;
		}
				
		public LogDTOBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public LogDTOBuilder value(String value) {
			this.value = value;
			return this;
		}	
		
		public LogDTOBuilder largeValue(String largeValue) {
			this.largeValue = largeValue;
			return this;
		}	
		
		public LogDTOBuilder urlContext(String urlContext) {
			this.urlContext = urlContext;
			return this;
		}	
		
		public LogDTOBuilder documentId(String documentId) {
			this.documentId = documentId;
			return this;
		}		
		
		public LogDTOBuilder documentTypeCode(String documentTypeCode) {
			this.documentTypeCode = documentTypeCode;
			return this;
		}
		
		public LogDTOBuilder businessCorrelationId(String businessCorrelationId) {
			this.businessCorrelationId = businessCorrelationId;
			return this;
		}		
		
		public LogDTOBuilder transaction(Transaction transaction) {
			this.transaction = transaction;
			return this;
		}		
		
		public LogDTOBuilder messageId(Long messageId) {
			this.messageId = messageId;
			return this;
		}
		
		public LogDTOBuilder messageBinaryId(Long messageBinaryId) {
			this.messageBinaryId = messageBinaryId;
			return this;
		}
		
		public LogDTOBuilder messageSize(Long messageSize) {
			this.messageSize = messageSize;
			return this;
		}		
		
		public LogDTOBuilder issuerParty(Party issuerParty) {
			this.issuerParty = issuerParty;
			return this;
		}
		
		public LogDTOBuilder senderParty(Party senderParty) {
			this.senderParty = senderParty;
			return this;
		}		
		
		public LogDTOBuilder receiverParty(Party receiverParty) {
			this.receiverParty = receiverParty;
			return this;
		}			
		
		public LogDTOBuilder businessDomain(BusinessDomain businessDomain) {
			this.businessDomain = businessDomain;
			return this;
		}

		public LogDTOBuilder businessDomains(Set<Long> businessDomains) {
			this.businessDomains = businessDomains;
			return this;
		}

		public LogDTOBuilder reinitializeCorrelationId(boolean reinitializeCorrelationId) {
			this.reinitializeCorrelationId = reinitializeCorrelationId;
			return this;
		}		
		
		public LogDTOBuilder module(LogModuleEnum module) {
			this.module = module;
			return this;
		}
		
		public LogDTOBuilder username(String username) {
			this.username = username;
			return this;
		}

		public LogDTOBuilder userRole(String userRole) {
			this.userRole = userRole;
			return this;
		}

		public LogDTOBuilder authIpAddress(String authIpAddress) {
			this.authIpAddress = authIpAddress;
			return this;
		}

		public LogDTOBuilder entity(String entity) {
			this.entity = entity;
			return this;
		}

		public LogDTOBuilder entities(List<String> entities) {
			this.entities = entities;
			return this;
		}

		public LogDTOBuilder entityId(Long entityId) {
			this.entityId = entityId;
			return this;
		}
				
		public LogDTO build() {
			return new LogDTO(this);
		}
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LOG_TYPE getLogType() {
		return logType;
	}
	
	public void setLogType(LOG_TYPE logType) {
		this.logType = logType;
	}
	public LOG_OPERATION getOperation() {
		return operation;
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
	public void setMessageId(Long message) {
		this.messageId = message;
	}
	public Long getMessageBinaryId() {
		return messageBinaryId;
	}
	public void setMessageBinaryId(Long messageBinary) {
		this.messageBinaryId = messageBinary;
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
	public Set<Long> getBusinessDomains() {
		return businessDomains;
	}
	public void setBusinessDomains(Set<Long> businessDomains) {
		this.businessDomains = businessDomains;
	}
	public Long getMessageSize() {
		return messageSize;
	}
	public void setMessageSize(Long messageSize) {
		this.messageSize = messageSize;
	}
	public boolean getReinitializeCorrelationId() {
		return reinitializeCorrelationId;
	}
	public void setReinitializeCorrelationId(boolean reinitializeCorrelationId) {
		this.reinitializeCorrelationId = reinitializeCorrelationId;
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
	public List<String> getEntities() { return entities;}
	public void setEntities(List<String> entities) {this.entities = entities;}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getLogClass() {
		return logClass;
	}
	
}
