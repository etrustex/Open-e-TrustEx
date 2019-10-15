package eu.europa.ec.etrustex.common.exception;

import java.util.Locale;

import eu.europa.ec.etrustex.types.ErrorResponseCode;

public class TechnicalErrorException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fault;
	private String description;
	private Locale locale;
	private ErrorResponseCode faultDetailResponseCode = ErrorResponseCode.TECHNICAL_ERROR;
	private String faultDetailDescription;
	private String documentTypeCode;
	
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	
		
	public TechnicalErrorException() {
	}

	public TechnicalErrorException(String fault, String descr, Locale l,String documentTypeCode , String faultDetailDescription  ){
		this.fault = fault;
		this.description = descr;
		this.locale = l;
		this.faultDetailDescription = faultDetailDescription;
		this.documentTypeCode = documentTypeCode;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getFaultDetailDescription() {
		return faultDetailDescription;
	}

	public void setFaultDetailDescription(String faultDetailDescription) {
		this.faultDetailDescription = faultDetailDescription;
	}
	public ErrorResponseCode getFaultDetailResponseCode() {
		return faultDetailResponseCode;
	}

	public void setFaultDetailResponseCode(ErrorResponseCode faultDetailResponseCode) {
		this.faultDetailResponseCode = faultDetailResponseCode;
	}

}
