package eu.europa.ec.etrustex.integration.dispatcher;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.api.ITrustExJmsMessageConverter;
import eu.europa.ec.etrustex.integration.exception.MessageRoutingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.TrustExMessageConverter;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

public class JMSMessageDispatcher extends MessageDispatcher implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JMSMessageDispatcher.class);

	private static final String MSG_PARENT_ID="MSG_PARENT_ID";
	private static final String MSG_PARENT_TYPE="MSG_PARENT_TYPE";
	private static final String MSG_PARENT_STATUS="MSG_PARENT_STATUS";
	private static final String MSG_PARENT_RESPONSE_CODE="MSG_PARENT_RESPONSE_CODE";
	private static final String MSG_PARENT_SENDER_GLN="MSG_PARENT_SENDER_GLN";
	private static final String MSG_PARENT_RECEIVER_GLN="MSG_PARENT_RECEIVER_GLN";

	@Autowired
	private ApplicationContext applicationContext;

	private JMSEndpoint endpoint;

	protected JMSMessageDispatcher(Endpoint endpoint) {
		super();
		this.endpoint = (JMSEndpoint) endpoint;
	}

	private Long REPLY_TO_TIMEOUT_DEFAULT = 15000L;

	@Override
	protected void sendMessage(TrustExMessage<String> message) throws MessageRoutingException {
		ConnectionFactory cfact = null;
		Properties env = new Properties();
		LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.DISPATCHING,
				"JMS endpoint: " + endpoint.getProviderUrl() + " - JMS queue: " + endpoint.getDestinationJndiName(),
				this.getClass().getName());
		logDTO = logService.saveLog(logDTO);

		if (StringUtils.isNotEmpty(endpoint.getInitalContextFactory())) {
			env.put(Context.INITIAL_CONTEXT_FACTORY, endpoint.getInitalContextFactory());
		}
		if (StringUtils.isNotEmpty(endpoint.getProviderUrl())){
			env.put(Context.PROVIDER_URL,endpoint.getProviderUrl() );
		}
		if (endpoint.getCredentials() != null){
			env.put(Context.SECURITY_PRINCIPAL,endpoint.getCredentials().getUser());
			env.put(Context.SECURITY_CREDENTIALS,endpoint.getCredentials().getPassword());
			// required for connection with JBoss.
			//TODO : check if this doesn't create an incompatibility with Weblogic
			env.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
		}
		Context ctx = null;
		Connection conn= null;
		Session session= null;
		TemporaryQueue replyQueue = null;
		try {
			ctx = new InitialContext(env);
			cfact = (ConnectionFactory)ctx.lookup(endpoint.getConnectionFactoryJndiName());
			if (endpoint.getCredentials() != null){
				conn= cfact.createConnection(endpoint.getCredentials().getUser(),endpoint.getCredentials().getPassword());
			}else {
				conn= cfact.createConnection();
			}
			Destination destination = (Destination) ctx.lookup(endpoint.getDestinationJndiName());
			session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(destination);
			Message msg = null;
			if(endpoint.getMessageConverterClass() != null){
				Class clazz =this.getClass().getClassLoader().loadClass(endpoint.getMessageConverterClass());
				ITrustExJmsMessageConverter converter = (ITrustExJmsMessageConverter)clazz.newInstance();
				applicationContext.getAutowireCapableBeanFactory().autowireBean(converter);
				msg = converter.toMessage(message, session);
			}else {
				TrustExMessageConverter converter = new TrustExMessageConverter();
				msg =converter.toMessage(message, session);
			}

			//ode: add parent information to JMS header for supplier portal. 

			eu.europa.ec.etrustex.domain.Message parentMsg = null;
			if (message.getHeader().getParentMessageId() !=null){
				parentMsg = messageService.retrieveMessageWithInitializedProperties(message.getHeader().getParentMessageId(), false);
			}

			if (parentMsg !=null){
				//eu.europa.ec.etrustex.domain.Message parentMsg = messageService.retrieveMessage(message.getHeader().getParentMessageId() );  								
				msg.setStringProperty(MSG_PARENT_ID ,parentMsg.getDocumentId());
				logger.info(MSG_PARENT_ID + ","+parentMsg.getDocumentId());

				msg.setStringProperty(MSG_PARENT_TYPE ,parentMsg.getMessageDocumentTypeCode());
				logger.info(MSG_PARENT_TYPE + ","+parentMsg.getMessageDocumentTypeCode());

				String codeStatus = translateStatusCodeValue(message.getHeader().getParentStatusCode(),  parentMsg,null);//don't take into account the parent of the parent
				msg.setStringProperty(MSG_PARENT_STATUS ,codeStatus);
				logger.info(MSG_PARENT_STATUS + ","+codeStatus);

				msg.setStringProperty(MSG_PARENT_RESPONSE_CODE,parentMsg.getResponseCode());
				logger.info(MSG_PARENT_RESPONSE_CODE + ","+parentMsg.getResponseCode());

				String partyId = getGLN( parentMsg.getSender());
				msg.setStringProperty(MSG_PARENT_SENDER_GLN ,partyId);
				logger.info(MSG_PARENT_SENDER_GLN + ","+partyId);

				partyId = getGLN( parentMsg.getReceiver());
				msg.setStringProperty(MSG_PARENT_RECEIVER_GLN,partyId);
				logger.info(MSG_PARENT_RECEIVER_GLN + ","+partyId);
			}else{
				msg.setStringProperty(MSG_PARENT_ID ,null);
				msg.setStringProperty(MSG_PARENT_TYPE ,null);
				msg.setStringProperty(MSG_PARENT_STATUS ,null);
				msg.setStringProperty(MSG_PARENT_RESPONSE_CODE,null);
				msg.setStringProperty(MSG_PARENT_SENDER_GLN ,null);
				msg.setStringProperty(MSG_PARENT_RECEIVER_GLN,null);
			}


			if(!endpoint.getIsSupportingReplyTo()){
				producer.send(msg);
				session.commit();
			}else{
				//TODO Add Authentication Parameters

				//Get the timeout value
				Long timeOut = REPLY_TO_TIMEOUT_DEFAULT;

				Map<MetaDataItemType, MetaDataItem> metadata=  metadataService.retrieveMetaData(null, null, null, (Long)null, null);
				MetaDataItem item = metadata.get(MetaDataItemType.JMS_REPLY_TO_TIMEOUT);
				if(item.getValue() != null){
					try{
						timeOut = Long.parseLong(item.getValue());
					}catch(NumberFormatException e){
					}
				}
				//Generate UUID
				String uuid = UUID.randomUUID().toString();

				//Creating the temporary queue
				replyQueue = session.createTemporaryQueue();
				MessageConsumer receiver = session.createConsumer(replyQueue);

				//Initiating the message fields
				msg.setJMSMessageID(uuid);
				msg.setJMSReplyTo(replyQueue);

				//Sending the Message
				producer.send(msg);
				session.commit();

				//Receiving the reply
				Message reply = receiver.receive(timeOut);

				//TODO Decide whether to Support correlation ID or not:							
				if(reply == null){
					// If nothing has been received
					logger.error("No reply received after "+timeOut);
					throw new MessageRoutingException("soapenv:Server",
							"No reply received after "+timeOut, null,
							ErrorResponseCode.TECHNICAL_ERROR,null,
							"No reply received after "+timeOut);

				}

				if((reply.getJMSCorrelationID() != null) && uuid.equals(reply.getJMSCorrelationID())){
					// Now we need to extract the Payload

					//String replyMessage = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header xmlns:ec=\"ec:services:wsdl:DocumentBundle-2\" xmlns:ec1=\"ec:schema:xsd:CommonBasicComponents-0.1\" xmlns:urn=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" xmlns:urn1=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\"><ec:Header><ec1:BusinessHeader xmlns:ec1=\"ec:schema:xsd:CommonAggregateComponents-2\"></ec1:BusinessHeader></ec:Header></SOAP-ENV:Header><SOAP-ENV:Body><ec:SubmitDocumentBundleResponse xmlns:ec=\"ec:services:wsdl:DocumentBundle-2\" xmlns:ec1=\"ec:schema:xsd:CommonBasicComponents-0.1\"><ns5:Ack xmlns:ns10=\"ec:schema:xsd:CommonBasicComponents-1\" xmlns:ns3=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" xmlns:ns4=\"ec:schema:xsd:CommonAggregateComponents-2\" xmlns:ns5=\"ec:services:wsdl:DocumentBundle-2\" xmlns:ns6=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\"><ns6:IssueDate>2015-01-23</ns6:IssueDate><ns10:AckIndicator>true</ns10:AckIndicator><ns4:AcknowledgedDocumentReference><ns3:DocumentReference><ns6:ID>BDL_Dispatch_16</ns6:ID><ns6:DocumentTypeCode>BDL</ns6:DocumentTypeCode></ns3:DocumentReference></ns4:AcknowledgedDocumentReference></ns5:Ack></ec:SubmitDocumentBundleResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>";
					String replyMessage = null;
					if (reply instanceof TextMessage){
						replyMessage = ((TextMessage)reply).getText();
					}else if(reply instanceof BytesMessage){
						replyMessage = ((BytesMessage)reply).readUTF();
					}

					final Configuration config = new Configuration();
					try {
						DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(replyMessage)));
						//*:Envelope/*:Body/*:*[1]
						List<String> queryResult = queryFor("//*:Ack/*:AcknowledgedDocumentReference", docInfo, config);
						if(queryResult.size() != 1){
							throw new MessageRoutingException("soapenv:Server",
									"Technical Error: Ack.AcknowledgedDocumentReference not found", null,
									ErrorResponseCode.CONFIGURATION_ERROR,null,
									"Invalid response message");
						}
					} catch (XPathException e) {
						logger.error("Invalid Ack Message",  e);
						throw new MessageRoutingException("soapenv:Server",
								"Technical Error: " + e.getMessage(), null,
								ErrorResponseCode.CONFIGURATION_ERROR,null,
								"Invalid response message");
					}
				}else{
					// If the correlation ID is not equal to the Message ID
					logger.error("Wrong or empty Correlation ID ");
					throw new MessageRoutingException("soapenv:Server",
							"Wrong or empty Correlation ID ", null,
							ErrorResponseCode.TECHNICAL_ERROR,null,
							"Wrong or empty Correlation ID ");
				}
			}

		} catch (NamingException e) {
			logger.error("Unable to initialize JNDI context",  e);
			throw new MessageRoutingException("soapenv:Server",
					"Technical Error: " + e.getMessage(), null,
					ErrorResponseCode.CONFIGURATION_ERROR,null,
					"Unable to initialize JNDI context");
		} catch (JMSException e) {
			logger.error("Unable to establish connection to the jms destination ",  e);
			throw new MessageRoutingException("soapenv:Server",
					"Technical Error: " + e.getMessage(), null,
					ErrorResponseCode.TECHNICAL_ERROR,null,
					"Unable to establish connection to the jms destination " + env.toString());
		} catch (ClassNotFoundException e) {
			logger.error("Config error specified Message Converter not found",  e);
			throw new MessageRoutingException("soapenv:Server",
					"Technical Error: " + e.getMessage(), null,
					ErrorResponseCode.CONFIGURATION_ERROR,null,
					"Config error specified Message Converter not found");
		} catch (InstantiationException e) {
			logger.error("Config error specified Message Converter not found",  e);
			throw new MessageRoutingException("soapenv:Server",
					"Technical Error: " + e.getMessage(), null,
					ErrorResponseCode.CONFIGURATION_ERROR,null,
					"Config error specified Message Converter not found");
		} catch (IllegalAccessException e) {
			logger.error("Config error specified Message Converter not found",  e);
			throw new MessageRoutingException("soapenv:Server",
					"Technical Error: " + e.getMessage(), null,
					ErrorResponseCode.CONFIGURATION_ERROR,null,
					"Config error specified Message Converter not found");
		}finally{
			if(replyQueue != null){
				try {
					replyQueue.delete();
				} catch (JMSException e) {
					logger.error("Deleting reply queue",e);
				}
			}
			if (conn !=null){
				try {
					conn.close();
				} catch (JMSException e) {
					logger.error("Closing connection",e);
				}
			}
		}

	}

	private List<String> queryFor(final String query, DocumentInfo docInfo, final Configuration config)
			throws XPathException {
		final DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
		final StaticQueryContext sqc = config.newStaticQueryContext();
		final XQueryExpression exp = sqc.compileQuery(query);

		logger.debug("queryFor [{}]", query);
		dynamicContext.setContextItem(docInfo);
		final SequenceIterator<?> iter = exp.iterator(dynamicContext);

		if (iter == null) {
			logger.debug("query got null iterator, returning null");
			return null;
		}

		List<String> result = new ArrayList<String>();
		Item item = iter.next();
		while (item != null) {
			result.add(item.getStringValue());
			item = iter.next();
		}

		logger.debug("query result has [{}] element(s)", result.size());
		return result;
	}

	@Override
	protected TrustExMessage<String> transformMessage(
			TrustExMessage<String> message, Transaction transaction) {
		return message;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	private String translateStatusCodeValue(String statusCode, eu.europa.ec.etrustex.domain.Message message, eu.europa.ec.etrustex.domain.Message parentMessage) {
		Map<String, MessageResponseCode> respcodes = metadataService.retrieveMessageResponseCodes(null, null, message.getTransaction().getDocument().getId(), null);
		logger.debug("Response count:" + respcodes.size());
		MessageResponseCode code = respcodes.get(statusCode);
		logger.debug("Code to translate:" + statusCode);
		String translatedCode = code == null ? "" : code.getStatusCode();
		logger.debug("Translated code:" + translatedCode);
		return translatedCode;
	}

	private String getGLN(Party party) {
		String partyGLN=null;
		if (party==null){
			return null;
		}
		for (PartyIdentifier id: party.getIdentifiers()){
			if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(id.getSchemeId().getISO6523Code())){
				partyGLN = id.getValue();
				break;
			}
		}
		return partyGLN;
	}

}
