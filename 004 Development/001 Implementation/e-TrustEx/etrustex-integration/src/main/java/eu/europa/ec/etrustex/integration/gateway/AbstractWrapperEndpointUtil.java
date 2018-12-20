package eu.europa.ec.etrustex.integration.gateway;

import eu.europa.ec.etrustex.dao.exception.AuthorisationFailedException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.integration.util.SOAPService;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import net.sf.saxon.trans.XPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.TransformerObjectSupport;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWrapperEndpointUtil {
	
	public AbstractWrapperEndpointUtil(IAuthorisationService as, SOAPService ss){
		authorisationService = as;
		soapService = ss;
	}
	
	protected SOAPService soapService;
	protected IAuthorisationService authorisationService = null;
	
	protected static final Map<String,JAXBContext> jaxbContextMap = new HashMap<String,JAXBContext>();
	protected static DatatypeFactory dtf = null;
	
	protected final TransformerSupportDelegate transformerSupportDelegate = new TransformerSupportDelegate();
	
	protected static final String STORE_WRP_TX_LN    = "StoreDocumentWrapperRequest";
	protected static final String DELETE_WRP_TX_LN   = "DeleteDocumentWrapperRequestRequest";

	protected static final String STREAM_BASE64_BINARY_LN = "StreamBase64Binary";
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractWrapperEndpointUtil.class);
	
	// StoreWrapper
	abstract Object extractStoreDocumentWrapperRequest(MessageContext context);
	abstract TrustExMessageBinary buildMessageBinary(Object storeDocumentWrapperRequest, TrustExMessageHeader messageHeader, Boolean isMltipart) throws IOException;
	abstract Source createStoreDocumentWrapperResponse(Object header, Object documentWrapper) throws JAXBException;
	abstract void releaseResources(Object storeDocumentWrapperRequest) throws IOException;
	
	// DeleteWrapper
	abstract Object extractDeleteDocumentWrapperRequest(MessageContext context, TrustExMessageHeader messageHeader);
	abstract Source createDeleteDocumentWrapperResponse(Object headerObj, Object documentWrapperObj)  throws JAXBException;
	
	// RetrieveWrapper
	abstract Object extractRetrieveDocumentWrapperRequest(MessageContext context, TrustExMessageHeader messageHeader);
	@SuppressWarnings("rawtypes")
	abstract void buildRetrieveWrapperResponse(Node headerNode, MessageContext context, TrustExMessage retrievedWrapper, TrustExMessageHeader trustExMessageHeader) throws IOException, XPathException, SOAPException;
	
	// Common
	abstract String extractRawHeader(Object headerObj);
	abstract String extractPayload(MessageContext context, String documentElementLocalName, EtrustexNamespaces documentNS)throws SOAPException, TransformerException, IOException;

    abstract void validateEnvelopeSize(Object storeDocumentWrapperRequest);

    abstract TrustExMessageHeader buildMessageHeader(Object headerType, String authenticatedUser, String separator) throws AuthorisationFailedException;
	abstract Object extractHeader(MessageContext messageContext, SaajSoapMessage request, Node headerNode) throws SOAPException, JAXBException;	
	
	abstract SOAPFault createSoapFault(ErrorResponseCode errorResponseCode, String faultDetailDescription);
	
	abstract void buildResponse(Node headerNode, MessageContext context, Source responseSource, TrustExMessageHeader trustExMessageHeader) throws TransformerException, SOAPException, JAXBException;
	abstract Node extractHeaderNode(MessageContext messageContext, SaajSoapMessage request) throws SOAPException;
	
	protected class TransformerSupportDelegate extends TransformerObjectSupport {
		void transformSourceToResult(Source source, Result result)
				throws TransformerException {
			this.transform(source, result);
		}
	}
	
	protected static DatatypeFactory getDataTypeFactory() throws DatatypeConfigurationException {
		if (dtf == null){			
			dtf = DatatypeFactory.newInstance();									
		}
		return dtf;
	}
	
	@SuppressWarnings("rawtypes")
	protected static JAXBContext getJaxBContext(Class param) throws JAXBException{
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
	
	protected static JAXBContext getJaxBContext(String key) throws JAXBException{		
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
}
