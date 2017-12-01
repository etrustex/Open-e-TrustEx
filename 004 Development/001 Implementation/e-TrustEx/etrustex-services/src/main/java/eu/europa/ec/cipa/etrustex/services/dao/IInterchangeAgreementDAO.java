package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.services.dto.ICASearchDTO;

import java.util.List;
import java.util.Set;

public interface IInterchangeAgreementDAO extends ITrustExDAO<InterchangeAgreement, Long> {
	
	public  List<InterchangeAgreement> getInterchangeAgreements(Party requester, List<String> documentTypeIds, Set<Party> senders, Set<Party> receivers);
	public  List<InterchangeAgreement> getInterchangeAgreements(Party senderParty, Party recieverParty, Long transactionId);

	/**
	 * @author batrian
	 */
	@Override
	public InterchangeAgreement create(InterchangeAgreement ica);

    List<InterchangeAgreement> findInterchangeAgreementsByCriteria(ICASearchDTO dto);
	public List<InterchangeAgreement> getInterchangeAgreements(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId);
	public Boolean isUniqueBy(Long profileId, Long firstPartyId, Long firstPartyRoleId, Long secondPartyId, Long secondPartyRoleId);
	public List<InterchangeAgreement> getInterchangeAgreements(PartyRole a, PartyRole b, Profile p, CIALevel l);
	public List<InterchangeAgreement> getInterchangeAgreementsByProfile(Long profileId);
	public Boolean containsParty(Party party);
	public Boolean existICAwithSameRoleForParties(Role role);
	public Boolean isUsed(Long id);
}
