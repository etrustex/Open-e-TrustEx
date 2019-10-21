package eu.europa.ec.etrustex.admin.old.web;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;

/**
 * We need to directly implement WebApplicationInitializer to ensure that Weblogic picks it up.
 */
public class MvcWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        container.setInitParameter("parentContextKey", "etrustex-integration-layer.context");
        container.setInitParameter("locatorFactorySelector", "classpath:etrustex-integration-layer-context.xml");


        AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext();
        rootCtx.register(AppConfig.class, SecurityConfig.class);
        container.addListener(new ContextLoaderListener(rootCtx));
        rootCtx.setServletContext(container);


        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(MvcConfig.class);
        ctx.setServletContext(container);
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcherAdminWeb", new DispatcherServlet(ctx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");


        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = container.addFilter("characterEncodingFilter", characterEncodingFilter);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
    }
}
