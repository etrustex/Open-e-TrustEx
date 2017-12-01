package eu.europa.ec.cipa.etrustex.integration.gateway;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MessageEndpoint;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.support.MarshallingUtils;
import org.springframework.xml.XmlException;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import ec.schema.xsd.commonaggregatecomponents_2.ObjectFactory;
import ec.schema.xsd.documentwrapper_1.DocumentWrapperType;
import ec.schema.xsd.documentwrapperrequest_1.DocumentWrapperRequestType;
import ec.services.wsdl.documentwrapper_2.DeleteDocumentWrapperRequestRequest;
import ec.services.wsdl.documentwrapper_2.DeleteDocumentWrapperRequestResponse;
import ec.services.wsdl.documentwrapper_2.RetrieveDocumentWrapperRequestRequest;
import ec.services.wsdl.documentwrapper_2.RetrieveDocumentWrapperRequestResponse;
import ec.services.wsdl.documentwrapper_2.StoreDocumentWrapperRequest;
import ec.services.wsdl.documentwrapper_2.StoreDocumentWrapperResponse;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.IMessageBinaryGateway;
import eu.europa.ec.cipa.etrustex.integration.api.IMessageProcessingGateway;
import eu.europa.ec.cipa.etrustex.integration.exception.BadRequestException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.MissingAttachmentException;
import eu.europa.ec.cipa.etrustex.integration.exception.RequestEntityTooLargeException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.util.EtrustexAttachmentUnmarshaller;
import eu.europa.ec.cipa.etrustex.integration.util.ResourceToDatabaseResolver;
import eu.europa.ec.cipa.etrustex.integration.util.SOAPService;
import eu.europa.ec.cipa.etrustex.integration.util.XMLUtil;
import eu.europa.ec.cipa.etrustex.integration.util.XsdValidationErrorHandler;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.IXmlService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import net.sf.saxon.trans.XPathException;

