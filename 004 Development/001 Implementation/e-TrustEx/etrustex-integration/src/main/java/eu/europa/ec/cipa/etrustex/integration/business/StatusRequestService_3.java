package eu.europa.ec.cipa.etrustex.integration.business;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.DocumentIdentification;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.PartnerIdentification;

import ec.schema.xsd.commoncomponents_3.DocumentReferenceResponseRelatedDocsType;
import ec.schema.xsd.commoncomponents_3.DocumentReferenceResponseType;
import ec.schema.xsd.commoncomponents_3.NoteType;
import ec.schema.xsd.commoncomponents_3.ReferenceIDType;
import ec.schema.xsd.commoncomponents_3.ResponseCodeType;
import ec.schema.xsd.commoncomponents_3.ResponseType;
import ec.schema.xsd.commoncomponents_3.StatusReasonCodeType;
import ec.schema.xsd.commoncomponents_3.StatusType;
import ec.schema.xsd.statusrequest_3.StatusRequestType;
import ec.schema.xsd.statusresponse_3.ObjectFactory;
import ec.schema.xsd.statusresponse_3.StatusResponseType;
import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class StatusRequestService_3 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(StatusRequestService_2.class);

	private static JAXBContext jaxbContext = null;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private static DatatypeFactory dtf = null;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public TrustExMessage<JAXBElement> processMessage(TrustExMessage<String> message) throws MessageProcessingException {
		try {
			TrustExMessage<JAXBElement> responseMessage = new TrustExMessage<JAXBElement>(null);
			responseMessage.setHeader(message.getHeader());
			Source s = new StreamSource(new StringReader(message.getPayload()));
			JAXBElement<StatusRequestType> requestElement = getJaxBContext().createUnmarshaller().unmarshal(s, StatusRequestType.class);
			StatusRequestType request = requestElement.getValue();
			if (request.getSenderParty() == null || request.getSenderParty().getIdentifier().getValue() == null || request.getReceiverParty() == null
					|| request.getReceiverParty().getIdentifier().getValue() == null) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}

			List<MetaDataItem> metadata = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name());
			String schemeIdSeparator = metadata.get(0).getValue();

			// retrieve sender
			String partyIdWithScheme = StringUtils.isNotBlank(request.getSenderParty().getIdentifier().getAuthority()) ? request.getSenderParty()
					.getIdentifier().getAuthority()
					+ schemeIdSeparator + request.getSenderParty().getIdentifier().getValue() : request.getSenderParty().getIdentifier().getValue();
			Party senderParty = authorisationService.getParty(partyIdWithScheme, message.getHeader().getIssuer().getBusinessDomain());

			// retrieve receiver
			partyIdWithScheme = StringUtils.isNotBlank(request.getReceiverParty().getIdentifier().getAuthority()) ? request.getReceiverParty()
					.getIdentifier().getAuthority()
					+ schemeIdSeparator + request.getReceiverParty().getIdentifier().getValue() : request.getReceiverParty().getIdentifier()
					.getValue();
			Party receiverParty = authorisationService.getParty(partyIdWithScheme, message.getHeader().getIssuer().getBusinessDomain());

			if (senderParty == null || receiverParty == null) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
			if (!message.getHeader().getSenderPartyId().equals(senderParty.getId())
					&& !message.getHeader().getSenderPartyId().equals(receiverParty.getId())) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
			String messageDocumentId = request.getDocumentReference().getInstanceIdentifier().trim();
			String documentTypeCode = request.getDocumentReference().getType();
			Set<Transaction> transactions = null;
			if (message.getHeader().getIssuerPartyId().compareTo(message.getHeader().getSenderPartyId()) != 0) {
				transactions = retrieveThirdPartyAuthorisedTransactions(message.getHeader());
			}

			List<Message> queryResult = messageService.retrieveMessages(receiverParty.getId(), senderParty.getId(), null, null, transactions, null,
					messageDocumentId, documentTypeCode, null, false, false, false);
			ObjectFactory fact = new ObjectFactory();
			StatusResponseType response = fact.createStatusResponseType();
			Transaction t = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
			if (queryResult != null && !queryResult.isEmpty()) {
				Message m = queryResult.get(0);
				DocumentReferenceResponseRelatedDocsType docRefResponse = new DocumentReferenceResponseRelatedDocsType();
				docRefResponse.setDocumentReferenceResponse(populateDocumentReferenceResponseType_3(m));
				Set<Message> related = m.getChildMessages();
				for (Message relatedMsg : related) {
					if (!DOC_WRAPPER_DOC_NM.equalsIgnoreCase(relatedMsg.getTransaction().getDocument().getName())) {
						docRefResponse.getRelatedDocument().add(populateDocumentReferenceResponseType_3(relatedMsg));
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

		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
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
	
	protected DocumentIdentification populateDocumentIdentification_3(Message m) {
		DocumentIdentification ref = new DocumentIdentification();
		ref.setInstanceIdentifier(m.getDocumentId());

		try {
			if (m.getIssueDate() != null) {
				XMLGregorianCalendar xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat(DATE_FORMAT).format(m.getIssueDate()));
				ref.setCreationDateAndTime(xgcal);
			}
		} catch (DatatypeConfigurationException e) {
			logger.error("Unable to parse Issue date will be set to null instead", e);
		}
		ref.setType(m.getMessageDocumentTypeCode());
		return ref;
	}
	
	private static DatatypeFactory getDataTypeFactory() throws DatatypeConfigurationException {
		if (dtf == null) {
			dtf = DatatypeFactory.newInstance();
		}
		return dtf;
	}
}
