/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.CredentialsDTO;
import eu.europa.ec.etrustex.admin.old.web.dto.PartyIdentifierDTO;
import eu.europa.ec.etrustex.admin.old.web.dto.PartyView;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.utils.UserRoleEnum;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.services.ICredentialsService;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import eu.europa.ec.etrustex.services.IPartyIdentifierService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author batrian
 */
@Component
public class PartyValidator implements Validator {
    private final static String NAME = "name";
    private final static String USERNAME = "credentials.user";
    private final static String PASSWORD = "credentials.password";
    private final static String CONFIRM_PASSWORD = "credentials.password2";
    private final static String IDENTIFIER_VALUE = "newIdentifierValue";
    private final static String IDENTIFIER_TYPE = "newIdentifierType";


    @Autowired
    private IPartyService partyService;
    @Autowired
    private ICredentialsService credentialsService;
    @Autowired
    private IPartyIdentifierService partyIdentifierService;
    @Autowired
    private IPartyAgreementService partyAgreementService;
//	@Autowired private CertificateValidator certificateValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return PartyView.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PartyView partyView = (PartyView) target;
        Long id = partyView.getId();


        boolean isNewParty = id == null ? true : false;

        // UC10_BR01 "Party Name" is mandatory.
        if (StringUtils.isBlank(partyView.getName())) {
            errors.rejectValue(NAME, "error.party.name.mandatory", "Name is mandatory");
        } else {
            if (partyView.getName().trim().length() > ValidationHelper.MAX_LENGHT) {
                errors.rejectValue(NAME, "error.party.name.tooLong", "Name is too long");
            } else {
                if (partyView.getName().trim().length() < ValidationHelper.MIN_LENGHT) {
                    errors.rejectValue(NAME, "error.party.name.tooShort", "Name is too short");
                }
            }
        }

        // UC10_BR02Party name must be unique per business domain (to consider the application business domain)
        if (isNewParty) {
            if (!partyService.isUniquePartyNamePerDomain(partyView.getName(), userInfo.getBusinessDomain().getId())) {
                errors.rejectValue(NAME, "error.party.exists");
            }
        } else {
            /*
             * UC10_BR31	Changing "Is 3rd Party" value from "Yes" to "No", will not be allowed if there are parties that use the current party as a third party.
             * A message will inform the user that "There are parties using this service provider, there for you cannot remove the 3rd party status."
             */
            Party existingParty = partyService.getParty(id);
            if (existingParty.getThirdPartyFlag() != null && existingParty.getThirdPartyFlag() && !partyView.getIsThirdParty() && partyAgreementService.existsAgreementForParty(partyService.getParty(id))) {
                errors.reject("error.party.thidParty.in.use");
            }
        }

        validateCredentials(partyView, errors, userInfo, isNewParty);

