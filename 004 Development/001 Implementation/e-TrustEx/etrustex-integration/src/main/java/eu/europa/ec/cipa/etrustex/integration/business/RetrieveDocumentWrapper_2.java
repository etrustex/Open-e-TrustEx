package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2.Base64BinaryType;
import ec.schema.xsd.commonaggregatecomponents_2.LargeAttachmentType;
import ec.schema.xsd.documentwrapper_1.DocumentWrapperType;
import ec.schema.xsd.documentwrapper_1.ObjectFactory;
import ec.schema.xsd.documentwrapperrequest_1.DocumentWrapperRequestType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.util.InputStreamDataSource;
import eu.europa.ec.cipa.etrustex.services.exception.MessageRetrieveException;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;

public class RetrieveDocumentWrapper_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(RetrieveDocumentWrapper_2.class);
	
	private static JAXBContext jaxbContext = null;
	private static JAXBContext jaxbContextDWT = null;
	
	private static String BDL_REQUEST = "SubmitDocumentBundleRequest";
	private static String BRIS_REQUEST = "SubmitBRISDocumentRequest";
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
		ObjectFactory factory = new ObjectFactory();
	
			try {
				Source s= new StreamSource(new StringReader(message.getPayload()));
				
				JAXBElement<DocumentWrapperRequestType> requestElement = getJaxBContext().createUnmarshaller().unmarshal(s,DocumentWrapperRequestType.class);
				DocumentWrapperRequestType request = requestElement.getValue();
				String wrapperIssuer= request.getSenderParty().getEndpointID().getValue();
				Party wrapperIssuerParty=  authorisationService.getParty(wrapperIssuer, message.getHeader().getIssuer().getBusinessDomain());
				
				responseMessage.setHeader(message.getHeader());
				
				Transaction documentWrappertransaction= authorisationService.getTransactionByNameSpace("ec:services:wsdl:DocumentWrapper-2", "StoreDocumentWrapperRequest");
				Set<Transaction> transactions = new HashSet<Transaction>();
				transactions.add(documentWrappertransaction);
				List<Message> queryResult = messageService.retrieveMessages(null,wrapperIssuerParty.getId(),null,null,transactions,null,message.getHeader().getMessageDocumentId(),request.getDocumentReferenceRequest().getDocumentTypeCode().getValue(),null,false,false,true);
				JAXBElement<DocumentWrapperType> wrapper = null;
				
				if (queryResult == null || queryResult.size() !=1){
					responseMessage.setPayload(null);
					return responseMessage;
				}
				Message toReturn = queryResult.get(0);
				if (! message.getHeader().getSenderPartyId().equals(wrapperIssuerParty.getId())){
					Set<Message> parents = toReturn.getParentMessages();
					Boolean bundleExists = false;
					for (Message parent : parents) {
						String parentReq = parent.getTransaction().getRequestLocalName();
						if (message.getHeader().getSenderPartyId().equals(parent.getReceiver().getId()) && (BDL_REQUEST.equalsIgnoreCase(parentReq) || BRIS_REQUEST.equalsIgnoreCase(parentReq))){
							bundleExists = true;
						}
					}
					if (!bundleExists){
						throw new BusinessException("soapenv:Client","Unauthorized Access",null,ErrorResponseCode.AUTHENTICATION_FAILED,null,"Unauthorized Access");
					}
				}
				
				InputStream is = null;
				String contentType = null;
				Set<MessageBinary> binaries=  toReturn.getBinaries();
				byte[] iv = null;
				for (MessageBinary messageBinary : binaries) {
					if (MessageBinaryType.RAW_MESSAGE.name().equalsIgnoreCase(messageBinary.getBinaryType())){
						Unmarshaller unmarshaller = getJaxBContextDWT().createUnmarshaller();
						wrapper = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(messageBinary.getBinary())),DocumentWrapperType.class);
					}
					if (request.getDocumentReferenceRequest().getDocumentTypeCode().getValue().equals(messageBinary.getBinaryType())){
						contentType = messageBinary.getMimeCode();
						if (messageBinary.getFileId() != null){
							is = new FileInputStream(messageBinary.getFileId()); 
							iv = messageBinary.getIv();
						}
					}
				}
				
				if (wrapper !=null && is!= null){
					InputStream decryptingIs= messageService.getDecryptedStream(is, iv);
					DataHandler dh = contentType != null ? 
							new DataHandler(new InputStreamDataSource(decryptingIs, contentType)) : 
							new DataHandler(new InputStreamDataSource(decryptingIs));
					
					// Fix for document wrappers created using the EJB, they have the large attachment element null as the binary is passed separately
					if 	(wrapper.getValue().getResourceInformationReference().getLargeAttachment() == null){
						LargeAttachmentType largeAttachment = new LargeAttachmentType();
						Base64BinaryType binaryType = new Base64BinaryType();
						String mime = contentType;
						if(StringUtils.isNotEmpty(mime)){
							int sep = mime.indexOf(';'); 
							if(sep != -1){
								mime = mime.substring(0, sep);
							}
						}
						binaryType.setMimeCode(mime.trim());
						largeAttachment.setStreamBase64Binary(binaryType);
						wrapper.getValue().getResourceInformationReference().setLargeAttachment(largeAttachment);
					}
					wrapper.getValue().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary().setValue(dh);
					wrapper.getValue().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary().setContentType(contentType);	
				}else{
					throw new BusinessException("soapenv:Server",ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
							ErrorResponseCode.TECHNICAL_ERROR, null, null);
				}
				responseMessage.setPayload(wrapper);
				return responseMessage;
			} catch (UndefinedIdentifierException e) {
				responseMessage.setHeader(message.getHeader());
				responseMessage.setPayload(null);
				return responseMessage;
				//throw new BusinessException("soapenv:Server",ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						//ErrorResponseCode.TECHNICAL_ERROR, null, null);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(),e);
				throw new BusinessException("soapenv:Server",ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			} catch (JAXBException e) {
				logger.error(e.getMessage(),e);
				throw new BusinessException("soapenv:Server",ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			} catch (MessageRetrieveException e) {
				logger.error(e.getMessage(),e);
				throw new BusinessException("soapenv:Server",ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		
	}
	
	private static JAXBContext getJaxBContext(){
		if (jaxbContext == null){
			try {
				jaxbContext = JAXBContext.newInstance(DocumentWrapperRequestType.class);				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}
	
	private static JAXBContext getJaxBContextDWT(){
		if (jaxbContextDWT == null){
			try {
				jaxbContextDWT = JAXBContext.newInstance(DocumentWrapperType.class);				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContextDWT;
	}
	
}
