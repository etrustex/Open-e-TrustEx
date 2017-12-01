/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.exception;

/**
 * @author batrian
 *
 */
public class RecordExistsException extends RecordException {

	private static final long serialVersionUID = 1L;
	
	public RecordExistsException(String entityName) {
		super(entityName, null);
	}
}
