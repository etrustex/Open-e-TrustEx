/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.MetadataSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MetadataSearchValidator implements Validator {
    // Form field names
    private static final String CRITERIA_METADATAITEMTYPE = "metaDataItemType_search";

    @Autowired
    ValidationHelper validationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return MetadataSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MetadataSearchForm form = (MetadataSearchForm) target;

        if (form.getMetaDataItemType_search() == null
                && (form.getDocument_search() == null || form.getDocument_search().getId() == null)
                && (form.getInterchangeAgreement_search() == null || form.getInterchangeAgreement_search().getId() == null)
                && (form.getParty_search() == null || form.getParty_search().getId() == null)
                && (form.getProfile_search() == null || form.getProfile_search().getId() == null || form.getProfile_search().getId().equals(Long.valueOf(-1)))
                && (form.getTansaction_search() == null || form.getTansaction_search().getId() == null)) {
            errors.rejectValue(CRITERIA_METADATAITEMTYPE, ValidationHelper.SEARCH_MANDATORY_FIELD);
        }
    }

}
