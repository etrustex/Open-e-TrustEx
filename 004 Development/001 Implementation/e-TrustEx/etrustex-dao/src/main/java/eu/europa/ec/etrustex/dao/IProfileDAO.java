/**
 * 
 */
package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

import java.util.List;

/**
 * @author batrian
 *
 */
public interface IProfileDAO extends ITrustExDAO<Profile, Long> {
		
	
	public Profile getProfile(Long id);

	/**
	 * @author batrian
	 * @param profile
	 * @return all business domains that are associated with the specified profile
	 */
	public List<Profile> findProfilesByCriteria(String name, String transactionName, BusinessDomain businessDomain);
	public Boolean isInUse(Profile profile);
	Profile findByName(String name);
}
