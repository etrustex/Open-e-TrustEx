/**
 *
 */
package eu.europa.ec.etrustex.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.IDocumentDAO;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.services.IDocumentService;

/**
 * @author batrian
 *
 */
@Service
public class DocumentService implements IDocumentService {

	private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	@Autowired
	private IDocumentDAO documentDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Document getDocument(Long id) {
		logger.info("getDocument(id={})", id);
		return  documentDAO.read(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Document createDocument(Document document) {

		logger.info("createDocument({})", document);

		//create the document
		Document createdDocument = documentDAO.create(document);

		//return the complete object
		return documentDAO.read(createdDocument.getId());	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Document updateDocument(Document document) {
		logger.info("updateDocument({})", document);
		Document originalDoc = documentDAO.read(document.getId());

		originalDoc.setName(document.getName());
		originalDoc.setLocalName(document.getLocalName());
		originalDoc.setNamespace(document.getNamespace());
		originalDoc.setDocumentTypeCode(document.getDocumentTypeCode());
		originalDoc.setVersion(document.getVersion());
		originalDoc.setAccessInfo(document.getAccessInfo());

		return documentDAO.update(originalDoc);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteDocument(Long id) {
		logger.info("deleteDocument({})", id);
		documentDAO.delete(id);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Document> getDocuments(String name, String localName,
									   String typeCode) {

		return documentDAO.getDocuments(name, localName, typeCode);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public Boolean isInUse(Document doc) {
		return documentDAO.isInUse(doc);
	}
}
