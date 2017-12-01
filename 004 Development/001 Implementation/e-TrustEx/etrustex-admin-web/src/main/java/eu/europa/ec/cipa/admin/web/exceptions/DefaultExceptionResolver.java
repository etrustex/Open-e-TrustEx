package eu.europa.ec.cipa.admin.web.exceptions;

import eu.europa.ec.cipa.admin.web.logs.UserActionsLog;
import eu.europa.ec.cipa.admin.web.utils.AjaxResult;
import eu.europa.ec.cipa.admin.web.utils.LogsUtil;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
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
    @Autowired private MessageSource messageSource;
    @Autowired private LogsUtil logsUtil;
    @Autowired private ILogService logService;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            saveLog(request, response, handler, ex);

            if(ex instanceof AccessDeniedException) {
                // 403
                if(handler.getClass().equals(HandlerMethod.class)
                        && ((HandlerMethod) handler).getReturnType().getParameterType().equals(AjaxResult.class)) {
                    // Ajax Result, Eg. after save, delete.
                    response.sendRedirect(request.getContextPath() + "/responseBodyException.do?msg=" + ex.getMessage());
                } else {
                    modelAndView.addObject("errorMessage",ex.getMessage());
                    // HTML page result or Ajax fragment result. Eg. exception thrown inside the "load" fragment method
                    modelAndView.setViewName("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ? "frag.accessDenied" : "accessDenied");
                }

            } else if(ex instanceof NoSuchRequestHandlingMethodException) {
                // 404
                modelAndView.setViewName("resourceNotFound");
                modelAndView.addObject("errorMessage", messageSource.getMessage("common.error.resourcenotfound.title", null, LocaleContextHolder.getLocale()));
            } else {
                // 500
                if(handler.getClass().equals(HandlerMethod.class)
                        && ((HandlerMethod) handler).getReturnType().getParameterType().equals(AjaxResult.class)) {
                    // Ajax Result, Eg. after save, delete.
                    response.sendRedirect(request.getContextPath() + "/responseBodyException.do");
                } else {
                    // HTML page result or Ajax fragment result. Eg. exception thrown inside the "load" fragment method
                    modelAndView.addObject("errorMessage", messageSource.getMessage("common.system.error", null, LocaleContextHolder.getLocale()));
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

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }


    private void saveLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String className = null;
        String description;
        Class entity = null;
        Long entityId = null;;
        LogDTO.LOG_OPERATION operation = null;

        /*
         * Who is throwing the exception?
         */
        if(handler.getClass().equals(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            className = handlerMethod.getBeanType().getName();
            UserActionsLog userActionsLogAnnotation = handlerMethod.getMethodAnnotation(UserActionsLog.class);
            if(userActionsLogAnnotation != null) {
                entity = userActionsLogAnnotation.entity();
            }

            String methodName = handlerMethod.getMethod().getName();

            if(methodName.equals("save")) {
                entityId = getEntityIdForSave(request, handlerMethod);
                operation = entityId == null ? LogDTO.LOG_OPERATION.CREATE : LogDTO.LOG_OPERATION.UPDATE;
            } else {
                operation = getLogOperation(methodName);
            }
        }

        /*
         * Description based on type of exception
         */
        if(ex instanceof AccessDeniedException) {
            description = ex.getMessage();
            operation = LogDTO.LOG_OPERATION.AUTHORIZATION;
        } else if (ex instanceof AuthenticationException) {
            description = ex.getMessage();
            operation = LogDTO.LOG_OPERATION.AUTHENTICATION;
        } else if(ex instanceof NoSuchRequestHandlingMethodException) {
            description = "Resource not found. Original URL: " + request.getAttribute("javax.servlet.error.request_uri");
        } else {
//            String stackTrace = ExceptionUtils.getStackTrace(ex);
//            description = stackTrace.substring(0, Math.min(stackTrace.length(), 2000));
            description = StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : ex.getClass().getName();
        }

        if(operation == null) {
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

        for(MethodParameter methodParameter : methodParameters) {
            if(methodParameter.getParameterAnnotation(ModelAttribute.class) != null && methodParameter.getParameterAnnotation(Valid.class) != null) {
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


    private LogDTO.LOG_OPERATION getLogOperation(String methodName) {
        LogDTO.LOG_OPERATION operation = null;

        if(StringUtils.isNotBlank(methodName)) {
            switch (methodName) {
                case "create":
                case "createLoad":
                    operation = LogDTO.LOG_OPERATION.CREATE;
                    break;
                case "edit":
                case "editLoad":
                    operation = LogDTO.LOG_OPERATION.UPDATE;
                    break;
                case "search":
                case "searchLoad":
                case "searchResults":
                    operation = LogDTO.LOG_OPERATION.SEARCH;
                    break;
                case "view":
                case "viewLoad":
                    operation = LogDTO.LOG_OPERATION.VIEW;
                    break;
                case "delete":
                    operation = LogDTO.LOG_OPERATION.DELETE;
                    break;
                default:
                    operation = LogDTO.LOG_OPERATION.OTHER;
            }
        }

        return operation;
    }
}
