package eu.europa.ec.etrustex.services;

import eu.europa.ec.etrustex.common.Constants;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class RetServlet
 */
public class RetServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private StdScheduler scheduler;

	
	@Override
	public void init() throws ServletException {
	    super.init();
	    this.scheduler = (StdScheduler)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("quartzScheduler");
	}
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RetServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			startQuartzJob();
		} catch (SchedulerException e) {
			response.getOutputStream().print("Something went wrong, the retention policy was NOT started.");
		}
		response.getOutputStream().print("Retention policy started.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws IOException {
		try {
			startQuartzJob();
		} catch (SchedulerException e) {
			response.getOutputStream().print("Something went wrong, the retention policy was NOT started.");
		}
		response.getOutputStream().print("Retention policy started.");
	}

	private void startQuartzJob() throws SchedulerException {
		// loop jobs by group
		for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Constants.QUARTZ_JOB_GROUP))) {
			if (jobKey.getName().equals(Constants.QUARTZ_RET_POLICY_JOB_NAME)) {
				scheduler.triggerJob(jobKey);
			}
		}
	}
}
