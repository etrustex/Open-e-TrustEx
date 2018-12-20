/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import static eu.europa.ec.etrustex.admin.old.web.utils.ConfigurationTypeEnum.AMQP;
import static eu.europa.ec.etrustex.admin.old.web.utils.ConfigurationTypeEnum.JMS;
import static eu.europa.ec.etrustex.admin.old.web.utils.ConfigurationTypeEnum.WS;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import eu.europa.ec.etrustex.admin.old.web.dto.BusinessDomainElement;
import eu.europa.ec.etrustex.admin.old.web.dto.EndpointForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.utils.ConfigurationTypeEnum;
import eu.europa.ec.etrustex.admin.old.web.utils.FormUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.UserRoleEnum;
import eu.europa.ec.etrustex.dao.dto.EndpointDTO;
import eu.europa.ec.etrustex.domain.AMQPCredentials;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.JMSCredentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.ProxyCredentials;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.WSCredentials;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.AMQPEndpoint;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.ICredentialsService;
import eu.europa.ec.etrustex.services.IEndpointService;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.IMessageRoutingService;

@Component
public class EndpointValidator implements Validator {
    @Autowired
    ValidationHelper validationHelper;
    @Autowired
    private IEndpointService endpointService;
    @Autowired
    private IBusinessDomainService businessDomainService;    
    @Autowired
    private ICredentialsService credentialsService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IMessageRoutingService messageRoutingService;

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
         * UC130_BR24	If the user tries to delete a routing endpoint (be it JMS, WS or AMQP), and this routing endpoint is referred to in the Message_Routing table, then an error message will be shown to the user
         */
        if (form.getId() != null && CollectionUtils.isNotEmpty(messageRoutingService.findByEndpointIdAndNotDispatched(form.getId()))) {
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

        if (!isBD) {
            errors.rejectValue("businessDomain", "error.endpoint.businessDomain.mandatory");
        } else {
            // UC130_BR11. The specified business domain shall be one of the business domains for which the user is configured
            if (!userInfo.getBusinessDomains().contains(new BusinessDomainElement(bd))) {
                errors.rejectValue("businessDomain", "error.endpoint.businessDomain.user");
            }
        }

        if (!isParty) {
            errors.rejectValue("party", "error.endpoint.party.mandatory");
        } else if (isParty && !party.getBusinessDomain().equals(bd)) {
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
            
            if(!validateUniqueCredentials(new JMSCredentials(), JMSEndpoint.class, form.getCredentials().getUser(), form.getCredentials().getId(), form.getBusinessDomain().getId(), form.getId(), form.getProviderUrl())){
            	errors.reject("error.endpoint.jms.credentials.username.exists");
            }
        } else if (configurationType.equals(WS)) {
            // Web Service specific UC130_BR04
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "wsEndpointURL", "error.endpoint.wsEndpointURL.empty");
            
            if(!validateUniqueCredentials(new WSCredentials(), WSEndpoint.class, form.getCredentials().getUser(), form.getCredentials().getId(), form.getBusinessDomain().getId(), form.getId(), form.getWsEndpointURL())){
            	errors.reject("error.endpoint.ws.credentials.username.exists");
            }				       
        } else if (configurationType.equals(AMQP)) {
            // AMQP specific UC130_BR30
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "providerUrl", "error.endpoint.providerUrl.empty");
            
