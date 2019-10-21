/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.DocumentSearchForm;
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
public class DocumentSearchValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(DocumentSearchValidator.class);

    // Form field names
    private static final String CRITERIA_NAME = "name_search";
    private static final String CRITERIA_LOCAL_NAME = "localName_search";
    private static final String CRITERIA_TYPE_CODE = "typeCode_search";

    @Autowired
    ValidationHelper validationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return DocumentSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DocumentSearchForm criteria = (DocumentSearchForm) target;

        logger.debug("validating {}", criteria);

        if (StringUtils.isBlank(criteria.getName_search())
                && StringUtils.isBlank(criteria.getLocalName_search())
                && StringUtils.isBlank(criteria.getTypeCode_search())) {
            errors.rejectValue(CRITERIA_NAME, ValidationHelper.SEARCH_MANDATORY_FIELD);
        } else {
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_NAME, criteria.getName_search(), "document.name", errors);
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_LOCAL_NAME, criteria.getLocalName_search(), "document.localName", errors);
            validationHelper.validateStringMinimumAndMaximumLength(CRITERIA_TYPE_CODE, criteria.getTypeCode_search(), "document.typeCode", errors);
        }
    }

}
