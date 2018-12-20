package eu.europa.ec.etrustex.dao.impl;

import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.etrustex.types.EventNotificationType;
import eu.europa.ec.etrustex.types.IIdentifierIssuingAgency;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PartyDAO extends TrustExDAO<Party, Long> implements IPartyDAO {
    public static final String EVENT_NOTIF_PUBLISHER_ETRUSTEX = "ETRUSTEX";
	private static final int EVENT_PUBLISHER_ROLE_ID = 11;
	private static final int EVENT_SUBSCRIBER_ROLE_ID = 12;
	private static final int EVENT_NOTIFICATION_TRANSACTION_ID = 75;

	@Override
	public Party getParty(IIdentifierIssuingAgency shemeId, String partyId, BusinessDomain bd) {
		List<Party> parties = entityManager
				.createQuery("select id.party from PartyIdentifier id join id.party pty where upper(id.value) = :id and id.schemeId = :scheme and pty.businessDomain.id = :bdid",Party.class)
				.setParameter("id", partyId.trim().toUpperCase())
				.setParameter("scheme", shemeId)
				.setParameter("bdid", bd!=null?bd.getId():null)
				.getResultList();
		return CollectionUtils.isNotEmpty(parties) ? parties.get(0) : null;
	}

	
	@Override
	public Party getMessageIssuer(String authenticatedUser) {
		List<Party> parties = entityManager
				.createQuery("select c.party from Credentials c where c.user =:authenticatedUser", Party.class)
				.setParameter("authenticatedUser", authenticatedUser)
				.getResultList();
		return CollectionUtils.isNotEmpty(parties) ? parties.get(0) : null;
	}

	@Override
	public List<Party> getThirdPartiesForBusinessDomain(Long businessDomainId) {
		return entityManager
				.createQuery("from Party p where p.thirdPartyFlag = 1 and p.businessDomain.id = :businessDomainId order by UPPER(p.name) ", Party.class)
				.setParameter("businessDomainId", businessDomainId)
				.getResultList();
	}
	
	@Override
	public void delete(Long id){
		Party party = read(id);
		entityManager.flush();
		super.delete(party);
	}
	
	@Override
	public Party getPartyForBusinessDomain(String name, Long businessDomainId) { 
//		return getUniqueByAttribute(Party_.name, name);
		List<Party> result = entityManager
				.createQuery("from Party p where UPPER(p.name) = UPPER(:name) and p.businessDomain.id = :businessDomainId", Party.class)
				.setParameter("name", name)
				.setParameter("businessDomainId", businessDomainId)
				.getResultList();
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<Party> getPartiesForBusinessDomain(String name, String username, String identifierValue, List<Long> businessDomainIds) {

		String queryString = "SELECT distinct party FROM Party as party ";

		if (StringUtils.isNotBlank(identifierValue)) {
			queryString += "join party.identifiers as identifiers ";
		}
		//TODO: use the list of domains instead
		queryString += "where party.businessDomain.id in (:businessDomainIds) ";

		if (StringUtils.isNotBlank(name)){
			queryString += "and UPPER(party.name) like UPPER(:partyName) ";
		}

		if (StringUtils.isNotBlank(username)) {
				queryString += "and UPPER(party.credentials.user) like UPPER(:username) ";
		}
	
		if (StringUtils.isNotBlank(identifierValue)) {
				queryString += "and UPPER(identifiers.value) like UPPER(:identifierValue) ";
		}

		queryString += "order by UPPER(party.name) ";

		TypedQuery<Party> query = entityManager.createQuery(queryString, Party.class);

		query.setParameter("businessDomainIds", businessDomainIds);

		if (StringUtils.isNotBlank(name)){
			query.setParameter("partyName", "%" + name.trim() + "%");
		}

		if (StringUtils.isNotBlank(username)){
			query.setParameter("username", "%" + username.trim() + "%");
		}

		if (StringUtils.isNotBlank(identifierValue)){
			query.setParameter("identifierValue", "%" + identifierValue.trim() + "%");
		}

		return query.getResultList();
	}

    @Override
    public List<Party> findPartiesByCriteria(String name, String username, String identifierValue, List<Long> businessDomainIds, Boolean isThirdParty) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Party> cq = cb.createQuery(Party.class);
        Root<Party> party = cq.from(Party.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(identifierValue)) {
        	Join<Party, PartyIdentifier> identifiersJoin = party.join(Party_.identifiers);
        	predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(identifiersJoin.get(PartyIdentifier_.value))), "%" + identifierValue.trim().toUpperCase() + "%"));
        }

        if(businessDomainIds != null && !businessDomainIds.isEmpty()) {
            predicates.add(party.get(Party_.businessDomain).get(BusinessDomain_.id).in(businessDomainIds));
        }

        if (StringUtils.isNotBlank(name)){
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(party.get(Party_.name))), "%" + name.trim().toUpperCase() + "%"));
        }

        if (StringUtils.isNotBlank(username)) {
            Join<Party, Credentials> credentialsJoin = party.join(Party_.credentials);
            predicates.add(cb.like(cb.trim(CriteriaBuilder.Trimspec.BOTH, cb.upper(credentialsJoin.get(Credentials_.user))), "%" + username.trim().toUpperCase() + "%"));
        }

        if (isThirdParty != null) {
            predicates.add(cb.equal(party.get(Party_.thirdPartyFlag), isThirdParty));
        }

        cq.select(party).distinct(true);
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.orderBy(cb.asc(cb.lower(party.get(Party_.name))));
        return entityManager.createQuery(cq).getResultList();
    }
		
	@Override
	public Party create(Party party) {		
		
		//TODO Check if working as expected 
		// No, it duplicates the record
		/*if(party.getCertificates() != null){
			for (Certificate cert : party.getCertificates()) {
				if(!entityManager.contains(cert)){
					entityManager.merge(cert);
				}
			}			
		}*/
		
		if(party.getCredentials() != null  
				&& !entityManager.contains(party.getCredentials())) {
			party.setCredentials(entityManager.merge(party.getCredentials()));
		}
		
		if(party.getBusinessDomain() != null  
				&& !entityManager.contains(party.getBusinessDomain())) {
			party.setBusinessDomain(entityManager.merge(party.getBusinessDomain()));
		}
		
		/* Party identifiers are created from the party service, 
		 * as at this point the new party id is not known in order 
		 * to link the identifiers to the party.*/
		
		return super.create(party);

	}
	
	@Override
	public Party update(Party party) {
		
		//TODO Check if working as expected
		if(party.getCertificates() != null){
			for (Certificate cert : party.getCertificates()) {
				if(!entityManager.contains(cert)){
					entityManager.merge(cert);
				}
			}
		}
		
		if(party.getCredentials() != null 
				&& !entityManager.contains(party.getCredentials())) {
			party.setCredentials(entityManager.merge(party.getCredentials()));
		}
		
		if(party.getBusinessDomain() != null  
				&& !entityManager.contains(party.getBusinessDomain())) {
			party.setBusinessDomain(entityManager.merge(party.getBusinessDomain()));
		}
		
//		if(party.getIdentifiers().size() > 0){
//			for (PartyIdentifier identifier : party.getIdentifiers()) {
//				identifier.setParty(party);
//				if (!entityManager.contains(identifier)){
//					if (identifier.getId() == null){
//						entityManager.merge(identifier);
//					} /*else {
//						entityManager.persist(identifier);
//					}*/
//					
//				}/* else {
//					party.addIdentifier(partyIdentifier)entityManager.persist(identifier);
//				}*/
//			}
//		}
		
		party = entityManager.merge(party);
		
		return super.update(party);

	}
	
	/**
	 * Checks if the party doesn't exist yet 
	 **/
	@Override
	public Boolean isUniquePartyPerBusinessDomain(String partyName, Long businessDomainId) {
		
		Boolean isUnique = true;
		
		String queryString = "SELECT party FROM Party party where party.name = :partyName and party.businessDomain.id = :businessDomainId";
		TypedQuery<Party> query = entityManager
				.createQuery(queryString, Party.class)
				.setParameter("partyName", partyName)
				.setParameter("businessDomainId", businessDomainId);
		
		if (CollectionUtils.isNotEmpty(query.getResultList())){
			isUnique = false;
		}
		
		return isUnique;
	}

	@Override
	public List<PartyListItemDTO> getAllPartiesForBusinessDomain(Long businessDomainId) {
		return entityManager
				.createQuery("select new eu.europa.ec.etrustex.dao.dto.PartyListItemDTO(p.id, p.name) from Party p " +
                        "where p.businessDomain.id = :businessDomainId " +
                        "order by UPPER(p.name) ", PartyListItemDTO.class)
				.setParameter("businessDomainId", businessDomainId)
				.getResultList();
	}

	@Override
	public List<Party> getPartiesConfiguredToReceiveEventNotification(EventNotificationType eventType, Long businessDomainId, Long profileId) {
        StringBuilder querySB = new StringBuilder("select en.party from EventNotification en where en.businessDomain.id = :businessDomainId and en.eventType = :eventType ");

        if(profileId != null) {
            querySB.append("and en.profile.id = :profileId ");
        }

        TypedQuery<Party> query = entityManager.createQuery(querySB.toString(), Party.class);

        query.setParameter("businessDomainId", businessDomainId).setParameter("eventType", eventType);

        if(profileId != null) {
            query.setParameter("profileId", profileId);
        }

		return query.getResultList();
	}

	@Override
	public Party getEventPublisherParty(Party confSubscriberParty) {
		StringBuilder stringBuilder = new StringBuilder()
			.append("select pty.* ") //the sender
			.append("from ETR_TB_PARTY pty ")
			.append("join ETR_TB_PARTY_ROLE pr on pr.PAR_PTY_ID = pty.PTY_ID ")
			.append("join ETR_TB_ICA_PARTYROLE ipr on ipr.PAR_ID = pr.PAR_ID ")
			.append("join ETR_TB_ICA_PARTYROLE ipr1 on (ipr.ICA_ID = ipr1.ICA_ID and ipr.PAR_ID <> ipr1.PAR_ID) ")
			.append("join ETR_TB_PARTY_ROLE pr1 on pr1.PAR_ID = ipr1.PAR_ID ")
			.append("join ETR_TB_PARTY pty1 on pty1.PTY_ID = pr1.PAR_PTY_ID ") 
			.append("join ETR_TB_INTERCHANGE_AGR ica on ipr.ICA_ID = ica.ICA_ID ")
			.append("join ETR_TB_PROFILE pro on pro.PRO_ID = ica.ICA_PRO_ID ")
			.append("join ETR_TB_PROFILE_TRANSACTION ptr on ptr.PTR_PRO_ID = pro.PRO_ID ")
			.append("where pty1.PTY_ID = :confSubscriberPartyId ") //subscriber id is taken from CPA_TB_CFG_NOTIF
			.append("and pr1.PAR_ROL_ID = :eventSubscriberRoleId ") //subscriber must have EventSubscriber role
			.append("and pr.PAR_ROL_ID = :eventPublisherRoleId ") //publisher must have EventPublisher role
			.append("and pty.PTY_BUSINESS_DOMAIN = pty1.PTY_BUSINESS_DOMAIN ") //sender and receiver must be from the same BD
			.append("and ptr.PTR_TRA_ID = :eventNotifTransactionId ")//the profile of the ICA must be linked to the transaction SubmitEventNotification
			.append("and pty.PTY_PARTY_NAME = :publisherName");
		
		
		List<Party> parties = entityManager.createNativeQuery(stringBuilder.toString(), Party.class)
				.setParameter("confSubscriberPartyId", confSubscriberParty.getId())
				.setParameter("eventSubscriberRoleId", EVENT_SUBSCRIBER_ROLE_ID)
				.setParameter("eventPublisherRoleId", EVENT_PUBLISHER_ROLE_ID)
				.setParameter("eventNotifTransactionId", EVENT_NOTIFICATION_TRANSACTION_ID)
				.setParameter("publisherName", EVENT_NOTIF_PUBLISHER_ETRUSTEX)
				.getResultList();

		return CollectionUtils.isNotEmpty(parties) ? parties.get(0) : null;
	}

    @Override
    public Party getEtrustexParty(Long businessDomainId) {
	    return getPartyForBusinessDomain(EVENT_NOTIF_PUBLISHER_ETRUSTEX, businessDomainId);
    }

	@Override
	public void flushEm(){
		entityManager.flush();
	}
}
