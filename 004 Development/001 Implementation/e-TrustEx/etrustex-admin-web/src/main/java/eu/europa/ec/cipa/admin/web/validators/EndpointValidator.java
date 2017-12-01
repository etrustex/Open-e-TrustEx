/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.BusinessDomainElement;
import eu.europa.ec.cipa.admin.web.dto.EndpointForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.utils.ConfigurationTypeEnum;
import eu.europa.ec.cipa.admin.web.utils.FormUtil;
import eu.europa.ec.cipa.admin.web.utils.UserRoleEnum;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.routing.AMQPEndpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.cipa.etrustex.services.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

import static eu.europa.ec.cipa.admin.web.utils.ConfigurationTypeEnum.*;

@Component
public class EndpointValidator implements Validator {
	@Autowired ValidationHelper validationHelper;
	@Autowired private IEndpointService endpointService;
	@Autowired private IBusinessDomainService businessDomainService;
	@Autowired private IProfileService profileService;
	@Autowired private ICredentialsService credentialsService;
    @Autowired private IInterchangeAgreementService interchangeAgreementService;
    @Autowired private IMessageRoutingService messageRoutingService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return EndpointForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EndpointForm form = (EndpointForm) target;

        // Modification
        /*
         * UC130_BR24	If there is at least one message that has not yet been successfully dispatched, the endpoint configuration cannot be changed.
         */
        if( form.getId() != null && CollectionUtils.isNotEmpty(messageRoutingService.findByEndpointIdAndNotDispatched(form.getId()))) {
            errors.reject("error.endpoint.messageRouting");
        }

        InterchangeAgreement ica = form.getInterchangeAgreement();
        Profile profile = form.getProfile();
        Transaction tx = form.getTansaction();
        Party party = form.getParty();
        Credentials authCredentials = form.getCredentials();
        Credentials proxyCredentials = form.getProxyCredential();
        BusinessDomain bd;
        /*
         * ETRUSTEX-1407 CIPAdmin shall allow CBO users to configure routing endpoints
         */
        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            bd = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId());
        } else {
            bd = form.getBusinessDomain();
        }

        boolean isProfile = profile != null && FormUtil.convertDefaultOptionToNull(profile.getId()) != null;
        boolean isTx = tx != null && tx.getId() != null;
        boolean isICA = ica != null && ica.getId() != null;
        boolean isParty = party != null && party.getId() != null;
        boolean isBD = bd != null && FormUtil.convertDefaultOptionToNull(bd.getId()) != null;

		// Common fields UC130_BR02
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endointMessageType", "error.endpoint.endointMessageType.empty");

        if (!isBD) {
            errors.rejectValue("businessDomain", "error.endpoint.businessDomain.mandatory");
        } else {
            // UC130_BR11. The specified business domain shall be one of the business domains for which the user is configured
            if(!userInfo.getBusinessDomains().contains(new BusinessDomainElement(bd))) {
                errors.rejectValue("businessDomain", "error.endpoint.businessDomain.user");
            }
        }

        if(!isParty) {
            errors.rejectValue("party", "error.endpoint.party.mandatory");
        } else if ( isParty && !party.getBusinessDomain().equals(bd) ) {
            /*
             * UC130_BR16	The specified Party shall pertain to the current business domain selected in the Business domain field of Message information section.
             */
            errors.rejectValue("party", "error.endpoint.party.businessDomain");
        }

        ConfigurationTypeEnum configurationType = form.getConfigurationType();
        if (configurationType.equals(JMS)) {
            // JMS specific UC130_BR03
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destinationJndiName", "error.endpoint.destinationJndiName.empty");

            // TODO Next 2 lines commented just for BRIS branch
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "providerUrl", "error.endpoint.providerUrl.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "initalContextFactory", "error.endpoint.initalContextFactory.empty");

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "connectionFactoryJndiName", "error.endpoint.connectionFactoryJndiName.empty");
        } else if (configurationType.equals(WS)) {
            // Web Service specific UC130_BR04
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "wsEndpointURL", "error.endpoint.wsEndpointURL.empty");
        } else if (configurationType.equals(AMQP)) {
            // AMQP specific UC130_BR30
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "providerUrl", "error.endpoint.providerUrl.empty");
        }

		/*
		 * UC130_BR05	If Is authentication required is set to Yes, the Authentication credentials are mandatory.
		 */
		if(form.getIsAuthenticationRequired() != null && form.getIsAuthenticationRequired()
                && (StringUtils.isEmpty(authCredentials.getUser()) || StringUtils.isEmpty(authCredentials.getPassword()))) {
			errors.rejectValue("isAuthenticationRequired", "error.endpoint.credentials.mandatory");
		}
		
		/*
		 * UC130_BR07	If Is using proxy is set to Yes, the following fields are mandatory:
			–	proxy Host
			–	proxy Port
			–	Proxy credentials (in next validation)
		 */
		if(form.getUseProxy()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyHost", "error.endpoint.proxyHost.empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyPort", "error.endpoint.proxyPort.empty");
		}


		/*
		 * UC130_BR08	If proxy credentials are required, or either proxy username or one of the passwords are filled in, then:
			•	Proxy username is mandatory
			•	Proxy password is mandatory
			•	Proxy password and confirm password must be identical (Already been checked)
		 REMOVED IN ETRUSTEX-1352
		 Rolled back in v1.102 01/06/2016
		 */
		if(form.getUseProxy() || !StringUtils.isEmpty(proxyCredentials.getUser()) || !StringUtils.isEmpty(proxyCredentials.getPassword())) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyCredential.user", "error.endpoint.proxyCredential.user.empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyCredential.password", "error.endpoint.proxyCredential.password.empty");
		}



		/*
		 * UC130_BR09	At least one of the fields in the Message Information section must be filled in.
		 */
