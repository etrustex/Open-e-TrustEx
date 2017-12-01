package eu.europa.ec.cipa.etrustex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import eu.europa.ec.cipa.etrustex.services.dao.IXmlDAO;

public class XmlService implements IXmlService {

	@Autowired
	private IXmlDAO xmlDAO;

	public IXmlDAO getXmlDAO() {
		return xmlDAO;
	}

	public void setXmlDAO(IXmlDAO xmlDAO) {
		this.xmlDAO = xmlDAO;
	}

	@Override
	@Cacheable(value="xmlDocumentCache")
	public String retrieveXmlDocument(String key) {
		return xmlDAO.retrieveXmlDocument(key);
	}

}
