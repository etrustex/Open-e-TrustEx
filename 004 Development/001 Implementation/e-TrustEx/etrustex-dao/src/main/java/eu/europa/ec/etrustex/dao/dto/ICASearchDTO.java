/**
 * 
 */
package eu.europa.ec.etrustex.dao.dto;

import java.io.Serializable;

/**
 * @author batrian
 * 
 */
public class ICASearchDTO implements Serializable {
	private static final long serialVersionUID = 1L;

    private String partyName;
    private String identifierValue;
	private Long roleId;
    private Long profileId;
	private Long businessDomainId;

    public ICASearchDTO() {
    }

    public ICASearchDTO(String partyName, String identifierValue, Long roleId, Long profileId, Long businessDomainId) {
        this.partyName = partyName;
        this.identifierValue = identifierValue;
        this.roleId = roleId;
        this.profileId = profileId;
        this.businessDomainId = businessDomainId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Long getBusinessDomainId() {
        return businessDomainId;
    }

    public void setBusinessDomainId(Long businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

}
