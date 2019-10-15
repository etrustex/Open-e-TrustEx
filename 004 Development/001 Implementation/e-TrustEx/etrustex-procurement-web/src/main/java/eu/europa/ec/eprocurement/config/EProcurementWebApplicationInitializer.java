package eu.europa.ec.eprocurement.config;

import bsh.servlet.BshServlet;
import eu.europa.ec.etrustex.integration.web.CustomMessageDispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class EProcurementWebApplicationInitializer implements WebApplicationInitializer {
	private final static Logger logger = LoggerFactory.getLogger(EProcurementWebApplicationInitializer.class);
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

		FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncodingFilter",
				characterEncodingFilter);
		characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");

		servletContext.setInitParameter("parentContextKey", "etrustex-integration-layer.context");
		servletContext.setInitParameter("locatorFactorySelector", "classpath:etrustex-integration-layer-context.xml");
		
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(EProcAppContextConfig.class); // configuration class for root context
		servletContext.addListener(new ContextLoaderListener(rootContext));// Manage the lifecycle of the root application context
		rootContext.setServletContext(servletContext);
		
		// configuration filters (Authentication & Log)
		DelegatingFilterProxy authenticationFilter = new DelegatingFilterProxy("authenticationFilter");
		authenticationFilter.setServletContext(servletContext);
		servletContext.addFilter("authenticationFilter", authenticationFilter).addMappingForUrlPatterns(null, true,	"/*");

		DelegatingFilterProxy logFilter = new DelegatingFilterProxy("logFilter");
		logFilter.setServletContext(servletContext);
		servletContext.addFilter("logFilter", logFilter).addMappingForUrlPatterns(null, true, "/*");
		
		//Services Servlet
		XmlWebApplicationContext servicesServletContext = new XmlWebApplicationContext();
	    servicesServletContext.setParent(rootContext);
	    servicesServletContext.setConfigLocation("classpath:services-servlet.xml");
	    ServletRegistration.Dynamic servicesServlet = servletContext.addServlet("services", new CustomMessageDispatcherServlet(servicesServletContext));
	    Map<String, String> servicesParams = new HashMap<>();
		servicesParams.put("wl-dispatch-policy", "epriorWorkManager");
		servicesParams.put("messageReceiverBeanName", "messageReceiver");
	    servicesServlet.setInitParameters(servicesParams);
	    servicesServlet.setLoadOnStartup(1);
	    servicesServlet.addMapping("/services/*");
	    
	    //Customer Services Servlet
	    XmlWebApplicationContext cServicesServletContext = new XmlWebApplicationContext();
	    cServicesServletContext.setParent(rootContext);
	    cServicesServletContext.setConfigLocation("classpath:services-servlet.xml");
		ServletRegistration.Dynamic customerServicesServlet = servletContext.addServlet("customerServices", new CustomMessageDispatcherServlet(cServicesServletContext));
		Map<String, String> cSservicesParams = new HashMap<>();
		cSservicesParams.put("wl-dispatch-policy", "epriorWorkManager");
		cSservicesParams.put("messageReceiverBeanName", "customerMessageReceiver");
		customerServicesServlet.setInitParameters(cSservicesParams);
		customerServicesServlet.setLoadOnStartup(1);
		customerServicesServlet.addMapping("/services/customer/*");
		
		//Supplier Services Servlet
	    XmlWebApplicationContext sServicesServletContext = new XmlWebApplicationContext();
	    sServicesServletContext.setParent(rootContext);
	    sServicesServletContext.setConfigLocation("classpath:services-servlet.xml");
		ServletRegistration.Dynamic supplierServicesServlet = servletContext.addServlet("supplierServices", new CustomMessageDispatcherServlet(sServicesServletContext));
		Map<String, String> sSservicesParams = new HashMap<>();
		sSservicesParams.put("wl-dispatch-policy", "epriorWorkManager");
		sSservicesParams.put("messageReceiverBeanName", "supplierMessageReceiver");
		supplierServicesServlet.setInitParameters(sSservicesParams);
		supplierServicesServlet.setLoadOnStartup(1);
		supplierServicesServlet.addMapping("/services/supplier/*");
		
		//BSH Servlet
		ServletRegistration.Dynamic bshServlet = servletContext.addServlet("bshservlet", new BshServlet());
		Map<String, String> bshServletParams = new HashMap<>();
		bshServletParams.put("wl-dispatch-policy", "epriorWorkManager");
		bshServlet.setInitParameters(bshServletParams);
		bshServlet.setLoadOnStartup(1);
		bshServlet.addMapping("/eval");
	}
	
}	