        validateIdentifiers(partyView, errors, userInfo);
    }


    private void validateCredentials(PartyView partyView, Errors errors, SessionUserInformation userInfo, boolean isNewParty) {
        CredentialsDTO credentials = partyView.getCredentials();
        String userRole = userInfo.getRole().getCode();
        /*
         * UC10_BR05	"Username", "password" and "password confirmation" are mandatory if "Is 3rd Party" is set to "Yes" (for users having ADM or CBO role).
         * "Username", "password" and "password confirmation" are optional for users having LBO role, case in which a warning is displayed in the credentials
         * section: "Please be aware that a party without credentials will have to connect to the System via a Service Provider / Third Party."
         */
        if (!userRole.equals(UserRoleEnum.LBO.name()) && partyView.getIsThirdParty() != null && partyView.getIsThirdParty()) {
            if (credentials == null) {
                errors.reject("error.party.credentials.mandatory");
            } else if (isNewParty || partyView.getChangePasswordFlag()) {
                if (StringUtils.isBlank(credentials.getUser()) || StringUtils.isBlank(credentials.getPassword())) {
                    errors.reject("error.party.credentials.mandatory");
                } else {
                    if (!StringUtils.equals(credentials.getPassword(), credentials.getPassword2())) {
                        errors.rejectValue(CONFIRM_PASSWORD, "error.party.credentials.password.mismatch");
                    }
                }
            }
        }

        //for any user role, username and password fields are mandatory if either of them is present
        if (credentials != null) {
            /*
             * TODO
             * UC10_BR06	The required signature and password encrypted flags will be set to false and respectively true by default if user fills in the credential fields.
             * These fields are only visible and modifiable for users having ADM role.
             */

            /*
             * UC10_BR09	The "Password" and "Confirm password" fields must contain the same value. If not, user is notified about the mismatching passwords.
             */
            if (StringUtils.isNotBlank(credentials.getPassword())) {
                if (!StringUtils.equals(credentials.getPassword(), credentials.getPassword2())) {
                    errors.reject("error.party.credentials.password.mismatch");
                }
            }

            if (StringUtils.isNotBlank(credentials.getUser())) {
                //credentials username must be unique for new credentials
                if (credentialsService.exists(credentials.toDomain(CredentialsDTO.CredentialsType.PARTY))) {
                    boolean exists = false;

                    if (partyView.getId() == null) {
                        // create
                        exists = true;
                    } else {
                        // update
                        if (credentials.getId() == null) {
                            // the party does not have credentials, new credentials
                            Credentials partyCredentialsExample = new PartyCredentials();
                            partyCredentialsExample.setUser(credentials.getUser());
                            if (credentialsService.findByTypeAndUsername(partyCredentialsExample).size() > 0) {
                                exists = true;
                            }
                        } else {
                            /* Since username cannot be changed, this should not happen */
                            // the party has credentials, check if trying to use another party credentials
                            Credentials crd = credentialsService.get(credentials.getId());
                            if (!crd.getParty().getId().equals(partyView.getId())) {
                                exists = true;
                            }
                        }
                    }

                    if (exists) {
                        errors.rejectValue(NAME, "error.party.credentials.username.exists");
                    }
                }

                // username length
                if (credentials.getUser().trim().length() > ValidationHelper.MAX_LENGHT) {
                    errors.rejectValue(USERNAME, "error.party.credentials.username.tooLong");
                } else {
                    if (credentials.getUser().trim().length() < ValidationHelper.MIN_LENGHT) {
                        errors.rejectValue(USERNAME, "error.party.credentials.username.tooShort");
                    }
                }

                if (StringUtils.isNotEmpty(credentials.getPassword())) {
                    if (credentials.getPassword().length() > ValidationHelper.MAX_LENGHT) {
                        errors.rejectValue(PASSWORD, "error.party.credentials.password.tooLong");
                    } else {
                        if (credentials.getPassword().length() < ValidationHelper.MIN_LENGHT) {
                            errors.rejectValue(PASSWORD, "error.party.credentials.password.tooShort");
                        }
                    }
                } else if (credentials.getId() == null) {
                    errors.rejectValue(PASSWORD, "error.party.credentials.mandatory.both");
                }
            }
        }
    }

    /*
     * UC10_BR07	User can associate one or more identifiers to the party.
        User can add one identifier value per identifier scheme id. E.g.: there will not be two VAT values identifying the same party.
        The identifier must be unique per business domain. E.g.: there shall be no two parties pertaining to same business domain and having the same VAT number.
        In case one of the business domains of the user is eProcurement, a warning is displayed in the identifiers section: "Please be aware that a Party with no LEF
        id cannot be used in an interchange agreement for eProcurement business domain".
     */
    private void validateIdentifiers(PartyView partyView, Errors errors, SessionUserInformation userInfo) {
        List<PartyIdentifierDTO> partyIdentifiers = partyView.getIdentifiers();
        String newIdType = partyView.getNewIdentifierType();
        String newIdValue = partyView.getNewIdentifierValue();

        boolean isEmptyList = !existsActiveIdentifier(partyIdentifiers);
        boolean isNewIdentifierFilledIn = isIdentifierFilledIn(newIdType, newIdValue);

        // UC10_BR08	At least one party identifier must be provided.
        if (isEmptyList && !isNewIdentifierFilledIn) {
            errors.reject("error.party.identifiers.mandatory");
        }

        // new identifier validation TODO: check - improve
        if (isNewIdentifierFilledIn) {
            PartyIdentifier pi = new PartyIdentifier();
            pi.setSchemeId(IdentifierIssuingAgency.valueOf(newIdType));
            pi.setValue(newIdValue.trim());

            // minimum and maximum lenght check
            if (pi.getValue().length() > ValidationHelper.MAX_LENGHT) {
                errors.rejectValue(IDENTIFIER_VALUE, "error.party.identifier.value.tooLong",
                        new Object[]{partyView.getNewIdentifierType()}, "Identifier value too long");
            } else {
                if (pi.getValue().length() < ValidationHelper.MIN_LENGHT) {
                    errors.rejectValue(IDENTIFIER_VALUE, "error.party.identifier.value.tooShort",
                            new Object[]{partyView.getNewIdentifierType()}, "Identifier value too short");
                }
            }

            if (isAlreadyInList(partyIdentifiers, pi)) {
                errors.rejectValue(IDENTIFIER_TYPE, "error.party.identifiers.type.unique");
            } else {
                boolean exists = false;
                if (isInListMarkedForDeletion(partyIdentifiers, pi)) {
                    PartyIdentifier pid = partyIdentifierService.getBySchemeAndValueInBusinessDomain(pi, userInfo.getBusinessDomain().getId());
                    if (pid.getId().longValue() != pi.getId().longValue()) {
                        exists = true;
                    }
                } else {
                    if (partyIdentifierService.existsPartyIdentifierInBusinessDomain(pi, userInfo.getBusinessDomain().getId())) {
                        exists = true;
                    }

                }

                if (exists) {
                    errors.rejectValue(IDENTIFIER_TYPE, "error.party.identifier.exists",
                            new Object[]{pi.getSchemeId(), pi.getValue()}, "Identifier already exists");
                }
            }
        } else if (!newIdType.equals("-1") ^ StringUtils.isNotBlank(newIdValue)) {
            errors.rejectValue(IDENTIFIER_TYPE, "error.party.identifier.type.value", null, "For an identifier, both the type and value are required");
        }


        if (!isEmptyList) {
            for (PartyIdentifierDTO partyIdentifierDTO : partyIdentifiers) {
                if (partyIdentifierDTO.getIsMarkedForDeletion()) {
                    continue;
                }

                // check if user did not clear existing identifiers
                if (partyIdentifierDTO.getType() == null || StringUtils.isBlank(partyIdentifierDTO.getValue())) {
                    errors.reject("error.party.identifier.missing.data");
                }

                if (StringUtils.isNotBlank(partyIdentifierDTO.getValue())) {
                    int pidLenght = partyIdentifierDTO.getValue().trim().length();
                    if (pidLenght > ValidationHelper.MAX_LENGHT) {
                        errors.reject("error.party.identifier.value.tooLong", new Object[]{partyIdentifierDTO.getType()}, "Identifier value too long");
                    } else {
                        if (pidLenght < ValidationHelper.MIN_LENGHT) {
                            errors.reject("error.party.identifier.value.tooShort", new Object[]{partyIdentifierDTO.getType()}, "Identifier value too short");
                        }
                    }
                }

                Boolean existsInDb = false;
                Long identifierId = partyIdentifierDTO.getId();
                if (identifierId == null) {// new identifiers of the party
                    if (partyIdentifierService.existsPartyIdentifierInBusinessDomain(partyIdentifierDTO.toDomain(), userInfo.getBusinessDomain().getId())) {
                        existsInDb = true;
                    }
                } else {// existing identifiers of the party (that might have been modified)
                    PartyIdentifier pid = partyIdentifierService.getBySchemeAndValueInBusinessDomain(partyIdentifierDTO.toDomain(), userInfo.getBusinessDomain().getId());
                    if (pid != null && pid.getId().longValue() != identifierId.longValue()) {
                        existsInDb = true;
                    }
                }
                if (existsInDb) {
                    errors.reject("error.party.identifier.exists", new Object[]{partyIdentifierDTO.getType(), partyIdentifierDTO.getValue()}, "Identifier already exists");
                }
            }
        }
    }

