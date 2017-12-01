package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.CIALevel;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.dao.ICIALevelDAO;
import eu.europa.ec.cipa.etrustex.services.dao.IProfileDAO;
import eu.europa.ec.cipa.etrustex.services.dao.ITransactionDAO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TransactionService implements ITransactionService {
	@Autowired private ITransactionDAO transactionDAO;
	@Autowired private ICIALevelDAO ciaLevelDAO;
	@Autowired private IProfileDAO profileDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Transaction create(Transaction transaction) {
		transaction.setCiaLevel(getCiaLevel(transaction));
		
		return transactionDAO.create(transaction);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Transaction update(Transaction transaction) {
		transaction.setCiaLevel(getCiaLevel(transaction));
		
		return transactionDAO.update(transaction);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Transaction getTransaction(Long transactionId) {
		Transaction tx = transactionDAO.read(transactionId);

		if(tx != null) {
			Hibernate.initialize(tx.getDocument());
			for(Profile p : tx.getProfiles()) {
				Hibernate.initialize(p);
			}
			Hibernate.initialize(tx.getReceiverRole());
			Hibernate.initialize(tx.getSenderRole());
			Hibernate.initialize(tx.getCiaLevel());
		}

		return tx;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long transactionId) {
		/*
		 * The owner side is the one without the mappedBy attribute, i.e. Profile. 
		 * JPA/Hibernate only cares about the owner side
		 */
		Transaction tx = transactionDAO.read(transactionId);
		for(Profile p : tx.getProfiles()) {
			p.getTransactions().remove(tx);
			profileDAO.update(p);
		}
		
		transactionDAO.delete(tx);
	}

	@Override
	public List<Transaction> getTransactionsByNameAndVersion(String name, String version) {
		return transactionDAO.getTransactionsByNameAndVersion(name, version);
	}

	@Override
	public List<Transaction> getTransactionsByNamespaceAndRequestLocalName(String namespace, String requestLocalName) {
		return transactionDAO.getTransactionsByNamespaceAndRequestLocalName(namespace, requestLocalName);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Transaction> getTransactionsByCriteria(Transaction transaction) {
		List<Transaction> list = transactionDAO.getTransactionsByCriteria(transaction);
		
		for(Transaction tx : list) {
			Hibernate.initialize(tx.getDocument());
			for(Profile p : tx.getProfiles()) {
				Hibernate.initialize(p);
			}
			Hibernate.initialize(tx.getReceiverRole());
			Hibernate.initialize(tx.getSenderRole());
		}
		
		return list;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public List<Transaction> getAll() {
		return transactionDAO.getAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public Boolean isInUse(Transaction tx) {
		return transactionDAO.isInUse(tx);
	}
	
	private CIALevel getCiaLevel(Transaction transaction) {
		CIALevel ciaLevel;
		List<CIALevel> ciaLevels = ciaLevelDAO.retrieveCIALevel(transaction.getCiaLevel().getConfidentialityLevel(), 
				transaction.getCiaLevel().getIntegrityLevel(), transaction.getCiaLevel().getAvailabilityLevel());
		if(ciaLevels.isEmpty()) {
			ciaLevel = new CIALevel();
			ciaLevel.setConfidentialityLevel(transaction.getCiaLevel().getConfidentialityLevel());
			ciaLevel.setIntegrityLevel(transaction.getCiaLevel().getIntegrityLevel());
			ciaLevel.setAvailabilityLevel(transaction.getCiaLevel().getAvailabilityLevel());
			
			EntityAccessInfo eai = new EntityAccessInfo();
			eai.setCreationId(transaction.getAccessInfo().getCreationId());
			ciaLevel.setAccessInfo(eai);
			
			ciaLevel = ciaLevelDAO.create(ciaLevel);
		} else {
			ciaLevel = ciaLevels.get(0);
		}
		
		return ciaLevel;
	}
}