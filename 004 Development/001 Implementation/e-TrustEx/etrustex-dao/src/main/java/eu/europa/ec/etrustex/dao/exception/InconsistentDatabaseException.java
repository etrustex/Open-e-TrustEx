/**
 * 
 */
package eu.europa.ec.etrustex.dao.exception;

/**
 * @author batrian
 *
 */
public class InconsistentDatabaseException extends RuntimeException  {

	private static final long serialVersionUID = 1L;
	
	private final String entityName;
	
	public InconsistentDatabaseException(String entityName) {
		super();
    	this.entityName = entityName;
	}
	
	public InconsistentDatabaseException(String message, String entityName) {  
    	super(message);
    	this.entityName = entityName;
    }
    
    public InconsistentDatabaseException(Throwable throwable, String entityName) {  
    	super(throwable);
    	this.entityName = entityName;
    }
    
    public InconsistentDatabaseException(String message, Throwable throwable, String entityName) {  
    	super(message, throwable);
    	this.entityName = entityName;
    }
    
    public String getEntityName(){
    	return this.entityName;
    }
     


	 
}
