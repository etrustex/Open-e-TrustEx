/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.DocumentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author batrian
 */
@Component
public class DocumentValidator implements Validator {
    private final static String NAME = "name";
    private final static String LOCAL_NAME = "localName";
    private final static String NAMESPACE = "namespace";
    private final static String TYPE_CODE = "documentTypeCode";
    private final static String VERSION = "version";
    private final static String EMPTY_NAME = "error.document.name.empty";
    private final static String EMPTY_LOCAL_NAME = "error.document.localname.empty";
    private final static String EMPTY_NAMESPACE = "error.document.namespace.empty";
    private final static String EMPTY_TYPE_CODE = "error.document.documentTypeCode.empty";
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return DocumentForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DocumentForm doc = (DocumentForm) target;
        log.debug("validate document: {}", doc);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME, EMPTY_NAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, LOCAL_NAME, EMPTY_LOCAL_NAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAMESPACE, EMPTY_NAMESPACE);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, TYPE_CODE, EMPTY_TYPE_CODE);

        validationHelper.validateStringMinimumAndMaximumLength(NAME, doc.getName(), "document.name", errors);
        validationHelper.validateStringMinimumAndMaximumLength(LOCAL_NAME, doc.getLocalName(), "document.localName", errors);
        validationHelper.validateStringMinimumAndMaximumLength(NAMESPACE, doc.getNamespace(), "document.namespace", errors);
        validationHelper.validateStringMinimumAndMaximumLength(TYPE_CODE, doc.getDocumentTypeCode(), "document.typeCode", errors);
        validationHelper.validateStringMaximumLength(VERSION, doc.getVersion(), errors);
    }

}
