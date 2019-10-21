/**
 * 
 */
package eu.europa.ec.etrustex.dao.exception;

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
