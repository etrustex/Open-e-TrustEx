/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.UserSearchForm;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author batrian
 */
@Component
public class UserSearchValidator implements Validator {
    // Form field names
    private static final String CRITERIA_USERNAME = "usernameSearch";
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSearchForm criteria = (UserSearchForm) target;

        log.debug("validating {}", criteria);

        if (StringUtils.isBlank(criteria.getUsernameSearch())
                && (criteria.getUserRoleIdSearch() == null || criteria.getUserRoleIdSearch() == -1)
                && (criteria.getBusinessDomainIdSearch() == null || criteria.getBusinessDomainIdSearch() == -1)
                && (criteria.getPartyIdSearch() == null || criteria.getPartyIdSearch() == -1)) {
            errors.rejectValue(CRITERIA_USERNAME, ValidationHelper.SEARCH_MANDATORY_FIELD);
        } else {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_USERNAME, criteria.getUsernameSearch(), "user.name", errors);
        }
    }

}
