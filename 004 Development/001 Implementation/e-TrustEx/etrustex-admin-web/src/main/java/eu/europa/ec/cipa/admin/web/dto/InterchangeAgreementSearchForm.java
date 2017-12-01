/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;

/**
 * @author batrian
 * 
 */
public class InterchangeAgreementSearchForm implements Serializable {
	private static final long serialVersionUID = 1L;

    private PartyForm party_search;
    private String identifier_search;
	private Long partyRoleId;
	private Long profileId;
    private Long businessDomainId;
	private Boolean searchOnLoad;

	public InterchangeAgreementSearchForm() {
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public PartyForm getParty_search() {
        return party_search;
    }

    public void setParty_search(PartyForm party_search) {
        this.party_search = party_search;
    }

    public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public Long getPartyRoleId() {
		return partyRoleId;
	}

	public void setPartyRoleId(Long partyRoleId) {
		this.partyRoleId = partyRoleId;
	}

    public Long getBusinessDomainId() {
        return businessDomainId;
    }

    public void setBusinessDomainId(Long businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

    public Boolean getSearchOnLoad() {
		return searchOnLoad;
	}

	public void setSearchOnLoad(Boolean searchOnLoad) {
		this.searchOnLoad = searchOnLoad;
	}

    public String getIdentifier_search() {
        return identifier_search;
    }

    public void setIdentifier_search(String identifier_search) {
        this.identifier_search = identifier_search;
    }
}
