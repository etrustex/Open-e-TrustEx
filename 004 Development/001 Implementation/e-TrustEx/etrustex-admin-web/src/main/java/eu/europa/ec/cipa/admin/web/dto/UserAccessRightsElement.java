/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;

import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;

/**
 * @author batrian
 * 
 */
public class UserAccessRightsElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private UserAccessRights uar;
	private Boolean isMarkedForDeletion = false;

	public UserAccessRightsElement(){}
	
	public UserAccessRightsElement(UserAccessRights uar){
		super();
		this.uar = uar;
		this.isMarkedForDeletion = false;
	}
	
	public UserAccessRights getUar() {
		return uar;
	}

	public void setUar(UserAccessRights uar) {
		this.uar = uar;
	}

	public Boolean getIsMarkedForDeletion() {
		return isMarkedForDeletion;
	}

	public void setIsMarkedForDeletion(Boolean isMarkedForDeletion) {
		this.isMarkedForDeletion = isMarkedForDeletion;
	}

}
