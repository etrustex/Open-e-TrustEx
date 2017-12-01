package eu.europa.ec.cipa.etrustex.integration;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class TemplateTest {

	@Test
	@Ignore
	public void testAckTempalte() throws Exception {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("freemarker-test-context.xml");
		Configuration freemarkerConfig = (Configuration) ctx.getBean("freemarkerConfig");
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		Map<String, String> root = new HashMap<String, String>();
		root.put("transactionResponseLocalName", "SubmitInvoiceResponse");
		root.put("transactionNameSpace", "ec:services:wsdl:Invoice-0.1");
		Template temp = freemarkerConfig.getTemplate("ack_0.1.flt");
		System.out.println(temp.toString());
		StringWriter sr = new StringWriter();
		temp.process(root, sr);
		System.out.println(sr.toString());
		DocumentBuilderFactory bfact = DocumentBuilderFactory.newInstance();
		bfact.setNamespaceAware(true);
		System.out.println(bfact.newDocumentBuilder().isNamespaceAware());

		Document doc = bfact.newDocumentBuilder().parse(new ByteArrayInputStream(sr.toString().getBytes()));

		System.out.println(doc.getDocumentElement().getPrefix());
		System.out.println(doc.getDocumentElement().getNamespaceURI());
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		StringWriter buffer = new StringWriter();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(doc), new StreamResult(buffer));
		System.out.println("The response content : " + buffer.toString());

		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory.createMessage();
		message.getSOAPBody().addDocument(doc);
		message.writeTo(System.out);

	}

}
