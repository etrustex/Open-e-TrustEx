package eu.europa.ec.cipa.etrustex.integration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ec.schema.xsd.documentwrapperrequest_1.DocumentWrapperRequestType;

public class UnMarshallTest {
	
	@Test
	public void testUnmarshallDocumentWrapperRequest() throws Exception{
		
		Source s= new StreamSource(this.getClass().getClassLoader()	.getResourceAsStream("docWrapperRequest.xml"));
		JAXBContext jaxbContext = JAXBContext
		.newInstance(DocumentWrapperRequestType.class);
		//InputSource source = new InputSource(new StringReader(message.getPayload()));
		 JAXBElement<DocumentWrapperRequestType> request =jaxbContext.createUnmarshaller().unmarshal(s,DocumentWrapperRequestType.class);
		
		
	}
	@Test
	public void testRetrieveDocumentNamespace() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(this.getClass().getClassLoader()	.getResourceAsStream("docWrapperRequest.xml"));
	    Element root = doc.getDocumentElement();
	   System.out.println( root.getTagName()); 
	   System.out.println( root.getLocalName());
	   System.out.println( root.getNamespaceURI());
	}

}
