/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.CIALevel;

/**
 * @author batrian
 *
 */
public interface ICIALevelDAO extends ITrustExDAO<CIALevel, Long> {

	public List<CIALevel> retrieveCIALevel(Integer confidentiality, Integer integrity, Integer availability);
	
}
