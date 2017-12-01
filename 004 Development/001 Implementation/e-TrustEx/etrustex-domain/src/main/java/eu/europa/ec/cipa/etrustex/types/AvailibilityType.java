package eu.europa.ec.cipa.etrustex.types;

public enum AvailibilityType {
	
	MODERATE(0,"Moderate"),
	CRITICAL(1,"Critical"),
	STRATEGIC(2,"Strategic");
	
	private Integer level;
	private String description;
	
	AvailibilityType(Integer level, String description) {
		this.level = level;
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public String getDescription() {
		return description;
	}
	
	public static AvailibilityType getByLevel(Integer id) {
	    for(AvailibilityType e : values()) {
	        if(e.level.equals(id)) return e;
	    }
	    return null;
	 }
}
