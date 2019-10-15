/**
 *
 */
package eu.europa.ec.etrustex.integration.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MessageEndpoint;

/**
 * @author chiricr
 *
 */
@Component("jmsMessageEndpoint")
public class JmsMessageEndpoint implements MessageEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(JmsMessageEndpoint.class);

	@Autowired
	private ETrustEXSoapInboundGateway etrustExSubmitSoapEndpoint;

	/* (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.MessageEndpoint#invoke(org.springframework.ws.context.MessageContext)
	 */
	@Override
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void invoke(MessageContext messageContext) throws Exception {
		try {
			etrustExSubmitSoapEndpoint.invoke(messageContext);
		} catch (Exception e) {
			logger.error("etrustExSubmitSoapEndpoint error",e);
		}

	}

}
