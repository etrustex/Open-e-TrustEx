package eu.europa.ec.etrustex.integration.gateway;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.support.MarshallingUtils;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ec.schema.xsd.ack_2.AcknowledgmentType;
import ec.schema.xsd.commonaggregatecomponents_2.AcknowledgedDocumentReferenceType;
import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import ec.schema.xsd.commonaggregatecomponents_2.ObjectFactory;
import ec.schema.xsd.commonaggregatecomponents_2.TechnicalHeaderType;
import ec.schema.xsd.documentwrapper_1.DocumentWrapperType;
import ec.schema.xsd.documentwrapperrequest_1.DocumentWrapperRequestType;
import ec.services.wsdl.documentwrapper_2.DeleteDocumentWrapperRequestRequest;
import ec.services.wsdl.documentwrapper_2.DeleteDocumentWrapperRequestResponse;
import ec.services.wsdl.documentwrapper_2.RetrieveDocumentWrapperRequestRequest;
import ec.services.wsdl.documentwrapper_2.RetrieveDocumentWrapperRequestResponse;
import ec.services.wsdl.documentwrapper_2.StoreDocumentWrapperRequest;
import ec.services.wsdl.documentwrapper_2.StoreDocumentWrapperResponse;
import eu.europa.ec.etrustex.dao.exception.AuthorisationFailedException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.exception.MissingAttachmentException;
import eu.europa.ec.etrustex.integration.exception.RequestEntityTooLargeException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.integration.util.EtrustexAttachmentUnmarshaller;
import eu.europa.ec.etrustex.integration.util.SOAPService;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import net.sf.saxon.trans.XPathException;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EndpointIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.signatureaggregatecomponents_2.SignatureInformationType;

@Component("wrapperEnpointUtil_2_0")
public class WrapperEnpointUtil_2_0 extends AbstractWrapperEndpointUtil{

	public WrapperEnpointUtil_2_0(IAuthorisationService as1, SOAPService ss) {
		super(as1, ss);
	}

	public Source createStoreDocumentWrapperResponse(Object headerObj, Object documentWrapperObj) throws JAXBException {
		DocumentWrapperType documentWrapper = ((StoreDocumentWrapperRequest)documentWrapperObj).getDocumentWrapper();
		
		StoreDocumentWrapperResponse response = new StoreDocumentWrapperResponse();
		AcknowledgmentType ack = generateAck((HeaderType)headerObj, documentWrapper.getID(), documentWrapper.getDocumentTypeCode());			
		response.setAck(ack);		
		
		return new JAXBSource(getJaxBContext(StoreDocumentWrapperResponse.class.getPackage().getName()), response);		
	}

	public Source createDeleteDocumentWrapperResponse(Object headerObj, Object documentWrapperObj) throws JAXBException {
		DocumentWrapperRequestType documentWrapper = ((DeleteDocumentWrapperRequestRequest)documentWrapperObj).getDocumentWrapperRequest();
		
		DeleteDocumentWrapperRequestResponse response = new DeleteDocumentWrapperRequestResponse();
		AcknowledgmentType ack = generateAck((HeaderType)headerObj, documentWrapper.getDocumentReferenceRequest().getID(), documentWrapper.getDocumentReferenceRequest().getDocumentTypeCode());
		response.setAck(ack);
		
		return new JAXBSource(getJaxBContext(DeleteDocumentWrapperRequestResponse.class.getPackage().getName()), response);
	}
	
