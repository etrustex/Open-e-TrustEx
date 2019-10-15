package eu.europa.ec.etrustex.admin.old.web.logs;

import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.LogsUtil;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.services.ILogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.lang.reflect.Method;

@Aspect
@Component
public class UserActionsLogAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ILogService logService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LogsUtil logsUtil;

    @Pointcut(value = "@annotation(userActionsLog)", argNames = "userActionsLog")
    public void beanAnnotatedWithUserActionsLog(UserActionsLog userActionsLog) {
    }

//    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) && !@annotation(eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog)")
//    public void otherHandlerMethods() {}

    @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, ..)")
    public void saveOperation(Object form) {
    }

    @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.delete(..)) && args(id, ..)")
    public void deleteOperation(Long id) {
    }

    @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, ..)")
    public void searchOperation(BindingResult result) {
    }

    @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.view(..)) && args(id, ..)")
    public void viewOperation(Long id) {
    }


	/* We don't loop through joinPoint arguments,
    Object[] args = joinPoint.getArgs();
    for (int i = 0; i < args.length; i++) {
    	...
    }
    
    enumaration in pointcut is much faster but we have to watch out the order of arguments.
    Remember that args(.., foo, ..) gives pointcut uses more than one .. in args (compiler limitation) error
    */

    /* SAVE */
    @AfterReturning(pointcut = "beanAnnotatedWithUserActionsLog(userActionsLog) && saveOperation(form)", returning = "ajaxResult")
    public void saveReturned(JoinPoint joinPoint, UserActionsLog userActionsLog, Object form, AjaxResult ajaxResult) {
        LogDTO.LOG_TYPE logType;
        String description;
        try {
            Method method = form.getClass().getMethod("getId");
            Long entityId = (Long) method.invoke(form);
            LogDTO logDTO = logsUtil.getLog(entityId == null ? LogDTO.LOG_OPERATION.CREATE : LogDTO.LOG_OPERATION.UPDATE,
                    LogDTO.LOG_TYPE.SUCCESS, userActionsLog.entity(), ajaxResult.getId(),
                    joinPoint.getTarget().getClass().getName());

            if (ajaxResult.getSuccess()) {
                logType = LogDTO.LOG_TYPE.SUCCESS;
                description = "Saved " + logDTO.getEntity() + " with id: " + logDTO.getEntityId();
            } else {
                logType = LogDTO.LOG_TYPE.ERROR;
                description = "Business error saving " + logDTO.getEntity() + ": " + ajaxResult.getMessage();
            }

            logDTO.setLogType(logType);
            logDTO.setDescription(description);

            logService.saveLog(logDTO);

            logsUtil.printLog(logDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /* DELETE */
    @AfterReturning(pointcut = "beanAnnotatedWithUserActionsLog(userActionsLog) && deleteOperation(id)", returning = "ajaxResult")
    public void deleteReturning(JoinPoint joinPoint, UserActionsLog userActionsLog, Long id, AjaxResult ajaxResult) {
        try {
            LogDTO logDTO = logsUtil.getLog(LogDTO.LOG_OPERATION.DELETE, LogDTO.LOG_TYPE.ERROR, userActionsLog.entity(), id,
                    joinPoint.getTarget().getClass().getName());

            if (ajaxResult.getSuccess()) {
                logDTO.setLogType(LogDTO.LOG_TYPE.SUCCESS);
                logDTO.setDescription("Deleted " + logDTO.getEntity() + " with id: " + id);
            } else {
                logDTO.setLogType(LogDTO.LOG_TYPE.ERROR);
                logDTO.setDescription("Business error deleting " + logDTO.getEntity() + " with id: " + id + ": " + ajaxResult.getMessage());
            }

            logService.saveLog(logDTO);

            logsUtil.printLog(logDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }


    /* SEARCH */
    @AfterReturning(pointcut = "beanAnnotatedWithUserActionsLog(userActionsLog) && searchOperation(result)")
    public void searchReturning(JoinPoint joinPoint, UserActionsLog userActionsLog, BindingResult result) {
        try {
            if (result.hasErrors()) {
                LogDTO logDTO = logsUtil.getLog(LogDTO.LOG_OPERATION.SEARCH, LogDTO.LOG_TYPE.ERROR, userActionsLog.entity(), null,
                        joinPoint.getTarget().getClass().getName());

                StringBuilder message = new StringBuilder("Business error searching. There are validation errors: \n");
                for (ObjectError e : result.getAllErrors()) {
                    String resolvedMsg = messageSource.getMessage(e.getCode(), e.getArguments(), LocaleContextHolder.getLocale());
                    message.append(resolvedMsg).append("\n");
                }

                logDTO.setDescription(message.toString());

                logService.saveLog(logDTO);

                logsUtil.printLog(logDTO);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}