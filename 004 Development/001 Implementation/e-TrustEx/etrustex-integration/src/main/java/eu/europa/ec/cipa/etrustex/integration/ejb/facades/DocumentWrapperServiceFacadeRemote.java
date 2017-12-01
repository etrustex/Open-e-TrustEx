package eu.europa.ec.cipa.etrustex.integration.ejb.facades;

import javax.ejb.Remote;

import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import ec.schema.xsd.documentwrapper_1.DocumentWrapperType;
import eu.europa.ec.cipa.etrustex.integration.exception.ServiceFacadeException;

@Remote
public interface DocumentWrapperServiceFacadeRemote {
	
	public void storeDocumentWrapper(String user,String password, HeaderType header, DocumentWrapperType wrapper, byte[] binaryfile, String binaryMimetype) throws ServiceFacadeException;

}
