/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services;

import java.util.List;

import eu.europa.ec.cipa.etrustex.domain.Document;

/**
 * @author batrian
 *
 */
public interface IDocumentService {
	/**
	 * Retrieves a document corresponding to a given id.
	 * @param id
	 * 		the id of the document to retrieve
	 * @return
	 * 		the document corresponding to the specified id  
	 */
	public Document getDocument(Long id);
	
	/**
	 * Retrieves all documents that satisfy the given criteria.
	 * @param name
	 * 		- the document name
	 * @param localName
	 * 		- the document local name
	 * @param typeCode
	 * 		- the document type code
	 * @return
	 * 		the list of documents which satisfy the above mentioned criteria
	 */
	public List<Document> getDocuments(String name, String localName, String typeCode);
	
	/**
	 * Creates a new document.
	 * @param document
	 * 		the document to create
	 * @return
	 * 		the newly created document
	 */
	public Document createDocument(Document document);
	
	/**
	 * Updates a document.
	 * @param document
	 * 		the document to update
	 * @return
	 * 		the updated document
	 */
	public Document updateDocument(Document document);
	
	/**
	 * Deletes a document.
	 * @param id
	 * 		the id of the document to delete
	 */
	public void deleteDocument(Long id);
	
	/**
	 * Checks if the given document is used by a transaction
	 * @param doc
	 * 		the document to check
	 * @return
	 * 		true if document is used, false otherwise
	 */
	public Boolean isInUse(Document doc);
}
