package eu.europa.ec.cipa.admin.web.validators;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.europa.ec.cipa.admin.web.dto.LogsSearchForm;

public class LogsSearchValidator implements Validator{
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String USERNAME = "username_search";
	private final static String CORRELATION_ID = "correlationId_search";
	private final static String DOCUMENT_TYPE_CODE = "documentTypeCode_search";
	private final static String BUSINESS_CORRELATION_ID = "businessCorrelationId_search";
	
	@Autowired ValidationHelper validationHelper;
	
	@Override
    public boolean supports(Class<?> clazz) {  
        return LogsSearchForm.class.equals(clazz);  
    }

	@Override
	public void validate(Object target, Errors errors) {
		LogsSearchForm form = (LogsSearchForm) target;
    	
		if(StringUtils.isNotEmpty(form.getUsername_search()))  {
			validationHelper.validateStringMinimumLength(USERNAME, form.getUsername_search(), errors);
		}
		
		if(StringUtils.isNotEmpty(form.getCorrelationId_search()))  {
			validationHelper.validateStringMinimumLength(CORRELATION_ID, form.getCorrelationId_search(), errors);
		}
        
		if(StringUtils.isNotEmpty(form.getDocumentTypeCode_search()))  {
			validationHelper.validateStringMinimumLength(DOCUMENT_TYPE_CODE, form.getDocumentTypeCode_search(), errors);
		}
		
		if(StringUtils.isNotEmpty(form.getBusinessCorrelationId_search()))  {
			validationHelper.validateStringMinimumLength(BUSINESS_CORRELATION_ID, form.getBusinessCorrelationId_search(), errors);
		}
	}
}
