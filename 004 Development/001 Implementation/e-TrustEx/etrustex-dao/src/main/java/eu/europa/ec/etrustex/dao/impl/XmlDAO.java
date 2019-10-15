package eu.europa.ec.etrustex.dao.impl;

import org.springframework.stereotype.Repository;

import eu.europa.ec.etrustex.dao.IXmlDAO;
import eu.europa.ec.etrustex.domain.util.XmlStore;

@Repository
public class XmlDAO extends TrustExDAO<XmlStore, Long> implements IXmlDAO {

	@Override
	public String retrieveXmlDocument(String namespaceURI) {
		XmlStore xmlStore = entityManager.createQuery("from XmlStore x where x.key =:namespaceURI", XmlStore.class)
		.setParameter("namespaceURI", namespaceURI).getSingleResult();
		return xmlStore == null ? null : xmlStore.getValue();
	}

}
