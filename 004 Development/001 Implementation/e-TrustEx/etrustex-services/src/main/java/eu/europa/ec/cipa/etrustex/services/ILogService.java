/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import java.util.Date;
import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.log.Log;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.FrequencyType;



/**
 * @author chiricr
 *
 */
public interface ILogService {
	
	/**
	 * creates a Log object with the values of the attributes of the given DTO argument
	 * and saves it to the database
	 * @param logDTO
	 */
	LogDTO saveLog(LogDTO logDTO);
	
	/**
	 * retrieves the thread-local variable representing the log correlation id
	 */
	String getLogCorrelationId();
	
	/**
	 * retrieves the total volume of data sent by the party with the given id
	 * since the beginning of the interval given by the SLA FrequencyType parameter
	 * @param partyId
	 * @param frequencyType
	 * @return
	 */
	long getVolumeCountForParty(long partyId, FrequencyType frequencyType);

	Log getLog(Long id);
	
	List<Log> findLogsByCriteria(LogDTO logDTO, Date from, Date to, int firstResult, int maxResults);
	
	long count(LogDTO logDTO, Date from, Date to);
	
	/**
	 * removes the value of the thread-local variable that represents the log correlation id
	 * for the current thread 
	 */
	void reinitializeCorrelationId();
	
}
