package eu.europa.ec.etrustex.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.query.DataExtractionConfig;

public class DataExtractionConfigDAOTest extends AbstractEtrustExTest {
	
	
	@Autowired IDataExtractionDAO dataExtractionDAO;
	@Autowired IDocumentDAO 	  documentDAO;
	
	@Test public void testCRUD(){
		Document document1 = new Document();
		document1.setName("N");
		document1.setLocalName("NS");
		document1 = documentDAO.create(document1);
		
		DataExtractionConfig dec1 = new DataExtractionConfig();
		dec1.setKey("Key");
		dec1.setXpath("XPATH");
		dec1.setDocument(document1);
		dataExtractionDAO.create(dec1);
		
		Assert.assertNotNull("Document not Created",dataExtractionDAO.read(dec1.getId()).getId());
		
		dec1.setKey("Key2");
		dataExtractionDAO.update(dec1);
		Assert.assertEquals("Document not Updated","Key2",dataExtractionDAO.read(dec1.getId()).getKey());
		
		dataExtractionDAO.delete(dec1);
		
		Assert.assertNull(dataExtractionDAO.read(dec1.getId()));
	}
	
	@Test public void testX(){
		Document document1 = new Document();
		document1.setName("N");
		document1.setLocalName("NS");
		document1 = documentDAO.create(document1);
		
		DataExtractionConfig dec1 = new DataExtractionConfig();
		dec1.setKey("Key");
		dec1.setXpath("XPATH");
		dec1.setDocument(document1);
		dataExtractionDAO.create(dec1);
		
		DataExtractionConfig dec2 = new DataExtractionConfig();
		dec2.setKey("Key");
		dec2.setXpath("XPATH2");
		dec2.setDocument(document1);
		dataExtractionDAO.create(dec2);
		
		List<DataExtractionConfig> decs = dataExtractionDAO.getXpathsForDocument(document1.getId());
		Assert.assertEquals("Count wrong",2,decs.size());		
	}
}
