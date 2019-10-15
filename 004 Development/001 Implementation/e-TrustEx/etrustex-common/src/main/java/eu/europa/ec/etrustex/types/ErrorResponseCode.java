package eu.europa.ec.etrustex.types;

public enum ErrorResponseCode {
	
	/**
	 * SOAP Fault response codes for synch processing
	 */
	
	TECHNICAL_ERROR(null, "Server Error", "ERR_MSG", "Server"),
	UNDEFINED_INCOMING_MESSAGE("message.undefined", "Undefined incoming message", "ERR_MSG", "Client"),
    AUTHENTICATION_FAILED("error.unauthorized", "Unauthorized Access", "ERR_MSG", "Client"),
	SOAP_VERSION_MISMATCH(null, "Soap Envelope Version Mismatch", "ERR_MSG", "VersionMismatch"),
	
	DUPLICATE_ENTITY("error.duplicate", "Hard business rule violated", "ERR_MSG", "Client"),
	INVALID_MESSAGE_ID("error.id", "Hard business rule violated", "ERR_MSG", "Client"),
	DOCUMENT_XSD_INVALID("error.xsd", "Hard business rule violated", "ERR_MSG", "Client"),
	HARD_BUSINESS_RULE_VIOLATION("error.hardrule", "Hard business rule violated", "ERR_MSG", "Client"),
		
	BINARY_ERROR("error.binary", "Hard business rule violated", "ERR_MSG", "Client"),	
	DELETE_DWR_NON_EXISTING_DOCUMENT("error.notexisting", "Hard business rule violated", "ERR_MSG", "Client"),
	DELETE_DWR_DOCUMENT_LINKED_TO_BUNDLE("error.linked", "Hard business rule violated", "ERR_MSG", "Client"),
	
	SLA_BINARY_SIZE_TOO_LARGE("sla.binary_size", "SLA rule violated", "ERR_MSG", "Client"),
	SLA_XML_SIZE_TOO_LARGE("sla.xml_size", "SLA rule violated", "ERR_MSG", "Client"),
	SLA_VOLUME_EXCEEDED("sla.volume", "SLA rule violated", "ERR_MSG", "Client"),
	
	//kept for compatibility with some clients
	DOCUMENT_ALREADY_EXISTS("3", "Document message ID already exists", "DOC_ID_EXISTS", "Client"),
	
	/**
	 * Response codes for the asynch. processing 
	 */
	
	CONFIGURATION_ERROR("0:0", "Configuration Error", "ERR_MSG", "Server"),
	DOCUMENT_PARENT_ID_NOT_EXIST("5", "Parent document ID does not exist.", "PARENT_ID_NOT_EXISTS", "Client"),
	DOCUMENT_VALIDATION_FAILED("4", "Hard business rule violated", "HARD_VIOLATION", "Client"),
	DOCUMENT_XSD_INVALID_ASYNC("4", "XSD validation Failed", "XSD_VAL_FAILED", "Client"),
	
	DOCUMENT_BUNDLE_DOCUMENT_REFERENCE_MUST_BE_UNIQUE("4", "DocumentBundle.DocumentReference must be unique", "BDL_HARD_VIOLATION","Client"),
	DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED("4", "Max. no. of wrappers authorised by the SLA policy exceeded", 
			"BDL_NUMER_OF_WRAPPERS_EXCEEDED", "Client"),
	DOCUMENT_BUNDLE_NON_EXISTING_DOCUMENT_REFERENCE("6", "Missing Attachment binary data", "BDL_MISSING_REFERENCE", "Client"),
	
	DOCUMENT_BUNDLE_JUST_ALREADY_EXISTS("3", "Message with the same CorrelationID has been sent by a different Originator" , "ERR_MSG", "Client"),
	DOCUMENT_BUNDLE_JUST_HARD_BR       ("4", "Hard business rule violated"		  ,"ERR_MSG", "Client"),
	DOCUMENT_BUNDLE_JUST_MISSING_ATT   ("6", "Missing Attachment binary data"    , "ERR_MSG", "Client"),
	
	INVOICE_REMINDER_ALREADY_EXISTS("6", "A Reminder ID has already been sent for the current invoice on this IssueDate", "ERR_MSG", "Client"),
	
	/**
	 * Submit Application Response Errors
	 */
	PARENT_DOCUMENT_IS_NOT_IN_SPECIFIC_STATE("6", "Parent document is not in specific state", "ERR_MSG", "Client"),
	
	REQUEST_XSD_VALIDATION_SYNC(null,"XSD validation Failed", "XSD_VAL_FAILED", "Client"),

	/**
	 * Response codes for SOAP/JMS
	 */

	REQUEST_ENTITY_TOO_LARGE("413","Request entity too large","","Client"),
	BAD_REQUEST("400", "Bad request", "", "Client"),
	UNAUTHORIZED("401", "Unauthorized", "", "Client");


	private String code;
	private String description;
	private String internalResponseCode;
	private String faultCode;
	
	ErrorResponseCode(String code, String description, String internalResponseCode, String faultCode) {
		this.code = code;
		this.description = description;
		this.internalResponseCode = internalResponseCode;
		this.faultCode = faultCode;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	public String getInternalResponseCode() {
		return internalResponseCode;
	}
	
	public String getFaultCode() {
		return faultCode;
	}
	
	public static ErrorResponseCode getByCode(String code) {
		ErrorResponseCode[] values = ErrorResponseCode.values();
		
		for (ErrorResponseCode value : values) {
			if (value.code != null && value.code.equals(code)) {
				return value;
			}
		}
		return null;
	}
	public static ErrorResponseCode getByInternalCode(String internalCode) {
		ErrorResponseCode[] values = ErrorResponseCode.values();
		
		for (ErrorResponseCode value : values) {
			if (value.internalResponseCode.equals(internalCode)) {
				return value;
			}
		}
		throw new RuntimeException("Unable to find the error code " + internalCode + ". Please update the class [eu.cec.ellipcis.domain.ErrorResponseCode] containing the error codes.");
	}
}
