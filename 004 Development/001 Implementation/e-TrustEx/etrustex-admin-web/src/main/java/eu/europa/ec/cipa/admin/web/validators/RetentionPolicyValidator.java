package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.BusinessDomainElement;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.dto.SlaPolicyForm;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.integration.task.ISlaPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by guerrpa on 23/05/2016.
 */
@Component
public class RetentionPolicyValidator implements Validator{
    @Autowired ValidationHelper validationHelper;
    @Autowired private ISlaPolicyService slaPolicyService;

    @Override
    public boolean supports(Class<?> aClass) {
        return SlaPolicyForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SlaPolicyForm form = (SlaPolicyForm) o;

        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /*
         * UC0030_BR02	The selected business domain must be one for which the User has access rights.
         */
        if ( !userInfo.getBusinessDomains().contains(new BusinessDomainElement(form.getBusinessDomain())) ) {
            errors.rejectValue("businessDomain", "error.slaPolicy.businessDomain.mandatory", "Business domain is mandatory");
        }

        /*
         * UC0030_BR15	Transaction is mandatory.
         */
        if ( form.getTransaction() == null || form.getTransaction().getId() == null ) {
            errors.rejectValue("transaction", "error.slaPolicy.transaction.mandatory", "Transaction is mandatory");
        } else {
            /*
             * UC0030_BR03  No other retention policy shall already exist for the specified transaction (identified by transaction name and version)
             * in the context of the selected business domain.
             * However, the existence of a retention policy for a transaction having the same name but different version shall not obstruct the
             * creation of the new retention policy in the same business domain.
             */
            List<BusinessDomain> businessDomains = new ArrayList<>();
            businessDomains.add(form.getBusinessDomain());
            SlaPolicy slaPolicy = new SlaPolicy();
            slaPolicy.setTransaction(form.getTransaction());

            // This will search by transaction name and business domain (not record id)
            List<SlaPolicy> slaPolicies = slaPolicyService.findPoliciesByCriteriaForView(slaPolicy, businessDomains);

            if (!CollectionUtils.isEmpty(slaPolicies)) {
                SlaPolicy existingPolicy = form.getId() == null ? null : slaPolicyService.findById(form.getId());

                if (form.getId() == null) { // New policy
                    for (SlaPolicy s : slaPolicies) {
                        if ( Objects.equals(s.getTransaction().getId(), form.getTransaction().getId()) ) {
                            errors.rejectValue("transaction", "error.slaPolicy.transaction.exists", "Transaction already used");
                            break;
                        }
                    }
                } else { // Existing policy
                    for (SlaPolicy s : slaPolicies) {
                        if ( Objects.equals(s.getTransaction().getId(), form.getTransaction().getId()) // same tx
                                && !Objects.equals(existingPolicy.getId(), s.getId())) { // but different policies
                            errors.rejectValue("transaction", "error.slaPolicy.transaction.exists", "Transaction already used");
                            break;
                        }
                    }
                }
            }
            if ( slaPolicies.size() > 0
                    && ( form.getId() == null || !slaPolicies.get(0).getTransaction().equals(slaPolicyService.findById(form.getId()).getTransaction()) ) ) {

            }
        }

        /*
         * UC0030_BR04	"Duration" is mandatory
         */
        if (form.getValue() == null) {
            errors.rejectValue("value", "error.slaPolicy.value.mandatory", "Value is mandatory");
        } else if(form.getValue().compareTo(Integer.valueOf(1)) < 0) {
            /*
             * UC0030_ BR06	"Duration" must be greater or equal to 1
             */
            errors.rejectValue("value", "error.slaPolicy.value.min", "Value must be greater or equal to 1");
        }

        /*
         * UC0030_ BR07	"Active" flag is mandatory (true or false)
         */
        if (form.getActiveFlag() == null) {
            errors.rejectValue("activeFlag", "error.slaPolicy.activeFlag.mandatory", "Active flag is mandatory");
        }

    }
}
