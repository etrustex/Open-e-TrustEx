/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import java.io.Serializable;

/**
 * @author batrian
 */
public class PartySearchForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name_search;
    private String username_search;
    private String identifier_search;
    private Long businessDomainId;
    private Boolean thirdPartyFlag_search;
    private Boolean searchOnLoad;

    public PartySearchForm() {
    }

    public String getName_search() {
        return name_search;
    }

    public void setName_search(String name) {
        this.name_search = name;
    }

    public String getUsername_search() {
        return username_search;
    }

    public void setUsername_search(String username) {
        this.username_search = username;
    }

    public String getIdentifier_search() {
        return identifier_search;
    }

    public void setIdentifier_search(String identifier) {
        this.identifier_search = identifier;
    }

    public Long getBusinessDomainId() {
        return businessDomainId;
    }

    public void setBusinessDomainId(Long businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

    public Boolean getThirdPartyFlag_search() {
        return thirdPartyFlag_search;
    }

    public void setThirdPartyFlag_search(Boolean thirdPartyFlag_search) {
        this.thirdPartyFlag_search = thirdPartyFlag_search;
    }

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }

}
