package eu.europa.ec.etrustex.dao.impl;

import eu.europa.ec.etrustex.dao.IEndpointDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.dao.dto.EndpointDTO;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.etrustex.domain.routing.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EndpointDAO extends TrustExDAO<Endpoint, Long> implements IEndpointDAO {

	@Autowired
	private ITransactionDAO txDao;

	@Override
	public List<Endpoint> getTransactionEndpoints(Long transactionId, Long receiverPartyId) {
		return entityManager.createQuery("from Endpoint e where e.tansaction.id =:transactionId and e.party.id = :receiverPartyId and e.isActive = TRUE",Endpoint.class)
				.setParameter("transactionId", transactionId).setParameter("receiverPartyId", receiverPartyId).getResultList();
	}

	@Override
	public List<Endpoint> getInterchangeAgreementEndpoints(Long icaId , Long receiverPartyId) {
		return entityManager.createQuery("from Endpoint e where  e.interchangeAgreement.id = :icaId and e.party.id = :receiverPartyId and e.isActive = TRUE",Endpoint.class)
				.setParameter("icaId", icaId).setParameter("receiverPartyId", receiverPartyId).getResultList();
	}

	@Override
	public List<Endpoint> getProfileEndpoints(Long profileId , Long receiverPartyId) {
		return entityManager.createQuery("from Endpoint e where  e.profile.id = :profileId and e.party.id = :receiverPartyId and e.isActive = TRUE",Endpoint.class)
				.setParameter("profileId", profileId).setParameter("receiverPartyId", receiverPartyId).getResultList();
	}

	@Override
	public List<Endpoint> getThirdPartyEndpointsForProfile(Long profileId, Long receiverPartyId, Long transactionId){
		//Retrieve endpoints where receiver has an agreement with receiver partyId parameter (retrieve endpoints for third parties), filter on Business Domain
		StringBuilder builder = new StringBuilder();
		builder
				.append("select e ")
				.append("from Endpoint as e, PartyAgreement as a ")
				.append("where e.profile.id = :profileId ")
				.append("and e.party.id = a.delegateParty.id ")
				.append("and a.authorizingParty.id = :receiverPartyId ")
				.append("and (a.transactions is empty or :transactionObj member of a.transactions) ")
				.append("and e.isActive = TRUE");
		return entityManager.createQuery(builder.toString(), Endpoint.class)
				.setParameter("profileId", profileId)
				.setParameter("receiverPartyId", receiverPartyId)
				.setParameter("transactionObj", txDao.read(transactionId))
				.getResultList();
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
				.append("and (a.transactions is empty or :transactionObj member of a.transactions) ")
				.append("and e.isActive = TRUE");
		return entityManager
				.createQuery(builder.toString(),Endpoint.class)
				.setParameter("receiverPartyId", receiverPartyId)
				.setParameter("transactionId", transactionId)
				.setParameter("transactionObj", txDao.read(transactionId))
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
		// To prevent Not an entity: class eu.europa.ec.etrustex.dao.dto.EndpointDTO
		Class<? extends Endpoint> epClass = endpoint.getClass().equals(EndpointDTO.class) ? Endpoint.class : endpoint.getClass();
		Root<? extends Endpoint> ep = cq.from(epClass);
		List<Predicate> predicates = getPredicates(endpoint, cb, ep);

		cq.select(ep).distinct(true);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		list = entityManager.createQuery(cq).getResultList();

		return list;
	}


	@Override
	public List<? extends Endpoint> findByCredentialsTypeAndUsernameLike(Credentials credentials, Long businessDomainId, Class<? extends Endpoint> endppointClass) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
		Root<? extends Endpoint> ep = cq.from(endppointClass);
		List<Predicate> predicates = new ArrayList<>();

		cq.select(ep).distinct(true);

		if (credentials instanceof ProxyCredentials) {
			Join proxyCredJoin = ep.join(Endpoint_.proxyCredential);
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(proxyCredJoin.get(ProxyCredentials_.user))), "%" + credentials.getUser().trim().toUpperCase() + "%"));
		} else {
			Join credJoin = ep.join(Endpoint_.credentials);
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(credJoin.get(Credentials_.user))), "%" + credentials.getUser().trim().toUpperCase() + "%"));
		}

		predicates.add(cb.equal(ep.get(Endpoint_.businessDomain).get(BusinessDomain_.id), businessDomainId));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		List<? extends Endpoint> endpointsResult = entityManager.createQuery(cq).getResultList();

		if (credentials instanceof ProxyCredentials) {
			Set<? extends Endpoint> endpoints = endpointsResult.stream().
					filter(e -> (e.getProxyHost() != null && e.getProxyPort() != null))
					.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator
							.comparing((Endpoint e) -> e.getProxyPort())
							.thenComparing(e -> e.getProxyHost())
							.thenComparing(e -> e.getProxyCredential().getUser())
					)));
			return endpoints.stream().collect(Collectors.toList());
		}

		return endpointsResult;
	}

	@Override
	public List<? extends Endpoint> findByCredentialsTypeAndUsernameEquals(Credentials credentials, Long businessDomainId, Class<? extends Endpoint> endppointClass) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Endpoint> cq = cb.createQuery(Endpoint.class);
		Root<? extends Endpoint> ep = cq.from(endppointClass);
		List<Predicate> predicates = new ArrayList<>();

		cq.select(ep).distinct(true);

		if (credentials instanceof ProxyCredentials) {
			Join proxyCredJoin = ep.join(Endpoint_.proxyCredential);
			predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(proxyCredJoin.get(ProxyCredentials_.user))), credentials.getUser().trim().toUpperCase()));
		} else {
			Join credJoin = ep.join(Endpoint_.credentials);
			predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(credJoin.get(Credentials_.user))), credentials.getUser().trim().toUpperCase()));
		}

		predicates.add(cb.equal(ep.get(Endpoint_.businessDomain).get(BusinessDomain_.id), businessDomainId));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		return entityManager.createQuery(cq).getResultList();
	}

	@Override
	public boolean existsEndpointForUsernameAndUrl(Credentials credentials, Long businessDomainId, Class<? extends Endpoint> endpointClass, String url, Long endpointId, Long newCredentialId) {
		StringBuilder builder = new StringBuilder();
		builder
				.append("select count(edp.id) ")
				.append("from " + endpointClass.getSimpleName() + " edp ")
				.append("where edp.businessDomain.id = :bdId ");
		if (endpointId != null) {
			//do not compare with itself
			builder.append("and edp.id != :endpointId ");
		}

		String differentUsernameCondition = (credentials instanceof ProxyCredentials)
				? " upper(edp.proxyCredential.user) <> upper(:username) "
				: " upper(edp.credentials.user) <> upper(:username) ";

		String sameUsernameCondition = (credentials instanceof ProxyCredentials)
				? " upper(edp.proxyCredential.user) = upper(:username) "
				: " upper(edp.credentials.user) = upper(:username) ";

		String sameCredentialIdCondition = (credentials instanceof ProxyCredentials)
				? " (edp.proxyCredential.user.id = :newCredentialId) "
				: " (edp.credentials.user.id = :newCredentialId) ";

		String sameUrlCondition = null;
		String differentUrlCondition = null;
		if (endpointClass.equals(JMSEndpoint.class) || endpointClass.equals(AMQPEndpoint.class) ) {
			sameUrlCondition = " edp.providerUrl = :url ) ";
			differentUrlCondition = " edp.providerUrl <> :url ";
		} else if (endpointClass.equals(WSEndpoint.class)) {
			sameUrlCondition = " edp.wsEndpointURL = :url) ";
			differentUrlCondition = " edp.wsEndpointURL <> :url ";
		}



		builder
				.append(" and (")
				//Same endpoint URL with different credentials is not allowed
				//.append(differentUsernameCondition).append(" and ").append(sameUrlCondition)
				//Re-using an existing username/id linked to a different endpoint than the one targeted is not allowed
				.append("(").append(sameUsernameCondition).append(" and ").append(differentUrlCondition).append(" and ").append(sameCredentialIdCondition).append(" and ").append(":isNewCredential = 'false'")
				//it is not allowed to create a duplicate username for an existing URL
				.append(") or ((").append(sameUrlCondition).append(" and ").append(sameUsernameCondition).append(" and ").append(":isNewCredential = 'true'")
				.append("))");

		boolean isNewCredential = (newCredentialId == null);
		TypedQuery<Long> query = entityManager
				.createQuery(builder.toString(), Long.class)
				.setParameter("bdId", businessDomainId)
				.setParameter("username", credentials.getUser().trim())
				.setParameter("url", url)
				.setParameter("newCredentialId", newCredentialId)
				.setParameter("isNewCredential", Boolean.valueOf(isNewCredential).toString());
		if (endpointId != null) {
			query.setParameter("endpointId", endpointId);
		}

		return query.getSingleResult().longValue() > 0;
	}


	private List<Predicate> getPredicates(Endpoint endpoint, CriteriaBuilder cb, Root<? extends Endpoint> ep) {
		List<Predicate> predicates = new ArrayList<>();

		if(endpoint.getIsActive() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.isActive), endpoint.getIsActive()));
		}

		if(endpoint.getCredentials() != null && StringUtils.isNotBlank(endpoint.getCredentials().getUser())) {
			Join credJoin = ep.join(Endpoint_.credentials);
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(credJoin.get(Credentials_.user))), "%" + endpoint.getCredentials().getUser().trim().toUpperCase() + "%"));
		}

		if(endpoint.getProxyCredential() != null && StringUtils.isNotBlank(endpoint.getProxyCredential().getUser())) {
			Join proxyCredJoin = ep.join(Endpoint_.proxyCredential);
			predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(proxyCredJoin.get(Credentials_.user))), "%" + endpoint.getProxyCredential().getUser().trim().toUpperCase() + "%"));
		}

		if(StringUtils.isNotEmpty(endpoint.getProxyHost())) {
			predicates.add(cb.equal(ep.get(Endpoint_.proxyHost), endpoint.getProxyHost()));
		}

		if(endpoint.getProxyPort() != null) {
			predicates.add(cb.equal(ep.get(Endpoint_.proxyPort), endpoint.getProxyPort()));
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
