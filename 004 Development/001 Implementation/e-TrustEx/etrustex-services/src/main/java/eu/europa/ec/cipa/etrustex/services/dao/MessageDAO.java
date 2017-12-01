package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;
import eu.europa.ec.cipa.etrustex.domain.query.QueryResult_;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint_;
import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting_;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo_;
import eu.europa.ec.cipa.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.cipa.etrustex.services.util.DispatchEnum;
import eu.europa.ec.cipa.etrustex.types.DataExtractionTypes;
import eu.europa.ec.cipa.etrustex.types.SortFieldTypeCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class MessageDAO extends TrustExDAO<Message, Long> implements IMessageDAO {

	private String insertMessageQuery;

	private String selectMessageQuery;

	@Autowired
	private DataSource eTrustExDS;

	@Override
	public Message createMessage(Message newMessage) throws SQLException {
		newMessage = create(newMessage);
		entityManager.flush();
		return newMessage;

	}
	
	@Override
	public Message retrieveMessage(String messageDocumentId, String documentTypeCode, Long senderId, Long receiverId){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		cq.select(message);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		
		if (messageDocumentId != null ) {
			predicate = cb.equal(message.get(Message_.documentId), messageDocumentId);
			predicates.add(predicate);
		}
		
		if (documentTypeCode != null) {
			predicate = cb.equal(message.get(Message_.messageDocumentTypeCode), documentTypeCode);
			predicates.add(predicate);
		}
		
		if (senderId != null) {
			predicate = cb.equal(message.get(Message_.sender), senderId);
			predicates.add(predicate);
		}
		if (receiverId != null) {
			predicate = cb.equal(message.get(Message_.receiver), receiverId);
			predicates.add(predicate);
		}
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		TypedQuery<Message> query = entityManager.createQuery(cq);
		
		List<Message> messages = query.getResultList();
		return CollectionUtils.isNotEmpty(messages) ? messages.get(0) : null;
	}

	@Override
	public Message retrieveMessage(String messageDocumentId, Long senderId, Long transactionTypeId) {
		List<Message> results = entityManager
				.createQuery("from Message msg where msg.sender.id =:messageIcaId and msg.documentId =:documentId and msg.transaction.id=:transactionTypeId", Message.class)
				.setParameter("messageIcaId", senderId).setParameter("documentId", messageDocumentId).setParameter("transactionTypeId", transactionTypeId).getResultList();
		if (results != null && results.size() == 1) {
			return results.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Message> retrieveMessages(String messageDocumentId, String documentTypeCode, Long senderId, Long receiverId, Boolean biDirectional, Set<String> statesToExclude) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		cq.select(message);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		if (messageDocumentId != null) {
			predicate = cb.equal(message.get(Message_.documentId), messageDocumentId);
			predicates.add(predicate);
		}
		if (documentTypeCode != null) {
			predicate = cb.equal(message.get(Message_.messageDocumentTypeCode), documentTypeCode);
			predicates.add(predicate);
		}

		// Exclusions
		if (statesToExclude != null && statesToExclude.size() > 0) {
			predicate = cb.not(message.get(Message_.statusCode).in(statesToExclude));
			predicates.add(predicate);
		}

		// Combinations Sender/Receiver
		Predicate p1;
		Predicate p2;
		if (biDirectional) {
			if (senderId != null && receiverId != null) {
				p1 = cb.and(cb.equal(message.get(Message_.sender), senderId), cb.equal(message.get(Message_.receiver), receiverId));
				p2 = cb.and(cb.equal(message.get(Message_.sender), receiverId), cb.equal(message.get(Message_.receiver), senderId));
				predicate = cb.or(p1, p2);
				predicates.add(predicate);
			}
		} else {
			if (senderId != null) {
				predicate = cb.equal(message.get(Message_.sender), senderId);
				predicates.add(predicate);
			}
			if (receiverId != null) {
				predicate = cb.equal(message.get(Message_.receiver), receiverId);
				predicates.add(predicate);
			}
		}
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);

		TypedQuery<Message> query = entityManager.createQuery(cq);
		query.setMaxResults(10);

		return query.getResultList();
	}
	
	
	@Override
	public List<Message> retrieveMessages(Long receiverPartyId, Long senderPartyId, Long issuerPartyId, Long icaId, Set<Transaction> transactions, Integer maxResult, String messageDocumentId,
			String documentTypeCode, Boolean retrievedInd, Boolean fetchParents, Boolean fetchBinaries, Boolean filterOutMessagesInError) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		// fetching the parents or the binaries might lead to duplicates in the
		// results due to the left outer join, we use distinct in tha case
		if (fetchParents) {
			cq.distinct(true);
			message.fetch(Message_.parentMessages, JoinType.LEFT);
		}
		if (fetchBinaries) {
			cq.distinct(true);
			message.fetch(Message_.binaries);
		}

		cq.select(message);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		if (receiverPartyId != null) {
			predicate = cb.equal(message.get(Message_.receiver), receiverPartyId);
			predicates.add(predicate);

		}
		if (messageDocumentId != null) {
			predicate = cb.equal(message.get(Message_.documentId), messageDocumentId);
			predicates.add(predicate);

		}
		if (senderPartyId != null) {
			predicate = cb.equal(message.get(Message_.sender), senderPartyId);
			predicates.add(predicate);
		}
		if (issuerPartyId != null) {
			predicate = cb.equal(message.get(Message_.issuer), issuerPartyId);
			predicates.add(predicate);
		}
		if (icaId != null) {
			predicate = cb.equal(message.get(Message_.agreement), icaId);
			predicates.add(predicate);
		}

		if (documentTypeCode != null) {
			predicate = cb.equal(message.get(Message_.messageDocumentTypeCode), documentTypeCode);
			predicates.add(predicate);
		}

		if (retrievedInd != null) {
			predicate = cb.equal(message.get(Message_.retrieveIndicator), retrievedInd);
			predicates.add(predicate);
		}

		if (transactions != null && transactions.size() > 0) {
			predicate = message.get(Message_.transaction).in(transactions);
			predicates.add(predicate);
		}
		
		//filter out submitted state anyway
		
		predicate = cb.notEqual(message.get(Message_.statusCode), "SUBMITTED");
		predicates.add(predicate);

		if (predicates.size() > 0) {
			if (filterOutMessagesInError) {
				predicate = cb.notEqual(message.get(Message_.statusCode), "ERROR");
				predicates.add(predicate);
			}
			Predicate[] preds = new Predicate[predicates.size()];
			Predicate clause = cb.and(predicates.toArray(preds));
			cq.where(clause);
			TypedQuery<Message> query = entityManager.createQuery(cq);

			if (maxResult != null) {
				query.setMaxResults(maxResult);
			}
			return query.getResultList();
		} else {
			return null;
		}

	}

	private void populateQuery(CriteriaBuilder cb, CriteriaQuery cq, Root<Message> message, Set<Long> receiverPartyIds, Set<Long> senderPartyIds, Set<Long> receiverPartyIds2,
			Set<Long> senderPartyIds2, Long issuerPartyId, Set<Transaction> transactions, Set<String> documentTypeCodes, String documentId, Boolean retrievedInd, Date startDate, Date endDate,
			Set<String> correlationIds, Boolean isSenderAlsoReceiver, String userId, Set<String> businessDocumentType, Boolean filterOutMessagesInError, Boolean hasParents) {

		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate senderpredicate = null;
		Predicate receiverPredicate = null;
		Predicate predicate = null;

		if (receiverPartyIds2 == null) {
			if (isSenderAlsoReceiver && senderPartyIds != null && !senderPartyIds.isEmpty()) {
				receiverPredicate = message.get(Message_.receiver).in(senderPartyIds);
				senderpredicate = message.get(Message_.sender).in(senderPartyIds);
				predicate = cb.or(senderpredicate, receiverPredicate);
				predicates.add(predicate);
			} else {
				if (senderPartyIds != null && !senderPartyIds.isEmpty()) {
					predicate = message.get(Message_.sender).in(senderPartyIds);
					predicates.add(predicate);
				}
				if (receiverPartyIds != null && !receiverPartyIds.isEmpty()) {
					predicate = message.get(Message_.receiver).in(receiverPartyIds);
					predicates.add(predicate);
				}
			}
		} else {
			receiverPredicate = cb.and(message.get(Message_.receiver).in(receiverPartyIds), message.get(Message_.sender).in(senderPartyIds));
			senderpredicate = cb.and(message.get(Message_.receiver).in(receiverPartyIds2), message.get(Message_.sender).in(senderPartyIds2));
			predicate = cb.or(senderpredicate, receiverPredicate);
			predicates.add(predicate);
		}

		if (documentId != null) {
			predicate = cb.equal(message.get(Message_.documentId), documentId);
			predicates.add(predicate);
		}
		if (correlationIds != null && !correlationIds.isEmpty()) {
			predicate = message.get(Message_.correlationId).in(correlationIds);
			predicates.add(predicate);
		}
		if (issuerPartyId != null) {
			predicate = cb.equal(message.get(Message_.issuer), issuerPartyId);
			predicates.add(predicate);
		}

		if (startDate != null && endDate != null) {
			predicate = cb.between(message.get(Message_.receptionDate), startDate, endDate);
			predicates.add(predicate);
		}

		if (transactions != null && transactions.size() > 0) {
			predicate = message.get(Message_.transaction).in(transactions);
			predicates.add(predicate);
		}
		if (documentTypeCodes != null && documentTypeCodes.size() > 0) {
			predicate = message.get(Message_.messageDocumentTypeCode).in(documentTypeCodes);
			predicates.add(predicate);
		}

		if (retrievedInd != null) {
			predicate = cb.equal(message.get(Message_.retrieveIndicator), retrievedInd);
			predicates.add(predicate);
		}

		if (userId != null) {
			SetJoin<Message, QueryResult> qResultUser = message.join(Message_.additionalInfo, JoinType.LEFT);
			predicate = cb.and(cb.equal(qResultUser.get(QueryResult_.key), DataExtractionTypes.JMS_USER_ID.name()), cb.equal(qResultUser.get(QueryResult_.value), userId));
			predicates.add(predicate);
		}
		if (businessDocumentType != null && businessDocumentType.size() > 0) {
			SetJoin<Message, QueryResult> qResultBdt = message.join(Message_.additionalInfo, JoinType.LEFT);
			// predicate = cb.and(cb.equal(qResultBdt.get(QueryResult_.key),
			// DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name()),
			// cb.equal(qResultBdt.get(QueryResult_.value),
			// businessDocumentType));
			predicate = cb.and(cb.equal(qResultBdt.get(QueryResult_.key), DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name()), qResultBdt.get(QueryResult_.value).in(businessDocumentType));
			predicates.add(predicate);
		}

		//filter out submitted documents anyway
		
		predicate = cb.notEqual(message.get(Message_.statusCode), "SUBMITTED");
		predicates.add(predicate);
		
		if (filterOutMessagesInError) {
			predicate = cb.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
		}

		if (hasParents != null) {
			if (hasParents.booleanValue()) {
				predicate = cb.isNotEmpty(message.get(Message_.parentMessages));
				predicates.add(predicate);
			} else {
				predicate = cb.isEmpty(message.get(Message_.parentMessages));
				predicates.add(predicate);
			}
		}

		if (predicates.size() > 0) {
			Predicate[] preds = new Predicate[predicates.size()];
			Predicate clause = cb.and(predicates.toArray(preds));
			cq.where(clause);
		}
	}

	@Override
	public List<Message> retrieveMessages(Set<Long> receiverPartyIds, Set<Long> senderPartyIds, Set<Long> receiverPartyIds2, Set<Long> senderPartyIds2, Long issuerPartyId,
			Set<Transaction> transactions, Set<String> documentTypeCodes, String documentId, Boolean retrievedInd, Date startDate, Date endDate, Set<String> correlationIds, Integer maxResult,
			Boolean fetchParents, Boolean fetchBinaries, Boolean isSenderAlsoReceiver, Boolean filterOutMessagesInError, Boolean hasParents) {

		CriteriaBuilder cbList = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cqList = cbList.createQuery(Message.class);
		Root<Message> message = cqList.from(Message.class);

		// fetching the parents or the binaries might lead to duplicates in the
		// results due to the left outer join, we use distinct in tha case
		if (fetchParents) {
			message.fetch(Message_.parentMessages, JoinType.LEFT);
		}

		if (fetchBinaries) {
			message.fetch(Message_.binaries);
		}

		populateQuery(cbList, cqList, message, receiverPartyIds, senderPartyIds, receiverPartyIds2, senderPartyIds2, issuerPartyId, transactions, documentTypeCodes, documentId, retrievedInd,
				startDate, endDate, correlationIds, isSenderAlsoReceiver, null, null, filterOutMessagesInError, hasParents);

		cqList.distinct(true);
		cqList.select(message);
		TypedQuery<Message> queryForResults = entityManager.createQuery(cqList);

		// Sorting
		List<Order> order = new ArrayList<Order>();
		order.add(0, cbList.asc(message.get(Message_.correlationId)));
		order.add(1, cbList.asc(message.get(Message_.issueDate)));
		cqList.orderBy(order);

		if (maxResult != null) {
			queryForResults.setMaxResults(maxResult);
		}

		return queryForResults.getResultList();
	}
	
	@Override
	public MessagesListVO retrieveMessagesJustice(Set<Long> receiverPartyIds, Set<Long> senderPartyIds, Set<Long> receiverPartyIds2, Set<Long> senderPartyIds2, Long issuerPartyId,
			Set<Transaction> transactions, Set<String> documentTypeCodes, String documentId, Boolean retrievedInd, Date startDate, Date endDate, Set<String> correlationIds, Integer maxResult,
			Boolean fetchParents, Boolean fetchBinaries, Boolean isSenderAlsoReceiver, String userId, Set<String> businessDocumentType, BigDecimal paginationFrom, BigDecimal paginationTo,
			SortFieldTypeCode sortField, Boolean filterOutMessagesInError) {

		CriteriaBuilder cbList = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> cqList = cbList.createTupleQuery();
		Root<Message> message = cqList.from(Message.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		// if errors messages must be included we have to add them to the result
		// independently of the business document types
		List<Predicate> forErrorMessagePredicates = new ArrayList<Predicate>();

		Predicate senderpredicate = null;
		Predicate receiverPredicate = null;
		Predicate predicate = null;

		// nfor justice messages shouc be the one send by dg just or received by
		// dg just
		receiverPredicate = message.get(Message_.receiver).in(senderPartyIds);
		senderpredicate = message.get(Message_.sender).in(senderPartyIds);
		predicate = cbList.or(senderpredicate, receiverPredicate);
		predicates.add(predicate);
		forErrorMessagePredicates.add(predicate);

		SetJoin<Message, QueryResult> qResultile = message.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbList.and(cbList.equal(qResultile.get(QueryResult_.key), DataExtractionTypes.JMS_TITLE.name()));
		predicates.add(predicate);
		forErrorMessagePredicates.add(predicate);

		SetJoin<Message, QueryResult> qResultUser = message.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbList.and(cbList.equal(qResultUser.get(QueryResult_.key), DataExtractionTypes.JMS_USER_ID.name()), cbList.equal(qResultUser.get(QueryResult_.value), userId));
		predicates.add(predicate);
		forErrorMessagePredicates.add(predicate);

		if (businessDocumentType != null && businessDocumentType.size() > 0) {

			Subquery<Date> sq = cqList.subquery(Date.class);
			Root<Message> s2 = sq.from(Message.class);
			sq.select(cbList.greatest(s2.get(Message_.issueDate)));
			sq.where(cbList.equal(s2.get(Message_.correlationId), message.get(Message_.correlationId)));
			predicate = cbList.equal(message.get(Message_.issueDate), sq);
			predicates.add(predicate);

			SetJoin<Message, QueryResult> qResultBdt = message.join(Message_.additionalInfo, JoinType.LEFT);
			predicate = cbList.and(cbList.equal(qResultBdt.get(QueryResult_.key), DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name()), qResultBdt.get(QueryResult_.value).in(businessDocumentType));

			predicates.add(predicate);
		}

		if (startDate != null && endDate != null) {
			predicate = cbList.between(message.get(Message_.issueDate), startDate, endDate);
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}

		if (retrievedInd != null) {
			predicate = cbList.equal(message.get(Message_.retrieveIndicator), retrievedInd);
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}

		if (filterOutMessagesInError) {
			predicate = cbList.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}
		if (correlationIds != null && !correlationIds.isEmpty()) {
			predicate = message.get(Message_.correlationId).in(correlationIds);
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}

		if (predicates.size() > 0) {
			Predicate[] preds = new Predicate[predicates.size()];

			if (filterOutMessagesInError) {
				Predicate clause = cbList.and(predicates.toArray(preds));
				cqList.where(clause);
			} else {
				predicate = cbList.equal(message.get(Message_.statusCode), "ERROR");
				forErrorMessagePredicates.add(predicate);
				Predicate[] forErrorMessagePreds = new Predicate[forErrorMessagePredicates.size()];
				Predicate clause = cbList.or(cbList.and(predicates.toArray(preds)), cbList.and(forErrorMessagePredicates.toArray(forErrorMessagePreds)));
				cqList.where(clause);
			}

		}

		cqList.distinct(true);

		cqList.select(cbList.tuple(message.get(Message_.correlationId), message.get(Message_.issueDate), qResultUser.get("value"), qResultile.get("value")));

		if (sortField != null) {
			if (SortFieldTypeCode.TITLE.equals(sortField)) {
				cqList.orderBy(cbList.asc(qResultile.get("value")));
			} else if (SortFieldTypeCode.SUBMISSION_DATE.equals(sortField)) {
				cqList.orderBy(cbList.desc(message.get(Message_.issueDate)));
			}
		} else {
			cqList.orderBy(cbList.desc(message.get(Message_.issueDate)));
		}

		TypedQuery<Tuple> queryForResults = entityManager.createQuery(cqList);

		List<Tuple> list = queryForResults.getResultList();

		LinkedHashSet<String> corrIDs = new LinkedHashSet<String>();

		for (Tuple tuple : list) {
			String correlationID = (String) tuple.get(0);
			corrIDs.add(correlationID);
		}

		Integer totalConversations = corrIDs.size();

		List<String> forpaging = new ArrayList<String>(corrIDs);

		if ((paginationFrom != null) && (paginationTo != null)) {

			int start = ((paginationFrom.intValue() - 1) > forpaging.size()) ? forpaging.size() : paginationFrom.intValue() - 1;
			int end = (paginationTo.intValue() > forpaging.size()) ? forpaging.size() : paginationTo.intValue();
			start = start < 0 ? 0 : start;
			forpaging = forpaging.subList(start, end);
		}

		List<Message> messages = new ArrayList<Message>();

		for (String coorId : forpaging) {
			messages.addAll(entityManager.createQuery("from Message msg where msg.correlationId= :correlationId ORDER BY msg.issueDate ASC", Message.class).setParameter("correlationId", coorId)
					.getResultList());

		}
		
		return new MessagesListVO(messages, totalConversations);
	}

	@Override
	public List<Message> retrieveOrphans(BusinessDomain businessDomain, Transaction transaction, Date startDate, Date endDate) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		cq.select(message);
		cq.distinct(true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;

		Join<Message, Party> senderParty = message.join(Message_.sender);
		predicate = cb.equal(senderParty.get(Party_.businessDomain), businessDomain);
		predicates.add(predicate);

		predicate = cb.isEmpty((message.get(Message_.parentMessages)));
		predicates.add(predicate);

		if (transaction != null) {
			predicate = cb.equal(message.get(Message_.transaction), transaction);
			predicates.add(predicate);
		}

		if (startDate != null && endDate != null) {
			Join<Message, EntityAccessInfo> aInfo = message.join(Message_.accessInfo, JoinType.LEFT);
			predicate = cb.or(cb.and(cb.isNotNull(aInfo.get(EntityAccessInfo_.modificationDate)), cb.between(aInfo.get(EntityAccessInfo_.modificationDate), startDate, endDate)),
					cb.and(cb.isNull(aInfo.get(EntityAccessInfo_.modificationDate)), cb.between(aInfo.get(EntityAccessInfo_.creationDate), startDate, endDate)));
			predicates.add(predicate);
		}

		if (predicates.size() > 0) {
			Predicate[] preds = new Predicate[predicates.size()];
			Predicate clause = cb.and(predicates.toArray(preds));
			cq.where(clause);
			TypedQuery<Message> query = entityManager.createQuery(cq);
			query.setMaxResults(500);
			return query.getResultList();
		} else {
			return null;
		}
	}

	@Override
	public List<Message> retrieveLeaves(BusinessDomain businessDomain, Transaction transaction, Date startDate, Date endDate, Set<Transaction> childTransactionsToIgnore) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		cq.select(message);
		cq.distinct(true);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;

		Join<Message, Party> senderParty = message.join(Message_.sender);
		predicate = cb.equal(senderParty.get(Party_.businessDomain), businessDomain);
		predicates.add(predicate);

		if (transaction != null) {
			predicate = cb.equal(message.get(Message_.transaction), transaction);
			predicates.add(predicate);

			Subquery<Long> subquery = cq.subquery(Long.class);
			Root<Message> subRoot = subquery.from(Message.class);
			subquery.select(cb.count(subRoot));
			subquery.where(cb.and(cb.equal(subRoot.get(Message_.transaction), transaction)), cb.isMember(subRoot, message.get(Message_.childMessages)));
			predicate = cb.equal(subquery, 0L);

			predicates.add(predicate);
		}

		if (childTransactionsToIgnore != null) {
			if (transaction != null) {
				childTransactionsToIgnore.add(transaction);
			}

			Subquery<Long> subquery = cq.subquery(Long.class);
			Root<Message> subRoot = subquery.from(Message.class);
			subquery.select(cb.count(subRoot));
			subquery.where(cb.and(cb.not(subRoot.get(Message_.transaction).in(childTransactionsToIgnore)), cb.isMember(subRoot, message.get(Message_.childMessages))));
			predicate = cb.equal(subquery, 0L);

			predicates.add(predicate);
		}

		if (startDate != null && endDate != null) {
			Join<Message, EntityAccessInfo> aInfo = message.join(Message_.accessInfo, JoinType.LEFT);
			predicate = cb.or(cb.and(cb.isNotNull(aInfo.get(EntityAccessInfo_.modificationDate)), cb.between(aInfo.get(EntityAccessInfo_.modificationDate), startDate, endDate)),
					cb.and(cb.isNull(aInfo.get(EntityAccessInfo_.modificationDate)), cb.between(aInfo.get(EntityAccessInfo_.creationDate), startDate, endDate)));
			predicates.add(predicate);
		}

		if (predicates.size() > 0) {
			Predicate[] preds = new Predicate[predicates.size()];
			Predicate clause = cb.and(predicates.toArray(preds));
			cq.where(clause);
			TypedQuery<Message> query = entityManager.createQuery(cq);
			query.setMaxResults(500);
			return query.getResultList();
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public void deleteMessages(List<Long> messages) {
		// this.entityManager.createQuery("").setParameter("ids", messages).e;
	}

	@Override
	public List<Message> findMessagesByTransaction(Long transactionId) {
		return entityManager.createQuery("from Message msg where msg.transaction.id = :transactionId", Message.class).setParameter("transactionId", transactionId).getResultList();
	}

    @Override
    public List<String> getDocumentTypeCodes() {
        return entityManager.createQuery("select distinct(m.messageDocumentTypeCode) from Message m order by UPPER(m.messageDocumentTypeCode)", String.class).getResultList();
    }

    @Override
    public List<String> getStatusCodes() {
        return entityManager.createQuery("select distinct(m.statusCode) from Message m order by UPPER(m.statusCode)", String.class).getResultList();
    }

	public String getSelectMessageQuery() {
		return selectMessageQuery;
	}

	public void setSelectMessageQuery(String selectMessageQuery) {
		this.selectMessageQuery = selectMessageQuery;
	}

	public DataSource geteTrustExDS() {
		return eTrustExDS;
	}

	public void seteTrustExDS(DataSource eTrustExDS) {
		this.eTrustExDS = eTrustExDS;
	}

	public String getInsertMessageQuery() {
		return insertMessageQuery;
	}

	public void setInsertMessageQuery(String insertMessageQuery) {
		this.insertMessageQuery = insertMessageQuery;
	}

    public List<Message> findMessagesByCriteria(Message message, Date createdFrom, Date createdTo, int firstResult, int maxResults, List<Long> businessDomainIds, DispatchEnum dispatched) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> m = cq.from(Message.class);
        List<Predicate> predicates = getPredicates(message, createdFrom, createdTo, cb, m, businessDomainIds);

        if (dispatched != null) {
            Subquery<Message> subquery = cq.subquery(Message.class);
            Root<MessageRouting> mrFrom = subquery.from(MessageRouting.class);

            if (dispatched.equals(DispatchEnum.PARTIAL)) {
                subquery.select(mrFrom.get(MessageRouting_.message))
                        .where(cb.equal(mrFrom.get(MessageRouting_.endpoint).get(Endpoint_.isActive), true))
                        .groupBy(mrFrom.get(MessageRouting_.message))
                        .having(cb.gt(cb.countDistinct(mrFrom.get(MessageRouting_.processed)), 1));
            } else {
                subquery.where(cb.equal(mrFrom.get(MessageRouting_.processed), dispatched.equals(DispatchEnum.NO) ? false : true));
                subquery.select(mrFrom.get(MessageRouting_.message)).distinct(true);
            }

            predicates.add(m.get(Message_.id).in(subquery));
        }

        cq.select(m);
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.orderBy(cb.desc(m.get(Message_.accessInfo).get(EntityAccessInfo_.creationDate)));

        return entityManager.createQuery(cq).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Override
    public long countMessagesByCriteria(Message message, Date createdFrom, Date createdTo, List<Long> businessDomainIds, DispatchEnum dispatched) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Message> m = cq.from(Message.class);
        List<Predicate> predicates = getPredicates(message, createdFrom, createdTo, cb, m, businessDomainIds);

        if (dispatched != null) {
            Subquery<Message> subquery = cq.subquery(Message.class);
            Root<MessageRouting> mrFrom = subquery.from(MessageRouting.class);

            if (dispatched.equals(DispatchEnum.PARTIAL)) {
                subquery.select(mrFrom.get(MessageRouting_.message))
                        .where(cb.equal(mrFrom.get(MessageRouting_.endpoint).get(Endpoint_.isActive), true))
                        .groupBy(mrFrom.get(MessageRouting_.message))
                        .having(cb.gt(cb.countDistinct(mrFrom.get(MessageRouting_.processed)), 1));
            } else {
                subquery.where(cb.equal(mrFrom.get(MessageRouting_.processed), dispatched.equals(DispatchEnum.NO) ? false : true));
                subquery.select(mrFrom.get(MessageRouting_.message)).distinct(true);
            }

            predicates.add(m.get(Message_.id).in(subquery));
        }

        cq.select(cb.count(m));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(cq).getSingleResult();
    }


    private List<Predicate> getPredicates(Message message, Date createdFrom, Date createdTo, CriteriaBuilder cb, Root<Message> m, List<Long> businessDomainIds) {
        List<Predicate> predicates = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(businessDomainIds)) {
            predicates.add(m.get(Message_.sender).get(Party_.businessDomain).get(BusinessDomain_.id).in(businessDomainIds));
        }

        if(StringUtils.isNotEmpty(message.getAccessInfo().getCreationId())) {
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(m.get(Message_.accessInfo).get(EntityAccessInfo_.creationId))), "%" + message.getAccessInfo().getCreationId().trim().toUpperCase() + "%"));
        }

        if(createdFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(m.get(Message_.accessInfo).get(EntityAccessInfo_.creationDate), createdFrom));
        }

        if(createdTo != null) {
            // to and from are truncated (to day) dates. Creation dates in DB are not truncated.
            predicates.add(cb.lessThan(m.get(Message_.accessInfo).get(EntityAccessInfo_.creationDate), DateUtils.addDays(createdTo, 1)));
        }

        if(StringUtils.isNotEmpty(message.getCorrelationId())) {
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(m.get(Message_.correlationId))), "%" + message.getCorrelationId().trim().toUpperCase() + "%"));
        }

        if(StringUtils.isNotEmpty(message.getDocumentId())) {
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(m.get(Message_.documentId))), "%" + message.getDocumentId().trim().toUpperCase() + "%"));
        }

        if(StringUtils.isNotEmpty(message.getMessageDocumentTypeCode())) {
            predicates.add(cb.equal(m.get(Message_.messageDocumentTypeCode), message.getMessageDocumentTypeCode()));
        }

        if(message.getRetrieveIndicator() != null) {
            predicates.add(cb.equal(m.get(Message_.retrieveIndicator), message.getRetrieveIndicator()));
        }

        if(message.getIssuer() != null) {
            predicates.add(cb.equal(m.get(Message_.issuer).get(Party_.id), message.getIssuer().getId()));
        }

        if(message.getSender() != null) {
            predicates.add(cb.equal(m.get(Message_.sender).get(Party_.id), message.getSender().getId()));
        }

        if(message.getReceiver() != null) {
            predicates.add(cb.equal(m.get(Message_.receiver).get(Party_.id), message.getReceiver().getId()));
        }

        if(message.getAgreement() != null) {
            predicates.add(cb.equal(m.get(Message_.agreement).get(InterchangeAgreement_.id), message.getAgreement().getId()));
        }

        if(message.getTransaction() != null) {
            predicates.add(cb.equal(m.get(Message_.transaction).get(Transaction_.id), message.getTransaction().getId()));
        }

        if(message.getStatusCode() != null) {
            predicates.add(cb.equal(m.get(Message_.statusCode), message.getStatusCode()));
        }

        return predicates;
    }
}
