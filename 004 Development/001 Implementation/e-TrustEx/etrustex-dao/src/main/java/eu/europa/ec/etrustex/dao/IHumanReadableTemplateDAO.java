package eu.europa.ec.etrustex.dao;

import java.util.List;

import eu.europa.ec.etrustex.domain.HumanReadableTemplate;

public interface IHumanReadableTemplateDAO extends ITrustExDAO<HumanReadableTemplate, Long> {
	
	/**
	 * Method used to retrieve human readable template based on TransactionId and name
	 * @param transactionId
	 * @param name
	 * @return List of human readable template
	 */
	public List<HumanReadableTemplate> findByTransactionAndName(Long transactionId,String hrName);
	
	/**
	 * Method used to retrieve human readable template based on (documentId or TransactionId) and name
	 * @param documentId
	 * @param transactionId
	 * @param name
	 * @return List of human readable template
	 */
	public List<HumanReadableTemplate> findByDocumentAndTransactionAndName(Long documentId, Long transactionId,String hrName);
	
	/**
	 * Method used to retrieve human readable template based on (documentId or TransactionId) and isDefault = true
	 * @param documentId
	 * @param transactionId
	 * @return List of human readable template
	 */
	public List<HumanReadableTemplate> findByDefault(Long documentId, Long transactionId);

	/**
	 * Method used to retrieve human readable template based on documentId and name
	 * @param documentId
	 * @param name
	 * @return List of human readable template
	 */
	public List<HumanReadableTemplate> findByDocumentIdAndName(Long documentId,String hrName);
	
	/**
	 * Method used to retrieve human readable template based on documentId and TransactionId
	 * @param documentId
	 * @param transactionId
	 * @return List of human readable template
	 */
	public List<HumanReadableTemplate> findByDocumentIdAndTransactionId(Long documentId, Long transactionId);
}
