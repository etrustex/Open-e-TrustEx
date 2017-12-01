package eu.europa.ec.cipa.etrustex.integration.service;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.IMessageProcessorActivator;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.XsdValidationErrorHandler;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.validation.Schema;
import java.util.Map;

public class XSDValidationServiceActivator  extends XmlProcessingServiceActivator implements IMessageProcessorActivator{

	private static final Logger logger = LoggerFactory.getLogger(XSDValidationServiceActivator.class);
	//@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public Message<TrustExMessage> processSynchMessage(Message<TrustExMessage<String>> message){
	 	Map<MetaDataItemType,MetaDataItem> metadata = message.getPayload().getHeader().getMetadata();
	  	MetaDataItem validateXsdItem =  metadata.get(MetaDataItemType.SYNCH_VALIDATE_XSD);
	  	MetaDataItem xsdItem 	   	 =  metadata.get(MetaDataItemType.DOCUMENT_XSD);
		MetaDataItem xsdURLItem	   	 =  metadata.get(MetaDataItemType.DOCUMENT_XSD_URL);
		MetaDataItem serverURLItem 	 =  metadata.get(MetaDataItemType.SERVER_URL);
		Transaction t = authorisationService.getTransactionById(message.getPayload().getHeader().getTransactionTypeId());
		
		if (validateXsdItem != null && Boolean.valueOf(validateXsdItem.getValue())){
			XsdValidationErrorHandler handler = null;
			try {
				Schema schema = getSchemaFromMetadata(xsdItem, xsdURLItem, serverURLItem);
		  		if (schema != null){
					handler = validateXSD(schema, message); 
				}
			}catch (Exception e) {
				logger.error("Technical error occured during XSD validation " +e.getMessage(),e);
				throw new MessageProcessingException("soapenv:Server",
						ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
  			if (handler!= null && !handler.isValid()) {
  				logger.error("Xsd validation generated the following errors :"+handler.getErrorMessage());
  				throw new MessageProcessingException("soapenv:Client",
						ErrorResponseCode.DOCUMENT_XSD_INVALID.getDescription(), null,
						ErrorResponseCode.DOCUMENT_XSD_INVALID, null, null);
  			}
	  		
	  	}
		MessageBuilder<TrustExMessage> builder = MessageBuilder.withPayload((TrustExMessage)message.getPayload()).copyHeaders(message.getHeaders());
		return builder.build();
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message){
	 	Map<MetaDataItemType,MetaDataItem> metadata = message.getPayload().getHeader().getMetadata();
	  	MetaDataItem validateXsdItem=  metadata.get(MetaDataItemType.SYNCH_VALIDATE_XSD);
	  	MetaDataItem xsdItem =  metadata.get(MetaDataItemType.DOCUMENT_XSD);
	  	MetaDataItem xsdURLItem=  metadata.get(MetaDataItemType.DOCUMENT_XSD_URL);
	  	MetaDataItem serverURLItem 	 =  metadata.get(MetaDataItemType.SERVER_URL);
	  	
		LogDTO logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.VALIDATION, 
				"Inside XSDValidationServiceActivator", 
				this.getClass().getName());	
	  	
	  	if (validateXsdItem != null && !Boolean.valueOf(validateXsdItem.getValue()) ){
	  		try {
	  			Schema schema = getSchemaFromMetadata(xsdItem, xsdURLItem, serverURLItem);
		  		if (schema != null){
		  			XsdValidationErrorHandler handler = validateXSD(schema, message);
		  			if (!handler.isValid()) {	
	  					logDTO.setDescription(logDTO.getDescription() + " XSD validation Failed");
                        logDTO.setLargeValue(handler.getErrorMessage());
	  					logService.saveLog(logDTO);		  				
		  				eu.europa.ec.cipa.etrustex.domain.Message msgDB = messageService.retrieveMessage(message.getPayload().getHeader().getMessageId());
						String appResponseId = message.getPayload().getHeader().getMessageDocumentId()+"_"+msgDB.getMessageDocumentTypeCode()+"_ERR"; 				
	  					createApplicationResponse(message.getPayload(), appResponseId, ErrorResponseCode.DOCUMENT_XSD_INVALID_ASYNC.getCode(), 
						handler.getErrorMessage(), message.getPayload().getHeader().getReplyTo());
	  					return null;
		  			}
		  		}
	  		} catch (Exception e) {
	  			logger.error("Technical error occured during XSD validation " +e.getMessage(),e);
				logDTO.setDescription(logDTO.getDescription() + " Technical error occured during XSD validation " + e.getMessage());
				logService.saveLog(logDTO);
	  			errorChannel.send(message);
	  			return null;
	  		}
	  	}
		MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
		return builder.build();
	}
	

}
