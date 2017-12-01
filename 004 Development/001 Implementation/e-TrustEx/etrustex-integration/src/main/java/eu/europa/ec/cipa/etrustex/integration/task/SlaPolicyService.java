package eu.europa.ec.cipa.etrustex.integration.task;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.dao.IMessageBinaryDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ISlaPolicyDAO;
import eu.europa.ec.cipa.etrustex.services.dto.SlaPolicySearchDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import eu.europa.ec.cipa.etrustex.types.SlaType;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.*;

@Service
public class SlaPolicyService implements ISlaPolicyService {

	protected static final Logger logger = LoggerFactory.getLogger(SlaPolicyService.class);
		
	public static final String APP_RESP_DTC = "301";
	public static final String WRAPPER_DTC = "DWR";
	
	@Autowired
	private ISlaPolicyDAO slaPolicyDAO;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private ILogService logService;

	@Autowired
	private IMessageBinaryDAO messageBinaryDao;

	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IAuthorisationService authorisationService;
	
	@Autowired
	private SlaPolicyHelper helper;

	private static int MAX_FILES_BUFFER = 400;
	private static String EXCLUSION_DIRECTORIES = ".backups .snapshot";
	
	@Override
	public List<SlaPolicy> getPoliciesByCriteria(
			SlaPolicySearchDTO slaPolicySearchDTO) {
		return slaPolicyDAO.getPoliciesByCriteria(slaPolicySearchDTO);
	}

