package eu.europa.ec.etrustex.services;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.ICredentialsDAO;
import eu.europa.ec.etrustex.dao.IInterchangeAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyAgreementDAO;
import eu.europa.ec.etrustex.dao.IPartyDAO;
import eu.europa.ec.etrustex.dao.IServiceEndpointDao;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.dao.exception.AuthorisationFailedException;
import eu.europa.ec.etrustex.dao.exception.UndefinedIdentifierException;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyRole;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgencyManager;
import eu.europa.ec.etrustex.types.MetaDataItemType;

public class AuthorisationServiceTest extends AbstractEtrustExTest{
	
	@Autowired private IAuthorisationService authorisationService;
	@Mock private IPartyDAO partyDAO;
	@Mock private ITransactionDAO transactionDAO;
	@Mock private IInterchangeAgreementDAO interchangeAgreementDAO;
	@Mock private IPartyAgreementDAO partyAgreementDAO;
	@Mock private ICredentialsDAO credentialsDAO;
	@Mock private IMetadataService metadataService;
	@Mock private IServiceEndpointDao serviceEndpointDAO;

	@Before public void init() {
		MockitoAnnotations.initMocks(this);

		ReflectionTestUtils.setField(authorisationService, "partyDAO", partyDAO);
		ReflectionTestUtils.setField(authorisationService, "transactionDAO", transactionDAO);
		ReflectionTestUtils.setField(authorisationService, "interchangeAgreementDAO", interchangeAgreementDAO);
		ReflectionTestUtils.setField(authorisationService, "partyAgreementDAO", partyAgreementDAO);
		ReflectionTestUtils.setField(authorisationService, "credentialsDAO", credentialsDAO);
		ReflectionTestUtils.setField(authorisationService, "metadataService", metadataService);
		ReflectionTestUtils.setField(authorisationService, "serviceEndpointDAO", serviceEndpointDAO);
	}

	@Test public void testCheckAuthorisation(){
		Party issuerParty = new Party();
		issuerParty.setId(1L);
		Party senderParty = new Party();
		senderParty.setId(2L);
		senderParty.setName("name");
		Long transactionId = 1L;
		Role role = new Role();
		role.setCode("ANY");
		role.setName("myName");
		Transaction tx = new Transaction();
		tx.setId(transactionId);
		tx.setName("name");
		tx.setSenderRole(role);
		tx.setReceiverRole(role);
		Party receiverParty = new Party();
		receiverParty.setId(3L);
		receiverParty.setName("name2");
		Long profileId = 1L;
		Profile profile = new Profile();
		profile.setId(profileId);
		
		//Testing tx not in profile
		try {
			authorisationService.checkAuthorisation(issuerParty, senderParty, receiverParty, transactionId, profile);
			fail("Should throw an exception as the Transaction is not in the profile");
		} catch (AuthorisationFailedException e) {}
		
		//Testing no PA for transaction
		profile.getTransactions().add(tx);
		when(partyAgreementDAO.existsPartyAgreementForTransaction(senderParty, issuerParty, transactionId)).thenReturn(false);
		try {
			authorisationService.checkAuthorisation(issuerParty, senderParty, receiverParty, transactionId, profile);
			fail("Should throw an exception Since there is no PartyAGreement");
		} catch (AuthorisationFailedException e) {}
		
		//Testing Empty ICA List
		when(partyAgreementDAO.existsPartyAgreementForTransaction(senderParty, issuerParty, transactionId)).thenReturn(true);
		when(interchangeAgreementDAO.getInterchangeAgreements(senderParty, receiverParty, transactionId, profileId)).thenReturn(Arrays.asList());
		try {
			authorisationService.checkAuthorisation(issuerParty, senderParty, receiverParty, transactionId, profile);
			fail("Should throw an exception Since there is no ICA");
		} catch (AuthorisationFailedException e) {}
		
		when(interchangeAgreementDAO.getInterchangeAgreements(senderParty, receiverParty, transactionId, profileId)).thenReturn(Arrays.asList(new InterchangeAgreement()));
		try {
			authorisationService.checkAuthorisation(issuerParty, senderParty, receiverParty, transactionId, profile);
			fail("Should throw an exception Since there is no ICA");
		} catch (AuthorisationFailedException e) {}
		
		//Testing the case of valid ICA
		PartyRole pr1 = new PartyRole();
		pr1.setParty(senderParty);
		pr1.setRole(role);
		PartyRole pr2 = new PartyRole();
		pr2.setParty(receiverParty);
		pr2.setRole(role);
		InterchangeAgreement ica = new InterchangeAgreement();
		ica.getPartyRoles().add(pr1);
		ica.getPartyRoles().add(pr2);
		ica.setProfile(profile);
		when(interchangeAgreementDAO.getInterchangeAgreements(senderParty, receiverParty, transactionId, profileId)).thenReturn(Arrays.asList(ica));
		try {
			authorisationService.checkAuthorisation(issuerParty, senderParty, receiverParty, transactionId, profile);
		} catch (AuthorisationFailedException e) {fail("Should Not throws an Exception");}
	}
	
