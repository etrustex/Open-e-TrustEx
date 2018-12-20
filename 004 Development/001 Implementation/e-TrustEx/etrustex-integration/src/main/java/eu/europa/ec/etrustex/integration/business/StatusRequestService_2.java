package eu.europa.ec.etrustex.integration.business;

import ec.schema.xsd.commonaggregatecomponents_2.DocumentReferenceResponseRelatedDocsType;
import ec.schema.xsd.statusrequest_2.StatusRequestType;
import ec.schema.xsd.statusresponse_2.ObjectFactory;
import ec.schema.xsd.statusresponse_2.StatusResponseType;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NoteType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

public class StatusRequestService_2 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(StatusRequestService_2.class);

	private static JAXBContext jaxbContext = null;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public TrustExMessage<JAXBElement<StatusResponseType>> processMessage(TrustExMessage<String> message) throws MessageProcessingException {

        TrustExMessage<JAXBElement<StatusResponseType>> responseMessage = new TrustExMessage<JAXBElement<StatusResponseType>>(null);
        responseMessage.setHeader(message.getHeader());
        Source s = new StreamSource(new StringReader(message.getPayload()));
        JAXBElement<StatusRequestType> requestElement = null;

        try {
            requestElement = getJaxBContext().createUnmarshaller().unmarshal(s, StatusRequestType.class);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
            throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
                    ErrorResponseCode.TECHNICAL_ERROR, null, null);
        }

        StatusRequestType request = requestElement.getValue();
        if (request.getSenderParty() == null || request.getSenderParty().getEndpointID() == null
                || StringUtils.isBlank(request.getSenderParty().getEndpointID().getValue())) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.sender.missing"));
        }
        if (request.getReceiverParty() == null || request.getReceiverParty().getEndpointID() == null
                || StringUtils.isBlank(request.getReceiverParty().getEndpointID().getValue())) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.receiver.missing"));
        }

        List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
        String schemeIdSeparator = metadata.get(0).getValue();

        // retrieve sender
        String partyIdWithScheme = StringUtils.isNotBlank(request.getSenderParty().getEndpointID().getSchemeID())
                ? request.getSenderParty().getEndpointID().getSchemeID() + schemeIdSeparator + request.getSenderParty().getEndpointID().getValue()
                : request.getSenderParty().getEndpointID().getValue();
        Party senderParty = null;
        try {
            senderParty = authorisationService.getParty(partyIdWithScheme, message.getHeader().getIssuer().getBusinessDomain());
        } catch (UndefinedIdentifierException e) {
            logger.error(e.getMessage(), e);
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.sender.notFound"));
        }

        // retrieve receiver
        partyIdWithScheme = StringUtils.isNotBlank(request.getReceiverParty().getEndpointID().getSchemeID())
                ? request.getReceiverParty().getEndpointID().getSchemeID() + schemeIdSeparator + request.getReceiverParty().getEndpointID().getValue()
                : request.getReceiverParty().getEndpointID().getValue();
        Party receiverParty = null;
        try {
            receiverParty = authorisationService.getParty(partyIdWithScheme, message.getHeader().getIssuer().getBusinessDomain());
        } catch (UndefinedIdentifierException e) {
            logger.error(e.getMessage(), e);
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.receiver.notFound"));
        }

        if (!message.getHeader().getSenderPartyId().equals(senderParty.getId())
                && !message.getHeader().getSenderPartyId().equals(receiverParty.getId())) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.businessHdr.sender"));
        }
        String messageDocumentId = request.getDocumentReferenceRequest().getID().getValue().trim();
        if (StringUtils.isBlank(messageDocumentId)) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docId"));
        }
        String documentTypeCode = request.getDocumentReferenceRequest().getDocumentTypeCode().getValue();
        if (StringUtils.isBlank(documentTypeCode)) {
            throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
                    ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docTypeCode"));
        }
        Set<Transaction> transactions = null;
        if (message.getHeader().getIssuerPartyId().compareTo(message.getHeader().getSenderPartyId()) != 0) {
            transactions = retrieveThirdPartyAuthorisedTransactions(message.getHeader());
        }

        MessageQueryDTO mq = new MessageQueryDTO();
        mq.setReceiverPartyId(receiverParty.getId());
        mq.setSenderPartyId(senderParty.getId());
        mq.setTransactions(transactions);
        mq.setMessageDocumentId(messageDocumentId);
        mq.setDocumentTypeCode(documentTypeCode);
        mq.setFilterOutMessagesInError(Boolean.FALSE);
        List<Message> queryResult = messageService.retrieveMessages(mq);
        ObjectFactory fact = new ObjectFactory();
        StatusResponseType response = fact.createStatusResponseType();
        Transaction t = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
        if (queryResult != null && !queryResult.isEmpty()) {
            Message m = queryResult.get(0);
            DocumentReferenceResponseRelatedDocsType docRefResponse = new DocumentReferenceResponseRelatedDocsType();
            docRefResponse.setDocumentReferenceResponse(populateDocumentReferenceResponseType(m));
            Set<Message> related = m.getChildMessages();
            for (Message relatedMsg : related) {
                if (!DOC_WRAPPER_DOC_NM.equalsIgnoreCase(relatedMsg.getTransaction().getDocument().getName())) {
                    docRefResponse.getRelatedDocument().add(populateDocumentReferenceResponseType(relatedMsg));
                }
            }
            response.getDocumentReferenceResponseRelatedDocs().add(docRefResponse);
        } else {
            NoteType note = new NoteType();
            note.setValue("No matches were found");
            response.getNote().add(note);
        }
        String responseElementLocalName = fact.createStatusResponse(response).getName().getLocalPart();

        JAXBElement<StatusResponseType> responseElement = new JAXBElement<StatusResponseType>(new QName(t.getNamespace(),
                responseElementLocalName, "ec"), StatusResponseType.class, response);

        responseMessage.setPayload(responseElement);
        return responseMessage;
	}

	private static JAXBContext getJaxBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(StatusRequestType.class);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}

}
