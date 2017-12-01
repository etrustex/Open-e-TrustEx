package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.InterchangeAgreementForm;
import eu.europa.ec.cipa.admin.web.dto.InterchangeAgreementMultiForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by guerrpa on 03/03/2016.
 *
 * Used to prevent invalid target for validator when coming from ICA view
 */
public class ICACompositeValidator implements Validator {
    @Autowired
    private MultipleInterchangeAgreementValidator multipleInterchangeAgreementValidator;
    @Autowired
    private InterchangeAgreementValidator interchangeAgreementValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return InterchangeAgreementMultiForm.class.equals(clazz) || InterchangeAgreementForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if(o.getClass().equals(InterchangeAgreementMultiForm.class)) {
            multipleInterchangeAgreementValidator.validate(o, errors);
        } else {
            interchangeAgreementValidator.validate(o, errors);
        }
    }
}
