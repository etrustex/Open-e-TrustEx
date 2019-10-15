/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.RoleForm;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.services.IRoleService;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.reflect.InvocationTargetException;

/**
 * @author batrian
 */
@Component
public class RoleValidator implements Validator {
    private final static String CODE = "code";
    private final static String NAME = "name";
    @Autowired
    IRoleService roleService;
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            Role role = toDomain((RoleForm) target);
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, CODE, "role.code.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME, "role.name.empty");

            validationHelper.validateStringMinimumAndMaximumLength(CODE, role.getCode(), "role.code", errors);
            validationHelper.validateStringMinimumAndMaximumLength(NAME, role.getName(), "role.name", errors);

            if (!roleService.isUniqueRoleCode(role)) {
                errors.rejectValue(CODE, "error.role.code.notUnique");
            }

            if (!roleService.isUniqueRoleName(role)) {
                errors.rejectValue(NAME, "error.role.name.notUnique");
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            errors.reject("common.system.error", new Object[]{e.getMessage()}, "System error" + e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    private Role toDomain(RoleForm form) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Role role = new Role();
        PropertyUtils.copyProperties(role, form);

        return role;
    }
}
