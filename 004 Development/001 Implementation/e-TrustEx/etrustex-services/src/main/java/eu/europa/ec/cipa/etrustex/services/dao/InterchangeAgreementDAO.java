package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.cipa.etrustex.services.dto.ICASearchDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class InterchangeAgreementDAO extends TrustExDAO<InterchangeAgreement, Long> implements IInterchangeAgreementDAO {

	private static final Logger logger = LoggerFactory.getLogger(InterchangeAgreementDAO.class);
	
	@Autowired
	private IPartyRoleDao partyRoleDao;
	
	@Override
	public List<InterchangeAgreement> getInterchangeAgreements(
			Party requester, List<String> documentTypeIds, Set<Party> senders, Set<Party> receivers) {
		
		StringBuffer stringBuffer = new StringBuffer("select distinct ica from InterchangeAgreement as ica ");
		if (CollectionUtils.isNotEmpty(senders)) {
			stringBuffer.append(" join ica.partyRoles as ptr1 ");
		}
		if (CollectionUtils.isNotEmpty(receivers)) {
			stringBuffer.append(" join ica.partyRoles as ptr2 ");
		}
		boolean whereAdded = false;
		if (CollectionUtils.isNotEmpty(senders)) {
			stringBuffer.append(" where ptr1 in (select partyRole from PartyRole partyRole where partyRole.party in :senders)");
			whereAdded = true;
		}
		
		if (CollectionUtils.isNotEmpty(receivers)) {
			if (whereAdded) {
				stringBuffer.append(" and ptr2 in (select partyRole from PartyRole partyRole where partyRole.party in :receivers) ");				
			} else {
				stringBuffer.append(" where ptr2 in (select partyRole from PartyRole partyRole where partyRole.party in :receivers) ");
			}

		}
		Query query = null;
		if (documentTypeIds == null || documentTypeIds.size() == 0) {
			query = entityManager.createQuery(stringBuffer.toString(), InterchangeAgreement.class);
		} else {
			stringBuffer.append(" and EXISTS (select d from Document as d join d.transactions as tx join tx.profiles as p where d.documentTypeCode in :documentTypeIds and p.id = ica.profile.id)"); 
			query = entityManager.createQuery(stringBuffer.toString(),	InterchangeAgreement.class);
			query.setParameter("documentTypeIds", documentTypeIds);
		}
		if (CollectionUtils.isNotEmpty(senders)) {
			query.setParameter("senders", senders);
		}
		if (CollectionUtils.isNotEmpty(receivers)) {
			query.setParameter("receivers", receivers);
		}
		return query.getResultList();
	}

	@Override
	public List<InterchangeAgreement> getInterchangeAgreements(
			Party senderParty, Party recieverParty, Long transactionId) {
		Query autQuery = null;

		if (recieverParty != null) {
			autQuery = entityManager
					.createQuery("select distinct ica from InterchangeAgreement as ica join ica.partyRoles as ptr1 join ica.partyRoles as ptr2 "
							+ "where ica.profile IN (select p from Profile p where :identifier member of p.transactions) "
							+ "and ptr1 in (select partyRole from PartyRole partyRole where partyRole.party.id= :senderId) "
							+ "and ptr2 in (select partyRole from PartyRole partyRole where partyRole.party.id= :receiverId) "
							+ "and (ica.validityStartDate is null or ica.validityStartDate <= :now)",
							InterchangeAgreement.class);
			autQuery.setParameter("senderId", senderParty.getId())
					.setParameter("receiverId", recieverParty.getId())
					.setParameter("identifier", transactionId)
					.setParameter("now", new Date());
		} else {
			autQuery = entityManager
					.createQuery(
							"select distinct ica from InterchangeAgreement as ica join ica.partyRoles as ptr1  "
							+"where ica.profile IN ("
							+ "		select p from Profile p "
							+ "		where :identifier member of p.transactions) "
							+ "		and ptr1 in (select partyRole from PartyRole partyRole where partyRole.party.id= :senderId) "
							+ "and (ica.validityStartDate is null or ica.validityStartDate <= :now)"
							,InterchangeAgreement.class);
			autQuery.setParameter("senderId", senderParty.getId())
					.setParameter("identifier", transactionId)
					.setParameter("now", new Date());
		}

		if (autQuery != null) {
			return autQuery.getResultList();
		} else {
			return null;
		}

	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public InterchangeAgreement create(InterchangeAgreement ica) {

		if(!entityManager.contains(ica.getProfile())) {
			ica.setProfile(entityManager.merge(ica.getProfile()));
		}
		
		for(PartyRole partyRole: ica.getPartyRoles()) {
			List<PartyRole> roles = partyRoleDao.getPartyRoles(partyRole.getParty().getId(), partyRole.getRole().getId());
			if (roles != null && !roles.isEmpty()) {
				//party role already exists in the DB
				partyRole.setId(roles.get(0).getId());
			}
			if(!entityManager.contains(partyRole.getParty())) {
				partyRole.setParty(entityManager.merge(partyRole.getParty()));
			}
			if(!entityManager.contains(partyRole.getRole())) {
				partyRole.setRole(entityManager.merge(partyRole.getRole()));
			}
			if (partyRole.getId() == null) {
				entityManager.persist(partyRole);
			}
		}
		if(ica.getCiaLevel() != null && !entityManager.contains(ica.getCiaLevel())) {
			ica.setCiaLevel(entityManager.merge(ica.getCiaLevel()));
		}
		
		return super.create(ica);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public InterchangeAgreement update(InterchangeAgreement ica) {
		
		if(!entityManager.contains(ica.getProfile())) {
			ica.setProfile(entityManager.merge(ica.getProfile()));
		}
		
		for(PartyRole partyRole: ica.getPartyRoles()) {
			if(!entityManager.contains(partyRole.getParty())) {
				partyRole.setParty(entityManager.merge(partyRole.getParty()));
			}
			if(!entityManager.contains(partyRole.getRole())) {
				partyRole.setRole(entityManager.merge(partyRole.getRole()));
			}
			if(partyRole.getId() != null) {
				entityManager.merge(partyRole);
			} else {
				entityManager.persist(partyRole);
			}
		}


		if(!entityManager.contains(ica.getCiaLevel())) {
			ica.setCiaLevel(entityManager.merge(ica.getCiaLevel()));
		}
		
		
		return super.update(ica);
	}

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly=true)
    public List<InterchangeAgreement> findInterchangeAgreementsByCriteria(ICASearchDTO dto) {
        String partyName = dto.getPartyName();
        String identifierValue = dto.getIdentifierValue();
        Long profileId = dto.getProfileId();
        Long roleId = dto.getRoleId();
        Long businessDomainId = dto.getBusinessDomainId();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        List<InterchangeAgreement> list;
        CriteriaQuery<InterchangeAgreement> cq = cb.createQuery(InterchangeAgreement.class);
        Root<InterchangeAgreement> ica = cq.from(InterchangeAgreement.class);
//        Join<PartyRole, Party> partyJoin = ica.join(InterchangeAgreement_.partyRoles).join(PartyRole_.party);
        SetJoin<InterchangeAgreement, PartyRole> icaPartyRoleJoin = ica.join(InterchangeAgreement_.partyRoles); //.join(PartyRole_.role);
        Join<Party, PartyIdentifier> identifierJoin = icaPartyRoleJoin.join(PartyRole_.party).join(Party_.identifiers);
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(partyName)) {
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(icaPartyRoleJoin.get(PartyRole_.party).get(Party_.name))), "%" + partyName.trim().toUpperCase() + "%"));
        }

        if (StringUtils.isNotBlank(identifierValue)) {
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(identifierJoin.get(PartyIdentifier_.value))), "%" + identifierValue.trim().toUpperCase() + "%"));
        }

        if (profileId != null) {
            predicates.add(cb.equal(ica.get(InterchangeAgreement_.profile).get(Profile_.id), profileId));
        }

        if (roleId != null) {
            predicates.add(cb.equal(icaPartyRoleJoin.get(PartyRole_.role).get(Role_.id), roleId));
        }

        predicates.add(cb.equal(icaPartyRoleJoin.get(PartyRole_.party).get(Party_.businessDomain).get(BusinessDomain_.id), businessDomainId));

        cq.select(ica).distinct(true);
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(cq).getResultList();
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<InterchangeAgreement> getInterchangeAgreements(Long firstPartyId,
			Long firstPartyRoleId, Long profileId, Long secondPartyRoleId) {	
		
		String queryString = "select distinct ica from InterchangeAgreement as ica " + 
				"join ica.partyRoles as ptr1 fetch all properties  " +
				"join ica.partyRoles as ptr2 fetch all properties  " + 
				"where ptr1.party.id = :firstPartyId and ica.profile.id = :profileId and ptr1.role.id = :firstPartyRoleId " + 
				"and ptr2.role.id = :secondPartyRoleId";	
		
		TypedQuery<InterchangeAgreement> query = entityManager.createQuery(queryString, InterchangeAgreement.class);
		
		query.setParameter("firstPartyId", firstPartyId);
		query.setParameter("profileId", profileId);
		query.setParameter("firstPartyRoleId", firstPartyRoleId);
		query.setParameter("secondPartyRoleId", secondPartyRoleId);
		
		return query.getResultList();
	}

	@Override
	public Boolean isUniqueBy(Long profileId, Long firstPartyId,
			Long firstPartyRoleId, Long secondPartyId, Long secondPartyRoleId) {
		
		Boolean isUnique = true;
		
		String queryString = "select distinct ica from InterchangeAgreement as ica " + 
				"join ica.partyRoles as ptr1 fetch all properties  " +
				"join ica.partyRoles as ptr2 " + 
				"where ica.profile.id  = :profileId " + 
					"and ptr1.party.id = :firstPartyId " + 
					"and ptr1.role.id  = :firstPartyRoleId " + 
					"and ptr2.party.id = :secondPartyId " + 
					"and ptr2.role.id  = :secondPartyRoleId";

		TypedQuery<InterchangeAgreement> query = entityManager.createQuery(queryString, InterchangeAgreement.class);
		
		query.setParameter("profileId", profileId);
		query.setParameter("firstPartyId", firstPartyId);
		query.setParameter("firstPartyRoleId", firstPartyRoleId);
		query.setParameter("secondPartyId", secondPartyId);
		query.setParameter("secondPartyRoleId", secondPartyRoleId);
		
		if (CollectionUtils.isNotEmpty(query.getResultList())){
			isUnique = false;
		}
		
		return isUnique;
	}
	
	@Override
	public List<InterchangeAgreement> getInterchangeAgreements(PartyRole a,	PartyRole b, Profile p, CIALevel l){
		String queryString = "select distinct ica from InterchangeAgreement as ica " + 
				"join ica.partyRoles as ptr1 fetch all properties  " +
				"join ica.partyRoles as ptr2 " + 
				"where ptr1.id = :firstPartyRoleId and ica.profile.id = :profileId and ptr2.id = :secondPartyRoleId " + 
				"and ica.ciaLevel.id = :ciaLevelId and ptr1.id <> ptr2.id";	
		
		TypedQuery<InterchangeAgreement> query = entityManager.createQuery(queryString, InterchangeAgreement.class);
		
		query.setParameter("firstPartyRoleId", a.getId());
		query.setParameter("profileId", p.getId());
		query.setParameter("ciaLevelId", l.getId());
		query.setParameter("secondPartyRoleId", b.getId());
		
		return query.getResultList();
	}

	@Override
	public Boolean containsParty(Party party) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<InterchangeAgreement> criteriaQuery = cb.createQuery(InterchangeAgreement.class);
		Root<InterchangeAgreement> ica = criteriaQuery.from(InterchangeAgreement.class);
		Join<PartyRole, Party> partyJoin = ica.join(InterchangeAgreement_.partyRoles).join(PartyRole_.party); 
		
		return !entityManager.createQuery(criteriaQuery.where(cb.equal(partyJoin, party))).getResultList().isEmpty();
	}

	@Override
	public Boolean existICAwithSameRoleForParties(Role role) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = cb.createQuery(Tuple.class);
		Root<InterchangeAgreement> ica = criteriaQuery.from(InterchangeAgreement.class);
		Join<PartyRole, Role> icaRoleJoin = ica.join(InterchangeAgreement_.partyRoles).join(PartyRole_.role); 
		
		CriteriaQuery<Tuple> query = criteriaQuery.where(cb.equal(icaRoleJoin, role))
				.multiselect(icaRoleJoin.get(Role_.id), ica.get(InterchangeAgreement_.id))
				.groupBy(icaRoleJoin.get(Role_.id), ica.get(InterchangeAgreement_.id))
				.having(cb.gt(cb.count(ica), 1) );
		
		return entityManager.createQuery(query).getResultList().size() > 1;
	}

	@Override
	public Boolean isUsed(Long id) {

		return isUsedInTable("Message", "agreement.id", id) 
				|| isUsedInTable("Endpoint", "interchangeAgreement.id", id)
				|| isUsedInTable("MetaDataItem", "interchangeAgreement.id", id)
				|| isUsedInTable("MessageResponseCode", "interchangeAgreement.id", id);

	}
	
	
	private Boolean isUsedInTable(String tableName, String icaFieldName, Long icaId){
		
		Boolean isUsed = false;
		
		String queryString = "select distinct t from " + tableName + " as t " + 
				"where t." + icaFieldName + " = :icaId";	
		
		logger.info(queryString);
		
		Query query = entityManager.createQuery(queryString);
		query.setParameter("icaId", icaId);

		if (CollectionUtils.isNotEmpty(query.getResultList())){
			isUsed = true;
		}
		
		return isUsed;
	}

	@Override
	public List<InterchangeAgreement> getInterchangeAgreementsByProfile(Long profileId) {
		return entityManager.createQuery("from InterchangeAgreement ica where ica.profile.id = :profileId", InterchangeAgreement.class).
				setParameter("profileId", profileId).getResultList();
	}
	
	
	
}