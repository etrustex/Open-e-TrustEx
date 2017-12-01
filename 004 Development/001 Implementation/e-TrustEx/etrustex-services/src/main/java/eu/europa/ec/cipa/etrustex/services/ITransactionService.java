package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.Transaction;

import java.util.List;

public interface ITransactionService {
	Transaction create(Transaction transaction);
	Transaction update(Transaction transaction);
	Transaction getTransaction(Long transactionId);
	void delete(Long transactionId);
	List<Transaction> getTransactionsByNameAndVersion(String name, String version);
	List<Transaction> getTransactionsByNamespaceAndRequestLocalName(String namespace, String requestLocalName);
	List<Transaction> getTransactionsByCriteria(Transaction transaction);
	List<Transaction> getAll();
	Boolean isInUse(Transaction tx);
}
