/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.admin.UserAccessRights;

import java.io.Serializable;

/**
 * @author batrian
 */
public class UserAccessRightsElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserAccessRights uar;
    private Boolean isMarkedForDeletion = false;

    public UserAccessRightsElement() {
    }

    public UserAccessRightsElement(UserAccessRights uar) {
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
