package eu.europa.ec.etrustex.integration.business;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ec.schema.xsd.createparty_2_1.CreatePartyType;
import ec.schema.xsd.eventnotification_1.EventType;
import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.util.Cipher;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.integration.api.ISynchBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.services.ICredentialsService;
import eu.europa.ec.etrustex.services.IEventNotificationService;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import eu.europa.ec.etrustex.services.IPartyIdentifierService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.services.util.EncryptionService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;

public class CreatePartyService_2_1 extends TrustExBusinessService implements ISynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(CreatePartyService_2_1.class);
	
	private static final String CREATE_PARTY_RESPONSE_OK = "<ec:Ack xmlns:ec='ec:services:wsdl:CreateParty-2_1' xmlns:ec1='ec:schema:xsd:CommonBasicComponents-1'><ec1:AckIndicator>true</ec1:AckIndicator></ec:Ack>";
	private static final String XPATH_PARTY_NAME = "//*:CreateParty/*:Party/*:PartyName/*:Name";
	private static final String XPATH_PERSON = "//*:CreateParty/*:Party/*:Person";
	private static final String XPATH_PARTY_IDENTIFIER = "//*:CreateParty/*:Party/*:PartyIdentification/*:ID";
	private static final String XPATH_CREDENTIALS = "//*:CreateParty/*:Party/*:PartyCredentials";
	private static final String XPATH_USERNAME = "//*:CreateParty/*:Party/*:PartyCredentials/*:Username";
	private static final String XPATH_PASSWORD = "//*:CreateParty/*:Party/*:PartyCredentials/*:Password";
	private static final String XPATH_PASSWORD_ENCRYPTED = "//*:CreateParty/*:Party/*:PartyCredentials/*:Password/@encrypted";
	private static final String XPATH_SIG_REQUIRED = "//*:CreateParty/*:Party/*:PartyCredentials/*:RequiredSignatureIndicator";
	private static final String XPATH_THIRD_PARTY = "//*:CreateParty/*:Party/*:ThirdPartyIndicator";
	private static final String XPATH_ENCRYPT_PASSWORD_IND = "//*:CreateParty/*:Party/*:PartyCredentials/*:EncryptedPasswordIndicator";
	private static final int MAX_CERTIFICATE_SIZE = 100 * 1024;
	
	private static JAXBContext jaxbContext = null;
	
	@Autowired
	private IPartyAgreementService partyAgreementService;
	@Autowired
	private IEventNotificationService eventNotificationService;
	
	@Autowired 
	@Qualifier("keyStore")
	private org.springframework.ws.soap.security.support.KeyStoreFactoryBean keyStore;		
	
	@Autowired
	private EncryptionService encryptionService;
	
	@Autowired
	private IPartyIdentifierService partyIdentifierService;
	
	@Autowired
	private ICredentialsService credentialsService;
	
	/**
	 * parses a CreateParty 2.1 request
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {

		try{
			logger.debug("Start processMessage");
			TrustExMessage<String> responseMessage = new TrustExMessage<String>(
					null);
			responseMessage.setHeader(message.getHeader());
			final Configuration config = new Configuration();
			DocumentInfo docInfo = null;
			try {
				docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			} catch (XPathException e) {
				logger.error("Not able to parse XML");
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
			
			
			CreatePartyType partyRequest = null;
			try {
				Source s = new StreamSource(new StringReader(message.getPayload()));
				JAXBElement<CreatePartyType> requestElement;
				requestElement = getJaxBContext().createUnmarshaller().unmarshal(s, CreatePartyType.class);
				partyRequest = requestElement.getValue();
			} catch (JAXBException e1) {
				logger.error("Not able to Build Request XML");
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
			
			
			String partyName = null;
			try {
				List<String> parties = queryFor(XPATH_PARTY_NAME,docInfo,config);
				if(parties.size() == 1){
					partyName = parties.get(0);
					logger.debug("Party name:"+partyName);
				}else{
					throw new MessageProcessingException("soapenv:Client", "No unique PartyName found", null,
                            ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.name.duplicate"));
				}				
			} catch (XPathException e) {
				logger.error("Not able to find party Id");
				throw new TechnicalErrorException("soapenv:Server",
						"Unable to retrieve information with query ", null,
						"301", "Technical Error Occured");
			}
			
			if(!StringUtils.isNotBlank(partyName)){
				logger.error("PartyName is Empty");
				throw new MessageProcessingException("soapenv:Client", "PartyName is Empty", null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
			
			if(partyName != null && partyName.trim().length() > 255){
				logger.error("PartyName exceeds 255 characters");
				throw new MessageProcessingException("soapenv:Client", "PartyName exceeds 255 characters", null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
			}
								
			Long issuerId = message.getHeader().getIssuerPartyId();
			logger.debug("Issuer id:"+issuerId);
			Party issuerParty = partyService.getParty(issuerId);
			logger.debug("Issuer Party:"+issuerParty);
			
			//checks first if party exists already otherwise creates a new one.
			Party dbParty = partyService.getPartyForBusinessDomain(partyName.trim(), issuerParty.getBusinessDomain().getId());
			if (dbParty != null){
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.identifier.duplicate"));
			}
			
			logger.debug("Create new party");
			Party party = new Party();
			party.setName(partyName);
			logger.debug("Create new party identifier");
			
			retrievePartyIdentifiers(docInfo, config, party, message);
			
			retrieveCredentials(docInfo, config, party, message);
			
			retrieveCertificate(party, message, partyRequest);
						
			party.setBusinessDomain(issuerParty.getBusinessDomain());
			party.setNaturalPersonFlag(queryForNode(XPATH_PERSON, docInfo, config).next() != null);
			party.setAccessInfo(createAccessInfo(message));
			
			if(party.getCredentials() != null && credentialsService.exists(party.getCredentials())){
				throw new MessageProcessingException("soapenv:Client", "Credentials username ("+ party.getCredentials().getUser() +") already exists.", null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.username.duplicate"));
			}
			
			for (PartyIdentifier pi : party.getIdentifiers()) {
				if(partyIdentifierService.getBySchemeAndValueInBusinessDomain(pi,party.getBusinessDomain().getId()) != null ){
					throw new MessageProcessingException("soapenv:Client", "Party identifier having type "+ pi.getSchemeId() +" and value "+ pi.getValue() +" already exists.", null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.identifier.duplicate"));
				}
			}
			
			partyService.createParty(party);	
			
			responseMessage.setPayload(CREATE_PARTY_RESPONSE_OK);
			
			sendNotification(issuerParty.getBusinessDomain().getId(), party);
			
			logger.debug(responseMessage.getPayload());
			logger.debug("Stop processMessage");			
			return responseMessage;
		} catch (MessageProcessingException mpe) {
			logger.error(mpe.getMessage(), mpe);
			throw mpe;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
					ErrorResponseCode.TECHNICAL_ERROR, null, null);
		}
	}
	
	private void sendNotification(Long businessDomainId, Party party) throws TechnicalErrorException {
		EventType eventType = eventNotificationService.getCreatePartyEventNotification(party);
		sendNotification(businessDomainId, eventType);
	}
	
	private void retrieveCertificate(Party party, TrustExMessage<String> message, CreatePartyType partyRequest) throws IOException, CertificateException, NoSuchAlgorithmException {

		if(partyRequest != null && partyRequest.getParty() != null && partyRequest.getParty().getX509Certificate() != null){
						
			String certString = new String(partyRequest.getParty().getX509Certificate(), StandardCharsets.UTF_8);			
			byte[] certB64 = Cipher.decodeFromBase64(certString.trim().replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll( "-----END CERTIFICATE-----", ""));
			if (certB64.length > MAX_CERTIFICATE_SIZE) {
				throw new MessageProcessingException("soapenv:Client", "Certificate too large", null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.certificate.size"));				
				
			}
			X509Certificate certificateFile = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(certB64));
			
			Certificate certificate = new Certificate();
			certificate.setAccessInfo(createAccessInfo(message));
			certificate.setIsActive(Boolean.TRUE);
			certificate.setIsRevoked(Boolean.FALSE);
			certificate.setUsage(USAGE_KEY_ENCIPHERMENT);
			certificate.setType(certificateFile.getType());
			certificate.setSerialNumber(hexify(certificateFile.getSerialNumber().toByteArray()));
			certificate.setHolder(certificateFile.getSubjectX500Principal().getName());
			certificate.setIssuer(certificateFile.getIssuerX500Principal().getName());
			certificate.setSignatureAlgorithm(certificateFile.getSigAlgName());
			certificate.setValidityStartDate(certificateFile.getNotBefore());
			certificate.setValidityEndDate(certificateFile.getNotAfter());
			certificate.setVersion("" + certificateFile.getVersion());
			certificate.setSignatureValue(getThumbPrint(certificateFile));
			certificate.setEncodedData(new String(Cipher.encodeToBase64(certificateFile.getEncoded())));
			certificate.setParty(party);
			
			certificate.setParty(party);
			party.getCertificates().add(certificate);			
		}
	}
	
	private void retrieveCredentials(DocumentInfo documentInfo, Configuration configuration, Party party, TrustExMessage<String> message) throws XPathException {
		Boolean thirdPartyFlag = false;				
		String third = queryForSingle(XPATH_THIRD_PARTY, documentInfo, configuration);
		
		if (third != null && "true".equalsIgnoreCase(third.trim())){
			thirdPartyFlag = true;
		}
		
		party.setThirdPartyFlag(thirdPartyFlag);
				
		final SequenceIterator<?> credIter = queryForNode(XPATH_CREDENTIALS, documentInfo, configuration);
		NodeInfo credentialsNode = (NodeInfo)credIter.next();

		if(thirdPartyFlag && credentialsNode == null){
			throw new MessageProcessingException("soapenv:Client", "Third party is set to true but no credentials available", null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.missing"));
		}
		
		if (credentialsNode == null) {
			return;
		}		

		//Getting the username
		String username = queryForSingle(XPATH_USERNAME, documentInfo, configuration);
		
		if(StringUtils.isEmpty(username)){
			logger.error("PartyCredentials is present but not the username");
			throw new MessageProcessingException("soapenv:Client", "PartyCredentials is present but not the username", null,
				ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.username.missing"));
		}		
	
		if(username.trim().length() > 255){
			logger.error("Username exceeds 255 characters");
			throw new MessageProcessingException("soapenv:Client", "Username exceeds 255 characters", null,
				ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.username.length"));
		}

					
		//Getting the password
		String password = queryForSingle(XPATH_PASSWORD, documentInfo, configuration);
		
		if(StringUtils.isEmpty(password)){
			logger.error("PartyCredentials is present but not the password");
			throw new MessageProcessingException("soapenv:Client", "PartyCredentials is present but not the password", null,
				ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.password.missing"));
		}

		String encrypted = queryForSingle(XPATH_PASSWORD_ENCRYPTED, documentInfo, configuration);			 
		if(StringUtils.isEmpty(encrypted) || !("true".equalsIgnoreCase(encrypted.trim()) || "false".equalsIgnoreCase(encrypted.trim()) )){
			logger.error("Password present but not the Encrypted Attribute");
			throw new MessageProcessingException("soapenv:Client", "Password present but not the Encrypted Attribute", null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, 
					soapErrorMessages.getProperty("error.hardrule.party.cred.password.encryptedAttr.missing"));
		}
		
		String reqSig = queryForSingle(XPATH_SIG_REQUIRED, documentInfo, configuration);
		
		String encPasswordInd = queryForSingle(XPATH_ENCRYPT_PASSWORD_IND, documentInfo, configuration);
		
		boolean saveEncrypted = true;
		if(!StringUtils.isEmpty(encPasswordInd) && "false".equalsIgnoreCase(encPasswordInd.trim())){
			saveEncrypted = false;
		}

		PartyCredentials cred = new PartyCredentials(); 
		cred.setUser(username);
		
		if("false".equalsIgnoreCase(encrypted.trim())){								
			
			String decodedPassword = null;
			try {
				decodedPassword = new String(Cipher.decodeFromBase64(password));
				if(!Charset.forName("US-ASCII").newEncoder().canEncode(decodedPassword)){
					throw new Exception();
				}
			} catch (Exception e) {
				logger.error("Problem decoding the password");
				throw new MessageProcessingException("soapenv:Client", "Problem decoding the password", null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, 
						soapErrorMessages.getProperty("error.hardrule.party.cred.password.encoding"));
			}
			
			if(decodedPassword.length() > 255){
				logger.error("Password exceeds 255 characters");
				throw new MessageProcessingException("soapenv:Client", "Password exceeds 255 characters", null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.password.length"));
			}
			
			savePassword(cred, decodedPassword, saveEncrypted);				
		}else{			
			cred.setPassword(null);					
			String decryptedValue = null;
			try{
				decryptedValue = encryptionService.dercryptRSA(password);
			}catch(BadPaddingException e){
					logger.error("Problem decrypting the Password");
					throw new MessageProcessingException("soapenv:Client", "Problem decrypting the Password", null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);		
				}
			catch(Exception e){
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}							
			
			if(decryptedValue.length() > 255){
				logger.error("Password exceeds 255 characters");
				throw new MessageProcessingException("soapenv:Client", "Password exceeds 255 characters", null, ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.cred.password.length"));
			}
			
			savePassword(cred,decryptedValue,saveEncrypted);	
		}
		
		if(!StringUtils.isEmpty(reqSig) && "true".equalsIgnoreCase(reqSig.trim())){
			cred.setSignatureRequired(true);
		}else{
			cred.setSignatureRequired(false);			
		}
		
		cred.setAccessInfo(createAccessInfo(message));
		//cred.setParty(party);
		party.setCredentials(cred);
		
	}
	
	private void retrievePartyIdentifiers(DocumentInfo documentInfo, Configuration configuration, Party party, TrustExMessage<String> message) throws XPathException {

		final SequenceIterator<?> iter = queryForNode(XPATH_PARTY_IDENTIFIER, documentInfo, configuration);

		NodeInfo node = (NodeInfo)iter.next();
		List<String> schemeIds = new ArrayList<>();
		Set<PartyIdentifier> identifiers = new HashSet<>();
		while (node != null) {
			if (StringUtils.isBlank(node.getStringValue())) {
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Party identifier is blank");
			}
			PartyIdentifier partyIdentifier = new PartyIdentifier();
			partyIdentifier.setParty(party);
			String schemeId = node.getAttributeValue("", "schemeID");
			//schemeID is GLN if empty 
			schemeId = StringUtils.isNotEmpty(schemeId) ? schemeId : "GLN";
			if (schemeIds.contains(schemeId)) {
				//no two identifiers should have the same schemeID
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.identifier.noPerScheme"));
			}
			schemeIds.add(schemeId);
			try {
				partyIdentifier.setSchemeId(IdentifierIssuingAgency.valueOf(schemeId.replaceFirst(":", "_")));
			} catch (IllegalArgumentException e) {
				//schemeID is not valid
				if (e.getMessage().startsWith("No enum constant")) {
					throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
							ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, soapErrorMessages.getProperty("error.hardrule.party.identifier.scheme"));
				}
			}
			partyIdentifier.setAccessInfo(createAccessInfo(message));
			partyIdentifier.setValue(node.getStringValue());
			identifiers.add(partyIdentifier);
			if(partyIdentifier.getSchemeId().getSchemeID().length() > 255 || partyIdentifier.getValue().length() > 255){
				logger.error("PartyIdentification exceeds 255 characters");
				throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, "Party identifier exceeds 255 characters");
			}
			node = (NodeInfo)iter.next();
		}
		if (CollectionUtils.isEmpty(identifiers)) {
			logger.error("No party identifiers found in request");
			throw new MessageProcessingException("soapenv:Client", ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
					ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, null, null);
		}
		party.setIdentifiers(identifiers);
	}

	public IPartyAgreementService getPartyAgreementService() {
		return partyAgreementService;
	}

	public void setPartyAgreementService(IPartyAgreementService partyAgreementService) {
		this.partyAgreementService = partyAgreementService;
	}
	
	public IPartyService getPartyService() {
		return partyService;
	}

	public void setPartyService(IPartyService partyService) {
		this.partyService = partyService;
	}
	
	private static JAXBContext getJaxBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(CreatePartyType.class);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MessageProcessingException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null,
						ErrorResponseCode.TECHNICAL_ERROR, null, null);
			}
		}
		return jaxbContext;
	}
	
	private void savePassword(PartyCredentials cred, String password, boolean encrypted){
		cred.setSalt(encrypted);
		cred.setPasswordEncrypted(encrypted);
		if(encrypted){			
			cred.setPassword(encryptionService.encryptPasswordByBCrypt(password));
		}else{
			cred.setPassword(password);
		}
	}
	
	private String getThumbPrint(X509Certificate cert) throws NoSuchAlgorithmException, CertificateEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] der = cert.getEncoded();
		md.update(der);
		byte[] digest = md.digest();
		return hexify(digest);
		
	}
	
	private String hexify(byte bytes[]) {
		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		
		for (int i = 0; i < bytes.length; ++i) {
			buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
			buf.append(hexDigits[bytes[i] & 0x0f]);
			buf.append(" ");
		}
		
		return buf.toString();
	}

	public IPartyIdentifierService getPartyIdentifierService() {
		return partyIdentifierService;
	}

	public void setPartyIdentifierService(IPartyIdentifierService partyIdentifierService) {
		this.partyIdentifierService = partyIdentifierService;
	}

	public ICredentialsService getCredentialsService() {
		return credentialsService;
	}

	public void setCredentialsService(ICredentialsService credentialsService) {
		this.credentialsService = credentialsService;
	}
}
