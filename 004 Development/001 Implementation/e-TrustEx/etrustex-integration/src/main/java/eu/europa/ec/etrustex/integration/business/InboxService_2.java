package eu.europa.ec.etrustex.integration.business;

import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2.DocumentReferenceResponseParentDocsType;
import ec.schema.xsd.inboxresponse_2.InboxResponseType;
import ec.schema.xsd.inboxresponse_2.ObjectFactory;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public class InboxService_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(InboxService_2.class);
	
	private static final int MAX_NUMBER_INBOX_LINES = 500;
	
	@Override
	@Transactional(propagation=Propagation.MANDATORY)
	public TrustExMessage<JAXBElement> processMessage(TrustExMessage<String> message)
			throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage;
		try {
			ObjectFactory factory = new ObjectFactory();
			InboxResponseType response = new InboxResponseType();
			List<DocumentReferenceResponseParentDocsType> documentReferenceResponseParentDocs = response.getDocumentReferenceResponseParentDocs();
			Set<Transaction> transactions = null;
			if (message.getHeader().getIssuerPartyId().compareTo(message.getHeader().getSenderPartyId()) != 0){
				transactions= retrieveThirdPartyAuthorisedTransactions(message.getHeader());
			}
			
			Boolean filterRS = Boolean.FALSE;
			
			MetaDataItem mdi = message.getHeader().getMetadata().get(MetaDataItemType.FILTER_READ_SERVICES_RESULTS);
			if(mdi != null && Boolean.parseBoolean(mdi.getValue()) == true){
				filterRS = Boolean.TRUE;
			}
			MessageQueryDTO mq = new MessageQueryDTO();
			mq.setReceiverPartyId(message.getHeader().getSenderPartyId());
			mq.setTransactions(transactions);
			mq.setRetrievedInd(Boolean.FALSE);
			mq.setFetchParents(Boolean.TRUE);
			mq.setFilterReadServices(filterRS);
			mq.setParentIssuerId(message.getHeader().getIssuerPartyId());
			mq.setInboxServiceFilter(Boolean.TRUE );
			mq.setMaxResult(MAX_NUMBER_INBOX_LINES);
			List<Message> inbox = messageService.retrieveMessages(mq); 

			for (Message msg : inbox) {
				DocumentReferenceResponseParentDocsType docParentRef = new DocumentReferenceResponseParentDocsType();
				documentReferenceResponseParentDocs.add(docParentRef);
				
				docParentRef.setDocumentReferenceResponse(populateDocumentReferenceResponseType(msg));
				Set<Message> parent = msg.getParentMessages();
				for (Message parentMsg : parent) {
					if ( !DOC_WRAPPER_DOC_NM.equalsIgnoreCase(parentMsg.getTransaction().getDocument().getName())){
						
						//we show the parent anyway (and we expect only one parent) 
						docParentRef.getParentDocument().add(populateDocumentReferenceResponseType(parentMsg));
					}
				}
			}
			Transaction t= authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName =factory.createInboxResponse(response).getName().getLocalPart();
			JAXBElement<InboxResponseType> responseElement = new JAXBElement<InboxResponseType>(
					new QName(t.getNamespace(),
							responseElementLocalName, "ec"), 
							InboxResponseType.class,response);
			responseMessage = new TrustExMessage<JAXBElement>(responseElement);
			responseMessage.setHeader(message.getHeader());
			return responseMessage;

		} catch (Exception e) {
			logger.error("Error Occured querying the inbox ", e);
			throw new BusinessException(e.getMessage(),e.getCause().getMessage());
		}
		
	}

}
