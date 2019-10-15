package eu.europa.ec.etrustex.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IHumanReadableTemplateDAO;
import eu.europa.ec.etrustex.domain.HumanReadableTemplate;

@Repository
public class HumanReadableTemplateDAO extends TrustExDAO<HumanReadableTemplate, Long> implements IHumanReadableTemplateDAO {

	
	@Override
	public List<HumanReadableTemplate> findByTransactionAndName(Long transactionId,String hrName) {
		return entityManager.createQuery("from eu.europa.ec.etrustex.domain.HumanReadableTemplate hr where hr.transaction.id =:transactionId and UPPER(hr.name) like UPPER(:hrName)",HumanReadableTemplate.class)
				.setParameter("transactionId", transactionId)
				.setParameter("hrName", "%" + hrName.trim() + "%")
				.getResultList();
		
	}


	@Override
	public List<HumanReadableTemplate> findByDefault(Long documentId, Long transactionId) {
		return entityManager.createQuery("from eu.europa.ec.etrustex.domain.HumanReadableTemplate hr where (hr.document.id =:documentId or hr.transaction.id =:transactionId) and hr.isDefault = 1 ",HumanReadableTemplate.class)
				.setParameter("documentId", documentId)
				.setParameter("transactionId", transactionId)
				.getResultList();
	}

	@Override
	public List<HumanReadableTemplate> findByDocumentAndTransactionAndName(Long documentId, Long transactionId, String hrName) {
		return entityManager.createQuery("from eu.europa.ec.etrustex.domain.HumanReadableTemplate hr where (hr.document.id =:documentId or hr.transaction.id =:transactionId) and UPPER(hr.name) like UPPER(:hrName)",HumanReadableTemplate.class)
				.setParameter("documentId", documentId)
				.setParameter("transactionId", transactionId)
				.setParameter("hrName", "%" + hrName.trim() + "%")
				.getResultList();
	}


	@Override
	public List<HumanReadableTemplate> findByDocumentIdAndName(Long documentId, String hrName) {
		return entityManager.createQuery("from eu.europa.ec.etrustex.domain.HumanReadableTemplate hr where hr.document.id =:documentId and UPPER(hr.name) like UPPER(:hrName)",HumanReadableTemplate.class)
				.setParameter("documentId", documentId)
				.setParameter("hrName", "%" + hrName.trim() + "%")
				.getResultList();
	}
	
	@Override
	public List<HumanReadableTemplate> findByDocumentIdAndTransactionId(Long documentId, Long transactionId) {
		return entityManager.createQuery("from eu.europa.ec.etrustex.domain.HumanReadableTemplate hr where hr.document.id =:documentId and hr.transaction.id =:transactionId",HumanReadableTemplate.class)
				.setParameter("documentId", documentId)
				.setParameter("transactionId", transactionId)
				.getResultList();
	}

		
}
