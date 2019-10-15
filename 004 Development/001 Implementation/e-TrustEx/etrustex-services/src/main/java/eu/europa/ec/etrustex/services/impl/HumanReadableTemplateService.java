package eu.europa.ec.etrustex.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.IHumanReadableTemplateDAO;
import eu.europa.ec.etrustex.domain.HumanReadableTemplate;
import eu.europa.ec.etrustex.services.IHumanReadableTemplateService;


@Service
public class HumanReadableTemplateService implements IHumanReadableTemplateService {

	@Autowired
	private IHumanReadableTemplateDAO humanReadableTemplateDAO;

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public HumanReadableTemplate findByDefault(Long documentId, Long transactionId) throws TechnicalErrorException {
		List<HumanReadableTemplate> templateList = null;
		if(documentId != null && transactionId != null){
			templateList = humanReadableTemplateDAO.findByDefault(null, transactionId);
			templateList = CollectionUtils.isEmpty(templateList) ? humanReadableTemplateDAO.findByDefault(documentId, null) : templateList;
		}else{
			templateList = humanReadableTemplateDAO.findByDefault(documentId, transactionId);
		}
		
		if(CollectionUtils.size(templateList) > 1){
			throw new TechnicalErrorException();
		}

		return CollectionUtils.isNotEmpty(templateList) ? templateList.get(0) : null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public HumanReadableTemplate findByDocumentAndTransactionAndName(Long documentId, Long transactionId,String hrName) {
		List<HumanReadableTemplate> hrList = null;
		if(documentId != null && transactionId != null){
			hrList = humanReadableTemplateDAO.findByTransactionAndName(transactionId, hrName);
			hrList = CollectionUtils.isEmpty(hrList) ? humanReadableTemplateDAO.findByDocumentIdAndName(documentId, hrName) : hrList;
		} else {
			hrList = humanReadableTemplateDAO.findByDocumentAndTransactionAndName(documentId, transactionId, hrName);		
		}
		return CollectionUtils.isNotEmpty(hrList) ? hrList.get(0) : null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Long create(HumanReadableTemplate hr) {
		hr = humanReadableTemplateDAO.create(hr);
		return hr.getId();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public void delete(Long id) {
		HumanReadableTemplate hr = humanReadableTemplateDAO.read(id);
		if(hr != null){
			humanReadableTemplateDAO.delete(hr);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public HumanReadableTemplate update(HumanReadableTemplate hr) {
		hr = humanReadableTemplateDAO.update(hr);
		return hr;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public HumanReadableTemplate findByDocumentIdAndTransactionId(Long documentId, Long transactionId) {
		List<HumanReadableTemplate> hrList = humanReadableTemplateDAO.findByDocumentIdAndTransactionId(documentId, transactionId);
		return CollectionUtils.isNotEmpty(hrList) ? hrList.get(0) : null;
	}


}