/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.EndpointSearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.utils.FormUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.UserRoleEnum;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EndpointSearchValidator implements Validator {
    @Autowired
    ValidationHelper validationHelper;
    @Autowired
    private IBusinessDomainService businessDomainService;

    @Override
    public boolean supports(Class<?> clazz) {
        return EndpointSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EndpointSearchForm form = (EndpointSearchForm) target;
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /*
         * ETRUSTEX-1407 CIPAdmin shall allow CBO users to configure routing endpoints
         */
        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            form.setBusinessDomain_search(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()));
        }

        // UC130_BR23	At least one search field should be filled in.
        boolean allNonInputFieldsEmpty = form.getConfigurationType() == null
                && form.getIsActive_search() == null
                && FormUtil.convertDefaultOptionToNull(form.getBusinessDomain_search().getId()) == null
                && form.getTansaction_search().getId() == null
                && form.getInterchangeAgreement_search().getId() == null
                && form.getParty_search().getId() == null;

        if (allNonInputFieldsEmpty) {
            if (StringUtils.isBlank(form.getUsername()) && StringUtils.isBlank(form.getProxyUsername())) {
                errors.rejectValue("username", "error.search.criteria.needed");
            }
            /*
             * UC130_BR22 If a single search field is filled in, it needs to contain at least 2 characters (free text fields).
             */
            else if (StringUtils.isBlank(form.getUsername()) && StringUtils.isNotBlank(form.getProxyUsername())) {
                validationHelper.validateStringMinimumAndMaximumLength("proxyUsername", form.getProxyUsername(), "endpoint.proxyUsername", errors);
            } else if (StringUtils.isNotBlank(form.getUsername()) && StringUtils.isBlank(form.getProxyUsername())) {
                validationHelper.validateStringMinimumAndMaximumLength("username", form.getUsername(), "endpoint.username", errors);
            }
        }
    }
}
