package eu.europa.ec.cipa.etrustex.integration.util;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XsdValidationErrorHandler implements ErrorHandler {
	private StringBuffer errorBuffer = null;
	private boolean valid = true;
	
	public XsdValidationErrorHandler() {
		errorBuffer = new StringBuffer("XSD validation failure: \n");
	}
	
	public void warning(SAXParseException exception) throws SAXException {
		errorBuffer.append(exception.getMessage() + "\n");
	}

	public void error(SAXParseException exception) throws SAXException {
		valid = false;
		errorBuffer.append(exception.getMessage() + "\n");
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		valid = false;
		errorBuffer.append(exception.getMessage() + "\n");
	}

	public String getErrorMessage() {
		return errorBuffer.toString();
	}
	
	public boolean isValid() {
		return valid;
	}
}