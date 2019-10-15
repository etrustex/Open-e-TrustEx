package eu.europa.ec.etrustex.integration.gateway;

import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import ec.services.wsdl.document_1.FaultResponse;
import ec.services.wsdl.document_1.SubmitDocumentRequest;
import ec.services.wsdl.document_1.SubmitDocumentResponse;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.AuthenticationFailedException;
import eu.europa.ec.etrustex.dao.exception.EncodedBusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.SOAPService;
import eu.europa.ec.etrustex.services.IAuthenticationService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ResponseCodeType;
import oasis.names.specification.ubl.schema.xsd.fault_1.FaultType;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.transport.jms.JMSConstants;
import org.apache.cxf.transport.jms.JMSMessageHeadersType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.ws.SimpleWebServiceInboundGateway;
import org.springframework.messaging.Message;
import org.springframework.ws.InvalidXmlException;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class SubmitDocumentService extends SimpleWebServiceInboundGateway implements SubmitDocumentPortType, ApplicationContextAware {

    private IAuthenticationService authenticationService;

    private ILogService logService;

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private WebServiceContext wsContext;

    @Autowired
    private Properties soapErrorMessages;

    @Autowired
    private SOAPService soapService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String HEADER_RESPONSE_CODE = "RESPONSE_CODE";
    private static final String IS_FAULT_PROPERTY = "SOAPJMS_isFault";

    private final Logger logger = LoggerFactory.getLogger(SubmitDocumentService.class);




    @Override
    public SubmitDocumentResponse submitDocument(SubmitDocumentRequest submitDocumentRequest, Holder<HeaderType> header) throws FaultResponse {
        LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.PROCESS_MSG, this.getClass().getName())
                .description("Inside SubmitDocumentService")
                .build();
        String authenticationHeader = retrieveAuthenticationHeader();
        try {
            authenticationService.authenticate(authenticationHeader);
        } catch (AuthenticationFailedException | IOException | NoSuchAlgorithmException e) {
            sendResponseCode(e);
        }

        WrappedMessageContext messageContext = (WrappedMessageContext)wsContext.getMessageContext();
        SoapMessage cxfMessage = (SoapMessage) messageContext.getWrappedMessage();
        SOAPMessage soapMessage = cxfMessage.getContent(SOAPMessage.class);

        try {
            soapService.validateSOAPMessage(soapMessage.getSOAPPart().getContent(), submitDocumentRequest.getTransactionNamespace());
        } catch (MessageProcessingException e) {
            addResponseCodeProperty("500");
            throw new FaultResponse(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription(), null);
        } catch (SOAPException e) {
            throw new FaultResponse(null, null);
        }

        //build Spring Integration message

        MessageBuilder<?> builder = MessageBuilder.withPayload(soapMessage);

        builder.setHeader("receivedDate", Calendar.getInstance().getTime());
        builder.setHeader("webserviceRootQName", submitDocumentRequest.getTransactionNamespace());
        try {
            builder.setHeader("username", getUsername(authenticationHeader));
        } catch (IOException e) {
            //should not happen since IOException is caught above
        }
        Message<?> replyMessage = null;
        try {
            replyMessage = this.sendAndReceiveMessage(builder.build());
        } catch (MessageProcessingException e) {
            //sendResponseCode(e);
            FaultType faultType = new FaultType();
            ResponseCodeType responseCodeType = new ResponseCodeType();
            responseCodeType.setValue("error.xxxx");
            faultType.setResponseCode(responseCodeType);
            throw new FaultResponse("ZZZZZZZZZZZZZZZZ", faultType, e);
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
                    MessageProcessingException  xmle = (MessageProcessingException)cause;
                    throw buildFaultResponse(xmle);
                } else {
                    cause = cause.getCause();
                }
            }
        }
        messageContext.put(Header.HEADER_LIST, cxfMessage.getHeaders());
        return buildResponse(replyMessage);
    }

    private String getUsername(String authenticationHeader) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.bouncycastle.util.encoders.Base64.decode(authenticationHeader, baos);
        return StringUtils.split(baos.toString("UTF-8"), ":")[0];
    }

    private SubmitDocumentResponse buildResponse(Message<?> replyMessage) throws FaultResponse {
        if (replyMessage == null) {
            //Runtime exception occurred in the synch. part, send SOAP fault server error
            throw new FaultResponse(ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null);
        }
        TrustExMessage<Document> reply = (TrustExMessage<Document>) replyMessage.getPayload();
        SubmitDocumentResponse response = null;
        Document payload = reply.getPayload();
        try {
            JAXBContext jc = JAXBContext.newInstance(SubmitDocumentResponse.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            response = (SubmitDocumentResponse)unmarshaller.unmarshal(payload);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
            throw new FaultResponse(ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null);
        }
        addResponseCodeProperty("200");
        return response;
    }

    private void addResponseCodeProperty(String responseCodeValue) {
        MessageContext messageContext = wsContext.getMessageContext();
        Map<String, List<String>> responseHeaders = (Map<String, List<String>>)messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
        if (responseHeaders == null) {
            responseHeaders = new HashMap<>();
            messageContext.put(MessageContext.HTTP_RESPONSE_HEADERS, responseHeaders);
        }
        List<String> headerList = new ArrayList<>();
        headerList.add(responseCodeValue);
        responseHeaders.put(HEADER_RESPONSE_CODE, headerList);
    }

    private FaultResponse buildFaultResponse(MessageProcessingException exception) {
        FaultType fault = new FaultType();

        ResponseCodeType responseCodeType = new ResponseCodeType();
        String responseCodeString = (exception.getDocumentTypeCode() != null)
                ? (exception.getDocumentTypeCode() + ":" + exception.getFaultDetailResponseCode().getCode())
                : exception.getFaultDetailResponseCode().getCode();
        responseCodeType.setValue(responseCodeString);

        DescriptionType description = new DescriptionType();
        if (ErrorResponseCode.DOCUMENT_ALREADY_EXISTS.equals(exception.getFaultDetailResponseCode())) {
            description.setValue(soapErrorMessages.getProperty("error.hardrule.docId.duplicate"));
        }
        if (StringUtils.isNotEmpty(exception.getFaultDetailDescription())) {
            description.setValue(exception.getFaultDetailDescription());
        }

        fault.getDescription().add(description);
        fault.setResponseCode(responseCodeType);

        //add JMS property SOAPJMS_isFault with value true as we are replying with a SOAP fault
        Map<String, List<String>> responseHeaders = new HashMap<>();
        List<String> headerList = new ArrayList<>();
        headerList.add("true");
        responseHeaders.put(IS_FAULT_PROPERTY, headerList);
        wsContext.getMessageContext().put(MessageContext.HTTP_RESPONSE_HEADERS, responseHeaders);
        return new FaultResponse(exception.getDescription(), fault, exception);


    }

    private String retrieveAuthenticationHeader() {
        MessageContext messageContext = wsContext.getMessageContext();
        JMSMessageHeadersType headers = (JMSMessageHeadersType) messageContext.get(JMSConstants.JMS_SERVER_REQUEST_HEADERS);
        return (String)headers.getProperty(AUTHORIZATION_HEADER);
    }


    private void sendResponseCode(Exception exception) throws FaultResponse {
        MessageContext messageContext = wsContext.getMessageContext();
        JMSMessageHeadersType headers = (JMSMessageHeadersType) messageContext.get(JMSConstants.JMS_SERVER_REQUEST_HEADERS);

        LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.INFO, LogDTO.LOG_OPERATION.RECEIVE_JMS_MSG,
                this.getClass().getName())
                .description("Inside SubmitDocumentService Message id: " + headers.getJMSMessageID() + " Exception: " + exception.getMessage())
                .build();
        logService.saveLog(logDTO);
        String responseCode = "";
        if (exception instanceof AuthenticationFailedException || exception instanceof IOException) {
            //failed authentication (AuthenticationFailedException) or token is invalid Base64-encoded string (IOException)
            responseCode = ErrorResponseCode.UNAUTHORIZED.getCode();
        } else if(exception instanceof EncodedBusinessException) {
            EncodedBusinessException ebe = (EncodedBusinessException)exception;
            responseCode = ebe.getResponseCode().getCode();
        } else if (exception instanceof NoSuchAlgorithmException) {
            //exception in EncryptionService - this should never happen
            responseCode = "500";
        }

        //add RESPONSE_CODE property
        addResponseCodeProperty(responseCode);

        //add JMS property SOAPJMS_isFault with value true as we are replying with a SOAP fault
        Map<String, List<String>> responseHeaders = (Map<String, List<String>>)messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
        List<String> headerList = new ArrayList<>();
        headerList.add("true");
        responseHeaders.put(IS_FAULT_PROPERTY, headerList);
        throw new FaultResponse(responseCode,null, null);
    }


    public void setAuthenticationService(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void setLogService(ILogService logService) {
        this.logService = logService;
    }

    @PostConstruct
    public void setBeanFactory() {
        setBeanFactory(applicationContext);
    }
}
