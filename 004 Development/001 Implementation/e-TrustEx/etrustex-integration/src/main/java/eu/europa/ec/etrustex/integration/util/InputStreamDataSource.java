package eu.europa.ec.etrustex.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class InputStreamDataSource implements DataSource {
	private final InputStream is;

	private String contentType = "application/octet-stream";
	
	public InputStreamDataSource(InputStream is) {
		this.is = is;
	}

	public InputStreamDataSource(InputStream is, String contentType) {
		this.is = is;
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() throws IOException {
		return is;
	}

	public String getName() {
		return null;
	}

	public OutputStream getOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}
}