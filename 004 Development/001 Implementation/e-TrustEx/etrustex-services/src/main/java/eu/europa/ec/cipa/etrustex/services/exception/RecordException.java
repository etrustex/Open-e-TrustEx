/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.exception;

/**
 * @author batrian
 *
 */
public class RecordException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String entityName;
	private final Long entityId; 
    
	public RecordException(String entityName, Long id) {  
    	super();
    	this.entityName = entityName;
    	this.entityId = id;  
    }
    
    public RecordException(String message, String entityName, Long id) {  
    	super(message);
    	this.entityName = entityName;
    	this.entityId = id;  
    }
    
    public RecordException(Throwable throwable, String entityName, Long id) {  
    	super(throwable);
    	this.entityName = entityName;
    	this.entityId = id;  
    }
    
    public RecordException(String message, Throwable throwable, String entityName, Long id) {  
    	super(message, throwable);
    	this.entityName = entityName;
    	this.entityId = id;  
    }
    
    public Long getId(){
    	return this.entityId;
    }
    
    public String getEntityName(){
    	return this.entityName;
    }
     
}