	private AcknowledgmentType generateAck(HeaderType header, IDType id, DocumentTypeCodeType dtc){
		header.setTechnicalHeader(new TechnicalHeaderType());
		header.getTechnicalHeader().getSignatureInformation().add(new SignatureInformationType());
		
		AcknowledgmentType ack = new AcknowledgmentType();
		ec.schema.xsd.commonbasiccomponents_1.AckIndicatorType ackIndicatorType = new ec.schema.xsd.commonbasiccomponents_1.AckIndicatorType();
		ackIndicatorType.setValue(true);
		ack.setAckIndicator(ackIndicatorType);
		try {
			ack.setIssueDate(getIssueDateSetToCurrent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ack.setAcknowledgedDocumentReference(new AcknowledgedDocumentReferenceType());
		ack.getAcknowledgedDocumentReference().setSenderParty(convertPartyType(header.getBusinessHeader().getSender().get(0)));
		if (header.getBusinessHeader().getReceiver() != null && header.getBusinessHeader().getReceiver().size() > 0) {
			for (Partner receiver : header.getBusinessHeader().getReceiver()) {
				ack.getAcknowledgedDocumentReference().getReceiverParty().add(convertPartyType(receiver));
			}
		}
		
		DocumentReferenceType documentReference = new DocumentReferenceType();
		documentReference.setID(id);
		documentReference.setDocumentTypeCode(dtc);
		ack.getAcknowledgedDocumentReference().setDocumentReference(documentReference);
		
		return ack;
	}
	
	private static IssueDateType getIssueDateSetToCurrent() throws DatatypeConfigurationException {
		IssueDateType issueDate = new IssueDateType();
		
		GregorianCalendar gcal = new GregorianCalendar();
	    XMLGregorianCalendar xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(gcal.getTime()));
	    issueDate.setValue(xgcal);
	    
		return issueDate;
	}
	
	private static PartyType convertPartyType(Partner partner) {
		PartyType partyType = null;
		
		if (partner == null) {
			return null;
		}
		
		partyType = new PartyType();
		
		EndpointIDType endpointID = new EndpointIDType();
		if (partner.getIdentifier().getAuthority() != null) {
			endpointID.setSchemeID(partner.getIdentifier().getAuthority());
		}
		partyType.setEndpointID(endpointID);
		partyType.getEndpointID().setValue(partner.getIdentifier().getValue());
		
		PartyIdentificationType partyIdentification = new PartyIdentificationType();
		IDType id = new IDType();
		if (partner.getIdentifier().getAuthority() != null) {
			id.setSchemeID(partner.getIdentifier().getAuthority());
		}
		id.setValue(partner.getIdentifier().getValue());		
		partyIdentification.setID(id);		
		partyType.getPartyIdentification().add(partyIdentification);
		
		return partyType;
	}

	public TrustExMessageBinary buildMessageBinary(Object storeDocumentWrapperRequestObj, TrustExMessageHeader messageHeader, Boolean isMltipart) throws IOException{
		StoreDocumentWrapperRequest storeDocumentWrapperRequest = (StoreDocumentWrapperRequest)storeDocumentWrapperRequestObj;
		
		if(storeDocumentWrapperRequest.getDocumentWrapper().getID() == null || 
				storeDocumentWrapperRequest.getDocumentWrapper().getIssueDate() == null ||
				storeDocumentWrapperRequest.getDocumentWrapper().getDocumentTypeCode() == null ||
				storeDocumentWrapperRequest.getDocumentWrapper().getSenderParty() == null ||
				storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference() == null){
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.DOCUMENT_XSD_INVALID.getDescription(), null,
					ErrorResponseCode.DOCUMENT_XSD_INVALID, null, null);
		}
		
		if (storeDocumentWrapperRequest.getDocumentWrapper().getID() != null) {
			messageHeader.setMessageDocumentId(storeDocumentWrapperRequest.getDocumentWrapper().getID().getValue().trim());
		}
		if (storeDocumentWrapperRequest.getDocumentWrapper().getIssueDate() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper().getIssueDate().getValue() != null) {
			Calendar issueDate = storeDocumentWrapperRequest.getDocumentWrapper().getIssueDate().getValue().toGregorianCalendar();
		    //ETRUSTEX-859 issue date should be in the range 1900-2100
		    if(issueDate.get(Calendar.YEAR) < 1900 || issueDate.get(Calendar.YEAR) > 2100) {
		    	throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(),null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
		    }
			messageHeader.setIssueDate(issueDate.getTime());
		} else {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.DOCUMENT_XSD_INVALID.getDescription(),null, ErrorResponseCode.DOCUMENT_XSD_INVALID, null, null);
		}
		
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
		binary.setMimeType(getContentType(storeDocumentWrapperRequest, isMltipart));
		binary.setBinaryContentIS(getDocumentWrapperBinary(storeDocumentWrapperRequest));
		binary.setBinaryType(storeDocumentWrapperRequest.getDocumentWrapper().getDocumentTypeCode().getValue());
		return binary;	
	}


    public TrustExMessageHeader buildMessageHeader(Object headerType, String authenticatedUser, String separator) throws AuthorisationFailedException {
		HeaderType header = (HeaderType)headerType;
		TrustExMessageHeader messageHeader = new TrustExMessageHeader();
		messageHeader.setAuthenticatedUser(authenticatedUser);
        Party issuer;
        try {
            issuer = authorisationService.getMessageIssuer(authenticatedUser);
        } catch (UndefinedIdentifierException e) {
            throw new AuthorisationFailedException("error.unauthorized.credentials.noIssuer");
        }
		messageHeader.setIssuer(issuer);

		List<Partner> senders = header.getBusinessHeader().getSender();
        if (senders == null || senders.isEmpty() || senders.size() > 1 || senders.get(0).getIdentifier() == null) {
            throw new AuthorisationFailedException("error.unauthorized.senderParty.missing");
		}
		//build sender id with scheme
		String schemeId = senders.get(0).getIdentifier().getSchemeID();
        if (StringUtils.isNotBlank(schemeId)) {
			messageHeader.setSenderIdWithScheme(schemeId + separator + senders.get(0).getIdentifier().getValue());
		} else {
			messageHeader.setSenderIdWithScheme(senders.get(0).getIdentifier().getValue());
		}
        Party sender;
        try {
            sender = authorisationService.getParty(messageHeader.getSenderIdWithScheme(), issuer.getBusinessDomain());
        } catch (UndefinedIdentifierException e) {
            throw new AuthorisationFailedException("error.unauthorized.senderParty.unknown");
        }
		messageHeader.setSender(sender);
		messageHeader.setTransactionNamespace(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace());

		return messageHeader;
	}
	
	@SuppressWarnings({"rawtypes" })
	public Object extractStoreDocumentWrapperRequest(MessageContext context) {		
		StoreDocumentWrapperRequest storeDocumentWrapperRequest = null;
		// extract a StoreDocumentWrapperRequest from the SOAP Envelope request
		try {
			SaajSoapMessage request = (SaajSoapMessage) context.getRequest();

			Unmarshaller unmarshaller = getJaxBContext(StoreDocumentWrapperRequest.class.getPackage().getName()).createUnmarshaller();
			unmarshaller.setAttachmentUnmarshaller(new EtrustexAttachmentUnmarshaller(request));
			//unmarshaller.setValidating(false);

			SOAPBody body = request.getSaajMessage().getSOAPBody();

			NodeList nl = body.getElementsByTagNameNS(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace(),STORE_WRP_TX_LN);
			if (nl.getLength() != 1)
				throw new RuntimeException("Invalid ! Multiple StoreDocumentWrapperRequest");

			Node bodyNode = nl.item(0);
			JAXBElement bodyObject = (JAXBElement) unmarshaller.unmarshal(bodyNode, StoreDocumentWrapperRequest.class);
			storeDocumentWrapperRequest = (StoreDocumentWrapperRequest) bodyObject
					.getValue();
		} catch (MissingAttachmentException e){
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.BINARY_ERROR.getDescription(),null, ErrorResponseCode.BINARY_ERROR, null, null);
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
		
		if(storeDocumentWrapperRequest != null && storeDocumentWrapperRequest.getDocumentWrapper() == null){
			storeDocumentWrapperRequest = null;
		}

		return storeDocumentWrapperRequest;
	}
	
	@SuppressWarnings("rawtypes")
	public HeaderType extractHeader(MessageContext messageContext,
			SaajSoapMessage request, Node headerNode) throws JAXBException {
		//TODO Change Namespace
		Unmarshaller unmarshaller = getJaxBContext(HeaderType.class.getPackage().getName()).createUnmarshaller();

		Document doc = headerNode.getOwnerDocument();
		headerNode = doc.renameNode(headerNode, "ec:schema:xsd:CommonAggregateComponents-2", "HeaderType");
		JAXBElement headerObject = (JAXBElement) unmarshaller.unmarshal(
				headerNode, HeaderType.class);
		HeaderType headerType = (HeaderType) headerObject.getValue();
		return headerType;
	}
	
	/**
	 * This is an utility method to create Soap fault in case of error processing
	 * the document wrappers
	 * 
	 * @param errorResponseCode
	 *            an Enum of possible error response codes
	 *	@param faultDetailDescription
	 *            the detailed fault description 
	 * @return the Soap fault returned to the caller
	 * 
	 */
	public SOAPFault createSoapFault(ErrorResponseCode errorResponseCode, String faultDetailDescription) {
		//TODO CHange Namespaces
		try {

			SOAPFactory factory = SOAPFactory.newInstance();
			SOAPFault sf = factory.createFault(errorResponseCode.getDescription(), 
					new QName("http://schemas.xmlsoap.org/soap/envelope/",
					errorResponseCode.getFaultCode()));

            if (ErrorResponseCode.TECHNICAL_ERROR != errorResponseCode) {
				Detail detail = sf.getDetail();

				if (detail == null) {
					detail = sf.addDetail();
                    detail.addNamespaceDeclaration("cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
				}
				String faultNS = errorResponseCode.equals(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE) 
					? ETrustEXDefaultEndpoint.DEFAULT_SOAP_FAULT_NAMESPACE : EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace();
				SOAPElement element = detail.addDetailEntry(new QName(faultNS, "Fault", "ec"));

				String responseCode = ErrorResponseCode.DOCUMENT_ALREADY_EXISTS.equals(errorResponseCode) 
						? "DWR:" + errorResponseCode.getCode() : errorResponseCode.getCode();

				// For backward compatibility with old arch we just return the response code

                element.addChildElement("ResponseCode", "cbc").setValue(responseCode);
                if (StringUtils.isNotBlank(faultDetailDescription)) {
                    element.addChildElement("Description", "cbc").setValue(faultDetailDescription);
				}				
			}
			return sf;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Utility method to retrieve the content type of the binary
	 * 
	 * @param storeDocumentWrapperRequest
	 *            the request
	 * @return the MTOM binary stream
	 * @throws IOException
	 */
	private String getContentType(StoreDocumentWrapperRequest storeDocumentWrapperRequest, Boolean isMultipart) {
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
			if (!isMultipart) {
				//ETRUSTEX-1349 embedded Base64 binary - get the contentType attribute, otherwise contentType of the DataHandler is by default "application/octet-stream"
				String contentTypeAttr = storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary().getContentType();
				contentType = StringUtils.isNotBlank(contentTypeAttr) ? contentTypeAttr : contentType;
			}			
		}

		return contentType;
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
		if (storeDocumentWrapperRequest != null	&& storeDocumentWrapperRequest.getDocumentWrapper() != null) {
			if (storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getLargeAttachment() != null
					&& storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary() != null
					&& storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary().getValue() != null) {
				inputStream = storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference().getLargeAttachment()
						.getStreamBase64Binary().getValue().getInputStream();
			} else {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.BINARY_ERROR.getDescription(), null,
						ErrorResponseCode.BINARY_ERROR, null, null);
			}


		}
		return inputStream;
	}

	public void validateEnvelopeSize(Object storeDocumentWrapperRequestObj){
		StoreDocumentWrapperRequest storeDocumentWrapperRequest = (StoreDocumentWrapperRequest)storeDocumentWrapperRequestObj;
		//check that envelope is max 5MB
		storeDocumentWrapperRequest.getDocumentWrapper().getResourceInformationReference().getLargeAttachment().getStreamBase64Binary().setValue(null);			
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
	}
	
	public String extractRawHeader(Object headerObj){
		String document = null;
		HeaderType header = (HeaderType) headerObj;
		try {
			final StringWriter w = new StringWriter();
			Marshaller mar = getJaxBContext(HeaderType.class.getPackage().getName()).createMarshaller();
			ObjectFactory objectFactory = new ObjectFactory();
			JAXBElement<HeaderType> headerJaxbElement = objectFactory.createHeader(header);
			mar.marshal(headerJaxbElement, w);
			document = w.toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		
		return document;
	}
	
	/**
	 * Utility method called at the end of the wrapper processing to close the
	 * MTOM binary stream.
	 * 
	 * @param storeDocumentWrapperRequestObj
	 */
	public void releaseResources(Object storeDocumentWrapperRequestObj) throws IOException {
		StoreDocumentWrapperRequest storeDocumentWrapperRequest = (StoreDocumentWrapperRequest)storeDocumentWrapperRequestObj; 
		if (storeDocumentWrapperRequest != null
				&& storeDocumentWrapperRequest.getDocumentWrapper() != null
				&& storeDocumentWrapperRequest.getDocumentWrapper()
						.getResourceInformationReference() != null
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
	}
	
	public Node extractHeaderNode(MessageContext messageContext,
			SaajSoapMessage request) throws SOAPException {
		SOAPHeader header = request.getSaajMessage().getSOAPHeader();
		NodeList nl = header.getElementsByTagNameNS(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace(), "Header");

		// SOAP Header invalid
		if (nl == null || nl.getLength() != 1)
			return null;

		Node headerNode = nl.item(0);
		return headerNode;
	}
	
	public void buildResponse(Node headerNode, MessageContext context,
			Source responseSource, TrustExMessageHeader trustExMessageHeader) throws TransformerException, SOAPException {
		WebServiceMessage soapResponse = context.getResponse();
		this.transformerSupportDelegate.transformSourceToResult(responseSource,
				soapResponse.getPayloadResult());

		updateHeader(headerNode, context, trustExMessageHeader);
	}
	
	private void updateHeader(Node headerNode, MessageContext context, TrustExMessageHeader trustExMessageHeader)
            throws SOAPException, XPathException {
		SaajSoapMessage saajResponse = (SaajSoapMessage) context.getResponse();
		SoapHeader sh = saajResponse.getSoapHeader();

		sh.addNamespaceDeclaration("urn1",EtrustexNamespaces.CAC_2_0_NS.getNamespace());
		sh.addNamespaceDeclaration("ec1",EtrustexNamespaces.ECCBC_0_1_NS.getNamespace());
		sh.addNamespaceDeclaration("urn",EtrustexNamespaces.CBC_2_0_NS.getNamespace());
		sh.addNamespaceDeclaration("ec", EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace());

        soapService.addECHeaderNodeToResponse(headerNode, saajResponse.getSaajMessage().getSOAPHeader(), trustExMessageHeader);
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public DeleteDocumentWrapperRequestRequest extractDeleteDocumentWrapperRequest(MessageContext context, TrustExMessageHeader messageHeader) {
		DeleteDocumentWrapperRequestRequest deleteDocumentWrapperRequest = null;
		// extract a DeleteDocumentWrapperRequest from the SOAP Envelope request
		try {
			SaajSoapMessage request = (SaajSoapMessage) context.getRequest();

			Unmarshaller unmarshaller = getJaxBContext(DeleteDocumentWrapperRequestRequest.class.getPackage().getName()).createUnmarshaller();			
						
			SOAPBody body = request.getSaajMessage().getSOAPBody();

			NodeList nl = body.getElementsByTagNameNS(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace(),
					DELETE_WRP_TX_LN);
			if (nl.getLength() != 1)
				throw new RuntimeException(
						"Invalid ! Multiple DeleteDocumentWrapperRequestRequest");

			Node bodyNode = nl.item(0);
			final Iterator attIterator = request.getAttachments();

			JAXBElement bodyObject = (JAXBElement) unmarshaller.unmarshal(
					bodyNode, DeleteDocumentWrapperRequestRequest.class);
			deleteDocumentWrapperRequest = (DeleteDocumentWrapperRequestRequest) bodyObject.getValue();
			
			if(deleteDocumentWrapperRequest != null && deleteDocumentWrapperRequest.getDocumentWrapperRequest() == null){
				deleteDocumentWrapperRequest = null;
			}
			
			if (deleteDocumentWrapperRequest != null && deleteDocumentWrapperRequest.getDocumentWrapperRequest() != null && deleteDocumentWrapperRequest.getDocumentWrapperRequest().getDocumentReferenceRequest() != null &&
					deleteDocumentWrapperRequest.getDocumentWrapperRequest().getDocumentReferenceRequest().getID() != null) {
				messageHeader.setMessageDocumentId(deleteDocumentWrapperRequest.getDocumentWrapperRequest().getDocumentReferenceRequest().getID().getValue().trim());
			}
			
		} catch (Exception e) {
			logger.error(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			deleteDocumentWrapperRequest = null;
		}
		return deleteDocumentWrapperRequest;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	public RetrieveDocumentWrapperRequestRequest extractRetrieveDocumentWrapperRequest(MessageContext context, TrustExMessageHeader messageHeader) {
		RetrieveDocumentWrapperRequestRequest retrieveDocumentWrapperRequest = null;
		try {
			SaajSoapMessage request = (SaajSoapMessage) context.getRequest();

			Unmarshaller unmarshaller = getJaxBContext(RetrieveDocumentWrapperRequestRequest.class.getPackage().getName()).createUnmarshaller();

			SOAPBody body = request.getSaajMessage().getSOAPBody();

			NodeList nl = body.getElementsByTagNameNS(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace(),
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
		
		if(retrieveDocumentWrapperRequest != null && retrieveDocumentWrapperRequest.getDocumentWrapperRequest() == null){
			retrieveDocumentWrapperRequest = null;
		}
		
		if(retrieveDocumentWrapperRequest != null && retrieveDocumentWrapperRequest.getDocumentWrapperRequest() != null 
				&& retrieveDocumentWrapperRequest.getDocumentWrapperRequest().getDocumentReferenceRequest() != null
				&& retrieveDocumentWrapperRequest.getDocumentWrapperRequest().getDocumentReferenceRequest().getID() != null){

			messageHeader.setMessageDocumentId(retrieveDocumentWrapperRequest
					.getDocumentWrapperRequest()
					.getDocumentReferenceRequest().getID().getValue().trim());
			
		}		
		return retrieveDocumentWrapperRequest;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void buildRetrieveWrapperResponse(Node headerNode, MessageContext context, TrustExMessage retrievedWrapperRaw, TrustExMessageHeader messageHeader) throws IOException, XPathException, SOAPException{
		JAXBElement<DocumentWrapperType> retrievedWrapperElement = ((TrustExMessage<JAXBElement<DocumentWrapperType>>)retrievedWrapperRaw).getPayload();
		
		RetrieveDocumentWrapperRequestResponse documentWrapperRequestResponse = new RetrieveDocumentWrapperRequestResponse();
		if (retrievedWrapperElement != null) {
			documentWrapperRequestResponse.setDocumentWrapper(retrievedWrapperElement.getValue());
		}
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(RetrieveDocumentWrapperRequestResponse.class);
		marshaller.setMtomEnabled(true);
		
		WebServiceMessage soapResponse = context.getResponse();
		MarshallingUtils.marshal(marshaller,documentWrapperRequestResponse, soapResponse);
		updateHeader(headerNode, context, messageHeader);	
	}

	String extractPayload(MessageContext messageContext, String documentElementLocalName, EtrustexNamespaces documentNS) throws SOAPException, TransformerException, IOException {
		SaajSoapMessage soapMessage = (SaajSoapMessage)messageContext.getRequest();
		SOAPBody body = soapMessage.getSaajMessage().getSOAPBody();
		NodeList nodeList = body.getElementsByTagNameNS(EtrustexNamespaces.ECCAC_2_0_NS.getNamespace(), STREAM_BASE64_BINARY_LN);
		if (nodeList.getLength() > 0) {
			//remove cid: as it's not a valid Base64-encoded string
			nodeList.item(0).setTextContent("");
		}
		nodeList = body.getElementsByTagNameNS(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace(), documentElementLocalName);
		if (nodeList.getLength() != 1) {
			throw new RuntimeException("Invalid ! Multiple or no documents");
		}
		Node bodyNode = nodeList.item(0);
		DOMSource source = new DOMSource(bodyNode);
		try (StringWriter stringResult = new StringWriter()) {
			TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
			String document = stringResult.toString().replaceAll(EtrustexNamespaces.WSDL_DOC_WRAPPER_2_0.getNamespace(), documentNS.getNamespace());
			return document;
		}
	}
}
