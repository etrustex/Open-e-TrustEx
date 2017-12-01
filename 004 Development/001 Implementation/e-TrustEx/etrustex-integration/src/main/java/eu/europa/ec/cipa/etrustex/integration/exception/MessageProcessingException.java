/* 
 * Copyright 2010  EUROPEAN COMMISSION
 * 
 * Licensed under the EUPL, Version 1.1 only (the "License");	
 * 
 * You may not use this work except in compliance with the
 * License.
 * You may obtain a copy of the License at:
 * 
 * http://ec.europa.eu/idabc/en/document/7774
 * 
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
*/
package eu.europa.ec.cipa.etrustex.integration.exception;

import java.util.Locale;

import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;


/**
 * This is the exception Is throw in case of error during 
 * the synchronous invocation of services. the parameters passed
 * to the constructor are used to populate a FaultDocument xml bean
 * that is then serialized to the soap fault body returned by the webservice
 * in case of problem 
 **/
public class MessageProcessingException extends RuntimeException{ 

	private String fault;
	private String description;
	private Locale locale;
	private ErrorResponseCode faultDetailResponseCode;
	private String faultDetailDescription;
	private String documentTypeCode;
	
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public MessageProcessingException(String fault, String descr){
		this.fault = fault;
		this.description = descr;
	}
	
	public MessageProcessingException(String fault, String descr, Locale l,
			ErrorResponseCode faultDetailResponseCode,String documentTypeCode , String faultDetailDescription  ){
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
