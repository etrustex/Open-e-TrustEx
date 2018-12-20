/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.PartySearchForm;
import eu.europa.ec.etrustex.admin.old.web.utils.FormUtil;
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
public class PartySearchValidator implements Validator {
    // Form field names
    private static final String CRITERIA_NAME = "name_search";
    private static final String CRITERIA_USERNAME = "username_search";
    private static final String CRITERIA_IDENTIFIER = "identifier_search";
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return PartySearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PartySearchForm criteria = (PartySearchForm) target;

        log.debug("validating {}", criteria);

        if (StringUtils.isBlank(criteria.getName_search())
                && StringUtils.isBlank(criteria.getUsername_search())
                && StringUtils.isBlank(criteria.getIdentifier_search())
                && FormUtil.convertDefaultOptionToNull(criteria.getBusinessDomainId()) == null
                && criteria.getThirdPartyFlag_search() == null) {
            errors.rejectValue(CRITERIA_NAME, ValidationHelper.SEARCH_MANDATORY_FIELD);
        } else {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_NAME, criteria.getName_search(), "party.name", errors);
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_USERNAME, criteria.getUsername_search(), "party.credentials.username", errors);
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_IDENTIFIER, criteria.getIdentifier_search(), "party.identifier.value", errors);
        }
    }

}
