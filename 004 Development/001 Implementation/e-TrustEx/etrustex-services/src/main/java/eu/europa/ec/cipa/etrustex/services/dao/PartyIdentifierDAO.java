/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;

/**
 * @author batrian
 *
 */
@Repository
public class PartyIdentifierDAO extends TrustExDAO<PartyIdentifier, Long> implements
		IPartyIdentifierDAO {

	@Override
	public boolean exists(PartyIdentifier pi) {
		boolean exists = false;
		
		String queryString = "SELECT partyIdentifier FROM PartyIdentifier partyIdentifier " + 
								"where partyIdentifier.schemeId = :schemeId " + 
								"and partyIdentifier.value = :value ";
		
		TypedQuery<PartyIdentifier> query = entityManager.createQuery(queryString, PartyIdentifier.class);
		query.setParameter("schemeId", pi.getSchemeId());
		query.setParameter("value", pi.getValue() != null ? pi.getValue().trim() : null);
		
		if (CollectionUtils.isNotEmpty(query.getResultList())){
			exists = true;
		}
		
		return exists;
	}

	@Override
	public List<PartyIdentifier> getBySchemeAndValue(PartyIdentifier pi) {
		
		String queryString = "SELECT partyIdentifier FROM PartyIdentifier partyIdentifier "
				+ "where partyIdentifier.schemeId = :schemeId "
				+ "and partyIdentifier.value = :value ";

		TypedQuery<PartyIdentifier> query = entityManager.createQuery(
				queryString, PartyIdentifier.class);
		query.setParameter("schemeId", pi.getSchemeId());
		query.setParameter("value", pi.getValue() != null ? pi.getValue().trim() : null);

		return query.getResultList(); 
	}

	@Override
	public void delete(Long id) {
		PartyIdentifier partyIdentifier = read(id);
		super.delete(partyIdentifier);
	}
	
	@Override
	public PartyIdentifier update(PartyIdentifier partyIdentifier) {
		if(partyIdentifier.getParty() != null 
				&& !entityManager.contains(partyIdentifier.getParty())) {
			partyIdentifier.setParty(entityManager.merge(partyIdentifier.getParty()));
		}
	
		return super.update(partyIdentifier);
	}
	
	@Override
	public PartyIdentifier create(PartyIdentifier partyIdentifier) {
		if(partyIdentifier.getParty() != null 
				&& !entityManager.contains(partyIdentifier.getParty())) {
			partyIdentifier.setParty(entityManager.merge(partyIdentifier.getParty()));
		}
	
		return super.create(partyIdentifier);
	}
	
	@Override
	public void flushEm(){
		entityManager.flush();
	}
}
