/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.TransactionSearchForm;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class TransactionSearchValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(TransactionSearchValidator.class);

    private static final String CRITERIA_NAME = "name_search";
    private static final String CRITERIA_DOCUMENT_NAME = "document_search";

    @Autowired
    ValidationHelper validationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return TransactionSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransactionSearchForm criteria = (TransactionSearchForm) target;

        logger.debug("validating {}", criteria);

        if (StringUtils.isBlank(criteria.getName_search())
                && StringUtils.isBlank(criteria.getDocument_search())
                && (criteria.getSenderRole_search() == null || criteria.getSenderRole_search().getId() == null)
                && (criteria.getReceiverRole_search() == null || criteria.getReceiverRole_search().getId() == null)
                && (criteria.getProfiles_search() == null || criteria.getProfiles_search().isEmpty()) || criteria.getProfiles_search().toArray()[0] == null) {
            errors.rejectValue(CRITERIA_NAME, ValidationHelper.SEARCH_MANDATORY_FIELD);
        }

        if (!StringUtils.isBlank(criteria.getName_search())) {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_NAME, criteria.getName_search(), "transaction.name", errors);
        }

        if (!StringUtils.isBlank(criteria.getDocument_search())) {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_DOCUMENT_NAME, criteria.getDocument_search(), "transaction.document", errors);
        }
    }

}