public class EtrustEXWrapperEndpoint implements MessageEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(EtrustEXWrapperEndpoint.class);
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private SOAPService soapService;
	
	private static final String WRAPPER_NS = "ec:services:wsdl:DocumentWrapper-2";
	
	private static final String DOCUMENT_WRAPPER_XSD_NS = "ec:schema:xsd:DocumentWrapper-1";
	private static final String DOCUMENT_WRAPPER_REQUEST_XSD_NS = "ec:schema:xsd:DocumentWrapperRequest-1";
	
	private static final String STORE_WRP_TX_LN    = "StoreDocumentWrapperRequest";
	private static final String RETRIEVE_WRP_TX_LN = "RetrieveDocumentWrapperRequestRequest";
	private static final String DELETE_WRP_TX_LN   = "DeleteDocumentWrapperRequestRequest";
	
	
	private IMessageProcessingGateway messageProcessingGateway;
	private IMessageBinaryGateway messageBinaryGateway;
	private IAuthorisationService authorisationService;
	private IMetadataService metadataService;
	private IXmlService xmlService;
	
	private final TransformerSupportDelegate transformerSupportDelegate = new TransformerSupportDelegate();
	
	private static final Map<String,JAXBContext> jaxbContextMap = new HashMap<String,JAXBContext>(); 
	private static final HashMap<String, Schema> schemasMap = new HashMap<String, Schema>();
	
	@SuppressWarnings({ "rawtypes" })
	public void invoke(MessageContext messageContext) {
		SaajSoapMessage request = null;
		LogDTO logDTO = null;
		try {
			request = (SaajSoapMessage)messageContext.getRequest();
			
			QName wsRootQname = PayloadRootUtils.getPayloadRootQName(
					request.getPayloadSource(),
					TransformerFactory.newInstance());
			
			//validate SOAP envelope
			soapService.validateSOAPMessage(request, false, wsRootQname.toString());
						
			logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.PROCESS_MSG, this.getClass().getName())
				.description("Inside EtrustEXWrapperEndpoint")
				.urlContext(request.getSaajMessage().getSOAPHeader().getNamespaceURI())
				.build();
			
			logDTO.setBusinessDomain(soapService.retrieveBusinessDomain(request.getSaajMessage().getSOAPHeader()));
			
			Node headerNode = extractHeaderNode(messageContext, request);
			if (headerNode == null) {
				setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED),
						messageContext);
				return;
			}
			Node headerNodeClone = headerNode.cloneNode(true);
			
			////ETRUSTEX-603 remove xml comments from response header
			XMLUtil.removeRecursively(headerNodeClone, Node.COMMENT_NODE);
			
			HeaderType headerType = extractHeader(messageContext, request,
					headerNode);

			
			
			if (("{" + WRAPPER_NS + "}"+STORE_WRP_TX_LN)
					.equals(wsRootQname.toString())) {
				storeWrapper(headerType, headerNodeClone, messageContext);
			} else if (("{" + WRAPPER_NS + "}"+RETRIEVE_WRP_TX_LN)
					.equals(wsRootQname.toString())) {
				retrieveWrapper(headerType, headerNodeClone, messageContext);
			} else if (("{" + WRAPPER_NS + "}"+DELETE_WRP_TX_LN)
					.equals(wsRootQname.toString())) {
				deleteWrapper(headerType, headerNodeClone, messageContext);
			} else {
				// Should never happened after endpoint mapping routing
				setSoapFault(
						createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), messageContext);
				return;
			}

		} catch (MessageProcessingException mpe) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getStackTrace(mpe));
			SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
			if (mpe.getFaultDetailResponseCode() == null) {
				response.getSoapBody().addClientOrSenderFault(mpe.getDescription(), mpe.getLocale());
			} else {
				setSoapFault(
						createSoapFault(mpe.getFaultDetailResponseCode()), messageContext);
			}
		} catch (BadRequestException bre) {
			throw bre;
		} catch (RequestEntityTooLargeException retl) {
			throw retl;
		} catch (Exception e) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getStackTrace(e));
			logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
			logService.saveLog(logDTO);
			setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), messageContext);
		}finally{
//			release streams
			try{
				Iterator  it =  request.getSaajMessage().getAttachments();
				
				while(it.hasNext()){
					AttachmentPart att = (AttachmentPart)it.next();
					try{
						//att.getDataHandler().getInputStream().close();
						att.clearContent();
						
					}catch(Exception e2){
						logger.error("Error when closing attachment stream"+ org.apache.commons.lang.exception.ExceptionUtils
								.getFullStackTrace(e2));
						logDTO.setDescription(logDTO.getDescription() + " Error when closing attachment stream " + e2.getMessage());
						logService.saveLog(logDTO);
					}		
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
				logger.error("Error when releasing attachment streams"+ org.apache.commons.lang.exception.ExceptionUtils
						.getFullStackTrace(e));
				logDTO.setDescription(logDTO.getDescription() + " Error when releasing attachment streams " + e.getMessage());
				logService.saveLog(logDTO);
			} 
			
		}
		
		
	}
		
	private void storeWrapper(HeaderType header, Node headerNode,
			MessageContext context) {
		StoreDocumentWrapperRequest storeDocumentWrapperRequest = null;
		Long binaryId = null;
		try {
			logger.debug("storeDocumentWrapper start");
			
			storeDocumentWrapperRequest = extractStoreDocumentWrapperRequest(context);
			if (storeDocumentWrapperRequest == null
					|| storeDocumentWrapperRequest.getDocumentWrapper() == null) {
				setSoapFault(
						createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
				return;
			}

			TrustExMessage<String> message = new TrustExMessage<String>(null);
			String authenticatedUser = soapService.getUserPrincipal();

			TrustExMessageHeader messageHeader = buildMessageHeader(header, authenticatedUser);
			messageHeader
					.setTransactionRequestLocalName(STORE_WRP_TX_LN);
			if (storeDocumentWrapperRequest.getDocumentWrapper().getID() != null) {
				messageHeader.setMessageDocumentId(storeDocumentWrapperRequest
						.getDocumentWrapper().getID().getValue().trim());
			}
			if (storeDocumentWrapperRequest.getDocumentWrapper().getIssueDate() != null) {
				Calendar issueDate = storeDocumentWrapperRequest.getDocumentWrapper().getIssueDate().getValue()
						.toGregorianCalendar();
			    //ETRUSTEX-859 issue date should be in the range 1900-2100
			    if(issueDate.get(Calendar.YEAR) < 1900 || issueDate.get(Calendar.YEAR) > 2100) {
			    	throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),
							null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			    }
				messageHeader.setIssueDate(issueDate.getTime());
			}
			
			checkAuthorisation(messageHeader);
			
			message.setHeader(messageHeader);
			TrustExMessageBinary binary = buildMessageBinary(storeDocumentWrapperRequest, messageHeader);
			
			try{
				binaryId = messageBinaryGateway.storeMessageBinary(binary);
			}catch(Exception e){
				try{
					binary.closeInsputStream();					
				}catch(Exception ex){		
					logger.error(ex.getMessage(),ex);			
					//to do nothing
				}
				throw e;
			}
			
			binary.setBinaryId(binaryId);
			binary.setBinaryContentIS(null);
			message.addBinary(binary);
			storeDocumentWrapperRequest.getDocumentWrapper()
					.getResourceInformationReference().getLargeAttachment()
					.getStreamBase64Binary().setValue(null);
			
			//check that envelope is max 5MB
			long startTime = System.currentTimeMillis();
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setClassesToBeBound(StoreDocumentWrapperRequest.class);
			ByteArrayOutputStream bios = new ByteArrayOutputStream();
			StreamResult streamResult = new StreamResult(bios);
			marshaller.marshal(storeDocumentWrapperRequest, streamResult);
			logger.debug("Marshalling took " + (System.currentTimeMillis() - startTime) + "ms for " + bios.size() + " bytes");
			if (bios.size() > ETrustEXSoapInboundGateway.MAX_SOAP_ENVELOPE_SIZE) {
				String wrapperId = storeDocumentWrapperRequest != null && storeDocumentWrapperRequest.getDocumentWrapper() != null 
						&& storeDocumentWrapperRequest.getDocumentWrapper().getID() != null ? storeDocumentWrapperRequest.getDocumentWrapper().getID().getValue() : "";
				logger.error("DocumentWrapper " + wrapperId + " of size " + bios.size() + " bytes rejected");
				throw new RequestEntityTooLargeException();
			}			

			StringWriter writer = extractStoreWrapperPayload(storeDocumentWrapperRequest);
									
			message.setPayload(writer.toString());
			
			//MFF
			message.getHeader().setRawHeader(extractRawHeader(header));
			
			logger.debug("storeDocumentWrapper call messageProcessingGateway");

			messageProcessingGateway.processSynchMessage(message);
			
			logger.debug("storeDocumentWrapper start building response");
			StoreDocumentWrapperResponse response = XmlMessageTransformer_2_0
					.createStoreDocumentWrapperResponse(header,
							storeDocumentWrapperRequest.getDocumentWrapper());
			
			Source responseSource = new JAXBSource(getJaxBContext(StoreDocumentWrapperResponse.class.getPackage().getName()), response);

			buildResponse(headerNode, context, responseSource, messageHeader);
			logger.debug("storeDocumentWrapper stop");
		} catch (MissingAttachmentException e){
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.BINARY_ERROR.getDescription(),null, ErrorResponseCode.BINARY_ERROR, null, null);
		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage(), e);
			if (!cleanPhantomBinary(binaryId, context))
				return;
			setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED), context);
			return;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			if (!cleanPhantomBinary(binaryId, context))
				return;
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (XmlException e) {
			logger.error(e.getMessage(), e);
			if (!cleanPhantomBinary(binaryId, context))
				return;
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			if (!cleanPhantomBinary(binaryId, context))
				return;
			ErrorResponseCode errorCode = e.getFaultDetailResponseCode();
			if (errorCode == null) {
				SaajSoapMessage response = (SaajSoapMessage) context.getResponse();
				response.getSoapBody().addClientOrSenderFault(e.getDescription(), e.getLocale());
			} else {
				setSoapFault(createSoapFault(errorCode, e.getFaultDetailDescription()), context);
			}
			return;
		} catch (RequestEntityTooLargeException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (!cleanPhantomBinary(binaryId, context))
				return;
			setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
			return;
		} finally {
			releaseResources(storeDocumentWrapperRequest);
		}
	}
	
	private String extractRawHeader(HeaderType header){
		ObjectFactory objectFactory = new ObjectFactory(); 
		JAXBElement<HeaderType> jaxbElement = objectFactory.createHeader(header);
		String document = null;
		try {
			final StringWriter w = new StringWriter();
			Marshaller mar = getJaxBContext(HeaderType.class.getPackage().getName()).createMarshaller();
			mar.marshal(jaxbElement, w);
			document = w.toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		
		return document;
	}
	
	private void deleteWrapper(HeaderType header, Node headerNode,
			MessageContext context) {

		try {						
			DeleteDocumentWrapperRequestRequest deleteDocumentWrapperRequestRequest = null;
			logger.debug("deleteWrapper start");
			deleteDocumentWrapperRequestRequest = extractDeleteDocumentWrapperRequest(context);

			if (deleteDocumentWrapperRequestRequest == null
					|| deleteDocumentWrapperRequestRequest
							.getDocumentWrapperRequest() == null) {
				setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);				
				return;
			}

			TrustExMessage<String> message = new TrustExMessage<String>(null);
			String authenticatedUser = soapService.getUserPrincipal();

			TrustExMessageHeader messageHeader = buildMessageHeader(header, authenticatedUser);
			messageHeader.setAuthenticatedUser(authenticatedUser);
			messageHeader
					.setTransactionRequestLocalName(DELETE_WRP_TX_LN);
			if (deleteDocumentWrapperRequestRequest
							.getDocumentWrapperRequest()
							.getDocumentReferenceRequest().getID() != null) {
				messageHeader
						.setMessageDocumentId(deleteDocumentWrapperRequestRequest
								.getDocumentWrapperRequest()
								.getDocumentReferenceRequest().getID().getValue().trim());
			}
			message.setHeader(messageHeader);

			StringWriter writer = extractDeleteWrapperPayload(deleteDocumentWrapperRequestRequest);
			
			checkAuthorisation(messageHeader);
			message.setPayload(writer.toString());
			
			logger.debug("deleteWrapper call messageProcessingGateway");
			messageProcessingGateway.processSynchMessage(message);
			logger.debug("deleteWrapper start building response");
			DeleteDocumentWrapperRequestResponse response = XmlMessageTransformer_2_0
					.createDeleteDocumentWrapperResponse(header,
							deleteDocumentWrapperRequestRequest
									.getDocumentWrapperRequest());

			Source responseSource = new JAXBSource(getJaxBContext(DeleteDocumentWrapperRequestResponse.class.getPackage().getName()), response);

			buildResponse(headerNode, context, responseSource, messageHeader);

			logger.debug("deleteDocumentWrapper stop");
			
		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED), context);
			return;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (XmlException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			ErrorResponseCode errorCode = e.getFaultDetailResponseCode();
			setSoapFault(createSoapFault(errorCode), context);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
			return;
		}
	}
	
	//TODO Validation is done Anyway (For Retrieve and Delete) regardless of the Metadata Validation flag in order not to do it twice (XSDValidationServiceActivator)
	private XsdValidationErrorHandler validatePayload(String messageString, String xsdNamespace, String txLocalName) throws DOMException, SOAPException, TransformerException, SAXException, IOException{
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);						
		Transaction t = authorisationService.getTransactionByNameSpace(WRAPPER_NS, txLocalName);
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData(null, t.getId(), t.getDocument().getId(), null);
		//MetaDataItem validateXsdItem=  metadata.get(MetaDataItemType.SYNCH_VALIDATE_XSD);
	  	MetaDataItem xsdItem =  metadata.get(MetaDataItemType.DOCUMENT_XSD);
	  	MetaDataItem xsdURLItem=  metadata.get(MetaDataItemType.DOCUMENT_XSD_URL);
	  	MetaDataItem serverURLItem 	 =  metadata.get(MetaDataItemType.SERVER_URL);
	  	Schema schema = getSchemaFromMetadata(xsdItem, xsdURLItem, serverURLItem);
		schemaFactory.setResourceResolver(new LSResourceResolver() {
			@Override
			public LSInput resolveResource(String type, String namespaceURI,
					String publicId, String systemId, String baseURI) {
				throw new RuntimeException();
			}
		});
		Validator validator = schema.newValidator();
		XsdValidationErrorHandler xsdValidationErrorHandler = new XsdValidationErrorHandler();
		validator.setErrorHandler(xsdValidationErrorHandler);			        
        //Replacing the WSDL-NS with Schema-NS
        messageString = messageString.replace(WRAPPER_NS, xsdNamespace);
		validator.validate(new StreamSource(new StringReader(messageString)));     		
		return xsdValidationErrorHandler;
	}
	
	@SuppressWarnings("unchecked")
	private void retrieveWrapper(HeaderType header, Node headerNode,
			MessageContext context) {

		try {
//			String messageString = getPayloadAsString(context, "DocumentWrapperRequest");			
//			XsdValidationErrorHandler errorH = validatePayload(messageString, DOCUMENT_WRAPPER_REQUEST_XSD_NS, RETRIEVE_WRP_TX_LN);
//			if(!errorH.isValid()){
//				setSoapFault(
//						createSoapFault(
//								ErrorResponseCode.DOCUMENT_XSD_INVALID,
//								errorH.getErrorMessage(), "DWR"), context);
//				return;
//			}
			
			RetrieveDocumentWrapperRequestRequest retrieveDocumentWrapperRequestRequest = null;
			logger.debug("retrieveWrapper start");
			retrieveDocumentWrapperRequestRequest = extractRetrieveDocumentWrapperRequest(context);

			if (retrieveDocumentWrapperRequestRequest == null
					|| retrieveDocumentWrapperRequestRequest
							.getDocumentWrapperRequest() == null) {
				setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
				return;
			}
			

			TrustExMessage<String> message = new TrustExMessage<String>(null);
			String authenticatedUser = soapService.getUserPrincipal();

			TrustExMessageHeader messageHeader = buildMessageHeader(header, authenticatedUser);
			messageHeader.setAuthenticatedUser(authenticatedUser);
			messageHeader.setTransactionRequestLocalName(RETRIEVE_WRP_TX_LN);
			if (retrieveDocumentWrapperRequestRequest
							.getDocumentWrapperRequest()
							.getDocumentReferenceRequest().getID() != null) {
				messageHeader
						.setMessageDocumentId(retrieveDocumentWrapperRequestRequest
								.getDocumentWrapperRequest()
								.getDocumentReferenceRequest().getID().getValue().trim());
			}
			message.setHeader(messageHeader);

			StringWriter writer = extractRetrieveWrapperPayload(retrieveDocumentWrapperRequestRequest);
			
			checkAuthorisation(messageHeader);
			
			message.setPayload(writer.toString());			
			
			logger.debug("retrieveWrapper call messageProcessingGateway");
			JAXBElement<DocumentWrapperType> retrievedWrapperElement = ((TrustExMessage<JAXBElement<DocumentWrapperType>>) messageProcessingGateway
					.processSynchMessage(message)).getPayload();
			logger.debug("retrieveWrapper start building response");

			RetrieveDocumentWrapperRequestResponse documentWrapperRequestResponse = new RetrieveDocumentWrapperRequestResponse();
			if (retrievedWrapperElement != null) {
				documentWrapperRequestResponse
						.setDocumentWrapper(retrievedWrapperElement.getValue());
			}

			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller
					.setClassesToBeBound(RetrieveDocumentWrapperRequestResponse.class);
			marshaller.setMtomEnabled(true);

			WebServiceMessage soapResponse = context.getResponse();
			MarshallingUtils.marshal(marshaller,
					documentWrapperRequestResponse, soapResponse);
			updateHeader(headerNode, context, messageHeader);//
			logger.debug("retrieveWrapper stop");
		
		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED), context);
			return;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (XmlException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			ErrorResponseCode errorCode = e.getFaultDetailResponseCode();
			setSoapFault(createSoapFault(errorCode), context);
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
			return;
		}
	}

	private StringWriter extractRetrieveWrapperPayload(
			RetrieveDocumentWrapperRequestRequest retrieveDocumentWrapperRequestRequest)
			throws JAXBException, PropertyException {
		StringWriter writer = new StringWriter();

		Marshaller marshaller = getJaxBContext(RetrieveDocumentWrapperRequestRequest.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		JAXBElement<DocumentWrapperRequestType> jaxbElement = new JAXBElement<DocumentWrapperRequestType>(
				new QName(DOCUMENT_WRAPPER_REQUEST_XSD_NS,
						"DocumentWrapperRequest", "ec"),
				DocumentWrapperRequestType.class,
				retrieveDocumentWrapperRequestRequest
						.getDocumentWrapperRequest());

		marshaller.marshal(jaxbElement, writer);
		return writer;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private RetrieveDocumentWrapperRequestRequest extractRetrieveDocumentWrapperRequest(
			MessageContext context) {
		RetrieveDocumentWrapperRequestRequest retrieveDocumentWrapperRequest = null;
		// extract a RetrieveDocumentWrapperRequest from the SOAP Envelope
		// request
		try {
			SaajSoapMessage request = (SaajSoapMessage) context.getRequest();

			Unmarshaller unmarshaller = getJaxBContext(RetrieveDocumentWrapperRequestRequest.class.getPackage().getName()).createUnmarshaller();
//			unmarshaller.setValidating(false);

			SOAPBody body = request.getSaajMessage().getSOAPBody();

			NodeList nl = body.getElementsByTagNameNS(WRAPPER_NS,
					"RetrieveDocumentWrapperRequestRequest");
			if (nl.getLength() != 1)
				throw new RuntimeException(
						"Invalid ! Multiple RetrieveDocumentWrapperRequestRequest");

			Node bodyNode = nl.item(0);
			final Iterator attIterator = request.getAttachments();

			JAXBElement bodyObject = (JAXBElement) unmarshaller.unmarshal(
					bodyNode, RetrieveDocumentWrapperRequestRequest.class);
			retrieveDocumentWrapperRequest = (RetrieveDocumentWrapperRequestRequest) bodyObject
					.getValue();
		} catch (Exception e) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			retrieveDocumentWrapperRequest = null;
		}
		return retrieveDocumentWrapperRequest;
	}

	private StringWriter extractDeleteWrapperPayload(
			DeleteDocumentWrapperRequestRequest deleteDocumentWrapperRequestRequest)
			throws JAXBException, PropertyException {
		StringWriter writer = new StringWriter();
		Marshaller marshaller = getJaxBContext(DeleteDocumentWrapperRequestRequest.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		JAXBElement<DocumentWrapperRequestType> jaxbElement = new JAXBElement<DocumentWrapperRequestType>(
				new QName(DOCUMENT_WRAPPER_REQUEST_XSD_NS,
						"DocumentWrapperRequest", "ec"),
				DocumentWrapperRequestType.class,
				deleteDocumentWrapperRequestRequest.getDocumentWrapperRequest());

		marshaller.marshal(jaxbElement, writer);
		return writer;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private DeleteDocumentWrapperRequestRequest extractDeleteDocumentWrapperRequest(
			MessageContext context) {
		DeleteDocumentWrapperRequestRequest deleteDocumentWrapperRequest = null;
		// extract a DeleteDocumentWrapperRequest from the SOAP Envelope request
		try {
			SaajSoapMessage request = (SaajSoapMessage) context.getRequest();

			Unmarshaller unmarshaller = getJaxBContext(DeleteDocumentWrapperRequestRequest.class.getPackage().getName()).createUnmarshaller();			
//			unmarshaller.setValidating(false);
			
			
			SOAPBody body = request.getSaajMessage().getSOAPBody();

			NodeList nl = body.getElementsByTagNameNS(WRAPPER_NS,
					DELETE_WRP_TX_LN);
			if (nl.getLength() != 1)
				throw new RuntimeException(
						"Invalid ! Multiple DeleteDocumentWrapperRequestRequest");

			Node bodyNode = nl.item(0);
			final Iterator attIterator = request.getAttachments();

			JAXBElement bodyObject = (JAXBElement) unmarshaller.unmarshal(
					bodyNode, DeleteDocumentWrapperRequestRequest.class);
			deleteDocumentWrapperRequest = (DeleteDocumentWrapperRequestRequest) bodyObject
					
					
					.getValue();
		} catch (Exception e) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			deleteDocumentWrapperRequest = null;
		}
		return deleteDocumentWrapperRequest;
	}

	private void buildResponse(Node headerNode, MessageContext context,
			Source responseSource, TrustExMessageHeader trustExMessageHeader) throws TransformerException, SOAPException,
			JAXBException {
		WebServiceMessage soapResponse = context.getResponse();
		this.transformerSupportDelegate.transformSourceToResult(responseSource,
				soapResponse.getPayloadResult());

		updateHeader(headerNode, context, trustExMessageHeader);
	}

	private void updateHeader(Node headerNode, MessageContext context, TrustExMessageHeader trustExMessageHeader)
            throws SOAPException, XPathException {
		SaajSoapMessage saajResponse = (SaajSoapMessage) context.getResponse();
		SoapHeader sh = saajResponse.getSoapHeader();

		sh.addNamespaceDeclaration("urn1",
				"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
		sh.addNamespaceDeclaration("ec1",
				"ec:schema:xsd:CommonBasicComponents-0.1");
		sh.addNamespaceDeclaration("urn",
				"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
		sh.addNamespaceDeclaration("ec", WRAPPER_NS);

        soapService.addECHeaderNodeToResponse(headerNode, saajResponse.getSaajMessage().getSOAPHeader(), trustExMessageHeader);
	}

	@SuppressWarnings("rawtypes")
	private HeaderType extractHeader(MessageContext messageContext,
			SaajSoapMessage request, Node headerNode) throws SOAPException,
			JAXBException {

		Unmarshaller unmarshaller = getJaxBContext(HeaderType.class.getPackage().getName()).createUnmarshaller();

		Document doc = headerNode.getOwnerDocument();
		headerNode = doc.renameNode(headerNode,
				"ec:schema:xsd:CommonAggregateComponents-2", "HeaderType");
		JAXBElement<HeaderType> headerObject = unmarshaller.unmarshal(headerNode, HeaderType.class);
		HeaderType headerType = headerObject.getValue();
		return headerType;
	}

	private Node extractHeaderNode(MessageContext messageContext,
			SaajSoapMessage request) throws SOAPException {
		SOAPHeader header = request.getSaajMessage().getSOAPHeader();
		NodeList nl = header.getElementsByTagNameNS(WRAPPER_NS, "Header");

		// SOAP Header invalid
		if (nl == null || nl.getLength() != 1)
			return null;

		Node headerNode = nl.item(0);
		return headerNode;
	}

	private StringWriter extractStoreWrapperPayload(
			StoreDocumentWrapperRequest storeDocumentWrapperRequest)
			throws JAXBException, PropertyException {
		StringWriter writer = new StringWriter();
		Marshaller marshaller = getJaxBContext(DocumentWrapperType.class).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		JAXBElement<DocumentWrapperType> jaxbElement = new JAXBElement<DocumentWrapperType>(
				new QName(DOCUMENT_WRAPPER_XSD_NS, "DocumentWrapper",
						"ec"), DocumentWrapperType.class,
				storeDocumentWrapperRequest.getDocumentWrapper());
		marshaller.marshal(jaxbElement, writer);
		return writer;
	}

	private TrustExMessageBinary buildMessageBinary(
			StoreDocumentWrapperRequest storeDocumentWrapperRequest,
			TrustExMessageHeader messageHeader) throws IOException {
		TrustExMessageBinary binary = new TrustExMessageBinary();
		if (storeDocumentWrapperRequest!=null && storeDocumentWrapperRequest.getDocumentWrapper()!=null &&
				storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference() != null &&
				storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getDocumentSize() !=null &&
						storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getDocumentSize().getValue() != null	){
				
				Long size =  storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getDocumentSize().getValue().longValue();

				binary.setExpectedSize(size);	
			}	
		
		EntityAccessInfo info = new EntityAccessInfo();
		info.setCreationId(messageHeader.getAuthenticatedUser());
		info.setCreationDate(Calendar.getInstance().getTime());
		binary.setSubmitterIdWithScheme(messageHeader.getSenderIdWithScheme());
		binary.setIssuerParty(messageHeader.getIssuer());
		binary.setSenderParty(messageHeader.getSender());
		binary.setMessageDocumentId(messageHeader.getMessageDocumentId());
		binary.setMimeType(getContentType(storeDocumentWrapperRequest));
		binary.setBinaryContentIS(getDocumentWrapperBinary(storeDocumentWrapperRequest));
		binary.setBinaryType(storeDocumentWrapperRequest.getDocumentWrapper().getDocumentTypeCode().getValue());
		return binary;
	}

	private TrustExMessageHeader buildMessageHeader(HeaderType header, String authenticatedUser) throws UndefinedIdentifierException {
		TrustExMessageHeader messageHeader = new TrustExMessageHeader();
		messageHeader.setAuthenticatedUser(authenticatedUser);
		Party issuer = authorisationService.getMessageIssuer(authenticatedUser);
		messageHeader.setIssuer(issuer);

		List<Partner> senders = header.getBusinessHeader().getSender();
		if (senders == null || senders.isEmpty() || senders.size() > 1) {
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(), null,
					ErrorResponseCode.AUTHENTICATION_FAILED, null, null);
		}
		//build sender id with scheme
		String schemeId = senders.get(0).getIdentifier().getSchemeID();
		if (StringUtils.isNotBlank(schemeId)) {
			List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
			messageHeader.setSenderIdWithScheme(schemeId + metadata.get(0).getValue() + senders.get(0).getIdentifier()
					.getValue());
		} else {
			messageHeader.setSenderIdWithScheme(senders.get(0).getIdentifier().getValue());
		}
		Party sender = authorisationService.getParty(messageHeader.getSenderIdWithScheme(), issuer.getBusinessDomain());
		messageHeader.setSender(sender);
		messageHeader.setTransactionNamespace("ec:services:wsdl:DocumentWrapper-2");

		return messageHeader;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private StoreDocumentWrapperRequest extractStoreDocumentWrapperRequest(
			MessageContext context) {
		StoreDocumentWrapperRequest storeDocumentWrapperRequest = null;
		// extract a StoreDocumentWrapperRequest from the SOAP Envelope request
		try {
			SaajSoapMessage request = (SaajSoapMessage) context.getRequest();

			Unmarshaller unmarshaller = getJaxBContext(StoreDocumentWrapperRequest.class.getPackage().getName()).createUnmarshaller();
			unmarshaller.setAttachmentUnmarshaller(new EtrustexAttachmentUnmarshaller(request));
			//unmarshaller.setValidating(false);

			SOAPBody body = request.getSaajMessage().getSOAPBody();

			NodeList nl = body.getElementsByTagNameNS(WRAPPER_NS,
					STORE_WRP_TX_LN);
			if (nl.getLength() != 1)
				throw new RuntimeException(
						"Invalid ! Multiple StoreDocumentWrapperRequest");

			Node bodyNode = nl.item(0);
			JAXBElement bodyObject = (JAXBElement) unmarshaller.unmarshal(
					bodyNode, StoreDocumentWrapperRequest.class);
			storeDocumentWrapperRequest = (StoreDocumentWrapperRequest) bodyObject
					.getValue();
		} catch (Exception e) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			if (e.getCause() != null && e.getCause().getClass().getName().equals("org.jvnet.mimepull.MIMEParsingException")
					&& e.getMessage().contains("MaxPostSize") && e.getMessage().contains("exceeded")) {
				String wrapperId = storeDocumentWrapperRequest != null && storeDocumentWrapperRequest.getDocumentWrapper() != null 
						&& storeDocumentWrapperRequest.getDocumentWrapper().getID() != null ? storeDocumentWrapperRequest.getDocumentWrapper().getID().getValue() : "";
				logger.error("Document Wrapper " + wrapperId + " rejected");
				throw new RequestEntityTooLargeException();
			}
			storeDocumentWrapperRequest = null;
		}
		return storeDocumentWrapperRequest;
	}

	private void setSoapFault(SOAPFault fault, MessageContext messageContext) {
		try {
			Source responseSource = new DOMSource(fault);
			WebServiceMessage response = messageContext.getResponse();
			this.transformerSupportDelegate.transformSourceToResult(
					responseSource, response.getPayloadResult());
		} catch (Exception e2) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e2));
		}

	}
	
	private SOAPFault createSoapFault(ErrorResponseCode errorResponseCode) {
		return createSoapFault(errorResponseCode, null);
	}
	
	/**
	 * This is an utility method to create Soap fault in case of error processing
	 * the document wrappers
	 * 
	 * @param errorResponseCode
	 *            an Enum of possible error response codes
	 * @param message
	 *            the error message
	 *	@param faultDetailDescription
	 *            the detailed fault description 
	 * @return the Soap fault returned to the caller
	 * 
	 */
	private SOAPFault createSoapFault(ErrorResponseCode errorResponseCode, String faultDetailDescription) {
		try {

			SOAPFactory factory = SOAPFactory.newInstance();
			SOAPFault sf = factory.createFault(errorResponseCode.getDescription(), 
					new QName("http://schemas.xmlsoap.org/soap/envelope/",
					errorResponseCode.getFaultCode()));

			if (ErrorResponseCode.TECHNICAL_ERROR != errorResponseCode 
					&& ErrorResponseCode.AUTHENTICATION_FAILED != errorResponseCode) {
				Detail detail = sf.getDetail();

				if (detail == null) {
					detail = sf.addDetail();
				}
				String faultNS = errorResponseCode.equals(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE) 
					? ETrustEXDefaultEndpoint.DEFAULT_SOAP_FAULT_NAMESPACE : UBLNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace();
				SOAPElement element = detail.addDetailEntry(new QName(faultNS, "Fault", "ec"));

				String responseCode = ErrorResponseCode.DOCUMENT_ALREADY_EXISTS.equals(errorResponseCode) 
						? "DWR:" + errorResponseCode.getCode() : errorResponseCode.getCode();

				// For backward compatibility with old arch we just return the response code

				element.addChildElement("ResponseCode", "cbc",
						"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
						.setValue(responseCode);
				if (ErrorResponseCode.DOCUMENT_ALREADY_EXISTS.equals(errorResponseCode) && StringUtils.isNotBlank(faultDetailDescription)) {
					element.addChildElement("Description", "cbc",
							"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2")
							.setValue(faultDetailDescription);
				}
				
			}

			return sf;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This utility method is used to compensate any error occuring stroring the
	 * wrapper metadata. the system will store first the binary stream and the
	 * the metadata of the wrapper. The storing of the binary can be non
	 * transaction when the platform is configured with binary storage into
	 * files. so we need this method to delete the binary if an error occurs
	 * while storing the metadata.
	 * 
	 * @param binaryId
	 * @throws MessageProcessingException
	 */
	private boolean cleanPhantomBinary(Long binaryId, MessageContext context) {
		try {
			if (binaryId != null) {
				messageBinaryGateway.deleteMessageBinary(binaryId);
			}
			return true;
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(
					createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
			return false;
		}
	}

	/**
	 * Utility method called at the end of the wrapper processing to close the
	 * MTOM binary stream.
	 * 
	 * @param storeDocumentWrapperRequest
	 */
	private void releaseResources(
			StoreDocumentWrapperRequest storeDocumentWrapperRequest) {
		try {
			if (storeDocumentWrapperRequest != null
					&& storeDocumentWrapperRequest.getDocumentWrapper() != null
					&& storeDocumentWrapperRequest.getDocumentWrapper()
							.getResourceInformationReference()
							.getLargeAttachment() != null
					&& storeDocumentWrapperRequest.getDocumentWrapper()
							.getResourceInformationReference()
							.getLargeAttachment().getStreamBase64Binary() != null
					&& storeDocumentWrapperRequest.getDocumentWrapper()
							.getResourceInformationReference()
							.getLargeAttachment().getStreamBase64Binary()
							.getValue() != null) {
				storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary().getValue().getInputStream()
						.close();
			}
		} catch (Exception e) {
			// logger.error(e);
		}
	}

	/**
	 * Utility method to retrieve the MTOM binary stream from the request
	 * 
	 * @param storeDocumentWrapperRequest
	 *            the request
	 * @return the MTOM binary stream
	 * @throws IOException
	 */
	private InputStream getDocumentWrapperBinary(
			StoreDocumentWrapperRequest storeDocumentWrapperRequest)
			throws IOException {
		InputStream inputStream = null;
		if (storeDocumentWrapperRequest != null
				&& storeDocumentWrapperRequest.getDocumentWrapper() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary().getValue() != null) {
			inputStream = storeDocumentWrapperRequest.getDocumentWrapper()
					.getResourceInformationReference().getLargeAttachment()
					.getStreamBase64Binary().getValue().getInputStream();

		}
		return inputStream;
	}

	/**
	 * Utility method to retrieve the content type of the binary
	 * 
	 * @param storeDocumentWrapperRequest
	 *            the request
	 * @return the MTOM binary stream
	 * @throws IOException
	 */
	private String getContentType(
			StoreDocumentWrapperRequest storeDocumentWrapperRequest)
			throws IOException {
		String contentType = null;
		if (storeDocumentWrapperRequest != null
				&& storeDocumentWrapperRequest.getDocumentWrapper() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary().getValue() != null) {
			contentType = storeDocumentWrapperRequest.getDocumentWrapper()
					.getResourceInformationReference().getLargeAttachment()
					.getStreamBase64Binary().getValue().getContentType();
			if (!soapService.isMultipartHttpRequest()) {
				//ETRUSTEX-1349 embedded Base64 binary - get the contentType attribute, otherwise contentType of the DataHandler is by default "application/octet-stream"
				String contentTypeAttr = storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary().getContentType();
				contentType = StringUtils.isNotBlank(contentTypeAttr) ? contentTypeAttr : contentType;
			}			
		}

		return contentType;
	}

	public IMessageProcessingGateway getMessageProcessingGateway() {
		return messageProcessingGateway;
	}

	public void setMessageProcessingGateway(
			IMessageProcessingGateway messageProcessingGateway) {
		this.messageProcessingGateway = messageProcessingGateway;
	}

	public IMessageBinaryGateway getMessageBinaryGateway() {
		return messageBinaryGateway;
	}

	public void setMessageBinaryGateway(
			IMessageBinaryGateway messageBinaryGateway) {
		this.messageBinaryGateway = messageBinaryGateway;
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

	public IXmlService getXmlService() {
		return xmlService;
	}

	public void setXmlService(IXmlService xmlService) {
		this.xmlService = xmlService;
	}

	private class TransformerSupportDelegate extends TransformerObjectSupport {
		void transformSourceToResult(Source source, Result result)
				throws TransformerException {
			this.transform(source, result);
		}
	}

	
	private static JAXBContext getJaxBContext(String key) throws JAXBException{		
		synchronized (jaxbContextMap) {
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
	}
	
	private static JAXBContext getJaxBContext(Class param) throws JAXBException{
		synchronized (jaxbContextMap) {
			String key = param.getPackage().getName();		
			if (jaxbContextMap.containsKey(key)){
				return jaxbContextMap.get(key);
			}else{
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(param);
					jaxbContextMap.put(key, jaxbContext);
					return jaxbContext;
				} catch (JAXBException e) {
					throw e;
				}
			}
		}
	}
	
	private Schema getSchemaFromMetadata(MetaDataItem localDbItem, MetaDataItem urlItem, MetaDataItem serverURLItem) throws SAXException, MalformedURLException{
		logger.debug("schema source localDbItem length [{}] urlItem [{}]", localDbItem == null ? null: localDbItem.getValue().length(), urlItem == null ? null : urlItem.getValue());
		Schema schema = null;
		if (localDbItem != null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schemaFactory.setResourceResolver(new ResourceToDatabaseResolver(xmlService));
			StreamSource source = new StreamSource(
					metadataService.getMetadataResourceAsStream(localDbItem
							.getId()));
			
			logger.debug("schema source system id[{}] public id[{}]", source.getSystemId(), source.getPublicId());
			schema = schemaFactory.newSchema(source);		
		} else if (urlItem != null) {
			if(urlItem.getValue().startsWith("http")){
				schema = getSchema(urlItem.getValue());
			}else if (urlItem.getValue().startsWith("/") && serverURLItem !=null){
				schema = getSchema(serverURLItem.getValue().concat(urlItem.getValue()));
			}			
		}
		return schema;
	}
	
	private static Schema getSchema(String url) throws MalformedURLException, SAXException {
		synchronized (schemasMap) {
			if (schemasMap.containsKey(url)){
				return schemasMap.get(url);
			}else{
				SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);				
				URL schemaFile = new URL(url);
				Schema sc = schemaFactory.newSchema(schemaFile);
				schemaFactory.setResourceResolver(new LSResourceResolver() {
					@Override
					public LSInput resolveResource(String type, String namespaceURI,
							String publicId, String systemId, String baseURI) {
						throw new RuntimeException();
					}
				});
				schemasMap.put(url, sc);
				return schemasMap.get(url);
			}
		}
	}
	
	/**
	 * Checks only if the issuer & party exist
	 * @param messageHeader
	 * @throws UndefinedIdentifierException
	 */
	
	private void checkAuthorisation(TrustExMessageHeader messageHeader) throws UndefinedIdentifierException{
		Transaction tx = authorisationService.getTransactionByNameSpace(messageHeader.getTransactionNamespace(), messageHeader.getTransactionRequestLocalName());
		messageHeader.setTransactionTypeId(tx.getId());
		//authorisationService.checkTransactionAuthorisation(issuerParty, senderParty, tx.getId());		
	}
	
}


