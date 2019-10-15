package eu.europa.ec.etrustex.dao;

import java.util.List;
import java.util.Set;

import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;

public interface IMessageResponseCodeDAO extends ITrustExDAO<MessageResponseCode, Long> {
	public List<MessageResponseCode> getTransactionMessageResponseCode(Long transactionId);
	public List<MessageResponseCode> getDocumentMessageResponseCode(Long documentId);
	public List<MessageResponseCode> getInterchangeAgreementMessageResponseCode(Long icaId);
	public List<MessageResponseCode> getDefaultMessageResponseCode();
	public List<MessageResponseCode> getProfileMessageResponseCode(Long profileId);
	public List<MessageResponseCode> getProfileMessageResponseCode(Set<Profile> profiles);

}
