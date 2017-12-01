/**
 * 
 */
package eu.europa.ec.cipa.admin.web.utils;

/**
 * @author batrian
 *
 */
public class FormUtil {

	/**
	 * Returns null if the selected option from view select list is the user hint.
	 * @param value - the value received from the view
	 * @return
	 * 		- null, in case the selected option is the hint
	 * 		- value (the id of the selected option), otherwise
	 */
	public static Long convertDefaultOptionToNull(Long value) {
		if (value == null || value.longValue() == -1) {
			return null;
		} else { 
			return value;
		}
	}

}
