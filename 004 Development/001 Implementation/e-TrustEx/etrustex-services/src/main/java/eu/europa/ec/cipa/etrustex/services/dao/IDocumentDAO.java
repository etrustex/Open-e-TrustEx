/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.Document;

/**
 * @author batrian
 *
 */
public interface IDocumentDAO extends ITrustExDAO<Document, Long> {

	public void delete(Long id);

	public List<Document> getDocuments(String name, String localName, String docTypeCode);

	public boolean isInUse(Document document);
}
