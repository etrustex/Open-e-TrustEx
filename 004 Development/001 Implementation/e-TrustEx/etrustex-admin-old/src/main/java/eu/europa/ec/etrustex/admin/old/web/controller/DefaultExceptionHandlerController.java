package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.exceptions.ErrorInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author guerrpa
 * <p>
 * Exceptions resolved by DefaultExceptionResolver (configured in the MVC namespace) are
 * redirected to this controller, which returns to the view either:
 * - a JSON object containing info about the error (for ajax requests),
 * - or a view name
 * <p>
 * It is like a global exception handler and it can be overriden by using @ExceptionHandler
 * in a Controller. E.g:
 * @ExceptionHandler(Exception.class)
 * @ResponseBody public ErrorInfo handleException(HttpServletRequest req, Exception ex) {
 * ...
 * return new ErrorInfo(errorURL, errorMessage);
 * }
 * <p>
 * NOTE: This solution is for Spring 3.1. With @ControllerAdvice, introduced in Spring 3.2 we
 * don't need to do all this to handle exceptions globally.
 */
@Controller
public class DefaultExceptionHandlerController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/responseBodyException")
    // , headers = "x-requested-with=XMLHttpRequest" headers = "Accept=application/json"
    @ResponseBody
    public ErrorInfo handleResponseBodyException(@RequestParam(required = false, value = "msg") String msg, HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), StringUtils.isNotBlank(msg) ? msg : messageSource.getMessage("common.system.error", null, LocaleContextHolder.getLocale()));
    }


    @RequestMapping("/accessDenied")
    public String accessDenied(@RequestParam(required = false, value = "msg") String msg, Model model) {
        model.addAttribute("message", StringUtils.isNotBlank(msg) ? msg : messageSource.getMessage("common.error.access.rights", null, LocaleContextHolder.getLocale()));
        return "accessDenied";
    }

    /*
        This may be called either from DefaultExceptionResolver in case of NoSuchRequestHandlingMethodException,
        or from web.xml in case of 404
     */
    @RequestMapping("/resourceNotFound")
    public String resourceNotFound(Model model, HttpServletRequest request) { // UserAtctionsLogAspect Pointcut
        model.addAttribute("message", messageSource.getMessage("common.error.resourcenotfound.problemdescription", null, LocaleContextHolder.getLocale()));
        return "uncaughtException";
    }


    @RequestMapping("/forbidden")
    public String forbidden(@RequestParam(required = false, value = "msg") String msg, Model model) {
        model.addAttribute("message", StringUtils.isNotBlank(msg) ? msg : messageSource.getMessage("error.login.user.notConfigured", null, LocaleContextHolder.getLocale()));
//        if(SecurityContextHolder.getContext().getAuthentication() != null) {
//            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
//        }
//
//        session.invalidate();
//        model.addAttribute("message", messageSource.getMessage("error.login.user.notConfigured", null, LocaleContextHolder.getLocale()));
//        response.reset();
//        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        return "accessDenied";
    }

    @RequestMapping("/outOfSession")
    public String outOfSession(Model model) {
        model.addAttribute("message", messageSource.getMessage("common.error.outOfSession.problemdescription", null, LocaleContextHolder.getLocale()));
        return "outOfSession";
    }
}
