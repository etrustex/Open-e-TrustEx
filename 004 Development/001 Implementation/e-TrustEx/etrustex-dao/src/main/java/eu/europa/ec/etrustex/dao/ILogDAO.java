/**
 * 
 */
package eu.europa.ec.etrustex.dao;

import java.util.Date;
import java.util.List;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.log.Log;
import eu.europa.ec.etrustex.types.FrequencyType;

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
	 * @return the volume in bytes
	 */
	public long getVolumeCountForParty(long partyId, FrequencyType frequencyType);	

	public Log getLog(Long id);
	
	public List<Log> findLogsByCriteria(LogDTO logDTO, Date from, Date to, int firstResult, int maxResults);
	
	public long count(LogDTO logDTO, Date from, Date to);
	
	/**
	 * delete size of wrapper in the logs
	 * @param correlationId the log correlation ID 
	 * @return the number of records updated
	 */
	public int removeBinarySizeInLogs(String correlationId);
}
