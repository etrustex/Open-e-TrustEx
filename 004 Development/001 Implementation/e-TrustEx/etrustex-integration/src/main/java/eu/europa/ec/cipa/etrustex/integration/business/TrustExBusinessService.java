package eu.europa.ec.cipa.etrustex.integration.business;

import ec.schema.xsd.commonaggregatecomponents_2.*;
import ec.schema.xsd.commonaggregatecomponents_2.CertificateType;
import ec.schema.xsd.commonaggregatecomponents_2.TitleType;
import ec.schema.xsd.commonbasiccomponents_1.*;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.integration.TrustExIntegrationSupport;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.task.ISlaPolicyService;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.IStateMachineService;
import eu.europa.ec.cipa.etrustex.types.DataExtractionTypes;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;
import net.sf.saxon.trans.XPathException;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.DocumentIdentification;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;
import org.unece.cefact.namespaces.standardbusinessdocumentheader.PartnerIdentification;

import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.BinaryObjectType;
import un.unece.uncefact.data.specification.unqualifieddatatypesschemamodule._2.TextType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class TrustExBusinessService extends TrustExIntegrationSupport {

	@Autowired
	protected IAuthorisationService authorisationService;
	@Autowired
	protected IStateMachineService stateMachineService;
	@Autowired
	protected ISlaPolicyService slaPolicyService;

	// protected final Configuration config = new Configuration();
	// protected final DynamicQueryContext dynamicContext = new
	// DynamicQueryContext(config);

	private static final Logger logger = LoggerFactory.getLogger(TrustExBusinessService.class);
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm:ss";

	public final static String DOC_WRAPPER_DOC_NM = "DocumentWrapper";

	public final static String APP_RESPONSE_TYPE_CD = "301";

	protected static final String ID_SEPARATOR = "::";

	public static final String USAGE_KEY_ENCIPHERMENT = "KEY_ENCIPHERMENT";

	private static DatatypeFactory dtf = null;

	public IAuthorisationService getAuthorisationService() {
		return authorisationService;
	}

	public void setAuthorisationService(IAuthorisationService authorisationService) {
		this.authorisationService = authorisationService;
	}

	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public IMetadataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public IStateMachineService getStateMachineService() {
		return stateMachineService;
	}

	public void setStateMachineService(IStateMachineService stateMachineService) {
		this.stateMachineService = stateMachineService;
	}

	protected DocumentReferenceType populateDocumentReferenceType(Message m) {
		DocumentReferenceType ref = new DocumentReferenceType();
		IDType id = new IDType();
		id.setValue(m.getDocumentId());
		ref.setID(id);
		try {
			if (m.getIssueDate() != null) {
				IssueDateType issueDate = new IssueDateType();
				XMLGregorianCalendar xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat(DATE_FORMAT).format(m.getIssueDate()));
				issueDate.setValue(xgcal);
				ref.setIssueDate(issueDate);
			}
		} catch (DatatypeConfigurationException e) {
			logger.error("Unable to parse Issue date will be set to null instead", e);
		}
		DocumentTypeCodeType docType = new DocumentTypeCodeType();
		docType.setValue(m.getMessageDocumentTypeCode());
		ref.setDocumentTypeCode(docType);
		return ref;
	}

	protected Set<Transaction> retrieveThirdPartyAuthorisedTransactions(TrustExMessageHeader header) {
		Party authorizingParty = new Party();
		authorizingParty.setId(header.getSenderPartyId());
		Party delegateParty = new Party();
		delegateParty.setId(header.getIssuerPartyId());

		return authorisationService.retrievePartyAgreement(authorizingParty, delegateParty).getTransactions();
	}

	protected DocumentReferenceResponseType populateDocumentReferenceResponseType(Message m) {
		DocumentReferenceResponseType docResRespType = new DocumentReferenceResponseType();
		String documentTypeCd = m.getTransaction().getDocument().getDocumentTypeCode();
		Map<String, MessageResponseCode> respcodes = metadataService.retrieveMessageResponseCodes((m.getAgreement() == null) ? null : m.getAgreement().getId(), m.getTransaction().getId(), m
				.getTransaction().getDocument().getId(), null);
		docResRespType.setSenderParty(populateParty(m.getSender().getIdentifiers()));
		if (m.getReceiver() != null) {
			docResRespType.getReceiverParty().add(populateParty(m.getReceiver().getIdentifiers()));
		}
		Set<Message> parents = m.getParentMessages();
		// if it's an application response we need to go check the parent and
		// the update that was done to oi
		if (APP_RESPONSE_TYPE_CD.equalsIgnoreCase(documentTypeCd) && parents != null && parents.size() > 0) {
			// app responses must have one and only one parent
			if (parents.size() > 1) {
				throw new RuntimeException("Found application response with more than one parent, database is corrupted");
			}
			Message parent = parents.iterator().next();
			Map<String, MessageResponseCode> parentrespcodes = metadataService.retrieveMessageResponseCodes((parent.getAgreement() == null) ? null : parent.getAgreement().getId(), parent
					.getTransaction().getId(), parent.getTransaction().getDocument().getId(), null);

			ResponseType response = new ResponseType();
			ReferenceIDType refId = new ReferenceIDType();
			response.setReferenceID(refId);
			ResponseCodeType responseCode = new ResponseCodeType();
			if (m.getResponseCode() != null)
				responseCode.setValue(m.getResponseCode());
			response.setResponseCode(responseCode);
			docResRespType.setResponse(response);
		}

		StatusType status = new StatusType();
		StatusReasonCodeType reasonCode = new StatusReasonCodeType();
		reasonCode.setValue(respcodes.get(m.getStatusCode()).getStatusCode());
		status.setStatusReasonCode(reasonCode);
		docResRespType.setStatus(status);
		docResRespType.setDocumentReference(populateDocumentReferenceType(m));
		return docResRespType;
	}
	
	protected ec.schema.xsd.commoncomponents_3.DocumentReferenceResponseType populateDocumentReferenceResponseType_3(Message m) {
		ec.schema.xsd.commoncomponents_3.DocumentReferenceResponseType docResRespType = new ec.schema.xsd.commoncomponents_3.DocumentReferenceResponseType();
		String documentTypeCd = m.getTransaction().getDocument().getDocumentTypeCode();
		Map<String, MessageResponseCode> respcodes = metadataService.retrieveMessageResponseCodes((m.getAgreement() == null) ? null : m.getAgreement().getId(), m.getTransaction().getId(), m
				.getTransaction().getDocument().getId(), null);
		docResRespType.setSenderParty(populateParty_3(m.getSender().getIdentifiers()));
		if (m.getReceiver() != null) {
			docResRespType.getReceiverParty().add(populateParty_3(m.getReceiver().getIdentifiers()));
		}
		Set<Message> parents = m.getParentMessages();
		// if it's an application response we need to go check the parent and
		// the update that was done to oi
		if (APP_RESPONSE_TYPE_CD.equalsIgnoreCase(documentTypeCd) && parents != null && parents.size() > 0) {
			// app responses must have one and only one parent
			if (parents.size() > 1) {
				throw new RuntimeException("Found application response with more than one parent, database is corrupted");
			}
			Message parent = parents.iterator().next();
			Map<String, MessageResponseCode> parentrespcodes = metadataService.retrieveMessageResponseCodes((parent.getAgreement() == null) ? null : parent.getAgreement().getId(), parent
					.getTransaction().getId(), parent.getTransaction().getDocument().getId(), null);

			ec.schema.xsd.commoncomponents_3.ResponseType response = new ec.schema.xsd.commoncomponents_3.ResponseType();
			ec.schema.xsd.commoncomponents_3.ReferenceIDType refId = new ec.schema.xsd.commoncomponents_3.ReferenceIDType();
			response.setReferenceID(refId);
			ec.schema.xsd.commoncomponents_3.ResponseCodeType responseCode = new ec.schema.xsd.commoncomponents_3.ResponseCodeType();
			if (m.getResponseCode() != null)
				responseCode.setValue(m.getResponseCode());
			response.setResponseCode(responseCode);
			docResRespType.setResponse(response);
		}

		ec.schema.xsd.commoncomponents_3.StatusType status = new ec.schema.xsd.commoncomponents_3.StatusType();
		ec.schema.xsd.commoncomponents_3.StatusReasonCodeType reasonCode = new ec.schema.xsd.commoncomponents_3.StatusReasonCodeType();
		reasonCode.setValue(respcodes.get(m.getStatusCode()).getStatusCode());
		status.setStatusReasonCode(reasonCode);
		docResRespType.setStatus(status);
		docResRespType.setDocumentReference(populateDocumentIdentification_3(m));
		return docResRespType;
	}

	protected ExtendedDocumentReferenceResponseType populateExtendedDocumentReferenceResponseType(Message m) {
		ExtendedDocumentReferenceResponseType docResRespType = new ExtendedDocumentReferenceResponseType();
		String documentTypeCd = m.getTransaction().getDocument().getDocumentTypeCode();
		Map<String, MessageResponseCode> respcodes = metadataService.retrieveMessageResponseCodes((m.getAgreement() == null) ? null : m.getAgreement().getId(), m.getTransaction().getId(), m
				.getTransaction().getDocument().getId(), null);
		docResRespType.setSenderParty(populateParty(m.getSender().getIdentifiers()));
		if (m.getReceiver() != null) {
			docResRespType.getReceiverParty().add(populateParty(m.getReceiver().getIdentifiers()));
		}
		Set<Message> parents = m.getParentMessages();
		// if it's an application response we need to go check the parent and
		// the update that was done to oi
		if (APP_RESPONSE_TYPE_CD.equalsIgnoreCase(documentTypeCd) && parents != null && parents.size() > 0) {
			// app responses must have one and only one parent
			if (parents.size() > 1) {
				throw new RuntimeException("Found application response with more than one parent, database is corrupted");
			}
			Message parent = parents.iterator().next();
			Map<String, MessageResponseCode> parentrespcodes = metadataService.retrieveMessageResponseCodes((parent.getAgreement() == null) ? null : parent.getAgreement().getId(), parent
					.getTransaction().getId(), parent.getTransaction().getDocument().getId(), null);

			ResponseType response = new ResponseType();
			ReferenceIDType refId = new ReferenceIDType();
			response.setReferenceID(refId);
			ResponseCodeType responseCode = new ResponseCodeType();
			responseCode.setValue(parent.getTransaction().getDocument().getDocumentTypeCode() + ":" + parentrespcodes.get(parent.getStatusCode()).getResponseCodeValue());
			response.setResponseCode(responseCode);
			docResRespType.setResponse(response);
		}

		RetrievedDocumentsIndicatorType retrievedIndicator = new RetrievedDocumentsIndicatorType();
		retrievedIndicator.setValue(m.getRetrieveIndicator());
		docResRespType.setRetrievedDocumentsIndicator(retrievedIndicator);

		if (m.getStatusCode() != null && respcodes.get(m.getStatusCode()) != null) {
			StatusType status = new StatusType();
			StatusReasonCodeType reasonCode = new StatusReasonCodeType();
			reasonCode.setValue(respcodes.get(m.getStatusCode()).getStatusCode());
			status.setStatusReasonCode(reasonCode);
			docResRespType.setStatus(status);
		}

		docResRespType.setDocumentReference(populateDocumentReferenceType(m));

		try {
			if (m.getIssueDate() != null) {
				IssueDateType issueDate = new IssueDateType();
				XMLGregorianCalendar xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat(DATE_FORMAT).format(m.getIssueDate()));
				issueDate.setValue(xgcal);
				docResRespType.setIssueDate(issueDate);

				IssueTimeType issueTime = new IssueTimeType();
				xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat(TIME_FORMAT).format(m.getIssueDate()));
				issueTime.setValue(xgcal);
				docResRespType.setIssueTime(issueTime);
			}
		} catch (DatatypeConfigurationException e) {
			logger.error("Unable to parse Issue date will be set to null instead", e);
		}

		String title = m.getAdditionalInfoAsMap().get(DataExtractionTypes.JMS_TITLE.name());
		if (title != null) {
			TitleType tType = new TitleType();
			tType.setValue(title);
			docResRespType.setTitle(tType);
		}

		String bdt = m.getAdditionalInfoAsMap().get(DataExtractionTypes.JMS_BUSINESS_DOC_TYPE.name());
		if (bdt != null) {
			BusinessDocumentTypeType bdtt = new BusinessDocumentTypeType();
			bdtt.setValue(bdt);
			docResRespType.setBusinessDocumentType(bdtt);
		}

		IDType id = new IDType();
		id.setValue(m.getCorrelationId());
		DocumentReferenceType ref = new DocumentReferenceType();
		ref.setID(id);
		docResRespType.setOriginatorDocumentReference(ref);

		return docResRespType;
	}

	protected PartyType populateParty(Set<PartyIdentifier> identifiers) {
		PartyType partyType = new PartyType();
		boolean endpointIdSet = false;
		for (PartyIdentifier partyIdentifier : identifiers) {
			PartyIdentificationType partyIdType = new PartyIdentificationType();
			IDType id = new IDType();
			id.setSchemeID(partyIdentifier.getSchemeId().getSchemeID());
			id.setValue(partyIdentifier.getValue());
			partyIdType.setID(id);
			partyType.getPartyIdentification().add(partyIdType);
			//ETRUSTEX-471 set EndpointID even when GLN identifier is missing
			if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(partyIdentifier.getSchemeId().getISO6523Code()) || !endpointIdSet) {
				EndpointIDType endpoint = new EndpointIDType();
				endpoint.setValue(partyIdentifier.getValue());
				endpoint.setSchemeID(partyIdentifier.getSchemeId().getSchemeID());
				partyType.setEndpointID(endpoint);
				endpointIdSet = true;
			}
		}
		return partyType;
	}
	
	protected Partner populateParty_3(Set<PartyIdentifier> identifiers) {
		Partner partner = new Partner();
		boolean endpointIdSet = false;
		for (PartyIdentifier partyIdentifier : identifiers) {
			PartnerIdentification partyIdType = new PartnerIdentification();
			partyIdType.setAuthority(partyIdentifier.getSchemeId().getSchemeID());
			partyIdType.setValue(partyIdentifier.getValue());
			partner.setIdentifier(partyIdType);
			//ETRUSTEX-471 set EndpointID even when GLN identifier is missing
			if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(partyIdentifier.getSchemeId().getISO6523Code()) || !endpointIdSet) {
				PartnerIdentification partyIdType2 = new PartnerIdentification();
				partyIdType2.setValue(partyIdentifier.getValue());
				partyIdType2.setAuthority(partyIdentifier.getSchemeId().getSchemeID());
				partner.setIdentifier(partyIdType2);
				endpointIdSet = true;
			}
		}
		return partner;
	}

	protected InterchangeAgreementType populateInterchangeAgreementType(InterchangeAgreement interchangeAgreement, Party sender, Party receiver, CIALevel cia) {
		logger.trace("creating InterchangeAgreementType");
		InterchangeAgreementType iat = new InterchangeAgreementType();
		iat.setSenderParty(populateParty(sender.getIdentifiers()));
		iat.setReceiverParty(populateParty(receiver.getIdentifiers()));
		iat.setDocumentTypeCode(new DocumentTypeCodeType());
		logger.trace("added empty DocumentTypeCodeType");
		iat.setSecurityInformation(populateSecurityInformation(sender, receiver, cia));
		iat.setProfileID(populateProfileId(interchangeAgreement.getProfile()));
		logger.debug("created and populated InterchangeAgreementType");
		return iat;
	}
	
	protected ec.schema.xsd.commoncomponents_3.InterchangeAgreementType populateInterchangeAgreementType_3(InterchangeAgreement interchangeAgreement, Party sender, Party receiver, CIALevel cia) {
		logger.trace("creating InterchangeAgreementType");
		ec.schema.xsd.commoncomponents_3.InterchangeAgreementType iat = new ec.schema.xsd.commoncomponents_3.InterchangeAgreementType();
		iat.setSenderParty(populateParty_3(sender.getIdentifiers()));
		iat.setReceiverParty(populateParty_3(receiver.getIdentifiers()));
		iat.setDocumentTypeCode(new ec.schema.xsd.commoncomponents_3.DocumentTypeCodeType());
		logger.trace("added empty DocumentTypeCodeType");
		iat.setSecurityInformation(populateSecurityInformation_3(sender, receiver, cia));
		iat.setProfileID(populateProfileId_3(interchangeAgreement.getProfile()));
		logger.debug("created and populated InterchangeAgreementType");
		return iat;
	}

	protected ProfileIDType populateProfileId(Profile profile) {
		logger.trace("creating ProfileIDType");
		ProfileIDType pit = new ProfileIDType();
		pit.setValue(profile.getName());
		logger.trace("created and populated ProfileIDType [{}]", pit.getValue());
		return pit;
	}
	
	protected ec.schema.xsd.commoncomponents_3.ProfileIDType populateProfileId_3(Profile profile) {
		logger.trace("creating ProfileIDType");
		ec.schema.xsd.commoncomponents_3.ProfileIDType pit = new ec.schema.xsd.commoncomponents_3.ProfileIDType();
		pit.setValue(profile.getName());
		logger.trace("created and populated ProfileIDType [{}]", pit.getValue());
		return pit;
	}
	
	protected SecurityInformationType populateSecurityInformation(Party sender, Party receiver, CIALevel ciaLevel) {
		logger.trace("determining if SecurityInformationType is needed");
		
		SecurityInformationType sit = null;
		if (ciaLevel != null) {
			sit = new SecurityInformationType();

			AvailabilityLevelCodeType a = new AvailabilityLevelCodeType();
			a.setValue(ciaLevel.getAvailabilityLevel().toString());
			sit.setAvailabilityLevelCode(a);
			logger.trace("added and populated AvailabilityLevelCodeType [{}]", a.getValue());

			ConfidentialityLevelCodeType c = new ConfidentialityLevelCodeType();
			c.setValue(ciaLevel.getConfidentialityLevel().toString());
			sit.setConfidentialityLevelCode(c);
			logger.trace("added and populated ConfidentialityLevelCodeType [{}]", c.getValue());

			IntegrityLevelCodeType i = new IntegrityLevelCodeType();
			i.setValue(ciaLevel.getIntegrityLevel().toString());
			sit.setIntegrityLevelCode(i);
			logger.trace("added and populated IntegrityLevelCodeType [{}]", i.getValue());			
		}
		if (CollectionUtils.isNotEmpty(sender.getCertificates())) {
			if (sit == null) {
				sit = new SecurityInformationType();				
			}			
			populateThisCertificate(sit.getSenderPartyCertificate(), sender);
		}
		if (CollectionUtils.isNotEmpty(receiver.getCertificates())) {
			if (sit == null) {
				sit = new SecurityInformationType();
			}
			populateThisCertificate(sit.getReceiverPartyCertificate(), receiver);
		}
		
		return sit;
	}
	
	protected ec.schema.xsd.commoncomponents_3.SecurityInformationType populateSecurityInformation_3(Party sender, Party receiver, CIALevel ciaLevel) {
		logger.trace("determining if SecurityInformationType is needed");
		
		ec.schema.xsd.commoncomponents_3.SecurityInformationType sit = null;
		if (ciaLevel != null) {
			sit = new ec.schema.xsd.commoncomponents_3.SecurityInformationType();

			ec.schema.xsd.commoncomponents_3.AvailabilityLevelCodeType a = new ec.schema.xsd.commoncomponents_3.AvailabilityLevelCodeType();
			a.setValue(ciaLevel.getAvailabilityLevel().toString());
			sit.setAvailabilityLevelCode(a);
			logger.trace("added and populated AvailabilityLevelCodeType [{}]", a.getValue());

			ec.schema.xsd.commoncomponents_3.ConfidentialityLevelCodeType c = new ec.schema.xsd.commoncomponents_3.ConfidentialityLevelCodeType();
			c.setValue(ciaLevel.getConfidentialityLevel().toString());
			sit.setConfidentialityLevelCode(c);
			logger.trace("added and populated ConfidentialityLevelCodeType [{}]", c.getValue());

			ec.schema.xsd.commoncomponents_3.IntegrityLevelCodeType i = new ec.schema.xsd.commoncomponents_3.IntegrityLevelCodeType();
			i.setValue(ciaLevel.getIntegrityLevel().toString());
			sit.setIntegrityLevelCode(i);
			logger.trace("added and populated IntegrityLevelCodeType [{}]", i.getValue());			
		}
		
		if (CollectionUtils.isNotEmpty(receiver.getCertificates())) {
			if (sit == null) {
				sit = new ec.schema.xsd.commoncomponents_3.SecurityInformationType();
			}
			populateThisCertificate_3(sit.getReceiverPartyCertificate(), receiver);
		}
		
		return sit;
	}
	
	private void populateThisCertificate_3(List<ec.schema.xsd.commoncomponents_3.CertificateType> partyCertificate, Party party) {
		logger.trace("determining if CertificateType is needed");

		List<Certificate> certs = new ArrayList<Certificate>(party.getCertificates());
		Certificate c = null;

		Collections.sort(certs, new Comparator<Certificate>() {
			@Override
			public int compare(Certificate c1, Certificate c2) {
				Date date1 = (c1.getAccessInfo().getModificationDate() != null) ? c1.getAccessInfo().getModificationDate() : c1.getAccessInfo().getCreationDate();
				Date date2 = (c2.getAccessInfo().getModificationDate() != null) ? c2.getAccessInfo().getModificationDate() : c2.getAccessInfo().getCreationDate();
				if (date1.before(date2)) {
					return 1;
				} else if (date1.after(date2)) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		for (Certificate cert : certs) {
			if (cert.getIsActive() && !cert.getIsRevoked() && USAGE_KEY_ENCIPHERMENT.equals(cert.getUsage())) {
				c = cert;
				break;
			}
		}

		if (c == null) {
			logger.debug("certificate is not present for party ID [{}], CertificateType is not needed, omitting", party.getId());
		} else {
			logger.debug("certificate is present for party ID [{}], CertificateType is needed, creating", party.getId());
			ec.schema.xsd.commoncomponents_3.CertificateType ct = new ec.schema.xsd.commoncomponents_3.CertificateType();

			ec.schema.xsd.commoncomponents_3.KeyUsageCodeType kt = new ec.schema.xsd.commoncomponents_3.KeyUsageCodeType();
			if (c.getUsage() == null) {
				logger.error("KeyUsageCodeType for certificate ID [{}] is null, but this is mandatory for CertificateType", c.getId());
				throw new RuntimeException("KeyUsageCodeType for certificate ID [" + c.getId() + "] is null, but this is mandatory for CertificateType");

			}
			kt.setValue(c.getUsage());
			ct.setKeyUsageCode(kt);
			logger.trace("added and populated KeyUsageCodeType [{}]", kt.getValue());

			if (c.getValidityEndDate() != null || c.getValidityStartDate() != null) {
				logger.trace("creating PeriodType");
				ec.schema.xsd.commoncomponents_3.PeriodType pt = new ec.schema.xsd.commoncomponents_3.PeriodType();

				XMLGregorianCalendar xmlCalendar;
				GregorianCalendar gregorian = new GregorianCalendar();

				if (c.getValidityEndDate() != null) {
					gregorian.setTime(c.getValidityEndDate());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, DATE_FORMAT);
					ec.schema.xsd.commoncomponents_3.EndDateType edt = new ec.schema.xsd.commoncomponents_3.EndDateType();
					edt.setValue(xmlCalendar);
					pt.setEndDate(edt);
					logger.trace("added and populated EndDateType [{}]", edt.getValue());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, TIME_FORMAT);
					ec.schema.xsd.commoncomponents_3.EndTimeType ett = new ec.schema.xsd.commoncomponents_3.EndTimeType();
					ett.setValue(xmlCalendar);
					pt.setEndTime(ett);
					logger.trace("added and populated EndTimeType [{}]", ett.getValue());
				}

				if (c.getValidityStartDate() != null) {
					gregorian.setTime(c.getValidityStartDate());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, DATE_FORMAT);
					ec.schema.xsd.commoncomponents_3.StartDateType sdt = new ec.schema.xsd.commoncomponents_3.StartDateType();
					sdt.setValue(xmlCalendar);
					pt.setStartDate(sdt);
					logger.trace("added and populated StartDateType [{}]", sdt.getValue());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, TIME_FORMAT);
					ec.schema.xsd.commoncomponents_3.StartTimeType stt = new ec.schema.xsd.commoncomponents_3.StartTimeType();
					stt.setValue(xmlCalendar);
					pt.setStartTime(stt);
					logger.trace("added and populated StartTimeType [{}]", stt.getValue());
				}

				ct.setValidityPeriod(pt);
				logger.trace("created and populated PeriodType");
			}

			BinaryObjectType bt = new BinaryObjectType();
			bt.setMimeCode("application/base64");
			if (c.getEncodedData() == null) {
				logger.error("X509Certificate for certificate ID [{}] is null, but this is mandatory for CertificateType", c.getId());
				throw new RuntimeException("X509Certificate for certificate ID [" + c.getId() + "] is null, but this is mandatory for CertificateType");
			}
			String encoded = c.getEncodedData();
			byte[] certificateBytes = Base64.decode(encoded);

			bt.setValue(certificateBytes);
			ct.setX509Certificate(bt);
			logger.trace("added and populated X509Certificate as BinaryObjectType");

			// not populated in current implementation (otherwise it should
			// contain [0..1])
			// ct.setX509CRL(null);

			if (c.getHolder() != null) {
				logger.trace("creating X509SubjectName as TextType");
				TextType tt = new TextType();
				tt.setValue(c.getHolder());
				logger.trace("added and populated holder [{}] as TextType", tt.getValue());
				ct.setX509SubjectName(tt);
				logger.trace("added and populated X509SubjectName as TextType");
			}

			partyCertificate.add(ct);
			logger.debug("created and populated CertificateType");
		}
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
	
	private void populateThisCertificate(List<CertificateType> partyCertificate, Party party) {
		logger.trace("determining if CertificateType is needed");

		List<Certificate> certs = new ArrayList<Certificate>(party.getCertificates());
		Certificate c = null;

		Collections.sort(certs, new Comparator<Certificate>() {
			@Override
			public int compare(Certificate c1, Certificate c2) {
				Date date1 = (c1.getAccessInfo().getModificationDate() != null) ? c1.getAccessInfo().getModificationDate() : c1.getAccessInfo().getCreationDate();
				Date date2 = (c2.getAccessInfo().getModificationDate() != null) ? c2.getAccessInfo().getModificationDate() : c2.getAccessInfo().getCreationDate();
				if (date1.before(date2)) {
					return 1;
				} else if (date1.after(date2)) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		for (Certificate cert : certs) {
			if (cert.getIsActive() && !cert.getIsRevoked() && USAGE_KEY_ENCIPHERMENT.equals(cert.getUsage())) {
				c = cert;
				break;
			}
		}

		if (c == null) {
			logger.debug("certificate is not present for party ID [{}], CertificateType is not needed, omitting", party.getId());
		} else {
			logger.debug("certificate is present for party ID [{}], CertificateType is needed, creating", party.getId());
			CertificateType ct = new CertificateType();

			KeyUsageCodeType kt = new KeyUsageCodeType();
			if (c.getUsage() == null) {
				logger.error("KeyUsageCodeType for certificate ID [{}] is null, but this is mandatory for CertificateType", c.getId());
				throw new RuntimeException("KeyUsageCodeType for certificate ID [" + c.getId() + "] is null, but this is mandatory for CertificateType");

			}
			kt.setValue(c.getUsage());
			ct.setKeyUsageCode(kt);
			logger.trace("added and populated KeyUsageCodeType [{}]", kt.getValue());

			if (c.getValidityEndDate() != null || c.getValidityStartDate() != null) {
				logger.trace("creating PeriodType");
				PeriodType pt = new PeriodType();

				XMLGregorianCalendar xmlCalendar;
				GregorianCalendar gregorian = new GregorianCalendar();

				if (c.getValidityEndDate() != null) {
					gregorian.setTime(c.getValidityEndDate());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, DATE_FORMAT);
					EndDateType edt = new EndDateType();
					edt.setValue(xmlCalendar);
					pt.setEndDate(edt);
					logger.trace("added and populated EndDateType [{}]", edt.getValue());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, TIME_FORMAT);
					EndTimeType ett = new EndTimeType();
					ett.setValue(xmlCalendar);
					pt.setEndTime(ett);
					logger.trace("added and populated EndTimeType [{}]", ett.getValue());
				}

				if (c.getValidityStartDate() != null) {
					gregorian.setTime(c.getValidityStartDate());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, DATE_FORMAT);
					StartDateType sdt = new StartDateType();
					sdt.setValue(xmlCalendar);
					pt.setStartDate(sdt);
					logger.trace("added and populated StartDateType [{}]", sdt.getValue());
					xmlCalendar = getXMLGregorianCalendarInstance(gregorian, TIME_FORMAT);
					StartTimeType stt = new StartTimeType();
					stt.setValue(xmlCalendar);
					pt.setStartTime(stt);
					logger.trace("added and populated StartTimeType [{}]", stt.getValue());
				}

				ct.setValidityPeriod(pt);
				logger.trace("created and populated PeriodType");
			}

			BinaryObjectType bt = new BinaryObjectType();
			bt.setMimeCode("application/base64");
			if (c.getEncodedData() == null) {
				logger.error("X509Certificate for certificate ID [{}] is null, but this is mandatory for CertificateType", c.getId());
				throw new RuntimeException("X509Certificate for certificate ID [" + c.getId() + "] is null, but this is mandatory for CertificateType");
			}
			String encoded = c.getEncodedData();
			byte[] certificateBytes = Base64.decode(encoded);

			bt.setValue(certificateBytes);
			ct.setX509Certificate(bt);
			logger.trace("added and populated X509Certificate as BinaryObjectType");

			// not populated in current implementation (otherwise it should
			// contain [0..1])
			// ct.setX509CRL(null);

			if (c.getHolder() != null) {
				logger.trace("creating X509SubjectName as TextType");
				TextType tt = new TextType();
				tt.setValue(c.getHolder());
				logger.trace("added and populated holder [{}] as TextType", tt.getValue());
				ct.setX509SubjectName(tt);
				logger.trace("added and populated X509SubjectName as TextType");
			}

			partyCertificate.add(ct);
			logger.debug("created and populated CertificateType");
		}
	}

	private XMLGregorianCalendar getXMLGregorianCalendarInstance(GregorianCalendar gregorian, String format) {
		try {
			return getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat(format).format(gregorian.getTime()));
		} catch (DatatypeConfigurationException e) {
			logger.error("XMLGregorianCalendar instance creation failed: {}", e.getMessage());
			throw new RuntimeException("XMLGregorianCalendar instance creation failed", e);
		} catch (IllegalArgumentException e) {
			logger.error("XMLGregorianCalendar instance creation failed: {}", e.getMessage());
			throw new RuntimeException("XMLGregorianCalendar instance creation failed", e);
		}
	}

	protected String queryForSingle(final String query, DocumentInfo docInfo, final Configuration config) throws XPathException {
		List<String> queryResult = queryFor(query, docInfo, config);
		if (queryResult.size() == 0) {
			logger.debug("queryForSingle got no result, returning null");
			return null;
		}
		if (queryResult.size() == 1) {
			String result = queryResult.get(0);
			logger.trace("queryForSingle got single result [{}]", result);
			return result;
		}
		logger.error("queryForSingle got unexpected result count: [{}]", queryResult.size());
		throw new RuntimeException("queryForSingle got unexpected result count: [" + queryResult.size() + "]");
	}

	protected List<String> queryFor(final String query, DocumentInfo docInfo, final Configuration config) throws XPathException {
		final DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
		final StaticQueryContext sqc = config.newStaticQueryContext();
		final XQueryExpression exp = sqc.compileQuery(query);

		logger.debug("queryFor [{}]", query);
		dynamicContext.setContextItem(docInfo);
		final SequenceIterator<Item> iter = exp.iterator(dynamicContext);

		//chiricr: XQueryExpression.iterator never returns null
//		if (iter == null) {
//			logger.debug("query got null iterator, returning null");
//			return null;
//		}

		List<String> result = new ArrayList<String>();
		Item item = iter.next();
		while (item != null) {
			result.add(item.getStringValue());
			item = iter.next();
		}

		logger.debug("query result has [{}] element(s)", result.size());
		return result;
	}
	
	/**
	 * 
	 * @param query the XPath query
	 * @param documentInfo the xml document
	 * @param configuration
	 * @return a node iterator
	 * @throws XPathException
	 */
	protected SequenceIterator<NodeInfo> queryForNode(String query, DocumentInfo documentInfo, Configuration configuration) throws XPathException {
		final DynamicQueryContext dynamicContext = new DynamicQueryContext(configuration);
		final StaticQueryContext sqc = configuration.newStaticQueryContext();
		final XQueryExpression exp = sqc.compileQuery(query);

		logger.debug("queryFor [{}]", query);
		dynamicContext.setContextItem(documentInfo);
		return exp.iterator(dynamicContext);
	}

	protected XdmNode getChild(XdmNode parent, String childName) {

		XdmSequenceIterator iter = parent.axisIterator(Axis.CHILD, new QName(childName));

		if (iter.hasNext()) {
			return (XdmNode) iter.next();
		} else {
			return null;
		}
	}

	private static DatatypeFactory getDataTypeFactory() throws DatatypeConfigurationException {
		if (dtf == null) {
			dtf = DatatypeFactory.newInstance();
		}
		return dtf;
	}
	
	protected EntityAccessInfo createAccessInfo(TrustExMessage<String> message) {
		EntityAccessInfo accessInfo = new EntityAccessInfo();
		accessInfo.setCreationDate(new Date());
		accessInfo.setModificationDate(new Date());
		accessInfo.setCreationId(message.getHeader().getAuthenticatedUser());
		accessInfo.setModificationId(message.getHeader().getAuthenticatedUser());
		return accessInfo;
	}
}
