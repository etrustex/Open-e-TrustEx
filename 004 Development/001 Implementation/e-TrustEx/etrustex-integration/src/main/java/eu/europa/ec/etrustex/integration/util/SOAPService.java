/**
 *
 */
package eu.europa.ec.etrustex.integration.util;

import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.exception.BadRequestException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.gateway.EtrustexNamespaces;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.integration.web.AuthenticationFilter;
import eu.europa.ec.etrustex.services.IAuthorisationService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chiricr
 *
 */
@Component("soapService")
public class SOAPService {

	@Autowired
	private IAuthorisationService authorisationService;

	@Autowired
	private IMetadataService metadataService;

	@Autowired
	public Configuration freemarkerConfig;

	@Autowired
	private Properties soapErrorMessages;

	private static final String SOAP_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";
	private final static String SOAP_FAULT_TEMPLATE = "templates/soapFaultTemplate.flt";

	public BusinessDomain retrieveBusinessDomain(String username) {
		Party issuer;
		try {
			issuer = authorisationService.getMessageIssuer(username);
		} catch (UndefinedIdentifierException e) {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(),
					null, ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.credentials.noIssuer"));
		} catch (Exception e) {
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(),
					null, ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
		return issuer.getBusinessDomain();
	}

	public BusinessDomain retrieveBusinessDomain() {
		String username = getUserPrincipal();
		if (username == null) {
			return null;
		}
		return retrieveBusinessDomain(username);
	}

	/**
	 * performs the following validations
	 * 1. max 1 element SOAP Header in the XML request
	 * 2. SOAP header is the first child of the SOAP envelope
	 * 3. SOAP body is present
	 * 4. exactly 1 operation inside the SOAP body
	 * 5. operation is a valid transaction
	 * 6. exactly 1 document inside the operation element
	 * @param soapEnvelope
	 * @param webserviceRootQName
	 */
	public void validateSOAPMessage(Source soapEnvelope, String webserviceRootQName) {

		Document domDocument = null;
		try {
			domDocument = XMLUtil.sourceToDom(soapEnvelope);
		} catch (Exception e) {
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(),
					null, ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}

		NodeList nodeList = domDocument.getElementsByTagNameNS(SOAP_ENVELOPE_NS, "Envelope");
		if (nodeList.getLength() != 1) {
			//shouldn't arrive here, Spring WS should have already thrown an exception
			throw new BadRequestException();
		}
		Node envelope = nodeList.item(0);
		removeNonElementNodes(envelope);

		nodeList = domDocument.getElementsByTagNameNS(SOAP_ENVELOPE_NS, "Header");
		if (nodeList.getLength() != 1) {
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.AUTHENTICATION_FAILED.getDescription(),
					null, ErrorResponseCode.AUTHENTICATION_FAILED, null, soapErrorMessages.getProperty("error.unauthorized.SOAPHeaderElement"));
		}

		//check if header is the first child of the SOAP envelope
		Node header = nodeList.item(0);
		if (!envelope.getFirstChild().isSameNode(header)) {
			throw new BadRequestException();
		}

		nodeList = domDocument.getElementsByTagNameNS(SOAP_ENVELOPE_NS, "Body");
		//validate presence of the SOAP body
		if (nodeList == null || nodeList.getLength() != 1) {
			throw new BadRequestException();
		}

		//validate number of operations inside the SOAP body
		Node bodyNode = nodeList.item(0);
		Node operationNode = null;
		int counter = 0;
		for (int i=0; i<bodyNode.getChildNodes().getLength(); i++) {
			if (bodyNode.getChildNodes().item(i).getLocalName() != null) {
				operationNode = bodyNode.getChildNodes().item(i);
				counter++;
			}
			if (counter > 1) {
				//more than 1 operation is not permitted
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(),
						null, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
			}
		}
		if (counter == 0) {
			//no operation is not permitted
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(),
					null, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
		}

		//validate operation
		if (StringUtils.isEmpty(webserviceRootQName)) {
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null,ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE,null,null);
		}
		String transactionLocalName = webserviceRootQName.substring(webserviceRootQName.indexOf('}')+1);
		String transactionNamespace = webserviceRootQName.substring(webserviceRootQName.indexOf('{')+1, webserviceRootQName.indexOf('}'));
		Transaction t = authorisationService.getTransactionByNameSpace(transactionNamespace, transactionLocalName);
		if (t == null){
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null,ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE,null,null);
		}

		//validate number of document elements inside the operation element
		counter = 0;
		for (int i=0; i<operationNode.getChildNodes().getLength(); i++) {
			if (operationNode.getChildNodes().item(i).getLocalName() != null &&
					!"transactionNamespace".equals(operationNode.getChildNodes().item(i).getLocalName())) {
				counter++;
			}
			if (counter > 1) {
				//more than 1 document is not permitted
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(),
						null, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
			}
		}
		if (counter == 0) {
			//no document is not permitted
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(),
					null, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
		}

	}

	private void removeNonElementNodes(Node parent) {
		//sanitize body document
		NodeList childElements = parent.getChildNodes();
		ArrayList<Node> childrenToBeRemoved = new ArrayList<Node>();

		for (int i = 0; i < childElements.getLength(); i++) {
			Node child = childElements.item(i);
			short childType = child.getNodeType();
			if (childType != Node.ELEMENT_NODE) {
				childrenToBeRemoved.add(child);
			}
		}
		for (Node child : childrenToBeRemoved) {
			parent.removeChild(child);
		}
	}

	public String getPayloadRootQName(SaajSoapMessage request) throws Exception {
		String payloadRootQName = null;
		NodeList nodes = request.getSaajMessage().getSOAPBody().getElementsByTagNameNS(
				EtrustexNamespaces.WSDL_DOC_1_0.getNamespace(), "transactionNamespace");
		if (nodes == null || nodes.getLength() == 0) {
			//for SOAP over HTTP
			try {
				payloadRootQName = PayloadRootUtils.getPayloadRootQName(request.getPayloadSource(),
						TransformerFactory.newInstance()).toString();
			} catch (Exception e) {
				return null;
			}
		} else {
			//for SOAP over JMS
			payloadRootQName = nodes.item(0).getTextContent();
			if (StringUtils.isEmpty(payloadRootQName) || !validateTransactionNamespace(payloadRootQName)) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(),
						null,ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
			}
		}
		return payloadRootQName;
	}

	private boolean validateTransactionNamespace(String transactionNamespace) {
		String regex = "\\{[a-zA-Z_0-9:-]+\\}[a-zA-Z]+";
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(transactionNamespace);
		return matcher.find();
	}

	/**
	 * creates a SOAP fault from an ErrorResponseCode
	 * @param errorResponseCode
	 * @return
	 * @throws SOAPException
	 */
	public String createSoapFault(ErrorResponseCode errorResponseCode) throws Exception {
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");

		Map<String, String> root = new HashMap<String, String>();
		Template temp = freemarkerConfig.getTemplate(SOAP_FAULT_TEMPLATE);
		root.put("FAULT_CODE", errorResponseCode.getFaultCode());
		root.put("FAULT_DESCRIPTION", errorResponseCode.getDescription());

		StringWriter sr = new StringWriter();
		temp.process(root, sr);
		return sr.toString();
	}

	/**
	 * this method retrieves the user principal from the {@link TransportContext}
	 * @return authenticated user Principal
	 */
	public String getUserPrincipal() {
		TransportContext context = TransportContextHolder.getTransportContext();
		String username = null;

		if (context.getConnection() instanceof HttpServletConnection) {
			//SOAP over HTTP
			HttpServletConnection connection = (HttpServletConnection) context
					.getConnection();
			HttpServletRequest req = connection.getHttpServletRequest();
			username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName()
					: (String)req.getAttribute(AuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE_NAME);
		}
		return username;
	}

	/*
	 * The system must strip all info except the sender and receiver details when creating the response.
	 * ETRUSTEX-1134. Remove signature from the request header if signature is not required
	 * ETRUSTEX-1178 CREATE_STATUS_AVAILABLE is copied to the response header
	 */
	public void addECHeaderNodeToResponse(Node requestHeaderNode, SOAPHeader responseHeader, TrustExMessageHeader trustExMessageHeader) throws XPathException {
		if (requestHeaderNode.getLocalName().equals("AuthorisationHeader")) {
			// this is a version 0.x service - copy the header as it is
			Node requestHeaderClone = responseHeader.getOwnerDocument().importNode(requestHeaderNode, true);
			responseHeader.appendChild(requestHeaderClone);
			return;
		}
		Map<MetaDataItemType, MetaDataItem> defaultMetadata =  metadataService.retrieveMetaData((Long)null, null, null, null, null); // Default metadata
		net.sf.saxon.Configuration config = new net.sf.saxon.Configuration();
		DocumentInfo docInfo = config.buildDocument(new DOMSource(requestHeaderNode.getOwnerDocument()));
		DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
		dynamicContext.setContextItem(docInfo);

		/*
		 * There is no Metadata record for XPath expressions for Sender and Receiver,
		 * only Sender and Receiver identifiers. So we use a substring of them:
		 * //*:Sender/*:Identifier    -->  //*:Sender/
		 * //*:Receiver/*:Identifier  -->  //*:Receiver/
		 */
		String senderXquery = StringUtils.substringBeforeLast(getMetadataXpathExp(MetaDataItemType.SENDER_ID_XQUERY, trustExMessageHeader), "/*:");

		final StaticQueryContext sqc = config.newStaticQueryContext();
		final XQueryExpression businessHeaderXQueryExpression = sqc.compileQuery(defaultMetadata.get(MetaDataItemType.BUSINESS_HEADER_XPATH).getValue());
		final XQueryExpression senderXQueryExpression = sqc.compileQuery(senderXquery);

		//            NodeInfo securityHeaderInfo = (NodeInfo) securityHeaderXQueryExpression.evaluateSingle(dynamicContext);
		NodeInfo businessHeaderInfo = (NodeInfo) businessHeaderXQueryExpression.evaluateSingle(dynamicContext);
		NodeInfo senderInfo = (NodeInfo) senderXQueryExpression.evaluateSingle(dynamicContext);
		NodeInfo receiverInfo = null;

		if(trustExMessageHeader.getReceiverPartyId() != null) {
			String receiverXquery = StringUtils.substringBeforeLast(getMetadataXpathExp(MetaDataItemType.RECEIVER_ID_XQUERY, trustExMessageHeader), "/*:");
			final XQueryExpression receiverXQueryExpression = sqc.compileQuery(receiverXquery);
			receiverInfo = (NodeInfo) receiverXQueryExpression.evaluateSingle(dynamicContext);
		}

		// BusinessHeader node
		NodeList businessHeaders = requestHeaderNode.getOwnerDocument().getElementsByTagNameNS(businessHeaderInfo.getURI(), businessHeaderInfo.getLocalPart());

		if(businessHeaders != null && businessHeaders.getLength() > 0) {
			Node businessHeader = businessHeaders.item(0);
			Node ecHeader = businessHeader.getParentNode();

			//ETRUSTEX-603 remove xml comments from response header.
			XMLUtil.removeRecursively(ecHeader, Node.COMMENT_NODE);
			stripECHeader(ecHeader, businessHeader);
			stripBusinessHeader(businessHeader, senderInfo, receiverInfo);

			// Append request header to response header
			responseHeader.appendChild(responseHeader.getOwnerDocument().importNode(ecHeader, true));
		}
	}

	/**
	 * retrieve metadata associated with the message transaction, document or one of the profiles linked to the transaction
	 * @param metaDataItemType
	 * @param header
	 * @return
	 */
	private String getMetadataXpathExp(MetaDataItemType metaDataItemType, TrustExMessageHeader header) {
		Transaction t = authorisationService.getTransactionById(header.getTransactionTypeId());
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData(t, null);
		MetaDataItem item = metadata.get(metaDataItemType);
		if (item == null) {
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}

		return item.getValue();
	}

	/*
	 * Remove all child nodes but:
	 * - BusinessHeader
	 */
	private void stripECHeader(Node ecHeader, Node businessHeader) {
		List<Node> childrenToBeRemoved = new ArrayList<>();
		NodeList ecHeaderChildren = ecHeader.getChildNodes();

		for (int i = 0; i < ecHeaderChildren.getLength() ;  i++) {
			Node child = ecHeaderChildren.item(i);
			if(child != null && !child.isEqualNode(businessHeader)) {
				childrenToBeRemoved.add(child);
			}
		}

		for(Node n : childrenToBeRemoved) {
			ecHeader.removeChild(n);
		}
	}

	/*
	 * Remove all child nodes but:
	 * - Sender node
	 * - Receiver node (if present)
	 */
	private void stripBusinessHeader(Node businessHeader, NodeInfo senderInfo, NodeInfo receiverInfo) {
		List<Node> childrenToBeRemoved = new ArrayList<>();
		NodeList businessHeaderChildren = businessHeader.getChildNodes();

		for (int i = 0; i < businessHeaderChildren.getLength() ;  i++) {
			Node child = businessHeaderChildren.item(i);
			if(child != null) {
				String childName = child.getLocalName();
				String childNamespace = child.getNamespaceURI();
				if ( !StringUtils.equals(childNamespace, senderInfo.getURI())
						|| !(StringUtils.equals(childName, senderInfo.getLocalPart())
						|| ( receiverInfo != null  && StringUtils.equals(childName, receiverInfo.getLocalPart())) ) ) {
					childrenToBeRemoved.add(child);
				}
			}
		}

		for(Node n : childrenToBeRemoved) {
			businessHeader.removeChild(n);
		}
	}

	/**
	 *
	 * @return true if the SOAP message was sent in a multipart HTTP request
	 */
	public boolean isMultipartHttpRequest() {
		TransportContext context = TransportContextHolder.getTransportContext();
		boolean isMultipartHttpRequest = false;
		if (context.getConnection() instanceof HttpServletConnection) {
			//SOAP over HTTP
			HttpServletConnection connection = (HttpServletConnection) context
					.getConnection();
			HttpServletRequest req = connection.getHttpServletRequest();
			isMultipartHttpRequest = req.getContentType().contains("multipart");
		}
		return isMultipartHttpRequest;
	}

}
