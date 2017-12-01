package eu.europa.ec.cipa.etrustex.services.dao.admin;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.admin.User;
import eu.europa.ec.cipa.etrustex.services.dao.ITrustExDAO;

/**
 * 
 * @author batrian
 *
 */
public interface IUserDAO extends ITrustExDAO<User, Long>{

	public User getByName(String name);
	public User getByName(String name, String password);
	public List<User> getUsers(String username, Long userRoleId, Long businessdomainId, Long partyId);
	public List<User> getUsers(Long userRoleId, Long businessdomainId, Long partyId);
	public User create(User newUser);
	void flushEm();

}