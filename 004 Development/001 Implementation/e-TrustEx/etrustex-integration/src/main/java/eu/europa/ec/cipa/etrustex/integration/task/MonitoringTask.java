package eu.europa.ec.cipa.etrustex.integration.task;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import eu.europa.ec.cipa.etrustex.domain.MonitoringQuery;
import eu.europa.ec.cipa.etrustex.integration.util.JndiSessionMailSender;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.IMonitoringService;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;


public class MonitoringTask {

	@Autowired
	private IMonitoringService monitoringService;
	
	@Autowired
	private JndiSessionMailSender mailSender;
	
	@Autowired
	public Configuration freemarkerConfig;
	
	@Autowired
	private IMetadataService metadataService;
	
	private Boolean enableMonitoring;
	
	private final static String MONITORING_ALERT_EMAIL_TEMPLATE = "templates/monitoring_alert_email_template.flt";

	private static final Logger logger = LoggerFactory.getLogger(MonitoringTask.class);

    //@Scheduled(cron = "0 0/1 * * * ?") //every 5 minutes
    @Scheduled(cron = "0 0 6 * * ?") //run at 6 AM every day
    public void run() {
    	if (BooleanUtils.isNotTrue(enableMonitoring)) {
    		return;
    	}
        logger.info("Start MonitoringTask");
        List<MonitoringQuery> queriesWithAlerts = monitoringService.runMonitoringQueries();
        if (CollectionUtils.isEmpty(queriesWithAlerts)) {
        	return;
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        String to = metadataService.getDefaultMetadataByType(MetaDataItemType.MONITORING_EMAIL_RECIPIENTS.name()).get(0).getValue();        
        try {
        	mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
			mimeMessage.setText(generateEmailBody(queriesWithAlerts));
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
        
        logger.info("Stop MonitoringTask");        
    }    
    
    private String generateEmailBody(List<MonitoringQuery> queries) throws Exception {
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("QUERIES", queries);
		Template temp = freemarkerConfig.getTemplate(MONITORING_ALERT_EMAIL_TEMPLATE);
		StringWriter sr = new StringWriter();
		temp.process(root, sr);
		return sr.toString();    	
    }

	public void setEnableMonitoring(Boolean enableMonitoring) {
		this.enableMonitoring = enableMonitoring;
	}
    
}
