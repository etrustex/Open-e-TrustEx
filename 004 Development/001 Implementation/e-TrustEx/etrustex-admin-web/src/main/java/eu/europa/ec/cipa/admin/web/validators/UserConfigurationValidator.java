/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.utils.UserRoleEnum;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.domain.admin.UserRole;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserAccessRightsService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserRoleService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

/**
 * @author batrian
 *
 */
@Component
public class UserConfigurationValidator implements Validator {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserConfigurationValidator.class.getName());
	
	@Autowired
	private IUserRoleService userRoleService;
	
	@Autowired
	private IPartyService partyService;
	
	@Autowired
	private IUserService userService;

	@Autowired
	private IUserAccessRightsService userAccessRightsService;

	@Override
    public boolean supports(Class<?> clazz) {
        return UserAccessRights.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    	logger.debug("UserConfigurationValidator - validate...");

    	UserAccessRights userConfiguration = (UserAccessRights) target;


        // UC80_BR07	User role is mandatory.
        UserRole role = userConfiguration.getRole();
        if (role == null || role.getId() == null || role.getId() == -1){
    		errors.rejectValue("role.id", "error.user.userrole.empty");
    	} else {
    		role = userRoleService.getRole(role.getId());

            BusinessDomain businessDomain = userConfiguration.getBusinessDomain();

            // UC80_BR08	The business domain is mandatory if selected user role is not ADM.
            if ( !StringUtils.equalsIgnoreCase(role.getCode(), UserRoleEnum.ADM.name())
                    && (businessDomain == null || businessDomain.getId() == null || businessDomain.getId() == -1) ) {
                errors.rejectValue("businessDomain.id", "error.user.businessDomain.empty");
            }

            // UC80_BR09	The party is mandatory if selected user role is LBO, and must pertain to the chosen business domain.
            Party party = userConfiguration.getParty();
            if ( StringUtils.equalsIgnoreCase(role.getCode(), UserRoleEnum.LBO.name()) && (party == null || party.getId() == null) ) {
                errors.rejectValue("party.id", "error.user.party.empty");
            } else {
                party = partyService.getParty(party.getId());
                if ( party != null && party.getId() != null
                        && businessDomain != null && businessDomain.getId() != null && businessDomain.getId() != -1
                        && (party.getBusinessDomain() == null || party.getBusinessDomain() != null && !Objects.equals(party.getBusinessDomain().getId(), businessDomain.getId()))) {
                    logger.debug("selected business domain = {} <> {} = selected party business domain", businessDomain.getId(), party.getBusinessDomain().getId());
                    errors.rejectValue("party.id", "error.user.party.notInBusinessDomain");
                }
            }
    	}

    	logger.debug("UserConfigurationValidator - validate END.");
    }

}
