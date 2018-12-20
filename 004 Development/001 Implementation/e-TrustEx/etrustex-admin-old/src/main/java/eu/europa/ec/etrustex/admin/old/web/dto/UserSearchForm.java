/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import java.io.Serializable;

/**
 * @author batrian
 */
public class UserSearchForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usernameSearch;
    private Long userRoleIdSearch;
    private Long businessDomainIdSearch;
    private Long partyIdSearch;
    private Boolean searchOnLoad;

    public UserSearchForm() {
    }

    public String getUsernameSearch() {
        return usernameSearch;
    }

    public void setUsernameSearch(String username) {
        this.usernameSearch = username;
    }

    public Long getUserRoleIdSearch() {
        return userRoleIdSearch;
    }

    public void setUserRoleIdSearch(Long userRoleId) {
        this.userRoleIdSearch = userRoleId;
    }

    public Long getBusinessDomainIdSearch() {
        return businessDomainIdSearch;
    }

    public void setBusinessDomainIdSearch(Long businessDomainId) {
        this.businessDomainIdSearch = businessDomainId;
    }

    public Long getPartyIdSearch() {
        return partyIdSearch;
    }

    public void setPartyIdSearch(Long partyId) {
        this.partyIdSearch = partyId;
    }

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }

}
