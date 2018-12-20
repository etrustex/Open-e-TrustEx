package eu.europa.ec.etrustex.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.log.Log;
import eu.europa.ec.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.services.IDocumentService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IMessageRoutingService;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.IMetadataService;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

@Component("RedispatchingBean")
public class RedispatchingBean {
	
	public RedispatchingBean() {
		super();
	}

	@Autowired private IMessageRoutingService messageRoutingService;
	@Autowired private IMessageService messageService;
	@Autowired private ILogService logService;
	@Autowired private IDocumentService documentService;
	@Autowired private IMetadataService metadataService;
	@Autowired private JmsTemplate toRoutingQueueTemplate;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> redispatch(Long mRoutingId) throws XPathException, IOException{
		Message message;
        TrustExMessage<String> trustExMessage;
		
		MessageRouting mr = messageRoutingService.findById(mRoutingId);
		message = messageService.retrieveMessageWithInitializedProperties(mr.getMessage().getId(), false);
        
		//Building ETX Message
		Document document = documentService.getDocument(message.getTransaction().getDocument().getId());
		Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long) null, null, null, null, null);
        String separator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
        metadata = metadataService.retrieveMetaData(message.getTransaction(), null);     
        LogDTO logDTO = new LogDTO.LogDTOBuilder(null, LogDTO.LOG_OPERATION.DISPATCHING, null)
                .messageId(message.getId())
                .build();
        Log etrustexLog = logService.findLogsByCriteria(logDTO, null, null, 0, 1).get(0);        
        
        trustExMessage = getTrustExMessage(message, document, separator, etrustexLog, metadata);
        trustExMessage.getHeader().setMessageRoutingId(String.valueOf(mr.getId()));
        toRoutingQueueTemplate.convertAndSend(trustExMessage);
        return trustExMessage;
	}
	
	private TrustExMessage<String> getTrustExMessage(Message message, Document document, String separator, Log etrustexLog, Map<MetaDataItemType, MetaDataItem> metadata) throws XPathException, IOException {
        TrustExMessage<String> trustExMessage = new TrustExMessage<>(null);
        TrustExMessageHeader header = new TrustExMessageHeader();
        
        PartyIdentifier senderIdentifier = message.getSender().getIdentifiers().iterator().next();
        PartyIdentifier receiverIdentifier = message.getReceiver().getIdentifiers().iterator().next();

        header.setMessageId(message.getId());
        header.setAuthenticatedUser(message.getAccessInfo().getCreationId());
        header.setMessageDocumentId(message.getDocumentId());
        header.setCorrelationId(message.getCorrelationId());
        header.setMessageDocumentVersion(document.getVersion());
        header.setSenderIdWithScheme(senderIdentifier.getSchemeId() + separator + senderIdentifier.getValue()); // identifierSchemeId : identifierValue
        header.setReceiverIdWithScheme(receiverIdentifier.getSchemeId() + separator + receiverIdentifier.getValue()); // identifierSchemeId : identifierValue
        header.setTransactionTypeId(message.getTransaction().getId());
        header.setIssuerPartyId(message.getIssuer() != null ? message.getIssuer().getId() : null);
        header.setIssuer(message.getIssuer());
        header.setSenderPartyId(message.getSender().getId());
        header.setReceiverPartyId(message.getReceiver().getId());
        header.setAvailableNotification(null);
        header.setTransactionNamespace(message.getTransaction().getNamespace());
        header.setTransactionRequestLocalName(message.getTransaction().getRequestLocalName());
        header.setInterchangeAgreementId(message.getAgreement().getId());
        header.setLogCorrelationId(etrustexLog.getCorrelationId());

        // Set parent document id and type code
        setParentDocumentInfo(message, header, metadata);
        trustExMessage.setHeader(header);

        return trustExMessage;
    }
    
    private void setParentDocumentInfo(Message message, TrustExMessageHeader header, Map<MetaDataItemType, MetaDataItem> metadata) throws XPathException, IOException {

        // find parent document id
        Configuration config = new Configuration();
        try (InputStream is = messageService.getMessageBinaryAsStream(message.getId(), MessageBinaryType.RAW_MESSAGE)) {
	        DocumentInfo docInfo = config.buildDocument(new StreamSource(is));
	        DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
	        dynamicContext.setContextItem(docInfo);
	
	        final StaticQueryContext sqc = config.newStaticQueryContext();
	
	        MetaDataItem parentIdQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_ID_XQUERY);
	
	        // If parent id metadata is not present, means that there is no pareent document
	        if (parentIdQueryItem != null) {
	            Item it;
	            final XQueryExpression parentIdQuery = sqc.compileQuery(parentIdQueryItem.getValue());
	            it = (Item) parentIdQuery.evaluateSingle(dynamicContext);
	            if (it != null) {
	                header.setMessageParentDocumentId(StringUtils.trimWhitespace(it.getStringValue()));
	            }
	
	            MetaDataItem parentTCQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_TYPECODE_XQUERY);
	            if (parentTCQueryItem != null) {
	                final XQueryExpression parentTCQuery = sqc.compileQuery(parentTCQueryItem.getValue());
	                it = (Item) parentTCQuery.evaluateSingle(dynamicContext);
	                if (it != null) {
	                    header.setMessageParentDocumentTypeCode(StringUtils.trimWhitespace(it.getStringValue()));
	                }
	            }
	        }
        }
    }

}
