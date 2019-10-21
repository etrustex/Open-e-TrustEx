/**
 * 
 */
package eu.europa.ec.etrustex.services;

import java.util.Date;
import java.util.List;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.log.Log;
import eu.europa.ec.etrustex.types.FrequencyType;



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
	 * @return the volume in bytes
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
	
	void updateLog(Log log);
	
	/**
	 * delete size of wrapper in the logs
	 * @param correlationId the log correlation ID 
	 * @return the number of records updated
	 */
	public int removeBinarySizeInLogs(String correlationId);	
}
