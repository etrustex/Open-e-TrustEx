package eu.europa.ec.cipa.etrustex.domain.util;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.cipa.etrustex.types.LogicalOperatorType;

public class SearchExpression {
	private LogicalOperatorType operation;
	private List<Object> values;
	
	public SearchExpression() {
		
	}
	
	public SearchExpression(LogicalOperatorType o, List<Object> v) {
		this.operation = o;
		this.values = new ArrayList<Object>(v);
	}
	
	public LogicalOperatorType getOperation() {
		return operation;
	}
	public void setOperation(LogicalOperatorType operation) {
		this.operation = operation;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
	
	public String toString() {
		return this.operation.restriction + (this.values != null ? " " + this.values : "");
	}
}
