/**
 *
 */
package eu.europa.ec.etrustex.integration.util;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import eu.europa.ec.etrustex.integration.ejb.RoutingQueueMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jndi.JndiTemplate;

/**
 * @author chiricr
 *
 */
public class UserCredentialsConnectionFactoryAdapter
        extends org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter {

    private JndiTemplate jndiTemplate;

    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }

    private Logger logger = LoggerFactory.getLogger(UserCredentialsConnectionFactoryAdapter.class);

    @Override
    protected Connection doCreateConnection(String username, String password) throws JMSException {
        wlFix();
        return super.doCreateConnection(username, password);
    }

    /**
     * Associate JNDI variables (user and password) with this thread for the
     * benefit of the WL drivers.
     */
    protected void wlFix() {
        try {
            @SuppressWarnings("unused")
            InitialContext initialContext = new InitialContext(jndiTemplate.getEnvironment());
        } catch (NamingException e) {
            logger.error("Error calling  wlFix()",e);
        }
    }


}
