package eu.europa.ec.etrustex.admin.old.web.interceptors;

import eu.europa.ec.etrustex.admin.old.web.dto.InterchangeAgreementForm;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by guerrpa on 03/03/2016.
 */
public class ICAMultiInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object icaForm = request.getSession().getAttribute("interchangeAgreement");
        if (icaForm != null && icaForm.getClass().equals(InterchangeAgreementForm.class)) {
            request.getSession().removeAttribute("interchangeAgreement");
        }
        return true;
    }
}
