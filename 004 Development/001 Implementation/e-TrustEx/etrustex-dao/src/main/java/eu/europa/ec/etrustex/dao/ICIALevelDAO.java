/**
 * 
 */
package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.domain.CIALevel;

/**
 * @author batrian
 *
 */
public interface ICIALevelDAO extends ITrustExDAO<CIALevel, Long> {

	/**
	 * retrieve CIA level corresponding to the confidentiality, integrity, availability params 
	 * @param confidentiality the confidentiality level
	 * @param integrity the integrity level
	 * @param availability the availability level
	 * @return the CIALevel
	 */
	public List<CIALevel> retrieveCIALevel(Integer confidentiality, Integer integrity, Integer availability);
	
}
