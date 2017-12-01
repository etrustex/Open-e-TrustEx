/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.InterchangeAgreementMultiForm;
import eu.europa.ec.cipa.admin.web.dto.PartyListItem;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.utils.FormUtil;
import eu.europa.ec.cipa.admin.web.utils.ICAUtil;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Objects;

/**
 * @author batrian
 *
 */
@Component
public class MultipleInterchangeAgreementValidator implements Validator {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private MessageSource messageSource;
    @Autowired private IPartyService partyService;
	@Autowired private IBusinessDomainService businessDomainService;
    @Autowired private ICAUtil icaUtil;


	@Override
	public boolean supports(Class<?> clazz) {
		return InterchangeAgreementMultiForm.class.equals(clazz);
	}


	@Override
	public void validate(Object target, Errors errors) {
		logger.debug("validating multiple interchange agreement data");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SessionUserInformation userInfo = (SessionUserInformation) auth.getPrincipal();		
		InterchangeAgreementMultiForm icaMultiForm = (InterchangeAgreementMultiForm) target;
		
		icaUtil.validateProfile(icaMultiForm.getProfile(), businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()), userInfo, errors);
        icaUtil.validateFirstParty(userInfo, icaMultiForm.getFirstParty(), icaMultiForm.getFirst3rdParty(), errors);

        validateSecondParties(icaMultiForm.getFirstParty().getId(),
                icaMultiForm.getFirstParty().getRoleId(), icaMultiForm.getSecondPartyRoleId(),
                icaMultiForm.getProfile().getId(), icaMultiForm.getPartyListItems(), userInfo, errors);

        icaUtil.validateStartDate(icaMultiForm.getIcaValidityStartDate(), errors);
	}


    /**
     * Validates the second party fields used for batch interchange agreement creation:
     *  - second party role is mandatory
     *  - at least one second party must be specified.
     *  - the chosen parties must have either credentials either delegated parties within the same business domain
     *  - the two roles involved in the interchange agreement must be different
     *  - all second parties specified should be different than the first party
     *  - all parties and third parties must pertain to the same business domain
     */
    private void validateSecondParties(Long firstPartyId, Long firstPartyRoleId, Long secondPartyRoleId, Long profileId, List<PartyListItem> secondParties, SessionUserInformation userInfo, Errors errors){
        if (secondParties == null || CollectionUtils.isEmpty(secondParties)) {
            errors.rejectValue("partyListItems", "error.ica.secondParty.name.mandatory");
            return;
        }

        if (FormUtil.convertDefaultOptionToNull(secondPartyRoleId) == null){
            errors.rejectValue("secondPartyRoleId", "error.ica.secondParty.role.mandatory");
        } else {
            icaUtil.validateRoles(firstPartyRoleId, secondPartyRoleId, profileId, errors);
        }

        Party firstParty = partyService.getParty(firstPartyId);
        Long firstPartyBDid = firstParty.getBusinessDomain().getId();

        for (PartyListItem partyItem : secondParties) {
            Long secondPartyId = partyItem.getId();
            Party secondParty = partyService.getParty(secondPartyId);
            StringBuilder msg = new StringBuilder();
            List<Party> delegatedParties = partyService.getAllThirdPartiesFor(secondParty.getId());

            if (secondParty.getBusinessDomain() == null || !Objects.equals(secondParty.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                msg.append(messageSource.getMessage("error.ica.party.notAuthorized", null, LocaleContextHolder.getLocale())).append("\n");
            }

            if (secondParty.getCredentials() == null && CollectionUtils.isEmpty(delegatedParties)){
                msg.append(messageSource.getMessage("error.ica.party.this.notConnected", null, LocaleContextHolder.getLocale())).append("\n");
            }

            if (secondPartyId != null && firstPartyId.longValue() == secondPartyId.longValue()) {
                msg.append(messageSource.getMessage("error.ica.parties.mustBeDifferent", null, LocaleContextHolder.getLocale())).append("\n");
            }

            if (firstPartyBDid.longValue() != secondParty.getBusinessDomain().getId().longValue()){
                msg.append(messageSource.getMessage("error.ica.parties.domain.not.identical", null, LocaleContextHolder.getLocale())).append("\n");
            } else if (secondParty.getCredentials() == null){
                for (Party thirdParty : partyService.getAllThirdPartiesFor(secondPartyId)) {
                    if (thirdParty.getBusinessDomain().getId().longValue() != firstPartyBDid.longValue()){
                        msg.append(messageSource.getMessage("error.ica.parties.domain.not.identical", null, LocaleContextHolder.getLocale())).append("\n");
                        break;
                    }
                }
            }

            PartyListItem pli = new PartyListItem(secondParty, partyService.getAllThirdPartiesFor(secondPartyId));
            partyItem.setPartyName(pli.getPartyName());
            partyItem.setPartyCredentials(pli.getPartyCredentials());
            partyItem.setPartyIdentifiers(pli.getPartyIdentifiers());
            partyItem.setThirdParties(pli.getThirdParties());

            if(StringUtils.isNotBlank(msg.toString())) {
                partyItem.setMessage(msg.toString());
                partyItem.setStatusColor("red");
                partyItem.setIsValid(false);
            }
        }
    }
}
