package eu.europa.ec.cipa.etrustex.integration.ejb.facades;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.query.QueryResult;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.IMessageProcessingGateway;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.ServiceFacadeException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.transformers.TrustExMessageTransformer;
import eu.europa.ec.cipa.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

/**
 * Session Bean implementation class IntegrationServiceFacade
 */
@Stateless(mappedName = "IntegrationServiceFacade")
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors(CustomAutowiringInterceptor.class)
public class IntegrationServiceFacade extends TrustExMessageTransformer implements IntegrationServiceFacadeRemote{

	private static final Logger logger = LoggerFactory.getLogger(IntegrationServiceFacade.class);
	
	@Autowired
	private IMessageService messageService;	
	
	@Autowired
	private IAuthorisationService authorisationService;	
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private IMessageProcessingGateway messageProcessingGateway;
	
	@Autowired
	private IBusinessDomainService businessDomainService;
	
	private static final Map<String,JAXBContext> jaxbContextMap = new HashMap<String,JAXBContext>();
	
	private static final long DG_JUSTICE_BD_ID = 3;
	
    /**
     * Default constructor. 
     */
    public IntegrationServiceFacade() {
        // TODO Auto-generated constructor stub
    }
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TrustExMessageBinary retrieveMessageBinary(String senderPartyId,
			String messageDocumentId, String binaryType) throws ServiceFacadeException {
		// TODO implement Authentication/Authorisation
		Party senderParty;
		TrustExMessageBinary bin = null;
		try {
			BusinessDomain dgJusticeBd = businessDomainService.getBusinessDomain(DG_JUSTICE_BD_ID);
			senderParty = authorisationService.getParty(senderPartyId, dgJusticeBd);
			List<Message> result = messageService.retrieveMessages(null, senderParty.getId(), null, null, null, 50, messageDocumentId, binaryType, false, false, true, true);
			if (result == null || result.size() != 1){
				throw new ServiceFacadeException("Unable to retireve Message"); 
			}
			Set<MessageBinary> binaries = result.get(0).getBinaries();
			
			for (MessageBinary messageBinary : binaries) {
				if (messageBinary.getBinaryType().equalsIgnoreCase(binaryType)){
					bin = new TrustExMessageBinary();
					bin.setFileName(messageBinary.getFileId());
//					if (messageBinary.getSize().longValue() > 5000000){
//						throw new ServiceFacadeException("Binary is too big to be retireved using this method"); 
//					}
					FileInputStream fis = new FileInputStream(messageBinary.getFileId());
					
					bin.setMimeType(messageBinary.getMimeCode());
					byte[] bytes = IOUtils.toByteArray(messageService.getDecryptedStream(fis, messageBinary.getIv()));
					bin.setBinaryContent(bytes);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceFacadeException(e); 
		} 
		return bin;
	}
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TrustExMessage<String> retrieveMessageWithoutBinaries(String user,String password, String messageDocumentId,String messageDocumentTypeCode, String senderIdWithScheme, String receiverIdWithScheme ) throws ServiceFacadeException{
		Party senderParty;
		Party receiverParty = null;
		
		Long receiverPartyId = null;
		
		TrustExMessageBinary bin = null;
		
		TrustExMessage<String> responseMessage = new TrustExMessage<String>(null);
		Party issuer;
		try {
			issuer = authorisationService.getMessageIssuer(user);
		} catch (UndefinedIdentifierException e) {
			throw new ServiceFacadeException(e.getMessage(), e);
		}
		
		
		
		if (senderIdWithScheme == null || messageDocumentId == null ||messageDocumentTypeCode== null  ){
			throw new ServiceFacadeException(" senderIdWithScheme , messageDocumentTypeCode and messageDocumentId cannot be null ");
		}
		
		
			try {
				senderParty = authorisationService.getParty(senderIdWithScheme, issuer.getBusinessDomain());
				if (receiverIdWithScheme != null){
					receiverParty = authorisationService.getParty(receiverIdWithScheme, issuer.getBusinessDomain());
					if (receiverParty != null){
						receiverPartyId= receiverParty.getId();
					}
				}
				//logger.error("Message not found searching for message with receiverParty :" + receiverPartyId+ " senderPartyID : "+ senderParty.getId()+" Message doc ID: " +messageDocumentId + " Message Document Type CD : " +messageDocumentTypeCode);
				List<Message> result = messageService.retrieveMessages(receiverPartyId, senderParty.getId(), null, null, null, 50, messageDocumentId, messageDocumentTypeCode, null, false, true, true);
				
				if (result == null || result.size() != 1){
					
					throw new ServiceFacadeException("Unable to retireve Mesage"); 
				}
				Message msg = result.get(0);
				TrustExMessageHeader header = new TrustExMessageHeader();
				header.setCorrelationId(msg.getCorrelationId());
				header.setAuthenticatedUser(msg.getAccessInfo().getCreationId());
				header.setSenderIdWithScheme(senderIdWithScheme);
				header.setMessageDocumentId(msg.getDocumentId().trim());
				header.setIssueDate(msg.getIssueDate());
				header.setReceivedDate(msg.getReceptionDate());
				responseMessage.setPayload(messageService.getMessageBinaryAsString(msg.getId(), MessageBinaryType.RAW_MESSAGE));
				responseMessage.setHeader(header);
				
				Map<String, String> additionalInfo = new HashMap<String, String>();
				Set<QueryResult> queryResults = msg.getAdditionalInfo();
				for (QueryResult queryResult : queryResults) {
					additionalInfo.put(queryResult.getKey(), queryResult.getValue());
				}
				responseMessage.setAdditionalInfo(additionalInfo);
				
			} catch (UndefinedIdentifierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ServiceFacadeException(e); 
			}
			
		
		return responseMessage;
	}
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitDocument(String user, String password, HeaderType header,
			String document) throws ServiceFacadeException {
		
		//TODO implement Authentication
		TrustExMessage<String> message = new TrustExMessage<String>(null);
		TrustExMessageHeader messageHeader = new TrustExMessageHeader();
		message.setHeader(messageHeader);
		message.setPayload(document);
		messageHeader.setAuthenticatedUser(user);
		
		List<Partner> senders = header.getBusinessHeader()
				.getSender();
		if (senders == null || senders.isEmpty() || senders.size() > 1) {
			throw new ServiceFacadeException("Unauthorized Access");
		}
		messageHeader.setSenderIdWithScheme(senders.get(0).getIdentifier().getValue());
		
		List<Partner> recivers = header.getBusinessHeader()
				.getReceiver();
		if (recivers == null || recivers.isEmpty() || recivers.size() > 1) {
			throw new ServiceFacadeException("Unauthorized Access");
		}
		messageHeader.setReceiverIdWithScheme(recivers.get(0).getIdentifier().getValue());
		
		messageHeader.setReceivedDate(Calendar.getInstance().getTime());
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse( new ByteArrayInputStream(document.getBytes()) );
			
			Element root = doc.getDocumentElement();
			String localName= root.getLocalName();
			String namespace= root.getNamespaceURI();
			Transaction t = authorisationService.getTransactionByNameSpace(namespace, localName);
			
			if (t != null){
				messageHeader.setTransactionNamespace(namespace);
				messageHeader.setTransactionRequestLocalName(localName);
				messageHeader.setTransactionTypeId(t.getId());
			}else{
				List<Transaction> documentTransactions = authorisationService.getTransactionsForDocument(namespace, localName);
				if(documentTransactions != null && documentTransactions.size()==1){
					t= documentTransactions.get(0);
					messageHeader.setTransactionNamespace(t.getNamespace());
					messageHeader.setTransactionRequestLocalName(t.getRequestLocalName());
					messageHeader.setTransactionTypeId(t.getId());
				}else{
					throw new ServiceFacadeException("Undefined incoming Message");
				}
			}
			
			Map<MetaDataItemType, MetaDataItem> metadata=  metadataService.retrieveMetaData(null, t, t.getDocument(), t.getProfiles());
			populateCommonMessageHeader(message, metadata);
			
			if (message.getHeader().getMessageDocumentId() == null){
				throw new ServiceFacadeException("Unable to extract document ID");
			}
			message.getHeader().setRawHeader(extractRawHeader(header));
			messageProcessingGateway.processSubmitMessage(message);
			
		}catch (MessageProcessingException e) {
			e.printStackTrace();
			throw new ServiceFacadeException(e.getMessage(),e);
		}  
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceFacadeException(e.getMessage(),e);
		} 
		
	}
	
	private String extractRawHeader(HeaderType header){
		String document = null;
		try {
			final StringWriter w = new StringWriter();
			Marshaller mar = getJaxBContext(HeaderType.class.getPackage().getName()).createMarshaller();
			mar.marshal(header, w);
			document = w.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return document;
	}
	
	private static JAXBContext getJaxBContext(String key) throws JAXBException{		
		if (jaxbContextMap.containsKey(key)){
			return jaxbContextMap.get(key);
		}else{
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(key);
				jaxbContextMap.put(key, jaxbContext);
				return jaxbContext;
			} catch (JAXBException e) {
				throw e;
			}
		}
	}
	
	public IAuthorisationService getAuthorisationService() {
		return authorisationService;
	}
	public void setAuthorisationService(IAuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}
	public IMessageService getMessageService() {
		return messageService;
	}
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}
}
