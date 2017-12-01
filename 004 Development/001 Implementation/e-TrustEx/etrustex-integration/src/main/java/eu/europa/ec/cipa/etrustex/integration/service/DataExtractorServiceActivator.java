package eu.europa.ec.cipa.etrustex.integration.service;

import java.io.StringReader;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.query.DataExtractionConfig;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IQueryService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;

public class DataExtractorServiceActivator extends TrustExServiceActivator {
	
	private static final Logger logger = LoggerFactory.getLogger(DataExtractorServiceActivator.class);
	
	
	@Autowired
	private IQueryService queryService;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Message<TrustExMessage<String>> processASynchMessage(Message<TrustExMessage<String>> message){
		
		//Getting Parameters from the message
		String tNamespace = message.getPayload().getHeader().getTransactionNamespace();
		String requestLocalName = message.getPayload().getHeader().getTransactionRequestLocalName();
		Long messageId = message.getPayload().getHeader().getMessageId();
		
		//Getting the list to be queried
		final Configuration config = new Configuration();
		final DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
		final StaticQueryContext sqc = config.newStaticQueryContext();		
		List<DataExtractionConfig> qList = queryService.getXpathsForDocument(requestLocalName, tNamespace);
		LogDTO logDTO = null;
		try {
			logDTO = logServiceHelper.createLog(message.getPayload(), LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.DATA_EXTRACTION, 
					"Inside DataExtractorServiceActivator", 
					this.getClass().getName());			
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload().getPayload())));
			dynamicContext.setContextItem(docInfo);
			
			//Extracting the Elements if present from the message
			String xpath;
			XQueryExpression expression;
			Item it;
			
			for (DataExtractionConfig dataExtractionConfig : qList) {
				xpath = dataExtractionConfig.getXpath();
				if (xpath != null) {
					expression = sqc.compileQuery(xpath);
					it = (Item) expression.evaluateSingle(dynamicContext);
					if (it != null && it.getStringValue() != null) {						
						queryService.saveQueryResult(dataExtractionConfig.getKey(), it.getStringValue(), messageId);
					}
				}
			}
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			logDTO.setDescription(logDTO.getDescription() + " " + e.getMessage());
			logService.saveLog(logDTO);
			return null;
		} 	

		MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders());
		return builder.build();
	}
	
	
	
}
