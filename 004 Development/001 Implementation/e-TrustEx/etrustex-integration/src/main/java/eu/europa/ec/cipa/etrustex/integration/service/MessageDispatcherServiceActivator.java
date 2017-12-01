package eu.europa.ec.cipa.etrustex.integration.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.cipa.etrustex.integration.IMessageProcessorActivator;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IEndpointService;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessageRoutingService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;

public class MessageDispatcherServiceActivator extends TrustExServiceActivator implements IMessageProcessorActivator {
	
	@Autowired
	private IEndpointService endpointService;
	
	@Autowired
	private IInterchangeAgreementService interchangeAgreementService;
	
	@Autowired
	private IMessageRoutingService messageRoutingService;
		
	@Autowired
	private JmsTemplate toRoutingQueueTemplate;
	
	@Autowired
	private ILogService logService;
	
	private static final Logger logger = LoggerFactory
			.getLogger(MessageDispatcherServiceActivator.class);
	
	public IInterchangeAgreementService getInterchangeAgreementService() {
		return interchangeAgreementService;
	}
	public void setInterchangeAgreementService(
			IInterchangeAgreementService interchangeAgreementService) {
		this.interchangeAgreementService = interchangeAgreementService;
	}
	
	
	@Override
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message)	throws MessageProcessingException {
		return null;
	}
	
	@Override
	@Transactional
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message) throws MessageProcessingException {
		logger.debug("Entering message Dispatcher");
		TrustExMessage<String> trustExMessage = message.getPayload();
		//BusinessDomain bd = authorisationService.getParty(trustExMessage.getHeader().getSenderPartyId()).getBusinessDomain();
		InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(trustExMessage.getHeader().getInterchangeAgreementId());
		List<Endpoint> endpoints = endpointService.retrieveEndpoints(trustExMessage.getHeader().getInterchangeAgreementId(),
				trustExMessage.getHeader().getReceiverPartyId(),ica.getProfile().getId(),
				trustExMessage.getHeader().getTransactionTypeId());
		
		Transaction transaction = authorisationService.getTransactionById(message.getPayload().getHeader().getTransactionTypeId());
		trustExMessage.getHeader().setTransactionNamespace(transaction.getNamespace());
		trustExMessage.getHeader().setTransactionRequestLocalName(transaction.getRequestLocalName());
		eu.europa.ec.cipa.etrustex.domain.Message domainMessage = messageService.retrieveMessage(trustExMessage.getHeader().getMessageId());
		if (endpoints != null){
			for (Endpoint endpoint : endpoints) {
				
				if (domainMessage.getReceiver() != null && endpoint.getParty() != null 
						&& domainMessage.getReceiver().getId().longValue() != endpoint.getParty().getId().longValue()){
					//we are in the context of a third party agreement ! we need to verify the filterning for attached documents and application responses
					
					//retrieve transactions
					Set<Transaction> transactions = retrieveThirdPartyAuthorisedTransactions(
							domainMessage.getReceiver().getId(), 
							endpoint.getParty().getId());
					
					if (transactions != null && transactions.size()>0){//if no transactions -> no need to filter
						
						//TODO revise the logical: it is mainly implemented based on usage for eProcurement: 'worst' case is app resp on an attachment on a main doc (3 levels)
						if (("916".equals(domainMessage.getMessageDocumentTypeCode()) || "301".equals(domainMessage.getMessageDocumentTypeCode()))&& message.getPayload().getHeader().getParentMessageId() != null ){
							//at that moment msg_msg link could not exist (yet) in db. Get the info from the header instead.
							//eu.europa.ec.cipa.etrustex.domain.Message parent = domainMessage.getParentMessages().iterator().next(); //take first. currently we only store one parent.
							
							eu.europa.ec.cipa.etrustex.domain.Message parent = messageService.retrieveMessage(message.getPayload().getHeader().getParentMessageId());
							
							if ("916".equals(parent.getMessageDocumentTypeCode()) && parent.getParentMessages()!=null &&parent.getParentMessages().size()>0){//if parent is an attached document, check the parent!
								parent = parent.getParentMessages().iterator().next(); //take first. currently we only store one parent.
							}
							
							Transaction parentTransaction = parent.getTransaction();
							if (!transactions.contains(parentTransaction)){ //if parent transaction not in the list of authorized transactions
								continue; //just skip that endpoint
							}
						}
					}
					
				}
				
				
				//save message routing info and send its id to trustExMessageRoutingOUT channel
				MessageRouting messageRouting = new MessageRouting(endpoint, domainMessage);
				messageRouting = messageRoutingService.save(messageRouting);
				trustExMessage.getHeader().setMessageRoutingId(messageRouting.getId().toString());
				
				LogDTO logDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.DISPATCHING, 
						"Inside MessageDispatcherServiceActivator messageRouting id = " + messageRouting.getId(), 
						this.getClass().getName());				
				logService.saveLog(logDTO);		
				toRoutingQueueTemplate.convertAndSend(trustExMessage);
			}
		}
		return message;
	}
	
	protected Set<Transaction> retrieveThirdPartyAuthorisedTransactions(Long partyId, Long thirdPartyId) {
		Party authorizingParty = new Party();
		authorizingParty.setId(partyId);
		Party delegateParty = new Party();
		delegateParty.setId(thirdPartyId);

		return authorisationService.retrievePartyAgreement(authorizingParty, delegateParty).getTransactions();
	}
	
}
