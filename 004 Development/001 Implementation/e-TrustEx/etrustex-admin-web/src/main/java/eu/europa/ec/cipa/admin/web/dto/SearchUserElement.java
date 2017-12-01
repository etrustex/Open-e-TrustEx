/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;
import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.Role;

/**
 * @author batrian
 * 
 */
public class SearchUserElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private Role role;
	private List<BusinessDomainElement> businessDomains;
	private List<SelectElement> parties;

	public SearchUserElement() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<BusinessDomainElement> getBusinessDomains() {
		return businessDomains;
	}

	public void setBusinessDomains(List<BusinessDomainElement> businessDomains) {
		this.businessDomains = businessDomains;
	}

	public List<SelectElement> getParties() {
		return parties;
	}

	public void setParties(List<SelectElement> parties) {
		this.parties = parties;
	}

}
