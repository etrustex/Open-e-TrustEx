package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.RetentionPolicySearchForm;
import eu.europa.ec.etrustex.admin.old.web.utils.FormUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by guerrpa on 23/05/2016.
 */
@Component
public class RetentionPolicySearchValidator implements Validator {
    @Autowired
    ValidationHelper validationHelper;

    @Override
    public boolean supports(Class<?> aClass) {
        return RetentionPolicySearchForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RetentionPolicySearchForm form = (RetentionPolicySearchForm) o;

        /*
         * UC0030_BR11	At least one search field should be filled in.
         */
        if (FormUtil.convertDefaultOptionToNull(form.getBusinessDomain_search().getId()) == null
                && StringUtils.isBlank(form.getTransaction_search())
                && form.getActiveFlag_search() == null) {
            errors.rejectValue("businessDomain_search", ValidationHelper.SEARCH_MANDATORY_FIELD);
        }
    }
}
