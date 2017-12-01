package eu.europa.ec.cipa.etrustex.integration.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.TrustExIntegrationSupport;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.integration.util.ResourceToDatabaseResolver;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.ITransactionService;
import eu.europa.ec.cipa.etrustex.services.IXmlService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;

public class TrustExServiceActivator extends TrustExIntegrationSupport {

	private static final Logger logger = LoggerFactory
			.getLogger(TrustExServiceActivator.class);


	@Autowired
	protected IAuthorisationService authorisationService;
		
	@Autowired
	protected IXmlService xmlService;
	@Autowired
	protected IInterchangeAgreementService interchangeAgreementService;

	@Autowired
	protected MessageChannel errorChannel;
	
	@Autowired
	protected ILogService logService;
	
	@Autowired
	protected IBusinessDomainService businessDomainService;
	
	@Autowired
	protected IPartyService partyService;
	
	@Autowired
	protected ITransactionService transactionService;
	
	@Autowired
	protected LogServiceHelper logServiceHelper;
	
	private static final HashMap<String, Schema> schemasMap = new HashMap<String, Schema>();
	
	protected StreamSource getStreamSource(MetaDataItem localDbItem,
			MetaDataItem urlItem) {
		logger.debug("stream source localDbItem length [{}] urlItem [{}]", localDbItem == null || localDbItem.getValue() == null 
				? null: localDbItem.getValue().length(), urlItem == null ? null : urlItem.getValue());
		StreamSource stream = null;		
		LogDTO logDTO = new LogDTO.LogDTOBuilder(LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.BUSINESS, this.getClass().getName())
			.description("Inside TrustExServiceActivator")
			.build();
		if (localDbItem != null) {
			stream = getStreamSourceFromLocalDb(localDbItem);
		} else if (urlItem != null) {
			try {
				
				URL schemaFile = new URL(urlItem.getValue());
				stream = new StreamSource(schemaFile.openStream());
				logger.debug("stream source system id[{}] public id[{}]", stream.getSystemId(), stream.getPublicId());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
				logService.saveLog(logDTO);
			}
		}
		return stream;
	}
	
	protected StreamSource getStreamSourceFromLocalDb(MetaDataItem localDbItem) {
		logger.debug("stream source localDbItem length [{}]", localDbItem == null ? null: localDbItem.getValue().length());
		StreamSource stream = null;
		if (localDbItem != null) {
			stream = new StreamSource(metadataService.getMetadataResourceAsStream(localDbItem.getId()));
			logger.debug("stream source system id[{}] public id[{}]", stream.getSystemId(), stream.getPublicId());
		}
		return stream;
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
	
	protected Schema getSchemaFromMetadata(MetaDataItem localDbItem, MetaDataItem urlItem, MetaDataItem serverURLItem) throws SAXException, MalformedURLException{
		logger.debug("schema source localDbItem length [{}] urlItem [{}]", localDbItem == null ? null: localDbItem.getValue().length(), urlItem == null ? null : urlItem.getValue());
		Schema schema = null;
		if (localDbItem != null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			schemaFactory.setResourceResolver(new ResourceToDatabaseResolver(xmlService));
			StreamSource source = new StreamSource(
					metadataService.getMetadataResourceAsStream(localDbItem
							.getId()));
			
			logger.debug("schema source system id[{}] public id[{}]", source.getSystemId(), source.getPublicId());
			schema = schemaFactory.newSchema(source);		
		} else if (urlItem != null) {
			if(urlItem.getValue().startsWith("http")){
				schema = getSchema(urlItem.getValue());
			}else if (urlItem.getValue().startsWith("/") && serverURLItem !=null){
				schema = getSchema(serverURLItem.getValue().concat(urlItem.getValue()));
			}			
		}
		return schema;
	}
	
	protected TrustExMessageHeader enrichMessageHeader(
			TrustExMessageHeader header) {
		// metadataService.retrieveMetaData(agrId, traID, documentTypeID)
		if (header.getTransactionTypeId() == null) {
			if (header.getTransactionNamespace() != null
					&& header.getTransactionRequestLocalName() != null) {
				Transaction t = authorisationService.getTransactionByNameSpace(
						header.getTransactionNamespace(),
						header.getTransactionRequestLocalName());
				if (t != null) {
					header.setTransactionTypeId(t.getId());
					header.setDocumentTypeId(t.getDocument().getId());
				}
			}
		}
		if (header.getMetadata() == null) {
			header.setMetadata(metadataService.retrieveMetaData(
					header.getInterchangeAgreementId(),
					header.getTransactionTypeId(), header.getDocumentTypeId(),
					null));
		}
		return header;
	}

	public IAuthorisationService getAuthorisationService() {
		return authorisationService;
	}

	public void setAuthorisationService(
			IAuthorisationService authorisationService) {
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

	public MessageChannel getErrorChannel() {
		return errorChannel;
	}

	public void setErrorChannel(MessageChannel errorChannel) {
		this.errorChannel = errorChannel;
	}

	public IInterchangeAgreementService getInterchangeAgreementService() {
		return interchangeAgreementService;
	}

	public void setInterchangeAgreementService(
			IInterchangeAgreementService interchangeAgreementService) {
		this.interchangeAgreementService = interchangeAgreementService;
	}
	
	private static Schema getSchema(String url) throws MalformedURLException, SAXException {
		if (schemasMap.containsKey(url)){
			return schemasMap.get(url);
		}else{
			//TODO use the IP:Port
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);				
			URL schemaFile = new URL(url);
			Schema sc = schemaFactory.newSchema(schemaFile);
			schemaFactory.setResourceResolver(new LSResourceResolver() {
				@Override
				public LSInput resolveResource(String type, String namespaceURI,
						String publicId, String systemId, String baseURI) {
//						System.out.println(type);
//						System.out.println(namespaceURI);
//						System.out.println(publicId);
//						System.out.println(systemId);
//						System.out.println(baseURI);
					throw new RuntimeException();
				}
			});
				schemasMap.put(url, sc);
				return schemasMap.get(url);
		}
	}
	
	protected BusinessDomain retrieveBusinessDomain(TrustExMessage<String> trustexMessage) {
		if (trustexMessage.getHeader().getIssuer() != null) {
			return trustexMessage.getHeader().getIssuer().getBusinessDomain(); 
		} else {
			return businessDomainService.retrieveBusinessDomain(trustexMessage.getHeader().getAuthenticatedUser());
		}
	}
}
