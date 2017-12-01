/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.exception;

/**
 * @author batrian
 *
 */
public class RecordNotFoundException extends RecordException {

	private static final long serialVersionUID = 1L;
	
	public RecordNotFoundException(String entityName, Long id) {
		super(entityName, id);
	}


	 
}
