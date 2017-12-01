package eu.europa.ec.cipa.etrustex.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.scxml.model.ModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineConfigException;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineException;
import eu.europa.ec.cipa.etrustex.services.util.StateMachineWrapper;

public class StateMachineService implements IStateMachineService {

	private static final Logger logger = LoggerFactory.getLogger(StateMachineService.class);

	private Map<Document, StateMachineWrapper> stateMachines = new HashMap<Document, StateMachineWrapper>();

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	private StateMachineWrapper getInstance(Document document) throws StateMachineConfigException {
		StateMachineWrapper stateMachine = stateMachines.get(document);
		if (stateMachine == null) {
			logger.trace("creating state machine wrapper for document [{}]", document.getLocalName());
			try {
				if (document.getStateMachine() == null) {
					logger.error("state machine object for document [{}] is null", document.getLocalName());
					throw new StateMachineConfigException("state machine object for document [" + document.getLocalName() + "] is null");
				}
				String definition = document.getStateMachine().getDefinition();
				if (definition == null) {
					logger.error("state machine definition for document [{}] is null", document.getLocalName());
					throw new StateMachineConfigException("state machine definition for document [" + document.getLocalName() + "] is null");
				}
				stateMachine = new StateMachineWrapper(definition);
			} catch (IOException e) {
				logger.error(e.toString());
				throw new StateMachineConfigException("IO exception during state machine creation for document [" + document.getLocalName() + "]", e);
			} catch (SAXException e) {
				logger.error(e.toString());
				throw new StateMachineConfigException("SAX exception during state machine creation for document [" + document.getLocalName() + "]", e);
			} catch (ModelException e) {
				logger.error(e.toString());
				throw new StateMachineConfigException("Model exception during state machine creation for document [" + document.getLocalName() + "]", e);
			}
			stateMachines.put(document, stateMachine);
			logger.trace("created state machine wrapper for document [{}]", document.getLocalName());
		}
		return stateMachine;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getNextState(Document document, String fromStateName, String eventName, Map<String, Object> variables) throws StateMachineException {
		return getInstance(document).getNextState(fromStateName, eventName, variables);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getNextState(Document document, String fromStateName, String eventName) throws StateMachineException {
		logger.debug("state machine was invoked with no variables, will not evaluate", document.getLocalName());
		return getNextState(document, fromStateName, eventName, null);
	}
}
