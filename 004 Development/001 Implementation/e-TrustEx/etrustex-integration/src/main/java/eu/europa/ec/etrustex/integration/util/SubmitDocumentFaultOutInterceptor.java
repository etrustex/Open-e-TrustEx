package eu.europa.ec.etrustex.integration.util;

import eu.europa.ec.etrustex.integration.gateway.SubmitDocumentService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ResponseCodeType;
import oasis.names.specification.ubl.schema.xsd.fault_1.FaultType;
import oasis.names.specification.ubl.schema.xsd.fault_1.ObjectFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitDocumentFaultOutInterceptor extends AbstractSoapInterceptor {

    private static final String UBL_CBC_NS = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";
    private static final String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String RESPONSE_CODE_LOCAL_NAME = "ResponseCode";

    private static final String NOT_WELL_FORMED_XML_MESSAGE = "Problems creating SAAJ object model";
    private static final String UNDEFINED_MESSAGE_FAULT = "was not recognized.  (Does it exist in service WSDL?)";
    private static final String NOT_WELL_FORMED_XML_MESSAGE_2 = "Error reading XMLStreamReader";



    public SubmitDocumentFaultOutInterceptor() {
        super(Phase.MARSHAL);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        // set fault code soap:Client if processing failure is caused by the request content
        Fault fault = (Fault)message.getContent(Exception.class);
        String responseCode = (fault.getDetail() != null
                && fault.getDetail().getElementsByTagNameNS(UBL_CBC_NS, RESPONSE_CODE_LOCAL_NAME) != null
                && fault.getDetail().getElementsByTagNameNS(UBL_CBC_NS, RESPONSE_CODE_LOCAL_NAME).getLength() > 0)
                ? fault.getDetail().getElementsByTagNameNS(UBL_CBC_NS, RESPONSE_CODE_LOCAL_NAME).item(0).getTextContent()
                : null;
        if (responseCode != null && ErrorResponseCode.getByCode(responseCode) != null) {
            fault.setFaultCode(new QName(SOAP_NS, "Client"));
        }

        if (NOT_WELL_FORMED_XML_MESSAGE.equals(fault.getMessage())
                || (fault.getMessage() != null && fault.getMessage().startsWith(NOT_WELL_FORMED_XML_MESSAGE_2))) {
            fault.setMessage(ErrorResponseCode.BAD_REQUEST.getCode());
        }

        if (fault.getMessage() != null
                && (fault.getMessage().contains(UNDEFINED_MESSAGE_FAULT) || fault.getMessage().equals(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription()))) {
            addResponseCodeProperty(message, "500");
            fault.setMessage(ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getDescription());
            fault.setDetail(buildFaultDetail(fault, ErrorResponseCode.UNDEFINED_INCOMING_MESSAGE.getCode()));
        }

        ErrorResponseCode errorResponseCode = ErrorResponseCode.getByCode(fault.getMessage());
        if (errorResponseCode != null) {
            //xml is not well-formed
            fault.setFaultCode(new QName(SOAP_NS, errorResponseCode.getFaultCode()));
            addResponseCodeProperty(message, errorResponseCode.getCode());

        }
    }

    private void addResponseCodeProperty(SoapMessage message, String responseCodeValue) {
        //set the RESPONSE_CODE property on the response message
        Map<String, List<String>> headerMap  = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        if (headerMap == null) {
            headerMap = new HashMap<>();
            message.put(Message.PROTOCOL_HEADERS, headerMap);
        }
        List<String> headerList = new ArrayList<>();
        headerList.add(responseCodeValue);
        headerMap.put(SubmitDocumentService.HEADER_RESPONSE_CODE, headerList);
    }

    private Element buildFaultDetail(Fault fault, String responseCode) {
        Element detailElement = fault.getOrCreateDetail();
        Document document = detailElement.getOwnerDocument();
        Element faultElement = document.createElementNS("urn:oasis:names:specification:ubl:schema:xsd:Fault-1", "Fault");
        Element responseCodeElement = document.createElementNS("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "ResponseCode");
        responseCodeElement.setTextContent(responseCode);
        faultElement.appendChild(responseCodeElement);
        detailElement.appendChild(faultElement);
        return detailElement;
    }
}
