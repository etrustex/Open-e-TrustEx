package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import eu.europa.ec.etrustex.dao.IBusinessDomainDAO;
import eu.europa.ec.etrustex.dao.ICIALevelDAO;
import eu.europa.ec.etrustex.dao.IEventNotificationDAO;
import eu.europa.ec.etrustex.dao.IProfileDAO;
import eu.europa.ec.etrustex.dao.ITransactionDAO;
import eu.europa.ec.etrustex.domain.CIALevel;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.routing.EventNotification;

public class ProfileServiceTest extends AbstractEtrustExTest {
	
		
	@Mock private IProfileDAO 	profileDao;
	@Mock private IBusinessDomainDAO businessDomainDAO;	
	@Mock private ICIALevelDAO ciaLevelDAO;	
	@Mock private IEventNotificationDAO eventNotificationDAO;
	@Mock private ITransactionDAO transactionDAO;
	
	@Autowired private IProfileService profileService;
	
	@BeforeClass public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("ProfileService");
		System.out.println("--------------------------------------------------");		
	}
	
	@Before public void init2(){
		//Injection doesn't work transactional service through @InjectMocks se we have to do it through reflection 
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(profileService, "profileDao", profileDao);
		ReflectionTestUtils.setField(profileService, "businessDomainDAO", businessDomainDAO);
		ReflectionTestUtils.setField(profileService, "ciaLevelDAO", ciaLevelDAO);
		ReflectionTestUtils.setField(profileService, "eventNotificationDAO", eventNotificationDAO);
		ReflectionTestUtils.setField(profileService, "transactionDAO", transactionDAO);
	}
	
	@Test public void testIsInUse(){
		Long id = -666L;
		Profile profile = new Profile();
		profile.setId(id);
		
		when(profileDao.isInUse(profile)).thenReturn(false);
		assertFalse(profileService.isInUse(profile));
		
		when(profileDao.isInUse(profile)).thenReturn(true);
		assertTrue(profileService.isInUse(profile));
	}

	@Test public void testGetProfile(){			
		Long id = -666L;
		Profile profile = new Profile();
		profile.setId(id);
		profile.getTransactions().add(new Transaction());
		BusinessDomain bd = new BusinessDomain();
		bd.getProfiles().add(profile);
		profile.getBusinessDomains().add(bd);
		
		when(profileDao.getProfile(id)).thenReturn(profile);
		
		Profile p = profileService.getProfile(id);
		assertTrue(p.equals(profile));
	}
	
	@Test public void testFindByName(){
		Long id = -666L;
		Profile profile = new Profile();
		profile.setId(id);
		profile.setName("myName");
		
		when(profileDao.findByName("myName")).thenReturn(profile);
		
		assertEquals(profile, profileService.findByName("myName"));
	}
	
	@Test public void  testCreateUpdate(){
		CIALevel ciaLevel = new CIALevel();             
		ciaLevel.setConfidentialityLevel(1);             
		ciaLevel.setIntegrityLevel(1);             
		ciaLevel.setAvailabilityLevel(1);
		
		List<CIALevel> list = new ArrayList<CIALevel>();
		list.add(ciaLevel);
		
		Profile profile1 = new Profile();
		profile1.setId(1L);
		profile1.setCiaLevel(ciaLevel);
		Profile profile2 = new Profile();
		profile2.setId(2L);
		profile2.setCiaLevel(ciaLevel);
		
		when(profileDao.create(profile1)).thenReturn(profile2);
		when(profileDao.update(profile1)).thenReturn(profile2);
		when(ciaLevelDAO.retrieveCIALevel(1,1,1)).thenReturn(list);
		assertEquals(profile2,profileService.create(profile1));
		assertEquals(profile2,profileService.update(profile1));
		
		when(ciaLevelDAO.retrieveCIALevel(1,1,1)).thenReturn(new ArrayList<CIALevel>());
		when(ciaLevelDAO.create(ciaLevel)).thenReturn(ciaLevel);
		assertEquals(profile2,profileService.create(profile1));
		assertEquals(profile2,profileService.update(profile1));
	}
	
	@Test public void  testFindProfilesByCriteria(){
		BusinessDomain bd = new BusinessDomain(); 
		
		List<Profile> profiles = new ArrayList<Profile>();
		profiles.add(new Profile());
		profiles.add(new Profile());
		profiles.add(new Profile());
		
		when(profileDao.findProfilesByCriteria("p1", "t1", bd)).thenReturn(profiles);
		
		List<Profile> pList = profileService.findProfilesByCriteria("p1", "t1", bd);
		
		assertEquals(3, pList.size());
		assertEquals(profiles, pList);
		
		verify(profileDao, times(1)).findProfilesByCriteria("p1", "t1", bd);
	}
	
	@Test public void  testDelete(){
		Long id = 45L;
		Profile profile = new Profile();
		
		BusinessDomain    bd = new BusinessDomain();
		Transaction    	  tx = new Transaction();
		EventNotification en = new EventNotification();
		bd.getProfiles().add(profile);
		tx.getProfiles().add(profile);
		en.setProfile(profile);
		List<EventNotification> enList = new ArrayList<EventNotification>();
		enList.add(en);
		
		profile.getBusinessDomains().add(bd);
		profile.getTransactions().add(tx);
		
		when(profileDao.getProfile(id)).thenReturn(profile);
		when(eventNotificationDAO.findByProfile(profile)).thenReturn(enList);
		
		profileService.delete(id);
		
		verify(businessDomainDAO	, times(1)).update(bd);
		verify(transactionDAO		, times(1)).update(tx);
		verify(eventNotificationDAO	, times(1)).delete(en);
		verify(profileDao			, times(1)).delete(profile);
	}
}
