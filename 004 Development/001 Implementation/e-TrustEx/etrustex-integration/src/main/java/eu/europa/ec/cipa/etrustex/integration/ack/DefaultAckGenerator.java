package eu.europa.ec.cipa.etrustex.integration.ack;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.stereotype.Component;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component("defaultAckGenerator")
public class DefaultAckGenerator implements IAckGenerator {
	
	private final static String ACK_TEMPLATE = "templates/ack_0.1.flt";
	private final static String ACK_TEMPLATE_2_0 = "templates/ack_2.0.flt";
	private final static String ACK_TEMPLATE_3_0 = "templates/ack_3.0.flt";

	@Autowired
	protected IMessageService messageService;

	@Autowired
	protected MessageChannel dispatcherChannel;

	@Autowired
	protected IMetadataService metadataService;

	@Autowired
	public Configuration freemarkerConfig;

	@Override
	public String generateAck(Transaction transaction, TrustExMessageHeader header) throws IOException, TemplateException {
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		Template temp = null;
		if ("0.1".equalsIgnoreCase(transaction.getVersion())) {
			temp = freemarkerConfig.getTemplate(ACK_TEMPLATE);
			return temp.toString();
		} else {
			if (transaction.getVersion().startsWith("2.")){
				temp = freemarkerConfig.getTemplate(ACK_TEMPLATE_2_0);
			}else{
				temp = freemarkerConfig.getTemplate(ACK_TEMPLATE_3_0);
			}
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("TRANSACTION_NS", transaction.getNamespace());
			root.put("ISSUE_DATE", format.format(Calendar.getInstance().getTime()));
			root.put("DOCUMENT_ID", StringEscapeUtils.escapeXml(header.getMessageDocumentId()));
			
			//https://webgate.ec.europa.eu/CITnet/jira/browse/ETRUSTEX-1187
			String documentTypeCode = (transaction.getTransactionTypeCode()!=null)?transaction.getTransactionTypeCode():transaction.getDocument().getDocumentTypeCode();
			
			root.put("DOCUMENT_TYPE_CD", documentTypeCode);

			MetaDataItem schemeIdSeparatorItem = metadataService.getDefaultMetadataByType(MetaDataItemType.SCHEME_ID_SEPARATOR.name()).get(0);
			String schemeIdSeparator = schemeIdSeparatorItem.getValue();
			// set sender schemeID attribute and value
			if (header.getSenderIdWithScheme().contains(schemeIdSeparator)) {
				String[] splittedId = header.getSenderIdWithScheme().split(schemeIdSeparator);
				root.put("SENDER_SCHEME_ID", splittedId[0]);
				root.put("SENDER_ID", splittedId[1]);
			} else {
				root.put("SENDER_ID", header.getSenderIdWithScheme());
			}
			// set receiver schemeID attribute and value
			if (header.getReceiverIdWithScheme() !=null && header.getReceiverIdWithScheme().contains(schemeIdSeparator)) {
				String[] splittedId = header.getReceiverIdWithScheme().split(schemeIdSeparator);
				root.put("RECEIVER_SCHEME_ID", splittedId[0]);
				root.put("RECEIVER_ID", splittedId[1]);
			} else {
				root.put("RECEIVER_ID", header.getReceiverIdWithScheme());
			}
			//ETRUSTEX-1264 
			root.put("MULTICAST", header.isMulticastSupported());
			
			if (header.isMulticastSupported()){
				
				Map<String, String> receiverIDs = new HashMap<String, String>();
				Map<String, String> receiverSchemes = new HashMap<String, String>();
				for (String idWithScheme : header.getReceiverIdWithSchemeList()){		
					if (idWithScheme.indexOf(schemeIdSeparator)==-1){
						idWithScheme="GLN"+schemeIdSeparator+idWithScheme;
					}
					String[] splittedId = idWithScheme.split(schemeIdSeparator);
					receiverSchemes.put(idWithScheme, splittedId[0]);
					receiverIDs.put(idWithScheme, splittedId[1]);
					
				}
				root.put("RECEIVER_SCHEMES_MAP",receiverSchemes);
				root.put("RECEIVER_IDS_MAP",receiverIDs);
			}
			
			StringWriter sr = new StringWriter();
			temp.process(root, sr);
			return sr.toString();
		}
	}

}
