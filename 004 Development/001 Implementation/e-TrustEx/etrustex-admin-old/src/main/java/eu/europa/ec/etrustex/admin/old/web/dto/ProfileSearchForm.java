/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

import java.io.Serializable;

public class ProfileSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name_search;
    private BusinessDomain businessDomain;
    private String transactionName;
    private Boolean searchOnLoad;


    public String getName_search() {
        return name_search;
    }

    public void setName_search(String name_search) {
        this.name_search = name_search;
    }

    public BusinessDomain getBusinessDomain() {
        return businessDomain;
    }

    public void setBusinessDomain(BusinessDomain businessDomain) {
        this.businessDomain = businessDomain;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }
}