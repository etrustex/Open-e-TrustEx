package eu.europa.ec.cipa.etrustex.integration.business;

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
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;

public class InboxService_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(InboxService_2.class);
	
	private static final int MAX_NUMBER_INBOX_LINES = 500;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
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
			List<Message> inbox = messageService.retrieveMessages(message.getHeader().getSenderPartyId(), null, null, null, transactions, null, null,null,false, true, false,true); //don't limit query to 500 because some msgs can be not displayed
			int counter = 0;
			for (Message msg : inbox) {
				
				if (transactions != null && transactions.size()>0){//if no transactions -> no need to filter
					
					//TODO revise the logical: it is mainly implemented based on usage for eProcurement: 'worst' case is app resp on an attachment on a main doc (3 levels)
					if (("916".equals(msg.getMessageDocumentTypeCode()) || "301".equals(msg.getMessageDocumentTypeCode()))&& msg.getParentMessages()!=null &&msg.getParentMessages().size()>0){
						Message parent = msg.getParentMessages().iterator().next(); //take first. currently we only store one parent.
						
						if ("916".equals(parent.getMessageDocumentTypeCode()) && parent.getParentMessages()!=null &&parent.getParentMessages().size()>0){//if parent is an attached document, check the parent!
							parent = parent.getParentMessages().iterator().next(); //take first. currently we only store one parent.
						}
						
						Transaction parentTransaction = parent.getTransaction();
						if (!transactions.contains(parentTransaction)){ //if parent transaction not in the list of authorized transactions
							continue; //just skip that message
						}
					}
				}
				
				
				
				DocumentReferenceResponseParentDocsType docParentRef = new DocumentReferenceResponseParentDocsType();
				documentReferenceResponseParentDocs.add(docParentRef);
				
				docParentRef.setDocumentReferenceResponse(populateDocumentReferenceResponseType(msg));
				Set<Message> parent = msg.getParentMessages();
				for (Message parentMsg : parent) {
					if ( ! DOC_WRAPPER_DOC_NM.equalsIgnoreCase(parentMsg.getTransaction().getDocument().getName())){
						
						//we show the parent anyway (and we expect only one parent) 
						docParentRef.getParentDocument().add(populateDocumentReferenceResponseType(parentMsg));
					}
				}
				
				counter++;
				if (counter >= MAX_NUMBER_INBOX_LINES)
					break;
			}
			Transaction t= authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			String responseElementLocalName =factory.createInboxResponse(response).getName().getLocalPart();
			JAXBElement<InboxResponseType> responseElement = new JAXBElement<InboxResponseType>(
					new QName(t.getNamespace(),
							responseElementLocalName, "ec"), 
							InboxResponseType.class,response);
			responseMessage = new TrustExMessage<JAXBElement>(responseElement);
		//	responseMessage = new TrustExMessage<JAXBElement>(factory.createInboxResponse(response));
			responseMessage.setHeader(message.getHeader());
			return responseMessage;

		} catch (Exception e) {
			logger.error("Error Occured querying the inbox ", e);
			throw new BusinessException(e.getMessage(),e.getCause().getMessage());
		}
		
	}

}
