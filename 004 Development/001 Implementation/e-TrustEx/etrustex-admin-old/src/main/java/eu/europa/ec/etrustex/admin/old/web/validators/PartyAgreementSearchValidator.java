/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.PartyAgreementSearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import eu.europa.ec.etrustex.services.IPartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PartyAgreementSearchValidator implements Validator {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPartyService partyService;
    @Autowired
    private IPartyAgreementService partyAgreementService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PartyAgreementSearchForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PartyAgreementSearchForm form = (PartyAgreementSearchForm) target;
        String userRole = userInfo.getRole().getCode();

        // UC160_BR09	At least one search field should be filled in.
        if (form.getAuthorizingParty_search().getId() == null
                && form.getDelegateParty_search().getId() == null
                && form.getTransaction_search().getId() == null) {
            errors.rejectValue("authorizingParty_search.id", "error.search.criteria.needed");
        }
    }
}