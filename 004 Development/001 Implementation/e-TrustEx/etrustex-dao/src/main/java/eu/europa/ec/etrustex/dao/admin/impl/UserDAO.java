/**
 * 
 */
package eu.europa.ec.etrustex.dao.admin.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.admin.IUserDAO;
import eu.europa.ec.etrustex.dao.exception.InconsistentDatabaseException;
import eu.europa.ec.etrustex.dao.impl.TrustExDAO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Party_;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights_;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import eu.europa.ec.etrustex.domain.admin.UserRole_;
import eu.europa.ec.etrustex.domain.admin.User_;

/**
 * @author batrian
 *
 */
@Repository
public class UserDAO extends TrustExDAO<User, Long> implements IUserDAO {

	@Override
	public User getByName(String name) {
		List<User> users = entityManager
				.createQuery("from eu.europa.ec.etrustex.domain.admin.User user where UPPER(user.name) = :name", User.class)
				.setParameter("name", name.toUpperCase())
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
	
	
//	public List<User> getUsers2(String username, Long userRoleId, Long businessdomainId, Long partyId) {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<User> cq = cb.createQuery(User.class);
//		Root<eu.europa.ec.etrustex.domain.admin.User> myUser = cq.from(eu.europa.ec.etrustex.domain.admin.User.class);
//		cq.select(myUser);
//		
//		SetJoin<User, UserAccessRights>  accessRight = myUser.join(User_.accessRights);
//		
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		Predicate predicate = null;
//		
//		if(StringUtils.isNotEmpty(username)){
//			predicate = cb.like(cb.upper(myUser.get(User_.name)),'%'+StringUtils.upperCase(username.trim())+'%');
//			predicates.add(predicate);
//		}
//		
//		if(userRoleId != null){
//			Join<UserAccessRights, UserRole> urole = accessRight.join(UserAccessRights_.role);
//			predicate = cb.equal(urole.get(UserRole_.id), userRoleId);
//			predicates.add(predicate);
//		}
//		
//		if(businessdomainId != null){
//			Join<UserAccessRights, BusinessDomain> bd = accessRight.join(UserAccessRights_.businessDomain);
//			predicate = cb.equal(bd.get(BusinessDomain_.id), businessdomainId);
//			predicates.add(predicate);
//		}
//		
//		if(partyId != null){
//			Join<UserAccessRights, Party> party = accessRight.join(UserAccessRights_.party);
//			predicate = cb.equal(party.get(Party_.id), partyId);
//			predicates.add(predicate);
//		}
//		
//		Predicate[] preds = new Predicate[predicates.size()];
//		Predicate clause = cb.and(predicates.toArray(preds));
//		cq.where(clause);
//		cq.orderBy(cb.desc(cb.upper(myUser.get(User_.name))));
//		TypedQuery<User> query = entityManager.createQuery(cq);
//		
//		return query.getResultList();
//	}
	
	@Override
	public List<User> getUsers(String username, Long userRoleId,
			Long businessdomainId, Long partyId) {
		String queryString = "SELECT distinct user FROM eu.europa.ec.etrustex.domain.admin.User as user ";

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
	public User create(User user){
		return super.create(user);
	}
	
	
	@Override
	public void flushEm(){
		entityManager.flush();
	}
}
