/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;


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
