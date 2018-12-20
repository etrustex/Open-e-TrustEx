package eu.europa.ec.etrustex.types;

public enum ConfidentialityType {
	
	PUBLIC(0,"Public"),
	LIMITED_BASIC(1,"Limited Basic"),
	LIMITED_HIGH(2,"Limited High");
	
	private Integer level;
	private String description;
	
	ConfidentialityType(Integer level, String description) {
		this.level = level;
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public String getDescription() {
		return description;
	}
	
	public static ConfidentialityType getByLevel(Integer id) {
	    for(ConfidentialityType e : values()) {
	        if(e.level.equals(id)) return e;
	    }
	    return null;
	 }
}
