package eu.europa.ec.etrustex.integration.util;

public class AmqpHeaders extends MessageHeaders {
	
	private String replyTo;
		
	public AmqpHeaders(String replyTo) {
		this.replyTo = replyTo;
	}
		
	@Override
	public String getReplyTo() {
		return replyTo;
	}
	
	
}
