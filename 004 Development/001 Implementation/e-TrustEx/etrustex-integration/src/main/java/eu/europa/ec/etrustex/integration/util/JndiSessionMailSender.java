package eu.europa.ec.etrustex.integration.util;

import javax.annotation.PostConstruct;
import javax.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class JndiSessionMailSender extends JavaMailSenderImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(JndiSessionMailSender.class);
	
	@PostConstruct
	public void configure() {
		try {
			Session session = this.getSession();
			this.setPort(Integer.parseInt(((String)session.getProperties().get("mail.smtp.port")).trim()));
			this.setProtocol( ((String)session.getProperties().get("mail.transport.protocol")).trim());
			this.setHost(((String)session.getProperties().get("mail.smtp.host")).trim());
//			super.setSession(session);
			logger.info("The mail session has been correctly initialized on "+this.getProtocol()+"://"+this.getHost()+":"+this.getPort());
		} catch(Throwable e) {
			//TODO: to log
			logger.error("The mail session cannot be initialized." + e.getMessage());
	    }
	}
	
}