//		if((bd == null || FormUtil.convertDefaultOptionToNull(bd.getId()) == null)
//				&& (tx == null || tx.getId() == null)
//				&& (party == null || party.getId() == null)
//				&& (profile == null || FormUtil.convertDefaultOptionToNull(profile.getId()) == null)
//				&& (ica == null || ica.getId() == null)) {
//			errors.rejectValue("businessDomain", "error.endpoint.messageInfo.empty");
//		}



		// Check that credentials and proxyCredentials useranme are not equal ???
//		if (authCredentials != null && proxyCredentials != null
//				&& !StringUtils.isEmpty(authCredentials.getUser()) && !StringUtils.isEmpty(proxyCredentials.getUser())
//				&& StringUtils.equals(authCredentials.getUser(), proxyCredentials.getUser())) {
//			errors.rejectValue("proxyCredential.user", "error.endpoint.proxyCredential.user.equals");
//		}

        if (authCredentials != null ) {
            validateUsername(form, authCredentials, errors, "credentials.user", userInfo.getBusinessDomain().getId());
        }

        if (proxyCredentials != null) {
            validateUsername(form, proxyCredentials, errors, "proxyCredential.user", userInfo.getBusinessDomain().getId());
        }

        /*
         * ETRUSTEX-1442 At least one of profile, ICA or tx shall be specified
         */
        if(!isProfile && !isICA && !isTx) {
            errors.rejectValue("profile", "error.endpoint.messageInfo.mandatory.fields");
        }

        // UC130_BR12	Profile, Transaction and Interchange Agreement are mutually exclusive.
        if(isTx ^ isProfile ? isICA : isTx) {
            errors.rejectValue("profile", "error.endpoint.messageInfo.mutually.exclusive");
        }

        /*
         * ETRUSTEX-1352
         * Rolled back in v1.102 01/06/2016
         */
