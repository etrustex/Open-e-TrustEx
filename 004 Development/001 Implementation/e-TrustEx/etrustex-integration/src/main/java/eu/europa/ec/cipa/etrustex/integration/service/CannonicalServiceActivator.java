package eu.europa.ec.cipa.etrustex.integration.service;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.ITransactionService;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@Component
public class CannonicalServiceActivator  {
	
	private final static String CANNONICAL_ROOT_TEMPLATE = "templates/cannonical.flt";	
	private final static String SUPPLIER_ROLE_CD ="SUP";
	private final static String CUSTOMER_ROLE_CD ="CUST";

	private Configuration freemarkerConfig;
	
	@Autowired
	private ITransactionService transactionService;
	
	public CannonicalServiceActivator(){
		
		
		FreeMarkerConfigurationFactoryBean ft = new FreeMarkerConfigurationFactoryBean();
		ft.setTemplateLoaderPaths(new String[]{"classpath:/templates"});
		
		Properties settings = new Properties();
		settings.put("datetime_format", "dd/MM/yyyy");
		settings.put("number_format", "#");
		settings.put("whitespace_stripping", "true");
		ft.setFreemarkerSettings(settings);

		
		try {
			freemarkerConfig = ft.createConfiguration();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
		
	protected String getDefaultIdentifier (Set<PartyIdentifier> identifiers){
		String defaultIdentifier = null;
	
		for (PartyIdentifier partyIdentifier : identifiers) {
			if (IdentifierIssuingAgency.GLN.equals(partyIdentifier.getSchemeId())){
				defaultIdentifier= partyIdentifier.getValue();
			}
		}
		if (defaultIdentifier == null){
			defaultIdentifier = identifiers.iterator().next().getValue();
		}
		return defaultIdentifier;
	}	

	public String createCanonical(TrustExMessage<String> message,			
			eu.europa.ec.cipa.etrustex.domain.Message internalMessage,
			eu.europa.ec.cipa.etrustex.domain.Message internalParentMessage)
			throws TechnicalErrorException {
		
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		freemarkerConfig.setEncoding(Locale.getDefault(), "UTF-8");
		TrustExMessageHeader header = message.getHeader();
		
		Map<String, Object> root = new HashMap<String, Object>();
		try {
			Template temp = freemarkerConfig.getTemplate(CANNONICAL_ROOT_TEMPLATE);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Set<PartyRole> partyRoles = internalMessage.getAgreement().getPartyRoles();
			Party supplierParty = null;
			Party customerParty = null;
			for (PartyRole partyRole : partyRoles) {
				if (CUSTOMER_ROLE_CD.equals(partyRole.getRole().getCode())) {
					customerParty = partyRole.getParty();
				} else if (SUPPLIER_ROLE_CD.equals(partyRole.getRole().getCode())) {
					supplierParty = partyRole.getParty();
				}
			}
			String supplierIdentifier = null;
			supplierIdentifier = supplierParty == null ? null : getDefaultIdentifier(supplierParty.getIdentifiers());
			String customerIdentifier = null;
			customerIdentifier = customerParty == null ? null : getDefaultIdentifier(customerParty.getIdentifiers());
			eu.europa.ec.cipa.etrustex.domain.Document doc = internalMessage.getTransaction().getDocument();

			String documentLocalName = doc.getLocalName() + "Received";
			String namespace = null;
			Transaction transaction = transactionService.getTransaction(message.getHeader().getTransactionTypeId());
			if (transaction.getTransactionTypeCode() != null) {
				//replace transaction NS with document NS
				String requestLocalName = transaction.getRequestLocalName().replaceAll("Submit", "");
				requestLocalName = requestLocalName.substring(0, requestLocalName.lastIndexOf("Request"));
				namespace = message.getHeader().getTransactionNamespace().replaceAll(requestLocalName, documentLocalName);
			} else {
				namespace = message.getHeader().getTransactionNamespace().replaceAll(doc.getLocalName(), documentLocalName);
			}
			namespace = namespace.replaceAll("services:wsdl", "schema:xsd");
			namespace = namespace.replaceAll("-2.1", "-0.1");
			namespace = namespace.replaceAll("-2", "-0.1");

			List<AdditionalId> cust = null;
			List<AdditionalId> supp = null;

			if (supplierParty != null) {
				supp = extractaditionalIDs(supplierParty);
			}

			if (customerParty != null) {
				cust = extractaditionalIDs(customerParty);
			}

			// String supplierPartyId
			root.put("LOCAL_NAME", documentLocalName);
			root.put("NAMESPACE", namespace == null ? "" : namespace);
			root.put("MESSAGE_ID", "" + header.getMessageId());
			root.put("SUPPLIER_ID", supplierIdentifier == null ? "" : supplierIdentifier);
			root.put("CUSTOMER_ID", customerIdentifier == null ? "" : customerIdentifier);
			if (supp != null) {
				root.put("SUPPLIER_ADD", supp);
			}

			if (cust != null) {
				root.put("CUSTOMER_ADD", cust);
			}

			root.put("DOCUMENT_ID", internalMessage.getDocumentId());
			root.put("RECEPTION_DATE", format.format(internalMessage.getReceptionDate()));
			root.put("REGISTRATION_DATE", format.format(internalMessage.getReceptionDate()));
			root.put("SUP_INT_ID", supplierParty == null ? "" : "" + supplierParty.getId());
			root.put("CUST_INT_ID", customerParty == null ? "" : "" + customerParty.getId());
			root.put("SCHEMATRON_WARNINGS", message.getHeader().getSchematronResult() == null ? "" : message.getHeader().getSchematronResult());
			root.put("STATUS", header.getParentStatusCode() == null ? "" : header.getParentStatusCode());

			if (internalParentMessage != null) {
				eu.europa.ec.cipa.etrustex.domain.Message parentMessage = internalParentMessage;
				root.put("REC_DATE", parentMessage.getReceptionDate() == null ? "" : format.format(parentMessage.getReceptionDate()));
				root.put("REC_DOC_NAME", parentMessage.getTransaction().getDocument().getName());

			} else {
				root.put("REC_DATE", "");
				root.put("REC_DOC_NAME", "");
			}
			root.put("DOCUMENT", message.getPayload());

			root.put("DOCUMENT", message.getPayload());

			StringWriter sr = new StringWriter();
			temp.process(root, sr);
			return sr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, internalMessage.getTransaction().getDocument()
					.getDocumentTypeCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}
	}

	private List<AdditionalId> extractaditionalIDs(Party supplierParty) {
		List<AdditionalId> out = new ArrayList<AdditionalId>();

		for (PartyIdentifier ident : supplierParty.getIdentifiers()) {
			String schemeId = ident.getSchemeId().getSchemeID();
			String addIdValue = ident.getValue();

			AdditionalId adId = new AdditionalId(schemeId, addIdValue);
			out.add(adId);
		}
		return out;
	}

	public class AdditionalId {

		private String schemeID;
		private String value;

		public AdditionalId(String schemeID, String value) {
			this.schemeID = schemeID;
			this.value = value;
		}

		public String getSchemeID() {
			return schemeID;
		}

		public void setSchemeID(String schemeID) {
			this.schemeID = schemeID;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
	
	
	
}
