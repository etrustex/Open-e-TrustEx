package eu.europa.ec.cipa.redirect;


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

public class ECodexRedirect extends HttpServlet
{

    private static final long serialVersionUID = 0x23f2c013817cd888L;
    
    private static final String REDIRECT_KEY = "REDIRECT_KEY";
	private static final String REDIRECT_ROOT= "REDIRECT_ROOT";
	private static final String TRANSFER_ENCODING = "Transfer-Encoding";
	private static final String CONTENT_LENGTH = "content-length";
    public ECodexRedirect() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
    	String key =getInitParameter(REDIRECT_KEY);
		String redirectroot =getInitParameter(REDIRECT_ROOT);
    	
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType(request.getContentType());
		
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
        	url = url + request.getRequestURL().substring(request.getRequestURL().indexOf(root)+root.length());
        }
        
        int i = 0;
        Object paramValue = null;
        for (Object paramKey : request.getParameterMap().keySet()) {	
        	if(i == 0){
        		url += "?";
        	}else{
        		url += "&";
        	}
        	url += (String)paramKey;
        	paramValue = request.getParameterMap().get(paramKey);
        	if(!"wsdl".equals(paramKey)  && paramValue != null && !"".equals(paramValue)){
        		url +=  "=" + paramValue;
        	}
        	i++;
		}
        HttpGet get = new HttpGet(url);
        //HttpPost post = new HttpPost("http://solanum.cc.cec.eu.int:8090/busdox-transport-start-server/accesspointService");
        ByteArrayEntity entity = new ByteArrayEntity(buffer.toByteArray());
        entity.setContentEncoding(request.getContentType());

        HttpResponse resp = httpclient.execute(get);
        HttpEntity resEntity = resp.getEntity();
        Header[] headers = resp.getAllHeaders();
        for (Header header : headers) {
        	if(!CONTENT_LENGTH.equalsIgnoreCase(header.getName()) && !TRANSFER_ENCODING.equalsIgnoreCase(header.getName())){
        		response.setHeader(header.getName(), header.getValue());
        	}
        }
        OutputStream out = response.getOutputStream();
        resEntity.writeTo(out);
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
    	String key =getInitParameter(REDIRECT_KEY);
		String redirectroot =getInitParameter(REDIRECT_ROOT);
    	
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType(request.getContentType());
		
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
        	url = url + request.getRequestURL().substring(request.getRequestURL().indexOf(root)+root.length()); 
        }
        
        HttpPost post = new HttpPost(url);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);//30sec (very bad connection)
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);//30sec (very bad connection)
		
        for (Object paramKey : request.getParameterMap().keySet()) {	
        	post.getParams().setParameter((String)paramKey, request.getParameterMap().get(paramKey));
        }	
		
		Enumeration en = request.getHeaderNames();
		while (en.hasMoreElements()) {
			String keyH = (String) en.nextElement();
			String value = request.getHeader(keyH);
			if (!CONTENT_LENGTH.equalsIgnoreCase(keyH) && !TRANSFER_ENCODING.equalsIgnoreCase(keyH)) {
				post.addHeader(keyH, value);
			}	
		}

        ByteArrayEntity entity = new ByteArrayEntity(buffer.toByteArray());
     
        post.setEntity(entity);
        
        OutputStream out = response.getOutputStream();
        HttpResponse resp = httpclient.execute(post);
        HttpEntity resEntity = resp.getEntity();
        Header[] headers = resp.getAllHeaders();
        for (Header header : headers) {
        	if (!CONTENT_LENGTH.equalsIgnoreCase(header.getName()) && !TRANSFER_ENCODING.equalsIgnoreCase(header.getName())) {
        		response.setHeader(header.getName(), header.getValue());
        	}
        }
        resEntity.writeTo(out);
        out.close();
    }
}

