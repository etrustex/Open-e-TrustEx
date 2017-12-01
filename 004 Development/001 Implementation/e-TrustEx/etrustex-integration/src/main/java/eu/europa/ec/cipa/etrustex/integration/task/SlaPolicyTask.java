package eu.europa.ec.cipa.etrustex.integration.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SlaPolicyTask {

	@Autowired
	private ISlaPolicyService slaPolicyService;

	private static final Logger logger = LoggerFactory.getLogger(SlaPolicyTask.class);

    //@Scheduled(cron = "0 0/1 * * * ?") //every 5 minutes
    @Scheduled(cron = "0 0 1 * * ?") //at 1 A.M every day
    public void run() {
        logger.info("start SlaPolicyTask ");
        try{      	
        	slaPolicyService.applyRetentionPolicies();
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
        }        
        logger.info("stop SlaPolicyTask ");        
    }    

	public ISlaPolicyService getSLAPolicyService() {
		return slaPolicyService;
	}

	public void setSLAPolicyService(
			ISlaPolicyService slaPolicyService) {
		this.slaPolicyService = slaPolicyService;
	}
}
