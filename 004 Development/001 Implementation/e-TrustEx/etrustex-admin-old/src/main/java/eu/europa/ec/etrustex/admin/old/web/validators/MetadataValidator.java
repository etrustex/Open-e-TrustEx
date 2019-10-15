/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.MetadataForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.services.IProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Set;

@Component
public class MetadataValidator implements Validator {
    // Form field names
    private static final String METADATAITEMTYPE = "metaDataItemType";
    private static final String PROFILE = "profile";
    private static final String INTERCHANGEAGREEMENT = "interchangeAgreement";
    private static final String PARTY = "party";
    private final static String EMPTY_METADATAITEMTYPE = "error.metadata.metadataitemtype.empty";
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private IProfileService profileService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IPartyService partyService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MetadataForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MetadataForm form = (MetadataForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, METADATAITEMTYPE, EMPTY_METADATAITEMTYPE);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SessionUserInformation userInfo = (SessionUserInformation) auth.getPrincipal();
        BusinessDomain currentBusinessDomain = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId());

        // UC120_BR05	If Profile is specified, it shall pertain to the current business domain.
        if (form.getProfile() != null && form.getProfile().getId().longValue() != -1) {
            validateProfile(currentBusinessDomain, form.getProfile(), errors);
        }

		/*
		 * UC120_BR06 If Interchange Agreement is specified, it shall pertain to the current business domain.
            Note: The business domain of an interchange agreement can be determined by looking at the business domain of one of the parties involved in the interchange agreement.
		 */
        if (form.getInterchangeAgreement() != null && form.getInterchangeAgreement().getId() != null) {
            validateIca(currentBusinessDomain, interchangeAgreementService.getInterchangeArgreement(form.getInterchangeAgreement().getId()), errors);
        }

        // UC120_BR10 If Party is specified, it shall pertain to the current business domain.
        if (form.getParty() != null && form.getParty().getId() != null) {
            Party party = partyService.getParty(form.getParty().getId());
            if (!party.getBusinessDomain().getId().equals(currentBusinessDomain.getId())) {
                errors.rejectValue(PARTY, "error.metadata.party.notAuthorized");
            }
        }

    }

    private void validateProfile(BusinessDomain currentBusinessDomain, Profile profile, Errors errors) {
        Set<Profile> profiles = currentBusinessDomain.getProfiles();

        boolean isValid = false;
        for (Profile p : profiles) {
            if (p.getId().equals(profile.getId())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            errors.rejectValue(PROFILE, "error.metadata.profile.notAuthorized");
        }
    }

    private void validateIca(BusinessDomain currentBusinessDomain, InterchangeAgreement ica, Errors errors) {
        boolean isValid = false;

        for (PartyRole pr : ica.getPartyRoles()) {
            if (pr.getParty().getBusinessDomain().getId().equals(currentBusinessDomain.getId())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            errors.rejectValue(INTERCHANGEAGREEMENT, "error.metadata.ica.notAuthorized");
        }
    }
}