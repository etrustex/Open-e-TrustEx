/**
 * 
 */
package eu.europa.ec.etrustex.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IMessagePriorityDAO;
import eu.europa.ec.etrustex.domain.MessagePriority;
import eu.europa.ec.etrustex.domain.MessagePriority_;
import eu.europa.ec.etrustex.domain.Party_;
import eu.europa.ec.etrustex.domain.Transaction_;

@Repository
public class MessagePriorityDAO extends TrustExDAO<MessagePriority, Long> implements IMessagePriorityDAO {
	
	@Override
	public List<MessagePriority> findBySenderOrTransaction(Long senderId, Long transactionId){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MessagePriority> cq = cb.createQuery(MessagePriority.class);
		Root<MessagePriority> root = cq.from(MessagePriority.class);
		cq.select(root);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		
		predicate = cb.and(cb.equal(root.get(MessagePriority_.sender).get(Party_.id), senderId),cb.equal(root.get(MessagePriority_.transaction).get(Transaction_.id), transactionId));
		predicates.add(predicate);
		
		predicate = cb.and(cb.equal(root.get(MessagePriority_.sender).get(Party_.id), senderId),cb.isNull(root.get(MessagePriority_.transaction).get(Transaction_.id)));
		predicates.add(predicate);
		
		predicate = cb.and(cb.isNull(root.get(MessagePriority_.sender).get(Party_.id)),cb.equal(root.get(MessagePriority_.transaction).get(Transaction_.id), transactionId));
		predicates.add(predicate);

		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.or(predicates.toArray(preds));
		cq.where(clause);
		TypedQuery<MessagePriority> query = entityManager.createQuery(cq);
		
		return query.getResultList();
	}

}
