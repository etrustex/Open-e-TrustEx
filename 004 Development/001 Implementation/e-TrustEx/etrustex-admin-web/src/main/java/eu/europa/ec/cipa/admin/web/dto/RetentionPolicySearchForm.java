package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;

import java.io.Serializable;

/**
 * Created by guerrpa on 23/05/2016.
 */
public class RetentionPolicySearchForm implements Serializable {
    private Boolean activeFlag_search;
    private String transaction_search;
    private BusinessDomain businessDomain_search;
    private Boolean searchOnLoad;

    public Boolean getActiveFlag_search() {
        return activeFlag_search;
    }

    public void setActiveFlag_search(Boolean activeFlag_search) {
        this.activeFlag_search = activeFlag_search;
    }

    public String getTransaction_search() {
        return transaction_search;
    }

    public void setTransaction_search(String transaction_search) {
        this.transaction_search = transaction_search;
    }

    public BusinessDomain  getBusinessDomain_search() {
        return businessDomain_search;
    }

    public void setBusinessDomain_search(BusinessDomain  businessDomain_search) {
        this.businessDomain_search = businessDomain_search;
    }

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }
}
