package eu.europa.ec.cipa.etrustex.types;


/**
 * 
 * @author zhariil
 *
 */
public enum LogicalOperatorType {
	AND("and"),
	OR("or"),
	NOT("not"),
	BETWEEN ("between"),
	IN("in"),
	EQUAL("equal"),
	NOT_EQUALS("notEqual"),
	GREATER_THAN("greaterThan"),
	LESS_THAN("lessThan"),
	IS_EMPTY("isEmpty");
	
	/**
	 * that field should correspond exactly to one of the Hibernate Criteria API restrictions
	 * @see org.hibernate.criterion.Restrictions 
	 */
	public String restriction;
	
	LogicalOperatorType(String z) {
		this.restriction = z;
	}
}
