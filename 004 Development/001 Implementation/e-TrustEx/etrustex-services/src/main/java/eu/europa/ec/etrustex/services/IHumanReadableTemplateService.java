package eu.europa.ec.etrustex.services;

import java.util.List;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.domain.HumanReadableTemplate;

public interface IHumanReadableTemplateService {
	
	
	/**
	 * Method used to retrieve human readable template based on (documentId or TransactionId) and name
	 * @param documentId
	 * @param transactionId
	 * @param name
	 * @return List of human readable template
	 */
	public HumanReadableTemplate findByDocumentAndTransactionAndName(Long documentId, Long transactionId,String hrName);
	
	/**
	 * Method used to retrieve human readable template based on (documentId or TransactionId) and isDefault = true
	 * @param documentId
	 * @param transactionId
	 * @return List of human readable template
	 * @throws TechnicalErrorException 
	 */
	public HumanReadableTemplate findByDefault(Long documentId, Long transactionId) throws TechnicalErrorException;
	
	/**
	 * Create human readable template and return it
	 * @param HumanReadableTemplate
	 * @return HumanReadableTemplate
	 */
	public Long create(HumanReadableTemplate hr);
	
	/**
	 * Delete human readable template by id
	 * @param id
	 */
	public void delete(Long id);
	
	/**
	 * Update human readable template
	 * @param HumanReadableTemplate
	 * @return HumanReadableTemplate
	 */
	public HumanReadableTemplate update(HumanReadableTemplate hr);
	
	/**
	 * Method used to retrieve human readable template based on documentId and TransactionId
	 * @param documentId
	 * @param transactionId
	 * @return List of human readable template
	 */
	public HumanReadableTemplate findByDocumentIdAndTransactionId(Long documentId, Long transactionId);
}