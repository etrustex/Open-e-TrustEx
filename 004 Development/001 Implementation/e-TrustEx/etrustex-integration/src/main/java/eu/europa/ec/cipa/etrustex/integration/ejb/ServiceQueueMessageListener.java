package eu.europa.ec.cipa.etrustex.integration.ejb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.IMessageProcessingGateway;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.transformers.TrustExMessageTransformer;
import eu.europa.ec.cipa.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.MessageHeaderType;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

/**
 * 
 * @author almahfa
 *
 *Entry point for all the services integrating through JMS
 *
 */

@Interceptors(CustomAutowiringInterceptor.class)
public class ServiceQueueMessageListener extends TrustExMessageTransformer implements MessageListener  {

	private static final Logger logger = LoggerFactory.getLogger(ServiceQueueMessageListener.class);
	
	@Autowired
	private IMessageProcessingGateway messageProcessingGateway;
	
	@Autowired
	private IAuthorisationService authorisationService;
	
	@Autowired
	private IBusinessDomainService businessDomainService;
	
	@Autowired
	private IMetadataService metadataService;
	
	@Autowired
	private ILogService logService;	
	
	@Autowired
	private LogServiceHelper logServiceHelper;

	@Override
	public void onMessage(Message message) {
		try {
			
			TrustExMessage<String> trustExMessage = null;
			String user     = null;
			String sender   = null;
			String receiver = null;
			String correlationId = null;
			
			//TODO CHeck if we need to continue supporting Customer/supplier or switch to Sender/Receiver
			if(message.getStringProperty(MessageHeaderType.MSG_SENDER_ID.getCode()) != null){
				user     = message.getStringProperty(MessageHeaderType.USER.getCode());
				sender   = message.getStringProperty(MessageHeaderType.MSG_SENDER_ID.getCode());
				receiver = message.getStringProperty(MessageHeaderType.MSG_RECEIVER_ID.getCode());
			}else if(message.getStringProperty(MessageHeaderType.MSG_CUSTOMER.getCode()) != null){
				user     = message.getStringProperty(MessageHeaderType.MSG_USER.getCode());
				sender   = message.getStringProperty(MessageHeaderType.MSG_CUSTOMER.getCode());
				receiver = message.getStringProperty(MessageHeaderType.MSG_SUPPLIER.getCode());
				correlationId = message.getStringProperty(MessageHeaderType.JMSCorrelationID.getCode());
			}

			trustExMessage = getMessage(message, user, sender, receiver, correlationId);		
			trustExMessage.getHeader().setLogCorrelationId(message.getStringProperty(MessageHeaderType.LOG_CORRELATION_ID.getCode()));
			loadMetadata(trustExMessage);	
			
			long startTime = System.currentTimeMillis();
			messageProcessingGateway.processSubmitMessage(trustExMessage);
			
			long elapsed = System.currentTimeMillis() - startTime;
			LogDTO logDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.RECEIVE_JMS_MSG, 
					"Inside ServiceQueueMessageListener", 
					this.getClass().getName()); 	
			logDTO.setValue("Duration: " + elapsed + " ms");
			logService.saveLog(logDTO);
		} 
		//TODO Check where to SEND the Reply
		catch (JMSException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Unable to read the incoming JMS message.", e);			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (MessageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private TrustExMessage<String> getMessage(Message message, String user, String sender, String receiver, String correlationId) throws JMSException{
		TrustExMessage<String> trustExMessage = new TrustExMessage<String>(null);
		TrustExMessageHeader header = new TrustExMessageHeader();
		
		header.setAuthenticatedUser(user);
		header.setSenderIdWithScheme(sender);
		header.setReceiverIdWithScheme(receiver);
		header.setCorrelationId(correlationId);
		
		trustExMessage.setHeader(header);
		trustExMessage.setPayload(((TextMessage)message).getText());
		
		return trustExMessage;
	}
	
	private void loadMetadata(TrustExMessage<String> trustExMessage) throws SAXException, IOException, ParserConfigurationException, XPathException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder;
	    
	    builder = factory.newDocumentBuilder();
		Document doc = builder.parse( new ByteArrayInputStream(trustExMessage.getPayload().getBytes()) );
		
		Element root = doc.getDocumentElement();
		String localName= root.getLocalName();
		String namespace= root.getNamespaceURI();
		Transaction t = authorisationService.getTransactionByNameSpace(namespace, localName);
		
		if (t != null){
			trustExMessage.getHeader().setTransactionNamespace(namespace);
			trustExMessage.getHeader().setTransactionRequestLocalName(localName);
			trustExMessage.getHeader().setTransactionTypeId(t.getId());
		}else{
			List<Transaction> documentTransactions = authorisationService.getTransactionsForDocument(namespace, localName);
			if(documentTransactions != null && documentTransactions.size()==1){
				t= documentTransactions.get(0);
				trustExMessage.getHeader().setTransactionNamespace(t.getNamespace());
				trustExMessage.getHeader().setTransactionRequestLocalName(t.getRequestLocalName());
				trustExMessage.getHeader().setTransactionTypeId(t.getId());
			}else{
				throw new MessageProcessingException("Undefined incoming Message", null);
			}
		}
		
		Map<MetaDataItemType, MetaDataItem> metadata=  metadataService.retrieveMetaData(null, t, t.getDocument(), t.getProfiles());
		populateCommonMessageHeader(trustExMessage, metadata);
		if (trustExMessage.getHeader().getMessageDocumentId() == null){
			throw new MessageProcessingException("Unable to extract document ID", null);
		}
	}
	
	public IMessageProcessingGateway getMessageProcessingGateway() {
		return messageProcessingGateway;
	}

	public void setMessageProcessingGateway(
			IMessageProcessingGateway messageProcessingGateway) {
		this.messageProcessingGateway = messageProcessingGateway;
	}

	public IAuthorisationService getAuthorisationService() {
		return authorisationService;
	}

	public void setAuthorisationService(IAuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}
	
}
