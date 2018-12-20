package eu.europa.ec.etrustex.types;

public enum IntegrityType {
	
	MODERATE(0,"Moderate"),
	CRITICAL(1,"Critical"),
	STRATEGIC(2,"Strategic");
	
	private Integer level;
	private String description;
	
	IntegrityType(Integer level, String description) {
		this.level = level;
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public String getDescription() {
		return description;
	}
	
	public static IntegrityType getByLevel(Integer id) {
	    for(IntegrityType e : values()) {
	        if(e.level.equals(id)) return e;
	    }
	    return null;
	 }
}
