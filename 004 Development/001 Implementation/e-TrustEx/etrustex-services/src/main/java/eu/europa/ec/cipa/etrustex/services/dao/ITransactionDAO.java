package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.Transaction;

import java.util.List;

public interface ITransactionDAO extends ITrustExDAO<Transaction, Long> {
	
	public Transaction getTransactionByNameSpace(String transactionRequestLocalName,String transactionLocalName);
	public List<Transaction> getTransactionsByDocumentTypeCd(String documentTypeCode);
	public List<Transaction> getTransactionsForDocument(String documentNamespace,String documentlocalName);
	public List<Transaction> getTransactionsByNameAndVersion(String name, String version);
	public List<Transaction> getTransactionsByNamespaceAndRequestLocalName(String namespace, String requestLocalName);
	List<Transaction> getTransactionsByCriteria(Transaction transaction);
	boolean isInUse(Transaction tx);

}