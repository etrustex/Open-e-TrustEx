package eu.europa.ec.etrustex.admin.old.web;

import eu.europa.ec.etrustex.admin.old.web.exceptions.DefaultExceptionResolver;
import eu.europa.ec.etrustex.admin.old.web.interceptors.ICAMultiInterceptor;
import eu.europa.ec.etrustex.admin.old.web.security.CurrentUserHandlerMethodArgumentResolver;
import eu.europa.ec.etrustex.admin.old.web.utils.Referer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletContextAttributeExporter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"eu.europa.ec.etrustex.admin.old.web"}, useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "eu.europa.ec.etrustex.admin.old.web.validators.*"),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class)
        })
@PropertySource({"classpath:application.properties"})
public class MvcConfig extends WebMvcConfigurerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserHandlerMethodArgumentResolver());
    }

    /**
     * Allows for mapping the DispatcherServlet to "/" by forwarding static resource requests to the container's default Servlet
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/, classpath:/META-INF/web-resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ICAMultiInterceptor()).addPathPatterns("/ica/multi/create*");
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"classpath:tiles3-defs.xml"});
//        tilesConfigurer.setCompleteAutoload(true);
        tilesConfigurer.setCheckRefresh(true);
        return tilesConfigurer;
    }

    @Bean
    public ViewResolver tilesViewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(TilesView.class);
        viewResolver.setOrder(0);
        return viewResolver;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setOrder(1);

        return viewResolver;
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:eu/europa/ec/cipa/admin/web/i18n/uitext", "classpath:messages/errors/applicationErrors");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public HandlerExceptionResolver customExceptionResolver() {
        return new DefaultExceptionResolver();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    /**
     * We use this session scoped bean to know if the navigation is coming from "Search" or "Creation" screens. For example, in the user module:
     * 1. Set its value property to "create" and "search" in the respective "/create" and "/search" handler methods of the UserController
     * 2. Add an input type hidden in user.jsp <input type="hidden" id="refererPage" value="${refererPage.action}"/>.
     * 3. In user.js do the check with $("#refererPage").val() == 'create') return to /create, otherwise to search
     *
     * @return
     */
    @Bean(name = "refererPage")
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Referer referer() {
        return new Referer();
    }

    /**
     * This is to expose the bean in jsp views
     */
    @Bean
    public ServletContextAttributeExporter servletContextAttributeExporter() {
        ServletContextAttributeExporter servletContextAttributeExporter = new ServletContextAttributeExporter();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("refererPage", referer());
        servletContextAttributeExporter.setAttributes(attributes);

        return servletContextAttributeExporter;
    }
}