	@Test public void testCheckTransactionAuthorisation(){
		Party issuerParty = new Party();
		issuerParty.setId(1L);
		Party senderParty = new Party();
		senderParty.setId(2L);
		Long transactionId = 1L;
		
		when(partyAgreementDAO.existsPartyAgreementForTransaction(senderParty, issuerParty, transactionId)).thenReturn(false);
		try {
			authorisationService.checkTransactionAuthorisation(issuerParty, senderParty, transactionId);
			fail("Should fail if not authorised");
		} catch (AuthorisationFailedException e) {}
		
		when(partyAgreementDAO.existsPartyAgreementForTransaction(senderParty, issuerParty, transactionId)).thenReturn(true);
		when(interchangeAgreementDAO.getInterchangeAgreements(senderParty, null, transactionId, null)).thenReturn(null);
		try {
			authorisationService.checkTransactionAuthorisation(issuerParty, senderParty, transactionId);
			fail("Should fail if not agreement is returned");
		} catch (AuthorisationFailedException e) {}
		
		when(interchangeAgreementDAO.getInterchangeAgreements(senderParty, null, transactionId, null)).thenReturn(Arrays.asList(new InterchangeAgreement()));
		try {
			authorisationService.checkTransactionAuthorisation(issuerParty, senderParty, transactionId);
		} catch (AuthorisationFailedException e) {fail("Should not throw an exception");}
	}
	
	@Test public void testRetrievePartyAgreement(){
		Party authorizingParty =  new Party();
		Party delegateParty = new Party();
		authorisationService.retrievePartyAgreement(authorizingParty, delegateParty);
		verify(partyAgreementDAO, times(1)).retrievePartyAgreement(authorizingParty, delegateParty);
	}
	
	@Test public void testCheckPartyAgreementForTransaction(){
		Party authorizingParty =  new Party();
		Party delegateParty = new Party(); 
		Long transactionId = 1L;
		authorisationService.checkPartyAgreementForTransaction(authorizingParty, delegateParty, transactionId);
		verify(partyAgreementDAO, times(1)).existsPartyAgreementForTransaction(authorizingParty, delegateParty, transactionId);		
	}
	
	@Test public void testGetMessageIssuer(){}
	
	@Test public void testGetParty(){
		String partyIdWithScheme = "d";
		BusinessDomain bd = new BusinessDomain();
		
		try {
			authorisationService.getParty(null, bd);
			fail("An exception should be thrown");
		} catch (UndefinedIdentifierException e) {}
		
		// Testing # metadata & GLN
		Map<MetaDataItemType, MetaDataItem> metadata = new HashMap<MetaDataItemType, MetaDataItem>();
		MetaDataItem mdi = new MetaDataItem();
		mdi.setValue("#");
		metadata.put(MetaDataItemType.SCHEME_ID_SEPARATOR, mdi);
		when(metadataService.retrieveMetaData((Long) null, null, null, null, null)).thenReturn(new HashMap<MetaDataItemType, MetaDataItem>());
		when(partyDAO.getParty(IdentifierIssuingAgencyManager.getAgencyOfSchemeID(IdentifierIssuingAgency.GLN.getSchemeID()), partyIdWithScheme, bd)).thenReturn(new Party());
		try {
			authorisationService.getParty(partyIdWithScheme, bd);
		} catch (UndefinedIdentifierException e) {fail("Should pass");}
		
		//Testing : Metadata + VAT
		partyIdWithScheme = IdentifierIssuingAgency.VA_VAT.getSchemeID()+"#d";
		when(metadataService.retrieveMetaData((Long) null, null, null, null, null)).thenReturn(metadata);
		when(partyDAO.getParty(IdentifierIssuingAgencyManager.getAgencyOfSchemeID(IdentifierIssuingAgency.VA_VAT.getSchemeID()), "d", bd)).thenReturn(new Party());
		try {
			authorisationService.getParty(partyIdWithScheme, bd);
		} catch (UndefinedIdentifierException e) {fail("Should pass");}
	}
	
	@Test public void testGetTransactionByNameSpace(){}
	
	@Test public void testGetTransactionById(){
		Long tansactionId = 1L;
		authorisationService.getTransactionById(tansactionId);
		verify(transactionDAO, times(1)).read(tansactionId);
	}
	
	@Test public void testGetTransactionsByDocumentTypeCd(){
		String documentTypeCode = "dtc";
		authorisationService.getTransactionsByDocumentTypeCd(documentTypeCode);
		verify(transactionDAO, times(1)).getTransactionsByDocumentTypeCd(documentTypeCode);
	}
	
	@Test public void testGetTransactionsForDocument(){
		String documentNamespace = "ns";
		String documentlocalName = "ln";		
		authorisationService.getTransactionsForDocument(documentNamespace, documentlocalName);
		verify(transactionDAO, times(1)).getTransactionsForDocument(documentNamespace, documentlocalName);
	}
	
	@Test public void testIsSignatureRequired(){}
	@Test public void testIsTransactionAllowed(){}
	
}
