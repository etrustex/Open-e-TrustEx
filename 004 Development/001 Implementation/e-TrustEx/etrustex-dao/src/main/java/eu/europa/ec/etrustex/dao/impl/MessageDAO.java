package eu.europa.ec.etrustex.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.domain.InterchangeAgreement_;
import eu.europa.ec.etrustex.domain.Message_;
import eu.europa.ec.etrustex.domain.Party_;
import eu.europa.ec.etrustex.domain.Transaction_;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.etrustex.domain.query.QueryResult_;
import eu.europa.ec.etrustex.domain.routing.Endpoint_;
import eu.europa.ec.etrustex.domain.routing.MessageRouting_;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo_;
import eu.europa.ec.etrustex.dao.IMessageDAO;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.dto.RetrieveMessagesForQueryRequestDTO;
import eu.europa.ec.etrustex.dao.util.DispatchEnum;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.query.QueryResult;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.domain.util.MessagesListVO;
import eu.europa.ec.etrustex.types.DataExtractionTypes;
import eu.europa.ec.etrustex.types.SortFieldTypeCode;

@Repository
public class MessageDAO extends TrustExDAO<Message, Long> implements IMessageDAO {
	
	private static final String APPLICATION_RESPONSE_DTC = "301";
	private static final String ATTACHED_DOCUMENT_DTC 	 = "916";
	
