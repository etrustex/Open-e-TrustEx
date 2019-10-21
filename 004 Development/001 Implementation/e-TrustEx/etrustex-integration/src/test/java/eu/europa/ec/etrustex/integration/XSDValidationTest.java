package eu.europa.ec.etrustex.integration;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Ignore;
import org.junit.Test;

public class XSDValidationTest {

	@Test
	@Ignore
	public void testXsdValidation() throws Exception {
		URL schemaFile = new URL("http://localhost:8080/etrustex/xsd/maindoc/EC-DocumentWrapper-1.0.xsd");
		StreamSource stream = new StreamSource(schemaFile.openStream());
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema;
		schema = schemaFactory.newSchema(schemaFile);
	}

}
