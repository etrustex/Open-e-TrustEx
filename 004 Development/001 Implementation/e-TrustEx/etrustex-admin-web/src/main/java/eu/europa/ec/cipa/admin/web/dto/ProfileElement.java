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
public class ProfileElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private List<Role> roles;
	
	public ProfileElement(){
	}
	
	public ProfileElement(Long id, String name, List<Role> roles) {
		this.id = id;
		this.name = name;
		this.roles = roles;
	}
	
//	public ProfileElement(Profile p){
//		this.id = p.getId();
//		this.name = p.getName();
//		this.roles = p.getTransactions()
//	}
	
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
