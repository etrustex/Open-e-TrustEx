/**
 *
 */
package eu.europa.ec.etrustex.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.ILogDAO;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain_;
import eu.europa.ec.etrustex.domain.log.Log;
import eu.europa.ec.etrustex.domain.log.Log_;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo_;
import eu.europa.ec.etrustex.types.FrequencyType;
import eu.europa.ec.etrustex.types.LogModuleEnum;

/**
 * @author chiricr
 *
 */
@Repository
public class LogDAO extends TrustExDAO<Log, Long> implements ILogDAO {

    private static final int WEEKLY_FREQUENCY_INTERVAL = 7;
    private static final int MONTHLY_FREQUENCY_INTERVAL = 30;


    @Override
    public long getVolumeCountForParty(long partyId, FrequencyType frequencyType) {

        StringBuilder queryStringBuilder = new StringBuilder();
        queryStringBuilder
                .append("select SUM(log.messageSize) ")
                .append(" from Log log ")
                .append(" where log.senderParty.id = :partyId ")
                .append(" and log.accessInfo.creationDate >= :startDate");

		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
		startDateCalendar.set(Calendar.MINUTE, 0);
		startDateCalendar.set(Calendar.SECOND, 0);
		if (frequencyType.equals(FrequencyType.WEEKLY)) {
			startDateCalendar.set(Calendar.DAY_OF_MONTH, startDateCalendar.get(Calendar.DAY_OF_MONTH) - WEEKLY_FREQUENCY_INTERVAL);
		} if (frequencyType.equals(FrequencyType.MONTHLY)) {
			startDateCalendar.set(Calendar.DAY_OF_MONTH, startDateCalendar.get(Calendar.DAY_OF_MONTH) - MONTHLY_FREQUENCY_INTERVAL);
		}

		Object result = entityManager.createQuery(queryStringBuilder.toString())
			.setParameter("partyId", partyId)
			.setParameter("startDate", startDateCalendar.getTime())
			.getSingleResult();

		return result != null ? Long.valueOf(result.toString()).longValue() : 0;

	}

    @Override
    public Log getLog(Long id) {
        return entityManager.find(Log.class, id);
    }


    @Override
    public List<Log> findLogsByCriteria(LogDTO logDTO, Date from, Date to, int firstResult, int maxResults) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Log> cq = cb.createQuery(Log.class);
        Root<Log> l = cq.from(Log.class);
        List<Predicate> predicates = getPredicates(logDTO, from, to, cb, l);

        cq.select(l);
        cq.where(predicates.toArray(new Predicate[predicates.size()]));
        cq.orderBy(cb.desc(l.get(Log_.accessInfo).get(EntityAccessInfo_.creationDate)));

        return entityManager.createQuery(cq).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Override
    public long count(LogDTO logDTO, Date from, Date to) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Log> l = cq.from(Log.class);
        List<Predicate> predicates = getPredicates(logDTO, from, to, cb, l);

        cq.select(cb.count(l));
        cq.where(predicates.toArray(new Predicate[predicates.size()]));

		return entityManager.createQuery(cq).getSingleResult();
	}

    private List<Predicate> getPredicates(LogDTO logDTO, Date from, Date to, CriteriaBuilder cb, Root<Log> l) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(from != null) {
            predicates.add(cb.greaterThanOrEqualTo(l.get(Log_.accessInfo).get(EntityAccessInfo_.creationDate), from));
        }

        if(to != null) {
            // to and from are truncated (to day) dates. Creation dates in DB are not truncated.
            predicates.add(cb.lessThan(l.get(Log_.accessInfo).get(EntityAccessInfo_.creationDate), to));
        }

        if (logDTO.getModule() != null) {
            if(StringUtils.isNotEmpty(logDTO.getModule().name()) && logDTO.getModule().name().equalsIgnoreCase(LogModuleEnum.ETXADMIN.name())) {
                predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(l.<String>get(Log_.module.getName()))), logDTO.getModule().name().trim().toUpperCase()));
            } else {
                predicates.add(cb.or(cb.notEqual(cb.trim(Trimspec.BOTH, cb.upper(l.<String>get(Log_.module.getName()))), LogModuleEnum.ETXADMIN.name()), cb.isNull(l.get(Log_.module.getName()))));
            }
        }

        if(logDTO.getLogType() != null) {
            predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.logType))), logDTO.getLogType().name().toUpperCase()));
        }

        if(logDTO.getOperation() != null) {
            predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.operation))), logDTO.getOperation().name().toUpperCase()));
        }

        if(StringUtils.isNotEmpty(logDTO.getEntity())) {
            predicates.add(cb.equal(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.entity))), logDTO.getEntity().trim().toUpperCase()));
        }

        if(logDTO.getEntities() != null && logDTO.getEntities().size() > 0) {
            predicates.add(l.get(Log_.entity).in(logDTO.getEntities()));
        }

        if(logDTO.getBusinessDomain() != null) {
            predicates.add(cb.equal(l.get(Log_.businessDomain).get(BusinessDomain_.id), logDTO.getBusinessDomain().getId()));
        }else if(logDTO.getBusinessDomains() != null){
            predicates.add(l.get(Log_.businessDomain).get(BusinessDomain_.id).in(logDTO.getBusinessDomains()));
        }

        if(StringUtils.isNotEmpty(logDTO.getUsername())) {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.username))), "%" + logDTO.getUsername().trim().toUpperCase() + "%"));
        }

        if(StringUtils.isNotEmpty(logDTO.getCorrelationId())) {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.correlationId))), "%" + logDTO.getCorrelationId().trim().toUpperCase() + "%"));
        }

        if(logDTO.getEntityId() != null) {
            predicates.add(cb.equal(l.get(Log_.entityId), logDTO.getEntityId()));
        }

        if(StringUtils.isNotEmpty(logDTO.getDocumentTypeCode())) {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.documentTypeCode))), "%" + logDTO.getDocumentTypeCode().trim().toUpperCase() + "%"));
        }

        if(StringUtils.isNotEmpty(logDTO.getDocumentId())) {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.documentId))), "%" + logDTO.getDocumentId().trim().toUpperCase() + "%"));
        }

        if(StringUtils.isNotEmpty(logDTO.getBusinessCorrelationId())) {
            predicates.add(cb.like(cb.trim(Trimspec.BOTH, cb.upper(l.get(Log_.businessCorrelationId))), "%" + logDTO.getBusinessCorrelationId().trim().toUpperCase() + "%"));
        }

        if(logDTO.getIssuerParty() != null) {
            predicates.add(cb.equal(l.get(Log_.issuerParty), logDTO.getIssuerParty()));
        }

        if(logDTO.getSenderParty() != null) {
            predicates.add(cb.equal(l.get(Log_.senderParty), logDTO.getSenderParty()));
        }

        if(logDTO.getReceiverParty() != null) {
            predicates.add(cb.equal(l.get(Log_.receiverParty), logDTO.getReceiverParty()));
        }

        return predicates;
    }

    public int removeBinarySizeInLogs(String correlationId) {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	CriteriaUpdate<Log> updateQuery = cb.createCriteriaUpdate(Log.class);
    	Root<Log> root = updateQuery.from(Log.class);
    	Expression<Long> nullMessageSize = cb.nullLiteral(Long.class);
    	updateQuery.set(root.get(Log_.messageSize), nullMessageSize);
    	updateQuery.where(cb.and(
    			cb.equal(root.get(Log_.correlationId), correlationId)),
    			cb.isNotNull(root.get(Log_.messageSize)));
    	return entityManager.createQuery(updateQuery).executeUpdate();
    }
}
