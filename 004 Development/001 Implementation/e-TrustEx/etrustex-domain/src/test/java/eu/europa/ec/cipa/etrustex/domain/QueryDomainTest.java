package eu.europa.ec.cipa.etrustex.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType; 
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
  


import javax.persistence.criteria.SetJoin;

import org.junit.Before; 
import org.junit.Test;
 
















import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;
import eu.europa.ec.cipa.etrustex.domain.query.QueryResult_;
import eu.europa.ec.cipa.etrustex.types.DataExtractionTypes;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;


public class QueryDomainTest { 
  
	private EntityManagerFactory factory;
   
	@Before
	public void initTest() throws Exception {
		factory = Persistence.createEntityManagerFactory("etrustex-test");
	} 

	@Test  
	public void queryDocumentTransaction() throws Exception {  

		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		List<Transaction> trans=  entityManager.createQuery("from Transaction t where t.document.name='Invoice'").getResultList();
		for (Transaction transaction : trans) {
			System.out.println(transaction.getName()); 
			System.out.println(transaction.getSenderRole().getName());
			System.out.println(transaction.getReceiverRole().getName());
		}
		entityManager.getTransaction().commit();
		entityManager.close();

	}
	@Test  
	public void testParentMesages() throws Exception { 

		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		Message m = entityManager.find(Message.class, new Long(213));
		System.out.println( m.getChildMessages().iterator().next().getId());
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	
	@Test 
	public void queryCorrelationIds() throws Exception { 

		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		
		CriteriaBuilder cbList = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> cqList = cbList.createTupleQuery();
		
		Root<Message> message = cqList.from(Message.class);
		
	
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Predicate senderpredicate = null;
		Predicate receiverPredicate = null;
		Predicate predicate = null;
		List<Long> senderPartyIds = new ArrayList<Long>(); 
		senderPartyIds.add(new Long(311));
		// nfor justice messages shouc be the one send by dg just or received by dg just
		receiverPredicate = message.get(Message_.receiver).in(senderPartyIds);
		senderpredicate = message.get(Message_.sender).in(senderPartyIds);
		predicate = cbList.or(senderpredicate,receiverPredicate);
		predicates.add(predicate);
	
		SetJoin<Message, QueryResult> qResultUser = message.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbList.and(cbList.equal(qResultUser.get(QueryResult_.key), DataExtractionTypes.JMS_USER_ID.name()), cbList.equal(qResultUser.get(QueryResult_.value), "esadmin1"));
		predicates.add(predicate);
		
		SetJoin<Message, QueryResult> qResultile = message.join(Message_.additionalInfo, JoinType.LEFT);
		predicate = cbList.and(cbList.equal(qResultile.get(QueryResult_.key), DataExtractionTypes.JMS_TITLE.name()));
		predicates.add(predicate);
//		if(businessDocumentType != null && businessDocumentType.size() > 0){			
//			SetJoin<Message, QueryResult> qResultBdt = message.join(Message_.additionalInfo, JoinType.LEFT);
//			//predicate = cb.and(cb.equal(qResultBdt.get(QueryResult_.key), DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name()), cb.equal(qResultBdt.get(QueryResult_.value), businessDocumentType));
//			predicate = cbList.and(cbList.equal(qResultBdt.get(QueryResult_.key), DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name()), qResultBdt.get(QueryResult_.value).in(businessDocumentType));
//			predicates.add(predicate);
//		}
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		
		cal.set(Calendar.YEAR, 2012);
		
		Date startDate = cal.getTime();
		
		if (startDate != null && endDate !=null) {
			predicate = cbList.between(message.get(Message_.receptionDate), startDate,endDate);
			predicates.add(predicate);
		}
		

			predicate = cbList.equal(message.get(Message_.retrieveIndicator),false);
			predicates.add(predicate);
		 
		
	
			predicate = cbList.notEqual(message.get(Message_.statusCode), "ERROR");
			predicates.add(predicate);
	
//		if (correlationIds != null && !correlationIds.isEmpty()) {
//			predicate = message.get(Message_.correlationId).in(correlationIds);
//			predicates.add(predicate);
//		}
		
		if (predicates.size() >0){		
			Predicate[] preds = new Predicate[predicates.size()];
			Predicate clause = cbList.and(predicates.toArray(preds));			
			cqList.where(clause);
		}
		
		cqList.orderBy(cbList.desc(message.get(Message_.receptionDate)));
		
		cqList.select(cbList.tuple( message.get(Message_.correlationId),message. get(Message_.receptionDate),qResultUser.get("value"),qResultile.get("value"),message.get(Message_.documentId)));
		//cqList.select(cbList.tuple( message.get(Message_.correlationId)));
		cqList.distinct(true);
		//TypedQuery<String> queryForResults = entityManager.createQuery(cqList);
	//	cqList.groupBy(message.get(Message_.correlationId));
		
		//Paging
		
			int start = 10;
			int end =19;
				
				
		//queryForResults.setMaxResults(50);
		
//		if(sortField != null){ 			
//			List<Order> order = new ArrayList<Order>();
//			if (SortFieldTypeCode.TITLE.equals(sortField) ){
//				//TODO SORT BY TITLE
//				cbList.desc(message.get(Message_.correlationId));
//				//order.add(0, cbList.asc(message.get(Message_.additionalInfo)));
//			} else if(SortFieldTypeCode.SUBMISSION_DATE.equals(sortField) ){
//				cqList.orderBy(cbList.desc(message.get(Message_.issueDate)));
//				//order.add(0, cbList.desc(message.get(Message_.issueDate)));
//			}
//			//cqList.orderBy(order);
//		}
//		TypedQuery<Tuple>
		TypedQuery<Tuple> queryForResults =(TypedQuery<Tuple>)entityManager.createQuery(cqList);
		 System.out.println("Total Conversations : "+ queryForResults.getResultList().size());
		
//		queryForResults.setFirstResult(start);
//		queryForResults.setMaxResults(end - start + 1);
		
		
		
		List<Tuple> list =queryForResults.getResultList();
		List<Message> messages =new ArrayList<Message>();
		Set<String> corrIDs= new LinkedHashSet<String>();
		
		
		
		for (Tuple tuple : list) {
			String correlationID = (String)tuple.get(0);
			corrIDs.add(correlationID);
			
			System.out.println(tuple.get(0));
		//	System.out.println(tuple.get(1));
		//	System.out.println(tuple.get(2));
		//	System.out.println(tuple.get(3));
		//	System.out.println(tuple.get(4));
//				messages.addAll( entityManager.createQuery(
//						"from Message msg where msg.correlationId= :correlationId ORDER BY msg.issueDate ASC",Message.class).setParameter("correlationId", tuple.get(0)).
//						getResultList());
		}
		
		List<String> forPaging = new ArrayList<String>(corrIDs).subList(0, 5);
		
		System.out.println("----------------------------");
		for(String corrId :forPaging ){
			System.out.println(corrId);
//			messages.addAll( entityManager.createQuery(
//			"from Message msg where msg.correlationId= :correlationId ORDER BY msg.issueDate ASC",Message.class).setParameter("correlationId",corrId).
//			getResultList());
		}
		
		System.out.println("----------------------------");
		for (Message msg : messages){
			System.out.println(msg.getCorrelationId());
			System.out.println(msg.getDocumentId());
		}
		System.out.println(list.size());
		System.out.println(corrIDs.size());
		System.out.println(messages.size());
	 
		
		entityManager.getTransaction().commit();
		
		//populateQuery(cbList, cqList, message, receiverPartyIds, senderPartyIds, receiverPartyIds2, senderPartyIds2, issuerPartyId, transactions, documentTypeCodes, documentId, retrievedInd, startDate, endDate, correlationIds, isSenderAlsoReceiver, null, null, filterOutMessagesInError);
		

	}
	
	
	
	@Test 
	public void queryInboxTest() throws Exception { 

		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		
		Query q =  entityManager.createQuery("from Message msg left join fetch msg.parentMessages where msg.receiver.id =:receiverId", Message.class);
		q.setParameter("receiverId", new Long(2));
	//	q.setParameter("issuerPartyId", new Long(2));
	//	q.setParameter("icaId",new Long(1));
		q.setMaxResults(100);

		List<Message> results = q.getResultList();
		for (Message message : results) {
			System.out.println("ID = " +message.getDocumentId());
			if (message.getParentMessages() != null){
				//System.out.println("parent = "+message.getParentMessages(). .getDocumentId());
			}
			
		}
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> message = cq.from(Message.class);
		//Join<Message, Message> p =message.join(Message_.parentMessage);
		message.fetch(Message_.parentMessages, JoinType.LEFT);
		cq.select(message);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = cb.equal(message.get(Message_.receiver), new Long(1));
		//Predicate predicate1 = cb.equal(message.get(Message_.agreement), new Long(1));
		predicates.add(predicate);
		List<Transaction> tansactions = new ArrayList<Transaction>();
		Transaction t = new Transaction();
		t.setId(new Long(1));
		tansactions.add(t);
		Transaction t2 = new Transaction();
		t2.setId(new Long(2));
		tansactions.add(t2);
		//predicates.add(predicate1);
		predicate = message.get(Message_.transaction).in(tansactions);
		predicates.add(predicate);
		
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		//predicate.
		cq.where(clause);
		
		
		TypedQuery<Message> query =entityManager.createQuery(cq);
		results = query.getResultList();
		for (Message msg : results) {
			System.out.println("criteria query");
			System.out.println(msg.getDocumentId());
//			if (msg.getParentMessage() != null){
//				System.out.println(msg.getParentMessage().getDocumentId());
//			}
			
		}
		
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
//		Root<Message> message = cq.from(Message.class);
//		cq.where(cb.equal(message.get(Message.name), "Fido")
//		    .and(cb.equal(pet.get(Pet_.color), "brown")));
//		entityManager.getTransaction().commit();
//		entityManager.close();

	}
	
	
	@Test
	public void queryDocumentProfiles() throws Exception {

		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
//		List<Profile> profiles=  entityManager.createQuery("select p from Profile p, IN (p.transactions) AS t where t.document.name='Invoice'").getResultList();
//		for (Profile proflie : profiles) {
//			System.out.println(proflie.getName());
//			//System.out.println(proflie.getSenderRole().getName());
//			//System.out.println(proflie.getReceiverRole().getName());
//		}
		Query autQuery = null;
//		autQuery = entityManager
//				.createQuery("select distinct ica from InterchangeAgreement as ica join ica.partyRoles as ptr where ica.profile IN (select p from Profile p where :identifier member of p.transactions) and ptr in (select partyRole from PartyRole partyRole where partyRole.party.id= :senderId)",InterchangeAgreement.class);
//		
//		autQuery.setParameter("senderId", new Long(200)).setParameter("identifier", new Long(8));
//		
//		List<InterchangeAgreement> res = autQuery.getResultList();
//		for (InterchangeAgreement interchangeAgreement : res) {
//			System.out.println("ICA ID = " +interchangeAgreement.getId() );
//		}
		//System.out.println("profiles :"+autQuery.getResultList());
//		
		
		autQuery = entityManager
				.createQuery("select distinct ica from InterchangeAgreement as ica join ica.partyRoles as ptr1 join ica.partyRoles as ptr2   where ica.profile IN (select p from Profile p where :identifier member of p.transactions) and ptr1 in (select partyRole from PartyRole partyRole where partyRole.party.id= :senderId) and ptr2 in (select partyRole from PartyRole partyRole where partyRole.party.id= :receiverId)",InterchangeAgreement.class);
		
		autQuery.setParameter("senderId", new Long(200)).setParameter("receiverId", new Long(199)).setParameter("identifier", new Long(8));
		
		List<InterchangeAgreement> res = autQuery.getResultList();
		
		for (InterchangeAgreement interchangeAgreement : res) {
			System.out.println("ICA ID = " +interchangeAgreement.getId() );
		}
//		autQuery = entityManager
//				.createQuery(
//						"select partyRole from PartyRole partyRole where partyRole.party.id= :senderId or partyRole.party.id= :receiverId ",
//						PartyRole.class);
//		autQuery.setParameter("senderId", new Long(200))
//				.setParameter("receiverId", new Long(203));
//		System.out.println("party Roles" + autQuery.getResultList());
		entityManager.getTransaction().commit();
		entityManager.close();

	}
	@Test
	public void checkAuthorisation() throws Exception{
		Long timer = System.currentTimeMillis();
		
		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		Long transactionId = new Long(8); 
		Query q= entityManager.createQuery("select id.party from PartyIdentifier id where id.value= :id and id.schemeId = :scheme");
		Party senderParty = (Party) q.setParameter("id", "TRUSTSUPPARTY1").setParameter("scheme", IdentifierIssuingAgency.GLN).getSingleResult();
		System.out.println(senderParty.getName());
		Party recieverParty =(Party) q.setParameter("id", "TRUSTCUSTPARTY6").setParameter("scheme", IdentifierIssuingAgency.GLN).getSingleResult();
		System.out.println(recieverParty.getName());
		List<Long> senderIds = new ArrayList<Long>();
		senderIds.add(senderParty.getId());
		
		List<Long> receiverIds = new ArrayList<Long>();
		receiverIds.add(recieverParty.getId());
		Query autQuery = 
			entityManager.createQuery(
					"select distinct ica from InterchangeAgreement as ica join ica.partyRoles as ptr where :senderId member of ptr.party.id and :receiverId member of  ptr.party.id and EXISTS (select p from Profile p, IN (p.transactions) AS t where t.id=:identifier and p.id= ica.profile.id)");
		
	List<InterchangeAgreement> agrs = autQuery.setParameter("senderId",senderParty.getId()).setParameter("receiverId",recieverParty.getId()).setParameter("identifier",  new Long(8)).getResultList() ;
		System.out.println(agrs.size());
		if (agrs.size() == 1){
			InterchangeAgreement agr=  agrs.get(0);
			Set<PartyRole> partyRoles= agr.getPartyRoles();
			Role senderRole = null;
			Role receiverRole= null ;
			
			for (PartyRole partyRole : partyRoles) {
				if(partyRole.getParty().getId().equals(senderParty.getId())){
					senderRole = partyRole.getRole();
				}
				if (partyRole.getParty().getId().equals(recieverParty.getId())){
					receiverRole = partyRole.getRole();
				}
			}
			Set<Transaction> transactions = agr.getProfile().getTransactions();
			Boolean authorised = false;
			System.out.println(transactions.size());
			for (Transaction transaction : transactions) {
				if (transaction.getReceiverRole().getName().equalsIgnoreCase(receiverRole.getName()) 
						&& transaction.getSenderRole().getName().equalsIgnoreCase(senderRole.getName())
						&& transactionId.compareTo(transaction.getId()) ==0)
				{
					authorised = true;
				}
			}
			System.out.println(authorised);
			
		}
		System.out.println("Execution Time : " + (System.currentTimeMillis() -  timer));
		entityManager.getTransaction().commit();
		entityManager.close();
		
		
		
	}

}
