package eu.europa.ec.cipa.etrustex.domain.util;

import java.util.Arrays;
import java.util.List;

import eu.europa.ec.cipa.etrustex.types.LogicalOperatorType;



public class SearchCriteria extends SearchExpression {
	private String attributeName;
	private Boolean valueIsAttribute = false;
	
	public SearchCriteria() {
		attributeName = "";
	}
	
	public SearchCriteria(String n, LogicalOperatorType o, List<Object> v) {
		super(o, v);
		this.attributeName = n;
	}

	public SearchCriteria(String n, LogicalOperatorType o, Object value) {
		super(o, Arrays.asList(new Object[]{value}));
		this.attributeName = n;
		this.valueIsAttribute = true;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	public boolean valueIsAttribute() {
		return this.valueIsAttribute != null ? this.valueIsAttribute.booleanValue() : false;
	}
	public Boolean getValueIsAttribute() {
		return this.valueIsAttribute;
	}
	public void setValueIsAttribute(Boolean v) {
		this.valueIsAttribute = v;
	}
	
	public String toString() {
		return this.attributeName + " " + super.toString();
	}
}
