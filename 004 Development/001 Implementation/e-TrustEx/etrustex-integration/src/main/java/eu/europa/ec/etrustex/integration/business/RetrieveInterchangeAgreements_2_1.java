package eu.europa.ec.etrustex.integration.business;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ec.schema.xsd.commonaggregatecomponents_2_1.InterchangeAgreementType;
import ec.schema.xsd.commonaggregatecomponents_2_1.PartyRoleType;
import ec.schema.xsd.commonaggregatecomponents_2_1.PartyType;
import ec.schema.xsd.commonaggregatecomponents_2_1.SecurityInformationType;
import ec.schema.xsd.commonbasiccomponents_1.AvailabilityLevelCodeType;
import ec.schema.xsd.commonbasiccomponents_1.ConfidentialityLevelCodeType;
import ec.schema.xsd.commonbasiccomponents_1.IntegrityLevelCodeType;
import ec.schema.xsd.retrieveinterchangeagreementsrequest_2_2.RetrieveInterchangeAgreementsRequestType;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_2_2.ObjectFactory;
import ec.schema.xsd.retrieveinterchangeagreementsresponse_2_2.RetrieveInterchangeAgreementsResponseType;
import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RoleCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.StartDateType;

public class RetrieveInterchangeAgreements_2_1 extends TrustExBusinessService implements ISynchBusinessService{
	
	private static final Logger logger = LoggerFactory
			.getLogger(RetrieveInterchangeAgreements_2_1.class);
	
	@Autowired
	protected IInterchangeAgreementService interchangeAgreementService;
		
