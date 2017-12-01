package eu.europa.ec.cipa.etrustex.services.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.env.AbstractStateMachine;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.europa.ec.cipa.etrustex.services.exception.StateMachineConditionEvaluationException;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineTransitionException;

public class StateMachineWrapper extends AbstractStateMachine {

	private static final Logger logger = LoggerFactory.getLogger(StateMachineWrapper.class);

	public StateMachineWrapper(String definition) throws IOException, SAXException, ModelException {
		super(SCXMLParser.parse(new InputSource(new StringReader(definition)), new SimpleErrorHandler()));
		expressionEvaluator = new JexlEvaluator();
	}

	private JexlEvaluator expressionEvaluator;

	@Override
	public boolean invoke(String methodName) {
		return true;
	}

	public String getNextState(String sourceStateName, String eventName, Map<String, Object> variables) throws StateMachineTransitionException {
		logger.debug("querying state machine for target state of transition: source[{}]--event({})-->???", sourceStateName, eventName);

		State sourceState = fetchSourceState(sourceStateName);

		List<Transition> transitions = fetchTransitions(sourceStateName, eventName, sourceState);
		logger.trace("transition count with event [{}] is {}", eventName, transitions.size());

		List<Transition> filteredTransitions = fetchFilteredTransitions(variables, transitions);
		int filteredTransitionCount = filteredTransitions.size();
		logger.trace("filtered transition count is {}", filteredTransitionCount);

		if (filteredTransitionCount == 0) {
			logger.error("no valid transition remains");
			throw new StateMachineTransitionException("no valid transition remains");
		}
		if (filteredTransitionCount != 1) {
			logger.error("expected exactly one transition, got {} instead", filteredTransitionCount);
			throw new StateMachineTransitionException("expected exactly one transition, got " + filteredTransitionCount + " instead");
		}

		List<State> targetStates = (List<State>) filteredTransitions.get(0).getTargets();
		int candidateTargetCount = targetStates.size();
		logger.trace("candidate target count is {}", candidateTargetCount);
		if (candidateTargetCount == 0) {
			logger.error("transition has no target state");
			throw new StateMachineTransitionException("transition has no target state");
		}
		if (candidateTargetCount != 1) {
			logger.error("expected exactly one target state, got [{}] instead", candidateTargetCount);
			throw new StateMachineTransitionException("expected exactly one target state, got [" + candidateTargetCount + "] instead");
		}

		String result = targetStates.get(0).getId();
		logger.debug("state machine result for transition is source[{}]--event({})-->target[{}]", sourceStateName, eventName, result);

		return result;
	}

	private List<Transition> fetchFilteredTransitions(Map<String, Object> variables, List<Transition> transitions) throws StateMachineConditionEvaluationException {
		List<Transition> filteredTransitions;
		if (variables == null) {
			filteredTransitions = transitions;
			logger.debug("no variables to evaluate, skipping evaluation");
		} else {
			logger.debug("evaluating condition(s)");
			filteredTransitions = new ArrayList<Transition>(transitions.size());
			for (Transition t : transitions) {
				String condition = t.getCond();
				boolean fits;
				try {
					fits = expressionEvaluator.evalCond(new JexlContext(variables), condition);
					logger.debug("condition [{}] evaluated to [{}]", condition, fits);
				} catch (SCXMLExpressionException e) {
					logger.error("state machine error evaluating condition [{}]", condition);
					throw new StateMachineConditionEvaluationException("state machine error evaluating condition [" + condition + "]");
				}
				if (fits) {
					filteredTransitions.add(t);
				}
			}

		}
		return filteredTransitions;
	}

	private List<Transition> fetchTransitions(String sourceStateName, String eventName, State sourceState) throws StateMachineTransitionException {
		List<Transition> transitions = (List<Transition>) sourceState.getTransitionsList(eventName);
		if (transitions == null || transitions.size() == 0) {
			logger.error("source state [{}] has no transition to any state via event [{}]", sourceStateName, eventName);
			throw new StateMachineTransitionException("source state [" + sourceStateName + "] has no transition to any state via event [" + eventName + "]");
		}
		return transitions;
	}

	private State fetchSourceState(String sourceStateName) throws StateMachineTransitionException {
		State sourceState = (State) getEngine().getStateMachine().getChildren().get(sourceStateName);
		if (sourceState == null) {
			logger.error("source state [{}] not found in state machine", sourceStateName);
			throw new StateMachineTransitionException("source state [" + sourceStateName + "] not found in state machine");
		}
		return sourceState;
	}
}
