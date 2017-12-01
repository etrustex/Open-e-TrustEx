/**
 * 
 */
package eu.europa.ec.cipa.admin.web.validators;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import eu.europa.ec.cipa.admin.web.dto.CertificateWrapper;
import eu.europa.ec.cipa.admin.web.dto.CertificatesForm;
import eu.europa.ec.cipa.etrustex.domain.Certificate;

/**
 * @author batrian
 *
 */
@Component
public class CertificateValidator implements Validator {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
//	private final static String SN = "serialNumber";
//	private final static String TYPE = "type";
//	private final static String USAGE = "usage";
//	private final static String ISSUER = "issuer";
//	private final static String HOLDER = "holder";
//	private final static String DATA = "encodedData";
//	private final static String ATTRIBUTES = "attributes";
//	private final static String VERSION = "version";
//	private final static String SIGN_ALG = "signatureAlgorithm";
//	private final static String SIGN_VAL = "signatureValue";
//	private final static String START_DATE = "validityStartDate";
//	private final static String END_DATE = "validityEndDate";
//	
//	private final static String EMPTY_SN_MSG_KEY = "error.certificate.serialNumber.empty";
//	private final static String EMPTY_TYPE_MSG_KEY = "error.certificate.type.empty";
//	private final static String EMPTY_USAGE_MSG_KEY = "error.certificate.usage.empty";
//	private final static String EMPTY_ISSUER_MSG_KEY = "error.certificate.issuer.empty";
//	private final static String EMPTY_HOLDER_MSG_KEY = "error.certificate.holder.empty";
//	private final static String EMPTY_DATA_MSG_KEY = "error.certificate.data.empty";
////	private final static String INVALID_END_DATE_MSG_KEY = "error.certificate.invalid.validTo";
////	private final static String INVALID_START_DATE_MSG_KEY = "error.certificate.invalid.validFrom";
//	private final static String MULTIPLE_ACTIVE_CERTIFICATES_PER_USAGE = "error.certificate.isActive.usage";
	
	@Autowired ValidationHelper validationHelper;
	
	@Override  
    public boolean supports(Class<?> clazz) {  
        return CertificatesForm.class.equals(clazz);  
    }  
  
    @Override  
    public void validate(Object target, Errors errors) {  
    	
    	CertificatesForm certificatesForm = (CertificatesForm) target;
    	log.debug("validate certificate: {}", certificatesForm);
    	  
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, SN, EMPTY_SN_MSG_KEY);
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, TYPE, EMPTY_TYPE_MSG_KEY);
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USAGE, EMPTY_USAGE_MSG_KEY);
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, HOLDER, EMPTY_HOLDER_MSG_KEY);
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ISSUER, EMPTY_ISSUER_MSG_KEY);
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, DATA, EMPTY_DATA_MSG_KEY);
//        
//        validationHelper.validateStringMinimumAndMaximumLength(SN, certificatesForm.getNewCertificate().getCertificate().getSerialNumber(), "certificate.serialNumber", errors);
//        validationHelper.validateStringMinimumAndMaximumLength(TYPE, certificatesForm.getNewCertificate().getCertificate().getType(), "certificate.type", errors);
//        validationHelper.validateStringMinimumAndMaximumLength(USAGE, certificatesForm.getNewCertificate().getCertificate().getUsage(), "certificate.usage", errors);
//        validationHelper.validateStringMinimumAndMaximumLength(ISSUER, certificatesForm.getNewCertificate().getCertificate().getIssuer(), "certificate.issuer", errors);
//        validationHelper.validateStringMinimumAndMaximumLength(HOLDER, certificatesForm.getNewCertificate().getCertificate().getHolder(), "certificate.holder", errors);
//        validationHelper.validateStringMinimumLength(DATA, certificatesForm.getNewCertificate().getCertificate().getEncodedData(), errors);
//        validationHelper.validateStringMaximumLength(DATA, certificatesForm.getNewCertificate().getCertificate().getEncodedData(), 4000, errors);
//        validationHelper.validateStringMinimumAndMaximumLength(ATTRIBUTES, certificatesForm.getNewCertificate().getCertificate().getAttributes(), "certificate.attributes", errors);
//        validationHelper.validateStringMaximumLength(VERSION, certificatesForm.getNewCertificate().getCertificate().getVersion(), errors);
//        validationHelper.validateStringMinimumAndMaximumLength(SIGN_ALG, certificatesForm.getNewCertificate().getCertificate().getSignatureAlgorithm(), "certificate.signatureAlgorithm", errors);
//        validationHelper.validateStringMinimumAndMaximumLength(SIGN_VAL, certificatesForm.getNewCertificate().getCertificate().getSignatureValue(), "certificate.signatureValue", errors);
//        
//        validationHelper.validateDateOrder(START_DATE, certificatesForm.getNewCertificate().getCertificate().getValidityStartDate(), END_DATE, certificatesForm.getNewCertificate().getCertificate().getValidityEndDate(), errors);
        
        /*
         * UC10_BR49 A Party can have more than one certificate, as certificates have different usages. 
         * However only one active certificate per usage is allowed at a given time. 
         */
    	List<CertificateWrapper> certificates = certificatesForm.getCertificates();
    	
    	outerloop: for (int i = 0; i < certificates.size(); i++) {
    		Certificate cert = certificates.get(i).getCertificate();
    		
    		if(cert.getIsActive()) {
    			String usage = cert.getUsage();
    			
    			for (int j = i+1; j < certificates.size(); j++) {
    				Certificate otherCert = certificates.get(j).getCertificate();
    				if(otherCert.getUsage().equals(usage) && otherCert.getIsActive()) {
    					errors.reject("error.certificate.isActive.usage");
    					break outerloop;
    				}
    			}
    		}
    	}
    }
}
