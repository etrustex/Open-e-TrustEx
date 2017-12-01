/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.MetadataForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.IProfileService;
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
	private Logger log = LoggerFactory.getLogger(this.getClass());

	// Form field names
	private static final String METADATAITEMTYPE = "metaDataItemType";
	private static final String PROFILE = "profile";
	private static final String INTERCHANGEAGREEMENT = "interchangeAgreement";
	
	private final static String EMPTY_METADATAITEMTYPE = "error.metadata.metadataitemtype.empty";
	
	@Autowired ValidationHelper validationHelper;
	@Autowired private IBusinessDomainService businessDomainService;
	@Autowired private IProfileService profileService;	
	@Autowired private IInterchangeAgreementService interchangeAgreementService;
	
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
		
		if(form.getProfile() != null && form.getProfile().getId().longValue() != -1) {
			validateProfile(userInfo, form.getProfile(), errors);
		}
		
		if(form.getInterchangeAgreement() != null && form.getInterchangeAgreement().getId() != null) {
			validateIca(userInfo, interchangeAgreementService.getInterchangeArgreement(form.getInterchangeAgreement().getId()), errors);
		}
	}

	private void validateProfile(SessionUserInformation userInfo, Profile profile, Errors errors){
		BusinessDomain currentBusinessDomain = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId());
		Set<Profile> profiles = currentBusinessDomain.getProfiles();

		boolean isValid = false;
		for (Profile p : profiles) {
			if (p.getId().equals(profile.getId())){
				isValid = true;
				break;
			}
		}
		
		if (!isValid){
			errors.rejectValue(PROFILE, "error.metadata.profile.notAuthorized");
		}
	}
	
	private void validateIca(SessionUserInformation userInfo, InterchangeAgreement ica, Errors errors){
		BusinessDomain currentBusinessDomain = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId());
		boolean isValid = false;
		
		for (PartyRole pr : ica.getPartyRoles()) {
			if(pr.getParty().getBusinessDomain().getId().equals(currentBusinessDomain.getId())) {
				isValid = true;
				break;
			}
		}
		
		if (!isValid){
			errors.rejectValue(PROFILE, "error.metadata.ica.notAuthorized");
		}
	}	
}