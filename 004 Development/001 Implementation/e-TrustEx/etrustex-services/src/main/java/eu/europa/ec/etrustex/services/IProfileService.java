/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;

/**
 * @author batrian
 *
 */
public interface IProfileService {

	public Profile getProfile(Long id);
	public Profile create(Profile profile);
	public Profile update(Profile profile);
	public void delete(Long id);
	public List<Profile> getAllProfiles();
	public List<Profile> findProfilesByCriteria(String name, String transactionName, BusinessDomain businessDomain);
	public Boolean isInUse(Profile profile);
    Profile findByName(String name);
}
