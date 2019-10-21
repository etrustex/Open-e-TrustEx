package eu.europa.ec.etrustex.integration.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.etrustex.integration.IMessageProcessorActivator;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.services.IEndpointService;
import eu.europa.ec.etrustex.services.IMessageRoutingService;

public class MessageDispatcherServiceActivator extends TrustExServiceActivator implements IMessageProcessorActivator {
	
	@Autowired
	private IEndpointService endpointService;
		
	@Autowired
	private IMessageRoutingService messageRoutingService;
		
	@Autowired
	private JmsTemplate toRoutingQueueTemplate;
		
	private static final Logger logger = LoggerFactory
			.getLogger(MessageDispatcherServiceActivator.class);
		
	
	@Override
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message)	throws MessageProcessingException {
		throw new UnsupportedOperationException("Dispatching in the synchronous part is not supported");
	}
	
	@Override
	@Transactional
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message) throws MessageProcessingException {
		logger.debug("Entering message Dispatcher");
		TrustExMessage<String> trustExMessage = message.getPayload();
		InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(trustExMessage.getHeader().getInterchangeAgreementId());
		Set<Endpoint> endpoints = endpointService.retrieveEndpoints(trustExMessage.getHeader().getInterchangeAgreementId(),
				trustExMessage.getHeader().getReceiverPartyId(),ica.getProfile().getId(),
				trustExMessage.getHeader().getTransactionTypeId());
				
		Transaction transaction = authorisationService.getTransactionById(message.getPayload().getHeader().getTransactionTypeId());
		trustExMessage.getHeader().setTransactionNamespace(transaction.getNamespace());
		trustExMessage.getHeader().setTransactionRequestLocalName(transaction.getRequestLocalName());
		eu.europa.ec.etrustex.domain.Message domainMessage = messageService.retrieveMessage(trustExMessage.getHeader().getMessageId());
		if (endpoints == null) {
			return message;
		}
		for (Endpoint endpoint : endpoints) {
			dispatch(endpoint, domainMessage, message);
		}
		return message;
	}
	
	private void dispatch(Endpoint endpoint, eu.europa.ec.etrustex.domain.Message domainMessage, Message<TrustExMessage<String>> message) {
		if (domainMessage.getReceiver() != null && endpoint.getParty() != null 
				&& domainMessage.getReceiver().getId().longValue() != endpoint.getParty().getId().longValue()){
			//we are in the context of a third party agreement ! we need to verify the filtering for attached documents and application responses
			
			//retrieve transactions
			Set<Transaction> transactions = retrieveThirdPartyAuthorisedTransactions(
					domainMessage.getReceiver().getId(), 
					endpoint.getParty().getId());
			
			if (transactions != null && transactions.size()>0){//if no transactions -> no need to filter
				
				//TODO revise the logical: it is mainly implemented based on usage for eProcurement: 'worst' case is app resp on an attachment on a main doc (3 levels)
				if (("916".equals(domainMessage.getMessageDocumentTypeCode()) || "301".equals(domainMessage.getMessageDocumentTypeCode()))&& message.getPayload().getHeader().getParentMessageId() != null ){
					//at that moment msg_msg link could not exist (yet) in db. Get the info from the header instead.
					//eu.europa.ec.cipa.etrustex.domain.Message parent = domainMessage.getParentMessages().iterator().next(); //take first. currently we only store one parent.
					
					eu.europa.ec.etrustex.domain.Message parent = messageService.retrieveMessage(message.getPayload().getHeader().getParentMessageId());
					
					if ("916".equals(parent.getMessageDocumentTypeCode()) && parent.getParentMessages()!=null &&parent.getParentMessages().size()>0){//if parent is an attached document, check the parent!
						parent = parent.getParentMessages().iterator().next(); //take first. currently we only store one parent.
					}
					
					Transaction parentTransaction = parent.getTransaction();
					if (!transactions.contains(parentTransaction)){ //if parent transaction not in the list of authorized transactions
						return; //just skip the current endpoint
					}
				}
			}
			
		}
		
		
		//save message routing info and send its id to trustExMessageRoutingOUT channel
		MessageRouting messageRouting = new MessageRouting(endpoint, domainMessage);
		messageRouting = messageRoutingService.save(messageRouting);
		message.getPayload().getHeader().setMessageRoutingId(messageRouting.getId().toString());
		
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.DISPATCHING, 
				"Inside MessageDispatcherServiceActivator messageRouting id = " + messageRouting.getId(), 
				this.getClass().getName());				
		logService.saveLog(logDTO);		
		toRoutingQueueTemplate.convertAndSend(message.getPayload());		
	}	
	
	protected Set<Transaction> retrieveThirdPartyAuthorisedTransactions(Long partyId, Long thirdPartyId) {
		Party authorizingParty = new Party();
		authorizingParty.setId(partyId);
		Party delegateParty = new Party();
		delegateParty.setId(thirdPartyId);

		return authorisationService.retrievePartyAgreement(authorizingParty, delegateParty).getTransactions();
	}
	
}
