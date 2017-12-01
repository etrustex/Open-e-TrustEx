package eu.europa.ec.cipa.etrustex.services.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class TrustExDAO<T, PK extends Serializable> implements ITrustExDAO<T, PK> {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private Class<T> type;
	
	private static final Logger logger = LoggerFactory
			.getLogger(TrustExDAO.class.getName());
	
	public TrustExDAO(){
		Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public T create(T newInstance) {
		entityManager.persist(newInstance);
		return newInstance;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public T read(PK id) {
		return entityManager.find(type,id) ;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public T update(T transientObject) {		
		return entityManager.merge(transientObject) ;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(T persistentObject) {
		this.entityManager.remove(persistentObject);
	}
	
	@Override
	public boolean isManaged(T object) {
		return entityManager.contains(object);
	}	
	
	//@Override
	
	//TODO to be continued niot ready to use.
//	public List<T> search(List<SearchCriteria> criterias) throws Exception {
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<T> cq = cb.createQuery(type);
//		Root<T> root = cq.from(type);
//		Class metaModeClass= Class.forName(type.getName() + "_");
//		List<Predicate> predicates = new ArrayList<Predicate>();
//		
//		for (SearchCriteria searchCriteria : criterias) {
//			//searchCriteria.getAttributeName()
//			Field f =metaModeClass.getDeclaredField(searchCriteria.getAttributeName());
//			Object attibute=f.get(null);
//			Method m = root.getClass().getMethod("get",f.getType());
//			Expression e = (Expression) m.invoke(root, attibute);
//			
//		}
//		return null;
//	}
	
	
	@Override
	public List<T> getAll() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
		criteriaQuery.from(type);
		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
	/**
	* Provides a convenient way for subclasses to implement simple queries
	* that compare an attribute of the entity to a target value.
	* @param attribute The attribute of the entity to compare.
	* @param value The target value of the given attribute.
	* @param <Y> The type of the attribute and target value.
	* @return A single result that matches the given criteria.
	*/
	protected <Y> T getUniqueByAttribute(SingularAttribute<T, Y> attribute,
				Y value) {
		TypedQuery<T> query = this.createTypedQuery(attribute, value);
		T result;
		try {
			result = query.getSingleResult();
		} catch (NonUniqueResultException nure) {
			logger.error("Result not unique.", nure);
			result = null;
		} catch (NoResultException nre) {
			logger.error("Result not existant", nre);
			result = null;
		}
		return result;
	}
	
	protected <Y> List<T> getListByAttribute(SingularAttribute<T,
				Y> attribute, Y value) {
		TypedQuery<T> query = this.createTypedQuery(attribute, value);
		return query.getResultList();
	}
	
	/**
	* Creates a typed query based on the attribute given and the target value
	* for that attribute.
	* @param attribute The attribute to compare.
	* @param value The target value for the given attribute.
	* @param <Y> The type of the target attribute and target value.
	* @return A typed query that contains the given criteria.
	*/
	private <Y> TypedQuery<T> createTypedQuery(SingularAttribute<T, Y> attribute,
	 			Y value) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
		Root<T> root = criteriaQuery.from(type);
		Path<Y> path = root.get(attribute);
		return entityManager.createQuery(criteriaQuery.where(builder.equal(path, value)));
	}
	
	/*@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Criterion... criterion) {  
		Session session = (Session)entityManager.getDelegate();
        Criteria crit = session.createCriteria(type);
        for (Criterion c : criterion) {  
            crit.add(c);  
        }  
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        return crit.list();  
   }*/
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	

}
