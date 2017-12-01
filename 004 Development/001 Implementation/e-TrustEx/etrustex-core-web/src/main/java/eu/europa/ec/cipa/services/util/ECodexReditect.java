package eu.europa.ec.cipa.services.util;

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

public class ECodexReditect extends HttpServlet
{

    private static final long serialVersionUID = 0x23f2c013817cd888L;

    public ECodexReditect() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	System.out.println("Message received from e-Codex");
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream in = request.getInputStream();
        byte temp[] = new byte[0x10000];
        for(int count = in.read(temp); count > 0; count = in.read(temp))
            buffer.write(temp, 0, count);

        in.close();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String url = "http://solanum.cc.cec.eu.int:8090";
        request.getRequestURL();
        url = url + request.getRequestURL().substring(request.getRequestURL().indexOf("/holodeck"));
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
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	System.out.println("Message received from e-Codex");
    	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream in = request.getInputStream();
        byte temp[] = new byte[0x10000];
        for(int count = in.read(temp); count > 0; count = in.read(temp))
            buffer.write(temp, 0, count);

        in.close();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String url = "http://solanum.cc.cec.eu.int:8090";
        request.getRequestURL();
        url = url + request.getRequestURL().substring(request.getRequestURL().indexOf("/holodeck"));
        HttpPost post = new HttpPost(url);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);//30sec (very bad connection)
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);//30sec (very bad connection)
	
		Enumeration en = request.getHeaderNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = request.getHeader(key);
			if (!"content-length".equalsIgnoreCase(key) && !"Transfer-encoding".equalsIgnoreCase(key)) {
				post.addHeader(key, value);
			}
		
		}

        ByteArrayEntity entity = new ByteArrayEntity(buffer.toByteArray());
     
        post.setEntity(entity);
        
        OutputStream out = response.getOutputStream();
        HttpResponse resp = httpclient.execute(post);
        HttpEntity resEntity = resp.getEntity();
        Header[] headers = resp.getAllHeaders();
        for (Header header : headers) {
        	response.setHeader(header.getName(), header.getValue());
        }
        resEntity.writeTo(out);
        out.close();
    }
}
