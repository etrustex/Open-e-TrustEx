/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.Date;
import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.log.Log;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.FrequencyType;

/**
 * @author chiricr
 *
 */
public interface ILogDAO extends ITrustExDAO<Log, Long> {
	
	/**
	 * retrieves the total volume of wrappers sent by the party with the given id
	 * since the beginning of the interval given by the SLA FrequencyType parameter 
	 * @param partyId
	 * @param frequencyType
	 * @return
	 */
	public long getVolumeCountForParty(long partyId, FrequencyType frequencyType);	

	Log getLog(Long id);
	
	List<Log> findLogsByCriteria(LogDTO logDTO, Date from, Date to, int firstResult, int maxResults);
	
	long count(LogDTO logDTO, Date from, Date to);
}
