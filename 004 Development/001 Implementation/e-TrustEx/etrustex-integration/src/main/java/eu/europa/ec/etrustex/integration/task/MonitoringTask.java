package eu.europa.ec.etrustex.integration.task;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.domain.MonitoringQuery;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.util.JndiSessionMailSender;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.services.IMonitoringService;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class MonitoringTask extends AbstractJob {

	@Autowired
	private IMonitoringService monitoringService;
	
	@Autowired
	private JndiSessionMailSender mailSender;
	
	@Autowired
	public Configuration freemarkerConfig;
	
	@Autowired
	private IMetadataService metadataService;
		
	private final static String MONITORING_ALERT_EMAIL_TEMPLATE = "templates/monitoring_alert_email_template.flt";

	private static final Logger logger = LoggerFactory.getLogger(MonitoringTask.class);

	@Override
	@Transactional(readOnly = true)
    public void run() {
		List<MetaDataItem> metadataList = metadataService.getDefaultMetadataByType(MetaDataItemType.ENABLE_MONITORING.name());		
		boolean enableMonitoring = CollectionUtils.isNotEmpty(metadataList)	? Boolean.valueOf(metadataList.get(0).getValue()) : false;
    	if (!enableMonitoring) {
    		logger.info("Monitoring is not enabled");
    		return;
    	}
    	metadataList = metadataService.getDefaultMetadataByType(MetaDataItemType.MONITORING_EMAIL_RECIPIENTS.name());
        String emailTo = CollectionUtils.isNotEmpty(metadataList) ? metadataList.get(0).getValue() : null;
        if (emailTo == null) {
        	logger.error("No monitoring email recipient found");
        	return;
        }    	
        logger.info("Start MonitoringTask");
        List<MonitoringQuery> queriesWithAlerts = monitoringService.runMonitoringQueries();
        if (CollectionUtils.isEmpty(queriesWithAlerts)) {
        	return;
        }
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();        
		try {
			mimeMessage.setSubject("e-TrustEx Node automatic monitoring");
			mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(emailTo));
			mimeMessage.setText(generateEmailBody(queriesWithAlerts));
		} catch (MessagingException | IOException | TemplateException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}		
		mailSender.send(mimeMessage);        
        logger.info("Stop MonitoringTask");        
    }    
    
    private String generateEmailBody(List<MonitoringQuery> queries) throws IOException, TemplateException {
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("QUERIES", queries);
		Template temp = freemarkerConfig.getTemplate(MONITORING_ALERT_EMAIL_TEMPLATE);
		StringWriter sr = new StringWriter();
		temp.process(root, sr);
		return sr.toString();    	
    }
    
}
