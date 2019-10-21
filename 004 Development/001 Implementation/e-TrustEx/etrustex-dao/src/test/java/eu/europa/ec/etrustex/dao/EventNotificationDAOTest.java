package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.EventNotification;
import eu.europa.ec.etrustex.types.EventNotificationType;

public class EventNotificationDAOTest extends AbstractEtrustExTest {

	@Autowired private IEventNotificationDAO enDao;
	@Autowired private IPartyDAO 			 partyDao;
	@Autowired private IBusinessDomainDAO 	 bdDao;
	@Autowired private IProfileDAO 	 		 profileDao;
	
	@Test public void testfindByParty(){
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("BD1");
		bd1.setResponseSignatureRequired(Boolean.TRUE);
		bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("BD2");
		bd2.setResponseSignatureRequired(Boolean.TRUE);
		bdDao.create(bd2);
		
		Profile p = new Profile();
		p.setName("P1");
		profileDao.create(p);
		
		Party party1 = new Party();
		party1.setName("UTEST_PARTY_1");
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("UTEST_PARTY_2");
		party2 = partyDao.create(party2);
		
		Party party3 = new Party();
		party3.setName("UTEST_PARTY_3");
		party3 = partyDao.create(party3);
		
		EventNotification en1 = new EventNotification();
		en1.setEventType(EventNotificationType.CFG);
		en1.setBusinessDomain(bd1);
		en1.setProfile(p);
		en1.setParty(party1);
		enDao.create(en1);
		
		EventNotification en2 = new EventNotification();
		en2.setEventType(EventNotificationType.DOC);
		en2.setBusinessDomain(bd1);
		en2.setProfile(p);
		en2.setParty(party1);
		enDao.create(en2);
		
		EventNotification en3 = new EventNotification();
		en3.setEventType(EventNotificationType.CFG);
		en3.setBusinessDomain(bd2);
		en3.setProfile(p);
		en3.setParty(party1);
		enDao.create(en3);
		
		EventNotification en4 = new EventNotification();
		en4.setEventType(EventNotificationType.DOC);
		en4.setBusinessDomain(bd1);
		en4.setProfile(p);
		en4.setParty(party3);
		enDao.create(en4);
		
		List<EventNotification> enList = enDao.findByParty(party1);
		Assert.assertEquals(3, enList.size());
		
		enList = enDao.findByParty(party2);
		Assert.assertEquals(0, enList.size());
		
		enList = enDao.findByParty(party3);
		Assert.assertEquals(1, enList.size());
	}
	
	@Test public void testfindByProfile(){
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("BD1");
		bd1.setResponseSignatureRequired(Boolean.TRUE);
		bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("BD2");
		bd2.setResponseSignatureRequired(Boolean.TRUE);
		bdDao.create(bd2);
		
		Profile p1 = new Profile();
		p1.setName("P1");
		profileDao.create(p1);
		
		Profile p2 = new Profile();
		p2.setName("P2");
		profileDao.create(p2);
		
		Profile p3 = new Profile();
		p3.setName("P3");
		profileDao.create(p3);
		
		Party party1 = new Party();
		party1.setName("UTEST_PARTY_1");
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("UTEST_PARTY_2");
		party2 = partyDao.create(party2);
		
		Party party3 = new Party();
		party3.setName("UTEST_PARTY_3");
		party3 = partyDao.create(party3);
		
		EventNotification en1 = new EventNotification();
		en1.setEventType(EventNotificationType.CFG);
		en1.setBusinessDomain(bd1);
		en1.setProfile(p1);
		en1.setParty(party1);
		enDao.create(en1);
		
		EventNotification en2 = new EventNotification();
		en2.setEventType(EventNotificationType.DOC);
		en2.setBusinessDomain(bd1);
		en2.setProfile(p1);
		en2.setParty(party1);
		enDao.create(en2);
		
		EventNotification en3 = new EventNotification();
		en3.setEventType(EventNotificationType.CFG);
		en3.setBusinessDomain(bd2);
		en3.setProfile(p1);
		en3.setParty(party1);
		enDao.create(en3);
		
		EventNotification en4 = new EventNotification();
		en4.setEventType(EventNotificationType.DOC);
		en4.setBusinessDomain(bd1);
		en4.setProfile(p2);
		en4.setParty(party3);
		enDao.create(en4);
		
		List<EventNotification> enList = enDao.findByProfile(p1);
		Assert.assertEquals(3, enList.size());
		
		enList = enDao.findByProfile(p2);
		Assert.assertEquals(1, enList.size());
		
		enList = enDao.findByProfile(p3);
		Assert.assertEquals(0, enList.size());
	}
}
