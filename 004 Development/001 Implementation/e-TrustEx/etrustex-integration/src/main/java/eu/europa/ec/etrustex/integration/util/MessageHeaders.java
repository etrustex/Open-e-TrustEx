package eu.europa.ec.etrustex.integration.util;

public abstract class MessageHeaders {
	
	protected String correlationId;
	protected String responseCode;
	protected int priority;
	
	public static final String HEADER_RESPONSE_CODE = "RESPONSE_CODE";

	public String getCorrelationId() {		
		return correlationId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public abstract Object getReplyTo();
}
