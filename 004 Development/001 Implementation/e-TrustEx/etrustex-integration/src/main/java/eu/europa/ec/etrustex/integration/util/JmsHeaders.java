/**
 * 
 */
package eu.europa.ec.etrustex.integration.util;

import javax.jms.Destination;

/**
 * @author chiricr
 *
 */
public class JmsHeaders extends MessageHeaders {
	
	private Destination destination;
	private Destination replyTo;
		
	
	public JmsHeaders(Destination destination, Destination replyTo, String correlationId) {
		this.destination = destination;
		this.replyTo = replyTo;
		this.correlationId = correlationId;
	}	
	
	public JmsHeaders() {}
	
	public Destination getDestination() {
		return destination;
	}
	public Destination getReplyTo() {
		return replyTo;
	}

	
}
