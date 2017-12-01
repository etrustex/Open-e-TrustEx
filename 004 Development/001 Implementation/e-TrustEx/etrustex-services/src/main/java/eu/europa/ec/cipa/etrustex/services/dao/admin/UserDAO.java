/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao.admin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.admin.User;
import eu.europa.ec.cipa.etrustex.domain.admin.User_;
import eu.europa.ec.cipa.etrustex.services.dao.TrustExDAO;
import eu.europa.ec.cipa.etrustex.services.exception.InconsistentDatabaseException;

/**
 * @author batrian
 *
 */
@Repository
public class UserDAO extends TrustExDAO<User, Long> implements IUserDAO {

	@Override
	public User getByName(String name) {
		List<User> users = entityManager
				.createQuery("from User user where user.name = :name", User.class)
				.setParameter("name", name)
				.getResultList();
		
		if (CollectionUtils.isNotEmpty(users)){
			if (users.size() > 1){
				throw new InconsistentDatabaseException("There are more than one user having the same username", "User");
			} else {
				return users.get(0);
			}
		} else {
			return null;
		}
	}

	@Override
	public User getByName(String name, String password) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);
		cq.select(user); 
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate predicate = null;
		
		predicate = cb.equal(cb.lower(user.get(User_.name)),
				StringUtils.lowerCase(name));
		predicates.add(predicate);
		
		predicate = cb.equal(user.get(User_.password),
				password);
		predicates.add(predicate);
		
		Predicate[] preds = new Predicate[predicates.size()];
		Predicate clause = cb.and(predicates.toArray(preds));
		cq.where(clause);
		TypedQuery<User> query = entityManager.createQuery(cq);
		
		List<User> results = query.getResultList();
		if (CollectionUtils.isNotEmpty(results)){
			return results.get(0);
		}
		return null;
	}

	@Override
	public List<User> getUsers(String username, Long userRoleId,
			Long businessdomainId, Long partyId) {
		String queryString = "SELECT distinct user FROM User as user ";

		if (userRoleId != null 
				|| businessdomainId != null 
				|| partyId != null) {
			queryString += "join user.accessRights as accessRights ";
		}
		
		boolean noCondition = true;

		if (StringUtils.isNotBlank(username)){
			queryString += "where UPPER(user.name) like UPPER(:username) ";
			noCondition = false;
		}

		if (userRoleId != null) {
			if (noCondition){
				queryString += "where accessRights.role.id = :userRoleId ";
				noCondition = false;
			} else {
				queryString += "and accessRights.role.id = :userRoleId ";
			}
		}
	
		if (businessdomainId != null ) {
			if (noCondition){
				queryString += "where accessRights.businessDomain.id = :businessdomainId ";
				noCondition = false;
			} else {
				queryString += "and accessRights.businessDomain.id = :businessdomainId ";
			}
		}

		if (partyId != null ) {
			if (noCondition){
				queryString += "where accessRights.party.id = :partyId ";
				noCondition = false;
			} else {
				queryString += "and accessRights.party.id = :partyId ";
			}
		}

		queryString += "order by UPPER(user.name)";
		
		TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
		
		if (StringUtils.isNotBlank(username)){
			query.setParameter("username", "%" + username.trim() + "%");
		}
		
		if (userRoleId != null){
			query.setParameter("userRoleId", userRoleId);
		}
		
		if (businessdomainId != null){
			query.setParameter("businessdomainId", businessdomainId);
		}
		
		if (partyId != null){
			query.setParameter("partyId", partyId);
		}
		
		return query.getResultList();
	}
	
	@Override
	public List<User> getUsers(Long userRoleId,
			Long businessdomainId, Long partyId) {
		String queryString = "SELECT distinct user FROM User as user ";

		if (userRoleId != null 
				|| businessdomainId != null 
				|| partyId != null) {
			queryString += "join user.accessRights as accessRights ";
		}
		
		boolean noCondition = true;

		if (userRoleId != null) {
			queryString += "where accessRights.role.id = :userRoleId ";
			noCondition = false;
		}
	
		if (businessdomainId != null ) {
			if (noCondition){
				queryString += "where accessRights.businessDomain.id = :businessdomainId ";
				noCondition = false;
			} else {
				queryString += "and accessRights.businessDomain.id = :businessdomainId ";
			}
		}

		if (partyId != null ) {
			if (noCondition){
				queryString += "where accessRights.party.id = :partyId ";
				noCondition = false;
			} else {
				queryString += "and accessRights.party.id = :partyId ";
			}
		} else {
			if (noCondition){
				queryString += "where accessRights.party.id is null ";
				noCondition = false;
			} else {
				queryString += "and accessRights.party.id is null ";
			}
		}

		TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
		
		if (userRoleId != null){
			query.setParameter("userRoleId", userRoleId);
		}
		
		if (businessdomainId != null){
			query.setParameter("businessdomainId", businessdomainId);
		}
		
		if (partyId != null){
			query.setParameter("partyId", partyId);
		}
		
		return query.getResultList();
	}
	
	@Override
	public User create(User user){
		return super.create(user);
	}
	
	
	@Override
	public void flushEm(){
		entityManager.flush();
	}
}
