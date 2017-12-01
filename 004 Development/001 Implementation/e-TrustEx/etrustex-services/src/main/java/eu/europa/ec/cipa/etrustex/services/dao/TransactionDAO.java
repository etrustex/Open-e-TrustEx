package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.log.Log;
import eu.europa.ec.cipa.etrustex.domain.log.Log_;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint_;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy_;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode_;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem_;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class TransactionDAO extends TrustExDAO<Transaction, Long> implements
		ITransactionDAO {

	@Override
	public Transaction getTransactionByNameSpace(String transactionNameSpace,String transactionRequestLocalName){
		List<Transaction> results= entityManager.createQuery(
		"from Transaction tra where tra.namespace= :transactionNameSpace and tra.requestLocalName= :transactionRequestLocalName",Transaction.class).setParameter("transactionNameSpace", transactionNameSpace).
		setParameter("transactionRequestLocalName", transactionRequestLocalName).getResultList();
		if (results != null && results.size() == 1) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Transaction> getTransactionsByDocumentTypeCd(
			String documentTypeCode) {
		return entityManager.createQuery(
				"from Transaction tra where tra.document.documentTypeCode= :documentTypeCode ",Transaction.class).
				setParameter("documentTypeCode", documentTypeCode).getResultList();
	}

	@Override
	public List<Transaction> getTransactionsForDocument(
			String documentNamespace, String documentlocalName) {
		return entityManager.createQuery(
				"from Transaction tra where tra.document.localName= :documentlocalName and tra.document.namespace = :documentNamespace ",Transaction.class).
				setParameter("documentlocalName", documentlocalName).setParameter("documentNamespace", documentNamespace).getResultList();
	}

	@Override
	public List<Transaction> getAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Transaction> criteriaQuery = builder.createQuery(Transaction.class);
		Root<Transaction> r = criteriaQuery.from(Transaction.class);
		criteriaQuery.orderBy(builder.asc(builder.lower(r.get(Transaction_.name))));

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<Transaction> getTransactionsByNameAndVersion(String name, String version) {
		return entityManager.createQuery("from Transaction tx where tx.name = :name and tx.version = :version ", Transaction.class)
				.setParameter("name", name)
				.setParameter("version", version)
				.getResultList();
	}

	@Override
	public List<Transaction> getTransactionsByNamespaceAndRequestLocalName(String namespace, String requestLocalName) {
		return entityManager.createQuery("from Transaction tx where tx.namespace = :namespace and tx.requestLocalName = :requestLocalName ", Transaction.class)
				.setParameter("namespace", namespace)
				.setParameter("requestLocalName", requestLocalName)
				.getResultList();
	}

	@Override
	public List<Transaction> getTransactionsByCriteria(Transaction transaction) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
		Root<Transaction> tx = cq.from(Transaction.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if(StringUtils.isNotEmpty(transaction.getName())) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(tx.get(Transaction_.name))), "%" + transaction.getName().trim().toUpperCase() + "%"));
		}

		if(StringUtils.isNotEmpty(transaction.getNamespace())) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(tx.get(Transaction_.namespace))), "%" + transaction.getNamespace().trim().toUpperCase() + "%"));
		}

		if(StringUtils.isNotEmpty(transaction.getRequestLocalName())) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(tx.get(Transaction_.requestLocalName))), "%" + transaction.getRequestLocalName().trim().toUpperCase() + "%"));
		}

		if(transaction.getDocument() != null && StringUtils.isNotEmpty(transaction.getDocument().getName())) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(tx.get(Transaction_.document).get(Document_.name))), "%" + transaction.getDocument().getName().trim().toUpperCase() + "%"));
		}

		/*
		 * If selected profile is "Any", profiles = null. CIPADMIN-459 Option "Any" – search for transactions pertaining to any or no profile
		 * If selected profile is "None", profiles = empty
		 */
		if(transaction.getProfiles() != null) {
		 	if(CollectionUtils.isEmpty(transaction.getProfiles())) { // None
				predicates.add(cb.isEmpty(tx.get(Transaction_.profiles)));
			} else {
                List<Predicate> profilePredicates = new ArrayList<>();

                for (Profile p : transaction.getProfiles()) {
                    profilePredicates.add(cb.isMember(p, tx.get(Transaction_.profiles)));
                }

                predicates.add(cb.and(profilePredicates.toArray(new Predicate[profilePredicates.size()])));
            }
		}

		if(transaction.getSenderRole() != null) {
			predicates.add(cb.equal(tx.get(Transaction_.senderRole), transaction.getSenderRole()));
		}

		if(transaction.getReceiverRole() != null) {
			predicates.add(cb.equal(tx.get(Transaction_.receiverRole), transaction.getReceiverRole()));
		}

		cq.select(tx).distinct(true);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		cq.orderBy(cb.asc(cb.lower(tx.get(Transaction_.name))));

		return entityManager.createQuery(cq).getResultList();
	}

	/*
	 * The owner side is the one without the mappedBy attribute, i.e. Profile.
	 * JPA/Hibernate only cares about the owner side
	 */
	@Override
	public Transaction create(Transaction tx) {
//		Set<Profile> profiles = tx.getProfiles();
		for(Profile p : tx.getProfiles()) {
			entityManager.find(Profile.class, p.getId()).getTransactions().add(tx);
		}

		return super.create(tx);
	}

	/*
	 * Same as for create (avobe) and check if profiles have been removed from tx.
	 */
	@Override
	public Transaction update(Transaction tx) {
		Set<Profile> previousProfiles = new HashSet<Profile>(entityManager.find(Transaction.class, tx.getId()).getProfiles());
		Set<Profile> newProfiles = tx.getProfiles();

		for(Profile p : newProfiles) {
			if(!previousProfiles.contains(p)) {
				Profile p2 = entityManager.find(Profile.class, p.getId());
				p2.getTransactions().add(tx);
			}
		}

		// UC70_BR17) TODO CHECK
		/*boolean profileBelongsToOtherBD = false;

		for(Profile p : previousProfiles) {
			if( !newProfiles.contains(p)

					&& (newProfiles.isEmpty() && p.getBusinessDomains().size() > 1) ) {
				Profile p2 = entityManager.find(Profile.class, p.getId());
				p2.getTransactions().remove(tx);
			}
		}*/

		for(Profile p : previousProfiles) {
			if(!newProfiles.contains(p)) {
				Profile p2 = entityManager.find(Profile.class, p.getId());
				p2.getTransactions().remove(tx);
			}
		}

		return super.update(tx);
	}

	@Override
	public boolean isInUse(Transaction tx) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Endpoint> endpointRoot = cq.from(Endpoint.class);
        cq.select(cb.count(endpointRoot));
        cq.where(cb.equal(endpointRoot.get(Endpoint_.tansaction), tx));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<Log> logRoot = cq.from(Log.class);
        cq.select(cb.count(logRoot));
        cq.where(cb.equal(logRoot.get(Log_.transaction), tx));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<Message> messageRoot = cq.from(Message.class);
        cq.select(cb.count(messageRoot));
        cq.where(cb.equal(messageRoot.get(Message_.transaction), tx));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<MetaDataItem> metadataRoot = cq.from(MetaDataItem.class);
        cq.select(cb.count(metadataRoot));
        cq.where(cb.equal(metadataRoot.get(MetaDataItem_.tansaction), tx));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<MessageResponseCode> messageResponseCodeRoot = cq.from(MessageResponseCode.class);
        cq.select(cb.count(messageResponseCodeRoot));
        cq.where(cb.equal(messageResponseCodeRoot.get(MessageResponseCode_.tansaction), tx));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<SlaPolicy> policyRoot = cq.from(SlaPolicy.class);
        cq.select(cb.count(policyRoot));
        cq.where(cb.equal(policyRoot.get(SlaPolicy_.transaction), tx));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<PartyAgreement> partyAgreementRoot = cq.from(PartyAgreement.class);
        cq.select(cb.count(partyAgreementRoot));
        cq.where(cb.isMember(tx, partyAgreementRoot.get(PartyAgreement_.transactions)));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<Profile> profileRoot = cq.from(Profile.class);
        cq.select(cb.count(profileRoot));
        cq.where(cb.isMember(tx, profileRoot.get(Profile_.transactions)));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        cq = cb.createQuery(Long.class);
        Root<ServiceEndpoint> serviceEndpointRoot = cq.from(ServiceEndpoint.class);
        cq.select(cb.count(serviceEndpointRoot));
        cq.where(cb.isMember(tx, serviceEndpointRoot.get(ServiceEndpoint_.transactions)));
        if (entityManager.createQuery(cq).getSingleResult() > 0) {
            return true;
        }

        return false;
	}
}