/* 
 * Copyright 2010  EUROPEAN COMMISSION
 * 
 * Licensed under the EUPL, Version 1.1 only (the "License");	
 * 
 * You may not use this work except in compliance with the
 * License.
 * You may obtain a copy of the License at:
 * 
 * http://ec.europa.eu/idabc/en/document/7774
 * 
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
*/
package eu.europa.ec.cipa.etrustex.integration.transformers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.tiny.TinyElementImpl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.XmlException;
import org.w3c.dom.Document;

import eu.europa.ec.cipa.etrustex.domain.MessagePriority;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.TrustExIntegrationSupport;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.util.JmsHeaders;
import eu.europa.ec.cipa.etrustex.integration.util.SOAPService;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessagePriorityService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

/**
 * This class transforms the incoming {@link SaajSoapMessage} payload to  an {@link OpenETrustExIncomingMessage} 
 * extracting all the necessary informations and storing them into the appropriate objects.
 * It also extracts the xml payload of the incoming {@link SaajSoapMessage} and transforms it to an xmlobject
 * that is used by the other elements in the message handling channel.
 * 
 * This class also manage the namspaces. The incoming messages are qualified according to the wsld
 * namespaces. To process the messages we use xml bean representation of UBL schemas . Thus when a message
 * comes in we substitute wsdl namespaces by UBL namespaces  before parsing the xml bean. 
 * 
 * The webservice response body is build using a template mechanism where we replace a given token
 * by the Xml bean response object serialized to xml .  
 * 
 * 
 * @author orazisa
 *
 */
public class SAAJSOAPToTrustExMessageTransformer extends TrustExIntegrationSupport {
	
	@Autowired
	private IAuthorisationService authorisationService;
	@Autowired
	private IMetadataService metadataService;
	@Autowired
	private ILogService logService;
	@Autowired
	private IBusinessDomainService businessDomainService;
	@Autowired
	private SOAPService soapService;
	@Autowired
	private IMessagePriorityService messagePriorityService;
	
	private String senderType = null;
	private String endpointName = null;

	private static String SENDER_TYPE_CUSTOMER = "CUSTOMER";
	private static String SENDER_TYPE_SUPPLIER = "SUPPLIER";
	private static String CUSTOMER_XPATH = "//*:AuthorisationHeader/*:CustomerID";
	private static String SUPPLIER_XPATH = "//*:AuthorisationHeader/*:SupplierID";
	private static final String ID_SEPARATOR = "::";
	
	private static final Map<Class<?>,JAXBContext> jaxbContextMap = new HashMap<>(); 
		
