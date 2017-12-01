/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;

/**
 * @author batrian
 *
 */
public interface IPartyIdentifierDAO extends ITrustExDAO<PartyIdentifier, Long> {
	
	public boolean exists(PartyIdentifier pi);
	
	public List<PartyIdentifier> getBySchemeAndValue(PartyIdentifier pi);
	
	public void delete(Long id);
	
	//to be able to force flush in services when removing a party identifier and adding a similar one
	public void flushEm();
}
