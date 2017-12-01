package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.dto.SlaPolicySearchDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.SlaType;

public class SubmitDocumentBundle_2 extends TrustExBusinessService implements
		IASynchBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(SubmitDocumentBundle_2.class);

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException,TechnicalErrorException {
		try {
			
			Transaction documentWrappertransaction= authorisationService.getTransactionByNameSpace(
					"ec:services:wsdl:DocumentWrapper-2", "StoreDocumentWrapperRequest");			
			
			final Configuration config = new Configuration();
			final DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
			final StaticQueryContext sqc = config.newStaticQueryContext();
			
			Message parentMessage  =messageService.retrieveMessage(message.getHeader().getMessageId());
			String doctypeCode =parentMessage.getTransaction().getDocument().getDocumentTypeCode();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			
			
			
			
			dynamicContext.setContextItem(docInfo);
			
			final XQueryExpression exp = sqc
					.compileQuery("/*:DocumentBundle/*:DocumentWrapperReference/*:ID");

			final SequenceIterator<Item> iter = exp.iterator(dynamicContext);
			List<Message> messagesToUpdate = new ArrayList<Message>();
			List<String> wrappersIDs = new ArrayList<String>();
			while (true) {
				Item item = iter.next();
				if (item == null){
					break;
				}
				String wrapperDocId = item.getStringValue();
				if (wrappersIDs.contains(wrapperDocId)){
					throw new BusinessException( "soapenv:Client","Document bundle Reference must be unique", null,ErrorResponseCode.DOCUMENT_BUNDLE_DOCUMENT_REFERENCE_MUST_BE_UNIQUE,doctypeCode ,ErrorResponseCode.DOCUMENT_BUNDLE_DOCUMENT_REFERENCE_MUST_BE_UNIQUE.getDescription());
				}else {
					wrappersIDs.add(wrapperDocId);
				}
				Message msg = messageService.retrieveMessage(item.getStringValue(), message.getHeader().getSenderPartyId(), documentWrappertransaction.getId());
				if(msg == null ){
					throw new BusinessException( "soapenv:Client","Trying to Link unexisting wrapper to Document Bundle", null,ErrorResponseCode.DOCUMENT_BUNDLE_NON_EXISTING_DOCUMENT_REFERENCE,doctypeCode ,ErrorResponseCode.DOCUMENT_BUNDLE_NON_EXISTING_DOCUMENT_REFERENCE.getDescription());
				}
				messagesToUpdate.add(msg);
			}
			
			//ETRUSTEX-623 validate number of wrappers against the SLA policy
			Party sender = authorisationService.getParty(message.getHeader().getSenderPartyId());
			if (!validateNumberOfWrappers(sender, wrappersIDs.size())) {
				throw new BusinessException( "soapenv:Client","Maximum number of wrappers exceeded", 
						null,ErrorResponseCode.DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED,
						doctypeCode ,ErrorResponseCode.DOCUMENT_BUNDLE_MAX_NO_OF_WRAPPERS_EXCEEDED.getDescription());
			}
			
			for (Message messageToUpdate : messagesToUpdate) {
				if(messageToUpdate.getParentMessages() != null && !messageToUpdate.getParentMessages().contains(parentMessage)){
					messageToUpdate.addParentMessage(parentMessage);
				}
				messageService.updateMessage(messageToUpdate);
			}
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Unable to retrieve parent information with query ", null,
					"301", "Technical Error Occured");
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Unable to update the message status", null, "301",
					"Technical Error Occured");
		}
			
		
		return message;
		
	}
	
	private boolean validateNumberOfWrappers(Party sender, int wrapperCount) {
		SlaPolicySearchDTO slaPolicySearchDTO = new SlaPolicySearchDTO();
		slaPolicySearchDTO.setSender(sender);
		slaPolicySearchDTO.setSlaType(SlaType.SLA_COUNT);
		return slaPolicyService.validateNumberOfWrappers(slaPolicySearchDTO, wrapperCount);
	}

}
