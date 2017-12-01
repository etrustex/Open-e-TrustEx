package eu.europa.ec.cipa.etrustex.integration.util;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

public class CustomAutowiringInterceptor extends SpringBeanAutowiringInterceptor {
	private static final String CONTRACT_LOCATOR = "classpath:etrustex-integration-layer-context.xml";

	@Override
	protected BeanFactoryLocator getBeanFactoryLocator(Object target) {
		return ContextSingletonBeanFactoryLocator.getInstance(CONTRACT_LOCATOR);
	}
}
