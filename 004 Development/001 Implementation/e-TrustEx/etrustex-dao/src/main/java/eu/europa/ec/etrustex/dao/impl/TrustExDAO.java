package eu.europa.ec.etrustex.dao.impl;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.ITrustExDAO;

public class TrustExDAO<T, PK extends Serializable> implements ITrustExDAO<T, PK> {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private Class<T> type;
	
	private static final Logger logger = LoggerFactory
			.getLogger(TrustExDAO.class.getName());
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		} catch (NoResultException | EmptyResultDataAccessException nre) {
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
	
	@Override
	public void detachObject(T object) {
		if (object != null && entityManager != null && entityManager.contains(object)) {
			entityManager.detach(object);
		}
	}	
}