	private static final Logger logger = LoggerFactory.getLogger(SAAJSOAPToTrustExMessageTransformer.class);

	
	private void processPayload(TrustExMessage<String> trustExMessage, String messageString ,Transaction t,Map<MetaDataItemType, MetaDataItem> metadata,Configuration config, DynamicQueryContext dynamicContext){
		final StaticQueryContext sqc = config.newStaticQueryContext();
		try {
			
			final XQueryExpression documentQuery = sqc.compileQuery("//*:Body/*/*:"+t.getDocument().getLocalName());
			
			// integrity check
			List<Object> result = documentQuery.evaluate(dynamicContext);
			if (result == null || result.size() !=1){
				throw new MessageProcessingException("soapenv:Client",
						ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE,null, "");
			}
			final Properties props = new Properties();
			props.setProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
			props.setProperty(OutputKeys.METHOD, "xml");
			props.setProperty(OutputKeys.INDENT, "yes");
			StringWriter writer = new StringWriter();
			documentQuery.run(dynamicContext, new StreamResult(writer), props);
			String document = writer.toString();
			trustExMessage.setPayload(document.replaceAll(t.getNamespace(), t.getDocument().getNamespace()));
			MetaDataItem documentIdQueryItem = metadata.get(MetaDataItemType.DOCUMENT_ID_XQUERY);
			if (documentIdQueryItem != null){
							
				final XQueryExpression documentIdQuery = sqc.compileQuery(metadata.get(MetaDataItemType.DOCUMENT_ID_XQUERY).getValue());
				Object idObject = documentIdQuery.evaluateSingle(dynamicContext);
				if (idObject == null) {
					throw new MessageProcessingException("soapenv:Client",
							ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null, 
							ErrorResponseCode.INVALID_MESSAGE_ID,null, null);
				}
				String idString=null;
				if (idObject instanceof String) {
					idString = (String) idObject;
				} else {
					idString = ((Item)idObject).getStringValue();
				}
				//trim ID
				if (idString!=null){
					int sepIndex = idString.indexOf(ID_SEPARATOR);
					if(sepIndex != -1){
						idString = idString
								.substring(0, sepIndex)
								.trim()
								.concat(ID_SEPARATOR)
								.concat(idString.substring(sepIndex + ID_SEPARATOR.length(), idString.length())
								.trim());
					}else{
						idString = idString.trim();
					}
				}
				trustExMessage.getHeader().setMessageDocumentId(idString);
			}

			MetaDataItem parentQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_ID_XQUERY);
			Item it ;
			if (parentQueryItem != null){
				final XQueryExpression parentIdQuery = sqc.compileQuery(parentQueryItem.getValue());
				it = (Item)parentIdQuery.evaluateSingle(dynamicContext);
				if (it != null){
					//trim ID
					String parentId= StringUtils.trim(it.getStringValue());
					trustExMessage.getHeader().setMessageParentDocumentId(parentId);
				}				
			}
			MetaDataItem parentTCQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_TYPECODE_XQUERY);
			if (parentTCQueryItem != null){
				final XQueryExpression parentTCQuery = sqc.compileQuery(parentTCQueryItem.getValue());
				it = (Item)parentTCQuery.evaluateSingle(dynamicContext);
				if (it != null){
					trustExMessage.getHeader().setMessageParentDocumentTypeCode(it.getStringValue());
				}				
			}
			
			MetaDataItem versionQueryItem = metadata.get(MetaDataItemType.DOCUMENT_VERSION_XQUERY);
			if (versionQueryItem != null){
				final XQueryExpression versionQuery = sqc.compileQuery(versionQueryItem.getValue());
				it = (Item)versionQuery.evaluateSingle(dynamicContext);
				trustExMessage.getHeader().setMessageDocumentVersion(it.getStringValue());
			}
			
