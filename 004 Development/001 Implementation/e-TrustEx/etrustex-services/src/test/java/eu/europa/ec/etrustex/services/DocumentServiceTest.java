package eu.europa.ec.etrustex.services;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Document;

public class DocumentServiceTest extends AbstractEtrustExTest {
	
	@Autowired private IDocumentService documentService;
	
	@BeforeClass
	public static void init(){
		System.out.println("--------------------------------------------------");
		System.out.println("DocumentService");
		System.out.println("--------------------------------------------------");
	}

	@Test
	public void testCRUD() {
		System.out.println("------Create-update-delete------");
		Document document = new Document();
		document.setDocumentTypeCode("Test");
		document.setLocalName("TestRequest");
		document.setName("test");
		document.setNamespace("ec:junit:test");
		document = documentService.createDocument(document);
		assertNotNull(document.getId());
		System.out.println("Document has been created : "+document);
		
		document.setDocumentTypeCode("updated");
		document = documentService.updateDocument(document);
		System.out.println("Document has been updeted : "+document);
		
		documentService.deleteDocument(document.getId());
		System.out.println("Document has been deleted :");
	}

}
