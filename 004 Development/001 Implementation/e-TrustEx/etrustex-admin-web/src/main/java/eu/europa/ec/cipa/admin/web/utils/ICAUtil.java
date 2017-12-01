package eu.europa.ec.cipa.admin.web.utils;

import eu.europa.ec.cipa.admin.web.dto.PartyForm;
import eu.europa.ec.cipa.admin.web.dto.ProfileElement;
import eu.europa.ec.cipa.admin.web.dto.SelectElement;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.admin.User;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.IProfileService;
import eu.europa.ec.cipa.etrustex.services.IRoleService;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.*;

/**
 * Created by guerrpa on 03/03/2016.
 */
@Component
public class ICAUtil {
    @Autowired private MessageSource messageSource;
    @Autowired private IPartyService partyService;
    @Autowired private IRoleService roleService;
    @Autowired private IProfileService profileService;

    public boolean hasAccessRights(SessionUserInformation userInfo, InterchangeAgreement interchangeAgreement, User creationUser) {
        String userRole = userInfo.getRole().getCode();
        Long businessDomainId = interchangeAgreement.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId();

		/*
		 * UC20_BR31
		 * A user having LBO role can modify only interchange agreements pertaining to his/her business domain and that
		 * have been created by himself/herself or another LBO acting in behalf of the same party as the user.
		 *
		 * UC20_BR34
		 * A user having LBO role can delete only interchange agreements pertaining to his/her business domain and that
		 * have been created by himself/herself or another LBO acting in behalf of the same party as the user.
		 *
		 * UC20_BR32
		 * A user having CBO role can modify only interchange agreements pertaining to his/her business domain.
		 *
		 * UC20_BR35
		 * A user having CBO role can delete only interchange agreements pertaining to his/her business domain
		 *
		 *
		 * 	Note: The business domain of an Interchange Agreement is determined by the business domain of any of the parties involved.
		 */

        // Actually no one can modify/delete an ICA that does not pertain to his BD
        if( !userInfo.getBusinessDomain().getId().equals(businessDomainId) ) {
            return false;
        }

        if ( StringUtils.equalsIgnoreCase(userRole, UserRoleEnum.LBO.name()) ) {
            if(creationUser == null) {
                return false;
            }

            boolean createdByUser = StringUtils.equalsIgnoreCase(userInfo.getUsername(), creationUser.getName());
            boolean createdByLBOOnBehalf = PartyUtil.isCreatedByLBOOnBehalf(creationUser, userInfo, businessDomainId);

            if ( !(createdByUser || createdByLBOOnBehalf) ) {
                return false;
            }
        }

        return true;
    }

    /*
     * Interchange Agreement Profile is mandatory and user must be
     * authorized to use it (the profile must pertain to the current
     * selected business domain)
     */
    public void validateProfile(ProfileElement profileEl, BusinessDomain selectedBusinessDomain, SessionUserInformation userInfo, Errors errors){
        if (profileEl == null || FormUtil.convertDefaultOptionToNull(profileEl.getId()) == null){
            errors.rejectValue("profile.id", "error.ica.profile.mandatory");
        } else { // @batrian adapted for shared profiles
			/*
			Profile profile = profileService.getProfile(profileEl.getId());
			if (profile.getBusinessDomain() == null || profile.getBusinessDomain().getId() != userInfo.getBusinessDomain().getId()){
				errors.rejectValue("profile.id", "error.ica.profile.notAuthorized");
			}*/

            if (userInfo.getBusinessDomain() == null) {
                throw new NullPointerException("No business domain selected for user " + userInfo.getUsername());
            }

            if (selectedBusinessDomain == null){
                throw new NullPointerException("The selected business domain with id " + userInfo.getBusinessDomain().getId() + " could not be found");
            }

            Set<Profile> profiles = selectedBusinessDomain.getProfiles();

            if (CollectionUtils.isEmpty(profiles)){
                errors.rejectValue("profile.id", "error.ica.profile.notAuthorized");
                return;
            }

            boolean isValid = false;
            for (Profile profile : profiles) {
                if (profileEl.getId().longValue() == profile.getId().longValue()){
                    isValid = true;
                    break;
                }
            }

            if (!isValid){
                errors.rejectValue("profile.id", "error.ica.profile.notAuthorized");
            }
        }
    }

