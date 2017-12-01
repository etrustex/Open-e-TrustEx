/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.CIALevel;

/**
 * @author batrian
 *
 */
@Repository
public class CIALevelDAO extends TrustExDAO<CIALevel, Long> implements ICIALevelDAO {

	@Override
	public List<CIALevel> retrieveCIALevel(Integer confidentiality,
			Integer integrity, Integer availability) {
		
		String query = 	"from CIALevel cia " + 
						"where   cia.confidentialityLevel =:confidentiality " + 
						  "and   cia.integrityLevel 	  =:integrity " + 
						  "and   cia.availabilityLevel    =:availability";
		
		return entityManager.createQuery(query, CIALevel.class)
				.setParameter("confidentiality", confidentiality)
				.setParameter("integrity", integrity)
				.setParameter("availability", availability)
				.getResultList();
	}

}
