package eu.europa.ec.etrustex.services.admin;

import java.util.List;

import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.domain.admin.UserRole;

/**
 * 
 * @author batrian
 *
 */
public interface IUserService {

	public User getUser(Long id);
	public UserRole getUserRole(String username, Long domainId);
    public List<PartyListItemDTO> getUserParties(String username, Long domainId);


	public List<BusinessDomain> getUserBusinessDomains(User user);
	public User getUser(String username);

    public User getUser(String username, String password);
	public User create(User newUser);

    public User update(User modifiedUser, List<Long> userAccessRightsToDelete);
    public User changePwd(User modifiedUser);
	public void delete(User user);

    public List<User> getUsers(String username, Long userRoleId, Long businessDomainId, Long partyId);
    public Boolean exists(String username);

    // TODO None of the methods below is used. Delete them?
    public List<User> getUsers(Long userRoleId, Long businessDomainId, Long partyId);
    public List<BusinessDomain> getUserBusinessDomains(String username);
    public List<UserAccessRights> getUserAccessRights(String username);
    public List<UserAccessRights> getUserAccessRights(Long userId);
    public List<BusinessDomain> getUserBusinessDomains(Long userId);
}