//        if(isProfile) {
//            // UC130_BR28 If Profile and Transaction are specified, the transaction must belong to the specified Profile.
//            if(isTx && !tx.getProfiles().contains(profile)) {
//                errors.rejectValue("profile", "error.endpoint.messageInfo.tx.profile");
//            }
//
//            // UC130_BR29 If Profile and Interchange Agreement are specified, the interchange agreement profile must be identical to the profile specified in the configuration.
//            if(isICA && !ica.getProfile().equals(profile)) {
//                errors.rejectValue("profile", "error.endpoint.messageInfo.profile.ica");
//            }
//
//            // UC130_BR31 If Profile, Transaction and Interchange Agreement are specified, rules UC130_BR28 and UC130_BR29 shall apply.
//        }
//
//        // UC130_BR30 If Transaction and Interchange Agreement are specified, the transaction must belong to interchange agreement profile.
//        if(isTx && isICA && !tx.getProfiles().contains(ica.getProfile())) {
//            errors.rejectValue("profile", "error.endpoint.messageInfo.tx.ica");
//        }
//
//        // UC130_BR32 If a Party is specified and it is a Third Party, the Profile becomes mandatory.
//        if(isParty && party.getThirdPartyFlag() && !isProfile) {
//            errors.rejectValue("profile", "error.endpoint.messageInfo.party");
//        }

        /*
         * UC130_BR14	If Profile is specified, it shall pertain to the current business domain selected in the Business domain field of Message information section.
         * If no such business domain is selected, the profile shall pertain to the current selected business domain of the user.
         */
        if( isProfile ) {
            if( ( isBD && !bd.getProfiles().contains(profile) )
                    || ( !isBD && userInfo.getBusinessDomains().contains(new BusinessDomainElement(bd)) ) ) {
                errors.rejectValue("businessDomain", "error.endpoint.profile.businessDomain");
            }
        }

        /*
         * UC130_BR15	If Interchange Agreement is specified, it shall pertain to the current business domain selected in the
         * Business domain field of Message information section. If no such business domain is selected, the interchange agreement
         * shall pertain to the current selected business domain of the user.
         */
        if( isICA ){
            ica = interchangeAgreementService.getInterchangeArgreement(ica.getId());

            if( !ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().equals(bd) ) {
                errors.rejectValue("businessDomain", "error.endpoint.ica.businessDomain");
            }
        }
	}

    private void validateUsername(EndpointForm form, Credentials credentials, Errors errors, String field, Long businessDomainId){
        /*
         * Existing username check
         * only for new credentials
         * ETRUSTEX-2391 Routing Endpoint - Credentials username already exists Error when creating a WS EP with same url and authentication credentials as an existing one
         */
        if(credentials != null && credentials.getId() == null && !StringUtils.isEmpty(credentials.getUser())) {
            // If username already exists
            if(credentialsService.exists(credentials)) {
                String errorCode = credentials instanceof ProxyCredentials ? "error.endpoint.proxyCredential.user.exists" : "error.endpoint.credentials.username.exists";
                boolean uniqueUsername = true;
                String username = credentials.getUser();

                if (credentials instanceof ProxyCredentials ) {
                    /*
                     * UC130_BR31	For proxy credentials, username must be unique per proxy host and proxy port.
                     */
                     uniqueUsername = credentialsService.isUniqueProxyCredentials(username, form.getProxyHost(), form.getProxyPort());
                } else {
                    /*
                     * UC130_BR32	For authentication credentials, username must be unique per
                        •	Provider URL for JMS endpoints
                        •	Webservice URL for WebService endpoints
                        •	AMQP provider URL for AMQP endpoints
                        UPDATE AFTER ETRUSTEX-1902  type PROVIDER --> WS, JMS, AMQP
                    */
                    Class endpointClass;
                    if (credentials instanceof JMSCredentials) {
                        endpointClass = JMSEndpoint.class;
                    } else if (credentials instanceof WSCredentials) {
                        endpointClass = WSEndpoint.class;
                    } else if (credentials instanceof AMQPCredentials) {
                        endpointClass = AMQPEndpoint.class;
                    } else {
                        // Should never happen
                        endpointClass = Endpoint.class;
                    }

                    List<? extends Endpoint> endpointList = endpointService.findByCredentialsTypeAndUsernameEquals(credentials, businessDomainId, endpointClass);
                    if (CollectionUtils.isNotEmpty(endpointList)) {
                        for (Endpoint e : endpointList) {
                            if (e instanceof JMSEndpoint && StringUtils.equalsIgnoreCase(((JMSEndpoint) e).getProviderUrl(), form.getProviderUrl())
                                    || e instanceof WSEndpoint && StringUtils.equalsIgnoreCase(((WSEndpoint) e).getWsEndpointURL(), form.getWsEndpointURL())
                                    || e instanceof AMQPEndpoint && StringUtils.equalsIgnoreCase(((AMQPEndpoint) e).getProviderUrl(), form.getProviderUrl())) {
                                uniqueUsername = false;
                            }
                        }
                    }
                }

                if(!uniqueUsername) {
                    Long endpointId = form.getId();

                    if(endpointId == null) { // New routing endpoint
                        errors.rejectValue(field, errorCode);
                    } else { // Existing routing endpoint. Check if new or old credentials
                        // We cannot trust `form.getCredential().getId() == null` condition because it may be null after user click on reset button
                        Endpoint existingEndpoint = endpointService.findById(endpointId);
                        Credentials existingCredentials;

                        if (credentials instanceof ProxyCredentials ) {
                            existingCredentials = existingEndpoint.getProxyCredential();
                        } else {
                            existingCredentials = existingEndpoint.getCredentials();
                        }

                        if(existingCredentials == null || existingCredentials.getId() == null) { // New credentials
                            errors.rejectValue(field, errorCode);
                        } else if(!StringUtils.equalsIgnoreCase(username, credentialsService.get(existingCredentials.getId()).getUser())) { // Update credentials, check if username has changed
                            errors.rejectValue(field, errorCode);
                        } else if(credentials.getId() == null) {
                            // Update credentials, username has not changed. Set Credentials id if it has been reset by user
                            credentials.setId(existingCredentials.getId());
                        }
                    }
                }
            }
        }
    }
}