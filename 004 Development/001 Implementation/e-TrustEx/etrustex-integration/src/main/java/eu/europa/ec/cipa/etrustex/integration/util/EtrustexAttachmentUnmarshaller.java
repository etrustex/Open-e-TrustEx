package eu.europa.ec.cipa.etrustex.integration.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.bind.attachment.AttachmentUnmarshaller;

import org.springframework.ws.mime.Attachment;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import eu.europa.ec.cipa.etrustex.integration.exception.MissingAttachmentException;

public class EtrustexAttachmentUnmarshaller extends AttachmentUnmarshaller {
	
	private SaajSoapMessage request;
	
	public EtrustexAttachmentUnmarshaller(SaajSoapMessage request) {
		this.request = request;
	}

	@Override
	public DataHandler getAttachmentAsDataHandler(String cid) {
									
		if (cid != null && cid.indexOf("cid:") == 0) {
			cid = cid.substring("cid:".length());							
		}
		
		cid = URLDecoder.decode(cid);							
		Iterator<Attachment> attIterator = request.getAttachments();
		while (attIterator.hasNext()) {
			Attachment att = (Attachment) attIterator
					.next();
			
			if (att != null && att.getContentId() != null
					&& att.getContentId().indexOf(cid) > -1) {
				DataHandler dh = att.getDataHandler();								
				return dh;
			}
		}							
		throw new MissingAttachmentException();
	}

	@Override
	public byte[] getAttachmentAsByteArray(String cid) {
		//throw new RuntimeException("Not implemented !");
		//TODO (DERVEOL) validate that we never use that method for large attachments/binaries
		
		if (cid != null && cid.indexOf("cid:") == 0) {
			cid = cid.substring("cid:".length());
		
		}
		
		cid = URLDecoder.decode(cid);
		Iterator<Attachment> attIterator = request.getAttachments();
		while (attIterator.hasNext()) {
			Attachment att = (Attachment) attIterator
					.next();
		
			if (att != null && att.getContentId() != null
					&& att.getContentId().indexOf(cid) > -1) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();									
				InputStream is = null;
				try{
					is =att.getInputStream();
					byte[] buffer = new byte[1024];
					int count = 0;
					while((count=is.read(buffer))>0){
						baos.write(buffer, 0, count);
					}
				}catch(Exception e){
					if (is!=null){
						try {
							is.close();
						} catch (IOException e1) {
																		
						}
					}
					throw new RuntimeException(e);
				}									
				return baos.toByteArray();									
			}
		}							
		return null;
		
	}
	
	@Override
	public boolean isXOPPackage() { return true; }		

}
