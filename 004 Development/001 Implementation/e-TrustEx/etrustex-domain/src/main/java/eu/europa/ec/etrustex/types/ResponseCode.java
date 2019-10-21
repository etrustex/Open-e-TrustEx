package eu.europa.ec.etrustex.types;

public enum ResponseCode {
	
	DOCUMENT_BUNDLE_JUST_OK("8", "Notification: processing ok"),
	NOTIFICATION_AVAILABLE("1", "Available");
	
	private String code;
	private String description;
	
	ResponseCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
