package eu.europa.ec.etrustex.domain.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Domain Object to use for an Email attachment.
 * @author steduda
 */
public class EmailAttachment {

	private String attachmentFilename;
	
	private InputStream inputStream;

	private String contentType;
	
	private String content;
	
	public EmailAttachment() {
		
	}
	
	public EmailAttachment(String attachmentFilename, InputStream inputStream, String contentType) {
		this.attachmentFilename = attachmentFilename;
		this.inputStream = inputStream;
		this.contentType = contentType;
	}

	public String getAttachmentFileName() {
		return attachmentFilename;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFilename = attachmentFileName;
	}

	public InputStream getInputStream() throws UnsupportedEncodingException {
		if (content != null) {
			inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
		}
		
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	
}
