package eu.europa.ec.etrustex.dao;

import java.util.List;
import java.util.Set;

import eu.europa.ec.etrustex.dao.dto.ICASearchDTO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;

public interface IInterchangeAgreementDAO extends ITrustExDAO<InterchangeAgreement, Long> {
	
	@Override
	public InterchangeAgreement create(InterchangeAgreement ica);
	
	public  List<InterchangeAgreement> getInterchangeAgreements(List<String> documentTypeIds, Set<Party> senders, Set<Party> receivers);
	public List<InterchangeAgreement> getInterchangeAgreements(Party senderParty, Party recieverParty, Long transactionId, Long profileId);
	public List<InterchangeAgreement> findInterchangeAgreementsByCriteria(ICASearchDTO dto);
	public List<InterchangeAgreement> getInterchangeAgreements(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId);
	public List<Party> getPartiesInExistingAgreement(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId);
    public Boolean isUniqueBy(Long profileId, Long firstPartyId, Long firstPartyRoleId, Long secondPartyId, Long secondPartyRoleId, Long businessDomainId);
	public List<InterchangeAgreement> getInterchangeAgreements(PartyRole a, PartyRole b, Profile p, CIALevel l);
	public List<InterchangeAgreement> getInterchangeAgreementsByProfile(Long profileId);
	public Boolean containsParty(Party party);
	public List<InterchangeAgreement> findByParty(Party party);
    public Boolean existICAwithSameRoleForParties(Role role);
	public Boolean isUsed(Long id);
	public List<Party> getSecondParties(Long firstPartyId, Long firstPartyRoleId, Long profileId, Long secondPartyRoleId, Long businessDomainId);
}
