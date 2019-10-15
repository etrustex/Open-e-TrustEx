package eu.europa.ec.etrustex.integration.config;

import eu.europa.ec.etrustex.common.Constants;
import eu.europa.ec.etrustex.integration.ejb.AmqpQueueMessageListener;
import eu.europa.ec.etrustex.integration.gateway.ETrustEXDefaultEndpoint;
import eu.europa.ec.etrustex.integration.gateway.JmsMessageEndpoint;
import eu.europa.ec.etrustex.integration.task.MonitoringTask;
import eu.europa.ec.etrustex.integration.task.RetentionPolicyTask;
import eu.europa.ec.etrustex.integration.util.*;
import eu.europa.ec.etrustex.services.IAuthenticationService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.config.ServicesConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ws.server.EndpointMapping;
import org.springframework.ws.server.endpoint.mapping.PayloadRootQNameEndpointMapping;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.server.SoapMessageDispatcher;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.mail.Session;
import javax.naming.Context;
import javax.sql.DataSource;
import java.text.ParseException;
import java.util.*;

@PropertySources({
		@PropertySource("classpath:/etrustex-integration.properties")
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration(value="integrationConfig")
@Import({ServicesConfig.class,InfrastructureConfig.class})
@EnableScheduling
@EnableTransactionManagement
public class IntegrationConfig {

	@Autowired
	private Environment prop;

	@Autowired
	@Qualifier("eTrustExDS")
	private DataSource eTrustExDS;
	@Autowired
	private SaajSoapMessageFactory messageFactory;
	@Autowired
	ETrustEXDefaultEndpoint defaultEndpoint;
	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private IAuthenticationService authenticationService;
	@Autowired
	private ILogService logService;
	@Autowired
	private JmsMessageEndpoint soapOverJmsMessageEndpoint;
	@Autowired
	private AmqpSenderService amqpSenderService;

	private UserCredentialsConnectionFactoryAdapter jmsConnectionFactory;

	@Bean("eventNotificationMessageConverter")
	public EventNotificationMessageConverter eventNotificationMessageConverter(){
		return new EventNotificationMessageConverter();
	}

	@Bean("freemarkerConfig")
	public FreeMarkerConfigurationFactoryBean freemarkerConfig(){
		FreeMarkerConfigurationFactoryBean fb = new FreeMarkerConfigurationFactoryBean();
		fb.setTemplateLoaderPath("classpath:/templates");
		Properties settings = new Properties();
		settings.setProperty("datetime_format"		, "dd/MM/yyyy");
		settings.setProperty("number_format"		, "#");
		settings.setProperty("whitespace_stripping"	, "true");
		fb.setFreemarkerSettings(settings);
		return fb;
	}

	@Bean("jmsJndiTemplate")
	public JndiTemplate jmsJndiTemplate(){
		JndiTemplate jndiTemplate = new JndiTemplate();
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, 	prop.getProperty(Context.INITIAL_CONTEXT_FACTORY));
		settings.setProperty(Context.PROVIDER_URL, 				prop.getProperty(Context.PROVIDER_URL));
		settings.setProperty(Context.SECURITY_PRINCIPAL, 		prop.getProperty(Context.SECURITY_PRINCIPAL));
		settings.setProperty(Context.SECURITY_CREDENTIALS, 		prop.getProperty(Context.SECURITY_CREDENTIALS));
		jndiTemplate.setEnvironment(settings);
		return jndiTemplate;
	}


	@Bean("jndiJmsConnectionFactory")
	public JndiObjectFactoryBean jndiJmsConnectionFactory(){
		JndiObjectFactoryBean cf = new JndiObjectFactoryBean();
		cf.setJndiTemplate(jmsJndiTemplate());
		cf.setJndiName(prop.getProperty("jms.connection.factory.name"));
		return cf;
	}

	@Bean("jmsConnectionFactory")
	public synchronized UserCredentialsConnectionFactoryAdapter jmsConnectionFactory(){
		if (jmsConnectionFactory != null) {
			return jmsConnectionFactory;
		}
		jmsConnectionFactory = new UserCredentialsConnectionFactoryAdapter();
		jmsConnectionFactory.setJndiTemplate(jmsJndiTemplate());
		jmsConnectionFactory.setTargetConnectionFactory((ConnectionFactory)jndiJmsConnectionFactory().getObject());
		jmsConnectionFactory.setUsername(prop.getProperty(Context.SECURITY_PRINCIPAL));
		jmsConnectionFactory.setPassword(prop.getProperty(Context.SECURITY_CREDENTIALS));
		return jmsConnectionFactory;
	}

	//ActiveMQ

	@Bean("trustexMessageConverter")
	public TrustExMessageConverter trustexMessageConverter(){
		return new TrustExMessageConverter();
	}

	@Bean("documentEndpointMapping")
	public PayloadRootQNameEndpointMapping documentEndpointMapping(){
		PayloadRootQNameEndpointMapping endpointMapping = new PayloadRootQNameEndpointMapping();
		Map<String, Object> endpointMap = new HashMap<>();
		endpointMap.put("{ec:services:wsdl:Document-1}SubmitDocumentRequest", soapOverJmsMessageEndpoint);
		endpointMapping.setEndpointMap(endpointMap);
		endpointMapping.setDefaultEndpoint(defaultEndpoint);
		return endpointMapping;
	}


	@Bean("soapOverJmsMessageDispatcher")
	public SoapMessageDispatcher soapOverJmsMessageDispatcher(){
		SoapMessageDispatcher md = new SoapMessageDispatcher();
		List<EndpointMapping> endpointMappings = new ArrayList<>();
		endpointMappings.add(documentEndpointMapping());
		md.setEndpointMappings(endpointMappings);
		return md;
	}

	@Bean("loggingInterceptor")
	public LoggingInterceptor loggingInterceptor(){
		return new LoggingInterceptor();
	}


	//TEMPLATES
	@Bean("replyJmsTemplate")
	public JmsTemplate replyJmsTemplate(){
		JmsTemplate tpl = new JmsTemplate();
		tpl.setConnectionFactory(jmsConnectionFactory());
		tpl.setReceiveTimeout(30000);
		tpl.setMessageConverter(trustexMessageConverter());
		return tpl;
	}

	@Bean("toDocumentQueueTemplate")
	public JmsTemplate toDocumentQueueTemplate(){
		JmsTemplate tpl = new JmsTemplate();
		tpl.setConnectionFactory(jmsConnectionFactory());
		tpl.setReceiveTimeout(30000);
		tpl.setDefaultDestination((Destination)documentQueue().getObject());
		tpl.setMessageConverter(eventNotificationMessageConverter());
		return tpl;
	}

	//JavaMail setup 
	@Bean("mailSession")
	public JndiObjectFactoryBean mailSession(){
		JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
		fb.setJndiName(prop.getProperty("mail.session.name"));
		return fb;
	}

	@Bean("mailSender")
	public JndiSessionMailSender mailSender(){
		JndiSessionMailSender ms = new JndiSessionMailSender();
		ms.setSession((Session)mailSession().getObject());
		return ms;
	}

	//QUEUES
	@Bean("toAsynchProcessingQueue")
	public JndiObjectFactoryBean toAsynchProcessingQueue(){
		JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
		fb.setJndiName(prop.getProperty("integrationqueue.name"));
		return fb;
	}

	@Bean("waitingRoomQueue")
	public JndiObjectFactoryBean waitingRoomQueue(){
		JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
		fb.setJndiName(prop.getProperty("waitingroomqueue.name"));
		return fb;
	}

	@Bean("errorQueue")
	public JndiObjectFactoryBean errorQueue(){
		JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
		fb.setJndiName(prop.getProperty("errorqueue.name"));
		return fb;
	}

	@Bean("routingQueue")
	public JndiObjectFactoryBean routingQueue(){
		JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
		fb.setJndiName(prop.getProperty("routingqueue.name"));
		return fb;
	}

	@Bean("documentQueue")
	public JndiObjectFactoryBean documentQueue(){
		JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
		fb.setJndiName(prop.getProperty("documentqueue.name"));
		return fb;
	}

	@Bean("toAsynchProcessingTemplate")
	public JmsTemplate toAsynchProcessingTemplate(){
		JmsTemplate tpl = new JmsTemplate();
		tpl.setConnectionFactory(jmsConnectionFactory());
		tpl.setDefaultDestination((Destination)toAsynchProcessingQueue().getObject());
		tpl.setReceiveTimeout(30000);
		tpl.setMessageConverter(trustexMessageConverter());
		tpl.setExplicitQosEnabled(true);
		return tpl;
	}

	@Bean("toErrorQueueTemplate")
	public JmsTemplate toErrorQueueTemplate(){
		JmsTemplate tpl = new JmsTemplate();
		tpl.setConnectionFactory(jmsConnectionFactory());
		tpl.setDefaultDestination((Destination)errorQueue().getObject());
		tpl.setReceiveTimeout(30000);
		tpl.setMessageConverter(trustexMessageConverter());
		tpl.setExplicitQosEnabled(true);
		return tpl;
	}

	@Bean("toWaitingRoomTemplate")
	public JmsTemplate toWaitingRoomTemplate(){
		JmsTemplate tpl = new JmsTemplate();
		tpl.setConnectionFactory(jmsConnectionFactory());
		tpl.setDefaultDestination((Destination)waitingRoomQueue().getObject());
		tpl.setReceiveTimeout(30000);
		tpl.setMessageConverter(trustexMessageConverter());
		tpl.setExplicitQosEnabled(true);
		return tpl;
	}

	@Bean("toRoutingQueueTemplate")
	public JmsTemplate toRoutingQueueTemplate(){
		JmsTemplate tpl = new JmsTemplate();
		tpl.setConnectionFactory(jmsConnectionFactory());
		tpl.setDefaultDestination((Destination)routingQueue().getObject());
		tpl.setReceiveTimeout(30000);
		tpl.setMessageConverter(trustexMessageConverter());
		tpl.setExplicitQosEnabled(true);
		return tpl;
	}

	//Quartz CONFIG
	//Jobs
	@Bean(Constants.QUARTZ_RET_POLICY_JOB_NAME)
	public JobDetail retentionJob(){
		JobDetail job = JobBuilder.newJob(RetentionPolicyTask.class)
				.withIdentity(Constants.QUARTZ_RET_POLICY_JOB_NAME, Constants.QUARTZ_JOB_GROUP)
				.storeDurably()
				.requestRecovery()
				.build();
		return job;
	}
	@Bean(Constants.QUARTZ_MONITORING_JOB_NAME)
	public JobDetail monitoringJob(){
		JobDetail job = JobBuilder.newJob(MonitoringTask.class)
				.withIdentity(Constants.QUARTZ_MONITORING_JOB_NAME, Constants.QUARTZ_JOB_GROUP)
				.storeDurably()
				.requestRecovery()
				.build();
		return job;
	}

	@Bean("retentionTrigger") //Every Day at 5:30 am
	public Trigger retentionTrigger() throws ParseException{
		Trigger trg = org.quartz.TriggerBuilder.newTrigger()
				.withIdentity("retentionTrigger")
				.forJob(retentionJob())
				.withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("0 30 5 1/1 * ? *")))
				.build();
		return trg;
	}
	@Bean("monitoringTrigger") //Every day at 6 am
	public Trigger monitoringTrigger() throws ParseException{
		Trigger trg = org.quartz.TriggerBuilder.newTrigger()
				.withIdentity("monitoringTrigger")
				.forJob(monitoringJob())
				.withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("0 0 6 * * ?")))
				.build();
		return trg;
	}
	//Scheduler
	@Bean("quartzScheduler")
	public SchedulerFactoryBean quartzScheduler() throws ParseException{
		SchedulerFactoryBean fb = new SchedulerFactoryBean();
		fb.setConfigLocation(new ClassPathResource("quartz.properties"));
		fb.setDataSource(eTrustExDS);
		fb.setTransactionManager(txManager);
		fb.setSchedulerName("quartzScheduler1");
		fb.setOverwriteExistingJobs(true);
		fb.setAutoStartup(true);
		fb.setJobFactory(springBeanJobFactory());
		fb.setJobDetails(retentionJob(),monitoringJob());
		fb.setTriggers(retentionTrigger(),monitoringTrigger());
		return fb;
	}

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	/*
	 * ******************
	 * SECURITY
	 * ******************
	 */

	@Bean("keyStoreHandler")
	public KeyStoreCallbackHandler keyStoreHandler(){
		KeyStoreCallbackHandler ksh = new KeyStoreCallbackHandler();
		ksh.setTrustStore(trustStore().getObject());
		ksh.setKeyStore(keyStore().getObject());
		ksh.setPrivateKeyPassword(prop.getProperty("keyStoreHandler.privateKeyPassword"));
		return ksh;
	}

	@Bean("trustStore")
	public KeyStoreFactoryBean trustStore(){
		KeyStoreFactoryBean ts = new KeyStoreFactoryBean();
		ts.setLocation(new FileSystemResource(prop.getProperty("prop.location")+"/truststore.jks"));
		ts.setPassword(prop.getProperty("trustStore.password"));
		return ts;
	}

	@Bean("keyStore")
	public KeyStoreFactoryBean keyStore(){
		KeyStoreFactoryBean ts = new KeyStoreFactoryBean();
		ts.setLocation(new FileSystemResource(prop.getProperty("prop.location")+"/keystore.jks"));
		ts.setPassword(prop.getProperty("keyStore.password"));
		return ts;
	}

	@Bean("aesKeyStore")
	public KeyStoreFactoryBean aesKeyStore(){
		KeyStoreFactoryBean ts = new KeyStoreFactoryBean();
		ts.setLocation(new FileSystemResource(prop.getProperty("prop.location")+"/etxkeystore.jck"));
		ts.setPassword(prop.getProperty("aes.keyStore.password"));
		ts.setType("JCEKS");
		return ts;
	}
}
