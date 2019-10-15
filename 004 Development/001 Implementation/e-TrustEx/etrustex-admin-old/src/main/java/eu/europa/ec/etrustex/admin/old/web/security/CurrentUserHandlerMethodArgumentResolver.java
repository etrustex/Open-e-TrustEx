package eu.europa.ec.etrustex.admin.old.web.security;

import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.services.admin.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return
                methodParameter.getParameterAnnotation(ActiveUser.class) != null
                        && methodParameter.getParameterType().equals(SessionUserInformation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        log.debug("Resolving argument");

        if (this.supportsParameter(methodParameter)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.debug("Authentication: " + auth);
            SessionUserInformation userInfo = (SessionUserInformation) auth.getPrincipal();
            log.debug("SessionUserInformation: " + userInfo);
            return userInfo;
        } else {
            return WebArgumentResolver.UNRESOLVED;
        }
    }
}
