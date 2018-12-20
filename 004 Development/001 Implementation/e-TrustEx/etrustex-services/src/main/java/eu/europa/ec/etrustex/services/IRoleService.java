/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.domain.Role;

/**
 * @author batrian
 *
 */
public interface IRoleService {

	/**
	 * Creates a new role in the database.
	 * @param role
	 * 		the role to create
	 * @return
	 * 		the created role
	 */
	public Role createRole(Role role);
	
	/**
	 * Deletes a role from database.
	 * @param id
	 * 		the id of the role to delete
	 * 
	 */
	public void deleteRole(Long id); 
	
	/**
	 * Updates a role.
	 * @param role
	 * 		the role to be updated
	 * @return
	 * 		the updated role
	 */
	public Role updateRole(Role role);
	
	/**
	 * Returns a list with all available roles.
	 * @return
	 */
	public List<Role> getAll();
	
	/**
	 * Returns a list with all available roles which are not technical roles.
	 * @return
	 */
	public List<Role> getAllNonTechnical();
	
	/**
	 * Returns the role corresponding to a given id.
	 * @param id
	 * @return
	 */
	public Role getRole(Long id);

	/**
	 * Checks if a role is unique.
	 * @param role
	 * 		the role to be checked
	 * @return
	 * 		true if there is no other role with the same code or name
	 * 		false otherwise
	 */
	public Boolean isUniqueRole(Role role);
	
	/**
	 * Checks if a role's code is unique.
	 * @param role
	 * 		the role to be checked
	 * @return
	 * 		true if there is no other role with the same code
	 * 		false otherwise
	 */
	public Boolean isUniqueRoleCode(Role role);
	
	/**
	 * Checks if a role's name is unique.
	 * @param role
	 * 		the role to be checked
	 * @return
	 * 		true if there is no other role with the same name
	 * 		false otherwise
	 */
	public Boolean isUniqueRoleName(Role role);

    @Transactional(propagation = Propagation.REQUIRED, readOnly=true)
    Set<Role> getRolesForBusinessDomain(Long businessDomainId);

    /**
	 * Checks if a role is in use. 
	 * @param role
	 * 		the role to check
	 * @return
	 * 		true if role is in used
	 * 		false otherwise
	 */
	public Boolean isInUse(Role role);
	
	/**
	 * Returns all roles corresponding to the given profile, identified by its
	 * id, by searching them in the profile's transactions. Will include or
	 * exclude technical roles depending on the includeTechnical parameter.
	 * 
	 * @param profileId
	 *            the profile id of the profile for which, the roles will be
	 *            retrieved
	 * @param includeTechnical
	 *            true, if the search should include technical roles; false, if
	 *            only roles that are not technical should be retrieved
	 * @return the roles corresponding to the given profile id, inlcuding or
	 *         excluding technical ones
	 */
	public List<Role> getRolesForProfile(Long profileId, Boolean includeTechnical);
	
	/**
	 * retrieves the unique role with the given role code
	 * @param roleCode
	 * @return
	 */
	public Role findByCode(String roleCode);
}
