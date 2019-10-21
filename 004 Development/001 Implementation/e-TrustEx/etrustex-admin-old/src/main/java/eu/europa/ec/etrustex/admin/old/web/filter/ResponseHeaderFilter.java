package eu.europa.ec.etrustex.admin.old.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class ResponseHeaderFilter implements Filter {
    @SuppressWarnings({"RedundantFieldInitialization"})
    private FilterConfig filterConfig = null;

    @Override
    public void doFilter(ServletRequest aServletRequest, ServletResponse aServletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) aServletResponse;

        // set the provided HTTP response parameters
        for (Enumeration e = filterConfig.getInitParameterNames(); e.hasMoreElements(); ) {
            String headerName = (String) e.nextElement();
            response.addHeader(headerName, filterConfig.getInitParameter(headerName));
        }

        // pass the request/response on
        chain.doFilter(aServletRequest, response);
    }

    @Override
    public void init(FilterConfig aFilterConfig) {
        filterConfig = aFilterConfig;
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
