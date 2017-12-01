package eu.europa.ec.cipa.etrustex.integration.ejb.facades;

import javax.ejb.Remote;

import ec.schema.xsd.commonaggregatecomponents_2.HeaderType;
import eu.europa.ec.cipa.etrustex.integration.exception.ServiceFacadeException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;

@Remote
public interface IntegrationServiceFacadeRemote  {
	
	public TrustExMessageBinary retrieveMessageBinary(String senderPartyId, String binaryId, String binaryTye ) throws ServiceFacadeException;
	
	public void submitDocument(String user,String password, HeaderType header, String document) throws ServiceFacadeException; 
	
	public TrustExMessage<String> retrieveMessageWithoutBinaries(String user,String password, String messageDocumentId, String messageDocumentTypeCode,String senderIdWithScheme, String receiverIdWithScheme ) throws ServiceFacadeException;

}
