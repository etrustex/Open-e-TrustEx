/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.Role;

/**
 * @author batrian
 *
 */
public interface IRoleDAO extends ITrustExDAO<Role, Long> {
	
	public void delete(Long id);
	
	public boolean isUnique(Role role);

	public boolean isUniqueCode(Role role);

	public boolean isUniqueName(Role role);
	
	public boolean isInUse(Role role);
	
	public List<Role> getRolesForProfile(Long profileId, Boolean technical);
	
	@Override
	public List<Role> getAll();
	
	public List<Role> getAllNonTechnical();
	
	/**
	 * retrieves the unique role with the given role code
	 * @param roleCode
	 * @return
	 */
	public Role findByCode(String roleCode);

}
