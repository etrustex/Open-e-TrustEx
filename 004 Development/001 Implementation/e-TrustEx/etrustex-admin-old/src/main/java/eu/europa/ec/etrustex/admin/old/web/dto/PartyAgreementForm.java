package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PartyAgreementForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Party authorizingParty;
    private Party delegateParty;
    private Boolean allTransactions = Boolean.TRUE; // UC160_BR02	Authorization on all transactions is defaulted to Yes.
    private Transaction transaction;
    private List<Transaction> transactions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Party getAuthorizingParty() {
        return authorizingParty;
    }

    public void setAuthorizingParty(Party authorizingParty) {
        this.authorizingParty = authorizingParty;
    }

    public Party getDelegateParty() {
        return delegateParty;
    }

    public void setDelegateParty(Party delegateParty) {
        this.delegateParty = delegateParty;
    }

    public Boolean getAllTransactions() {
        return allTransactions;
    }

    public void setAllTransactions(Boolean allTransactions) {
        this.allTransactions = allTransactions;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
