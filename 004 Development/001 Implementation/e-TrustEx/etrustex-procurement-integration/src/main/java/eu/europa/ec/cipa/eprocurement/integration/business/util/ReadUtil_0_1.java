package eu.europa.ec.cipa.eprocurement.integration.business.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class ReadUtil_0_1 {

	private static final Logger logger = LoggerFactory
			.getLogger(ReadUtil_0_1.class);
	
	private static final String RESPONSE_CODE_START = "<urn:ResponseCode>";
	private static final String RESPONSE_CODE_STOP = "</urn:ResponseCode>";
	
	private static final String ISSUE_DATE_START = "<urn:IssueDate>";
	private static final String ISSUE_DATE_STOP = "</urn:IssueDate>";

	private static final String DOC_ID = "__DOC_ID__";
	private static final String DOC_ISSUE_DATE = "__DOC_ISSUE_DATE__";
	private static final String DOC_TYPE_CODE = "__DOC_TYPE_CODE__";
	private static final String DOC_STATUS_CODE = "__DOC_STATUS_CODE__";
	private static final String DOC_CUSTOMER = "__DOC_CUSTOMER__";
	private static final String DOC_RESPONSE_CODE = "__DOC_RESPONSE_CODE__";

	public static final String STATUS_RESPONSE_START = "<ec:StatusResponse xmlns:ec='ec:services:wsdl:StatusRequest-0.2'><ec1:DocumentReferenceResponseRelatedDocs xmlns:ec1='ec:schema:xsd:CommonAggregateComponents-0.1'>";
	public static final String STATUS_RESPONSE_STOP = "</ec1:DocumentReferenceResponseRelatedDocs></ec:StatusResponse>";

	public static final String STATUS_RESPONSE_NOTEXIST = "<ec1:DocumentReferenceResponse><urn:ID xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2'>__DOC_ID__</urn:ID><urn:DocumentTypeCode xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2'>__DOC_TYPE_CODE__</urn:DocumentTypeCode><urn:DocumentStatusCode xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2'>0</urn:DocumentStatusCode></ec1:DocumentReferenceResponse>";
	public static final String STATUS_RESPONSE_MAINDOC = "<ec1:DocumentReferenceResponse xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' xmlns:ec2='ec:schema:xsd:CommonBasicComponents-0.1'> <urn:ID>__DOC_ID__</urn:ID> __DOC_ISSUE_DATE__ <urn:DocumentTypeCode>__DOC_TYPE_CODE__</urn:DocumentTypeCode> <urn:DocumentStatusCode>__DOC_STATUS_CODE__</urn:DocumentStatusCode>__DOC_RESPONSE_CODE__</ec1:DocumentReferenceResponse>";
	public static final String STATUS_RESPONSE_RELATED = "<ec1:RelatedDocument xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' xmlns:ec2='ec:schema:xsd:CommonBasicComponents-0.1'> <urn:ID>__DOC_ID__</urn:ID> __DOC_ISSUE_DATE__ <urn:DocumentTypeCode>__DOC_TYPE_CODE__</urn:DocumentTypeCode> <urn:DocumentStatusCode>__DOC_STATUS_CODE__</urn:DocumentStatusCode>__DOC_RESPONSE_CODE__ </ec1:RelatedDocument>";
	
	public static final String QUERY_RESPONSE_RELATED = "<ec1:ParentDocument xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' xmlns:ec2='ec:schema:xsd:CommonBasicComponents-0.1'> <urn:ID>__DOC_ID__</urn:ID> __DOC_ISSUE_DATE__ <urn:DocumentTypeCode>__DOC_TYPE_CODE__</urn:DocumentTypeCode> <urn:DocumentStatusCode>__DOC_STATUS_CODE__</urn:DocumentStatusCode>__DOC_RESPONSE_CODE__<ec2:CustomerID>__DOC_CUSTOMER__</ec2:CustomerID> </ec1:ParentDocument>";
	public static final String QUERY_RESPONSE_MAINDOC = "<ec1:DocumentReferenceResponse xmlns:urn='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' xmlns:ec2='ec:schema:xsd:CommonBasicComponents-0.1'> <urn:ID>__DOC_ID__</urn:ID> __DOC_ISSUE_DATE__ <urn:DocumentTypeCode>__DOC_TYPE_CODE__</urn:DocumentTypeCode> <urn:DocumentStatusCode>__DOC_STATUS_CODE__</urn:DocumentStatusCode>__DOC_RESPONSE_CODE__<ec2:CustomerID>__DOC_CUSTOMER__</ec2:CustomerID> </ec1:DocumentReferenceResponse>";
	
	public static final String QUERY_RESPONSE_START = "<ec:QueryResponse xmlns:ec='ec:services:wsdl:QueryRequest-0.2' xmlns:ec1='ec:schema:xsd:CommonAggregateComponents-0.1'>";
	public static final Object QUERY_RESPONSE_STOP = "</ec:QueryResponse>";

	public static final Object QUERY_MAINDOC_START = "<ec1:DocumentReferenceResponseParentDocs>";
	public static final Object QUERY_MAINDOC_STOP = "</ec1:DocumentReferenceResponseParentDocs>";

	private IMetadataService metadataService;
	
	private String schemeSeparator ;

	public ReadUtil_0_1(IMetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public String createStatusResponseNotExists(String documentId,
			String documentTypeCode) {
		String responseNotExist = STATUS_RESPONSE_NOTEXIST;
		responseNotExist = responseNotExist.replaceAll(DOC_ID, documentId);
		responseNotExist = responseNotExist.replaceAll(DOC_TYPE_CODE,
				documentTypeCode);
		return responseNotExist;
	}

	public void appendDoc(StringBuffer response, String template,
			Message message, String supplierId) {
		String docStr = template;

		docStr = docStr.replaceAll(DOC_ID, message.getDocumentId());
		
		String dateStr = dateToXml(message.getIssueDate());
		String dateXML = "";
		if (dateStr !=null){
			dateXML = ISSUE_DATE_START+dateStr+ISSUE_DATE_STOP;
		}
		
		docStr = docStr.replaceAll(DOC_ISSUE_DATE,
				dateXML);
		docStr = docStr.replaceAll(DOC_TYPE_CODE,
				message.getMessageDocumentTypeCode());

		// if application response, take the parent status
		if (message != null
				&& TrustExBusinessService.APP_RESPONSE_TYPE_CD.equals(message
						.getMessageDocumentTypeCode())
				&& message.getParentMessages().size() > 0) {
			java.util.Iterator<Message> it = message.getParentMessages()
					.iterator();
			docStr = docStr.replaceAll(DOC_STATUS_CODE,
					translateStatusCodeValue(it.next()));

		} else { // else take message status
			docStr = docStr.replaceAll(DOC_STATUS_CODE,
					translateStatusCodeValue(message));
		}

		docStr = docStr.replaceAll(DOC_CUSTOMER,
				getCustomer(message, supplierId));

		// only show response code when relevant
		if (message.getResponseCode() != null) {
			String responseCode = RESPONSE_CODE_START
					+ message.getResponseCode() + RESPONSE_CODE_STOP;
			docStr = docStr.replaceAll(DOC_RESPONSE_CODE, responseCode);
		} else {
			docStr = docStr.replaceAll(DOC_RESPONSE_CODE, "");
		}

		logger.debug("append " + docStr);

		response.append(docStr);
	}

	private String getCustomer(Message message, String supplierId) {
		logger.debug("Supplier id:" + supplierId);
		String customerId = "";
		boolean found = false;
		if (message.getReceiver() != null) {
			for (PartyIdentifier id : message.getReceiver().getIdentifiers()) {
				String scheme = id.getSchemeId().getISO6523Code();
				String value = id.getValue();
				logger.debug("scheme:" + scheme);
				logger.debug("value:" + value);
				if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(scheme)) {
					customerId = value;
					logger.debug("ID GLN:" + customerId);
				} else {
					customerId = id.getSchemeId().getSchemeID()
							+ getSchemeSeparator() + value;
					logger.debug("ID:" + customerId);
				}
				if (!customerId.equals(supplierId)) {
					found = true;
					break;
				}
			}
			if (found) {
				logger.debug("Found:" + customerId);
				return customerId;
			}
		}
		if (message.getSender() != null) {
			for (PartyIdentifier id : message.getSender().getIdentifiers()) {
				String scheme = id.getSchemeId().getISO6523Code();
				String value = id.getValue();
				logger.debug("scheme:" + scheme);
				logger.debug("value:" + value);
				if (IdentifierIssuingAgency.GLN.getISO6523Code().equals(scheme)) {
					customerId = value;
					logger.debug("ID GLN:" + customerId);
				} else {
					customerId = id.getSchemeId().getSchemeID()
							+ getSchemeSeparator() + value;
					logger.debug("ID:" + customerId);
				}
				if (!customerId.equals(supplierId)) {
					found = true;
					break;
				}
			}

			if (found) {
				logger.debug("Found:" + customerId);
				return customerId;
			}
		}
		logger.debug("Not found");
		return "";
	}

	private String getSchemeSeparator() {
		if (schemeSeparator==null){ // only load once
			Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long)null, null, null, null);
			schemeSeparator = ":";
			if(metadata.containsKey(MetaDataItemType.SCHEME_ID_SEPARATOR)){
				schemeSeparator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
			}else{
				logger.error("--> 'SCHEME_ID_SEPARATOR' Metadata is not configured so ':' will be used");
			}
		}
		return schemeSeparator;
	}

	private String translateStatusCodeValue(Message message) {
		Map<String, MessageResponseCode> respcodes = metadataService
				.retrieveMessageResponseCodes(null, null, message
						.getTransaction().getDocument().getId(), null);
		logger.debug("Response count:" + respcodes.size());
		MessageResponseCode code = respcodes.get(message.getStatusCode());
		logger.debug("Code to translate:" + message.getStatusCode());
		String translatedCode = code == null ? "" : code.getStatusCode();
		logger.debug("Translated code:" + translatedCode);
		return translatedCode;
	}

	private String dateToXml(Date issueDate) {
		if (issueDate==null)
			return null;
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateFormted = sdf.format(issueDate);
		logger.debug("formated date:" + dateFormted);
		return dateFormted;
	}

}
