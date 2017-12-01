package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.cipa.etrustex.domain.routing.*;
import eu.europa.ec.cipa.etrustex.services.dto.EndpointDTO;
import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class EndpointDAO extends TrustExDAO<Endpoint, Long> implements IEndpointDAO {

	@Override
	public List<Endpoint> getTransactionEndpoints(Long transactionId, Long receiverPartyId) {
		return entityManager.createQuery("from Endpoint e where e.tansaction.id =:transactionId and e.party.id = :receiverPartyId",Endpoint.class)
		.setParameter("transactionId", transactionId).setParameter("receiverPartyId", receiverPartyId).getResultList();
	}

	@Override
	public List<Endpoint> getInterchangeAgreementEndpoints(Long icaId , Long receiverPartyId) {
		return entityManager.createQuery("from Endpoint e where  e.interchangeAgreement.id = :icaId and e.party.id = :receiverPartyId",Endpoint.class)
		.setParameter("icaId", icaId).setParameter("receiverPartyId", receiverPartyId).getResultList();
	}

	@Override
	public List<Endpoint> getProfileEndpoints(Long profileId , Long receiverPartyId) {
		return entityManager.createQuery("from Endpoint e where  e.profile.id = :profileId and e.party.id = :receiverPartyId",Endpoint.class)
				.setParameter("profileId", profileId).setParameter("receiverPartyId", receiverPartyId).getResultList();
	}
	
	@Override
	public List<Endpoint> getThirdPartyEndpoints(Long profileId, Long receiverPartyId){
		//Retrieve endpoints where receiver has an agreement with receiver partyId parameter (retrieve endpoints for third parties), filter on Business Domain
		return entityManager.createQuery("select e from Endpoint as e , PartyAgreement as a where  e.profile.id = :profileId and e.party.id = a.delegateParty.id and a.authorizingParty.id = :receiverPartyId",Endpoint.class)
				.setParameter("profileId", profileId).setParameter("receiverPartyId", receiverPartyId).getResultList();	
	}
	
	@Override
	public List<Endpoint> getThirdPartyEndpointsForTransaction(Long receiverPartyId, Long transactionId) {
		StringBuilder builder = new StringBuilder();
		builder
			.append("select e ")
			.append("from Endpoint as e, PartyAgreement as a ")
			.append("where e.party.id = a.delegateParty.id ")
			.append("and a.authorizingParty.id = :receiverPartyId ")
			.append("and e.tansaction.id = :transactionId ")
			.append("and (a.transactions is empty or :transactionId member of a.transactions)");
		return entityManager
					.createQuery(builder.toString(),Endpoint.class)
					.setParameter("receiverPartyId", receiverPartyId)
					.setParameter("transactionId", transactionId)
					.getResultList();	
	}

    @Override
    public List<? extends Endpoint> findByCredentials(Credentials credentials) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
        Root<? extends Endpoint> ep = cq.from(Endpoint.class);

        cq.select(ep).distinct(true);

        if (credentials instanceof ProxyCredentials) {
            cq.where(cb.equal(ep.get(Endpoint_.proxyCredential), credentials));
        } else {
            cq.where(cb.equal(ep.get(Endpoint_.credentials), credentials));
        }

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<? extends Endpoint> findEndpointsByParty(Party party) {
        return entityManager.createQuery("from Endpoint e where e.party.id = :partyId", Endpoint.class)
                .setParameter("partyId", party.getId()).getResultList();
    }

	@Override
	public List<? extends Endpoint> findEndpointsByCriteria(Endpoint endpoint) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		List<? extends Endpoint> list;
		CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
        // To prevent Not an entity: class eu.europa.ec.cipa.etrustex.services.dto.EndpointDTO
        Class epClass = endpoint.getClass().equals(EndpointDTO.class) ? Endpoint.class : endpoint.getClass();
		Root<? extends Endpoint> ep = cq.from(epClass);
		List<Predicate> predicates = getPredicates(endpoint, cb, ep);

        cq.select(ep).distinct(true);
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        list = entityManager.createQuery(cq).getResultList();

		return list;
	}


    @Override
    public List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class endppointClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
        Root<? extends Endpoint> ep = cq.from(endppointClass);
        List<Predicate> predicates = new ArrayList<>();

        cq.select(ep).distinct(true);

        if (credentials instanceof ProxyCredentials) {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(ep.get(Endpoint_.proxyCredential).get(Credentials_.user))), "%" + credentials.getUser().trim().toUpperCase() + "%"));
        } else {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(ep.get(Endpoint_.credentials).get(Credentials_.user))), "%" + credentials.getUser().trim().toUpperCase() + "%"));
        }

        predicates.add(cb.equal(ep.get(Endpoint_.businessDomain).get(BusinessDomain_.id), businessDomainId));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class endppointClass) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
        Root<? extends Endpoint> ep = cq.from(endppointClass);
        List<Predicate> predicates = new ArrayList<>();

        cq.select(ep).distinct(true);

        if (credentials instanceof ProxyCredentials) {
            predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(ep.get(Endpoint_.proxyCredential).get(Credentials_.user))), credentials.getUser().trim().toUpperCase()));
        } else {
            predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(ep.get(Endpoint_.credentials).get(Credentials_.user))), credentials.getUser().trim().toUpperCase()));
        }

        predicates.add(cb.equal(ep.get(Endpoint_.businessDomain).get(BusinessDomain_.id), businessDomainId));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(cq).getResultList();
    }


	private List<Predicate> getPredicates(Endpoint endpoint, CriteriaBuilder cb, Root<? extends Endpoint> ep) {
		List<Predicate> predicates = new ArrayList<>();
		
		if(endpoint.getEndointMessageType() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.endointMessageType), endpoint.getEndointMessageType()));
		}
		
		if(endpoint.getIsActive() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.isActive), endpoint.getIsActive()));
		}
		
		if(endpoint.getCredentials() != null && StringUtils.isNotBlank(endpoint.getCredentials().getUser())) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(ep.get(Endpoint_.credentials).get(Credentials_.user))), "%" + endpoint.getCredentials().getUser().trim().toUpperCase() + "%"));
		}
		
		if(endpoint.getProxyCredential() != null && StringUtils.isNotBlank(endpoint.getProxyCredential().getUser())) {
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(ep.get(Endpoint_.proxyCredential).get(Credentials_.user))), "%" + endpoint.getProxyCredential().getUser().trim().toUpperCase() + "%"));
		}
		
		if(endpoint.getBusinessDomain() != null && endpoint.getBusinessDomain().getId() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.businessDomain), endpoint.getBusinessDomain()));
		}
		
		if(endpoint.getProfile() != null && endpoint.getProfile().getId() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.profile), endpoint.getProfile()));
		}
		
		if(endpoint.getTansaction() != null && endpoint.getTansaction().getId() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.tansaction), endpoint.getTansaction()));
		}
		
		if(endpoint.getInterchangeAgreement() != null && endpoint.getInterchangeAgreement().getId() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.interchangeAgreement), endpoint.getInterchangeAgreement()));
		}
		
		if(endpoint.getParty() != null && endpoint.getParty().getId() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.party), endpoint.getParty()));
		}
		
		return predicates;
	}
}
