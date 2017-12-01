package eu.europa.ec.cipa.etrustex.integration.exception;

import java.util.Locale;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class MessageRoutingException extends Exception {


	private static final long serialVersionUID = 1L;
	private String fault;
	private String description;
	private Locale locale;
	private ErrorResponseCode faultDetailResponseCode;
	private String faultDetailDescription;
	private String documentTypeCode;
	
	public MessageRoutingException() {
		super();
	}
	public MessageRoutingException(String message, Throwable cause) {
		super(message, cause);
	}
	public MessageRoutingException(String message) {
		super(message);
	}
	public MessageRoutingException(Throwable cause) {
		super(cause);
	}
	public MessageRoutingException(String fault, String descr, Locale l,
			ErrorResponseCode faultDetailResponseCode, String documentTypeCode,
			String faultDetailDescription) {
		this.fault = fault;
		this.description = descr;
		this.locale = l;
		this.faultDetailDescription = faultDetailDescription;
		this.faultDetailResponseCode = faultDetailResponseCode;
		this.documentTypeCode = documentTypeCode;
	}
	
	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ErrorResponseCode getFaultDetailResponseCode() {
		return faultDetailResponseCode;
	}

	public void setFaultDetailResponseCode(ErrorResponseCode faultDetailResponseCode) {
		this.faultDetailResponseCode = faultDetailResponseCode;
	}

	public String getFaultDetailDescription() {
		return faultDetailDescription;
	}

	public void setFaultDetailDescription(String faultDetailDescription) {
		this.faultDetailDescription = faultDetailDescription;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
}
