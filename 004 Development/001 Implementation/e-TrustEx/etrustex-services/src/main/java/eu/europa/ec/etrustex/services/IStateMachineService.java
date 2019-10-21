package eu.europa.ec.etrustex.services;

import java.util.Map;

import eu.europa.ec.etrustex.dao.exception.StateMachineException;
import eu.europa.ec.etrustex.domain.Document;

public interface IStateMachineService {
	String getNextState(Document document, String sourceStateName, String eventName)  throws StateMachineException;
	String getNextState(Document document, String sourceStateName, String eventName, Map<String, Object> variables)  throws StateMachineException;
}
