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
package eu.europa.ec.etrustex.integration.gateway;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.integration.ejb.AmqpQueueMessageListener;
import eu.europa.ec.etrustex.integration.exception.BadRequestException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.exception.RequestEntityTooLargeException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.etrustex.integration.util.*;
import eu.europa.ec.etrustex.integration.web.AuthenticationFilter;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.dispatcher.AggregateMessageDeliveryException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.ws.SimpleWebServiceInboundGateway;
import org.springframework.messaging.Message;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.server.endpoint.MessageEndpoint;
import org.springframework.ws.server.endpoint.support.PayloadRootUtils;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This is a custom inbound SpingIntegration framework messaging gateway.
 * This class is the entry point for all the open e-prior services.
 * This allows to have a custom handling of the soap faults in case of exceptions.
 * Mainly to have control on the namespaces of the returned saop fault
 *
 * @author orazisa
 *
 */
public class ETrustEXSoapInboundGateway extends SimpleWebServiceInboundGateway implements MessageEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(ETrustEXSoapInboundGateway.class);

	@Autowired
	private ILogService logService;

	@Autowired
	private SOAPService soapService;

	@Autowired
	private IMessageService messageService;


	@Autowired
	@Qualifier("amqpSenderService")
	private IMessageSenderService amqpSenderService;

	@Autowired
	protected Properties soapErrorMessages;


	private boolean readService;

	public static final int MAX_SOAP_ENVELOPE_SIZE = 5*1024*1024;

	public static final int MAX_ATTACHED_DOCUMENT_REQUEST_SIZE = 21*1024*1024;


	@Override
	public void invoke(MessageContext messageContext) {
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.PROCESS_MSG, this.getClass().getName())
				.description("Inside ETrustEXSoapInboundGateway")
				.build();
		try {
			SaajSoapMessage request = (SaajSoapMessage)messageContext.getRequest();

			long startTime = System.currentTimeMillis();
			//check that SOAP envelope is max 5MB
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			try (ByteArrayOutputStream bios = new ByteArrayOutputStream()) {
				StreamResult streamResult = new StreamResult(bios);
				transformer.transform(new DOMSource(request.getSaajMessage().getSOAPPart()), streamResult);
				logger.debug("Transformation took " + (System.currentTimeMillis() - startTime) + "ms for " + bios.size() + " bytes");
				if (bios.size() > MAX_SOAP_ENVELOPE_SIZE) {
					throw new RequestEntityTooLargeException();
				}

				//check that sum of attachments + envelope size is max 21MB
				long attachmentSize = 0;
				Iterator<Attachment> attachmentIterator = request.getAttachments();
				while (attachmentIterator.hasNext()) {
					Attachment attachment = attachmentIterator.next();
					attachmentSize += attachment.getSize();
				}
				if (attachmentSize + bios.size() > MAX_ATTACHED_DOCUMENT_REQUEST_SIZE) {
					throw new RequestEntityTooLargeException();
				}
			}

			//TODO 1-Check the 1_0 Namespace used
			String payloadRootQName = soapService.getPayloadRootQName(request);

			//validate SOAP envelope
			soapService.validateSOAPMessage(request.getEnvelope().getSource(), payloadRootQName);

			//messageContext.getRequest().writeTo(System.out);
			//Remove non-element nodes from SOAP header
			SOAPHeader header = request.getSaajMessage().getSOAPHeader();
			logDTO.setBusinessDomain(soapService.retrieveBusinessDomain(getUsername()));
			NodeList childElements = header.getChildNodes();
			ArrayList<Node> childrenToBeRemoved = new ArrayList<Node>();

			for (int i = 0; i < childElements.getLength(); i++) {
				Node child = childElements.item(i);
				short childType = child.getNodeType();
				if (childType != Node.ELEMENT_NODE) {
					childrenToBeRemoved.add(child);
				}
			}
			for (Node child : childrenToBeRemoved) {
				header.removeChild(child);
			}

			logger.debug("Entering soap inbound gateway with payload : " + payloadRootQName);
			MessageBuilder<?> builder = MessageBuilder.withPayload(request.getSaajMessage());
			//DIGIT-ELNVOICING-1244: Adding message received date
			builder.setHeader("receivedDate", Calendar.getInstance().getTime());
			builder.setHeader("webserviceRootQName", payloadRootQName);
			builder.setHeader("username", getUsername());
			MessageHeaders messageHeaders = AmqpQueueMessageListener.getAmqpHeaders();
			builder.setHeader("messageHeaders", messageHeaders);

			String incomingNameSpace = getPayloadRootNamespaceURI(request);

			Message<?> msg = builder.build();

			Message<?> replyMessage = null;

			try{
				replyMessage= this.sendAndReceiveMessage(msg);
			}catch(AggregateMessageDeliveryException ex){
				logDTO.setDescription(logDTO.getDescription() + " " + ex.getMessage());
				logService.saveLog(logDTO);
				List<?> exceptions =ex.getAggregatedExceptions();
				for (Object object : exceptions) {
					if (((Exception) object).getCause() instanceof MessageProcessingException) {
						MessageProcessingException e = (MessageProcessingException)((Exception) object).getCause() ;
						addFaultToResponse(messageContext, messageHeaders, incomingNameSpace, e);
						return;
					}
				}
			} catch (RequestEntityTooLargeException e) {
				throw e;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				if (!(e instanceof MessageProcessingException)) {
					logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
					logService.saveLog(logDTO);
				}
				Throwable cause = e.getCause();
				// we must go through the stack trace to retrieve the original message processing exception thrown by the platform to be able to create the appropritae soap fault.
				while (cause != null){
					if ( cause instanceof MessageProcessingException){
						MessageProcessingException  xmle= (MessageProcessingException)cause;
						addFaultToResponse(messageContext, messageHeaders, incomingNameSpace, xmle);
						return;
					} else {
						cause =cause.getCause();
					}
				}
				SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
				response.getSoapBody().addServerOrReceiverFault("Server Error", null);
				return;
			}

			TrustExMessage<Document> reply = (TrustExMessage<Document>) replyMessage.getPayload();

			Document payload = reply.getPayload();

			SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
			if (CollectionUtils.isNotEmpty(reply.getBinaries())){
				for (TrustExMessageBinary binary : reply.getBinaries()) {
					InputStream attachmentStream = messageService.getMessageBinaryAsStream(binary.getBinaryId());
					DataHandler dataHandler = new DataHandler(new InputStreamDataSource(attachmentStream, binary.getMimeType()));
					String contentId = (binary.getFileName()== null) ? UUID.randomUUID().toString() : binary.getFileName();
					response.addAttachment(contentId, dataHandler);
				}
			}

			//TODO 2-CHange Namespaces
			response.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("urn1", "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
			response.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec1", "ec:schema:xsd:CommonBasicComponents-0.1");
			response.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("urn", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
			response.getSaajMessage().getSOAPHeader().addNamespaceDeclaration("ec", payload.getDocumentElement().getNamespaceURI());

			soapService.addECHeaderNodeToResponse(request.getSaajMessage().getSOAPHeader().getFirstChild(), response.getSaajMessage().getSOAPHeader(), reply.getHeader());

			response.getSaajMessage().getSOAPBody().addDocument(payload);

			//if communication is done via JMS, send the response to the ReplyTo destination
			if (messageHeaders != null) {
				messageHeaders.setResponseCode("200");
				sendMessage(response, messageHeaders);
			}
		} catch (MessageProcessingException e) {
			logger.error(e.getMessage(), e);
			JmsHeaders jmsHeaders = null;
			try {
				addFaultToResponse(messageContext, jmsHeaders, null, (MessageProcessingException)e);
			} catch (Exception e1) {
				SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
				response.getSoapBody().addServerOrReceiverFault("Server Error", null);
//				throw new RuntimeException(e);
			}
		} catch (BadRequestException bre) {
			throw bre;
		} catch (RequestEntityTooLargeException retle) {
			throw retle;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
			logService.saveLog(logDTO);
			SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();
			response.getSoapBody().addServerOrReceiverFault("Server Error", null);
//			throw new RuntimeException(e);
		}
	}

	//TODO 3-CHANGE the namespaces
	private void addFaultToResponse(MessageContext messageContext, MessageHeaders messageHeaders, String incomingNameSpace,
									MessageProcessingException exception) throws Exception {
		SaajSoapMessage response = (SaajSoapMessage) messageContext.getResponse();

		SoapFault fault;
		if ("soapenv:Client".equalsIgnoreCase(exception.getFault())) {
			fault = response.getSoapBody().addClientOrSenderFault(exception.getDescription(), exception.getLocale());
		} else {
			fault = response.getSoapBody().addServerOrReceiverFault(exception.getDescription(), exception.getLocale());
		}

		if (exception.getFaultDetailResponseCode() != null && StringUtils.isNotEmpty(exception.getFaultDetailResponseCode().getCode())) {
			SoapFaultDetail detail = fault.addFaultDetail();
			detail.addNamespaceDeclaration("cbc", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
			if (incomingNameSpace == null || exception.getFaultDetailResponseCode().equals(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE)) {
				incomingNameSpace = ETrustEXDefaultEndpoint.DEFAULT_SOAP_FAULT_NAMESPACE;
			}
			DOMResult res =(DOMResult)detail.addFaultDetailElement(new QName(incomingNameSpace,"Fault","ec")).getResult();
			SOAPElement responseCode =(SOAPElement) res.getNode().getOwnerDocument().createElementNS(
					"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc:ResponseCode");

			String responseCodeString = null;
			if (exception.getDocumentTypeCode() != null){
				responseCodeString = exception.getDocumentTypeCode() + ":" + exception.getFaultDetailResponseCode().getCode();
			}else{
				responseCodeString = exception.getFaultDetailResponseCode().getCode();
			}
			responseCode.setValue(responseCodeString);
			res.getNode().appendChild(responseCode);

			if (ErrorResponseCode.DOCUMENT_ALREADY_EXISTS.equals(exception.getFaultDetailResponseCode())) {
				SOAPElement description =(SOAPElement) res.getNode().getOwnerDocument().createElementNS(
						"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc:Description");
				description.setValue(soapErrorMessages.getProperty("error.hardrule.docId.duplicate"));
				res.getNode().appendChild(description);
			}
			if (StringUtils.isNotEmpty(exception.getFaultDetailDescription())) {
				SOAPElement description =(SOAPElement) res.getNode().getOwnerDocument().createElementNS(
						"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc:Description");
				description.setValue(exception.getFaultDetailDescription());
				res.getNode().appendChild(description);
			}
		}

		if (messageHeaders != null) {
			if (ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.equals(exception.getFaultDetailResponseCode())) {
				messageHeaders.setResponseCode("500");
			}
			sendMessage(response, messageHeaders);
		}
	}


	private String getPayloadRootNamespaceURI(SaajSoapMessage request) throws Exception {
		String namespaceURI = "";
		NodeList nodes = request.getSaajMessage().getSOAPBody().getElementsByTagNameNS(
				"ec:services:wsdl:Document-1", "transactionNamespace");
		if (nodes == null || nodes.getLength() == 0) {
			//for SOAP over HTTP
			namespaceURI = PayloadRootUtils.getPayloadRootQName(request.getPayloadSource(),
					TransformerFactory.newInstance()).getNamespaceURI();
		} else {
			//for SOAP over JMS
			String transactionNamespace = nodes.item(0).getTextContent() ;
			if (StringUtils.isNotEmpty(transactionNamespace) && validateTransactionNamespace(transactionNamespace)) {
				namespaceURI = transactionNamespace.substring(transactionNamespace.indexOf('{')+1, transactionNamespace.indexOf('}'));
			} else {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(),
						null,ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE, null, null);
			}
		}
		return namespaceURI;

	}

	private boolean validateTransactionNamespace(String transactionNamespace) {
		String regex = "\\{[a-zA-Z_0-9:-]+\\}[a-zA-Z]+";
		Pattern pattern =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(transactionNamespace);
		return matcher.find();
	}

	public void setReadService(boolean readService) {
		this.readService = readService;
	}

	private void sendMessage(SaajSoapMessage soapMessage, MessageHeaders headers) throws Exception {
		if (headers == null) {
			return;
		}
		amqpSenderService.sendMessage(soapMessage, headers);
	}

	private String getUsername() {
		TransportContext context = TransportContextHolder.getTransportContext();

		HttpServletConnection connection = (HttpServletConnection) context
				.getConnection();
		HttpServletRequest req = connection.getHttpServletRequest();
		return req.getUserPrincipal() != null
				? req.getUserPrincipal().getName()
				: (String)req.getAttribute(AuthenticationFilter.AUTHENTICATED_USER_ATTRIBUTE_NAME);

	}
}
