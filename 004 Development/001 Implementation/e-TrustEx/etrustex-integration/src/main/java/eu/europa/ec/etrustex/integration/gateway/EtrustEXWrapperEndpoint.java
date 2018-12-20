package eu.europa.ec.etrustex.integration.gateway;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MessageEndpoint;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.XmlException;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.w3c.dom.Node;

import ec.services.wsdl.documentwrapper_2.DeleteDocumentWrapperRequestRequest;
import ec.services.wsdl.documentwrapper_2.RetrieveDocumentWrapperRequestRequest;
import ec.services.wsdl.documentwrapper_2.StoreDocumentWrapperRequest;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.AuthorisationFailedException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.api.IMessageBinaryGateway;
import eu.europa.ec.etrustex.integration.api.IMessageProcessingGateway;
import eu.europa.ec.etrustex.integration.exception.BadRequestException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.exception.RequestEntityTooLargeException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.integration.util.SOAPService;
import eu.europa.ec.etrustex.integration.util.XMLUtil;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.services.IXmlService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import org.w3c.dom.NodeList;

public class EtrustEXWrapperEndpoint implements MessageEndpoint {

	private static final Logger logger = LoggerFactory
			.getLogger(EtrustEXWrapperEndpoint.class);
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private SOAPService soapService;

    @Autowired
    private Properties soapErrorMessages;
	
	@Autowired
	private IMessageService messageService;
	
	private static final String STORE_WRP_TX_LN    = "StoreDocumentWrapperRequest";
	private static final String RETRIEVE_WRP_TX_LN = "RetrieveDocumentWrapperRequestRequest";
	private static final String DELETE_WRP_TX_LN   = "DeleteDocumentWrapperRequestRequest";

	private static final String STORE_WRP_DOC_LN = "DocumentWrapper";
	private static final String RETRIEVE_WRP_DOC_LN = "DocumentWrapperRequest";
	private static final String DELETE_WRP_DOC_LN = "DocumentWrapperRequest";
	
	
	private IMessageProcessingGateway messageProcessingGateway;
	private IMessageBinaryGateway messageBinaryGateway;
	private IAuthorisationService authorisationService;
	private IMetadataService metadataService;
	private IXmlService xmlService;
	@Autowired
	private AbstractWrapperEndpointUtil endpointUtil;
	
	private final TransformerSupportDelegate transformerSupportDelegate = new TransformerSupportDelegate();
	
	@SuppressWarnings({ "rawtypes" })
	@Override
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
			
