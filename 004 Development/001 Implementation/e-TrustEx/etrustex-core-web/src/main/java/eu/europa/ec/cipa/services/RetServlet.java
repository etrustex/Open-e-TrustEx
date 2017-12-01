package eu.europa.ec.cipa.services;

import eu.europa.ec.cipa.etrustex.integration.task.ISlaPolicyService;
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
	
	private ISlaPolicyService policyService;
	
	@Override
	public void init() throws ServletException {
	    super.init();

	    this.policyService = (ISlaPolicyService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("slaPolicyService");
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		policyService.applyRetentionPolicies();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		policyService.applyRetentionPolicies();
	}

}
