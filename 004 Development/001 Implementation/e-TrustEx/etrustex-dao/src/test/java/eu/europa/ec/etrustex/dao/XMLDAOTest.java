package eu.europa.ec.etrustex.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import eu.europa.ec.etrustex.domain.util.XmlStore;
import org.junit.Assert;

public class XMLDAOTest extends AbstractEtrustExTest {
	
	public static final String NAMESPACE 	= "my_test_namespace";
	public static final String NAMESPACE_2 	= "my_test_namespace_2";
	public static final String XML_VALUE 	= "<test></test>";
	
	@Autowired
	private IXmlDAO xmlDao;
	
	@Test
	public void testCRUD(){
		XmlStore xml = new XmlStore();
		xml.setKey(NAMESPACE);
		xml.setValue(XML_VALUE);
		xml = xmlDao.create(xml);
		
		flush();
		
		xml.setKey(NAMESPACE_2);
		xml = xmlDao.update(xml);
		
		XmlStore xml2 = xmlDao.read(xml.getId());
		Assert.assertEquals("New Key Should be there", NAMESPACE_2, xml2.getKey());
		
		xmlDao.delete(xml2);
		Assert.assertNull("Key Should be deleted", xmlDao.read(xml.getId()));		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void testRetrieveXmlDocument(){
		XmlStore xml = new XmlStore();
		xml.setKey(NAMESPACE);
		xml.setValue(XML_VALUE);
		xml = xmlDao.create(xml);
		
		Assert.assertEquals("Should be the same value as the one saved", XML_VALUE, xmlDao.retrieveXmlDocument(NAMESPACE));
		
		xmlDao.retrieveXmlDocument(NAMESPACE+"__2");
	}

}
