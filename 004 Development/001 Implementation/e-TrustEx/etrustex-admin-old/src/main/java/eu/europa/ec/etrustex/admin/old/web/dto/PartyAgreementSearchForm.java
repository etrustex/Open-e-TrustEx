package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;

import java.io.Serializable;

public class PartyAgreementSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean searchOnLoad;
    private Party authorizingParty_search;
    private Party delegateParty_search;
    private Transaction transaction_search;

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }

    public Party getAuthorizingParty_search() {
        return authorizingParty_search;
    }

    public void setAuthorizingParty_search(Party authorizingParty_search) {
        this.authorizingParty_search = authorizingParty_search;
    }

    public Party getDelegateParty_search() {
        return delegateParty_search;
    }

    public void setDelegateParty_search(Party delegateParty_search) {
        this.delegateParty_search = delegateParty_search;
    }

    public Transaction getTransaction_search() {
        return transaction_search;
    }

    public void setTransaction_search(Transaction transaction_search) {
        this.transaction_search = transaction_search;
    }
}
