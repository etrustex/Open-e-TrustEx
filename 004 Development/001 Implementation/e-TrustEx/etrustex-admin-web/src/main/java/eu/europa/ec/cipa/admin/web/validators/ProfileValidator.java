/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import eu.europa.ec.cipa.admin.web.dto.ProfileForm;
import eu.europa.ec.cipa.etrustex.services.IProfileService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class ProfileValidator implements Validator {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String NAME = "name";
	private final static String NAMESPACE = "namespace";
	private final static String BUSINESS_DOMAINS = "businessDomains";
	private final static String CIA_LEVEL = "ciaLevel";
	
	private final static String EMPTY_NAME = "error.profile.name.empty";
	private final static String EMPTY_NAMESPACE = "error.profile.namespace.empty";
	private final static String EMPTY_BUSINESS_DOMAINS = "error.profile.businessDomains.empty";
	private final static String EMPTY_CIA_LEVEL = "error.profile.ciaLevel.empty";
	
	@Autowired ValidationHelper validationHelper;
	@Autowired private MessageSource messageSource;
	@Autowired private IProfileService profileService;
	
	
	@Override
    public boolean supports(Class<?> clazz) {  
        return ProfileForm.class.equals(clazz);  
    }

	@Override
	public void validate(Object target, Errors errors) {
		ProfileForm form = (ProfileForm) target;
    	
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME, EMPTY_NAME);
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAMESPACE, EMPTY_NAMESPACE);
    	
    	validationHelper.validateStringMinimumAndMaximumLength(NAME, form.getName(), "transaction.name", errors);
    	validationHelper.validateStringMinimumAndMaximumLength(NAMESPACE, form.getNamespace(), "transaction.namespace", errors);
    	
    	
    	// validate creation
    	if (form.getId() == null) {
    		// name is unique
    		if (StringUtils.isNotBlank(form.getName())) {
    			if (profileService.findByName(form.getName()) != null) {
    				errors.rejectValue(NAME, "common.error.exists", new String[]{messageSource.getMessage("profile.name", null, LocaleContextHolder.getLocale())}, "Profile name already exists");
    			}
    		}
    	}
    	
    	if(form.getBusinessDomains() == null || form.getBusinessDomains().isEmpty()) {
    		errors.rejectValue(BUSINESS_DOMAINS, EMPTY_BUSINESS_DOMAINS);
    	}
    	
    	if(form.getCiaLevel() == null || form.getCiaLevel().getAvailabilityLevel() == null || form.getCiaLevel().getConfidentialityLevel() == null || form.getCiaLevel().getIntegrityLevel() == null) {
    		errors.rejectValue(CIA_LEVEL, EMPTY_CIA_LEVEL);
    	}
	}

}
