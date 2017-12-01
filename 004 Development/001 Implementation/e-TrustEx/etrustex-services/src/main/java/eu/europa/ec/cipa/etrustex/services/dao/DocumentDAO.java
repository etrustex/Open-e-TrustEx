/**
 * 
 */
package eu.europa.ec.cipa.etrustex.services.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import eu.europa.ec.cipa.etrustex.domain.Document;

/**
 * @author batrian
 *
 */
@Repository
public class DocumentDAO extends TrustExDAO<Document, Long> implements IDocumentDAO {

	/*@Override
	public Document create(Document doc) {
		if(doc.getStateMachine() != null 
				&& !entityManager.contains(doc.getStateMachine())) {
			doc.set(entityManager.merge(party.getCertificate()));
		}
		
		if(doc.getCredentials() != null  
				&& !entityManager.contains(party.getCredentials())) {
			doc.setCredentials(entityManager.merge(party.getCredentials()));
		}
		
		return super.create(party);

	}*/
	
	@Override
	public void delete(Long id) {
		Document document = read(id);
		super.delete(document);

	}
	
	@Override
	public List<Document> getDocuments(String name, String localName, String documentTypeCode) {
		String queryString = "SELECT distinct document FROM Document as document ";

		boolean noCondition = true;

		if (StringUtils.isNotBlank(name)){
			queryString += "where UPPER(document.name) like UPPER(:name) ";
			noCondition = false;
		}

		if (StringUtils.isNotBlank(localName)) {
			if (noCondition){
				queryString += "where UPPER(document.localName) like UPPER(:localName) ";
				noCondition = false;
			} else {
				queryString += "and UPPER(document.localName) like UPPER(:localName) ";
			}
		}
	
		if (StringUtils.isNotBlank(documentTypeCode)) {
			if (noCondition){
				queryString += "where UPPER(document.documentTypeCode) like UPPER(:documentTypeCode) ";
				noCondition = false;
			} else {
				queryString += "and UPPER(document.documentTypeCode) like UPPER(:documentTypeCode) ";
			}
		}
		
		queryString += "order by UPPER(document.name) ";

		TypedQuery<Document> query = entityManager.createQuery(queryString, Document.class);
		
		if (StringUtils.isNotBlank(name)){
			query.setParameter("name", "%" + name.trim() + "%");
		}
		
		if (StringUtils.isNotBlank(localName)){
			query.setParameter("localName", "%" + localName.trim() + "%");
		}
		
		if (StringUtils.isNotBlank(documentTypeCode)){
			query.setParameter("documentTypeCode", "%" + documentTypeCode.trim() + "%");
		}
		
		return query.getResultList();

	}

	@Override
	public boolean isInUse(Document document){
		
		Boolean isInUse = false;
		
		String transactionQuery = "SELECT COUNT(trx) FROM Transaction trx "
				+ "WHERE trx.document.id = :documentId";
		
		Long trxNo = entityManager.createQuery(transactionQuery, Long.class)
				.setParameter("documentId", document.getId())
				.getSingleResult();
		
		if (trxNo != 0){
			isInUse = true;
		}
		
		return isInUse;
	} 
	
}
