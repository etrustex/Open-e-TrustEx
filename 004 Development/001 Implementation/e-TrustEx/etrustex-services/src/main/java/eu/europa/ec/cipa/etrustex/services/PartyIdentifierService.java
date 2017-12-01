/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.services.dao.IPartyIdentifierDAO;

/**
 * @author batrian
 *
 */
@Service
public class PartyIdentifierService implements IPartyIdentifierService {

	@Autowired
	private IPartyIdentifierDAO partyIdentifierDAO;
	
	private static final Logger logger = LoggerFactory
			.getLogger(PartyIdentifierService.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean existsPartyIdentifierInBusinessDomain(PartyIdentifier pi, Long bd) {
	
		if (pi == null){
			return false;
		}
		
		if (pi.getValue() != null){
			pi.setValue(pi.getValue().trim());
		}
		
		List<PartyIdentifier> pidList = partyIdentifierDAO.getBySchemeAndValue(pi);
		if (CollectionUtils.isEmpty(pidList)){
			return false;
		}

		for (PartyIdentifier partyIdentifier : pidList) {
			if (partyIdentifier.getParty() == null
					|| partyIdentifier.getParty().getBusinessDomain() == null) {
				logger.warn(
						"Dead record: party identifiers not linked to a party, or parties not linked to a domain. Starting point: {}",
						partyIdentifier);
			} else {
				if (partyIdentifier.getParty().getBusinessDomain().getId().equals(bd)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PartyIdentifier getBySchemeAndValueInBusinessDomain(PartyIdentifier pi, Long bd) {
	
		if (pi == null){
			return null;
		}
		
		if (pi.getValue() != null){
			pi.setValue(pi.getValue().trim());
		}
		
		List<PartyIdentifier> pidList = partyIdentifierDAO.getBySchemeAndValue(pi);
		if (CollectionUtils.isEmpty(pidList)){
			return null;
		}

		for (PartyIdentifier partyIdentifier : pidList) {
			if (partyIdentifier.getParty() == null
					|| partyIdentifier.getParty().getBusinessDomain() == null) {
				logger.warn(
						"Dead record: party identifiers not linked to a party, or parties not linked to a domain. Starting point: {}",
						partyIdentifier);
			} else {
				if (partyIdentifier.getParty().getBusinessDomain().getId().equals(bd)){
					return partyIdentifier;
				}
			}
		}
		
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public PartyIdentifier getPartyIdentifier(Long partyIdentifierId) {
		return partyIdentifierDAO.read(partyIdentifierId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletePartyIdentifier(Long partyIdentifierId) {
		partyIdentifierDAO.delete(partyIdentifierId);
	}

}
