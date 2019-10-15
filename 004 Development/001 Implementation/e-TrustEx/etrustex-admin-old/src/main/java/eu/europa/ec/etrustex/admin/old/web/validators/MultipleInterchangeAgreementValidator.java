/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.InterchangeAgreementMultiForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.utils.FormUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.ICAUtil;
import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import eu.europa.ec.etrustex.services.IPartyService;
import org.apache.commons.collections4.CollectionUtils;
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

import java.util.*;

/**
 * @author batrian
 */
@Component
public class MultipleInterchangeAgreementValidator implements Validator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private IPartyService partyService;
    @Autowired
    private IPartyAgreementService partyAgreementService;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private ICAUtil icaUtil;


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

        icaUtil.validateProfile(icaMultiForm.getProfile(), userInfo, errors);
        icaUtil.validateFirstParty(userInfo, icaMultiForm.getFirstParty(), icaMultiForm.getFirst3rdParty(), errors);


        validateSecondParties(icaMultiForm, userInfo, errors);

        icaUtil.validateStartDate(icaMultiForm.getIcaValidityStartDate(), null, errors);
    }


    /**
     * Validates the second party fields used for batch interchange agreement creation:
     * - second party role is mandatory
     * - at least one second party must be specified.
     * - the chosen parties must have either credentials either delegated parties within the same business domain
     * - the two roles involved in the interchange agreement must be different
     * - all second parties specified should be different than the first party
     * - all parties and third parties must pertain to the same business domain
     */
    private void validateSecondParties(InterchangeAgreementMultiForm form, SessionUserInformation userInfo, Errors errors) {
        Long firstPartyId = form.getFirstParty().getId();
        Long firstPartyRoleId = form.getFirstParty().getRoleId();
        Long profileId = form.getProfile().getId();
        Long secondPartyRoleId = form.getSecondPartyRoleId();

        Collection<PartyListItemDTO> secondParties = form.getPartyListItems();
        Collection<PartyListItemDTO> existingSecondParties = interchangeAgreementService.getPartiesInExistingAgreement(firstPartyId, firstPartyRoleId, profileId, secondPartyRoleId);

        // Remove existing second parties. They don't need validation
        secondParties.removeAll(existingSecondParties);

        if (CollectionUtils.isEmpty(secondParties)) {
            errors.rejectValue("partyListItems", "error.ica.secondParty.name.mandatory");
        }

        if (FormUtil.convertDefaultOptionToNull(secondPartyRoleId) == null) {
            errors.rejectValue("secondPartyRoleId", "error.ica.secondParty.role.mandatory");
        } else {
            icaUtil.validateRoles(firstPartyRoleId, secondPartyRoleId, profileId, errors);
        }

        Party firstParty = partyService.getParty(firstPartyId);
        Long firstPartyBDid = firstParty.getBusinessDomain().getId();

        for (PartyListItemDTO partyItem : secondParties) {
            Long secondPartyId = partyItem.getId();
            Party secondParty = partyService.getParty(secondPartyId);
            StringBuilder msg = new StringBuilder();
            List<Party> delegatedParties = partyService.getAllThirdPartiesFor(secondParty.getId());

            if (secondParty.getBusinessDomain() == null || !Objects.equals(secondParty.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                msg.append(messageSource.getMessage("error.ica.party.notAuthorized", null, LocaleContextHolder.getLocale())).append("\n");
            }

            if (secondParty.getCredentials() == null && CollectionUtils.isEmpty(delegatedParties)) {
                msg.append(messageSource.getMessage("error.ica.party.this.notConnected", null, LocaleContextHolder.getLocale())).append("\n");
            }

            if (secondPartyId != null && firstPartyId.longValue() == secondPartyId.longValue()) {
                msg.append(messageSource.getMessage("error.ica.parties.mustBeDifferent", null, LocaleContextHolder.getLocale())).append("\n");
            }

            if (firstPartyBDid.longValue() != secondParty.getBusinessDomain().getId().longValue()) {
                msg.append(messageSource.getMessage("error.ica.parties.domain.not.identical", null, LocaleContextHolder.getLocale())).append("\n");
            } else if (secondParty.getCredentials() == null) {
                for (Party thirdParty : partyService.getAllThirdPartiesFor(secondPartyId)) {
                    if (thirdParty.getBusinessDomain().getId().longValue() != firstPartyBDid.longValue()) {
                        msg.append(messageSource.getMessage("error.ica.parties.domain.not.identical", null, LocaleContextHolder.getLocale())).append("\n");
                        break;
                    }
                }
            }

            if (StringUtils.isNotBlank(msg.toString())) {
                partyItem.setMessage(msg.toString());
                partyItem.setValid(false);
            } else {
                partyItem.setValid(true);
            }

            // popuplate PartyListItemDTO
            partyItem.setName(secondParty.getName());
            List<String> identifiersList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(secondParty.getIdentifiers())) {
                for (PartyIdentifier partyIdentifier : secondParty.getIdentifiers()) {
                    identifiersList.add(partyIdentifier.getSchemeId() + " " + partyIdentifier.getValue());
                }

                Collections.sort(identifiersList);
            }
            partyItem.setIdentifiers(identifiersList);
            partyItem.setThirdParties(partyAgreementService.getDelegateThirdPartiesNamesFor(secondParty.getId()));
        }

        // Put back exisiting second parties in collection
        secondParties.addAll(existingSecondParties);
    }
}