//	public void validateCertificate(Certificate certificate, Errors errors){
//		try {
//            errors.pushNestedPath("certificate");
//            ValidationUtils.invokeValidator(certificateValidator, certificate, errors);
//        } finally {
//            errors.popNestedPath();
//        }
//		
//	}

    /**
     * Checks if the list contains only deleted identifiers.
     *
     * @param partyIdentifiers
     * @return true, if the list is empty or all identifiers in the list are marked for deletion
     * false, if there is at list one identifier in the list which is not marked for deletion
     */
    private boolean existsActiveIdentifier(List<PartyIdentifierDTO> partyIdentifiers) {
        if (CollectionUtils.isEmpty(partyIdentifiers)) {
            return false;
        }

        for (PartyIdentifierDTO partyIdentifierDTO : partyIdentifiers) {
            if (!partyIdentifierDTO.getIsMarkedForDeletion()) {
                return true;
            }
        }

        return false;
    }


    private boolean isIdentifierFilledIn(String type, String value) {
        return StringUtils.isNotBlank(value) && !StringUtils.equals(type, "-1");
    }

    /**
     * Checks if there is an identifier in the list having the same type as the given party identifier, and not being marked for deletion.
     *
     * @param partyIdentifiers
     * @param pi
     * @return true, if there is an identifier with the same type in the list and not marked for deletion
     * false, in the remaining cases
     */
    private boolean isAlreadyInList(List<PartyIdentifierDTO> partyIdentifiers, PartyIdentifier pi) {
        if (CollectionUtils.isEmpty(partyIdentifiers)) {
            return false;
        }

        for (PartyIdentifierDTO partyIdentifierDTO : partyIdentifiers) {
            if (partyIdentifierDTO.getType().compareTo(pi.getSchemeId()) == 0
                    && !partyIdentifierDTO.getIsMarkedForDeletion()) {
                return true;
            }
        }

        return false;
    }

    private boolean isInListMarkedForDeletion(List<PartyIdentifierDTO> partyIdentifiers, PartyIdentifier pi) {
        if (CollectionUtils.isEmpty(partyIdentifiers)) {
            return false;
        }

        for (PartyIdentifierDTO partyIdentifierDTO : partyIdentifiers) {
            if (partyIdentifierDTO.getType().compareTo(pi.getSchemeId()) == 0
                    && StringUtils.equalsIgnoreCase(partyIdentifierDTO.getValue(), pi.getValue())
                    && partyIdentifierDTO.getIsMarkedForDeletion()) {
                pi.setId(partyIdentifierDTO.getId());
                return true;
            }
        }

        return false;

    }

}