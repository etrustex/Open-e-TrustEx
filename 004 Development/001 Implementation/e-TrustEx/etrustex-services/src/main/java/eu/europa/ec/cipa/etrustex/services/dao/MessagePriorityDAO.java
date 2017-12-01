/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.Credentials_;
import eu.europa.ec.cipa.etrustex.domain.MessagePriority;
import eu.europa.ec.cipa.etrustex.domain.MessagePriority_;
import eu.europa.ec.cipa.etrustex.domain.Transaction_;

@Repository
public class MessagePriorityDAO extends TrustExDAO<MessagePriority, Long> implements IMessagePriorityDAO {

	/* (non-Javadoc)
	 * @see eu.europa.ec.cipa.etrustex.services.dao.IMessagePriorityDAO#findByUserAndTransaction()
	 */
	@Override
	public List<MessagePriority> findByUserAndTransaction(Long credentialId, Long transactionId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MessagePriority> cq = cb.createQuery(MessagePriority.class);
		Root<MessagePriority> root = cq.from(MessagePriority.class);
		cq.select(root);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		
		if (credentialId != null ) {
			predicate = cb.equal(root.get(MessagePriority_.credential).get(Credentials_.id), credentialId);
			predicates.add(predicate);
		}
		
		if (transactionId != null) {
			predicate = cb.equal(root.get(MessagePriority_.transaction).get(Transaction_.id), transactionId);
			predicates.add(predicate);
		}
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		TypedQuery<MessagePriority> query = entityManager.createQuery(cq);
		
		return query.getResultList();
	}

}
