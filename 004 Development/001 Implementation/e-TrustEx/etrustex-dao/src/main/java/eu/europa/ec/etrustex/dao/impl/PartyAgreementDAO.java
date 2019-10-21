package eu.europa.ec.etrustex.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.dao.exception.MissingImplementationException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.PartyAgreement_;
import eu.europa.ec.etrustex.domain.Party_;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain_;

@Repository
public class PartyAgreementDAO extends TrustExDAO<PartyAgreement, Long> implements IPartyAgreementDAO {
	
	@Autowired
	private ITransactionDAO transactionDAO;

	@Override
	public PartyAgreement retrievePartyAgreement(Party authorizingParty,
			Party delegateParty) {
		return entityManager.createQuery("from PartyAgreement pag where pag.authorizingParty.id =:authorizingPartyId and pag.delegateParty.id =:delegatePartyId", PartyAgreement.class)
		.setParameter("authorizingPartyId", authorizingParty.getId())
		.setParameter("delegatePartyId", delegateParty.getId())
		.getSingleResult();
	}
	
	@Override
	public Boolean existsPartyAgreement(Party authorizingParty, Party delegateParty) {
		return entityManager.createQuery("select count(pag) from PartyAgreement pag where pag.authorizingParty.id =:authorizingPartyId and pag.delegateParty.id =:delegatePartyId", Long.class)
				.setParameter("authorizingPartyId", authorizingParty.getId())
				.setParameter("delegatePartyId", delegateParty.getId())
				.getSingleResult() > 0;
	}
	
	@Override
	public Boolean existsPartyAgreementForTransaction(Party authorizingParty, Party delegateParty, Long transactionId) {
		return entityManager.createQuery("select count(pag) from PartyAgreement pag "
				+ "where pag.authorizingParty.id =:authorizingPartyId and pag.delegateParty.id =:delegatePartyId "
				+ "and (pag.transactions is empty or :transaction member of pag.transactions)", Long.class)
				.setParameter("authorizingPartyId", authorizingParty.getId())
				.setParameter("delegatePartyId", delegateParty.getId())
				.setParameter("transaction", transactionDAO.read(transactionId))
				.getSingleResult() > 0;
	}	

	@Override
	public Long create(Party authorizingParty, Party delegateParty) {
		/*EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationDate(Calendar.getInstance().getTime());
		eai.setModificationDate(Calendar.getInstance().getTime());
		eai.setCreationId("batrian");
		eai.setModificationId("batrian");
		*/
		
		//TODO: set access information
		PartyAgreement partyAgreement = new PartyAgreement();
		partyAgreement.setAuthorizingParty(authorizingParty);
		partyAgreement.setDelegateParty(delegateParty);
//		partyAgreement.setAccessInfo(eai);
		
		PartyAgreement saved = super.create(partyAgreement);
		
		return saved.getId();
	}

	@Override
	public PartyAgreement get(Long authorizingPartyId, Long delegatePartyId) throws MissingImplementationException {
		throw new MissingImplementationException();
	}

	@Override
	public List<Party> getDelegateThirdPartiesFor(Long partyId) {
		return entityManager.createQuery("select pag.delegateParty from PartyAgreement pag where pag.authorizingParty.id =:partyId order by UPPER(pag.authorizingParty.name)", Party.class)
				.setParameter("partyId", partyId)
				.getResultList();
	}

    @Override
    public Collection<String> getDelegateThirdPartiesNamesFor(Long partyId) {
        return entityManager.createQuery("select pag.delegateParty.name from PartyAgreement pag where pag.authorizingParty.id =:partyId order by UPPER(pag.authorizingParty.name)", String.class)
                .setParameter("partyId", partyId)
                .getResultList();
    }
	
	@Override
	public List<Party> getAuthorisedPartiesFor(Long partyId){
		return entityManager.createQuery("select pag.delegateParty from PartyAgreement pag where pag.authorizingParty.id =:partyId", Party.class)
				.setParameter("partyId", partyId)
				.getResultList();
	}
	
	@Override
	public Boolean existsAgreementForParty(Party delegateParty) {
		return (entityManager.createQuery("from PartyAgreement pag where pag.delegateParty.id =:delegatePartyId", PartyAgreement.class)
				.setParameter("delegatePartyId", delegateParty.getId())
				.getResultList().size() > 0) ? true : false;
	}

    @Override
    public List<PartyAgreement> findPartyAgreementsForParty(Party party) {
        return entityManager.createQuery("from PartyAgreement pag where pag.delegateParty.id =:partyId or pag.authorizingParty.id =:partyId", PartyAgreement.class)
                .setParameter("partyId", party.getId())
                .getResultList();
    }

    @Override
    public List<PartyAgreement> findPartyAgreementsForAuthorizingParty(Party party) {
        return entityManager.createQuery("from PartyAgreement pag where pag.authorizingParty.id =:partyId", PartyAgreement.class)
                .setParameter("partyId", party.getId())
                .getResultList();
    }

    @Override
    public List<PartyAgreement> findPartyAgreementsByCriteria(Party authorizingParty, Party delegateParty, Transaction transaction, Long businessDomainId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PartyAgreement> cq = cb.createQuery(PartyAgreement.class);
        Root<PartyAgreement> pa = cq.from(PartyAgreement.class);
        List<Predicate> predicates = new ArrayList<>();

        if(authorizingParty != null && authorizingParty.getId() != null) {
            predicates.add(cb.equal(pa.get(PartyAgreement_.authorizingParty), authorizingParty));
        }

        if(delegateParty != null && delegateParty.getId() != null) {
            predicates.add(cb.equal(pa.get(PartyAgreement_.delegateParty), delegateParty));
        }

        if ( (authorizingParty == null || authorizingParty.getId() == null)
                && (delegateParty == null || delegateParty.getId() == null) ) {
        	Join<PartyAgreement, Party> authPartyJoin = pa.join(PartyAgreement_.authorizingParty, JoinType.INNER);
        	Join<PartyAgreement, Party> delPartyJoin = pa.join(PartyAgreement_.delegateParty, JoinType.INNER);
            predicates.add(cb.equal(authPartyJoin.get(Party_.businessDomain).get(BusinessDomain_.id), businessDomainId));
            predicates.add(cb.equal(delPartyJoin.get(Party_.businessDomain).get(BusinessDomain_.id), businessDomainId));
        }

        if(transaction != null && transaction.getId() != null) {
            predicates.add(cb.or(cb.isEmpty(pa.get(PartyAgreement_.transactions)), cb.isMember(transaction, pa.get(PartyAgreement_.transactions))));
        }

        cq.select(pa).distinct(true);
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        return entityManager.createQuery(cq).getResultList();
    }
}
