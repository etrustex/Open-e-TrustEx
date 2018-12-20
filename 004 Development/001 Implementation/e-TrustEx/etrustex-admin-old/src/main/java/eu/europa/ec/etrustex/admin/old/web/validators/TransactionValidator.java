/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import eu.europa.ec.etrustex.admin.old.web.dto.TransactionForm;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.ITransactionService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;


@Component
public class TransactionValidator implements Validator {
    private final static String NAME = "name";
    private final static String NAMESPACE = "namespace";
    private final static String VERSION = "version";
    private final static String DOCUMENT = "document";
    private final static String PROFILES = "profiles";
    private final static String REQUEST_LOCAL_NAME = "requestLocalName";
    private final static String RESPONSE_LOCAL_NAME = "responseLocalName";
    private final static String SENDER_ROLE = "senderRole";
    private final static String RECEIVER_ROLE = "receiverRole";
    private final static String CIA_LEVEL = "ciaLevel";
    @Autowired
    ValidationHelper validationHelper;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IMessageService messageService;


    @Override
    public boolean supports(Class<?> clazz) {
        return TransactionForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransactionForm form = (TransactionForm) target;

        // UC70_BR05 & UC70_BR27 & UC70_BR28
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME, "error.transaction.name.empty");
        validationHelper.validateStringMinimumAndMaximumLength(NAME, form.getName(), errors);

        // UC70_BR06 & UC70_BR27 & UC70_BR28
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAMESPACE, "error.transaction.namespace.empty");
        validationHelper.validateStringMinimumAndMaximumLength(NAMESPACE, form.getNamespace(), errors);

        // UC70_BR07 & UC70_BR27
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, VERSION, "error.transaction.version.empty");
        validationHelper.validateStringMaximumLength(VERSION, form.getVersion(), errors);

        // UC70_BR08
        if (form.getDocument() == null || form.getDocument().getId() == null) {
            errors.rejectValue(DOCUMENT, "error.transaction.document.empty");
        }

        // UC70_BR09 & UC70_BR27 & UC70_BR28
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, REQUEST_LOCAL_NAME, "error.transaction.requestLocalName.empty");
        validationHelper.validateStringMinimumAndMaximumLength(REQUEST_LOCAL_NAME, form.getRequestLocalName(), errors);

        // UC70_BR10 & UC70_BR27 & UC70_BR28
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, RESPONSE_LOCAL_NAME, "error.transaction.responseLocalName.empty");
        validationHelper.validateStringMinimumAndMaximumLength(RESPONSE_LOCAL_NAME, form.getResponseLocalName(), errors);

        // UC70_BR11
        if (form.getSenderRole() == null || form.getSenderRole().getId() == null) {
            errors.rejectValue(SENDER_ROLE, "error.transaction.senderRole.empty");
        }

        // UC70_BR12
        if (form.getReceiverRole() == null || form.getReceiverRole().getId() == null) {
            errors.rejectValue(RECEIVER_ROLE, "error.transaction.receiverRole.empty");
        }


        // Check the following rules only if no previous errors
        if (!errors.hasErrors()) {
            // UC70_BR13
            if (!StringUtils.isEmpty(form.getName()) && !StringUtils.isEmpty(form.getVersion())) {
                List<Transaction> l = transactionService.getTransactionsByNameAndVersion(form.getName(), form.getVersion());

                if (!l.isEmpty() && (form.getId() == null || !l.get(0).getId().equals(form.getId()))) {
                    errors.rejectValue(NAME, "error.transaction.unique.nameAndVersion");
                }
            }

            // UC70_BR14
            if (!StringUtils.isEmpty(form.getNamespace()) && !StringUtils.isEmpty(form.getRequestLocalName())) {
                List<Transaction> l = transactionService.getTransactionsByNamespaceAndRequestLocalName(form.getNamespace(), form.getRequestLocalName());
                if (!l.isEmpty() && (form.getId() == null || !l.get(0).getId().equals(form.getId()))) {
                    errors.rejectValue(NAMESPACE, "error.transaction.unique.namespaceAndRequestLocalName");
                }
            }

            // UC70_BR24
            if (form.getId() != null && messageService.findMessagesByTransaction(form.getId()) > 0) {
                errors.reject("error.transaction.message");
            }
        }

        // UC70_BR15
        if (form.getCiaLevel() == null || form.getCiaLevel().getAvailabilityLevel() == null || form.getCiaLevel().getConfidentialityLevel() == null || form.getCiaLevel().getIntegrityLevel() == null) {
            errors.rejectValue(CIA_LEVEL, "error.transaction.ciaLevel.empty");
        }

        // UC70_BR16
        if (form.getProfiles() == null || form.getProfiles().isEmpty()) {
            errors.rejectValue(PROFILES, "error.transaction.profiles.empty");
        }
    }
}
