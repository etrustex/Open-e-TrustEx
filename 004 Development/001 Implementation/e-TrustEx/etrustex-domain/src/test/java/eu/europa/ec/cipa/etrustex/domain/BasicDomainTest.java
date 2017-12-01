package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.types.IdentifierIssuingAgency;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class BasicDomainTest {
	  private  EntityManagerFactory factory;
	  
	  @Before
	  public void initTest () throws Exception {
		  factory = Persistence.createEntityManagerFactory("etrustex-test");
	  }
	  
	  @Test
	  public void createPartyTest() throws Exception {
		  EntityManager entityManager = factory.createEntityManager();
		  entityManager.getTransaction().begin();
		  EntityAccessInfo info = new EntityAccessInfo();
		  info.setCreationDate(Calendar.getInstance().getTime());
		  info.setModificationDate(Calendar.getInstance().getTime());
		  info.setCreationId("orazisa");
		  info.setModificationId("orazisa");
		  Party party = new Party();
		  party.setAccessInfo(info);
		  party.setName("Test Party One");
		  Credentials cred = new PartyCredentials();
		  cred.setUser("TestUserOne");
		  String password = "TestUserOne";
		  MessageDigest digest = MessageDigest.getInstance("SHA-1");
		  byte[] hash = digest.digest(password.getBytes());		  
		  cred.setPassword(new String(Base64.encode(hash)));
		  cred.setAccessInfo(info);
		  party.setCredentials((PartyCredentials)cred);
		  Set<PartyIdentifier> identifiers = new HashSet<PartyIdentifier>();
		  PartyIdentifier id = new PartyIdentifier();
		  id.setSchemeId(IdentifierIssuingAgency.GLN);
		  id.setValue("000886688001");
		  id.setAccessInfo(info);
		  id.setParty(party);
		  identifiers.add(id); 
		  party.setIdentifiers(identifiers);
		  entityManager.persist(party);
		  
		  party = new Party();
		  party.setAccessInfo(info);
		  party.setName("Test Party Two");
		  cred = new PartyCredentials();
		  cred.setUser("TestUserTwo");
		  password = "TestUserTwo";
		  hash = digest.digest(password.getBytes());
		  cred.setPassword(new String (Base64.encode(hash)));
		  cred.setAccessInfo(info);
		  party.setCredentials((PartyCredentials)cred);
		  identifiers = new HashSet<PartyIdentifier>();
		  id = new PartyIdentifier();
		  id.setSchemeId(IdentifierIssuingAgency.GLN);
		  id.setValue("000886688002");
		  id.setAccessInfo(info);
		  id.setParty(party);
		  identifiers.add(id); 
		  party.setIdentifiers(identifiers);
		  entityManager.persist(party);
		  
		  entityManager.getTransaction().commit();
		  entityManager.close();
		  
	  }
	  @Test
	  public void createRoleTest() throws Exception{
		  EntityManager entityManager = factory.createEntityManager();
		  entityManager.getTransaction().begin(); 
		  EntityAccessInfo info = new EntityAccessInfo();
		  info.setCreationDate(Calendar.getInstance().getTime());
		  info.setModificationDate(Calendar.getInstance().getTime());
		  info.setCreationId("orazisa");
		  info.setModificationId("orazisa");

		  Role rol= new Role();
		  rol.setName("Supplier");
		  rol.setCode("SUP");
		  rol.setAccessInfo(info);
		  entityManager.persist(rol);

		  Role customer= new Role();
		  customer.setName("Customer");
		  customer.setCode("CUST");
		  customer.setAccessInfo(info);
		  entityManager.persist(customer);

		  Role any= new Role();
		  any.setName("Any");
		  any.setCode("ANY");
		  any.setAccessInfo(info);
		  entityManager.persist(any);

		  entityManager.getTransaction().commit();
		  entityManager.close();
	  }
	  @Test 
	  public void assignRoleTest() throws Exception{
		  EntityManager entityManager = factory.createEntityManager();
		  entityManager.getTransaction().begin(); 
		  EntityAccessInfo info = new EntityAccessInfo();
		  info.setCreationDate(Calendar.getInstance().getTime());
		  info.setModificationDate(Calendar.getInstance().getTime());
		  info.setCreationId("orazisa");
		  info.setModificationId("orazisa");
		  Party custParty = entityManager.find(Party.class, new Long(1));
		  Role custRole = (Role) entityManager.createQuery("from Role r where r.code='CUST'").getSingleResult();
		  PartyRole prCust = new PartyRole();
		  prCust.setParty(custParty);
		  prCust.setRole(custRole);
		  prCust.setAccessInfo(info);
		  entityManager.persist(prCust);
		  
		  Party supParty = entityManager.find(Party.class, new Long(2));
		  Role supRole = (Role) entityManager.createQuery("from Role r where r.code='SUP'").getSingleResult();
		  PartyRole prSup = new PartyRole();
		  prSup.setParty(supParty);
		  prSup.setRole(supRole);
		  prSup.setAccessInfo(info);
		  entityManager.persist( prSup);
		  entityManager.getTransaction().commit();
		  entityManager.close();
	  }
	  
	  @Test 
	  public void createProfileTest() throws Exception{
		  EntityManager entityManager = factory.createEntityManager();
		  entityManager.getTransaction().begin(); 
		  EntityAccessInfo info = new EntityAccessInfo();
		  info.setCreationDate(Calendar.getInstance().getTime());
		  info.setModificationDate(Calendar.getInstance().getTime());
		  info.setCreationId("orazisa");
		  info.setModificationId("orazisa");
		 
		  Document invoice = new Document();
		  invoice.setLocalName("Invoice");
		  invoice.setName("Invoice");
		  invoice.setNamespace("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
		  invoice.setVersion("2.0");
		  invoice.setDocumentTypeCode("380");
		  invoice.setAccessInfo(info);
		  entityManager.persist(invoice);
		  
		  Document creditNote = new Document();
		  creditNote.setLocalName("CreditNote");
		  creditNote.setName("CreditNote");
		  creditNote.setNamespace("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2");
		  creditNote.setAccessInfo(info);
		  creditNote.setVersion("2.0");
		  invoice.setDocumentTypeCode("390");
		  entityManager.persist(creditNote);
		  
		  Transaction submitInvoice = new Transaction();
		  submitInvoice.setName("SubmitInvoice");
		  submitInvoice.setNamespace("ec:services:wsdl:Invoice-0.1");
		  submitInvoice.setVersion("0.1");
		  submitInvoice.setRequestLocalName("SubmitInvoiceRequest");
		  submitInvoice.setResponseLocalName("SubmitInvoiceResponse");
		  submitInvoice.setSenderRole((Role) entityManager.createQuery("from Role r where r.code='SUP'").getSingleResult());
		  submitInvoice.setReceiverRole((Role) entityManager.createQuery("from Role r where r.code='CUST'").getSingleResult());
		  submitInvoice.setDocument(invoice);
		  submitInvoice.setAccessInfo(info);
		  entityManager.persist(submitInvoice);
		  
		  Transaction submitCreditNote = new Transaction();
		  submitCreditNote.setName("SubmitCreditNote");
		  submitCreditNote.setNamespace("ec:services:wsdl:CreditNote-0.1");
		  submitCreditNote.setVersion("0.1");
		  submitCreditNote.setRequestLocalName("SubmitCreditNoteRequest");
		  submitCreditNote.setResponseLocalName("SubmitCreditNoteResponse");
		  submitCreditNote.setSenderRole((Role) entityManager.createQuery("from Role r where r.code='SUP'").getSingleResult());
		  submitCreditNote.setReceiverRole((Role) entityManager.createQuery("from Role r where r.code='CUST'").getSingleResult());
		  submitCreditNote.setDocument(creditNote);
		  submitCreditNote.setAccessInfo(info);
		  entityManager.persist(submitCreditNote);
		  
		  Profile invoiceOnly = new Profile();
		  invoiceOnly.setName("Invoice Only");
		  invoiceOnly.setNamespace("BII-Profile-04-InvoiceOnly");
		  Set<Transaction> invoiceOnlyTrans = new HashSet<Transaction>();
		  invoiceOnlyTrans.add(submitInvoice);
		  invoiceOnly.setTransactions(invoiceOnlyTrans);
		  entityManager.persist(invoiceOnly);
		  
		  Profile billing = new Profile();
		  billing.setName("Billing");
		  billing.setNamespace("BII-Profile-05-Billing");
		  Set<Transaction> billingTrans = new HashSet<Transaction>();
		  billingTrans.add(submitInvoice);
		  billingTrans.add(submitCreditNote);
		  billing.setTransactions(billingTrans);
		  entityManager.persist(billing);
		  
		 // invoiceOnly.set
		  
		  entityManager.getTransaction().commit();
		  entityManager.close();
	  }
	  
	  @Test 
	  public void createInterchangeAgreement() throws Exception{
		  EntityManager entityManager = factory.createEntityManager();
		  entityManager.getTransaction().begin(); 
		  InterchangeAgreement agreement = new InterchangeAgreement();
		  Profile invoiceOnly =  (Profile)entityManager.createQuery("from Profile p where p.name='Billing'").getSingleResult();
		  agreement.setProfile(invoiceOnly);
		  Set<PartyRole> roles = new HashSet<PartyRole>();
		  roles.add( entityManager.find(PartyRole.class, new Long(1)));
		  roles.add( entityManager.find(PartyRole.class, new Long(2)));
		  agreement.setPartyRoles(roles);
		  entityManager.persist(agreement);
		  entityManager.getTransaction().commit();
		  entityManager.close();
		  
	  }
}
