package eu.europa.ec.etrustex.integration.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.xml.sax.SAXException;

import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.XsdValidationErrorHandler;

public class XmlProcessingServiceActivator extends TrustExServiceActivator {

	private static final Logger logger = LoggerFactory
			.getLogger(XmlProcessingServiceActivator.class);

	protected String transform(TrustExMessage<String> incoming,
							   Source xsltSource) throws TransformerConfigurationException,TransformerException {

		Source xmlSource = new StreamSource(new StringReader(
				incoming.getPayload()));
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
		Transformer transformer;
		transformer = factory.newTransformer(xsltSource);
		transformer.transform(xmlSource, result);
		return writer.toString();
	}

	protected String transform(String incoming, Source xsltSource) throws TransformerConfigurationException,TransformerException {

		Source xmlSource = new StreamSource(new StringReader(incoming));
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
		Transformer transformer;
		transformer = factory.newTransformer(xsltSource);
		transformer.transform(xmlSource, result);
		return writer.toString();
	}

	protected XsdValidationErrorHandler validateXSD(Schema schema,
													Message<TrustExMessage<String>> message) throws SAXException, IOException {
		Validator validator = schema.newValidator();
		XsdValidationErrorHandler xsdValidationErrorHandler = new XsdValidationErrorHandler();
		validator.setErrorHandler(xsdValidationErrorHandler);
		validator.validate(new StreamSource(new StringReader(message
				.getPayload().getPayload())));
		return xsdValidationErrorHandler;
	}

}
