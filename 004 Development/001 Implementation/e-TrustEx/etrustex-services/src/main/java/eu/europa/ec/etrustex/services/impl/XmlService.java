package eu.europa.ec.etrustex.services.impl;

import eu.europa.ec.etrustex.dao.IXmlDAO;
import eu.europa.ec.etrustex.services.IXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("xmlService")
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
    @Transactional(readOnly = true)
	public String retrieveXmlDocument(String key) {
		return xmlDAO.retrieveXmlDocument(key);
	}

}
