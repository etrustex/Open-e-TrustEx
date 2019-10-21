package eu.europa.ec.etrustex.integration.util;

import eu.europa.ec.etrustex.integration.gateway.ETrustEXSoapInboundGateway;
import eu.europa.ec.etrustex.integration.gateway.SubmitDocumentService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.jms.JMSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitDocumentInterceptor extends AbstractPhaseInterceptor<Message> {

    private final Logger logger = LoggerFactory.getLogger(SubmitDocumentInterceptor.class);

    public SubmitDocumentInterceptor() {
        super(Phase.UNMARSHAL);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        TextMessage jmsMessage = (TextMessage)message.get(JMSConstants.JMS_REQUEST_MESSAGE);
        try {
            String rawMessage = jmsMessage.getText();
            if (rawMessage != null && rawMessage.length() > ETrustEXSoapInboundGateway.MAX_SOAP_ENVELOPE_SIZE) {
                //set the RESPONSE_CODE property on the response message
                Map<String, List<String>> headerMap  = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
                if (headerMap == null) {
                    headerMap = new HashMap<>();
                    message.put(Message.PROTOCOL_HEADERS, headerMap);
                }
                List<String> headerList = new ArrayList<>();
                headerList.add(ErrorResponseCode.REQUEST_ENTITY_TOO_LARGE.getCode());
                headerMap.put(SubmitDocumentService.HEADER_RESPONSE_CODE, headerList);
                throw new Fault(new RuntimeException(ErrorResponseCode.REQUEST_ENTITY_TOO_LARGE.getCode()));
            }
        } catch (JMSException e) {
            logger.error("Error while retrieving the JMS request message " + e.getMessage());
        }
    }
}
