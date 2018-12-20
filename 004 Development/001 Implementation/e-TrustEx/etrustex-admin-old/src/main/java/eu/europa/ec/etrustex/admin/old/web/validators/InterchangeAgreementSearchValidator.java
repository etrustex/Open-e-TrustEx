/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.InterchangeAgreementSearchForm;
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
public class InterchangeAgreementSearchValidator implements Validator {
    // Form field names
    private static final String CRITERIA_PARTY_NAME = "party_search.partyName";
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return InterchangeAgreementSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InterchangeAgreementSearchForm criteria = (InterchangeAgreementSearchForm) target;

        log.debug("validating {}", criteria);

        if ((criteria.getParty_search() == null || StringUtils.isBlank(criteria.getParty_search().getPartyName()))
                && StringUtils.isBlank(criteria.getIdentifier_search())
                && (criteria.getProfileId() == null || criteria.getProfileId() == -1)
                && (criteria.getPartyRoleId() == null || criteria.getPartyRoleId() == -1)) {
            errors.rejectValue(CRITERIA_PARTY_NAME, ValidationHelper.SEARCH_MANDATORY_FIELD);
        } else {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_PARTY_NAME, criteria.getParty_search().getPartyName(), "party.name", errors);
        }
    }

}
