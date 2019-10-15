package eu.europa.ec.etrustex.dao.admin;

import java.util.List;

import eu.europa.ec.etrustex.dao.ITrustExDAO;
import eu.europa.ec.etrustex.domain.admin.User;

/**
 * 
 * @author batrian
 *
 */
public interface IUserDAO extends ITrustExDAO<User, Long>{

	public User getByName(String name);
	public User getByName(String name, String password);
	public List<User> getUsers(String username, Long userRoleId, Long businessdomainId, Long partyId);
	public User create(User newUser);
	void flushEm();

}