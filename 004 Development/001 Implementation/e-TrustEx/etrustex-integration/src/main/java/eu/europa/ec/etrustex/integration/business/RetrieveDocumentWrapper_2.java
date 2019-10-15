package eu.europa.ec.etrustex.integration.business;

import ec.schema.xsd.commonaggregatecomponents_2.Base64BinaryType;
import ec.schema.xsd.commonaggregatecomponents_2.DocumentSizeType;
import ec.schema.xsd.commonaggregatecomponents_2.LargeAttachmentType;
import ec.schema.xsd.documentwrapper_1.DocumentWrapperType;
import ec.schema.xsd.documentwrapper_1.ObjectFactory;
import ec.schema.xsd.documentwrapperrequest_1.DocumentWrapperRequestType;
import eu.europa.ec.etrustex.dao.exception.MessageRetrieveException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.InputStreamDataSource;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigDecimal;
import java.util.Set;

public class RetrieveDocumentWrapper_2 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory.getLogger(RetrieveDocumentWrapper_2.class);
	
	private static JAXBContext jaxbContext = null;
	private static JAXBContext jaxbContextDWT = null;
	private static String BDL_REQUEST = "SubmitDocumentBundleRequest";
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public TrustExMessage<JAXBElement> processMessage(TrustExMessage<String> message) throws BusinessException {
		TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
		ObjectFactory factory = new ObjectFactory();
		InputStream inputStream = null;
		InputStream decryptingIs = null;
		try {
			Source s= new StreamSource(new StringReader(message.getPayload()));
			
			JAXBElement<DocumentWrapperRequestType> requestElement = getJaxBContext().createUnmarshaller().unmarshal(s,DocumentWrapperRequestType.class);
			DocumentWrapperRequestType request = requestElement.getValue();
			String wrapperSenderIdentifier = request.getSenderParty().getEndpointID().getValue();
			Party wrapperSenderParty = authorisationService.getParty(wrapperSenderIdentifier, message.getHeader().getIssuer().getBusinessDomain());
			
			responseMessage.setHeader(message.getHeader());
			
			Message toReturn = messageService.retrieveMessage(
					message.getHeader().getMessageDocumentId(), 
					message.getHeader().getDocumentTypeCode(), 
					wrapperSenderParty.getId(), 
					null); 
			
			if (toReturn == null){
				responseMessage.setPayload(null);
				return responseMessage;
			}
			
			if (!message.getHeader().getSenderPartyId().equals(wrapperSenderParty.getId())){
				Set<Message> parents = toReturn.getParentMessages();
				Boolean bundleExists = false;
				for (Message parent : parents) {
					String parentReq = parent.getTransaction().getRequestLocalName();
					if (message.getHeader().getSenderPartyId().equals(parent.getReceiver().getId()) && BDL_REQUEST.equalsIgnoreCase(parentReq)){
						bundleExists = true;
					}
				}
                if (!bundleExists) {//TODO Hard Business Rule??
					throw new BusinessException("soapenv:Client","Unauthorized Access",null,ErrorResponseCode.AUTHENTICATION_FAILED,null,"Unauthorized Access");
				}
			}

			String contentType = null;
			byte[] iv = null;

			MessageBinary binary = messageService.getMessageBinary(toReturn.getId(), MessageBinaryType.RAW_MESSAGE.name());				
			Unmarshaller unmarshaller = getJaxBContextDWT().createUnmarshaller();
			JAXBElement<DocumentWrapperType> wrapper = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(binary.getValue().getValue())),DocumentWrapperType.class);
			
			binary = messageService.getMessageBinary(toReturn.getId(), request.getDocumentReferenceRequest().getDocumentTypeCode().getValue());
			contentType = binary.getMimeCode();
			if (binary.getFileId() != null){
				inputStream = new FileInputStream(binary.getFileId()); 
				iv = binary.getIv();
			}
			
			if (wrapper !=null && inputStream!= null){
				decryptingIs = messageService.getDecryptedStream(inputStream, iv);
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
				if (binary != null) {
					if (wrapper.getValue().getResourceInformationReference().getDocumentSize() == null) {
						wrapper.getValue().getResourceInformationReference().setDocumentSize(new DocumentSizeType());
					}
					wrapper.getValue().getResourceInformationReference().getDocumentSize().setValue(BigDecimal.valueOf(binary.getSize()));	
				}					
				wrapper.getValue().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary().setValue(dh);
				wrapper.getValue().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary().setContentType(contentType);	
			}else{
				if(inputStream != null){				
					try{
						inputStream.close();
					}catch(Exception e){}					
				}
				throw new BusinessException("soapenv:Server",ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
			responseMessage.setPayload(wrapper);
			return responseMessage;
		} catch (UndefinedIdentifierException e) {
			responseMessage.setHeader(message.getHeader());
			responseMessage.setPayload(null);
			return responseMessage;
		} catch (FileNotFoundException | JAXBException | MessageRetrieveException e) {
			logger.error(e.getMessage(),e);
			if(inputStream != null){				
				try{
					inputStream.close();
				}catch(Exception ex){}					
			}
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