package eu.europa.ec.cipa.etrustex.integration.service;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.cipa.etrustex.services.IXmlService;

public class CustomURIResolver implements URIResolver {
	private static final Logger logger = LoggerFactory.getLogger(CustomURIResolver.class);
	

	IXmlService xmlService;

	
	public CustomURIResolver(IXmlService xmlService)
	{
		super();
		this.xmlService = xmlService;
	}

	
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		if (href != null && !href.isEmpty())
			return resolveFromDatabase(href);
		else
			return null;
	}

	private Source resolveFromDatabase(String key) {
		String document = this.xmlService.retrieveXmlDocument(key);
		if (document != null && !document.isEmpty())
		{
			return new StreamSource(new StringReader(document));
		}
		else return null;
	}
}
