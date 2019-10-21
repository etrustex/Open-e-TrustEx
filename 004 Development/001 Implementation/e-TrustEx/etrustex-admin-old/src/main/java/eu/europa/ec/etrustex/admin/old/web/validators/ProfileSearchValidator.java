/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.ProfileSearchForm;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class ProfileSearchValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(ProfileSearchValidator.class);

    private static final String CRITERIA_NAME = "name_search";
    private static final String CRITERIA_TX_NAME = "transactionName";

    @Autowired
    ValidationHelper validationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProfileSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileSearchForm criteria = (ProfileSearchForm) target;

        logger.debug("validating {}", criteria);

        if (StringUtils.isBlank(criteria.getName_search())
                && StringUtils.isBlank(criteria.getTransactionName())
                && (criteria.getBusinessDomain() == null || criteria.getBusinessDomain().getId() == null || criteria.getBusinessDomain().getId().equals(Long.valueOf("-1")))) {
            errors.rejectValue(CRITERIA_NAME, ValidationHelper.SEARCH_MANDATORY_FIELD);
        }

        if (!StringUtils.isBlank(criteria.getName_search())) {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_NAME, criteria.getName_search(), "profile.name", errors);
        }

        if (!StringUtils.isBlank(criteria.getTransactionName())) {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_TX_NAME, criteria.getTransactionName(), "transaction.name", errors);
        }
    }

}