	@Override
	public void applyRetentionPolicies() {
		
		List<SlaPolicy> policies = null;

		Set<Transaction> childTransactionsToIgnore = new HashSet<Transaction>();
		childTransactionsToIgnore.addAll(authorisationService.getTransactionsByDocumentTypeCd(APP_RESP_DTC));
		childTransactionsToIgnore.addAll(authorisationService.getTransactionsByDocumentTypeCd(WRAPPER_DTC));

		policies = getPoliciesByType(SlaType.RETENTION_BUNDLE);
		logger.info("Number of RETENTION_BUNDLE found: " + policies.size());
		boolean doCleanup = false;
		boolean reLoop;
		// Iterates over SLA policies starting from the leaves
		for (SlaPolicy slaPolicy : policies) {
			if (slaPolicy.getActiveFlag()) {
				// If there there are no deletions for a specific SLA, it exits
				// the while loop otherwise climbs up to the parent
				reLoop = true;
				while (reLoop) {
					reLoop = false;
					if (handleBundleRetention(slaPolicy, childTransactionsToIgnore)) {
						reLoop = true;
					}
				}
			}
		}

		policies = getPoliciesByType(SlaType.RETENTION_WRAPPER);
		logger.info("Number of RETENTION_WRAPPER found: " + policies.size());
		for (SlaPolicy slaPolicy : policies) {
			if (slaPolicy.getActiveFlag()) {
				doCleanup = true;
				handleRetentionNoParent(slaPolicy);
			}
		}

		if(doCleanup){
			deleteFilesNotInDB();
		}
	}


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SlaPolicy create(SlaPolicy slaPolicy) {
        return slaPolicyDAO.create(slaPolicy);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SlaPolicy update(SlaPolicy slaPolicy) {
        return slaPolicyDAO.update(slaPolicy);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly=true)
    public SlaPolicy findById(Long id) {
        SlaPolicy slaPolicy = slaPolicyDAO.read(id);

        if(slaPolicy != null) {
            for(Profile p : slaPolicy.getBusinessDomain().getProfiles()) {
                Hibernate.initialize(p);
            }
        }

        return slaPolicy;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly=true)
    public List<SlaPolicy> findPoliciesByCriteriaForView(SlaPolicy slaPolicy, List<BusinessDomain> businessDomains) {
        return slaPolicyDAO.findPoliciesByCriteriaForView(slaPolicy, businessDomains);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        slaPolicyDAO.delete(slaPolicyDAO.read(id));
    }

	/**
	 * Specific Method for Wrappers
	 * 
	 * @param slaPolicy
	 */
	private void handleRetentionNoParent(SlaPolicy slaPolicy) {
		Date startDate = DateUtils.addYears(GregorianCalendar.getInstance()
				.getTime(), -20);
		Date endDate = DateUtils.addDays(GregorianCalendar.getInstance()
				.getTime(), -slaPolicy.getValue()); boolean loop = true;
		while(loop){			
			loop = false;
			List<Message> messages = messageService.retrieveOrphans(
				slaPolicy.getBusinessDomain(), slaPolicy.getTransaction(),
				startDate, endDate);
			
			if(messages.size() == 500)
				loop = true;
			for (Message message : messages) {
				try {
					helper.deleteMessage(message.getId());
				} catch (MessageUpdateException | JAXBException e) {
					logger.error("Could not delete message with doc Id: "+message.getDocumentId(), e);
				}
			}
		}
	}



	/**
	 * 
	 * @param slaPolicy
	 * @return
	 */
	private boolean handleBundleRetention(SlaPolicy slaPolicy, Set<Transaction> childTransactionsToIgnore) {
		Date startDate = DateUtils.addYears(GregorianCalendar.getInstance()
				.getTime(), -20);
		Date endDate = DateUtils.addDays(GregorianCalendar.getInstance()
				.getTime(), -slaPolicy.getValue());

		List<Message> messages = messageService.retrieveLeaves(
				slaPolicy.getBusinessDomain(), slaPolicy.getTransaction(),
				startDate, endDate, childTransactionsToIgnore);
		
		boolean hasMessages = (messages.size() > 0) ? true : false;
		
		if (hasMessages) {
			for (Message message : messages) {
				try {
					helper.deleteBundle(message.getId());
				} catch (MessageUpdateException e) {
					logger.error("Could not delete message with doc Id: "+message.getDocumentId(), e);
				}
			}
		}
		return hasMessages;
	}
	
	@Override
	public boolean validateWrapperVolume(SlaPolicySearchDTO slaPolicySearchDTO) {
		List<SlaPolicy> policies = slaPolicyDAO.getPoliciesByCriteria(slaPolicySearchDTO);
		if (policies == null || policies.size() == 0) {
			return true;
		}
		SlaPolicy policy = policies.get(0);
		//retrieve the volume of data sent by sender since the beginning of the period
		long accumulatedVolumeInMB = logService.getVolumeCountForParty(slaPolicySearchDTO.getSender().getId(), policy.getFrequency())/1048576;
		if (accumulatedVolumeInMB > policy.getValue()) {
			return false;
		}
		return true;
		
	}

	@Override
	public boolean validateNumberOfWrappers(SlaPolicySearchDTO slaPolicySearchDTO, int numberOfWrappers) {
		List<SlaPolicy> policies = slaPolicyDAO.getPoliciesByCriteria(slaPolicySearchDTO);
		if (policies == null || policies.size() == 0) {
			return true;
		}
		SlaPolicy policy = policies.get(0);
		if (policy.getValue() != null && numberOfWrappers > policy.getValue().intValue()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean validateWrapperSize(SlaPolicySearchDTO slaPolicySearchDTO, int wrapperSize) {
		List<SlaPolicy> policies = slaPolicyDAO.getPoliciesByCriteria(slaPolicySearchDTO);
		if (policies == null || policies.size() == 0) {
			return true;
		}
		SlaPolicy policy = policies.get(0);
		if (policy.getValue() != null && wrapperSize > policy.getValue().intValue()) {
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
				.retrieveMetaData((Long) null, null, null, null);
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
				map.put(file.getAbsolutePath(), file);
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
	public List<SlaPolicy> getPoliciesByType(SlaType type) {
		List<SlaType> types = new ArrayList<SlaType>(Arrays.asList(type));
		return slaPolicyDAO.getPoliciesByType(types);
	}

	@Override
	public List<SlaPolicy> getAllPolicies() {
		return slaPolicyDAO.getAll();
	}

	@Override
	public List<SlaPolicy> getRetentionPolicies() {
		List<SlaType> types = new ArrayList<SlaType>(Arrays.asList(
				SlaType.RETENTION_BUNDLE, SlaType.RETENTION_WRAPPER));
		return slaPolicyDAO.getPoliciesByType(types);
	}

	@Override
	public List<SlaPolicy> getSlaPolicies() {
		List<SlaType> types = new ArrayList<SlaType>(Arrays.asList(
				SlaType.SLA_VOLUME, SlaType.SLA_COUNT));
		return slaPolicyDAO.getPoliciesByType(types);
	}

	public ISlaPolicyDAO getSlaPolicyDAO() {
		return slaPolicyDAO;
	}

	public void setSlaPolicyDAO(ISlaPolicyDAO slaPolicyDAO) {
		this.slaPolicyDAO = slaPolicyDAO;
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

	public SlaPolicyHelper getHelper() {
		return helper;
	}

	public void setHelper(SlaPolicyHelper helper) {
		this.helper = helper;
	}

}
