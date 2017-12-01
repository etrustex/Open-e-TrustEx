/**
 *
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.UserAccessRightsElement;
import eu.europa.ec.cipa.admin.web.dto.UserForm;
import eu.europa.ec.cipa.admin.web.utils.UserRoleEnum;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.admin.User;
import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.services.admin.IUserRoleService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import eu.europa.ec.cipa.etrustex.services.util.EncryptionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author batrian
 */
@Component
public class UserValidator implements Validator {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final static String USERNAME = "user.name";
    private final static String PASSWORD = "user.password";
    //	private final static String CONFIRM_PASSWORD = "user.passwordCheck";
    private final static String ACCESS_RIGHTS = "newUserConfiguration.role.id";

    private final static String OLD_PASSWORD = "oldPassword";
    private final static String CONFIRM_PASSWORD = "passwordCheck";

    @Value("${user.password.login}")
    private boolean userPwdLogin;

    @Autowired
    ValidationHelper validationHelper;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private EncryptionService encryptionService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("validate user form");

        UserForm userForm = (UserForm) target;

        User user = userForm.getUser();

        if (userForm.getIsChangePwdMode() != null && userForm.getIsChangePwdMode()) {
            // Just validate change password
            validateChangePassword(userForm, errors);
        } else {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, USERNAME, "error.user.username.empty");
            validationHelper.validateStringMinimumAndMaximumLength(USERNAME, user.getName(), "user.name", errors);

            if (StringUtils.isNotBlank(user.getPassword())) {
                validationHelper.validateStringMinimumAndMaximumLength(PASSWORD, user.getPassword(), "user.password", errors);
            } else if (userPwdLogin && userForm.getIsChangePwdMode() != null && userForm.getIsChangePwdMode()) {
                // UC80_BR39
                errors.rejectValue(PASSWORD, "error.user.password.empty");
            }

            if (StringUtils.isNotBlank(user.getName()) && userService.exists(user.getName().trim()) && user.getId() == null) {
                errors.rejectValue(USERNAME, "error.user.username.exists");
            }

            if ((StringUtils.isNotEmpty(user.getPassword()) || StringUtils.isNotEmpty(userForm.getPasswordCheck()))
                    && !StringUtils.equals(user.getPassword(), userForm.getPasswordCheck())) {
                errors.rejectValue(PASSWORD, "error.user.password.mismatch");
            }

            if (CollectionUtils.isEmpty(userForm.getUserAccessRights())) {
                errors.rejectValue(ACCESS_RIGHTS, "error.user.accessRights.empty");
            } else {
                boolean allMarkedForDeletion = true;
                for (UserAccessRightsElement userAccessRightsElement : userForm.getUserAccessRights()) {
                    if (!userAccessRightsElement.getIsMarkedForDeletion()) {
                        allMarkedForDeletion = false;
                        break;
                    }
                }

                if (allMarkedForDeletion) {
                    errors.rejectValue(ACCESS_RIGHTS, "error.user.accessRights.empty");
                }
            }

