/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.domain.Role_;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author batrian
 * 
 */
@Repository
public class RoleDAO extends TrustExDAO<Role, Long> implements IRoleDAO {

	@Override
	public void delete(Long id) {
		Role role = read(id);
		super.delete(role);
	}

	@Override
	public boolean isUnique(Role role) {

		String queryString = "from Role r "
							+ "where  UPPER(r.code) = UPPER(:code) "
								+ "or UPPER(r.name) = UPPER(:name) ";
		TypedQuery<Role> query = entityManager
				.createQuery(queryString, Role.class)
				.setParameter("code", role.getCode().trim())
				.setParameter("name", role.getName().trim());

		List<Role> results = query.getResultList();

		return CollectionUtils.isEmpty(results) ? true : false;
	}

	@Override
	public boolean isUniqueCode(Role role) {
		String queryString = "from Role r "
							+ "where  UPPER(r.code) = UPPER(:code) ";
		
		if (role.getId() != null){
			queryString += "and r.id <> :id";
		}
		
		TypedQuery<Role> query = entityManager
				.createQuery(queryString, Role.class)
				.setParameter("code", role.getCode().trim());

		if (role.getId() != null){
			query.setParameter("id", role.getId());
		}
		
		List<Role> results = query.getResultList();

		return CollectionUtils.isEmpty(results) ? true : false;
	}

	@Override
	public boolean isUniqueName(Role role) {
		String queryString = "from Role r "
				+ "where  UPPER(r.name) = UPPER(:name) ";
		
		if (role.getId() != null){
			queryString += "and r.id <> :id";
		}
		
		TypedQuery<Role> query = entityManager
				.createQuery(queryString, Role.class)
				.setParameter("name", role.getName().trim());
		
		if (role.getId() != null){
			query.setParameter("id", role.getId());
		}
		
		List<Role> results = query.getResultList();
		
		return CollectionUtils.isEmpty(results) ? true : false;
	}
	
	@Override
	public boolean isInUse(Role role){
		
		Boolean isInUse = false;
		
		String transactionQuery = "SELECT COUNT(trx) FROM Transaction trx "
				+ "WHERE trx.senderRole.id = :roleId "
				+ 	 "OR trx.receiverRole.id = :roleId ";
		
		String partyRoleQuery = "SELECT COUNT(pr) FROM PartyRole pr "
				+ "WHERE pr.role.id = :roleId ";
		
		Long trxNo = entityManager.createQuery(transactionQuery, Long.class)
				.setParameter("roleId", role.getId())
				.getSingleResult();
		
		Long prNo = entityManager.createQuery(partyRoleQuery, Long.class)
				.setParameter("roleId", role.getId())
				.getSingleResult();
		
		if (trxNo != 0 || prNo != 0){
			isInUse = true;
		}
		
		return isInUse;
	} 
	
	@Override
	public List<Role> getRolesForProfile(Long profileId, Boolean includeTechnical){
		/*String queryString = "select distinct role from Profile p join p.transactions t join t.senderRole join t.receiverRole "
				+ "where  p.id = :profileId ";
		"select t.senderRole from Profile p join p.transactions t where p.id = :profileId "
		+ "union "
		+ "select t.receiverRole from Profile p join p.transactions t where p.id = :profileId ";*/
		
		String queryString = "select r from Role r where ";
		
		if (!includeTechnical){
			queryString += "(r.technicalFlag = 0 or r.technicalFlag is NULL) and ";
		}
		
		queryString += "(r.id in (select t.senderRole.id from Profile p join p.transactions t where p.id = :profileId) " +
				"or r.id in (select t.receiverRole.id from Profile p join p.transactions t where p.id = :profileId)) ";

		/*
		select t.tra_rec_rol_id
		  from ETR_TB_PROFILE p 
		    join ETR_TB_PROFILE_TRANSACTION pt on p.pro_id = pt.ptr_pro_id
		    join etr_tb_transaction t on t.tra_id = pt.ptr_tra_id
		  where p.pro_id = :profileId 
		union 
		select t.tra_sen_rol_id
		  from ETR_TB_PROFILE p 
		    join ETR_TB_PROFILE_TRANSACTION pt on p.pro_id = pt.ptr_pro_id
		    join etr_tb_transaction t on t.tra_id = pt.ptr_tra_id
		  where p.pro_id = :profileId 
		 */
		
		queryString += "order by UPPER(r.name)";
		
		List<Role> roles = entityManager.createQuery(queryString, Role.class)
				.setParameter("profileId", profileId).getResultList();

		return roles;
	}
	
	@Override
	public List<Role> getAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
		Root<Role> r = criteriaQuery.from(Role.class);
		criteriaQuery.orderBy(builder.asc(builder.lower(r.get(Role_.name))));
		TypedQuery<Role> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}

	@Override
	public List<Role> getAllNonTechnical() {
		String queryString = "select r from Role r " + 
				"where  r.technicalFlag = 0 or r.technicalFlag = null order by UPPER(r.name)";
		return entityManager.createQuery(queryString, Role.class).getResultList();
	}
	
	@Override
	public Role findByCode(String roleCode) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
		Root<Role> role = criteriaQuery.from(Role.class);
		Predicate predicate = criteriaBuilder.equal(role.get(Role_.code), roleCode);
		criteriaQuery.where(predicate);
		List<Role> result = entityManager.createQuery(criteriaQuery).getResultList();
		if (result != null && !result.isEmpty()) {
			return result.get(0);			
		}
		return null;
	}

}
