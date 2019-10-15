package eu.europa.ec.etrustex.services.util;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mchange.v1.lang.BooleanUtils;

import eu.europa.ec.etrustex.integration.task.IPolicyService;
import eu.europa.ec.etrustex.services.IMetadataService;

/**
 * Servlet implementation class RetServlet
 */
public class ClearMetadataCacheServlet extends HttpServlet {
		
	private IMetadataService metadataService;
	
	@Override
	public void init() throws ServletException {
	    super.init();
	    this.metadataService = (IMetadataService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("metadataService");
	}	
		    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		clearMetadataCache();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		clearMetadataCache();
		
	}

	/**
	 * @throws IOException
	 */
	private void clearMetadataCache() throws IOException {
		Properties prop = new Properties();
		prop.load(getServletContext().getResourceAsStream("/WEB-INF/classes/etrustex-core-web.properties"));
		boolean enableClearCache = BooleanUtils.parseBoolean(prop.getProperty("etrustex.enable.clearMetadataCache"));		
		if (enableClearCache) {
			metadataService.clearMetaDataCache();
		}
	}

}
