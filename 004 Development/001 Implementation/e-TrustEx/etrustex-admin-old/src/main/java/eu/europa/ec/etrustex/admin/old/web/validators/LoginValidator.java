/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.LoginForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author abatrinu
 */
@Component
//TODO: this class is not currently used
public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginForm.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        System.out.println("LoginValidator - validate");
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "user.name", "common.error.field.mandatory");
        ValidationUtils.rejectIfEmptyOrWhitespace(e, "user.password", "common.error.field.mandatory");
        LoginForm lf = (LoginForm) obj;
    }

}
