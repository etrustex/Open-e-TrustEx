/**
 * 
 */
package eu.europa.ec.etrustex.dao.exception;

/**
 * @author batrian
 *
 */
public class RecordInUseException extends RecordException {

	private static final long serialVersionUID = 1L;
	
	public RecordInUseException(String entityName, Long id) {
		super(entityName, id);
	}


	 
}
