package eu.europa.ec.cipa.etrustex.services;

import java.util.Map;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineException;

public interface IStateMachineService {
	String getNextState(Document document, String sourceStateName, String eventName)  throws StateMachineException;
	String getNextState(Document document, String sourceStateName, String eventName, Map<String, Object> variables)  throws StateMachineException;
}