			MetaDataItem issueDateQueryItem = metadata.get(MetaDataItemType.DOCUMENT_ISSUEDATE_XQUERY);
			//Time is ignored if Date is not present
			if (issueDateQueryItem != null){
				
				final XQueryExpression issueDateQuery = sqc.compileQuery(issueDateQueryItem.getValue());
				it = (Item)issueDateQuery.evaluateSingle(dynamicContext);								
				String date = null;					
				if(it!= null && it.getStringValue() != null){
					date = it.getStringValue();
				}
				
				String time = null;
				MetaDataItem issueTimeQueryItem = metadata.get(MetaDataItemType.DOCUMENT_ISSUETIME_XQUERY);
				if(issueTimeQueryItem != null){
					final XQueryExpression issueTimeQuery = sqc.compileQuery(issueTimeQueryItem.getValue());
					it = (Item)issueTimeQuery.evaluateSingle(dynamicContext);										
					if(it!= null && it.getStringValue() != null){
						time = it.getStringValue();
					}
				}
				
				Date NOW = Calendar.getInstance().getTime();
				
				if (date != null && time != null){				
					try {
						Calendar dateCal = javax.xml.bind.DatatypeConverter.parseDate(date);
						Calendar timeCal = javax.xml.bind.DatatypeConverter.parseTime(time);

					    dateCal.set(Calendar.HOUR       , timeCal.get(Calendar.HOUR));
					    dateCal.set(Calendar.MINUTE     , timeCal.get(Calendar.MINUTE));
					    dateCal.set(Calendar.SECOND     , timeCal.get(Calendar.SECOND));
					    dateCal.set(Calendar.MILLISECOND, timeCal.get(Calendar.MILLISECOND));
					    dateCal.set(Calendar.AM_PM, timeCal.get(Calendar.AM_PM));
					    //dateCal.setTimeZone(timeCal.getTimeZone());

					    //ETRUSTEX-859 issue date should be in the range 1900-2100
					    if(dateCal.get(Calendar.YEAR) < 1900 || dateCal.get(Calendar.YEAR) > 2100) {
					    	throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), 
					    			null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION,null, "");
					    }
					    
					    if(dateCal.getTime().after(NOW)){
					    	throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), 
					    			null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION,null, "");
					    }
					    
					    
						trustExMessage.getHeader().setIssueDate(dateCal.getTime());
					} catch (IllegalArgumentException iae) { // Error parsing date or time
						throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),
								null, ErrorResponseCode.DOCUMENT_XSD_INVALID ,null, "");
					} catch (Exception e){
						//if(e instanceof MessageProcessingException) 
						trustExMessage.getHeader().setIssueDate(null);
						throw e;
					}
				}else if (date != null && time == null){
					try {
					    Calendar dateCal = javax.xml.bind.DatatypeConverter.parseDate(date);	
					    
					    //ETRUSTEX-859 issue date should be in the range 1900-2100
					    if(dateCal.get(Calendar.YEAR) < 1900 || dateCal.get(Calendar.YEAR) > 2100) {
					    	throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),
									null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION,null, "");
					    }
					    
					    if(dateCal.getTime().after(NOW)){
					    	throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),
								null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION,null, "");
					    }					    
					    trustExMessage.getHeader().setIssueDate(dateCal.getTime());
						
					}catch (Exception e){
						if(e instanceof MessageProcessingException) throw e;
						trustExMessage.getHeader().setIssueDate(null);
					}
				}	
			}			
		} catch (XPathException e) {
			logger.error(e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}

	private void processHeaders(TrustExMessage<String> trustExMessage, MessageHeaders headers, String messageString ,Transaction t,Map<MetaDataItemType, MetaDataItem> metadata, Configuration config, DynamicQueryContext dynamicContext) throws MessageProcessingException{
		TrustExMessageHeader header = new TrustExMessageHeader();
		header.setReceivedDate((Date)headers.get("receivedDate"));
		header.setDocumentTypeId(t.getDocument().getId());
		header.setTransactionTypeId(t.getId());
		header.setMessageHeaders((eu.europa.ec.cipa.etrustex.integration.util.MessageHeaders)headers.get("messageHeaders"));
		try {
			String username = soapService.getUserPrincipal();
			Party issuerParty = authorisationService.getMessageIssuer(username);
			header.setIssuerPartyId(issuerParty.getId());
			header.setAuthenticatedUser(username);
			header.getMessageHeaders().setPriority(getMessagePriority(t, username));
		} catch (UndefinedIdentifierException e1) {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(),
					null, ErrorResponseCode.AUTHENTICATION_FAILED,null, "The authentication Data is incorrect");
		}
		try {			
			String senderXquery = null;
			if(senderType == null){
				MetaDataItem senderXqueryItem =  metadata.get(MetaDataItemType.SENDER_ID_XQUERY);
				if (senderXqueryItem == null || senderXqueryItem.getValue()== null){
					throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
							ErrorResponseCode.TECHNICAL_ERROR, null, null);
					
				}
				senderXquery = senderXqueryItem.getValue();
			}else if (SENDER_TYPE_SUPPLIER.equals(senderType)){
				senderXquery = SUPPLIER_XPATH;
			}else if (SENDER_TYPE_CUSTOMER.equals(senderType)){
				senderXquery = CUSTOMER_XPATH;
			}else{
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
						
			final StaticQueryContext sqc = config.newStaticQueryContext();
			XQueryExpression exp = sqc.compileQuery(senderXquery);
			List<Object> result = exp.evaluate(dynamicContext);
			if (result == null || result.size() !=1){
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(),
						null, ErrorResponseCode.AUTHENTICATION_FAILED,null, "The authentication Data is incorrect");
			}
			//Item it = (Item)exp.evaluateSingle(dynamicContext);
			Item it = (Item)result.get(0);
			String schemeID = ((TinyElementImpl)it).getAttributeValue("", "schemeID");
			MetaDataItem schemeIdSeparatorItem = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR);
			header.setSenderIdWithScheme(StringUtils.isNotBlank(schemeID) ? schemeID + schemeIdSeparatorItem.getValue() + it.getStringValue() : it.getStringValue());
			
			if(senderType == null){
				MetaDataItem receiverXqueryItem = metadata.get(MetaDataItemType.RECEIVER_ID_XQUERY);
				MetaDataItem supportMultiCast = metadata.get(MetaDataItemType.SUPPORT_MULTICAST);
				
				if (receiverXqueryItem != null){
					exp =sqc.compileQuery(receiverXqueryItem.getValue());
					//ETRUSTEX-597 if multiple receivers are found, send Unauthorized Access fault
					//ETRUSTEX-1264: excepted if the transaction has a metadata indicating that it supports multicasting.
					List<Object> receiverList = exp.evaluate(dynamicContext);
					
					if (supportMultiCast != null && supportMultiCast.getValue() != null && supportMultiCast.getValue().equals("true")){
						//if we support multicast
						header.setMulticastSupported(true);
						if (receiverList !=null){
							for(Object obj : receiverList){
								Item item = (Item)obj;
								if (item != null && item.getStringValue() != null){
									schemeID = ((TinyElementImpl)item).getAttributeValue("", "schemeID");
									String idWithScheme = StringUtils.isNotBlank(schemeID) ? schemeID + schemeIdSeparatorItem.getValue() + item.getStringValue() : item.getStringValue();
									header.getReceiverIdWithSchemeList().add(idWithScheme);									
								}								
							}
						}					
					}else{
						//if we don't support multicast
						header.setMulticastSupported(false);						
						if (receiverList.size() > 1) {
							throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(),
									null, ErrorResponseCode.AUTHENTICATION_FAILED,null, "Multiple receivers found");
						}
						it = (Item)exp.evaluateSingle(dynamicContext);					
						if (it != null && it.getStringValue() != null){
							schemeID = ((TinyElementImpl)it).getAttributeValue("", "schemeID");
							header.setReceiverIdWithScheme(StringUtils.isNotBlank(schemeID) ? schemeID + schemeIdSeparatorItem.getValue() + it.getStringValue() : it.getStringValue());
						}		
					}
				}
			}else if (SENDER_TYPE_SUPPLIER.equals(senderType)){
				exp =sqc.compileQuery(CUSTOMER_XPATH);				
				it = (Item)exp.evaluateSingle(dynamicContext);
				if (it != null && it.getStringValue() != null){	
					header.setReceiverIdWithScheme(it.getStringValue());
				}	
			}else if (SENDER_TYPE_CUSTOMER.equals(senderType)){
				exp =sqc.compileQuery(SUPPLIER_XPATH);				
				it = (Item)exp.evaluateSingle(dynamicContext);
				if (it != null && it.getStringValue() != null){	
					header.setReceiverIdWithScheme(it.getStringValue());
				}
			}
			
			MetaDataItem correlationXqueryItem = metadata.get(MetaDataItemType.CORRELATION_ID_XQUERY);
			if (correlationXqueryItem != null){
				exp =sqc.compileQuery(correlationXqueryItem.getValue());
				it = (Item)exp.evaluateSingle(dynamicContext);
				if (it != null && it.getStringValue() != null){				
					header.setCorrelationId(it.getStringValue());
				}
			}
			
			MetaDataItem InstanceIItem = metadata.get(MetaDataItemType.INSTANCE_IDENTIFIER_XQUERY);
			if (InstanceIItem != null){
				exp =sqc.compileQuery(InstanceIItem.getValue());
				it = (Item)exp.evaluateSingle(dynamicContext);
				if (it != null && it.getStringValue() != null){				
					header.setInstanceIdentifier(it.getStringValue());
				}
			}
			
			MetaDataItem replyTo = metadata.get(MetaDataItemType.REPLYTO_XQUERY);
			if (replyTo != null){
				exp =sqc.compileQuery(replyTo.getValue());
				it = (Item)exp.evaluateSingle(dynamicContext);
				if (it != null && it.getStringValue() != null){	
					header.setReplyTo(it.getStringValue());
				}
			}
			
			MetaDataItem available = metadata.get(MetaDataItemType.AVAILABLE_NOTIFICATION_XPATH);
			if (available != null){
				exp =sqc.compileQuery(available.getValue());
				it = (Item)exp.evaluateSingle(dynamicContext);
				if (it != null && it.getStringValue() != null){				
					header.setAvailableNotification(it.getStringValue());
				}
			}
			
			//Set the Raw Header
			final XQueryExpression documentQuery = sqc.compileQuery("//*:Envelope/*:Header");
			final Properties props = new Properties();
			props.setProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
			props.setProperty(OutputKeys.METHOD, "xml");
			props.setProperty(OutputKeys.INDENT, "yes");
			StringWriter writer = new StringWriter();
			documentQuery.run(dynamicContext, new StreamResult(writer), props);
			String document = writer.toString();
			//exp = sqc.compileQuery("//*:Envelope/*:Header");
			//it = (Item)exp.evaluateSingle(dynamicContext);
			header.setRawHeader(document);
			
			trustExMessage.setHeader(header);
		} catch (XPathException e) {
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(),
					null, ErrorResponseCode.TECHNICAL_ERROR,null, e.getMessage());
		} 
	}

	private void processAttachments(TrustExMessage<String> trustExMessage, SaajSoapMessage message) throws SOAPException,XmlException{
		
		Set<TrustExMessageBinary> binaries = new HashSet<TrustExMessageBinary>();
		
		Iterator<Attachment> it = message.getAttachments();
		if(it.hasNext()){
			Attachment att = it.next();
		
			TrustExMessageBinary bin = new TrustExMessageBinary();
			//byte[] content = new byte[(int)att.getSize()];
			bin.setMimeType(att.getContentType());
			bin.setFileName(att.getContentId());
			try {
				bin.setBinaryContentIS(att.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			binaries.add(bin);
		}
		
		trustExMessage.setBinaries(binaries);
		
	}
	
	/**
	 * This method extracts information from the incoming {@link SaajSoapMessage} and parse
	 * the soab body content to an xml bean (with namspace substitution)
	 * @param incoming SaajSoapMessage
	 * @return an {@link OpenETrustExIncomingMessage}
	 */
	public Message<TrustExMessage<String>> transformIncomingMessage(Message<?> incoming){
				
		TrustExMessage<String> trustExMessage = new TrustExMessage<String>(null);
		
		SaajSoapMessage message = (SaajSoapMessage) incoming.getPayload();
		
		String webserviceRootQName = "";
		
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.TRANSFORM_MSG, this.getClass().getName())
			.description("Inside SAAJSOAPToTrustExMessageTransformer")			
			.build();
		
		try {			
			logDTO.setBusinessDomain(soapService.retrieveBusinessDomain(message.getSaajMessage().getSOAPHeader()));
			webserviceRootQName = incoming.getHeaders().get("webserviceRootQName",String.class);
			String transactionLocalName = webserviceRootQName.substring(webserviceRootQName.indexOf('}')+1);
			String transactionNamespace = webserviceRootQName.substring(webserviceRootQName.indexOf('{')+1, webserviceRootQName.indexOf('}'));
			Transaction t = authorisationService.getTransactionByNameSpace(transactionNamespace, transactionLocalName);
			logDTO.setTransaction(t);
			if (t == null){
				throw new MessageProcessingException("soapenv:Client", 
						ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null,ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE,null,null);
			}
			if(!authorisationService.isTransactionAllowed(endpointName, t)){
				throw new MessageProcessingException("soapenv:Client", 
						ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
			}
			
			Map<MetaDataItemType, MetaDataItem> metadata=  metadataService.retrieveMetaData(null, t, t.getDocument(), t.getProfiles());
			processAttachments(trustExMessage,message);
			// attachments are processed so we can remove theme from the soap message
			message.getSaajMessage().removeAllAttachments();
			ByteArrayOutputStream messageBytes=new ByteArrayOutputStream();
			((WebServiceMessage)incoming.getPayload()).writeTo(messageBytes);
			String messageString =  messageBytes.toString("UTF-8");
									
									
			Configuration config = new Configuration();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(messageString)));
			DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
			dynamicContext.setContextItem(docInfo);
			processHeaders(trustExMessage,incoming.getHeaders(),messageString, t, metadata, config, dynamicContext);
			trustExMessage.getHeader().setMetadata(metadata);
			trustExMessage.getHeader().setSoapMessage(message.getSaajMessage());
			processPayload(trustExMessage, messageString, t, metadata, config, dynamicContext);
			MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(trustExMessage).copyHeaders(incoming.getHeaders());
			return builder.build();
		} catch (MessageProcessingException mpe){
			logDTO.setDescription(logDTO.getDescription() + " " + mpe.getMessage() + " " + mpe.getDescription());
			logService.saveLog(logDTO);
			throw mpe;
		} catch (NoResultException mpe){
			mpe.printStackTrace();
			logDTO.setDescription(logDTO.getDescription() + " " + "Unsupported transaction :" + webserviceRootQName);
			logService.saveLog(logDTO);
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null,
					ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
		} catch (Exception e) {	
			e.printStackTrace();
			logDTO.setDescription(logDTO.getDescription() + " " + "Technical Error :" + e.getMessage());
			logService.saveLog(logDTO);			
			throw new MessageProcessingException("soapenv:Server",
					"Server Error", null, ErrorResponseCode.TECHNICAL_ERROR,null, e.getMessage());
		}  
		
	}

	/**
	 * @param transaction
	 * @param siMessage
	 */
	private Integer getMessagePriority(Transaction transaction, String username) {
		MessagePriority messagePriority = messagePriorityService.findByUserAndTransaction(username, transaction.getId());			
		return messagePriority != null ? messagePriority.getPriority() : 0;
	}
	
	public Message<TrustExMessage<Document>> transformOutgoingMessage(Message<TrustExMessage<?>> outgoing){
		
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.TRANSFORM_MSG, this.getClass().getName())
			.description("Inside SAAJSOAPToTrustExMessageTransformer")			
			.businessDomain(retrieveBusinessDomain(outgoing.getPayload()))
			.build();
		
		try {
			Transaction t = authorisationService.getTransactionById(outgoing.getPayload().getHeader().getTransactionTypeId());
			logDTO.setTransaction(t);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			
			String wsResponse;
			

			
			Document doc = null;
			if (outgoing.getPayload().getPayload() instanceof String ){
				wsResponse = createWebserviceRootMessage(t.getNamespace(),t.getResponseLocalName(),(String)outgoing.getPayload().getPayload(),t.getDocument().getNamespace());
				doc = factory
						.newDocumentBuilder()
						.parse(new ByteArrayInputStream(wsResponse.getBytes("UTF-8")));
			} else if( outgoing.getPayload().getPayload() instanceof JAXBElement){
				JAXBElement<?> elem = (JAXBElement<?>)outgoing.getPayload().getPayload();
				StringWriter writer = new StringWriter();
				Marshaller  marshaller = getJaxBContext(elem.getDeclaredType()).createMarshaller();
		        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); 
		        marshaller.marshal(elem, writer);
				//System.out.println("The xml Object is : "+ writer.toString());
				wsResponse = createWebserviceRootMessage(t.getNamespace(),t.getResponseLocalName(),writer.toString(),t.getDocument().getNamespace());
				//System.out.println("the ws response is :" +wsResponse );				
				
				doc =  factory
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(wsResponse.getBytes("UTF-8")));;

			}
			//webServiceResponseRootDoc.appendChild(doc);
			TrustExMessage<Document> responseMessage = new TrustExMessage<Document>(doc);
			responseMessage.setBinaries(outgoing.getPayload().getBinaries());
			responseMessage.setHeader(outgoing.getPayload().getHeader());
			
			MessageBuilder<TrustExMessage<Document>> builder = MessageBuilder.withPayload(responseMessage).copyHeaders(outgoing.getHeaders());
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			logDTO.setDescription(logDTO.getDescription() + " " + "Technical Error :" + e.getMessage());
			logService.saveLog(logDTO);					
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		} 
	}
		
	private BusinessDomain retrieveBusinessDomain(TrustExMessage<?> trustexMessage) {
		if (trustexMessage.getHeader().getIssuer() != null) {
			return trustexMessage.getHeader().getIssuer().getBusinessDomain();
		} else {
			return businessDomainService.retrieveBusinessDomain(trustexMessage.getHeader().getAuthenticatedUser());
		}
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
	
	private static JAXBContext getJaxBContext(Class<?> c) throws Exception{
		if (jaxbContextMap.containsKey(c)){
			return jaxbContextMap.get(c);
		}else{
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(c);
				jaxbContextMap.put(c, jaxbContext);
				return jaxbContext;
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public String getSenderType() {
		return senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}
	
	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

}