            if(!validateUniqueCredentials(new AMQPCredentials(), AMQPEndpoint.class, form.getCredentials().getUser(), form.getCredentials().getId(),form.getBusinessDomain().getId(), form.getId(), form.getProviderUrl())){
            	errors.reject("error.endpoint.amqp.credentials.username.exists");
            }
        }
        
		/*
		 * UC130_BR07	If Is using proxy is set to Yes, the following fields are mandatory:
			–	proxy Host
			–	proxy Port
			–	Proxy credentials (in next validation)
		 */
        if (form.getUseProxy()) {
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
        
        if(StringUtils.isEmpty(proxyCredentials.getUser()) && (!StringUtils.isEmpty(form.getProxyHost()) || form.getProxyPort() != null)){
        	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyCredential.user", "error.endpoint.proxyCredential.user.empty");
        }
        
        if (form.getUseProxy() || !StringUtils.isEmpty(proxyCredentials.getUser()) || !StringUtils.isEmpty(proxyCredentials.getPassword())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyCredential.user", "error.endpoint.proxyCredential.user.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "proxyCredential.password", "error.endpoint.proxyCredential.password.empty");
            
            if(form.getProxyPort() == null || StringUtils.isEmpty(form.getProxyHost())){
        		errors.reject("error.endpoint.proxyCredential.hostorport.missing");
            }else if(!validateUniqueProxyCredentials(form.getId(), proxyCredentials, form.getProxyHost(), form.getProxyPort())){
        		errors.reject("error.endpoint.proxyCredential.user.exists");
        	}            
        }

        if (authCredentials != null && !StringUtils.isEmpty(authCredentials.getUser())) {
            validateUsername(form, authCredentials, errors, "credentials.user", userInfo.getBusinessDomain().getId());
        }

        if (proxyCredentials != null && !StringUtils.isEmpty(proxyCredentials.getUser())) {
            validateUsername(form, proxyCredentials, errors, "proxyCredential.user", userInfo.getBusinessDomain().getId());
        }

        /*
         * ETRUSTEX-1442 At least one of profile, ICA or tx shall be specified
         */
        if (!isProfile && !isICA && !isTx) {
            errors.rejectValue("profile", "error.endpoint.messageInfo.mandatory.fields");
        }

        // UC130_BR12	Profile, Transaction and Interchange Agreement are mutually exclusive.
        if (isTx ^ isProfile ? isICA : isTx) {
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
        if (isProfile) {
            if ((isBD && !bd.getProfiles().contains(profile))
                    || (!isBD && userInfo.getBusinessDomains().contains(new BusinessDomainElement(bd)))) {
                errors.rejectValue("businessDomain", "error.endpoint.profile.businessDomain");
            }
        }

        /*
         * UC130_BR15	If Interchange Agreement is specified, it shall pertain to the current business domain selected in the
         * Business domain field of Message information section. If no such business domain is selected, the interchange agreement
         * shall pertain to the current selected business domain of the user.
         */
        if (isICA) {
            ica = interchangeAgreementService.getInterchangeArgreement(ica.getId());

            if (!ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().equals(bd)) {
                errors.rejectValue("businessDomain", "error.endpoint.ica.businessDomain");
            }
        }
        
    }

    private void validateUsername(EndpointForm form, Credentials credentials, Errors errors, String field, Long businessDomainId) {
        boolean isUniqueUsername = isUniqueUsername(form, credentials, businessDomainId);

        if (!isUniqueUsername) {
            boolean isNewCredentials = credentials.getId() == null;
            boolean isProxyCredentials = credentials instanceof ProxyCredentials;
            String username = credentials.getUser();
            Long endpointId = form.getId();

            if (endpointId != null) {
                /*
                 * Existing routing endpoint. Check if new or existing credentials
                 * We cannot trust `form.getCredential().getId() == null` condition because it may be null after user click on reset button
                 */
                Endpoint existingEndpoint = endpointService.findById(endpointId);
                Credentials existingCredentials;

                if (isProxyCredentials) {
                    existingCredentials = existingEndpoint.getProxyCredential();
                    boolean isProxyHostChanged = !existingEndpoint.getProxyHost().equals(form.getProxyHost());
                    boolean isProxyPortChanged = !existingEndpoint.getProxyPort().equals(form.getProxyPort());
                    boolean isUsernameChanged = !StringUtils.equalsIgnoreCase(username, credentialsService.get(existingCredentials.getId()).getUser());

                    if (!isProxyHostChanged && !isProxyPortChanged && !isUsernameChanged) {
                        isUniqueUsername = true;
                    }
                } else {
                    existingCredentials = existingEndpoint.getCredentials();
                    boolean isEndpointUrlChanged = isEndpointUrlChanged(form);
                    boolean isUsernameChanged = !StringUtils.equalsIgnoreCase(username, credentialsService.get(existingCredentials.getId()).getUser());

                    if (!isEndpointUrlChanged && !isUsernameChanged) {
                        isUniqueUsername = true;
                    }
                }

                // Update credentials, username has not changed. Set Credentials id if it has been reset by user
                if (credentials.getId() == null) {
                    credentials.setId(existingCredentials.getId());
                }
            }

            if (!isUniqueUsername && isNewCredentials) {
                errors.rejectValue(field, isProxyCredentials ? "error.endpoint.proxyCredential.user.exists" : "error.endpoint.credentials.username.exists");
            }
        }
    }

    private boolean isUniqueUsername(EndpointForm form, Credentials credentials, Long businessDomainId) {
        boolean uniqueUsername = true;
        boolean isProxyCredentials = credentials instanceof ProxyCredentials;
        String username = credentials.getUser();

        if (isProxyCredentials) {
            /*
             * UC130_BR31	For proxy credentials, username must be unique per proxy host and proxy port.
             */
            uniqueUsername = credentialsService.isUniqueProxyCredentials(username, form.getProxyHost(), form.getProxyPort());
        } else if (credentials.getId() == null) {
            /*
             * ETRUSTEX-4224 unable to use existing authentication credentials when creating a Web Service Routing Endpoint.
             * So we only validate new credentials
             */
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

        return uniqueUsername;
    }

    private boolean isEndpointUrlChanged(EndpointForm form) {
        boolean changed = false;

        if (form.getId() != null) {
            Endpoint e = endpointService.findById(form.getId());
            String url = null;

            if (e instanceof JMSEndpoint) {
                url = ((JMSEndpoint) e).getProviderUrl();
                changed = !url.trim().equalsIgnoreCase(form.getProviderUrl());
            } else if (e instanceof WSEndpoint) {
                url = ((WSEndpoint) e).getWsEndpointURL();
                changed = !url.trim().equalsIgnoreCase(form.getWsEndpointURL());
            } else if (e instanceof AMQPEndpoint) {
                url = ((AMQPEndpoint) e).getProviderUrl();
                changed = !url.trim().equalsIgnoreCase(form.getProviderUrl());
            } else {
                throw new UnsupportedOperationException("Missing implementation for " + e.getClass().getName());
            }
        }
        
        return changed;
    }
    
    private boolean validateUniqueCredentials(Credentials credCriteria, Class<? extends Endpoint> endpointClass, String credUser, Long credId, Long bdId, Long edpId, String edpValue){
    	if(StringUtils.isNotBlank(credUser)){
    		credCriteria.setUser(credUser);
    		List<? extends Endpoint> edps = endpointService.findByCredentialsTypeAndUsernameEquals(credCriteria, bdId, endpointClass);
	        
    		if((credId != null) && edps.isEmpty()){
    			return false;
    		}
    		
			for (Endpoint endpoint : edps) { 
				String url = null;
				if(endpoint instanceof WSEndpoint){
					url = ((WSEndpoint)endpoint).getWsEndpointURL();
				}else if(endpoint instanceof JMSEndpoint){
					url = ((JMSEndpoint)endpoint).getProviderUrl();
				}else if(endpoint instanceof AMQPEndpoint){
					url = ((AMQPEndpoint)endpoint).getProviderUrl();
				}
				
				//Cannot create a new credential when we have an existing url/credential pair
				if((credId == null) && edpValue.equals(url)){
					return false;
				}
				
				//Reused Credentials
				if((credId != null) && 
						((edpValue.equals(url) && !endpoint.getCredentials().getId().equals(credId)) 
								|| (!edpValue.equals(url) && endpoint.getCredentials().getId().equals(credId)))
						){
					return false;
				}
			
			}			
	    }
        return true;
    }
    
    private boolean validateUniqueProxyCredentials(Long endpointId, Credentials proxyCredentials, String host, Integer port){
    	 if (proxyCredentials.getUser() != null) {      	
 	        Endpoint endpointCrit = null;
 	        List<? extends Endpoint> edps = null;        
 	        endpointCrit = new EndpointDTO();
 	        endpointCrit.setProxyHost(host);
 	        endpointCrit.setProxyPort(port);
 	        edps = endpointService.findEndpointsByCriteria(endpointCrit);
 	        if(proxyCredentials.getId() == null){
 	        	if(edps.size() == 0){
 		        	return true;
 		        }
 	        }else{
 	        	if(edps.size() == 0){
 		        	return false;
 		        }else{
 		        	for (Endpoint endpoint : edps) {
 						if(!endpoint.getProxyCredential().getUser().equals(proxyCredentials.getUser())){
 							return false;
 						}
 					}
 		        }
 	        }
         }
     	return true;
    }
}