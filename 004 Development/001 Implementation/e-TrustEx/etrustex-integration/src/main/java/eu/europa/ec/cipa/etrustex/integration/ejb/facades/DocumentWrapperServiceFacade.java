package eu.europa.ec.cipa.etrustex.integration.ejb.facades;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.xml.XmlException;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;

import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import ec.schema.xsd.documentwrapper_1.DocumentWrapperType;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.integration.api.IMessageBinaryGateway;
import eu.europa.ec.cipa.etrustex.integration.api.IMessageProcessingGateway;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.exception.ServiceFacadeException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.util.CustomAutowiringInterceptor;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

/**
 * Session Bean implementation class DocumentWrapperServiceFacade
 */
@Stateless(mappedName = "DocumentWrapperServiceFacade")
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors(CustomAutowiringInterceptor.class)
public class DocumentWrapperServiceFacade implements DocumentWrapperServiceFacadeRemote {
	
	@Autowired
	private IMessageProcessingGateway messageProcessingGateway;
	@Autowired
	private IMessageBinaryGateway messageBinaryGateway;
	@Autowired
	private IAuthorisationService authorisationService;
	
    /**
     * Default constructor. 
     */
    public DocumentWrapperServiceFacade() {
        // TODO Auto-generated constructor stub
    }

    private static final Map<String,JAXBContext> jaxbContextMap = new HashMap<String,JAXBContext>();
    
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void storeDocumentWrapper(String user, String password,
			HeaderType header, DocumentWrapperType wrapper, byte[] binaryfile,String binaryMimetype) throws ServiceFacadeException{
		// TODO Add authentication
		Long binaryId = null;
		try {
			TrustExMessage<String> message = new TrustExMessage<String>(null);
			String authenticatedUser = user;
			TrustExMessageHeader messageHeader = new TrustExMessageHeader();
			messageHeader.setAuthenticatedUser(authenticatedUser);
			
			List<Partner> senders = header.getBusinessHeader()
					.getSender();
			if (senders == null || senders.isEmpty() || senders.size() > 1) {
				throw new ServiceFacadeException("You are not authorised to call this service ");
			}
			String senderIdWithScheme = senders.get(0).getIdentifier().getValue();
			messageHeader.setSenderIdWithScheme(senderIdWithScheme);
			messageHeader.setTransactionNamespace("ec:services:wsdl:DocumentWrapper-2");
			messageHeader.setTransactionRequestLocalName("StoreDocumentWrapperRequest");
			String messageDocumentId = wrapper.getID().getValue().trim();
			messageHeader.setMessageDocumentId(messageDocumentId);
			if (wrapper.getIssueDate() !=null){				
				Date d = wrapper.getIssueDate().getValue().toGregorianCalendar().getTime();				
				if (wrapper.getIssueTime() !=null){
					XMLGregorianCalendar t = wrapper.getIssueTime().getValue();
					d.setHours(t.getHour());
					d.setMinutes(t.getMinute());
					d.setSeconds(t.getSecond());
				}				
				messageHeader.setIssueDate(d);
			}
			message.setHeader(messageHeader);
			message.getHeader().setRawHeader(extractRawHeader(header));
			
			TrustExMessageBinary binary = new TrustExMessageBinary();
			binary.setSubmitterIdWithScheme(messageHeader.getSenderIdWithScheme());
			Party issuer = authorisationService.getMessageIssuer(authenticatedUser);
			binary.setIssuerParty(issuer);
			binary.setSenderParty(authorisationService.getParty(senderIdWithScheme, issuer.getBusinessDomain()));
			binary.setMessageDocumentId(messageDocumentId);
			binary.setMimeType(binaryMimetype);
			binary.setBinaryContentIS(new ByteArrayInputStream(binaryfile));
			binary.setBinaryType(wrapper.getDocumentTypeCode().getValue());
			binaryId = messageBinaryGateway.storeMessageBinary(binary);
			binary.setBinaryId(binaryId);
			binary.setBinaryContentIS(null);
			message.addBinary(binary);
	
			JAXBContext jaxbContext = JAXBContext
					.newInstance(DocumentWrapperType.class);
			StringWriter writer = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			JAXBElement<DocumentWrapperType> jaxbElement = new JAXBElement<DocumentWrapperType>(
					new QName("ec:schema:xsd:DocumentWrapper-1",
							"DocumentWrapper", "ec"),
					DocumentWrapperType.class,
					wrapper);
			marshaller.marshal(jaxbElement, writer);
			message.setPayload(writer.toString());
			messageProcessingGateway.processSynchMessage(message);
		} catch (JAXBException e) {
			e.printStackTrace();
			cleanPhantomBinary(binaryId);
			throw new ServiceFacadeException("Undefined incoming message",e);
		} catch (XmlException e) {
			e.printStackTrace();
			cleanPhantomBinary(binaryId);
			throw new ServiceFacadeException("Undefined incoming message",e);
		} catch (MessageProcessingException e) {
			e.printStackTrace();
			cleanPhantomBinary(binaryId);
			throw new ServiceFacadeException(e.getFaultDetailDescription(),e);
		} catch (Exception e) {
			e.printStackTrace();
			cleanPhantomBinary(binaryId);
			throw new ServiceFacadeException(ErrorResponseCode.TECHNICAL_ERROR.getDescription(),e);
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
	
	
	private void cleanPhantomBinary(Long binaryId) throws ServiceFacadeException {
		if (binaryId != null) {
			try {
				messageBinaryGateway.deleteMessageBinary(binaryId);
			} catch (MessageProcessingException e) {
				e.printStackTrace();
				throw new ServiceFacadeException("Technical Error occured trying to parse the error code returned by the platform");
			}
		}
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

	public void setMessageBinaryGateway(IMessageBinaryGateway messageBinaryGateway) {
		this.messageBinaryGateway = messageBinaryGateway;
	}

}
