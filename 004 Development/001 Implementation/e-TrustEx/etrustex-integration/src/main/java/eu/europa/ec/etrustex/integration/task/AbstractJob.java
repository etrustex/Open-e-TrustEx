package eu.europa.ec.etrustex.integration.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public abstract class AbstractJob extends QuartzJobBean {
	
	@Override
    protected void executeInternal(JobExecutionContext context) {
		run();
    }
	
	abstract public void run();

}
