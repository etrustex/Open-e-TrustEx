package eu.europa.ec.etrustex.admin.old.web.exceptions;

import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.LogsUtil;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.services.ILogService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Method;

public class DefaultExceptionResolver extends AbstractHandlerExceptionResolver {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private int order;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LogsUtil logsUtil;
    @Autowired
    private ILogService logService;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            saveLog(request, response, handler, ex);

            if (ex instanceof AccessDeniedException) {
                // 403
                if (handler.getClass().equals(HandlerMethod.class)
                        && ((HandlerMethod) handler).getReturnType().getParameterType().equals(AjaxResult.class)) {
                    // Ajax Result, Eg. after save, delete.
                    response.sendRedirect(request.getContextPath() + "/responseBodyException.do?msg=" + ex.getMessage());
                } else {
                    modelAndView.addObject("message", ex.getMessage());
                    // HTML page result or Ajax fragment result. Eg. exception thrown inside the "load" fragment method
                    modelAndView.setViewName("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ? "frag.accessDenied" : "accessDenied");
                }

            } else if (ex instanceof NoSuchRequestHandlingMethodException) {
                // 404
                modelAndView.setViewName("resourceNotFound");
                modelAndView.addObject("message", messageSource.getMessage("common.error.resourcenotfound.title", null, LocaleContextHolder.getLocale()));
            } else if (ex instanceof RecordNotFoundException) {
                modelAndView.addObject("message", messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()));
                modelAndView.setViewName("frag.recordNotFound");
            } else {
                // 500
                if (handler.getClass().equals(HandlerMethod.class)
                        && ((HandlerMethod) handler).getReturnType().getParameterType().equals(AjaxResult.class)) {
                    // Ajax Result, Eg. after save, delete.
                    response.sendRedirect(request.getContextPath() + "/responseBodyException.do");
                } else {
                    // HTML page result or Ajax fragment result. Eg. exception thrown inside the "load" fragment method
                    modelAndView.addObject("message", messageSource.getMessage("common.system.error", null, LocaleContextHolder.getLocale()));
                    modelAndView.setViewName("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ? "frag.uncaughtException" : "uncaughtException");
                }
            }

            modelAndView.addObject("exception", ex);
            modelAndView.addObject("url", request.getRequestURL());

            return modelAndView;
        } catch (Exception e) {
            log.error("Error redirecting to DefaultExceptionHandlerController (/exception.do)...", e);
            return null; //Null-return = pass the exception to next handler in order
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    private void saveLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String className = null;
        String description;
        Class entity = null;
        Long entityId = null;
        ;
        LogDTO.LOG_OPERATION operation = null;

        /*
         * Who is throwing the exception?
         */
        if (handler.getClass().equals(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            className = handlerMethod.getBeanType().getName();
            UserActionsLog userActionsLogAnnotation = handlerMethod.getMethodAnnotation(UserActionsLog.class);
            if (userActionsLogAnnotation != null) {
                entity = userActionsLogAnnotation.entity();
            }

            String methodName = handlerMethod.getMethod().getName();

            if (methodName.equals("save")) {
                entityId = getEntityIdForSave(request, handlerMethod);
                operation = entityId == null ? LogDTO.LOG_OPERATION.CREATE : LogDTO.LOG_OPERATION.UPDATE;
            } else {
                operation = logsUtil.getLogOperation(methodName);
            }
        }

        /*
         * Description based on type of exception
         */
        if (ex instanceof AccessDeniedException) {
            description = ex.getMessage();
            operation = LogDTO.LOG_OPERATION.AUTHORIZATION;
        } else if (ex instanceof AuthenticationException) {
            description = ex.getMessage();
            operation = LogDTO.LOG_OPERATION.AUTHENTICATION;
        } else if (ex instanceof NoSuchRequestHandlingMethodException) {
            description = "Resource not found. Original URL: " + request.getAttribute("javax.servlet.error.request_uri");
        } else {
//            String stackTrace = ExceptionUtils.getStackTrace(ex);
//            description = stackTrace.substring(0, Math.min(stackTrace.length(), 2000));
            description = StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : ex.getClass().getName();
        }

        if (operation == null) {
            operation = LogDTO.LOG_OPERATION.OTHER;
        }

        LogDTO logDTO = logsUtil.getLog(operation, LogDTO.LOG_TYPE.ERROR, entity, entityId, className);
        logDTO.setDescription(description);
        logDTO.setLargeValue(ExceptionUtils.getStackTrace(ex));
        logService.saveLog(logDTO);
        logsUtil.printLog(logDTO);
    }

    /*
     * Extract entity id if present
     */
    private Long getEntityIdForSave(HttpServletRequest request, HandlerMethod handlerMethod) {
        Long entityId = null;
        Object form;

        // Search for form in request
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        for (MethodParameter methodParameter : methodParameters) {
            if (methodParameter.getParameterAnnotation(ModelAttribute.class) != null && methodParameter.getParameterAnnotation(Valid.class) != null) {
                try {
                    form = request.getSession().getAttribute(methodParameter.getParameterAnnotation(ModelAttribute.class).value());
                    Method method = form.getClass().getMethod("getId");
                    entityId = (Long) method.invoke(form);
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        return entityId;
    }
}
