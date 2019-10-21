/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class is used as an interface between the user configuration screens and
 * the user entity.
 *
 * @author batrian
 */
public class UserForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(UserForm.class.getName());

    private User user;

    private String oldPassword;

    //security checking for password
    private String passwordCheck;

    private Boolean changePasswordFlag;

    private Boolean isChangePwdMode;

    /*
     * A new user configuration to be added to user object
     */
    private UserAccessRights newUserConfiguration = new UserAccessRights();

    /*
     * User original configuration which is currently edited, in case the user cancels the modification
     */
    private UserAccessRights originalUserConfiguration = new UserAccessRights();

    /*
     * Set cannot be directly bind by spring
     */
    private List<UserAccessRightsElement> userAccessRights = new ArrayList<>();

    /*
     * Constructors
     */
    public UserForm() {
        this.changePasswordFlag = false;
        this.getNewUserConfiguration().setBusinessDomain(new BusinessDomain());
        this.getNewUserConfiguration().setRole(new UserRole());
        this.getNewUserConfiguration().setParty(new Party());
    }

    public UserForm(User user) {
        this();
        this.user = user;
    }

    public Long getId() {
        return user == null ? null : user.getId();
    }

    /*
     * Getters and Setters
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public UserAccessRights getNewUserConfiguration() {
        return newUserConfiguration;
    }

    public void setNewUserConfiguration(UserAccessRights userConfiguration) {
        this.newUserConfiguration = userConfiguration;
    }

    public UserAccessRights getOriginalUserConfiguration() {
        return originalUserConfiguration;
    }

    public void setOriginalUserConfiguration(UserAccessRights originalUserConfiguration) {
        this.originalUserConfiguration = originalUserConfiguration;
    }

    public List<UserAccessRightsElement> getUserAccessRights() {
        return userAccessRights;
    }

    public void setUserAccessRights(List<UserAccessRightsElement> userAccessRights) {
        this.userAccessRights = userAccessRights;
    }

    public void setUserAccessRights(Set<UserAccessRightsElement> userAccessRights) {
        this.userAccessRights = new ArrayList<>();
        this.userAccessRights.addAll(userAccessRights);
    }

    public void addAccessRights(UserAccessRightsElement uar) {

        if (userAccessRights == null) {
            userAccessRights = new ArrayList<>();
        }

        //if the same uar exists but is marked for deletion than only the flag value is changed
        Boolean updated = false;

        Iterator<UserAccessRightsElement> i = this.userAccessRights.iterator();
        while (i.hasNext()) {
            UserAccessRightsElement userAccessRights = i.next();
            if (userAccessRights.getUar().equals(uar.getUar()) && userAccessRights.getIsMarkedForDeletion()) {
                userAccessRights.setIsMarkedForDeletion(false);
                updated = true;
            }
        }

        if (!updated) {
            userAccessRights.add(uar);
        }

    }

    public void findAndUpdateConfiguration(Long cfgId) {

        if (cfgId == null || CollectionUtils.isEmpty(this.userAccessRights)) {
            logger.warn("findAndUpdateConfiguration: configuration id null or empty configuration list!");
            return;
        }

        Iterator<UserAccessRightsElement> i = this.userAccessRights.iterator();

        while (i.hasNext()) {
            UserAccessRightsElement userAccessRights = i.next();
            if (userAccessRights.getUar().getId() != null && userAccessRights.getUar().getId().equals(cfgId)) {
                userAccessRights.getUar().setRole(this.getNewUserConfiguration().getRole());
                userAccessRights.getUar().setBusinessDomain(this.getNewUserConfiguration().getBusinessDomain());
                userAccessRights.getUar().setParty(this.newUserConfiguration.getParty());
            }

        }
    }

    public Boolean getChangePasswordFlag() {
        return changePasswordFlag;
    }

    public void setChangePasswordFlag(Boolean changePasswordFlag) {
        this.changePasswordFlag = changePasswordFlag;
    }

    public Boolean getIsChangePwdMode() {
        return isChangePwdMode;
    }

    public void setIsChangePwdMode(Boolean isChangePwdMode) {
        this.isChangePwdMode = isChangePwdMode;
    }
}
