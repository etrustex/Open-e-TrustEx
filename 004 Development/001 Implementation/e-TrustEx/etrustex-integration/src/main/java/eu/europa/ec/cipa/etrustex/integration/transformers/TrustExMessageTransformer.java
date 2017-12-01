package eu.europa.ec.cipa.etrustex.integration.transformers;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.TrustExIntegrationSupport;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class TrustExMessageTransformer extends TrustExIntegrationSupport {
	
	private static final String ID_SEPARATOR = "::";
	
	protected void populateCommonMessageHeader(TrustExMessage<String> trustExMessage,Map<MetaDataItemType, MetaDataItem> metadata) throws XPathException{
		
		Configuration config = new Configuration();
		DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(trustExMessage.getPayload())));
		DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
		dynamicContext.setContextItem(docInfo);
		
		final StaticQueryContext sqc = config.newStaticQueryContext();
		DateFormat xsdDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat xsdDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		trustExMessage.getHeader().setReceivedDate(Calendar.getInstance().getTime());
		
		MetaDataItem documentIdQueryItem = metadata.get(MetaDataItemType.DOCUMENT_ID_XQUERY);
		if (documentIdQueryItem != null){
			
			final XQueryExpression documentIdQuery = sqc.compileQuery(metadata.get(MetaDataItemType.DOCUMENT_ID_XQUERY).getValue());
			Object idObject = documentIdQuery.evaluateSingle(dynamicContext);
			String idString = null;
			if (idObject != null){
				if (idObject instanceof String) {
					idString = (String) idObject;
				} else {
					idString = ((Item)idObject).getStringValue();
				}
			}
			//trim ID
			if (idString!=null){
				int sepIndex = idString.indexOf(ID_SEPARATOR);
				if(sepIndex != -1){
					idString = idString.substring(0, sepIndex).trim().concat(ID_SEPARATOR).concat(idString.substring(sepIndex+ID_SEPARATOR.length(), idString.length()).trim());
				}else{
					idString=idString.trim();
				}
			}
			trustExMessage.getHeader().setMessageDocumentId(idString);
			
		}

		MetaDataItem parentQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_ID_XQUERY);
		Item it ;
		if (parentQueryItem != null){
			final XQueryExpression parentIdQuery = sqc.compileQuery(parentQueryItem.getValue());
			it = (Item)parentIdQuery.evaluateSingle(dynamicContext);
			if (it != null){
				//trim ID
				String parentId =it.getStringValue();
				if (parentId!=null){
					parentId = parentId.trim();
				}
				trustExMessage.getHeader().setMessageParentDocumentId(parentId);
			}			
		}
		MetaDataItem parentTCQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_TYPECODE_XQUERY);
		if (parentTCQueryItem != null){
			final XQueryExpression parentTCQuery = sqc.compileQuery(parentTCQueryItem.getValue());
			it = (Item)parentTCQuery.evaluateSingle(dynamicContext);
			if (it != null){
				trustExMessage.getHeader().setMessageParentDocumentTypeCode(it.getStringValue());
			}				
		}
		MetaDataItem versionQueryItem = metadata.get(MetaDataItemType.DOCUMENT_VERSION_XQUERY);
		if (versionQueryItem != null){
			final XQueryExpression versionQuery = sqc.compileQuery(versionQueryItem.getValue());
			it = (Item)versionQuery.evaluateSingle(dynamicContext);
			if (it != null){
				trustExMessage.getHeader().setMessageDocumentVersion(it.getStringValue());
			}
		}
		
		MetaDataItem correlationXqueryItem = metadata.get(MetaDataItemType.CORRELATION_ID_XQUERY);
		if (correlationXqueryItem != null){
			final XQueryExpression  exp =sqc.compileQuery(correlationXqueryItem.getValue());
			it = (Item)exp.evaluateSingle(dynamicContext);
			if (it != null && it.getStringValue() != null){				
				trustExMessage.getHeader().setCorrelationId(it.getStringValue());
			}
		}
		
		MetaDataItem issueDateQueryItem = metadata.get(MetaDataItemType.DOCUMENT_ISSUEDATE_XQUERY);
		//Time is ignored if Date is not present
		if (issueDateQueryItem != null){
			
			final XQueryExpression issueDateQuery = sqc.compileQuery(issueDateQueryItem.getValue());
			it = (Item)issueDateQuery.evaluateSingle(dynamicContext);								
			String date = null;					
			if(it!= null && it.getStringValue() != null){
				date = it.getStringValue();
			}
			
			String time = null;
			MetaDataItem issueTimeQueryItem = metadata.get(MetaDataItemType.DOCUMENT_ISSUETIME_XQUERY);
			if(issueTimeQueryItem != null){
				final XQueryExpression issueTimeQuery = sqc.compileQuery(issueTimeQueryItem.getValue());
				it = (Item)issueTimeQuery.evaluateSingle(dynamicContext);										
				if(it!= null && it.getStringValue() != null){
					time = it.getStringValue();
				}
			}

			if (date != null && time != null){
				try {
						trustExMessage.getHeader().setIssueDate(xsdDateTimeFormat.parse(date+" "+time));
					} catch (ParseException e) {
						trustExMessage.getHeader().setIssueDate(null);
					}
			}else if (date != null && time == null){
				try {
					trustExMessage.getHeader().setIssueDate(xsdDateFormat.parse(date));
				} catch (ParseException e) {
					trustExMessage.getHeader().setIssueDate(null);
				}
			}	
		}			
	}
	
	

}
