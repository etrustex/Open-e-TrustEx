/**
 * 
 */
package eu.europa.ec.etrustex.services;

import eu.europa.ec.etrustex.domain.PartyIdentifier;


/**
 * @author batrian
 *
 */
public interface IPartyIdentifierService {

	
	public boolean existsPartyIdentifierInBusinessDomain(PartyIdentifier pi, Long bd);
	
	public PartyIdentifier getPartyIdentifier(Long partyIdentifierId);
	
	public void deletePartyIdentifier(Long partyIdentifierId);

	public PartyIdentifier getBySchemeAndValueInBusinessDomain(PartyIdentifier pi,
			Long bd);
	
}
