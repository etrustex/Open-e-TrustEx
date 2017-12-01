/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Role;

import java.io.Serializable;
import java.util.Set;

public class TransactionSearchForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name_search;
	private String document_search;
	private Role senderRole_search;
	private Role receiverRole_search;
	private Set<Profile> profiles_search;
    private Long businessDomainId;
	private Boolean searchOnLoad;
	
	public String getName_search() {
		return name_search;
	}
	public void setName_search(String name_search) {
		this.name_search = name_search;
	}
	public String getDocument_search() {
		return document_search;
	}
	public void setDocument_search(String document_search) {
		this.document_search = document_search;
	}
	public Role getSenderRole_search() {
		return senderRole_search;
	}
	public void setSenderRole_search(Role senderRole_search) {
		this.senderRole_search = senderRole_search;
	}
	public Role getReceiverRole_search() {
		return receiverRole_search;
	}
	public void setReceiverRole_search(Role receiverRole_search) {
		this.receiverRole_search = receiverRole_search;
	}
	public Boolean getSearchOnLoad() {
		return searchOnLoad;
	}
	public void setSearchOnLoad(Boolean searchOnLoad) {
		this.searchOnLoad = searchOnLoad;
	}

    public Long getBusinessDomainId() {
        return businessDomainId;
    }

    public void setBusinessDomainId(Long businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

    public Set<Profile> getProfiles_search() {
		return profiles_search;
	}
	public void setProfiles_search(Set<Profile> profiles_search) {
		this.profiles_search = profiles_search;
	}
}