			Node headerNode = endpointUtil.extractHeaderNode(messageContext, request);
			if (headerNode == null) {
                setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED, soapErrorMessages.getProperty("error.unauthorized.SOAPHeaderElement")),
						messageContext);
				return;
			}
			Node headerNodeClone = headerNode.cloneNode(true);
			
			////ETRUSTEX-603 remove xml comments from response header
			XMLUtil.removeRecursively(headerNodeClone, Node.COMMENT_NODE);

			Object headerType = endpointUtil.extractHeader(messageContext, request, headerNode);

			if (("{" + EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace() + "}"+STORE_WRP_TX_LN)
					.equals(wsRootQname.toString())) {
				storeWrapper(headerType, headerNodeClone, messageContext);
			} else if (("{" + EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace() + "}"+RETRIEVE_WRP_TX_LN)
					.equals(wsRootQname.toString())) {
				retrieveWrapper(headerType, headerNodeClone, messageContext);
			} else if (("{" + EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace() + "}"+DELETE_WRP_TX_LN)
					.equals(wsRootQname.toString())) {
				deleteWrapper(headerType, headerNodeClone, messageContext);
			} else {
				// Should never happened after endpoint mapping routing
				setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), messageContext);
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
		
	private void storeWrapper(Object header, Node headerNode,
			MessageContext context) {
		
		Object storeDocumentWrapperRequest = null;
		TrustExMessageBinary binary = null;
		boolean deleteDbRecordsRequired = true;
		try {
			logger.debug("storeDocumentWrapper start");
			
			storeDocumentWrapperRequest = endpointUtil.extractStoreDocumentWrapperRequest(context);
			if (storeDocumentWrapperRequest == null) {
				setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
				return;
			}
			
			TrustExMessage<String> message = new TrustExMessage<String>(null);
			String authenticatedUser = soapService.getUserPrincipal();
			
			List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
			String SEP = metadata.get(0).getValue();
			
			TrustExMessageHeader messageHeader = endpointUtil.buildMessageHeader(header, authenticatedUser, SEP);
			
			messageHeader.setTransactionRequestLocalName(STORE_WRP_TX_LN);
			checkAuthorisation(messageHeader);
			
			message.setHeader(messageHeader);
			
			//check if wrapper is duplicate in order to avoid writing data to the DB and the file system
			//TODO improve this because XSD validation is done later and any of these 2 elements could be empty/missing
			StoreDocumentWrapperRequest theRequest = (StoreDocumentWrapperRequest)storeDocumentWrapperRequest;
			
			if((theRequest.getDocumentWrapper().getID() == null)
					|| (theRequest.getDocumentWrapper().getDocumentTypeCode() == null)
					|| org.apache.commons.lang.StringUtils.isEmpty(theRequest.getDocumentWrapper().getDocumentTypeCode().getValue())){
				throw new MessageProcessingException("soapenv:Client",
						ErrorResponseCode.DOCUMENT_XSD_INVALID.getDescription(), null,
						ErrorResponseCode.DOCUMENT_XSD_INVALID, null, null);
			}

			if (org.apache.commons.lang.StringUtils.isEmpty(theRequest.getDocumentWrapper().getID().getValue())) {
				throw new MessageProcessingException("soapenv:Client",
						ErrorResponseCode.INVALID_MESSAGE_ID.getDescription(), null,
						ErrorResponseCode.INVALID_MESSAGE_ID,null, soapErrorMessages.getProperty("error.hardrule.docId"));
			}
			
			messageHeader.setDocumentTypeCode(theRequest.getDocumentWrapper() != null && theRequest.getDocumentWrapper().getDocumentTypeCode() != null
					? theRequest.getDocumentWrapper().getDocumentTypeCode().getValue()
					: null);
			messageHeader.setMessageDocumentId(theRequest.getDocumentWrapper() != null && theRequest.getDocumentWrapper().getID() != null
					? theRequest.getDocumentWrapper().getID().getValue().trim()
					: null);
			Message dbMessage = messageService.retrieveMessage(messageHeader.getMessageDocumentId(), messageHeader.getDocumentTypeCode(), messageHeader.getSender().getId(), null);
			if (dbMessage != null) {
				//a duplicate exists, but at this stage, nothing was saved in the DB yet
				deleteDbRecordsRequired = false;
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.DUPLICATE_ENTITY.getDescription(), null,
						ErrorResponseCode.DUPLICATE_ENTITY, null, soapErrorMessages.getProperty("error.hardrule.docId.duplicate"));
			}			
			
			binary = endpointUtil.buildMessageBinary(storeDocumentWrapperRequest, messageHeader, soapService.isMultipartHttpRequest());
			

			message.addBinary(binary);
			
			endpointUtil.validateEnvelopeSize(storeDocumentWrapperRequest);
			message.setPayload(endpointUtil.extractPayload(context, STORE_WRP_DOC_LN, EtrustexNamespaces.DOCUMENT_WRAPPER_XSD_NS_01));
			message.getHeader().setRawHeader(endpointUtil.extractRawHeader(header));
			
			logger.debug("storeDocumentWrapper call messageProcessingGateway");

			messageProcessingGateway.processSynchMessage(message);
			
			logger.debug("storeDocumentWrapper start building response");	
			Source responseSource = endpointUtil.createStoreDocumentWrapperResponse(header,storeDocumentWrapperRequest);
			endpointUtil.buildResponse(headerNode, context, responseSource, messageHeader);
			logger.debug("storeDocumentWrapper stop");
		} catch (AuthorisationFailedException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED,  soapErrorMessages.getProperty(e.getMessage())), context);			
		} catch (JAXBException | XmlException e) {
			logger.error(e.getMessage(), e);
			messageService.cleanPhantomBinary(binary != null? binary.getBinaryId():null);
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			if (deleteDbRecordsRequired) {		
				messageService.cleanPhantomBinary(binary != null? binary.getBinaryId():null);
			}
			ErrorResponseCode errorCode = e.getFaultDetailResponseCode();
			if (errorCode == null) {
				SaajSoapMessage response = (SaajSoapMessage) context.getResponse();
				response.getSoapBody().addClientOrSenderFault(e.getDescription(), e.getLocale());
			} else {
				setSoapFault(endpointUtil.createSoapFault(errorCode, e.getFaultDetailDescription()), context);
			}
		} catch (RequestEntityTooLargeException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			messageService.cleanPhantomBinary(binary != null? binary.getBinaryId():null);
			setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
		} finally {
			try {
				endpointUtil.releaseResources(storeDocumentWrapperRequest);
			} catch (IOException e) {
				setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
			}
        	if (binary != null) {
        		binary.closeInsputStream();
        	}			
		}
	}

	private void deleteWrapper(Object header, Node headerNode, MessageContext context) {
		try {						
			DeleteDocumentWrapperRequestRequest deleteDocumentWrapperRequestRequest = null;
			logger.debug("deleteWrapper start");

			TrustExMessage<String> message = new TrustExMessage<String>(null);
			String authenticatedUser = soapService.getUserPrincipal();

			List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
			String SEP = metadata.get(0).getValue();
			
			TrustExMessageHeader messageHeader = endpointUtil.buildMessageHeader(header, authenticatedUser, SEP);
			messageHeader.setAuthenticatedUser(authenticatedUser);
			messageHeader.setTransactionRequestLocalName(DELETE_WRP_TX_LN);
			
			deleteDocumentWrapperRequestRequest = (DeleteDocumentWrapperRequestRequest)endpointUtil.extractDeleteDocumentWrapperRequest(context, messageHeader);

			if (deleteDocumentWrapperRequestRequest == null) {
				setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);				
				return;
			}
			
			messageHeader.setDocumentTypeCode(deleteDocumentWrapperRequestRequest.getDocumentWrapperRequest().getDocumentReferenceRequest().getDocumentTypeCode().getValue());

			message.setHeader(messageHeader);

			checkAuthorisation(messageHeader);
			message.setPayload(endpointUtil.extractPayload(context, DELETE_WRP_DOC_LN, EtrustexNamespaces.DOCUMENT_WRAPPER_REQUEST_XSD_NS_01));
			
			logger.debug("deleteWrapper call messageProcessingGateway");
			messageProcessingGateway.processSynchMessage(message);
			logger.debug("deleteWrapper start building response");
			
			Source responseSource = endpointUtil.createDeleteDocumentWrapperResponse(header,deleteDocumentWrapperRequestRequest);

			endpointUtil.buildResponse(headerNode, context, responseSource, messageHeader);
			logger.debug("deleteDocumentWrapper stop");
		} catch (AuthorisationFailedException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED,  soapErrorMessages.getProperty(e.getMessage())), context);
			return;
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
			return;
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(e.getFaultDetailResponseCode(), e.getFaultDetailDescription()), context);
			return;
		} catch (SOAPException | TransformerException | IOException e) {
			logger.error(e.getMessage(), e);
			setSoapFault(createSoapFault(ErrorResponseCode.TECHNICAL_ERROR), context);
			return;
		}
	}
		
	private void retrieveWrapper(Object header, Node headerNode, MessageContext context) {

        try {
            RetrieveDocumentWrapperRequestRequest retrieveDocumentWrapperRequestRequest = null;
            logger.debug("retrieveWrapper start");

            TrustExMessage<String> message = new TrustExMessage<String>(null);
            String authenticatedUser = soapService.getUserPrincipal();

            List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
            String SEP = metadata.get(0).getValue();

            TrustExMessageHeader messageHeader = endpointUtil.buildMessageHeader(header, authenticatedUser, SEP);
            messageHeader.setAuthenticatedUser(authenticatedUser);
            messageHeader.setTransactionRequestLocalName(RETRIEVE_WRP_TX_LN);

            retrieveDocumentWrapperRequestRequest = (RetrieveDocumentWrapperRequestRequest) endpointUtil.extractRetrieveDocumentWrapperRequest(context, messageHeader);
            if (retrieveDocumentWrapperRequestRequest == null) {
                setSoapFault(createSoapFault(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE), context);
                return;
            }
            
            if(retrieveDocumentWrapperRequestRequest.getDocumentWrapperRequest().getDocumentReferenceRequest() == null || retrieveDocumentWrapperRequestRequest.getDocumentWrapperRequest().getDocumentReferenceRequest().getDocumentTypeCode() == null){
				throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.DOCUMENT_XSD_INVALID.getDescription(), null,
					ErrorResponseCode.DOCUMENT_XSD_INVALID, null, null);
			}
            
            messageHeader.setDocumentTypeCode(retrieveDocumentWrapperRequestRequest.getDocumentWrapperRequest().getDocumentReferenceRequest().getDocumentTypeCode().getValue());
            message.setHeader(messageHeader);

            checkAuthorisation(messageHeader);

            message.setPayload(endpointUtil.extractPayload(context, RETRIEVE_WRP_DOC_LN, EtrustexNamespaces.DOCUMENT_WRAPPER_REQUEST_XSD_NS_01));

            logger.debug("retrieveWrapper call messageProcessingGateway");

            @SuppressWarnings("rawtypes")
            TrustExMessage retrievedWrapper = messageProcessingGateway.processSynchMessage(message);

            logger.debug("retrieveWrapper start building response");

            endpointUtil.buildRetrieveWrapperResponse(headerNode, context, retrievedWrapper, messageHeader);

            logger.debug("retrieveWrapper stop");
        } catch (AuthorisationFailedException e) {
            logger.error(e.getMessage(), e);
            setSoapFault(createSoapFault(ErrorResponseCode.AUTHENTICATION_FAILED, soapErrorMessages.getProperty(e.getMessage())), context);
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
		return endpointUtil.createSoapFault(errorResponseCode, null);
	}
	
	private SOAPFault createSoapFault(ErrorResponseCode errorResponseCode, String faultDescription) {
		return endpointUtil.createSoapFault(errorResponseCode, faultDescription);
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
	
	/**
	 * @param messageHeader
	 * @throws UndefinedIdentifierException
	 */
	
	private void checkAuthorisation(TrustExMessageHeader messageHeader) {
		Transaction tx = authorisationService.getTransactionByNameSpace(messageHeader.getTransactionNamespace(), messageHeader.getTransactionRequestLocalName());
		messageHeader.setTransactionTypeId(tx.getId());	
	}	
}


