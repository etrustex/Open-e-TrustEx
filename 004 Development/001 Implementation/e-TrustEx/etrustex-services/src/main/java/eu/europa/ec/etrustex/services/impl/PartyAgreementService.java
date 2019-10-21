package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyAgreement;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service("partyAgreementService")
public class PartyAgreementService implements IPartyAgreementService {

	@Autowired
	private IPartyAgreementDAO partyAgreementDAO;
    @Autowired
    private IPartyDAO partyDAO;
    @Autowired
    private ITransactionDAO transactionDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PartyAgreement createPartyAgreement(PartyAgreement partyAgreement) {
        Set<Transaction> txs = new HashSet<>();
        for(Transaction tx : partyAgreement.getTransactions()){
            txs.add(transactionDAO.read(tx.getId()));
        }
        partyAgreement.setTransactions(txs);

		return partyAgreementDAO.create(partyAgreement);
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PartyAgreement updatePartyAgreement(PartyAgreement pa) {
        mergeProperties(pa);

        return partyAgreementDAO.update(pa);
    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existsPartyAgreement(Party authorizingParty,
			Party delegateParty)  {
		return partyAgreementDAO.existsPartyAgreement(authorizingParty, delegateParty);
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PartyAgreement findPartyAgreementById(Long id) {
        PartyAgreement partyAgreement = partyAgreementDAO.read(id);

        if (partyAgreement != null) {
            Hibernate.initialize(partyAgreement.getTransactions());
        }

        return partyAgreement;
    }

    @Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Party> getThirdPartiesForDelegatingParty(Long partyId) {
		return partyAgreementDAO.getAuthorisedPartiesFor(partyId);
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePartyAgreement(PartyAgreement partyAgreement) {
        partyAgreement = partyAgreementDAO.read(partyAgreement.getId());
        // remove transactions associations
        if(!CollectionUtils.isEmpty(partyAgreement.getTransactions())){
            for (Iterator<Transaction> it = partyAgreement.getTransactions().iterator(); it.hasNext();){
                it.next();
                it.remove();
            }
            partyAgreementDAO.update(partyAgreement);
        }

        partyAgreementDAO.delete(partyAgreement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartyAgreement> findPartyAgreementsForAuthorizingParty(Party party) {
        return partyAgreementDAO.findPartyAgreementsForAuthorizingParty(party);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<PartyAgreement> findPartyAgreementsByCriteria(Party authorizingParty, Party delegateParty, Transaction transaction, Long businessDomainId) {
        List<PartyAgreement> results = partyAgreementDAO.findPartyAgreementsByCriteria(authorizingParty, delegateParty, transaction, businessDomainId);

        for(PartyAgreement pa : results) {
            Hibernate.initialize(pa.getTransactions());
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<String> getDelegateThirdPartiesNamesFor(Long partyId) {
        return partyAgreementDAO.getDelegateThirdPartiesNamesFor(partyId);
    }


    public IPartyAgreementDAO getPartyAgreementDAO() {
		return partyAgreementDAO;
	}

	public void setPartyAgreementDAO(IPartyAgreementDAO partyAgreementDAO) {
		this.partyAgreementDAO = partyAgreementDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PartyAgreement retrievePartyAgreement(Party authorizingParty,
			Party delegateParty) {
		return partyAgreementDAO.retrievePartyAgreement(authorizingParty, delegateParty);
	}

	@Override
    @Transactional(readOnly = true)
	public Boolean existsAgreementForParty(Party delegateParty) {
		return partyAgreementDAO.existsAgreementForParty(delegateParty);
	}

    private void mergeProperties(PartyAgreement pa) {
        if(pa.getAuthorizingParty() != null && pa.getAuthorizingParty().getId() != null){
            pa.setAuthorizingParty(partyDAO.read(pa.getAuthorizingParty().getId()));
        }

        if(pa.getDelegateParty() != null && pa.getDelegateParty().getId() != null){
          pa.setDelegateParty(partyDAO.read(pa.getDelegateParty().getId()));
        }

        Set<Transaction> txs = new HashSet<>();
        for(Transaction tx : pa.getTransactions()){
            txs.add(transactionDAO.read(tx.getId()));
        }
        pa.setTransactions(txs);
    }
}
