/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.BusinessDomainElement;
import eu.europa.ec.cipa.admin.web.dto.MessageSearchForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.utils.FormUtil;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageSearchValidator implements Validator {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private @Autowired ValidationHelper validationHelper;
    private @Autowired IPartyService partyService;

	@Override
	public boolean supports(Class<?> clazz) {
		return MessageSearchForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MessageSearchForm form = (MessageSearchForm) target;

        // UC140_BR11	At least one search field should be filled in.
        if(FormUtil.convertDefaultOptionToNull(form.getBusinessDomain().getId()) == null
                && StringUtils.isBlank(form.getCreationId())
                && form.getCreationDateFrom() == null
                && form.getCreationDateTo() == null
                && StringUtils.isBlank(form.getCorrelationId())
                && StringUtils.isBlank(form.getDocumentId())
                && StringUtils.isBlank(form.getMessageDocumentTypeCode())
                && form.getRetrieveIndicator() == null
                && form.getDispatched() == null
                && form.getIssuer().getId() == null
                && form.getSender().getId() == null
                && form.getReceiver().getId() == null
                && form.getAgreement().getId() == null
                && form.getTransaction().getId() == null
                && StringUtils.isBlank(form.getStatusCode())) {
            errors.rejectValue("businessDomain.id", "error.search.criteria.needed");
        }


        //UC140_BR04	Creation period: from date must be less than or equal to to date.
        validationHelper.validateDateOrder("creationDateFrom", form.getCreationDateFrom(), "creationDateTo", form.getCreationDateTo(), errors);


        /*
        * UC140_BR05	Issuer, Sender and Receiver, when specified:
        *    •	need to pertain to the business domain criteria selected
        *    •	need to pertain to one of the business domains user is authorised to use, if All option is selected in the business domain criteria
        */
        List<Long> businessDomainIds = new ArrayList<>();
        if(form.getBusinessDomain().getId().equals(new Long("-1"))) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            SessionUserInformation userInfo = (SessionUserInformation) auth.getPrincipal();
            for (BusinessDomainElement bd :  userInfo.getBusinessDomains()) {
                businessDomainIds.add(bd.getId());
            }
        } else {
            businessDomainIds.add(form.getBusinessDomain().getId());
        }

        validateParty(form.getIssuer(), "issuer", businessDomainIds, errors);
        validateParty(form.getIssuer(), "sender", businessDomainIds, errors);
        validateParty(form.getIssuer(), "receiver", businessDomainIds, errors);

	}

    private void validateParty(Party party, String wwhich, List<Long> businessDomainIds, Errors errors) {
        if (party != null && party.getId() != null) {
            Party issuer = partyService.getParty(party.getId());

            if(!businessDomainIds.contains(issuer.getBusinessDomain().getId())) {
                errors.rejectValue("displayIssuerName", "error.message." + wwhich + ".bd");
            }
        }
    }
}
