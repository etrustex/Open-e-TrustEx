/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.ack;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.ITransactionService;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

/**
 * @author chiricr
 *
 */
@Component
public class GenerateAckService implements ApplicationContextAware {
	
	@Autowired
	protected ITransactionService transactionService;
	
	@Autowired
	protected IMetadataService metadataService;	
	
	private ApplicationContext applicationContext;
	
	public String generateAndSignAck(Transaction transaction, TrustExMessageHeader header) throws Exception {
		List<MetaDataItem> metadataItems = metadataService.getDefaultMetadataByType(MetaDataItemType.ACK_GENERATOR_CLASS.name());
		if (CollectionUtils.isEmpty(metadataItems)) {
			throw new TechnicalErrorException("soapenv:Server", ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, null, null);
		}		
		String ackGeneratorClass = metadataItems.get(0).getValue();
		Class clazz = this.getClass().getClassLoader().loadClass(ackGeneratorClass);				
		IAckGenerator ackGenerator = (IAckGenerator)applicationContext.getBean(clazz);
		String ackPayload = ackGenerator.generateAck(transaction, header);
		
		metadataItems = metadataService.getDefaultMetadataByType(MetaDataItemType.ACK_SIGNATURE_CLASS.name());
		if (CollectionUtils.isNotEmpty(metadataItems)) {		
			String ackSignatureClass = metadataItems.get(0).getValue();
			clazz = this.getClass().getClassLoader().loadClass(ackSignatureClass);				
			IAckSignatureGenerator ackSignatureGenerator = (IAckSignatureGenerator)applicationContext.getBean(clazz);		
			String signature = ackSignatureGenerator.signMessage(ackPayload);
			ackPayload = signature + ackPayload;
		}
		
		return ackPayload;
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}	

}
