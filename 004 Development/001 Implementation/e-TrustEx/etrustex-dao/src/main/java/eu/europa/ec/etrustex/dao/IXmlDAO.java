package eu.europa.ec.etrustex.dao;

import eu.europa.ec.etrustex.domain.util.XmlStore;

public interface IXmlDAO extends ITrustExDAO<XmlStore, Long> {

	public String retrieveXmlDocument(String key);

}
