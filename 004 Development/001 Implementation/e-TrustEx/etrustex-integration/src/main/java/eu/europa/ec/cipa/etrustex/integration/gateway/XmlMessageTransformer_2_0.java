package eu.europa.ec.cipa.etrustex.integration.gateway;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.EndpointIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.signatureaggregatecomponents_2.SignatureInformationType;

import org.unece.cefact.namespaces.standardbusinessdocumentheader.Partner;

import ec.schema.xsd.ack_2.AcknowledgmentType;
import ec.schema.xsd.commonaggregatecomponents_2.AcknowledgedDocumentReferenceType;
import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import ec.schema.xsd.commonaggregatecomponents_2.TechnicalHeaderType;
import ec.services.wsdl.documentwrapper_2.DeleteDocumentWrapperRequestResponse;
import ec.services.wsdl.documentwrapper_2.StoreDocumentWrapperResponse;

public class XmlMessageTransformer_2_0 {
	
	private static DatatypeFactory dtf = null;
	
	public static StoreDocumentWrapperResponse createStoreDocumentWrapperResponse(HeaderType header, ec.schema.xsd.documentwrapper_1.DocumentWrapperType documentWrapper) {
		StoreDocumentWrapperResponse response = new StoreDocumentWrapperResponse();
		
		AcknowledgmentType ack = new AcknowledgmentType();
		ec.schema.xsd.commonbasiccomponents_1.AckIndicatorType ackIndicatorType = new ec.schema.xsd.commonbasiccomponents_1.AckIndicatorType();
		ackIndicatorType.setValue(true);
		ack.setAckIndicator(ackIndicatorType);
		try {
			ack.setIssueDate(getIssueDateSetToCurrent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ack.setAcknowledgedDocumentReference(new AcknowledgedDocumentReferenceType());
		ack.getAcknowledgedDocumentReference().setSenderParty(convertPartyType(header.getBusinessHeader().getSender().get(0)));
		if (header.getBusinessHeader().getReceiver() != null && header.getBusinessHeader().getReceiver().size() > 0) {
			for (Partner receiver : header.getBusinessHeader().getReceiver()) {
				ack.getAcknowledgedDocumentReference().getReceiverParty().add(convertPartyType(receiver));
			}
		}
		DocumentReferenceType documentReference = new DocumentReferenceType();
		documentReference.setID(documentWrapper.getID());
		documentReference.setDocumentTypeCode(documentWrapper.getDocumentTypeCode());
		ack.getAcknowledgedDocumentReference().setDocumentReference(documentReference);
		
		header.setTechnicalHeader(new TechnicalHeaderType()); //This will be populated with the recomputed signature
		header.getTechnicalHeader().getSignatureInformation().add(new SignatureInformationType());
		response.setAck(ack);
		
		return response;
	}
	public static DeleteDocumentWrapperRequestResponse createDeleteDocumentWrapperResponse(HeaderType header, ec.schema.xsd.documentwrapperrequest_1.DocumentWrapperRequestType documentWrapper) {
		DeleteDocumentWrapperRequestResponse response = new DeleteDocumentWrapperRequestResponse();
		
		AcknowledgmentType ack = new AcknowledgmentType();
		ec.schema.xsd.commonbasiccomponents_1.AckIndicatorType ackIndicatorType = new ec.schema.xsd.commonbasiccomponents_1.AckIndicatorType();
		ackIndicatorType.setValue(true);
		ack.setAckIndicator(ackIndicatorType);
		try {
			ack.setIssueDate(getIssueDateSetToCurrent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ack.setAcknowledgedDocumentReference(new AcknowledgedDocumentReferenceType());
		ack.getAcknowledgedDocumentReference().setSenderParty(convertPartyType(header.getBusinessHeader().getSender().get(0)));
		if (header.getBusinessHeader().getReceiver() != null && header.getBusinessHeader().getReceiver().size() > 0) {
			for (Partner receiver : header.getBusinessHeader().getReceiver()) {
				ack.getAcknowledgedDocumentReference().getReceiverParty().add(convertPartyType(receiver));
			}
		}
		DocumentReferenceType documentReference = new DocumentReferenceType();
		documentReference.setID(documentWrapper.getDocumentReferenceRequest().getID());
		documentReference.setDocumentTypeCode(documentWrapper.getDocumentReferenceRequest().getDocumentTypeCode());
		ack.getAcknowledgedDocumentReference().setDocumentReference(documentReference);
		
		header.setTechnicalHeader(new TechnicalHeaderType()); //This will be populated with the recomputed signature
		header.getTechnicalHeader().getSignatureInformation().add(new SignatureInformationType());
		response.setAck(ack);
		
		return response;
	}
	private static IssueDateType getIssueDateSetToCurrent() throws DatatypeConfigurationException {
		IssueDateType issueDate = new IssueDateType();
		
		GregorianCalendar gcal = new GregorianCalendar();
	    XMLGregorianCalendar xgcal = getDataTypeFactory().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(gcal.getTime()));
	    issueDate.setValue(xgcal);
	    
		return issueDate;
	}
	
	private static PartyType convertPartyType(Partner partner) {
		PartyType partyType = null;
		
		if (partner == null) {
			return null;
		}
		
		partyType = new PartyType();
		
		EndpointIDType endpointID = new EndpointIDType();
		if (partner.getIdentifier().getAuthority() != null) {
			endpointID.setSchemeID(partner.getIdentifier().getAuthority());
		}
		partyType.setEndpointID(endpointID);
		partyType.getEndpointID().setValue(partner.getIdentifier().getValue());
		
		PartyIdentificationType partyIdentification = new PartyIdentificationType();
		IDType id = new IDType();
		if (partner.getIdentifier().getAuthority() != null) {
			id.setSchemeID(partner.getIdentifier().getAuthority());
		}
		id.setValue(partner.getIdentifier().getValue());		
		partyIdentification.setID(id);		
		partyType.getPartyIdentification().add(partyIdentification);
		
		return partyType;
	}
	
	private static DatatypeFactory getDataTypeFactory() throws DatatypeConfigurationException {
		if (dtf == null){			
			dtf = DatatypeFactory.newInstance();									
		}
		return dtf;
	}

}
