/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.validators;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.Date;

/**
 * @author batrian
 */
@Component
public class ValidationHelper {
    public static final Integer MIN_LENGHT = 2;
    public static final Integer MAX_LENGHT = 255;
    // Message Keys
    public static final String SEARCH_MANDATORY_FIELD = "error.search.criteria.needed";
    public static final String MINIMUM_FIELD_LENGHT = "validation.field.lenght.minimum";
    public static final String TOO_LONG_STRING_MSG_KEY = "error.tooLong";
    public static final String INVALID_START_DATE_MSG_KEY = "error.date.start";
    public static final String INVALID_END_DATE_MSG_KEY = "error.date.end";
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;

    public String getSaveErrorsMsg(BindingResult result) {
        log.debug("Binding errors");

        StringBuilder message = new StringBuilder("There are validation errors: <br/><ul>");
        for (ObjectError e : result.getAllErrors()) {
            String resolvedMsg = "";
            try {
                resolvedMsg = messageSource.getMessage(e.getCode(), e.getArguments(), LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException nsme) {
                resolvedMsg = e.getDefaultMessage();
            }
            log.debug(e.getObjectName() + " - " + resolvedMsg + " - " + e.toString());
            message.append("<li type='disc'>").append(resolvedMsg).append("</li>");
        }
        message.append("</ul>");

        return message.toString();
    }

    public void validateStringMinimumAndMaximumLength(String fieldName, String fieldValue, String fieldKey, Errors errors) {
        String fieldLabel = messageSource.getMessage(fieldKey, null, LocaleContextHolder.getLocale());
        if (StringUtils.isBlank(fieldValue)) {
            return;
        }

        if (fieldValue.trim().length() > MAX_LENGHT) {
            errors.rejectValue(fieldName, TOO_LONG_STRING_MSG_KEY,
                    new Object[]{MAX_LENGHT, fieldLabel}, "The value of the field " + fieldLabel + " is too long");
        } else {
            if (fieldValue.trim().length() < MIN_LENGHT) {
                errors.rejectValue(fieldName, MINIMUM_FIELD_LENGHT,
                        new Object[]{MIN_LENGHT, fieldLabel}, "At least " + MIN_LENGHT + " characters needed for the field " + fieldLabel + ". Default msg!");
            }
        }
    }

    public void validateStringMinimumAndMaximumLength(String fieldName, String fieldValue, Errors errors) {

        if (StringUtils.isBlank(fieldValue)) {
            return;
        }

        if (fieldValue.trim().length() > MAX_LENGHT) {
            errors.rejectValue(fieldName, TOO_LONG_STRING_MSG_KEY,
                    new Object[]{MAX_LENGHT, fieldName}, "The value of the field " + fieldName + " is too long");
        } else {
            if (fieldValue.trim().length() < MIN_LENGHT) {
                errors.rejectValue(fieldName, MINIMUM_FIELD_LENGHT,
                        new Object[]{MIN_LENGHT, fieldName}, "At least " + MIN_LENGHT + " characters needed for the field " + fieldName + ".");
            }
        }
    }


    public void validateStringMinimumLength(String fieldName, String fieldValue, Errors errors) {

        validateStringMinimumLength(fieldName, fieldValue, MIN_LENGHT, errors);

    }

    public void validateStringMinimumLength(String fieldName, String fieldValue, int minLenght, Errors errors) {

        if (fieldValue == null) {
            return;
        }

        if (fieldValue.trim().length() < minLenght) {
            errors.rejectValue(fieldName, MINIMUM_FIELD_LENGHT,
                    new Object[]{MIN_LENGHT, fieldName}, "At least " + MIN_LENGHT + " characters needed for the field " + fieldName + ".");

        }
    }


    public void validateStringMaximumLength(String fieldName, String fieldValue, Errors errors) {

        validateStringMaximumLength(fieldName, fieldValue, MAX_LENGHT, errors);

    }


    public void validateStringMaximumLength(String fieldName, String fieldValue, int maxLenght, Errors errors) {

        if (StringUtils.isBlank(fieldValue)) {
            return;
        }

        if (fieldValue.trim().length() > maxLenght) {
            errors.rejectValue(fieldName, TOO_LONG_STRING_MSG_KEY,
                    new Object[]{MAX_LENGHT, fieldName}, "The value of the field " + fieldName + " is too long");
        }
    }


    public void validateDateOrder(String startDateFieldName, Date startDate, String endDateFieldName, Date endDate, Errors errors) {

        if (startDate != null && endDate != null) {
            if (endDate.before(startDate)) {
                errors.rejectValue(startDateFieldName, INVALID_START_DATE_MSG_KEY);
                errors.rejectValue(endDateFieldName, INVALID_END_DATE_MSG_KEY);
            }
        }

    }
}