	@Override
    public Message createMessage(Message newMessage) {
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
    public long findMessagesByDocumentId(String messageDocumentId) {
        return entityManager
                .createQuery("select count(msg.id) from Message msg where msg.documentId =:documentId", Long.class)
                .setParameter("documentId", messageDocumentId).getSingleResult();
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
	public List<Message> retrieveMessages(MessageQueryDTO messageDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		// fetching the parents or the binaries might lead to duplicates in the
		// results due to the left outer join, we use distinct in tha case
		if (messageDTO.getFetchParents()) {
			cq.distinct(true);
			message.fetch(Message_.parentMessages, JoinType.LEFT);
		}

		cq.select(message);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		if (messageDTO.getReceiverPartyId() != null) {
			predicate = cb.equal(message.get(Message_.receiver), messageDTO.getReceiverPartyId());
			predicates.add(predicate);

		}
		if (messageDTO.getMessageDocumentId() != null) {
			predicate = cb.equal(message.get(Message_.documentId), messageDTO.getMessageDocumentId());
			predicates.add(predicate);

		}
		if (messageDTO.getSenderPartyId() != null) {
			predicate = cb.equal(message.get(Message_.sender), messageDTO.getSenderPartyId());
			predicates.add(predicate);
		}
		
		if (messageDTO.getIssuerPartyId() != null) {
			predicate = cb.equal(message.get(Message_.issuer), messageDTO.getIssuerPartyId());
			predicates.add(predicate);
		}
		
		if(messageDTO.getFilterReadServices()){
			Subquery<Long> subquery = cq.subquery(Long.class);
			Root<Message> subRoot = subquery.from(Message.class);
			subquery.select(cb.count(subRoot));
			subquery.where(
					cb.and(
							cb.isMember(subRoot,message.get(Message_.parentMessages)),
							cb.and(cb.equal(subRoot.get(Message_.sender), messageDTO.getReceiverPartyId()),cb.notEqual(subRoot.get(Message_.issuer), messageDTO.getParentIssuerId()))
							)
					);
			predicate = cb.or(cb.isEmpty(message.get(Message_.parentMessages)),cb.equal(subquery, 0L));
			predicates.add(predicate);
		}
		
		if (messageDTO.getIcaId() != null) {
			predicate = cb.equal(message.get(Message_.agreement), messageDTO.getIcaId());
			predicates.add(predicate);
		}

		if (messageDTO.getDocumentTypeCode() != null) {
			predicate = cb.equal(message.get(Message_.messageDocumentTypeCode), messageDTO.getDocumentTypeCode());
			predicates.add(predicate);
		}

		if (messageDTO.getRetrievedInd() != null) {
			predicate = cb.equal(message.get(Message_.retrieveIndicator), messageDTO.getRetrievedInd());
			predicates.add(predicate);
		}

		if (messageDTO.getTransactions() != null && messageDTO.getTransactions().size() > 0) {
			predicate = message.get(Message_.transaction).in(messageDTO.getTransactions());
			predicates.add(predicate);
			
			if(messageDTO.getInboxServiceFilter()){		
				SetJoin<Message, Message> parents = message.join(Message_.parentMessages, JoinType.LEFT);
				
				Predicate predicate0 = cb.or(cb.isEmpty(message.get(Message_.parentMessages)), cb.not(message.get(Message_.messageDocumentTypeCode).in(Arrays.asList(APPLICATION_RESPONSE_DTC, ATTACHED_DOCUMENT_DTC))));				
				Predicate predicate1 = cb.and(cb.isNotEmpty(message.get(Message_.parentMessages)), message.get(Message_.messageDocumentTypeCode).in(Arrays.asList(APPLICATION_RESPONSE_DTC, ATTACHED_DOCUMENT_DTC)), parents.get(Message_.transaction).in(messageDTO.getTransactions()));				
				Predicate predicate2 = cb.and(cb.isNotEmpty(parents.get(Message_.parentMessages)), cb.equal(parents.get(Message_.messageDocumentTypeCode), ATTACHED_DOCUMENT_DTC), parents.join(Message_.parentMessages, JoinType.LEFT).get(Message_.transaction).in(messageDTO.getTransactions())); 			
				
				predicate = cb.or(predicate0, predicate1, predicate2);	
				predicates.add(predicate);
			}
		}
		
		//filter out submitted state anyway
		
		predicate = cb.notEqual(message.get(Message_.statusCode), "SUBMITTED");
		predicates.add(predicate);
		
		if (messageDTO.getFilterOutMessagesInError()) {
			predicate = cb.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
		}
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		cq.orderBy(cb.desc(message.get(Message_.receptionDate)));
		
		TypedQuery<Message> query = entityManager.createQuery(cq);

		if (messageDTO.getMaxResult() != null) {
			query.setMaxResults(messageDTO.getMaxResult());
		}
		return query.getResultList();
	}
	
	@Override
	public long getMessageCount(MessageQueryDTO messageDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Message> message = cq.from(Message.class);
		// fetching the parents or the binaries might lead to duplicates in the
		// results due to the left outer join, we use distinct in tha case
		if (messageDTO.getFetchParents()) {
			cq.distinct(true);
			message.fetch(Message_.parentMessages, JoinType.LEFT);
		}

		cq.select(cb.count(message));
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		if (messageDTO.getReceiverPartyId() != null) {
			predicate = cb.equal(message.get(Message_.receiver), messageDTO.getReceiverPartyId());
			predicates.add(predicate);

		}
		if (messageDTO.getMessageDocumentId() != null) {
			predicate = cb.equal(message.get(Message_.documentId), messageDTO.getMessageDocumentId());
			predicates.add(predicate);

		}
		if (messageDTO.getSenderPartyId() != null) {
			predicate = cb.equal(message.get(Message_.sender), messageDTO.getSenderPartyId());
			predicates.add(predicate);
		}
		
		if (messageDTO.getIssuerPartyId() != null) {
			predicate = cb.equal(message.get(Message_.issuer), messageDTO.getIssuerPartyId());
			predicates.add(predicate);
		}
		
		if(messageDTO.getFilterReadServices()){
			Subquery<Long> subquery = cq.subquery(Long.class);
			Root<Message> subRoot = subquery.from(Message.class);
			subquery.select(cb.count(subRoot));
			subquery.where(
					cb.and(
							cb.isMember(subRoot,message.get(Message_.parentMessages)),
							cb.and(cb.equal(subRoot.get(Message_.sender), messageDTO.getReceiverPartyId()),cb.notEqual(subRoot.get(Message_.issuer), messageDTO.getParentIssuerId()))
							)
					);
			predicate = cb.or(cb.isEmpty(message.get(Message_.parentMessages)),cb.equal(subquery, 0L));
			predicates.add(predicate);
		}
		
		if (messageDTO.getIcaId() != null) {
			predicate = cb.equal(message.get(Message_.agreement), messageDTO.getIcaId());
			predicates.add(predicate);
		}

		if (messageDTO.getDocumentTypeCode() != null) {
			predicate = cb.equal(message.get(Message_.messageDocumentTypeCode), messageDTO.getDocumentTypeCode());
			predicates.add(predicate);
		}

		if (messageDTO.getRetrievedInd() != null) {
			predicate = cb.equal(message.get(Message_.retrieveIndicator), messageDTO.getRetrievedInd());
			predicates.add(predicate);
		}

		if (messageDTO.getTransactions() != null && messageDTO.getTransactions().size() > 0) {
			predicate = message.get(Message_.transaction).in(messageDTO.getTransactions());
			predicates.add(predicate);
			
			if(messageDTO.getInboxServiceFilter()){		
				SetJoin<Message, Message> parents = message.join(Message_.parentMessages, JoinType.LEFT);
				
				Predicate predicate0 = cb.or(cb.isEmpty(message.get(Message_.parentMessages)), cb.not(message.get(Message_.messageDocumentTypeCode).in(Arrays.asList(APPLICATION_RESPONSE_DTC, ATTACHED_DOCUMENT_DTC))));				
				Predicate predicate1 = cb.and(cb.isNotEmpty(message.get(Message_.parentMessages)), message.get(Message_.messageDocumentTypeCode).in(Arrays.asList(APPLICATION_RESPONSE_DTC, ATTACHED_DOCUMENT_DTC)), parents.get(Message_.transaction).in(messageDTO.getTransactions()));				
				Predicate predicate2 = cb.and(cb.isNotEmpty(parents.get(Message_.parentMessages)), cb.equal(parents.get(Message_.messageDocumentTypeCode), ATTACHED_DOCUMENT_DTC), parents.join(Message_.parentMessages, JoinType.LEFT).get(Message_.transaction).in(messageDTO.getTransactions())); 			
				
				predicate = cb.or(predicate0, predicate1, predicate2);	
				predicates.add(predicate);
			}
		}
		
		//filter out submitted state anyway
		
		predicate = cb.notEqual(message.get(Message_.statusCode), "SUBMITTED");
		predicates.add(predicate);
		
		if (messageDTO.getFilterOutMessagesInError()) {
			predicate = cb.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
		}
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		cq.orderBy(cb.desc(message.get(Message_.receptionDate)));
		
		TypedQuery<Long> query = entityManager.createQuery(cq);

		if (messageDTO.getMaxResult() != null) {
			query.setMaxResults(messageDTO.getMaxResult());
		}
		return query.getSingleResult();
	}	

	private void populateQuery(CriteriaBuilder cb, CriteriaQuery<Message> cq, Root<Message> message, RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO) {

		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate senderpredicate = null;
		Predicate receiverPredicate = null;
		Predicate predicate = null;

		
		if (BooleanUtils.isTrue(retrieveMessagesDTO.getIsSenderAlsoReceiver()) && CollectionUtils.isNotEmpty(retrieveMessagesDTO.getSenderPartyIds())) {
			receiverPredicate = message.get(Message_.receiver).in(retrieveMessagesDTO.getSenderPartyIds());
			senderpredicate = message.get(Message_.sender).in(retrieveMessagesDTO.getSenderPartyIds());
			predicate = cb.or(senderpredicate, receiverPredicate);
			predicates.add(predicate);
		} else {
			if (CollectionUtils.isNotEmpty(retrieveMessagesDTO.getSenderPartyIds())) {
				predicate = message.get(Message_.sender).in(retrieveMessagesDTO.getSenderPartyIds());
				predicates.add(predicate);
			}
			if (CollectionUtils.isNotEmpty(retrieveMessagesDTO.getReceiverPartyIds())) {
				predicate = message.get(Message_.receiver).in(retrieveMessagesDTO.getReceiverPartyIds());
				predicates.add(predicate);
			}
		}

		if (StringUtils.isNotBlank(retrieveMessagesDTO.getCorrelationId())) {
			predicate = message.get(Message_.correlationId).in(retrieveMessagesDTO.getCorrelationId());
			predicates.add(predicate);
		}

		if (retrieveMessagesDTO.getStartDate() != null && retrieveMessagesDTO.getEndDate() != null) {
			predicate = cb.between(message.get(Message_.receptionDate), retrieveMessagesDTO.getStartDate(), retrieveMessagesDTO.getEndDate());
			predicates.add(predicate);
		}

		if (CollectionUtils.isNotEmpty(retrieveMessagesDTO.getTransactions())) {
			predicate = message.get(Message_.transaction).in(retrieveMessagesDTO.getTransactions());
			predicates.add(predicate);
		}
		if (CollectionUtils.isNotEmpty(retrieveMessagesDTO.getDocumentTypeCodes())) {
			predicate = message.get(Message_.messageDocumentTypeCode).in(retrieveMessagesDTO.getDocumentTypeCodes());
			predicates.add(predicate);
		}

		//if retrieved indicator does not exist in the request or is false, restrict results to messages with retrieved flag false
		if (BooleanUtils.isNotTrue(retrieveMessagesDTO.getRetrievedInd())) {
			predicate = cb.equal(message.get(Message_.retrieveIndicator), Boolean.FALSE);
			predicates.add(predicate);
		}

		//filter out submitted documents anyway
		
		predicate = cb.notEqual(message.get(Message_.statusCode), "SUBMITTED");
		predicates.add(predicate);
		
		if (BooleanUtils.isTrue(retrieveMessagesDTO.getFilterOutMessagesInError())) {
			predicate = cb.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
		}

		if (predicates.size() > 0) {
			Predicate[] preds = new Predicate[predicates.size()];
			Predicate clause = cb.and(predicates.toArray(preds));
			cq.where(clause);
		}
	}

	@Override
	public List<Message> retrieveMessagesForQueryRequest(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO) {

		CriteriaBuilder cbList = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cqList = cbList.createQuery(Message.class);
		Root<Message> message = cqList.from(Message.class);

		// fetching the parents or the binaries might lead to duplicates in the
		// results due to the left outer join, we use distinct in tha case
		if (BooleanUtils.isTrue(retrieveMessagesDTO.getFetchParents())) {
			message.fetch(Message_.parentMessages, JoinType.LEFT);
		}

		if (BooleanUtils.isTrue(retrieveMessagesDTO.getFetchBinaries())) {
			message.fetch(Message_.binaries);
		}

		populateQuery(cbList, cqList, message, retrieveMessagesDTO);

		cqList.distinct(true);
		cqList.select(message);

		// Sorting
		cqList.orderBy(cbList.desc(message.get(Message_.receptionDate)));
		
		TypedQuery<Message> queryForResults = entityManager.createQuery(cqList);
		queryForResults.setHint(QueryHints.HINT_READONLY, Boolean.TRUE);

		if (retrieveMessagesDTO.getMaxResult() != null) {
			queryForResults.setMaxResults(retrieveMessagesDTO.getMaxResult());
		}
		
		
		
		
		return queryForResults.getResultList();
	}
	
	@Override
	public MessagesListVO retrieveMessagesJustice(RetrieveMessagesForQueryRequestDTO queryDTO) {

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
		receiverPredicate = message.get(Message_.receiver).in(queryDTO.getSenderPartyIds());
		senderpredicate = message.get(Message_.sender).in(queryDTO.getSenderPartyIds());
		predicate = cbList.or(senderpredicate, receiverPredicate);
		predicates.add(predicate);
		forErrorMessagePredicates.add(predicate);

		SetJoin<Message, QueryResult> qResultile = message.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbList.and(cbList.equal(qResultile.get(QueryResult_.key), DataExtractionTypes.JMS_TITLE.name()));
		predicates.add(predicate);
		forErrorMessagePredicates.add(predicate);

		SetJoin<Message, QueryResult> qResultUser = message.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbList.and(cbList.equal(qResultUser.get(QueryResult_.key), DataExtractionTypes.JMS_USER_ID.name()), 
				cbList.equal(qResultUser.get(QueryResult_.value), queryDTO.getUserId()));
		predicates.add(predicate);
		forErrorMessagePredicates.add(predicate);

		if (CollectionUtils.isNotEmpty(queryDTO.getBusinessDocumentTypes())) {

			Subquery<Date> sq = cqList.subquery(Date.class);
			Root<Message> s2 = sq.from(Message.class);
			sq.select(cbList.greatest(s2.get(Message_.issueDate)));
			sq.where(cbList.equal(s2.get(Message_.correlationId), message.get(Message_.correlationId)));
			predicate = cbList.equal(message.get(Message_.issueDate), sq);
			predicates.add(predicate);

			SetJoin<Message, QueryResult> qResultBdt = message.join(Message_.additionalInfo, JoinType.LEFT);
			predicate = cbList.and(cbList.equal(qResultBdt.get(
					QueryResult_.key), 
					DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name()), 
					qResultBdt.get(QueryResult_.value).in(queryDTO.getBusinessDocumentTypes()));

			predicates.add(predicate);
		}

		if (queryDTO.getStartDate() != null && queryDTO.getEndDate() != null) {
			predicate = cbList.between(message.get(Message_.issueDate), queryDTO.getStartDate(), queryDTO.getEndDate());
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}
		
		if (queryDTO.getRetrievedInd() != null) {
			predicate = cbList.equal(message.get(Message_.retrieveIndicator), queryDTO.getRetrievedInd());
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}	

		if (BooleanUtils.isTrue(queryDTO.getFilterOutMessagesInError())) {
			predicate = cbList.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}
		if (StringUtils.isNotBlank(queryDTO.getCorrelationId())) {
			predicate = cbList.equal(message.get(Message_.correlationId), queryDTO.getCorrelationId());
			predicates.add(predicate);
			forErrorMessagePredicates.add(predicate);
		}

		
		Predicate[] preds = new Predicate[predicates.size()];

		if (BooleanUtils.isTrue(queryDTO.getFilterOutMessagesInError())) {
			Predicate clause = cbList.and(predicates.toArray(preds));
			cqList.where(clause);
		} else {
			predicate = cbList.equal(message.get(Message_.statusCode), "ERROR");
			forErrorMessagePredicates.add(predicate);
			Predicate[] forErrorMessagePreds = new Predicate[forErrorMessagePredicates.size()];
			Predicate clause = cbList.or(cbList.and(predicates.toArray(preds)), cbList.and(forErrorMessagePredicates.toArray(forErrorMessagePreds)));
			cqList.where(clause);
		}


		cqList.distinct(true);

		cqList.select(cbList.tuple(message.get(Message_.correlationId), message.get(Message_.issueDate), qResultUser.get("value"), qResultile.get("value")));

		if (queryDTO.getSortField() != null) {
			if (SortFieldTypeCode.TITLE.equals(queryDTO.getSortField())) {
				cqList.orderBy(cbList.asc(qResultile.get("value")));
			} else if (SortFieldTypeCode.SUBMISSION_DATE.equals(queryDTO.getSortField())) {
				cqList.orderBy(cbList.desc(message.get(Message_.issueDate)));
			}
		} else {
			cqList.orderBy(cbList.desc(message.get(Message_.issueDate)));
		}

		TypedQuery<Tuple> queryForResults = entityManager.createQuery(cqList);
		queryForResults.setHint(QueryHints.HINT_READONLY, Boolean.TRUE);

		List<Tuple> list = queryForResults.getResultList();

		LinkedHashSet<String> corrIDs = new LinkedHashSet<String>();

		for (Tuple tuple : list) {
			String correlationID = (String) tuple.get(0);
			corrIDs.add(correlationID);
		}

		Integer totalConversations = corrIDs.size();

		List<String> forpaging = new ArrayList<String>(corrIDs);

		if ((queryDTO.getPaginationFrom() != null) && (queryDTO.getPaginationTo() != null)) {

			int start = ((queryDTO.getPaginationFrom().intValue() - 1) > forpaging.size()) ? forpaging.size() : queryDTO.getPaginationFrom().intValue() - 1;
			int end = (queryDTO.getPaginationTo().intValue() > forpaging.size()) ? forpaging.size() : queryDTO.getPaginationTo().intValue();
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
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		TypedQuery<Message> query = entityManager.createQuery(cq);
		query.setMaxResults(500);
		return query.getResultList();	
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

		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		TypedQuery<Message> query = entityManager.createQuery(cq);
		query.setMaxResults(500);
		return query.getResultList();
		
	}

	@Override
	public long findMessagesByTransaction(Long transactionId) {
		return entityManager.createQuery("select count(msg.id) from Message msg where msg.transaction.id = :transactionId", Long.class).setParameter("transactionId", transactionId).getSingleResult();
	}

    @Override
    public boolean isInterchangeAgreementUsedToSendMessages(InterchangeAgreement interchangeAgreement) {
        return !entityManager.createQuery("from Message msg where msg.agreement = :interchangeAgreementId", Message.class).setParameter("interchangeAgreementId", interchangeAgreement).getResultList().isEmpty();
    }

    @Override
    public List<String> getDocumentTypeCodes() {
        return entityManager.createQuery("select distinct(m.messageDocumentTypeCode) from Message m order by UPPER(m.messageDocumentTypeCode)", String.class).getResultList();
    }

    @Override
    public List<String> getStatusCodes() {
        return entityManager.createQuery("select distinct(m.statusCode) from Message m order by UPPER(m.statusCode)", String.class).getResultList();
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

        TypedQuery<Message> query = entityManager.createQuery(cq);
        query.setHint(QueryHints.HINT_READONLY, Boolean.TRUE);

        return query.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	Join<Message, Party> senderPartyJoin = m.join(Message_.sender, JoinType.LEFT);
            predicates.add(senderPartyJoin.get(Party_.businessDomain).get(BusinessDomain_.id).in(businessDomainIds));
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
    
    @Override
    public boolean existsEjusticeMessageForCorrelationId(RetrieveMessagesForQueryRequestDTO retrieveMessagesDTO) {
		CriteriaBuilder cbCount = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cqCount = cbCount.createQuery(Long.class);
		Root<Message> msgRoot = cqCount.from(Message.class);
		List<Predicate> predicates = new ArrayList<>();
		Predicate predicate = null;		
		if (CollectionUtils.isNotEmpty(retrieveMessagesDTO.getDocumentTypeCodes())) {
			predicate = msgRoot.get(Message_.messageDocumentTypeCode).in(retrieveMessagesDTO.getDocumentTypeCodes());
			predicates.add(predicate);
		}
		//if retrieved indicator does not exist in the request or is false, restrict results to messages with retrieved flag false
		if (BooleanUtils.isNotTrue(retrieveMessagesDTO.getRetrievedInd())) {
			predicate = cbCount.equal(msgRoot.get(Message_.retrieveIndicator), Boolean.FALSE);
			predicates.add(predicate);
		}		
		if (StringUtils.isNotBlank(retrieveMessagesDTO.getCorrelationId())) {
			predicate = msgRoot.get(Message_.correlationId).in(retrieveMessagesDTO.getCorrelationId());
			predicates.add(predicate);
		}		
		
		predicate = cbCount.notEqual(msgRoot.get(Message_.statusCode), "ERROR");
		predicates.add(predicate);
		predicate = cbCount.notEqual(msgRoot.get(Message_.statusCode), "SUBMITTED");
		predicates.add(predicate);	
		
		SetJoin<Message, QueryResult> qResultUser = msgRoot.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbCount.and(cbCount.equal(qResultUser.get(QueryResult_.key), DataExtractionTypes.JMS_USER_ID.name()), 
				cbCount.notEqual(qResultUser.get(QueryResult_.value), retrieveMessagesDTO.getUserId()));
		predicates.add(predicate);		
		
		cqCount.where(predicates.toArray(new Predicate[predicates.size()]));
		cqCount.select(cbCount.count(msgRoot));
		return entityManager.createQuery(cqCount).getSingleResult() > 0;
    }
    
}
