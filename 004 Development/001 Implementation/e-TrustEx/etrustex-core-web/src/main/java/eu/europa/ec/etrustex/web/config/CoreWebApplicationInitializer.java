package eu.europa.ec.etrustex.web.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import eu.europa.ec.etrustex.integration.web.CustomMessageDispatcherServlet;
import eu.europa.ec.etrustex.redirect.PostRedirect;
import eu.europa.ec.etrustex.services.CoreWebListenner;
import eu.europa.ec.etrustex.services.RetServlet;
import eu.europa.ec.etrustex.services.util.ClearMetadataCacheServlet;

/**
 * We need to directly implement WebApplicationInitializer to ensure that
 * Weblogic picks it up.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CoreWebApplicationInitializer implements WebApplicationInitializer {
	private final static Logger logger = LoggerFactory.getLogger(WebApplicationInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.debug("######################################################################");
		logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  WebApplicationInitializer.onStartup()");
		logger.debug("######################################################################");

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
		rootContext.register(AppContextConfig.class); // configuration class for root context
		servletContext.addListener(new ContextLoaderListener(rootContext));// Manage the lifecycle of the root application context
		rootContext.setServletContext(servletContext);
		
		// configuration filters (Authentication & Log)
		DelegatingFilterProxy authenticationFilter = new DelegatingFilterProxy("authenticationFilter");
		authenticationFilter.setServletContext(servletContext);
		servletContext.addFilter("authenticationFilter", authenticationFilter).addMappingForUrlPatterns(null, true,	"/*");

		DelegatingFilterProxy logFilter = new DelegatingFilterProxy("logFilter");
		logFilter.setServletContext(servletContext);
		servletContext.addFilter("logFilter", logFilter).addMappingForUrlPatterns(null, true, "/*");
		
		/**
		 * This servlet is the entry point of the webservices end points of
		 *	open eprior application that delegates the handling of incoming webservice calls
		 *	to the Spring Integration messaging endpoint
		 * 
		 */
		XmlWebApplicationContext servicesServletContext = new XmlWebApplicationContext();
	    servicesServletContext.setParent(rootContext);
	    servicesServletContext.setConfigLocation("classpath:services-servlet.xml");
	    ServletRegistration.Dynamic servicesServlet = servletContext.addServlet("services", new CustomMessageDispatcherServlet(servicesServletContext));
	    Map<String, String> servicesParams = new HashMap<>();
		servicesParams.put("wl-dispatch-policy", "servicesWorkManager");
		servicesParams.put("messageReceiverBeanName", "messageReceiver");
	    servicesServlet.setInitParameters(servicesParams);
	    servicesServlet.setLoadOnStartup(1);
	    servicesServlet.addMapping("/services/*");		
		servicesServlet.addMapping("/ApplicationResponse-2.0");	
		servicesServlet.addMapping("/AttachedDocument-2.0");
		servicesServlet.addMapping("/DocumentBundle-2.0");	
		servicesServlet.addMapping("/DocumentBundle-2.1");	
		servicesServlet.addMapping("/DocumentBundleJustice-2.0");	
		

	 // dispatcher readServletContext
		XmlWebApplicationContext readServicesServletContext = new XmlWebApplicationContext();
	    readServicesServletContext.setParent(rootContext);
	    readServicesServletContext.setConfigLocation("classpath:services-servlet.xml");
	    ServletRegistration.Dynamic readServicesServlet = servletContext.addServlet("readServices", new CustomMessageDispatcherServlet(readServicesServletContext));
	    Map<String, String> readParams = new HashMap<>();
	    readParams.put("wl-dispatch-policy", "readServicesWorkManager");
	    readParams.put("messageReceiverBeanName", "readMessageReceiver");
	    readServicesServlet.setInitParameters(readParams);
		readServicesServlet.setLoadOnStartup(1);
		readServicesServlet.addMapping("/readservices/*");
		readServicesServlet.addMapping("/InboxRequest-2.0");
		readServicesServlet.addMapping("/QueryRequest-2.0");
		readServicesServlet.addMapping("/QueryRequestJustice-2.0");
		readServicesServlet.addMapping("/RetrieveInterchangeAgreementsRequest-2.0");
		readServicesServlet.addMapping("/RetrieveRequest-2.0");
		readServicesServlet.addMapping("/RetrieveRequest-2.1");
		readServicesServlet.addMapping("/StatusRequest-2.0");
		readServicesServlet.addMapping("/StatusRequest-2.1");
		readServicesServlet.addMapping("/ViewRequest-2.0");
	    
	 // dispatcher wrapperServletContext
		XmlWebApplicationContext wrapperServletContext = new XmlWebApplicationContext();
	    wrapperServletContext.setParent(rootContext);
	    wrapperServletContext.setConfigLocation("classpath:services-servlet.xml");
	    ServletRegistration.Dynamic wrapperServicesServlet = servletContext.addServlet("wrapperServices", new CustomMessageDispatcherServlet(wrapperServletContext));
	    Map<String, String> wrapperParams = new HashMap<>();
 		wrapperParams.put("wl-dispatch-policy", "wrapperServicesWorkManager");
 		wrapperParams.put("messageReceiverBeanName", "wrapperMessageReceiver");
 		wrapperServicesServlet.setInitParameters(wrapperParams);
 		wrapperServicesServlet.setLoadOnStartup(1);
 		wrapperServicesServlet.addMapping("/wrapperservices/*");
 		wrapperServicesServlet.addMapping("/DocumentWrapper-2.0");	
 		

 		servletContext.addListener(CoreWebListenner.class);

		// postRedirectCFT TServlet
		ServletRegistration.Dynamic postRedirectCFT = servletContext.addServlet("PostRedirectCFT", PostRedirect.class);
		postRedirectCFT.setInitParameter("REDIRECT_KEY", "CFT_TEST_ENDPOINT");
		postRedirectCFT.setInitParameter("wl-dispatch-policy", "postRedirectWorkManager");		
		postRedirectCFT.addMapping("/CallForTendersTest-2.0");
		
		// retention policy servlet
		ServletRegistration.Dynamic retServlet = servletContext.addServlet("RetServlet", RetServlet.class);
		retServlet.addMapping("/retentionService");
		
		// clear metadata cache servlet
		ServletRegistration.Dynamic clearMetadataCacheServlet = servletContext.addServlet("ClearMetadataCacheServlet", ClearMetadataCacheServlet.class);
		clearMetadataCacheServlet.addMapping("/clearmetadatacache");		
	}

}
