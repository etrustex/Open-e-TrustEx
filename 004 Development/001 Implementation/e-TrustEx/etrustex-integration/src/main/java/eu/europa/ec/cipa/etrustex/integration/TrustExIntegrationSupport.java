package eu.europa.ec.cipa.etrustex.integration;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.services.IMessageService;
import eu.europa.ec.cipa.etrustex.services.IMetadataService;
import eu.europa.ec.cipa.etrustex.services.dto.CreateMessageDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MessageCreationException;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.types.DocumentStatusCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TrustExIntegrationSupport {

	private final static String ACK_TEMPLATE = "templates/ack_0.1.flt";
	private final static String ACK_TEMPLATE_2_0 = "templates/ack_2.0.flt";
	private final static String APP_RESP_TEMPLATE = "templates/appResponse.flt";
	private final static String WS_RESP_TEMPLATE = "templates/webServiceResponseTemplate.flt";
	private final static String WS_RESP_ROOT_TEMPLATE = "templates/webServiceRootResponseTemplate.flt";

	@Autowired
	protected IMessageService messageService;

	@Autowired
	protected MessageChannel dispatcherChannel;

	@Autowired
	protected IMetadataService metadataService;

	@Autowired
	public Configuration freemarkerConfig;

	private static final Logger logger = LoggerFactory.getLogger(TrustExIntegrationSupport.class);

	private final static String APP_RESPONSE_TRA_NAME = "SubmitApplicationResponse";

	private String createApplicationResponse(String id, String referenceId, String responseCode, String responseDescription, String parentMessageId, String parentMessageCode) throws IOException,
			TemplateException {
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> root = new HashMap<String, String>();
		root.put("APP_RESP_ID", id);
		root.put("ISSUE_DATE", format.format(Calendar.getInstance().getTime()));
		root.put("RESP_CODE", responseCode);
		root.put("REF_ID", referenceId);
		if (responseDescription != null) {
			root.put("RESP_DESCRIPTION", responseDescription);
		}
		root.put("DOC_ID", parentMessageId);
		root.put("DOC_TYPE", parentMessageCode);
		Template temp = freemarkerConfig.getTemplate(APP_RESP_TEMPLATE);
		StringWriter sr = new StringWriter();
		temp.process(root, sr);
		return sr.toString();
	}

	protected String createWebserviceRootMessage(String transactionNS, String transactionLocalName, String body, String documentNSToReplace) throws IOException, TemplateException {
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		if (body != null) {
			if (documentNSToReplace != null) {
				body = body.replaceAll(documentNSToReplace, transactionNS);
			}
			Map<String, String> root = new HashMap<String, String>();
			root.put("transactionLocalName", transactionLocalName);
			root.put("transactionNameSpace", transactionNS);
			root.put("body", body);
			Template temp = freemarkerConfig.getTemplate(WS_RESP_TEMPLATE);
			StringWriter sr = new StringWriter();
			temp.process(root, sr);
			return sr.toString();
		} else {
			Template temp = freemarkerConfig.getTemplate(WS_RESP_ROOT_TEMPLATE);
			return temp.toString();
		}
	}

	protected Configuration getFreemarkerConfig() {
		return freemarkerConfig;
	}

	protected void setFreemarkerConfig(Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void createApplicationResponse(TrustExMessage<String> message, String id, String responseCode, String responseDescription, String replyTo) throws IOException, TemplateException {
		createApplicationResponse(message, id, responseCode, responseDescription, replyTo, true, true);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void createApplicationResponse(TrustExMessage<String> message, String id, String responseCode, String responseDescription, String replyTo, boolean setParentToError,
			boolean dispatchMessage) throws IOException, TemplateException {
		// Getting back the parameters
		Long receiverPartyId = message.getHeader().getSenderPartyId();
		Long senderPartyId = message.getHeader().getReceiverPartyId();
		Long issuerPartyId = senderPartyId;
		Long parentMessageId = message.getHeader().getMessageId();

		EntityAccessInfo info = new EntityAccessInfo();
		info.setCreationDate(Calendar.getInstance().getTime());
		info.setCreationId("TRUSTEX");
		Message parentMessage = messageService.retrieveMessage(parentMessageId);
		String code = parentMessage.getTransaction().getDocument().getDocumentTypeCode();
		String correlation = message.getHeader().getCorrelationId();
		String appResponse = createApplicationResponse(id, (correlation != null) ? correlation : "", code + ":" + responseCode, responseDescription, parentMessage.getDocumentId(), code);
		List<MessageBinary> binaries = new ArrayList<MessageBinary>();
		MessageBinary rawMessage = new MessageBinary();
		rawMessage.setBinary(appResponse.getBytes("UTF-8"));
		rawMessage.setMimeCode("text/xml");
		rawMessage.setBinaryType(MessageBinaryType.RAW_MESSAGE.name());
		rawMessage.setAccessInfo(info);
		binaries.add(rawMessage);
		Long transactionId = parentMessage.getTransaction().getId();
		Set<Transaction> transactions = parentMessage.getAgreement().getProfile().getTransactions();

		// we need to retrieve the Application response transaction for this
		// profile if we cannot this is a technical error to log
		// the error app response is treated as any other app response
		for (Transaction transaction : transactions) {
			if (APP_RESPONSE_TRA_NAME.equalsIgnoreCase(transaction.getName())) {
				transactionId = transaction.getId();
				break;
			}
		}
		Long applicationResponseMessageId = null;
		try {
			String messageResponseCode = code + ":" + responseCode;
			applicationResponseMessageId = messageService.createMessage(
					new CreateMessageDTO.Builder()
						.icaId(parentMessage.getAgreement().getId())
						.documentId(id)
						.statusCode(DocumentStatusCode.RECEIVED.name())
						.issuerId(issuerPartyId)
						.transactionTypeId(transactionId)
						.authenticatedUser("TRUSTEX")
						.receptionDate(Calendar.getInstance().getTime())
						.issueDate(Calendar.getInstance().getTime())
						.receiverPartyId(receiverPartyId)
						.senderPartyId(senderPartyId)
						.parentMessageId(parentMessage.getId())
						.documentTypeCd("301")
						.responseCode(messageResponseCode)
						.binaries(binaries)
						.build());
			if (setParentToError) {
				messageService.updateMessageStatus(parentMessageId, DocumentStatusCode.ERROR.name());
				// keep parent status in the header
				message.getHeader().setParentStatusCode(DocumentStatusCode.ERROR.name());
			}
		} catch (MessageCreationException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e.getCause());
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e.getCause());
		}

		if (dispatchMessage) {
			TrustExMessage<String> applicationResponseMessage = new TrustExMessage<String>(appResponse);
			TrustExMessageHeader applicationResponseHeader = new TrustExMessageHeader();
			applicationResponseHeader.setInterchangeAgreementId(message.getHeader().getInterchangeAgreementId());
			applicationResponseHeader.setMessageDocumentId(id);
			applicationResponseHeader.setSenderIdWithScheme(message.getHeader().getReceiverIdWithScheme() != null
					? message.getHeader().getReceiverIdWithScheme() : message.getHeader().getReceiverIdWithSchemeList().get(0));
			applicationResponseHeader.setReceiverIdWithScheme(message.getHeader().getSenderIdWithScheme());
			applicationResponseHeader.setTransactionTypeId(transactionId);
			applicationResponseHeader.setSenderPartyId(senderPartyId);
			applicationResponseHeader.setReceiverPartyId(receiverPartyId);
			applicationResponseHeader.setIssuerPartyId(issuerPartyId);
			applicationResponseHeader.setLogCorrelationId(message.getHeader().getLogCorrelationId());
			applicationResponseHeader.setMessageId(applicationResponseMessageId);
			applicationResponseHeader.setParentMessageId(parentMessageId);
			applicationResponseMessage.setHeader(applicationResponseHeader);

			MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(applicationResponseMessage);

			dispatcherChannel.send(builder.build());
		}
	}
	
	public IMessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public MessageChannel getDispatcherChannel() {
		return dispatcherChannel;
	}

	public void setDispatcherChannel(MessageChannel dispatcherChannel) {
		this.dispatcherChannel = dispatcherChannel;
	}

}
