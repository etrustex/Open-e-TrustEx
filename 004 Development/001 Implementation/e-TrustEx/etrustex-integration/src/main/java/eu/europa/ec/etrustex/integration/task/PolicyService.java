package eu.europa.ec.etrustex.integration.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IMessageBinaryDAO;
import eu.europa.ec.etrustex.dao.IPolicyDAO;
import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.RetentionPolicy;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.MetaDataItemType;

@Service("policyService")
public class PolicyService implements IPolicyService {

	protected static final Logger logger = LoggerFactory.getLogger(PolicyService.class);
		
	public static final String APP_RESP_DTC = "301";
	public static final String WRAPPER_DTC = "DWR";	
	
	@Autowired
	private IPolicyDAO policyDAO;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IMessageBinaryDAO messageBinaryDao;

	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IAuthorisationService authorisationService;
	
	@Autowired
	private PolicyHelper helper;

	private static int MAX_FILES_BUFFER = 400;
	private static String EXCLUSION_DIRECTORIES = ".backups .snapshot";
	
	@Override
	public List<Policy> getPoliciesByCriteria(
			PolicySearchDTO slaPolicySearchDTO) {
		return policyDAO.getPoliciesByCriteria(slaPolicySearchDTO);
	}

	@Override
	public void applyRetentionPolicies() {
		
		List<Policy> policies = null;

		Set<Transaction> childTransactionsToIgnore = new HashSet<Transaction>();
		childTransactionsToIgnore.addAll(authorisationService.getTransactionsByDocumentTypeCd(APP_RESP_DTC));
		childTransactionsToIgnore.addAll(authorisationService.getTransactionsByDocumentTypeCd(WRAPPER_DTC));

		policies = getPoliciesByType(RetentionPolicy.class);
		logger.info("Number of RETENTIONs found: " + policies.size());
		boolean doCleanup = false;
		boolean reLoop;
		// Iterates over Retention Policies (That are not Wrappers) starting from the leaves
		for (Policy policy : policies) {
			if (policy.getActiveFlag() && !isWrapper(policy)) {
				// If there there are no deletions for a specific SLA, it exits
				// the while loop otherwise climbs up to the parent
				reLoop = true;
				while (reLoop) {
					reLoop = false;
					if (handleDocumentRetention(policy, childTransactionsToIgnore)) {
						reLoop = true;
					}
				}
			}
		}
		
		// Iterates over Retention Policies for Wrappers
		for (Policy policy : policies) {
			if (policy.getActiveFlag() && isWrapper(policy)) {
				doCleanup = true;
				handleRetentionNoParent(policy);
			}
		}

		if(doCleanup){
			deleteFilesNotInDB();
		}
	}


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Policy create(Policy policy) {
        return policyDAO.create(policy);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Policy update(Policy policy) {
        return policyDAO.update(policy);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly=true)
    public Policy findById(Long id) {
    	Policy policy = policyDAO.read(id);

        if(policy != null) {
            for(Profile p : policy.getBusinessDomain().getProfiles()) {
                Hibernate.initialize(p);
            }
        }

        return policy;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly=true)
    public List<RetentionPolicy> findRetentionPoliciesByCriteriaForView(RetentionPolicy policy, List<BusinessDomain> businessDomains) {
        return policyDAO.findRetentionPoliciesByCriteriaForView(policy, businessDomains);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        policyDAO.delete(policyDAO.read(id));
    }

	/**
	 * Specific Method for Wrappers
	 * 
	 * @param Policy
	 */
	private void handleRetentionNoParent(Policy policy) {
		Date startDate = DateUtils.addYears(GregorianCalendar.getInstance()
				.getTime(), -20);
		Date endDate = DateUtils.addDays(GregorianCalendar.getInstance()
				.getTime(), -policy.getValue()); boolean loop = true;
		while(loop){			
			loop = false;
			List<Message> messages = messageService.retrieveOrphans(
					policy.getBusinessDomain(), policy.getTransaction(),
				startDate, endDate);
			
			if(messages.size() == 500)
				loop = true;
			for (Message message : messages) {
				try {
					helper.deleteWrapperMessage(message.getId());
				} catch (MessageUpdateException | JAXBException e) {
					logger.error("Could not delete message with doc Id: "+message.getDocumentId(), e);
				}
			}
		}
	}



	/**
	 * 
	 * @param policy
	 * @return
	 */
	private boolean handleDocumentRetention(Policy policy, Set<Transaction> childTransactionsToIgnore) {
		Date startDate = DateUtils.addYears(GregorianCalendar.getInstance()
				.getTime(), -20);
		Date endDate = DateUtils.addDays(GregorianCalendar.getInstance()
				.getTime(), -policy.getValue());

		List<Message> messages = messageService.retrieveLeaves(
				policy.getBusinessDomain(), policy.getTransaction(),
				startDate, endDate, childTransactionsToIgnore);
		
		boolean hasMessages = (messages.size() > 0) ? true : false;
		
		if (hasMessages) {
			for (Message message : messages) {
				try {
					helper.deleteMessage(message.getId());
				} catch (MessageUpdateException e) {
					logger.error("Could not delete message with doc Id: "+message.getDocumentId(), e);
				}
			}
		}
		return hasMessages;
	}

	@Override
	public boolean validateNumberOfWrappers(PolicySearchDTO policySearchDTO, int numberOfWrappers) {
		List<Policy> policies = policyDAO.getPoliciesByCriteria(policySearchDTO);
		if (policies == null || policies.size() == 0) {
			return true;
		}
		Policy policy = policies.get(0);
		if (policy.getValue() != null && numberOfWrappers > policy.getValue().intValue()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Loads all the drives configured in the File store paths then iterates recursively 
	 * through the disks & Directories.
	 * In the main while loop it checks whether all the files are present in the DB, and if not it throws an exception, 
	 * deletes the files and start all over again.
	 * 
	 *   MAX_FILES_BUFFER is defining the buffer size
	 */
	public void deleteFilesNotInDB() {

		Map<String, File> locationFilesMap = new HashMap<String, File>();
		List<String> drives = new ArrayList<String>();
		
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService
				.retrieveMetaData((Long) null, null, null, null, null);
		if (metadata.containsKey(MetaDataItemType.DEFAULT_USE_FILE_STORE)
				&& metadata.containsKey(MetaDataItemType.FILE_STORE_PATH)) {
			String pListPath = metadata.get(MetaDataItemType.FILE_STORE_PATH)
					.getValue();
			String[] splitList = pListPath.split(",");
			for (String path : splitList) {
				if ((new File(path)).exists()) {
					drives.add(path);
				}
			}
		}

		boolean interrupted = false;
		while (true) {
			for (String drive : drives) {
				try {
					traversePath(drive, locationFilesMap);
				} catch (Exception e) {
					interrupted = true;
					break;
				}
			}
			deleteOrphanFiles(locationFilesMap);
			if (interrupted) {
				interrupted = false;
			} else {
				break;
			}
		}
	}

	/**
	 * Traverses the directories tree
	 * 
	 * @param path
	 * @param map
	 * @throws Exception
	 */
	private void traversePath(String path, Map<String, File> map)
			throws Exception {
		File mainFile = (new File(path));
		File[] content = mainFile.listFiles();
		for (File file : content) {
			if (file.isDirectory()) {
				if(!EXCLUSION_DIRECTORIES.contains(file.getName())){
					traversePath(file.getAbsolutePath(), map);
				}
			} else {
				if (map.size() >= MAX_FILES_BUFFER) {
					if (!checkFiles(map)) {
						throw new Exception("Flow");
					}
				}
				map.put(file.getAbsolutePath().replace('\\', '/'), file);
			}
		}
	}

	/**
	 * Checks if all the files in the current map have an equivalent entry in
	 * the Message Binaries table
	 * 
	 * @param map
	 * @return
	 */
	private boolean checkFiles(Map<String, File> map) {
		Long count = 0L;
		List<String> list = new ArrayList<String>(map.keySet());
		if (list != null && list.size() > 0) {
			count = messageBinaryDao.getMessageBinaryCountByFilenames(list);
		}
		if (count == map.size()) {
			map.clear();
			return true;
		}
		return false;
	}

	/**
	 * Deletes the files that have no reference in the Message Binary table and
	 * clears the files Map filled in during traversal
	 * 
	 * @param map
	 */
	private void deleteOrphanFiles(Map<String, File> map) {
		if(map.isEmpty()){
			return;
		}
		List<MessageBinary> binaries = messageBinaryDao
				.getMessageBinaryByFilenames(new ArrayList<String>(map.keySet()));
		for (MessageBinary messageBinary : binaries) {
			map.remove(messageBinary.getFileId());
		}
		for (String key : map.keySet()) {
			map.get(key).delete();
			logger.info("--->Orphan Message Deleted:" + key);
		}
		map.clear();
	}

	@Override
	public List<Policy> getPoliciesByType(Class<? extends Policy> type) {
		return policyDAO.getPoliciesByType(type);
	}

	@Override
	public List<Policy> getAllPolicies() {
		return policyDAO.getAll();
	}

	@Override
	public List<Policy> getRetentionPolicies() {
		return policyDAO.getPoliciesByType(RetentionPolicy.class);
	}
	
	private boolean isWrapper(Policy policy){
		return PolicyHelper.WRAPPER_TX_NAME.equals(policy.getTransaction().getName());
	}

	public IPolicyDAO getPolicyDAO() {
		return policyDAO;
	}

	public void setPolicyDAO(IPolicyDAO policyDAO) {
		this.policyDAO = policyDAO;
	}

	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public IMessageBinaryDAO getMessageBinaryDao() {
		return messageBinaryDao;
	}

	public void setMessageBinaryDao(IMessageBinaryDAO messageBinaryDao) {
		this.messageBinaryDao = messageBinaryDao;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public PolicyHelper getHelper() {
		return helper;
	}

	public void setHelper(PolicyHelper helper) {
		this.helper = helper;
	}

}
