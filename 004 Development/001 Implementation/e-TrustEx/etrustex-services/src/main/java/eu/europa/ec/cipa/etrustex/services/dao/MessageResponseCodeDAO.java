package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;
import java.util.Set;

import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;

public class MessageResponseCodeDAO extends TrustExDAO<MessageResponseCode, Long> implements IMessageResponseCodeDAO{

	@Override
	public List<MessageResponseCode> getTransactionMessageResponseCode(
			Long transactionId) {

		return entityManager.createQuery("from eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode m where m.tansaction.id =:transactionId",MessageResponseCode.class)
		.setParameter("transactionId", transactionId).getResultList();
	}

	@Override
	public List<MessageResponseCode> getDocumentMessageResponseCode(
			Long documentId) {
		return entityManager.createQuery("from eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode m where m.document.id =:documentId",MessageResponseCode.class)
		.setParameter("documentId", documentId).getResultList();
	}

	@Override
	public List<MessageResponseCode> getInterchangeAgreementMessageResponseCode(
			Long icaId) {
		return entityManager.createQuery("from eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode m where m.interchangeAgreement.id =:icaId",MessageResponseCode.class)
		.setParameter("icaId", icaId).getResultList();
	}

	@Override
	public List<MessageResponseCode> getDefaultMessageResponseCode() {
		return entityManager.createQuery("from eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode m where m.interchangeAgreement.id is NULL and m.document.id is NULL and m.tansaction.id is NULL and m.profile.id is NULL",MessageResponseCode.class).getResultList();
	}

	@Override
	public List<MessageResponseCode> getProfileMessageResponseCode(Long profileId) {
		return entityManager.createQuery("from eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode m where m.profile.id =:profileId",MessageResponseCode.class)
		.setParameter("profileId", profileId).getResultList();
	}
	
	@Override
	public List<MessageResponseCode> getProfileMessageResponseCode(Set<Profile> profiles) {
		return entityManager.createQuery("from eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode m where m.profile.id in :profiles",MessageResponseCode.class)
		.setParameter("profiles", profiles).getResultList();
	}

}
