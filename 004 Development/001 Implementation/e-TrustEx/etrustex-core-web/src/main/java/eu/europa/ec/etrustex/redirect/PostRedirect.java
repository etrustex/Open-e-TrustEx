package eu.europa.ec.etrustex.redirect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Servlet implementation class PostRedirect
 */
public class PostRedirect extends HttpServlet {
    
	private static final Logger logger = LoggerFactory
			.getLogger(PostRedirect.class);
	
	private static final long serialVersionUID = 1L;
	private static final String REDIRECT_KEY = "REDIRECT_KEY";
	private static final String REDIRECT_ROOT="REDIRECT_ROOT";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostRedirect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		try{
			String key =getInitParameter(REDIRECT_KEY);
			String redirectroot =getInitParameter(REDIRECT_ROOT);
			
			logger.debug("get request received by the "+key+" redirector");
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	        InputStream in = request.getInputStream();
	        byte temp[] = new byte[0x10000];
	        for(int count = in.read(temp); count > 0; count = in.read(temp))
	            buffer.write(temp, 0, count);

	        in.close();
	        DefaultHttpClient httpclient = new DefaultHttpClient();
	        
	          	        		
	        String url = DBUtils.getInstance().getParam(key);
	        if (redirectroot != null && !redirectroot.isEmpty() ){ 
	        	String root = DBUtils.getInstance().getParam(redirectroot);
	        	url = url + request.getRequestURL().substring(request.getRequestURL().indexOf(root));
	        }
	        HttpGet get = new HttpGet(url);
	        //HttpPost post = new HttpPost("http://solanum.cc.cec.eu.int:8090/busdox-transport-start-server/accesspointService");
	        ByteArrayEntity entity = new ByteArrayEntity(buffer.toByteArray());
	        entity.setContentEncoding(request.getContentType());
	        //get.setEntity(entity);
	        HttpResponse resp = httpclient.execute(get);
	        HttpEntity resEntity = resp.getEntity();
	        Header[] headers = resp.getAllHeaders();
	        for (Header header : headers) {
	        	response.setHeader(header.getName(), header.getValue());
	        }
	        OutputStream out = response.getOutputStream();
	        resEntity.writeTo(out);
	        out.close();
	        logger.debug("get response received by the "+key+" redirector");
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is = null;
		OutputStream out = null;
		try
        {
			String key =getInitParameter(REDIRECT_KEY);
			String redirectroot =getInitParameter(REDIRECT_ROOT);
			
			logger.debug("post request received by the "+key+" redirector");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            InputStream in = request.getInputStream();
            byte temp[] = new byte[0x10000];
            for(int count = in.read(temp); count > 0; count = in.read(temp))
                buffer.write(temp, 0, count);

            in.close();
            DefaultHttpClient httpclient = new DefaultHttpClient();
            
            		
	        String url = DBUtils.getInstance().getParam(key);
	        if (redirectroot != null && !redirectroot.isEmpty() ){ 
	        	String root = DBUtils.getInstance().getParam(redirectroot);
	        	url = url + request.getRequestURL().substring(request.getRequestURL().indexOf(root));
	        }
            HttpPost post = new HttpPost(url);
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);//30sec (very bad connection)
    		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);//30sec (very bad connection)
    	
    		Enumeration en = request.getHeaderNames();
    		while (en.hasMoreElements()) {
    			String keyH = (String) en.nextElement();
    			String value = request.getHeader(keyH);
    			if (!"content-length".equalsIgnoreCase(keyH) && !"Transfer-encoding".equalsIgnoreCase(keyH)) {
    				post.addHeader(keyH, value);
    			}
    		
    		}

            ByteArrayEntity entity = new ByteArrayEntity(buffer.toByteArray());
            entity.setContentEncoding(request.getContentType());
            post.setEntity(entity);
            HttpResponse resp = httpclient.execute(post);
            int statusCode = resp.getStatusLine().getStatusCode();
                        
            response.setStatus(statusCode);

                        
            
            HttpEntity resEntity = resp.getEntity();
            Header[] headers = resp.getAllHeaders();
            for (Header header : headers) {
            	logger.debug(header.getName()+","+ header.getValue());
            	if (!"content-length".equalsIgnoreCase(header.getName()) && !"Transfer-encoding".equalsIgnoreCase(header.getName())) {
            		response.setHeader(header.getName(), header.getValue());
            	}
            }
            out = response.getOutputStream();
            
            is = resEntity.getContent();

            int count = 0;
            
            while ((count=is.read(temp))>0){
            	out.write(temp, 0, count);
            	out.flush();
            }
            
            //resEntity.writeTo(out);
            out.close();
            logger.debug("post response received by the "+key+" redirector");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally{
        	if(is != null){
        		try{
        			is.close();
        		}catch(Exception e){}
        	}
        	if(out != null){
        		try{
        			out.close();
        		}catch(Exception e){}
        	}
        }
	}


	
}