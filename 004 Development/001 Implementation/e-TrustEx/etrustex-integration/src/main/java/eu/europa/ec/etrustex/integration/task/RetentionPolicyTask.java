package eu.europa.ec.etrustex.integration.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RetentionPolicyTask extends AbstractJob {

	@Autowired
	private IPolicyService policyService;

	private static final Logger logger = LoggerFactory
			.getLogger(RetentionPolicyTask.class);

	@Override
	public void run() {
		logger.info("start RetentionPolicyTask ");
		try {
			policyService.applyRetentionPolicies();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("stop RetentionPolicyTask ");
	}

	public IPolicyService getPolicyService() {
		return policyService;
	}

	public void setPolicyService(IPolicyService policyService) {
		this.policyService = policyService;
	}

}