            if (userForm.getUserAccessRights() != null) {
                validateUserConfigurations(userForm, errors);
            }
        }
    }

    /*
    * UC80_BR43	A user can have only one configuration per business domain.
    * E.g. There shall be no user having ADM and CBO role (or any other couple of roles) for the same business domain.
    */
    public void validateNewConfigurationAgainstExisting(Object target, Errors errors) {
        log.debug("validate new configuration against existing");

        UserForm userForm = (UserForm) target;
        UserAccessRights newConfig = userForm.getNewUserConfiguration();
        List<UserAccessRightsElement> accessRights = userForm.getUserAccessRights();

        if (!CollectionUtils.isEmpty(accessRights)) {
            Long businessDomainId = Objects.equals(newConfig.getBusinessDomain().getId(), Long.valueOf("-1")) ? null : newConfig.getBusinessDomain().getId();
            String roleCode = userRoleService.getRole(newConfig.getRole().getId()).getCode();
            Party party = newConfig.getParty();

            for (UserAccessRightsElement uare : accessRights) {
                if (!uare.getIsMarkedForDeletion()
                        && Objects.equals(businessDomainId, uare.getUar().getBusinessDomain().getId())
                        // It is possible to have multiple LBO ocnfigurations for differnt parties
                        && (!(roleCode.equals(UserRoleEnum.LBO.name()) && uare.getUar().getRole().getCode().equals(UserRoleEnum.LBO.name())
                        && !Objects.equals(party, uare.getUar().getParty())))) {
                    errors.rejectValue(ACCESS_RIGHTS, "error.user.configuration.bd.duplicate");
                    break;
                }
            }
        }
    }

    private void validateChangePassword(UserForm userForm, Errors errors) {
        // Validate minimum and maximum lenght
        validationHelper.validateStringMinimumAndMaximumLength(OLD_PASSWORD, userForm.getOldPassword(), "user.old.password", errors);
        validationHelper.validateStringMinimumAndMaximumLength(PASSWORD, userForm.getUser().getPassword(), "user.new.password", errors);
        validationHelper.validateStringMinimumAndMaximumLength(CONFIRM_PASSWORD, userForm.getPasswordCheck(), "user.new.password.confirm", errors);

        // Validate old password
        String oldPwd = userService.getUser(userForm.getUser().getId()).getPassword();
        try {
            if (!StringUtils.equals(oldPwd, encryptionService.encryptPassword(userForm.getOldPassword()))) {
                errors.rejectValue(OLD_PASSWORD, "error.user.password.wrong");
            }

            // Validate new password and confirm password
            if ((StringUtils.isNotEmpty(userForm.getUser().getPassword()) || StringUtils.isNotEmpty(userForm.getPasswordCheck()))
                    && !StringUtils.equals(userForm.getUser().getPassword(), userForm.getPasswordCheck())) {
                errors.rejectValue(PASSWORD, "error.user.password.mismatch");
            }

            // Validate that old and new passwords are different.
            if (StringUtils.equals(oldPwd, encryptionService.encryptPassword(userForm.getUser().getPassword()))) {
                errors.rejectValue(PASSWORD, "error.user.password.invalid");
            }
        } catch (NoSuchAlgorithmException e) {
            errors.rejectValue(OLD_PASSWORD, "common.system.error");
            log.error(e.getMessage(), e);
        }
    }

    /*
     * UC80_BR43	A user can have only one configuration per business domain.
     * E.g. There shall be no user having ADM and CBO role (or any other couple of roles) for the same business domain.
     */
    private void validateUserConfigurations(final UserForm form, Errors errors) {

        int sameUAR = CollectionUtils.select(form.getUserAccessRights(), new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                boolean duplicate = false;
                UserAccessRightsElement userAccessRightsElement = (UserAccessRightsElement) object;

                if (!userAccessRightsElement.getIsMarkedForDeletion()) {
                    Long businessDomainId = userAccessRightsElement.getUar().getBusinessDomain().getId();
                    String roleCode = userAccessRightsElement.getUar().getRole().getCode();
                    Party party = userAccessRightsElement.getUar().getParty();
                    int matches = 0;

                    for (Iterator<UserAccessRightsElement> iterator = form.getUserAccessRights().iterator(); iterator.hasNext() && matches < 2; ) {
                        UserAccessRightsElement uare = iterator.next();

                        if (Objects.equals(businessDomainId, uare.getUar().getBusinessDomain().getId())
                                // It is possible to have multiple LBO ocnfigurations for differnt parties
                                && (!(roleCode.equals(UserRoleEnum.LBO.name()) && uare.getUar().getRole().getCode().equals(UserRoleEnum.LBO.name())
                                && !Objects.equals(party, uare.getUar().getParty())))) {
                            matches++;
                        }
                    }

                    if (matches > 1) { // 1 is the UserAccessRightsElement being compared with itself
                        duplicate = true;
                    }
                }

                return duplicate;
            }
        }).size();

        if (sameUAR > 1) {
            errors.rejectValue(ACCESS_RIGHTS, "error.user.configuration.bd.duplicate");
        }

    }
}
