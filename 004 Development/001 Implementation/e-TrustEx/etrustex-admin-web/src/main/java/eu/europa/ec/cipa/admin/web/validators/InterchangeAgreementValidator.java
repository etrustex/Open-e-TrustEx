/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.InterchangeAgreementForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.utils.ICAUtil;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.IProfileService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class InterchangeAgreementValidator implements Validator {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired private IInterchangeAgreementService interchangeAgreementService;
	@Autowired private IProfileService profileService;
	@Autowired private IPartyService partyService;
	@Autowired private IUserService userService;
	@Autowired private IBusinessDomainService businessDomainService;
    @Autowired private ICAUtil icaUtil;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return InterchangeAgreementForm.class.equals(clazz) ; // || InterchangeAgreementMultiForm.class.equals(clazz)
	}

	@Override
	public void validate(Object target, Errors errors) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SessionUserInformation userInfo = (SessionUserInformation) auth.getPrincipal();
		InterchangeAgreementForm ica = (InterchangeAgreementForm) target;
        Long icaId = ica.getId();

        if (icaId != null) { // update
            InterchangeAgreement interchangeAgreement = interchangeAgreementService.getInterchangeArgreement(icaId);

            if (!icaUtil.hasAccessRights(userInfo, interchangeAgreement, userService.getUser(interchangeAgreement.getAccessInfo().getCreationId()))) {
                errors.reject("common.error.delete.modify.noRights");

                return;
            }
        } else {
            icaUtil.validateStartDate(ica.getIcaValidityStartDate(), errors);
            icaUtil.validateProfile(ica.getProfile(), businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()), userInfo, errors);
            icaUtil.validateFirstParty(userInfo, ica.getFirstParty(), ica.getFirst3rdParty(), errors);
            icaUtil.validateSecondParty(userInfo, ica.getSecondParty(), ica.getSecond3rdParty(), errors);
            icaUtil.validateRoles(ica.getFirstParty().getRoleId(), ica.getSecondParty().getRoleId(), ica.getProfile().getId(), errors);
        }


        if(!errors.hasErrors() && icaId == null) {
            // validate unicity
            if (!interchangeAgreementService.isUnique(ica.getProfile().getId(),
                    ica.getFirstParty().getId(), ica.getFirstParty().getRoleId(),
                    ica.getSecondParty().getId(), ica.getSecondParty().getRoleId())){
                errors.reject("error.ica.notUnique");
            }

            Long firstPartyId = ica.getFirstParty().getId();
            Long secondPartyId = ica.getSecondParty().getId();
            Long first3rdPartyId = ica.getFirst3rdParty() == null ? null : ica.getFirst3rdParty().getId();
            Long second3rdPartyId = ica.getSecond3rdParty() == null ? null : ica.getSecond3rdParty().getId();

            icaUtil.validateExtraBusiness(firstPartyId, secondPartyId, first3rdPartyId, second3rdPartyId, errors);
        }
    }

}