package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

import java.io.Serializable;
import java.util.Set;

public class ProfileForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String namespace;
    private CIALevel ciaLevel;
    private Set<BusinessDomain> businessDomains;
    private Set<Transaction> transactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public CIALevel getCiaLevel() {
        return ciaLevel;
    }

    public void setCiaLevel(CIALevel ciaLevel) {
        this.ciaLevel = ciaLevel;
    }

    public Set<BusinessDomain> getBusinessDomains() {
        return businessDomains;
    }

    public void setBusinessDomains(Set<BusinessDomain> list) {
        this.businessDomains = list;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
