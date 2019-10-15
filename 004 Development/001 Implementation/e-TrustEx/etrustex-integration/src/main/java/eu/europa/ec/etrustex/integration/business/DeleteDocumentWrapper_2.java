package eu.europa.ec.etrustex.integration.business;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;

public class DeleteDocumentWrapper_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(DeleteDocumentWrapper_2.class);
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(
			TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
		responseMessage.setHeader(message.getHeader());
		Message toDelete = messageService.retrieveMessage(
					message.getHeader().getMessageDocumentId(), 
					message.getHeader().getDocumentTypeCode(), 
					message.getHeader().getSenderPartyId(), 
					null);
		if (toDelete == null){
			throw new BusinessException("soapenv:Client", ErrorResponseCode.DELETE_DWR_NON_EXISTING_DOCUMENT.getDescription(), null,
					ErrorResponseCode.DELETE_DWR_NON_EXISTING_DOCUMENT, null, soapErrorMessages.getProperty("error.hardrule.document.notFound"));
		}
		if (toDelete.getParentMessages() != null && toDelete.getParentMessages().size() >0){
			throw new BusinessException("soapenv:Client", ErrorResponseCode.DELETE_DWR_DOCUMENT_LINKED_TO_BUNDLE.getDescription(), null,
					ErrorResponseCode.DELETE_DWR_DOCUMENT_LINKED_TO_BUNDLE, null, soapErrorMessages.getProperty("error.hardrule.document.hasParent"));
		}
		
		try {
			messageService.deleteMessage(toDelete);
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(),e);
			throw new BusinessException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
		return responseMessage;
		
	}

}
