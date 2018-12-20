package eu.europa.ec.etrustex.integration.service;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.AuthorisationFailedException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class AuthorisationServiceActivator  extends TrustExServiceActivator{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorisationServiceActivator.class);

    @Autowired
    private Properties soapErrorMessages;
	
	public Message<TrustExMessage<String>> checkSubmitAuthorisation(Message<TrustExMessage<String>> message) {
		logger.info("transaction Id:" + message.getPayload().getHeader().getTransactionTypeId());

        message.getPayload().setHeader(enrichMessageHeader(message.getPayload().getHeader()));
        Party issuerParty;
        try {
            issuerParty = authorisationService.getMessageIssuer(message.getPayload().getHeader().getAuthenticatedUser());
        } catch (UndefinedIdentifierException e1) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                    ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.credentials.noIssuer"));
        }

        message.getPayload().getHeader().setIssuerPartyId(issuerParty.getId());
        message.getPayload().getHeader().setIssuer(issuerParty);

        BusinessDomain bd = issuerParty != null ? issuerParty.getBusinessDomain() : null;


        Party sender;
        try {
            sender = authorisationService.getParty(message.getPayload().getHeader().getSenderIdWithScheme(), bd);
        } catch (UndefinedIdentifierException e1) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                    ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.senderParty.unknown"));
        }


        message.getPayload().getHeader().setSenderPartyId(sender.getId());
        message.getPayload().getHeader().setSender(sender);

        //ETRUSTEX-1264
        TrustExMessageHeader header = message.getPayload().getHeader();

        // ETRUSTEX-2131
        Profile profile = null;

        String profileName = message.getPayload().getHeader().getProfileName();
        if (StringUtils.isNotEmpty(profileName)) {
            List<Profile> profiles = profileService.findProfilesByCriteria(message.getPayload().getHeader().getProfileName(), null, message.getPayload().getHeader().getSender().getBusinessDomain());
            if (profiles.size() == 1) {
                profile = profiles.get(0);
            } else {
                throw new MessageProcessingException("soapenv:Client", "The provided Profile Name does not exist", null,
                        ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.nonExistingProfile"));
            }
        }

        if (header.isMulticastSupported()) {

            //ETRUSTEX-1320
            if (CollectionUtils.isEmpty(header.getReceiverIdWithSchemeList())) {
                throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                        ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.receiverParty.missing"));
            }

            for (String receiveIdWithScheme : header.getReceiverIdWithSchemeList()) {

                Party receiver;
                try {
                    receiver = authorisationService.getParty(receiveIdWithScheme, bd);
                } catch (UndefinedIdentifierException e) {
                    throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                            ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.receiverParty.unknown"));
                }

                InterchangeAgreement agreement;
                try {
                    agreement = authorisationService.checkAuthorisation(issuerParty,
							sender,receiver, message.getPayload().getHeader().getTransactionTypeId(), profile);
                } catch (AuthorisationFailedException e) {
                    throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                    		ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty(e.getMessage()));
                }
                //if one channel is not OK throw unauthorized access
                if (agreement.getValidityStartDate() != null && agreement.getValidityStartDate().after(Calendar.getInstance().getTime())) {
                    throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                            ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.ICAStartDate"));
                }

                message.getPayload().getHeader().getInterchangeAgreementIdByreceiverIdWithScheme().put(receiveIdWithScheme, agreement.getId());
                message.getPayload().getHeader().getReceiverIdByreceiverIdWithScheme().put(receiveIdWithScheme, receiver.getId());


                String description = "Submit authorization succeeded.";
                LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.OTHER,
                        description, this.getClass().getName());
                logDTO.setReceiverParty(receiver);
                logService.saveLog(logDTO);

            }

        } else {

            Party receiver;
            try {
                receiver = authorisationService.getParty(message.getPayload().getHeader().getReceiverIdWithScheme(), bd);
            } catch (UndefinedIdentifierException e) {
                logger.error(e.getMessage()/*,e*/); //no need to show the full trace
                throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                        ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.receiverParty.unknown"));
            }

            message.getPayload().getHeader().setReceiverPartyId(receiver.getId());

            //TODO ETRUSTEX-1656
            InterchangeAgreement agreement;
            try {
                agreement = authorisationService.checkAuthorisation(issuerParty,
						sender,receiver, message.getPayload().getHeader().getTransactionTypeId(), profile);
            } catch (AuthorisationFailedException e) {
                throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                        ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty(e.getMessage()));
            }
            if (agreement.getValidityStartDate() != null && agreement.getValidityStartDate().after(Calendar.getInstance().getTime())) {
                throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                        ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.ICAStartDate"));
            }
            message.getPayload().getHeader().setInterchangeAgreementId(agreement.getId());

        }
        MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());

        return builder.build();

    }

    public Message<TrustExMessage<String>> checkSynchAuthorisation(Message<TrustExMessage<String>> message) {
        logger.info("transaction Id:" + message.getPayload().getHeader().getTransactionTypeId());

        message.getPayload().setHeader(enrichMessageHeader(message.getPayload().getHeader()));
        Party issuerParty;
        try {
            issuerParty = authorisationService.getMessageIssuer(message.getPayload().getHeader().getAuthenticatedUser());
		} catch (UndefinedIdentifierException e) {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                    ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.credentials.noIssuer"));
        }

        Party senderParty;
        try {
            senderParty = authorisationService.getParty(message.getPayload().getHeader().getSenderIdWithScheme(), issuerParty.getBusinessDomain());
        } catch (UndefinedIdentifierException e) {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                    ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.senderParty.unknown"));
        }

        if (message.getPayload().getHeader().getReceiverIdWithScheme() != null) {
            Party receiver;
            try {
                receiver = authorisationService.getParty(message.getPayload().getHeader().getReceiverIdWithScheme(), issuerParty.getBusinessDomain());
            } catch (UndefinedIdentifierException e) {
                throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                        ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.receiverParty.unknown"));
            }
            message.getPayload().getHeader().setReceiverPartyId(receiver.getId());
        }
        message.getPayload().getHeader().setIssuerPartyId(issuerParty.getId());
        message.getPayload().getHeader().setIssuer(issuerParty);
        message.getPayload().getHeader().setSender(senderParty);
        message.getPayload().getHeader().setSenderPartyId(senderParty.getId());
        try {
			authorisationService.checkTransactionAuthorisation(issuerParty, senderParty,  message.getPayload().getHeader().getTransactionTypeId());
		} catch (AuthorisationFailedException e) {
			logger.error(e.getMessage()/*,e*/);//no need to show the full trace		
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
                    ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty(e.getMessage()));
        }
        MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
        return builder.build();
	}	
	

}