    /*
	 * The first party is mandatory and must pertain to the current selected business domain.
	 * Parties without credentials cannot connect if no third party is provided.
	 * If third party provided, it has to pertain to the current selected business domain.
	 * Party role is mandatory.
	 */
    public void validateFirstParty(SessionUserInformation userInfo, PartyForm firstParty, PartyForm first3rdParty, Errors errors){
        if (firstParty == null) {
            errors.rejectValue("firstParty", "error.ica.firstParty.name.mandatory");
            return;
        }

        Long firstPartyId = firstParty.getId();
        Long via3rdPartyId = first3rdParty == null ? null : first3rdParty.getId();

        if (firstPartyId == null) {
            errors.rejectValue("firstParty", "error.ica.firstParty.name.mandatory");
        } else {
            Party party = partyService.getParty(firstPartyId);

            if (party.getBusinessDomain() == null || !Objects.equals(party.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                errors.rejectValue("firstParty", "error.ica.party.notAuthorized", new Object[] {party.getName(), userInfo.getBusinessDomain().getName()}, "First Party not authorized");
            }

            if (via3rdPartyId != null) {
                Party thirdParty = partyService.getParty(via3rdPartyId);

                if (thirdParty.getBusinessDomain() == null || !Objects.equals(thirdParty.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                    errors.rejectValue("firstParty", "error.ica.party.notAuthorized", new Object[] {party.getName(), userInfo.getBusinessDomain().getName()}, "First Party not authorized");
                }
            }

            List<Party> delegatedParties = partyService.getAllThirdPartiesFor(firstPartyId);
            if (party.getCredentials() == null
                    && via3rdPartyId == null
                    && CollectionUtils.isEmpty(delegatedParties)) {
                errors.rejectValue("firstParty", "error.ica.party.notConnected", new Object[] {party.getName()}, "First Party has no credentials");
            }
        }
    }

    /**
     *
     */
    public void validateRoles(Long firstPartyRoleId, Long secondPartyRoleId, Long profileId, Errors errors) {
        firstPartyRoleId = FormUtil.convertDefaultOptionToNull(firstPartyRoleId);
        secondPartyRoleId = FormUtil.convertDefaultOptionToNull(secondPartyRoleId);

        if ( firstPartyRoleId == null) {
            errors.rejectValue("firstParty", "error.ica.firstParty.role.mandatory");
        }

        if (secondPartyRoleId == null) {
            errors.rejectValue("secondParty", "error.ica.secondParty.role.mandatory");
        }

        if(errors.hasErrors()) {
            return;
        } else if (firstPartyRoleId.longValue() == secondPartyRoleId.longValue()) {
            Role role = roleService.getRole(firstPartyRoleId);
            if (!role.getBidirectionalFlag()) {
                errors.reject("error.ica.parties.roles.mustBeDifferent");
            }
        } else {
            /*
             * UC20_BR39	The combination of roles assigned to the participating parties must match an existing combination of roles as defined in the profile transactions.
             */
            boolean existsCombinationOfRoles = false;
            for (Transaction tx : profileService.getProfile(profileId).getTransactions()) {
                Long senderRoleId = tx.getSenderRole().getId();
                Long receiverRoleId = tx.getReceiverRole().getId();
                if ( (Objects.equals(firstPartyRoleId, senderRoleId) && Objects.equals(secondPartyRoleId, receiverRoleId))
                        || (Objects.equals(firstPartyRoleId, receiverRoleId) && Objects.equals(secondPartyRoleId, senderRoleId))) {
                    existsCombinationOfRoles = true;
                    break;
                }
            }

            if (!existsCombinationOfRoles) {
                errors.reject("error.ica.parties.roles.profile.transactions");
            }
        }
    }

    /**
     * Validates the start date of an interchange agreement:
     * - cannot be in the past.
     */
    public void validateStartDate(Date date, Errors errors){
        if (date != null){
            Calendar today = Calendar.getInstance();
            Calendar startDay = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            startDay.setTime(date);

            if(startDay.before(today)){
                errors.rejectValue("icaValidityStartDate", "error.ica.validFrom.todayOrFutureDate");
            }
        }
    }

    /*
	 * The first party is mandatory and must pertain to the current selected business domain.
	 * LBO must be authorised to use the party.
	 * Parties without credentials cannot connect if no third party is provided.
	 * If third party provided, it has to pertain to the current selected business domain.
	 * Party role is mandatory.
	 */
    public void validateSecondParty(SessionUserInformation userInfo, PartyForm secondParty, PartyForm second3rdParty, Errors errors) {
        if (secondParty == null) {
            errors.rejectValue("secondParty", "error.ica.secondParty.name.mandatory");
            return;
        }

        Long secondPartyId = secondParty.getId();
        Long via3rdPartyId = second3rdParty == null ? null : second3rdParty.getId();


        if (secondPartyId == null) {
            errors.rejectValue("secondParty", "error.ica.secondParty.name.mandatory");
        } else {
            // LBO must be authorised to use the chosen second party
            if (StringUtils.equalsIgnoreCase(userInfo.getRole().getCode(), UserRoleEnum.LBO.name())){
                List<PartyListItemDTO> userParties = userInfo.getParties();
                boolean isAuthorized = false;
                for (PartyListItemDTO partyElement : userParties) {
                    if (Objects.equals(partyElement.getId(), secondPartyId)){
                        isAuthorized = true;
                    }
                }
                if ( !isAuthorized ){
                    errors.rejectValue("secondParty", "error.ica.lbo.party.notAuthorized", new Object[] {secondParty.getPartyName(), userInfo.getBusinessDomain().getName()}, "Not authorized");
                }
            }

            // Party must pertain to the current selected business domain
            Party party = partyService.getParty(secondPartyId);

            if (party.getBusinessDomain() == null && !Objects.equals(party.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                errors.rejectValue("secondParty", "error.ica.party.notAuthorized",  new Object[] {party.getName(), userInfo.getBusinessDomain().getName()}, "Not authorized");
            }

            // If third party provided, it has to pertain to the current selected business domain
            if (via3rdPartyId != null) {
                Party thirdParty = partyService.getParty(via3rdPartyId);

                if (thirdParty.getBusinessDomain() == null || !Objects.equals(thirdParty.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                    errors.rejectValue("secondParty", "error.ica.party.notAuthorized", new Object[] {thirdParty.getName(), userInfo.getBusinessDomain().getName()}, "Not authorized");
                }
            }

            // parties without credentials cannot connect if no third party is provided
            List<Party> delegatedParties = partyService.getAllThirdPartiesFor(secondPartyId);
            if (party.getCredentials() == null
                    && via3rdPartyId == null
                    && CollectionUtils.isEmpty(delegatedParties)){
                errors.rejectValue("secondParty", "error.ica.party.notConnected", new Object[] {party.getName()}, "Second Party has no credentials");
            }
        }
    }

    /**
     * Validates extra business rules an interchange agreement creation:
     *  - the two parties involved in the interchange agreement must be different
     *  - the parties used in the interchange agreement and the delegated parties must pertain
     *  to the same business domain
     *
     */
    public void validateExtraBusiness(Long firstPartyId, Long secondPartyId, Long first3rdPartyId, Long second3rdPartyId, Errors errors) {

        if (firstPartyId != null && secondPartyId != null) {
            if (firstPartyId.longValue() == secondPartyId.longValue()) {
                errors.reject("error.ica.parties.mustBeDifferent");
            }

            Party first = partyService.getParty(firstPartyId);
            Party second = partyService.getParty(secondPartyId);
            Party firstThirdParty = first3rdPartyId == null ? null : partyService.getParty(first3rdPartyId);
            Party secondThirdParty = second3rdPartyId == null ? null : partyService.getParty(second3rdPartyId);

            if (!Objects.equals(first.getBusinessDomain().getId(), second.getBusinessDomain().getId())
                    || (firstThirdParty != null && !Objects.equals(firstThirdParty.getBusinessDomain().getId(), first.getBusinessDomain().getId()))
                    || (secondThirdParty != null && !Objects.equals(secondThirdParty.getBusinessDomain().getId(), first.getBusinessDomain().getId()))) {
                errors.reject("error.ica.parties.domain.not.identical");
            }
        }
    }


    public List<SelectElement> getRolesForProfile(Long profileId, String userRoleCode) {
        List<SelectElement> roles = new ArrayList<>();

        if (FormUtil.convertDefaultOptionToNull(profileId) != null ){
            boolean technicalRoleFlag = false;
            if (StringUtils.equalsIgnoreCase(userRoleCode, UserRoleEnum.ADM.name())){
                technicalRoleFlag = true;
            }

            for(Role role : roleService.getRolesForProfile(profileId, technicalRoleFlag)) {
                roles.add(new SelectElement(role.getId(), role.getName()));
            }
        }

        return roles;
    }
}
