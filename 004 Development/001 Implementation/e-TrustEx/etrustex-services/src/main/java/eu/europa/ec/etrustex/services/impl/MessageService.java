package eu.europa.ec.etrustex.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;

import eu.europa.ec.etrustex.dao.dto.*;
import eu.europa.ec.etrustex.services.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IMessageBinaryDAO;
import eu.europa.ec.etrustex.dao.IMessageDAO;
import eu.europa.ec.etrustex.dao.IPolicyDAO;
import eu.europa.ec.etrustex.dao.exception.BinaryStreamLimitExceededException;
import eu.europa.ec.etrustex.dao.exception.MessageCreationException;
import eu.europa.ec.etrustex.dao.exception.MessageRetrieveException;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.SizeSlaPolicy;
import eu.europa.ec.etrustex.domain.policy.VolumeSlaPolicy;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.LogModuleEnum;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public class MessageService implements IMessageService {
	private final static String BASE_FILE_NAME = "etrustex";
	private final static String BASE_FILE_EXT = "bin";

	@Autowired
	private IMessageDAO messageDAO;

	@Autowired
	private IMessageBinaryDAO messageBinaryDAO;

	private Boolean defaultUseFileStore;
	private String fileStorePath;

	@Autowired
	private IMetadataService metadataService;

	@Autowired
	private ILogService logService;

	@Autowired
	private IAuthorisationService authorisationService;

	@Autowired
	private IPartyService partyService;

	@Autowired
	private ITransactionService transactionService;

	@Autowired
	private IMessageRoutingService messageRoutingService;

	@Autowired
	private IPolicyDAO policyDAO;

	@Autowired
	protected Properties soapErrorMessages;

	private Boolean localUsefilestore;
	private Boolean usefilestoreMetadata;
	private String localFileStorePath;

	private List<String> listDisks = new ArrayList<String>();
	private Map<String, Long> diskSpace = new HashMap<String, Long>();
	private long total = 0l;

	private static final String WRAPPER_TRANSACTION_NS = "ec:services:wsdl:DocumentWrapper-2";
	private static final String STORE_WRAPPER_TRANSACTION_REQ_LOCAL_NAME = "StoreDocumentWrapperRequest";

	@PostConstruct
	public void afterInit() {

		logger.info("localUsefilestore=" + localUsefilestore);
		logger.info("usefilestoreMetadata=" + usefilestoreMetadata);
		logger.info("localFileStorePath=" + localFileStorePath);

	}

	@Override
	synchronized public void reloadFileStoreParameters(boolean forceReload) throws MessageCreationException {
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long) null, null, null, null, null);
		defaultUseFileStore = true;
		String pListPath = "";

		if (usefilestoreMetadata) {
			logger.info("use file store meta data");
			if (metadata.containsKey(MetaDataItemType.DEFAULT_USE_FILE_STORE) && metadata.containsKey(MetaDataItemType.FILE_STORE_PATH)) {
				String defaultUseFileStoreStr = metadata.get(MetaDataItemType.DEFAULT_USE_FILE_STORE).getValue();
				defaultUseFileStore = Boolean.parseBoolean(defaultUseFileStoreStr);
				pListPath = metadata.get(MetaDataItemType.FILE_STORE_PATH).getValue();
			} else {
				throw new MessageCreationException(ErrorResponseCode.TECHNICAL_ERROR.getDescription(), ErrorResponseCode.TECHNICAL_ERROR);
			}

		} else {
			logger.info("don't use file store meta data");
			defaultUseFileStore = localUsefilestore;
			logger.info("localUsefilestore=" + localUsefilestore);
			pListPath = localFileStorePath;
			logger.info("localFileStorePath=" + localFileStorePath);
		}

		if (pListPath == null) {
			throw new MessageCreationException(ErrorResponseCode.TECHNICAL_ERROR.getDescription(), ErrorResponseCode.TECHNICAL_ERROR);
		}

		boolean reloaded = false;
		if (!pListPath.equals(this.fileStorePath)) {// only configure if
			// relevant (first time or
			// config change occured)
			this.fileStorePath = pListPath;
			String[] splitList = pListPath.split(",");
			List<String> paths = Arrays.asList(splitList);
			listDisks = paths;
			reloaded = true;
		}
		if (reloaded || forceReload) {// init stats
			diskSpace.clear();
			for (String k : listDisks) {
				File f = new File(k);
				if (f.isDirectory()) {
					long free = f.getFreeSpace();
					diskSpace.put(k, free);
				} else {
					diskSpace.put(k, 0l);
				}
			}
			total = 0l;
			for (String key : listDisks) {
				total = total + diskSpace.get(key);
			}
		}
	}

	synchronized private void updateStats(String path, long consumedBytes) {
		diskSpace.put(path, diskSpace.get(path) - consumedBytes);
		total = total - consumedBytes;
	}

	synchronized private String selectStore() throws MessageCreationException {
		long value = (long) (Math.random() * total);
		long total = 0;
		for (String k : listDisks) {
			total += diskSpace.get(k);
			if (value < total)
				return k;
		}
		throw new MessageCreationException(ErrorResponseCode.TECHNICAL_ERROR.getDescription(), ErrorResponseCode.TECHNICAL_ERROR);
	}

	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	/**
	 * Used By ParentDocumentHandlerServiceActivator
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Message retrieveMessage(String messageDocumentId, String documentTypeCode, Long senderId, Long receiverId, Boolean biDirectional, Set<String> statesToExclude) {
		List<Message> queryResult = messageDAO.retrieveMessages(messageDocumentId, documentTypeCode, senderId, receiverId, biDirectional, statesToExclude);
		if (queryResult != null && queryResult.size() > 0) {
			// TODO we always return the first
			if (queryResult.size() > 1) {
				logger.warn("Duplicate messages ! id= " + messageDocumentId + ", type=" + documentTypeCode + ", sender=" + senderId + ", receiver=" + receiverId);
			}
			return queryResult.get(0);
		}
		// if nothing available
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Message retrieveMessage(String messageDocumentId, String documentTypeCode, Long messageSenderId, Long messagereceiverId) {
		return messageDAO.retrieveMessage(messageDocumentId, documentTypeCode, messageSenderId, messagereceiverId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Message retrieveMessage(Long messageId) {
		return messageDAO.read(messageId);
	}

	@Override
	@Transactional(readOnly = true)
	public long findMessagesByDocumentId(String messageDocumentId) {
		return messageDAO.findMessagesByDocumentId(messageDocumentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean isInterchangeAgreementUsedToSendMessages(InterchangeAgreement interchangeAgreement) {
		return messageDAO.isInterchangeAgreementUsedToSendMessages(interchangeAgreement);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Message retrieveMessageWithInitializedProperties(Long messageId, boolean initCollections) {
		Message message = messageDAO.read(messageId);

		if (message != null) {
			initializeMessageProperties(message);
			if (initCollections) {
				initializeMessageCollections(message);
			}
		}

		return message;
	}

	@Override
	@Transactional
	public MessageBinary retrieveMessageBinary(Long messageBinaryId) {
		return messageBinaryDAO.read(messageBinaryId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMessageBinary(Long binaryId) {
		messageBinaryDAO.deleteMessageBinary(binaryId);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public InputStream getMessageBinaryAsStream(Long messageBinaryId) {
		return messageBinaryDAO.getMessageBinaryAsStream(messageBinaryId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public InputStream getMessageBinaryAsStream(Long messageId, MessageBinaryType binaryType) {
		return messageBinaryDAO.getMessageBinaryAsStream(messageId, binaryType);
	}

	@Override
	public InputStream getDecryptedStream(InputStream encryptedStream, byte[] iv) throws MessageRetrieveException {
		try {
			return messageBinaryDAO.getDecryptedStream(encryptedStream, iv);
		} catch (IOException e) {
			throw new MessageRetrieveException(e, e.getMessage(), ErrorResponseCode.TECHNICAL_ERROR);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Message updateMessage(Message message) {
		return messageDAO.update(message);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public MessageBinary updateMessageBinary(MessageBinary binary) throws MessageUpdateException {
		return messageBinaryDAO.update(binary);
	}

	@Override
	@Transactional(readOnly = true)
	public Long createMessageBinary(CreateMessageBinaryDTO createMessageBinaryDTO) throws MessageCreationException {
		MessageBinary bin = null;
		StringBuffer fullFilePath = null;
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.CRUD, this.getClass().getName())
				.description("Inside MessageService ")
				.businessDomain(createMessageBinaryDTO.getIssuer().getBusinessDomain())
				.build();
		try {
			Party senderParty = authorisationService.getParty(createMessageBinaryDTO.getSenderIdWithScheme(),
					createMessageBinaryDTO.getIssuer().getBusinessDomain());
			//ETRUSTEX-623 validate volume of data sent against the SLA policy
			SlaValidationDTO slaValidationDTO = buildSlaValidationDTO(senderParty);
			if (slaValidationDTO.isSlaVolumeExceeded()) {
				throw new MessageCreationException(ErrorResponseCode.SLA_VOLUME_EXCEEDED.getDescription(), ErrorResponseCode.SLA_VOLUME_EXCEEDED);
			}

			PushbackInputStream pBackIS = new PushbackInputStream(createMessageBinaryDTO.getInputStream());
			int b;
			b = pBackIS.read();
			if (b == -1) {
				throw new MessageCreationException(ErrorResponseCode.BINARY_ERROR.getDescription(), ErrorResponseCode.BINARY_ERROR);
			}
			pBackIS.unread(b);
			// check if parameters changed
			reloadFileStoreParameters(false);
			Boolean useFileStore = createMessageBinaryDTO.getUseFileStore();
			if (useFileStore == null)
				useFileStore = defaultUseFileStore;
			if (useFileStore) {
				String delimiter = System.getProperty("file.separator");
				fullFilePath = new StringBuffer();
				String subfolder = "";
				if (createMessageBinaryDTO.getSenderIdWithScheme() != null) {
					subfolder = subfolder.concat(createMessageBinaryDTO.getSenderIdWithScheme().replaceAll(":", "-")).concat(delimiter);
				}
				// get store
				String store = selectStore();

				fullFilePath.append(store).append(delimiter).append(subfolder).append(BASE_FILE_NAME).append("-").append(UUID.randomUUID().toString()).append('.').append(BASE_FILE_EXT);
				createMessageBinaryDTO.setFullFilePath(fullFilePath.toString());
				logger.debug("storing binary to file [{}], encryption [{}]", fullFilePath.toString(), createMessageBinaryDTO.getEncryptBinary());

				// ETRUSTEX-948 enforce 100MB wrapper size limit only if it is
				// not overriden by an SLA policy
				EncryptBinaryDTO encryptBinaryDTO = messageBinaryDAO.storeBinaryStreamToFile(pBackIS, fullFilePath.toString(), createMessageBinaryDTO.getEncryptBinary(),
						slaValidationDTO);

				//validate expected size from the XML part against the binary actual size
				if (createMessageBinaryDTO.getExpectedSize() != null && !createMessageBinaryDTO.getExpectedSize().equals(encryptBinaryDTO.getBinarySize())) {
					throw new MessageCreationException(ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION,
							soapErrorMessages.getProperty("error.hardrule.binary.sizeMismatch"));
				}

				createMessageBinaryDTO.setExpectedSize(encryptBinaryDTO.getBinarySize());
				createMessageBinaryDTO.setIv(encryptBinaryDTO.getIv());
				logDTO.setMessageSize(encryptBinaryDTO.getBinarySize());
				logDTO.setSenderParty(senderParty);
				logDTO.setIssuerParty(createMessageBinaryDTO.getIssuer());
				logDTO.setDocumentId(createMessageBinaryDTO.getDocumentId());
				bin = messageBinaryDAO.createMessageBinary(createMessageBinaryDTO);
				logDTO.setMessageBinaryId(bin.getId());
				logDTO.setMessageId(bin.getMessage() != null ? bin.getMessage().getId() : null);
				if (bin != null && bin.getMessage() != null && bin.getMessage().getSender() != null) {
					logDTO.setBusinessDomain(bin.getMessage().getSender().getBusinessDomain());
				}
				logService.saveLog(logDTO);
				updateStats(store, encryptBinaryDTO.getBinarySize());
			} else {
				bin = messageBinaryDAO.createMessageBinary(createMessageBinaryDTO);
				messageBinaryDAO.storeBinaryStreamToDataBase(bin.getId(), pBackIS, createMessageBinaryDTO.getEncryptBinary());
			}
		} catch (MessageCreationException e) {
			logDTO.setLogType(LogDTO.LOG_TYPE.ERROR);
			logDTO.setDescription(logDTO.getDescription() + e.getMessage());
			logService.saveLog(logDTO);
			if (fullFilePath != null) {
				File f = new File(fullFilePath.toString());
				if (f.exists()) {
					f.delete();
				}
			}
			throw e;
		} catch (BinaryStreamLimitExceededException e) {
			logDTO.setLogType(LogDTO.LOG_TYPE.ERROR);
			logDTO.setDescription(logDTO.getDescription() + e.getMessage());
			logService.saveLog(logDTO);
			logger.error(e.getMessage(), e);
			if (fullFilePath != null) {
				File f = new File(fullFilePath.toString());
				if (f.exists()) {
					f.delete();
				}
			}
			if (bin != null) {
				messageBinaryDAO.deleteMessageBinary(bin.getId());
			}
			throw new MessageCreationException(e, ErrorResponseCode.SLA_BINARY_SIZE_TOO_LARGE.getDescription(), ErrorResponseCode.SLA_BINARY_SIZE_TOO_LARGE);
		} catch (IOException ioe) {
			throw new MessageCreationException(ioe, ErrorResponseCode.BINARY_ERROR.getDescription(), ErrorResponseCode.BINARY_ERROR);
		} catch (Exception e) {
			logDTO.setLogType(LogDTO.LOG_TYPE.ERROR);
			logDTO.setDescription(logDTO.getDescription() + e.getMessage());
			logService.saveLog(logDTO);
			logger.error(e.getMessage(), e);
			if (fullFilePath != null) {
				File f = new File(fullFilePath.toString());
				if (f.exists()) {
					f.delete();
				}
			}
			if (bin != null) {
				messageBinaryDAO.deleteMessageBinary(bin.getId());
			}
			throw new MessageCreationException(e, ErrorResponseCode.TECHNICAL_ERROR.getDescription(), ErrorResponseCode.TECHNICAL_ERROR);
		}

		return bin.getId();

	}

	private SlaValidationDTO buildSlaValidationDTO(Party sender) {
		PolicySearchDTO policySearchDTO = new PolicySearchDTO();
		policySearchDTO.setSender(sender);
		policySearchDTO.setPolicyType(VolumeSlaPolicy.class);
		List<Policy> policies = policyDAO.getPoliciesByCriteria(policySearchDTO);
		Long slaMaxBinarySize = getWrapperMaxBinarySizeLimit(sender);
		if (policies == null || policies.size() == 0) {
			return new SlaValidationDTO(null, 0, slaMaxBinarySize);
		}
		VolumeSlaPolicy policy = (VolumeSlaPolicy)policies.get(0);
		//retrieve the volume of data sent by sender since the beginning of the period
		long expendedVolume = logService.getVolumeCountForParty(policySearchDTO.getSender().getId(), policy.getFrequency());
		return new SlaValidationDTO(Long.valueOf(policy.getValue() * 1024 * 1024 + ""), expendedVolume, slaMaxBinarySize);
	}

	/**
	 * check if there are SLA policies that set a higher size limit than MessageBinaryDAO.STREAM_MAX_NUMBER_OF_BYTES on wrappers
	 *
	 * @param senderParty
	 * @return the value in the SLA size policy
	 */
	private Long getWrapperMaxBinarySizeLimit(Party senderParty) {
		PolicySearchDTO policySearchDTO = new PolicySearchDTO();
		policySearchDTO.setSender(senderParty);
		policySearchDTO.setPolicyType(SizeSlaPolicy.class);
		List<Transaction> transactions = transactionService.getTransactionsByNamespaceAndRequestLocalName(WRAPPER_TRANSACTION_NS, STORE_WRAPPER_TRANSACTION_REQ_LOCAL_NAME);
		if (CollectionUtils.isNotEmpty(transactions)) {
			policySearchDTO.setTransaction(transactions.get(0));
		}
		List<Policy> policies = policyDAO.getPoliciesByCriteria(policySearchDTO);
		if (CollectionUtils.isNotEmpty(policies)) {
			for (Policy policy : policies) {
				if (policy.getValue() != null) {
					return Long.valueOf(policy.getValue()) * 1024 * 1024;
				}
			}
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMessage(Message message) throws MessageUpdateException {
		try {
			String fileSystemPath = messageBinaryDAO.getFileSystemPath(message.getId());
			messageDAO.delete(message);
			//ETRUSTEX-1241 delete the files only if no errors during message deletion
			if (StringUtils.isNotEmpty(fileSystemPath)) {
				File file = new File(fileSystemPath);
				file.delete();
			}

		} catch (Exception e) {
			throw new MessageUpdateException(e, ErrorResponseCode.TECHNICAL_ERROR.getDescription(), ErrorResponseCode.TECHNICAL_ERROR);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMessageTree(Message message2) throws MessageUpdateException {

		Message message = messageDAO.read(message2.getId());

		if(message == null){
			return;
		}

		initializeMessageCollections(message);

		List<Message> toDelete = new ArrayList<Message>();
		List<Message> toDetatch = new ArrayList<Message>();

		if(message.getChildMessages() != null){
			for (Message c : message.getChildMessages()) {
				if(c.getParentMessages() != null && c.getParentMessages().size() > 1){
					toDetatch.add(c);
					c.getParentMessages().remove(message);
				}else{
					c.getParentMessages().clear();
					toDelete.add(c);
				}
			}
		}

		message.getChildMessages().clear();

		if(!toDetatch.isEmpty()){
			updateMessage(message);
		}

		//Deleting Message Routing references
		messageRoutingService.deleteByMessageId(message.getId());

		deleteMessage(message);

		// Deleting children
		for (Message del : toDelete) {
//			messageRoutingService.deleteByMessageId(del.getId());
			//recursively delete children
			deleteMessageTree(del);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long createMessage(CreateMessageDTO createMessageDTO) throws MessageCreationException {
		Transaction transaction = authorisationService.getTransactionById(createMessageDTO.getTransactionTypeId());
		Party issuer = partyService.getParty(createMessageDTO.getIssuerId());
		Party sender = partyService.getParty(createMessageDTO.getSenderPartyId());
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.CRUD, this.getClass().getName())
				.businessCorrelationId(createMessageDTO.getCorrelationId())
				.businessDomain(issuer.getBusinessDomain())
				.description("Inside MessageService")
				.documentId(createMessageDTO.getDocumentId())
				.issuerParty(issuer)
				.module(LogModuleEnum.ETRUSTEX)
				.receiverParty(partyService.getParty(createMessageDTO.getReceiverPartyId()))
				.senderParty(sender)
				.transaction(transaction)
				.documentTypeCode(transaction != null ? transaction.getDocument().getDocumentTypeCode() : null)
				.build();

		// find metadata to determine what SOAP fault should be thrown in case
		// of duplicate message id
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData(createMessageDTO.getIcaId(), null, null, null, null);
		MetaDataItem oldFaultSupportMetadataItem = metadata.get(MetaDataItemType.OLD_FAULT_SUPPORT);
		boolean oldFaultSupport = false;
		if (oldFaultSupportMetadataItem == null) {
			// search for metadata for store document wrapper by sender and
			// transaction
			MetaDataItem criteria = new MetaDataItem();
			criteria.setSender(sender);
			criteria.setTansaction(transaction);
			criteria.setRawItemType(MetaDataItemType.OLD_FAULT_SUPPORT.name());
			List<MetaDataItem> metadataList = metadataService.getMetaDataItemsByCriteria(criteria);
			if (CollectionUtils.isNotEmpty(metadataList)) {
				oldFaultSupportMetadataItem = metadataList.get(0);
			}
		}
		if (oldFaultSupportMetadataItem != null) {
			oldFaultSupport = BooleanUtils.toBoolean(oldFaultSupportMetadataItem.getValue());
		}
		String exceptionDescription = oldFaultSupport ? "Message ID : " + createMessageDTO.getDocumentId() + " Already exists" : ErrorResponseCode.DUPLICATE_ENTITY.getDescription();
		ErrorResponseCode exceptionResponseCode = oldFaultSupport ? ErrorResponseCode.DOCUMENT_ALREADY_EXISTS : ErrorResponseCode.DUPLICATE_ENTITY;

		try {
			if (retrieveMessage(createMessageDTO.getDocumentId(), createMessageDTO.getDocumentTypeCd(), createMessageDTO.getSenderPartyId(),
					createMessageDTO.getReceiverPartyId(), false, null) != null) {
				throw new MessageCreationException(exceptionDescription, exceptionResponseCode);
			}
			EntityAccessInfo info = new EntityAccessInfo();
			info.setCreationDate(createMessageDTO.getReceptionDate());
			info.setModificationDate(createMessageDTO.getReceptionDate());
			info.setCreationId(createMessageDTO.getAuthenticatedUser());
			info.setModificationId(createMessageDTO.getAuthenticatedUser());
			Message message = null;
			try {
				message = messageDAO.createMessage(buildMessage(createMessageDTO));
			} catch (PersistenceException e) {

				if (e.getCause() instanceof ConstraintViolationException) {
					// we check if the ConstraintViolation Is caused by the
					// entry of a Duplicate Unique
					// TODO (ode) to be reviewed and tested on MySQL
					if (e.getCause().getMessage().toLowerCase().contains("unique")) {
						exceptionDescription = oldFaultSupport ? "Message ID : " + createMessageDTO.getDocumentId() + " Already exists" : ErrorResponseCode.DUPLICATE_ENTITY.getDescription();
						exceptionResponseCode = oldFaultSupport ? ErrorResponseCode.DOCUMENT_ALREADY_EXISTS : ErrorResponseCode.DUPLICATE_ENTITY;
						throw new MessageCreationException(exceptionDescription, exceptionResponseCode);
					} else {
						throw new MessageCreationException(exceptionDescription, exceptionResponseCode);
					}
				} else {
					throw e;
				}
			}

			Long messageId = message.getId();
			logDTO.setMessageId(messageId);
			if (createMessageDTO.getParentMessageId() != null) {
				Message parent = messageDAO.read(createMessageDTO.getParentMessageId());
				if (parent != null) {
					message.addParentMessage(parent);
				}
			}
			// Long messageId= toStore.getId();
			for (MessageBinary messageBinary : createMessageDTO.getBinaries()) {
				// this is in the normal case
				MessageBinary tosave = null;
				if (messageBinary.getId() == null) {
					tosave = messageBinaryDAO.createMessageBinary(messageId, messageBinary);
					tosave.setAccessInfo(info);
					tosave.setMessage(message);

				}
				// in thecase of a wrapper the binary is srteamed before and we
				// receive the id
				else {
					tosave = messageBinaryDAO.read(messageBinary.getId());
					tosave.setAccessInfo(info);
					tosave.setMessage(message);
				}
				message.addMessageBinary(tosave);
			}
			messageDAO.update(message);
			logService.saveLog(logDTO);
			return messageId;
		} catch (SQLException e) {
			throw new MessageCreationException(e, e.getMessage(), ErrorResponseCode.TECHNICAL_ERROR);
		}
	}

	private Message buildMessage(CreateMessageDTO createMessageDTO) {
		Message newMessage = new Message();
		if (createMessageDTO.getIcaId() != null) {
			InterchangeAgreement agreement = new InterchangeAgreement();
			agreement.setId(createMessageDTO.getIcaId());
			newMessage.setAgreement(agreement);
		}
		newMessage.setDocumentId(createMessageDTO.getDocumentId());
		newMessage.setCorrelationId(createMessageDTO.getCorrelationId());
		newMessage.setStatusCode(createMessageDTO.getStatusCode());
		newMessage.setReceptionDate(createMessageDTO.getReceptionDate());
		newMessage.setRetrieveIndicator(false);
		newMessage.setResponseCode(createMessageDTO.getResponseCode());
		Party issuer = new Party();
		issuer.setId(createMessageDTO.getIssuerId());
		newMessage.setIssuer(issuer);
		Transaction t = new Transaction();
		t.setId(createMessageDTO.getTransactionTypeId());
		newMessage.setTransaction(t);
		EntityAccessInfo accessInfo = new EntityAccessInfo();
		accessInfo.setCreationDate(createMessageDTO.getReceptionDate());
		accessInfo.setCreationId(createMessageDTO.getAuthenticatedUser());
		accessInfo.setModificationDate(createMessageDTO.getReceptionDate());
		accessInfo.setModificationId(createMessageDTO.getAuthenticatedUser());
		newMessage.setAccessInfo(accessInfo);
		newMessage.setIssueDate(createMessageDTO.getIssueDate());
		if (createMessageDTO.getReceiverPartyId() != null) {
			Party receiver = new Party();
			receiver.setId(createMessageDTO.getReceiverPartyId());
			newMessage.setReceiver(receiver);
		}
		Party sender = new Party();
		sender.setId(createMessageDTO.getSenderPartyId());
		newMessage.setSender(sender);
		newMessage.setMessageDocumentTypeCode(createMessageDTO.getDocumentTypeCd());
		return newMessage;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Message> retrieveMessages(MessageQueryDTO messageDTO) {
		return messageDAO.retrieveMessages(messageDTO);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMessage(Long messageId, String statusCode, String responseCode) {
		Message m = messageDAO.read(messageId);
		m.setResponseCode(responseCode);
		m.setStatusCode(statusCode);
		m.getAccessInfo().setModificationDate(Calendar.getInstance().getTime());
		messageDAO.update(m);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMessageStatus(Long messageId, String statusCode) {
		Message m = messageDAO.read(messageId);
		m.setStatusCode(statusCode);
		m.getAccessInfo().setModificationDate(Calendar.getInstance().getTime());
		messageDAO.update(m);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getMessageBinaryAsString(Long messageId, MessageBinaryType type) {
		return messageBinaryDAO.getMessageBinaryAsString(messageId, type);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Message> retrieveMessagesForQueryRequest(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO) {

		if (retrieveMessagesDTO.getEndDate() != null) {
			//set endDate to the last milisecond of the day - 23:59:59:999
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(retrieveMessagesDTO.getEndDate());
			calendar.add(Calendar.DATE, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			retrieveMessagesDTO.setEndDate(calendar.getTime());
		}

		return messageDAO.retrieveMessagesForQueryRequest(retrieveMessagesDTO);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Message> retrieveLeaves(BusinessDomain businessDomain, Transaction transaction, Date startDate, Date endDate, Set<Transaction> childTransactionsToIgnore) {
		return messageDAO.retrieveLeaves(businessDomain, transaction, startDate, endDate, childTransactionsToIgnore);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Message> retrieveOrphans(BusinessDomain businessDomain, Transaction transaction, Date startDate, Date endDate) {
		return messageDAO.retrieveOrphans(businessDomain, transaction, startDate, endDate);
	}

	@Override
	public MessagesListVO retrieveMessagesWithConversation(RetrieveMessagesForQueryRequestDTO queryDTO) {
		return messageDAO.retrieveMessagesJustice(queryDTO);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long findMessagesByTransaction(Long transactionId) {
		return messageDAO.findMessagesByTransaction(transactionId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<String> getDocumentTypeCodes() {
		return messageDAO.getDocumentTypeCodes();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<String> getStatusCodes() {
		return messageDAO.getStatusCodes();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Message> findMessagesByCriteria(Message message, Date createdFrom, Date createdTo, int firstResult, int maxResults, List<Long> businessDomainIds, DispatchEnum dispatched) {
		List<Message> messages = messageDAO.findMessagesByCriteria(message, createdFrom, createdTo, firstResult, maxResults, businessDomainIds, dispatched);

		for(Message m : messages) {
			initializeMessageProperties(m);
		}

		return messages;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long countMessagesByCriteria(Message message, Date createdFrom, Date createdTo, List<Long> businessDomainIds, DispatchEnum dispatched) {
		return messageDAO.countMessagesByCriteria(message, createdFrom, createdTo, businessDomainIds, dispatched);
	}

	private void initializeMessageProperties(Message message) {
		Hibernate.initialize(message.getIssuer());
		Hibernate.initialize(message.getSender());
		Hibernate.initialize(message.getReceiver());
		Hibernate.initialize(message.getTransaction());
		Hibernate.initialize(message.getAgreement());
	}

	private void initializeMessageCollections(Message message) {
		for (Message child : message.getChildMessages()) {
			Hibernate.initialize(child);
			Hibernate.initialize(child.getSender().getBusinessDomain());
		}

		for (Message parent : message.getParentMessages()) {
			Hibernate.initialize(parent);
			Hibernate.initialize(parent.getSender().getBusinessDomain());
		}

		message.getBinaries().size();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void detachParent(Long messageId) {
		Message message = retrieveMessage(messageId);
		message.setParentMessages(null);
		updateMessage(message);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isManaged(Message message) {
		return messageDAO.isManaged(message);
	}

	@Override
	@Transactional
	public MessageBinary getMessageBinary(Long messageId, String binaryType) {
		return messageBinaryDAO.getMessageBinary(messageId, binaryType);
	}

	@Override
	public void detachMessageBinary(MessageBinary binary) {
		messageBinaryDAO.detachObject(binary);
	}

	@Override
	@Transactional
	public void cleanPhantomBinary(Long binaryId) {
		logService.removeBinarySizeInLogs(logService.getLogCorrelationId());
		if (binaryId != null) {
			deleteMessageBinary(binaryId);
		}
	}

	@Override
	public boolean existsEjusticeMessageForCorrelationId(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO) {
		return messageDAO.existsEjusticeMessageForCorrelationId(retrieveMessagesDTO);
	}

	@Override
	public long getMessageCount(MessageQueryDTO messageDTO) {
		return messageDAO.getMessageCount(messageDTO);
	}

	public void setLocalUsefilestore(Boolean localUsefilestore) {
		this.localUsefilestore = localUsefilestore;
	}

	public void setLocalFileStorePath(String localFileStorePath) {
		this.localFileStorePath = localFileStorePath;
	}

	public void setUsefilestoreMetadata(Boolean usefilestoreMetadata) {
		this.usefilestoreMetadata = usefilestoreMetadata;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public IMessageDAO getMessageDAO() {
		return messageDAO;
	}

	public void setMessageDAO(IMessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	public IMessageBinaryDAO getMessageBinaryDAO() {
		return messageBinaryDAO;
	}

	public void setMessageBinaryDAO(IMessageBinaryDAO messageBinaryDAO) {
		this.messageBinaryDAO = messageBinaryDAO;
	}


}
