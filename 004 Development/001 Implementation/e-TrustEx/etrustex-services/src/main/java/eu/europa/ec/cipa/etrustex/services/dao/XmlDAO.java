package eu.europa.ec.cipa.etrustex.services.dao;

import eu.europa.ec.cipa.etrustex.domain.util.XmlStore;

public class XmlDAO extends TrustExDAO<XmlStore, Long> implements IXmlDAO {

	@Override
	public String retrieveXmlDocument(String namespaceURI) {
		XmlStore xmlStore = entityManager.createQuery("from XmlStore x where x.key =:namespaceURI", XmlStore.class)
		.setParameter("namespaceURI", namespaceURI).getSingleResult();
		return xmlStore == null ? null : xmlStore.getValue();
	}

}