	private static final String DATE_FORMAT_MASK = "yyyy-MM-dd";
	private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_MASK);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<JAXBElement<RetrieveInterchangeAgreementsResponseType>> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
		
		TrustExMessage<JAXBElement<RetrieveInterchangeAgreementsResponseType>> responseMessage = new TrustExMessage<JAXBElement<RetrieveInterchangeAgreementsResponseType>>(null);
		responseMessage.setHeader(message.getHeader());
		Unmarshaller unmarshaller =null;
		JAXBElement<RetrieveInterchangeAgreementsRequestType> requestElement = null;
		RetrieveInterchangeAgreementsRequestType retrieveICARequest = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(
					ec.schema.xsd.retrieveinterchangeagreementsrequest_2_2.ObjectFactory.class);
			unmarshaller = jaxbContext.createUnmarshaller();
			Source source = new StreamSource(new StringReader(message.getPayload()));
			requestElement = unmarshaller.unmarshal(source,RetrieveInterchangeAgreementsRequestType.class);
			retrieveICARequest = requestElement.getValue();	
		} catch (JAXBException e) {
			logger.error("Technical error occured during XSD validation " +e.getMessage(),e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.REQUEST_XSD_VALIDATION_SYNC.getDescription(), null,
					ErrorResponseCode.REQUEST_XSD_VALIDATION_SYNC, null, null);
		}
		
		Long requesterId = message.getHeader().getSenderPartyId();
		Party requester = partyService.getParty(requesterId);
		
		PartyIdentifier partyId1 = null;
		PartyIdentifier partyId2 = null;
		
		switch (retrieveICARequest.getParty().size()) {
			case 2: if (CollectionUtils.isEmpty(retrieveICARequest.getParty().get(0).getPartyIdentification()) || 
						retrieveICARequest.getParty().get(0).getPartyIdentification().size() > 1 ||
						CollectionUtils.isEmpty(retrieveICARequest.getParty().get(1).getPartyIdentification()) ||
						retrieveICARequest.getParty().get(1).getPartyIdentification().size() > 1) {
							throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
									ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.partyId.maxNo"));
					}
					partyId1 = new PartyIdentifier();
					partyId1.setValue(retrieveICARequest.getParty().get(0).getPartyIdentification().get(0).getID().getValue());
					String schemeID = retrieveICARequest.getParty().get(0).getPartyIdentification().get(0).getID().getSchemeID();
					partyId1.setSchemeId(StringUtils.isNotBlank(schemeID) 
							? IdentifierIssuingAgency.valueOf(schemeID.replace(":", "_"))
							: null);
					
					partyId2 = new PartyIdentifier();
					partyId2.setValue(retrieveICARequest.getParty().get(1).getPartyIdentification().get(0).getID().getValue());
					schemeID = retrieveICARequest.getParty().get(1).getPartyIdentification().get(0).getID().getSchemeID();
					partyId2.setSchemeId(StringUtils.isNotBlank(schemeID) 
							? IdentifierIssuingAgency.valueOf(schemeID.replace(":", "_"))
							: null);
										
					break;
					
			case 1: if (CollectionUtils.isEmpty(retrieveICARequest.getParty().get(0).getPartyIdentification()) || 
						retrieveICARequest.getParty().get(0).getPartyIdentification().size() > 1){
							throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
									ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.partyId.maxNo"));
					}
				
					partyId1 = new PartyIdentifier();
					partyId1.setValue(retrieveICARequest.getParty().get(0).getPartyIdentification().get(0).getID().getValue());
					schemeID = retrieveICARequest.getParty().get(0).getPartyIdentification().get(0).getID().getSchemeID();
					partyId1.setSchemeId(StringUtils.isNotBlank(schemeID) 
							? IdentifierIssuingAgency.valueOf(schemeID.replace(":", "_"))
							: IdentifierIssuingAgency.GLN);				    
					//When the party != requester
					if(!partyId1.equals(requester.getName()))
						partyId2 = requester.getIdentifiers().iterator().next();
					break;
	
			default: partyId1 = requester.getIdentifiers().iterator().next();	
		}
		
		Party party1 = partyService.getPartyByID(partyId1.getSchemeId(), partyId1.getValue(), message.getHeader().getIssuer().getBusinessDomain());
		Party party2 = null;
		if(partyId2 != null) {
			party2 = partyService.getPartyByID(partyId2.getSchemeId(), partyId2.getValue(), message.getHeader().getIssuer().getBusinessDomain());
		}
		if (party1 == null || (partyId2 != null && party2 == null)) {
			logger.error("Technical error: party not found");
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party"));
		}
			
		List<String> documentTypeIds = new ArrayList<String>();	
		checkBusinessRules(retrieveICARequest.getDocumentTypeCode().size());			
		for (DocumentTypeCodeType doc : retrieveICARequest.getDocumentTypeCode()) {
			documentTypeIds.add(doc.getValue());
		}
		
		logger.debug("retrieving interchange agreement(s)");
		List<InterchangeAgreement> agreements = interchangeAgreementService.getInterchangeAgreements(message.getHeader().getIssuer(), party1, party2, documentTypeIds);
		logger.debug("retrieved [{}] interchange agreement(s)", agreements == null ? null : agreements.size());
		
		RetrieveInterchangeAgreementsResponseType response = new RetrieveInterchangeAgreementsResponseType();
		List<InterchangeAgreementType> agreementsInResponse = response.getInterchangeAgreement();
		for (InterchangeAgreement ica : agreements) {
			logger.debug("processing interchange agreement with ID [{}]", ica.getId());
			Party partyA = null;
			Party partyB = null;
			
			for (PartyRole partyRole : ica.getPartyRoles()) {
				if(partyA == null){
					partyA = partyRole.getParty();
				}else if (partyB == null) {
					partyB = partyRole.getParty();
				}
			}
			
			logger.debug("processing next interchange agreement with ID [{}]", ica.getId());
			agreementsInResponse.add(populateInterchangeAgreementType_2_1(ica, partyA, partyB, ica.getCiaLevel()));
			logger.trace("added interchange agreement with ID [{}], requester [{}] -> receiver [{}] to result list in response", ica.getId(), requester.getName(), partyA.getName());
		}
		
		Transaction transaction = authorisationService.getTransactionById(message.getHeader().getTransactionTypeId());
		
		ObjectFactory factory = new ObjectFactory();
		String responseElementLocalName = factory.createRetrieveInterchangeAgreementsResponse(response).getName().getLocalPart();
	    JAXBElement<RetrieveInterchangeAgreementsResponseType> responseElement = new JAXBElement<RetrieveInterchangeAgreementsResponseType>(
	    		new QName(transaction.getNamespace(), responseElementLocalName, "ec"), RetrieveInterchangeAgreementsResponseType.class,response);
	    responseMessage.setPayload(responseElement);
		logger.debug("finished building response");
		return responseMessage;
			
		
		
	}
	
	private InterchangeAgreementType populateInterchangeAgreementType_2_1(InterchangeAgreement interchangeAgreement, Party sender, Party receiver, CIALevel cia) {
		logger.trace("creating InterchangeAgreementType");
		InterchangeAgreementType iat = new InterchangeAgreementType();
		populateICAInformation(iat, interchangeAgreement, sender, receiver);		
		
		ec.schema.xsd.commonaggregatecomponents_2_1.SecurityInformationType sit = new ec.schema.xsd.commonaggregatecomponents_2_1.SecurityInformationType();
		if(cia != null){
			populateCIA(sit, cia);
		}else{
			sit = null;
		}
		iat.setSecurityInformation(sit);		
		iat.setProfileID(populateProfileId(interchangeAgreement.getProfile()));	
		if(interchangeAgreement.getValidityStartDate() != null)
			iat.setValidityPeriod(populateValidityDate(interchangeAgreement.getValidityStartDate()));
		logger.debug("created and populated InterchangeAgreementType");
		return iat;
	}
	
	private void populateICAInformation(InterchangeAgreementType iat, InterchangeAgreement ica, Party sender, Party receiver){
		for (PartyRole partyRole : ica.getPartyRoles()) {
			iat.getPartyRole().add(populatePartyRole(partyRole));		
			
		}
	}
	
	private PeriodType populateValidityDate(Date startDate){
		PeriodType periodType = new PeriodType();
		XMLGregorianCalendar dateG = null;
		try {
			dateG = DatatypeFactory.newInstance().newXMLGregorianCalendar(dateFormat.format(startDate));			
		} catch (DatatypeConfigurationException e) {
			logger.error("Technical error", e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}

		StartDateType startDateType = new StartDateType();
		startDateType.setValue(dateG);
		periodType.setStartDate(startDateType);
		return periodType;
	}
	
	private PartyRoleType populatePartyRole(PartyRole partyRole){
		PartyRoleType partyRoleType = new PartyRoleType();
		RoleCodeType roleCodeType = new RoleCodeType();
		PartyType partyType = new PartyType();
				
		partyType.setX509Certificate(populateCertificates(partyRole.getParty().getCertificates()));
		for (PartyIdentifier partyIdentifier : partyRole.getParty().getIdentifiers()) {
			PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
			IDType id = new IDType();
			if(partyIdentifier.getSchemeId().getSchemeID().isEmpty()){
				id.setSchemeID("GLN");
			}else{
				id.setSchemeID(partyIdentifier.getSchemeId().getSchemeID());
			}
			
			id.setValue(partyIdentifier.getValue());
			partyIdentificationType.setID(id);
			partyType.getPartyIdentification().add(partyIdentificationType);			
		}
		partyRoleType.setParty(partyType);
		roleCodeType.setName(partyRole.getRole().getName());
		roleCodeType.setValue(partyRole.getRole().getCode());		
		partyRoleType.setRoleCode(roleCodeType);
		return partyRoleType;
	}
	
	private byte[] populateCertificates(Set<Certificate> certificates){
		byte[] certifByte = null;
		for (Certificate certificate : certificates) {
			if(certificate.getIsActive() && !certificate.getIsRevoked()){
				if(certificate != null)
					certifByte = certificate.toString().getBytes();
			}
		}
		return certifByte;
	}
	
	private void checkBusinessRules(int documentTypeIds) {		
		if (documentTypeIds > 500) {
			throw new MessageProcessingException("soapenv:Client",
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.docTypeCode.maxNo")); 
		}
	}

}
