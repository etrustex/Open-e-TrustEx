package eu.europa.ec.etrustex.integration.service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.transaction.UnexpectedRollbackException;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.IMessageProcessorActivator;
import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.types.DocumentStatusCode;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import eu.europa.ec.etrustex.types.ResponseCode;
import freemarker.template.TemplateException;

public class BusinessServiceActivator extends TrustExServiceActivator implements ApplicationContextAware, IMessageProcessorActivator {
	
	private final String AVAILABLE_STATUS = "CREATE_STATUS_AVAILABLE"; 
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessServiceActivator.class);
	private static ApplicationContext ctx;
	
	private String getBusisnessServiceName(TrustExMessage<String> message) {
		TrustExMessageHeader header = message.getHeader();
		String serviceName = null;
		Map<MetaDataItemType,MetaDataItem> md  = header.getMetadata();
		if (md == null){
			md = metadataService.retrieveMetaData(header.getInterchangeAgreementId(),
					header.getTransactionTypeId(), header.getDocumentTypeId(),null, null);
		}
		MetaDataItem businessServiceItem = md.get(MetaDataItemType.BUSINESS_SERVICE_BEAN);
		// we check if the business service is configured in the metadata
		if (businessServiceItem != null){
			serviceName = businessServiceItem.getValue();
		}
		// if not we use a naming convention to look up the business service in the appcontext
		else {
			Transaction t = authorisationService.getTransactionById(header.getTransactionTypeId());
			serviceName= t.getName()+"-"+t.getVersion();
		}
		return serviceName;
	}
	
	@Override
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message) {
		TrustExMessage response = message.getPayload() ;
		LogDTO logDTO = logServiceHelper.createLog(response, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.BUSINESS, 
				"Inside BusinessServiceActivator", 
				this.getClass().getName());;
		try {String beanName = getBusisnessServiceName(message.getPayload());
			logger.debug("bean name="+beanName);
			
			if (beanName != null){
				ISynchBusinessService businessService = ctx.getBean(beanName,ISynchBusinessService.class);
				if (businessService != null){
					response = businessService.processMessage(message.getPayload());
				}
			}

		}catch (NoSuchBeanDefinitionException nsbe){
			logger.error("Technical Error Occured while while calling the synch business service", nsbe);
			// for synch services the business service must be defined in order to populate the response the response 
			logDTO.setDescription("Technical Error Occured while while calling the synch business service " + nsbe.getMessage());
			logService.saveLog(logDTO);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} catch (BusinessException e) {
			//No need for stack trace if it is a business exception (DERVEOL)
			logger.error(e.getMessage()/*, e*/);
			logDTO.setDescription("BusinessException " + e.getDescription());
			logService.saveLog(logDTO);
			throw new MessageProcessingException(e.getFault(),e.getDescription(),null,e.getFaultDetailResponseCode(), null, e.getFaultDetailDescription());
		} catch (TechnicalErrorException e) {
			logger.error("Technical Error Occured while while calling the synch business service", e);
			//TODO (derveol): investigate: if we keep this we have a JMS message error. 
			//errorChannel.send(message); 
			//we need to throw a server error
			logDTO.setDescription("TechnicalErrorException " + e.getDescription());
			logService.saveLog(logDTO);
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}catch (UnexpectedRollbackException e) {
			logDTO.setDescription("UnexpectedRollbackException " + e.getMessage());
			logService.saveLog(logDTO);
			if (message.getPayload().getHeader().getBusinessException()!=null){
				BusinessException businessException = message.getPayload().getHeader().getBusinessException();
				logger.error("UnexpectedRollbackException but there is a BusinessException is the header");
				logger.error(businessException.getMessage()/*, e*/);
				throw new MessageProcessingException(businessException.getFault(),businessException.getDescription(),null,businessException.getFaultDetailResponseCode(), null, null);
			}else{
				logger.error("Technical Error Occured while while calling the synch business service", e);
				throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}catch(MessageProcessingException e){
			logger.error("Unknown Error Occured while while calling the synch business service", e);//catch transaction rollback problems
			logDTO.setDescription("MessageProcessingException " + e.getDescription());
			logService.saveLog(logDTO);
			//ETRUSTEX-648			
			throw e;
		}catch(Exception e){
			logDTO.setDescription("Exception " + e.getMessage());
			logService.saveLog(logDTO);
			logger.error("Unknown Error Occured while while calling the synch business service", e);		
			throw new MessageProcessingException("soapenv:Server",
					ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		
		}
		MessageBuilder<TrustExMessage> builder = MessageBuilder.withPayload(response).copyHeaders(message.getHeaders());
		return builder.build();
	}
	
	@Override
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message){
		TrustExMessage<String> response = message.getPayload();
		LogDTO logDTO = null;
		try {
			logDTO = logServiceHelper.createLog(response, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.BUSINESS, 
					"Inside BusinessServiceActivator", 
					this.getClass().getName());

			String beanName = getBusisnessServiceName(message.getPayload());
			if (beanName != null && ctx.containsBean(beanName)){
				IASynchBusinessService businessService = ctx.getBean(beanName,IASynchBusinessService.class);
				if (businessService != null){
					response = businessService.processMessage(message.getPayload());
				}
			}
			//update response code as well
			messageService.updateMessage(message.getPayload().getHeader().getMessageId(),	DocumentStatusCode.RECEIVED.name(), message.getPayload().getHeader().getResponseCode());
			//update  header with new status info
			message.getPayload().getHeader().setMessageStatusCode(DocumentStatusCode.RECEIVED.name());
			
			
			if(AVAILABLE_STATUS.equals(message.getPayload().getHeader().getAvailableNotification())){
				String documentTypeCode = authorisationService.getTransactionById(message.getPayload().getHeader().getTransactionTypeId()).getDocument().getDocumentTypeCode();
				String appResponseId = generateMessageId(message.getPayload().getHeader().getMessageDocumentId(), documentTypeCode, MSG_ID_OUTCOME.OK);
				createApplicationResponse(message.getPayload(), appResponseId,
						ResponseCode.NOTIFICATION_AVAILABLE.getCode(),"Available",message.getPayload().getHeader().getReplyTo(), false, true);
			}
		} catch (BusinessException e) {
			try {
				//No need for stack trace if it is a business exception (DERVEOL)
				//logger.error("Business error occured",e);
				logDTO.setDescription(logDTO.getDescription() + " " + e.getDescription());
				logService.saveLog(logDTO);
				eu.europa.ec.etrustex.domain.Message msgDB = messageService.retrieveMessage(message.getPayload().getHeader().getMessageId());
				String appResponseId = generateMessageId(message.getPayload().getHeader().getMessageDocumentId(), msgDB.getMessageDocumentTypeCode(), MSG_ID_OUTCOME.ERR);
				createApplicationResponse(message.getPayload(),appResponseId,e.getFaultDetailResponseCode().getCode(), e.getFaultDetailDescription(), message.getPayload().getHeader().getReplyTo());
				return null;
			} catch (Exception e2) {
				logger.error("Technical Error Occured upon Application response creation ", e2);
				logDTO.setDescription(logDTO.getDescription() + " Technical Error Occured upon Application response creation " + e.getMessage());
				logService.saveLog(logDTO);
			}
		} catch (TechnicalErrorException e) {
			logger.error("Technical Error Occured while while calling the asynch business service", e);
			logDTO.setDescription(logDTO.getDescription() + 
					" Technical Error Occured while calling the asynch business service " + e.getMessage());
			logService.saveLog(logDTO);
			errorChannel.send(message);
			return null;
		}
		catch (MessageUpdateException e) {
			logger.error("Technical Error Occured while trying to upate the message Status ", e);
			logDTO.setDescription(logDTO.getDescription() + 
					" Technical Error Occured while trying to upate the message Status " + e.getMessage());
			logService.saveLog(logDTO);
			errorChannel.send(message);
			return null;
		} catch (IOException e) {
			logger.error("Technical Error Occured while trying to upate the message Status ", e);
			logDTO.setDescription(logDTO.getDescription() 
					+ " Technical Error Occured while trying to upate the message Status " + e.getMessage());
			logService.saveLog(logDTO);
			errorChannel.send(message);
			return null;
		} catch (TemplateException e) {
			logger.error("Technical Error Occured while trying to upate the message Status ", e);
			logDTO.setDescription(logDTO.getDescription() 
					+ " Technical Error Occured while trying to upate the message Status " + e.getMessage());
			logService.saveLog(logDTO);
			errorChannel.send(message);
			return null;
		}
		MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
		return builder.build();
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}	
}
