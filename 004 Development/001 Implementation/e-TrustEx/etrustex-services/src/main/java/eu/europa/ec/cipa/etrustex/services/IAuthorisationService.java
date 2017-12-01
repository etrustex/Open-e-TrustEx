package eu.europa.ec.cipa.etrustex.services;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.Credentials;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.services.exception.AuthorisationFailedException;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.IIdentifierIssuingAgency;

public interface IAuthorisationService {
		
	/**
	 * This method check if there is one and only one interchange agreement between the sender and receiver for the given Transaction type. If the issuer party and the sender party are different the system
	 * checcks that there is a agreement between the sender and the issuer for the specific transaction  
	 * 
	 * @param issuerParty the issuer of the message can be the sender party of a third party
	 * @param senderParty the sender of the message
	 * @param receiverParty the receiver of the message
	 * @param transactionId the transaction id
	 * @return  the interchange agreement if it exists
	 * @throws AuthorisationFailedException if there is no valid agreement be  between the sender and receiver for the given Transaction type
	 */
	public InterchangeAgreement checkAuthorisation(Party issuerParty	,Party senderParty, Party receiverParty,Long transactionId) throws AuthorisationFailedException;
	
	/**
	 * This method checks if the supplied  sender is authorised to execute the provided transaction
	 * 
	 * @param issuerParty the issuer of the message
	 * @param senderParty the sender of the message (party calling the transaction)
	 * @param transactionId the transaction to check 
	 * @throws AuthorisationFailedException if the system cannot find at least one interchange agreement involving the sender and the given transaction
	 */
	public void checkTransactionAuthorisation(Party issuerParty,Party senderParty, Long transactionId) throws AuthorisationFailedException;
	
	/**
	 * This method retrieves an agreement between two parties. this used to check for third party agreements 
	 * @param authorizingParty the party delegating rights to submit documents 
	 * @param delegateParty the party acting on behalf of the authorizing party
	 * @return
	 */
	public PartyAgreement retrievePartyAgreement(Party authorizingParty,Party delegateParty);
	/**
	 * This method check if an agreement between two parties exists for the given transaction. this used to check for third party agreements 
	 * @param authorizingParty the party delegating rights to submit documents 
	 * @param delegateParty the party acting on behalf of the authorizing party
	 * @param transactionId 
	 * @return
	 */
	public PartyAgreement checkPartyAgreementForTransaction(Party authorizingParty,Party delegateParty,Long transactionId);
	
	/**
	 * This returns the party linked to the authenticated user id ake the issuer party of the message. This is used for third parties support
	 * 
	 * @param authenticatedUser user that connected to the systme
	 * @return
	 * @throws UndefinedIdentifierException
	 */
	public Party getMessageIssuer(String authenticatedUser)throws UndefinedIdentifierException;
	
	/**
	 * This method gets a party from the database using its internal id
	 * @param partyId 
	 * @return the PArty or null if the party is not found
	 *
	 */
	public Party getParty(Long partyId) ;

	/**
	 * This method gets a party from the database using its id with scheme
	 * @param partyIdWithScheme fi 0088:12336554
	 * @param bd the business domain
	 * @return the PArty
	 * @throws UndefinedIdentifierException
	 */	
	public Party getParty(String partyIdWithScheme,BusinessDomain bd) throws UndefinedIdentifierException;
	
	/**
	 * Utility method to retrieve a part from it'd id and sheme
	 * @param schemeIsoCode
	 * @param partyId
	 * @return
	 */	
	public Party getParty(String schemeIsoCode, String partyId, BusinessDomain bd);
	
	public Party getParty(IIdentifierIssuingAgency shemeId, String partyId, BusinessDomain bd);
	/**
	 * Utility method to retrieve a transaction from it's namespace and it's request local name
	 * @param transactionNameSpace
	 * @param transactionRequestLocalName
	 * @return
	 */
	public Transaction getTransactionByNameSpace(String transactionNameSpace,String transactionRequestLocalName);
	/**
	 *  Utility method to retrieve a transaction from  it's internal id
	 * @param tansactionId
	 * @return
	 */
	public Transaction getTransactionById(Long tansactionId);
	
	/**
	 *  Utility method to retrieve the list of transactions that involved the provided document type code
	 * @param documentTypeCode the document type code
	 * @return
	 */
	public List<Transaction> getTransactionsByDocumentTypeCd(String documentTypeCode);
	
	public List<Transaction> getTransactionsForDocument(String documentNamespace,String documentlocalName);
	
	public Boolean isSignatureRequired(String authenticatedUser);
	
	/**
	 * Method that checks if the current transaction is authorized through a given checkpoint 
	 * @param endpointName
	 * @param trans
	 * @return
	 */
	public Boolean isTransactionAllowed(String endpointName, Transaction trans);

}
