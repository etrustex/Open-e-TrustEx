package eu.europa.ec.etrustex.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.dao.dto.PolicySearchDTO;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.policy.CountSlaPolicy;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.policy.RetentionPolicy;
import eu.europa.ec.etrustex.domain.policy.SizeSlaPolicy;
import eu.europa.ec.etrustex.domain.policy.VolumeSlaPolicy;

public class PolicyDAOTest extends AbstractEtrustExTest {

	@Autowired private IPolicyDAO 			policyDao;
	@Autowired private ITransactionDAO 		txDao;
	@Autowired private IBusinessDomainDAO 	bdDao;
	@Autowired private IPartyDAO 			partyDao;
	
	@Test public void testFindRetentionPoliciesByCriteriaForView(){
		Assert.assertNull(policyDao.findRetentionPoliciesByCriteriaForView(null, null));
		
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("bd1");
		bd1.setResponseSignatureRequired(false);
		bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("bd2");
		bd2.setResponseSignatureRequired(false);
		bdDao.create(bd2);
		
		Transaction tx = new Transaction();
		tx.setName("tx-utest-1");
		txDao.create(tx);
		
		Transaction tx2 = new Transaction();
		tx2.setName("tx-utest-2");
		txDao.create(tx2);

		Policy p1 = new RetentionPolicy();
		p1.setActiveFlag(true);
		p1.setTransaction(tx);
		p1.setBusinessDomain(bd1);
		policyDao.create(p1);
		
		Policy p2 = new RetentionPolicy();
		p2.setActiveFlag(true);
		p2.setTransaction(tx);
		p2.setBusinessDomain(bd1);
		policyDao.create(p2);
		
		Policy p3 = new RetentionPolicy();
		p3.setActiveFlag(true);
		p3.setTransaction(tx2);
		p3.setBusinessDomain(bd2);
		policyDao.create(p3);
		
		Policy p4 = new VolumeSlaPolicy();
		p4.setActiveFlag(true);
		p4.setTransaction(tx2);
		p4.setBusinessDomain(bd2);
		policyDao.create(p4);
		
		RetentionPolicy criteria = new RetentionPolicy();
		criteria.setActiveFlag(true);
		criteria.setTransaction(tx);
		
		List<RetentionPolicy> policyList = policyDao.findRetentionPoliciesByCriteriaForView(criteria, new ArrayList<BusinessDomain>(){{add(bd1);}});
		Assert.assertEquals(2, policyList.size());
		
		policyList = policyDao.findRetentionPoliciesByCriteriaForView(criteria, new ArrayList<BusinessDomain>(){{add(bd2);}});
		Assert.assertEquals(0, policyList.size());
		
		criteria.setTransaction(tx2);
		policyList = policyDao.findRetentionPoliciesByCriteriaForView(criteria, new ArrayList<BusinessDomain>(){{add(bd1);}});
		Assert.assertEquals(0, policyList.size());
		
		policyList = policyDao.findRetentionPoliciesByCriteriaForView(criteria, new ArrayList<BusinessDomain>(){{add(bd2);}});
		Assert.assertEquals(1, policyList.size());
	} 
	
	@Test public void testGetPoliciesByCriteria(){
		BusinessDomain bd1 = new BusinessDomain();
		bd1.setName("bd1");
		bd1.setResponseSignatureRequired(false);
		bdDao.create(bd1);
		
		BusinessDomain bd2 = new BusinessDomain();
		bd2.setName("bd2");
		bd2.setResponseSignatureRequired(false);
		bdDao.create(bd2);
		
		Party party1 = new Party();
		party1.setName("P1");
		party1.setBusinessDomain(bd1);
		party1 = partyDao.create(party1);
		
		Party party2 = new Party();
		party2.setName("P2");
		party2.setBusinessDomain(bd2);
		party2 = partyDao.create(party2);
		
		Transaction tx = new Transaction();
		tx.setName("tx-utest-1");
		txDao.create(tx);
		
		Transaction tx2 = new Transaction();
		tx2.setName("tx-utest-2");
		txDao.create(tx2);
		
		Policy p1 = new RetentionPolicy();
		p1.setActiveFlag(true);
		p1.setTransaction(tx);
		p1.setBusinessDomain(bd1);
		policyDao.create(p1);
		
		Policy p2 = new RetentionPolicy();
		p2.setActiveFlag(true);
		p2.setTransaction(tx2);
		p2.setBusinessDomain(bd2);
		policyDao.create(p2);
		
		PolicySearchDTO policySearchDTO1 = new PolicySearchDTO();
		policySearchDTO1.setPolicyType(RetentionPolicy.class);
		policySearchDTO1.setTransaction(tx);
		policySearchDTO1.setSender(party1);
		
		PolicySearchDTO policySearchDTO2 = new PolicySearchDTO();
		policySearchDTO2.setPolicyType(RetentionPolicy.class);
		policySearchDTO2.setTransaction(tx2);
		policySearchDTO2.setSender(party2);
		
		List<Policy> pList = policyDao.getPoliciesByCriteria(policySearchDTO1);
		Assert.assertEquals(1, pList.size());
		Assert.assertEquals(p1.getId(), pList.get(0).getId());
		
		pList = policyDao.getPoliciesByCriteria(policySearchDTO2);
		Assert.assertEquals(1, pList.size());
		Assert.assertEquals(p2.getId(), pList.get(0).getId());
	} 
	
	@Test public void testGetPoliciesByType(){
		List<Policy> policyInitList = policyDao.getPoliciesByType(VolumeSlaPolicy.class);		
		policyDao.create(new VolumeSlaPolicy());		
		List<Policy> policyList = policyDao.getPoliciesByType(VolumeSlaPolicy.class);		
		Assert.assertEquals(1, policyList.size() - policyInitList.size());
		
		policyInitList = policyDao.getPoliciesByType(RetentionPolicy.class);
		policyDao.create(new RetentionPolicy());
		policyList = policyDao.getPoliciesByType(RetentionPolicy.class);
		Assert.assertEquals(1, policyList.size() - policyInitList.size());
		
		policyInitList = policyDao.getPoliciesByType(SizeSlaPolicy.class);
		policyDao.create(new SizeSlaPolicy());
		policyList = policyDao.getPoliciesByType(SizeSlaPolicy.class);
		Assert.assertEquals(1, policyList.size() - policyInitList.size());
		
		policyInitList = policyDao.getPoliciesByType(CountSlaPolicy.class);
		policyDao.create(new CountSlaPolicy());
		policyList = policyDao.getPoliciesByType(CountSlaPolicy.class);
		Assert.assertEquals(1, policyList.size() - policyInitList.size());
	}

}
