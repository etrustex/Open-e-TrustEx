package eu.europa.ec.cipa.etrustex.integration.service;

import java.util.Calendar;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.AuthorisationFailedException;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class AuthorisationServiceActivator  extends TrustExServiceActivator{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorisationServiceActivator.class);

	
	public Message<TrustExMessage<String>> checkSubmitAuthorisation(Message<TrustExMessage<String>> message) {
		logger.info("transaction Id:" + message.getPayload().getHeader().getTransactionTypeId());
		try {
			message.getPayload().setHeader(enrichMessageHeader(message.getPayload().getHeader()));
			Party issuerParty = authorisationService.getMessageIssuer(message.getPayload().getHeader().getAuthenticatedUser());
			
			message.getPayload().getHeader().setIssuerPartyId(issuerParty.getId());
			message.getPayload().getHeader().setIssuer(issuerParty);
			
			BusinessDomain bd = issuerParty!=null?issuerParty.getBusinessDomain():null;
			Party sender = authorisationService.getParty(message.getPayload().getHeader().getSenderIdWithScheme(),bd);
			message.getPayload().getHeader().setSenderPartyId(sender.getId());			
			message.getPayload().getHeader().setSender(sender);
			
			//ETRUSTEX-1264
			TrustExMessageHeader header = message.getPayload().getHeader();
			if (header.isMulticastSupported()){
				
				//ETRUSTEX-1320
				if (CollectionUtils.isEmpty(header.getReceiverIdWithSchemeList())) {
					throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
							ErrorResponseCode.AUTHENTICATION_FAILED, null, null);					
				}				
				
				for (String receiveIdWithScheme : header.getReceiverIdWithSchemeList()){
					
					Party receiver = authorisationService.getParty(receiveIdWithScheme, bd);					
									
					InterchangeAgreement agreement= authorisationService.checkAuthorisation(issuerParty, 
							sender,receiver, message.getPayload().getHeader().getTransactionTypeId());
					//if one channel is not OK throw unauthorized access
					if (agreement.getValidityStartDate() != null && agreement.getValidityStartDate().after(Calendar.getInstance().getTime())){
						throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
								ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
					}					
					
					message.getPayload().getHeader().getInterchangeAgreementIdByreceiverIdWithScheme().put(receiveIdWithScheme, agreement.getId());
					message.getPayload().getHeader().getReceiverIdByreceiverIdWithScheme().put(receiveIdWithScheme, receiver.getId());
					
					
					String description = "Submit authorization succeeded.";
					LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.OTHER, 
							description, this.getClass().getName());			
					logDTO.setReceiverParty(receiver);		
					logService.saveLog(logDTO);
					
				}
				
			}else{
			
				Party receiver = authorisationService.getParty(message.getPayload().getHeader().getReceiverIdWithScheme(), bd);			
				
				message.getPayload().getHeader().setReceiverPartyId(receiver.getId());
				
				//message.getPayload().getHeader().setReceiver(receiver);
				
								
				InterchangeAgreement agreement= authorisationService.checkAuthorisation(issuerParty, 
						sender,receiver, message.getPayload().getHeader().getTransactionTypeId());
				if (agreement.getValidityStartDate() != null && agreement.getValidityStartDate().after(Calendar.getInstance().getTime())){
					throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
							ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
				}
				
				message.getPayload().getHeader().setInterchangeAgreementId(agreement.getId());
			}
			MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
			
			return builder.build();
		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage()/*,e*/); //no need to show the full trace
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
		} catch (AuthorisationFailedException e) {
			logger.error(e.getMessage()/*,e*/);//no need to show the full trace
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
		}
	}
	
	public Message<TrustExMessage<String>> checkSynchAuthorisation(Message<TrustExMessage<String>> message) {
		logger.info("transaction Id:" + message.getPayload().getHeader().getTransactionTypeId());
		try {
			message.getPayload().setHeader(enrichMessageHeader(message.getPayload().getHeader()));
			Party issuerParty = authorisationService.getMessageIssuer(message.getPayload().getHeader().getAuthenticatedUser());	
			Party senderParty = authorisationService.getParty(message.getPayload().getHeader().getSenderIdWithScheme(), issuerParty.getBusinessDomain());
						
			if(message.getPayload().getHeader().getReceiverIdWithScheme() != null){
				Party receiver = authorisationService.getParty(message.getPayload().getHeader().getReceiverIdWithScheme(), issuerParty.getBusinessDomain());
				message.getPayload().getHeader().setReceiverPartyId(receiver.getId());
			}
			message.getPayload().getHeader().setIssuerPartyId(issuerParty.getId());
			message.getPayload().getHeader().setIssuer(issuerParty);
			message.getPayload().getHeader().setSender(senderParty);
			message.getPayload().getHeader().setSenderPartyId(senderParty.getId());
			authorisationService.checkTransactionAuthorisation(issuerParty, senderParty,  message.getPayload().getHeader().getTransactionTypeId());
			MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
			return builder.build();
		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage()/*,e*/);//no need to show the full trace		
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
		} catch (AuthorisationFailedException e) {
			logger.error(e.getMessage()/*,e*/);//no need to show the full trace		
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
		} 
		
	}	
	

}
