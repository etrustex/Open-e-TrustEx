package eu.europa.ec.etrustex.integration.service;

import com.itextpdf.text.pdf.BaseFont;
import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.HumanReadableTemplate;
import eu.europa.ec.etrustex.domain.MessageBinary;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.etrustex.services.IHumanReadableTemplateService;
import eu.europa.ec.etrustex.services.IXmlService;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import eu.europa.ec.etrustex.types.MessageBinaryType;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Calendar;
import java.util.Map;

@Transactional(propagation=Propagation.MANDATORY)
public class HumanReadableServiceActivator extends XmlProcessingServiceActivator {


	//	final Configuration config = new Configuration();
	private static final Logger logger = LoggerFactory.getLogger(HumanReadableServiceActivator.class);
//	final StaticQueryContext sqc = config.newStaticQueryContext();

	@Autowired
	private CannonicalServiceActivator cannonicalService;

	@Autowired
	private IHumanReadableTemplateService humanReadableTemplateService;

	@ServiceActivator
	@Transactional(propagation = Propagation.REQUIRED)
	public Message<TrustExMessage<String>> processMessage(Message<TrustExMessage<String>> arg) throws BusinessException, TechnicalErrorException {
		TrustExMessage<String> message = arg.getPayload();
		TrustExMessageHeader header = message.getHeader();
		eu.europa.ec.etrustex.domain.Message internalMessage = messageService.retrieveMessage(header.getMessageId());
		String documentTypeCode = internalMessage.getTransaction().getDocument().getDocumentTypeCode();
		HumanReadableTemplate hrTemplate = null;

		if (StringUtils.isNotEmpty(header.getHumanReadableTemplateName())) {
			hrTemplate = humanReadableTemplateService.findByDocumentAndTransactionAndName(internalMessage.getTransaction().getDocument().getId(),
					internalMessage.getTransaction().getId(),header.getHumanReadableTemplateName());
			if(hrTemplate == null){
				logger.error("HR template with name "+ header.getHumanReadableTemplateName() + " not found for message with id " + header.getMessageDocumentId());
				LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.OTHER,
						"HR template with name "+ header.getHumanReadableTemplateName() + " not found for message with id " + header.getMessageDocumentId(),
						this.getClass().getName());
				logService.saveLog(logDTO);
				throw new BusinessException("soapenv:Client",
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION.getDescription(), null,
						ErrorResponseCode.HARD_BUSINESS_RULE_VIOLATION, documentTypeCode,
						"HR template with name "+ header.getHumanReadableTemplateName() + " not found for message with id " + header.getMessageDocumentId());
			}
		}else{
			try {
				hrTemplate = humanReadableTemplateService.findByDefault(internalMessage.getTransaction().getDocument().getId(), internalMessage.getTransaction().getId());
			} catch (TechnicalErrorException e) {
				LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.OTHER,
						"Corrupted data (there are 2 HR templates having the 'is_default' flag set to 1 exist for the same document_id or trasaction_id)",
						this.getClass().getName());
				logService.saveLog(logDTO);
				throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
						ErrorResponseCode.TECHNICAL_ERROR.getDescription());
			}

			if (hrTemplate == null) {
				//no HR template is configured
				return arg;
			}
		}

		if(hrTemplate.getDocument() != null && hrTemplate.getTansaction() != null) {
			LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.OTHER,
					"Corrupted data (there is one HR template configured at both document and transaction level)",
					this.getClass().getName());
			logService.saveLog(logDTO);
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
					ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}

		String canonical = message.getPayload();
		Map<MetaDataItemType, MetaDataItem> metadata = message.getHeader().getMetadata();
		if (metadata.get(MetaDataItemType.DOCUMENT_HUMAN_READABLE_PREPROC) != null && Boolean.parseBoolean(metadata.get(MetaDataItemType.DOCUMENT_HUMAN_READABLE_PREPROC).getValue()) == true) {
			canonical = prepocessMessage(message);
		}

		Source xsltSource = new StreamSource(new ByteArrayInputStream(hrTemplate.getValue().getBytes(Charsets.UTF_8)));

		String html = transformToHtml(canonical, xsltSource);
		if (html == null) {
			logger.error("html result from xslt transformation is null");
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
					ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}

		Document xhtml = transformToXHtml(html);
		if (xhtml == null) {
			logger.error("xhtml transform result is null");
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
					ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}

		byte[] pdf = transformToPdf(xhtml);
		if (pdf == null) {
			logger.error("can not transform xhtml to pdf");
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
					ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}

		EntityAccessInfo accessInfo = new EntityAccessInfo();
		accessInfo.setCreationDate(Calendar.getInstance().getTime());
		accessInfo.setModificationDate(Calendar.getInstance().getTime());
		accessInfo.setCreationId(message.getHeader().getAuthenticatedUser());
		accessInfo.setModificationId(message.getHeader().getAuthenticatedUser());

		MessageBinary bin = new MessageBinary();
		bin.setAccessInfo(accessInfo);
		bin.setBinary(pdf);
		bin.setSize(Long.valueOf(pdf.length));
		bin.setBinaryType(MessageBinaryType.HUMAN_READABLE_MESSAGE.name());
		bin.setMimeCode("application/pdf");
		bin.setMessage(internalMessage);

		try {
			messageService.updateMessageBinary(bin);
		} catch (MessageUpdateException e) {
			LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.OTHER,
					e.getMessage(), this.getClass().getName());
			logService.saveLog(logDTO);
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
					ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}

		message.setPayload(html);
		MessageBuilder<TrustExMessage<String>> builder = MessageBuilder.withPayload(arg.getPayload()).copyHeaders(arg.getHeaders());
		return builder.build();
	}

	private String transformToHtml(String message, Source xsltSource) {
		String transformationResult = null;
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			Source xmlSource = new StreamSource(new StringReader(message));
			StringWriter writer = new StringWriter();
			Result result = new StreamResult(writer);
			TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
			factory.setURIResolver(new CustomURIResolver(xmlService));
			Transformer transformer;
			transformer = factory.newTransformer(xsltSource);
			transformer.transform(xmlSource, result);
			transformationResult = writer.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return transformationResult;
	}

	private Document transformToXHtml(String html) {
		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		tidy.setQuiet(true);
		tidy.setXHTML(true);
		PrintWriter errorWriter = new PrintWriter(new StringWriter(), true);
		tidy.setErrout(errorWriter);
		StringWriter sw = new StringWriter();

		Document transformationResult = tidy.parseDOM(new StringReader(html), sw);

		int errorCount = tidy.getParseErrors();
		int warningCount = tidy.getParseWarnings();
		logger.debug("xhtml transformation error count [{}], warning count [{}]", errorCount, warningCount);
		if (errorCount > 0) {
			logger.error(errorWriter.toString());
		} else {
			// no way to display warnings and errors separately
			if (warningCount > 0) {
				logger.warn(errorWriter.toString());
			}
		}
		return transformationResult;
	}

	private byte[] transformToPdf(Document xhtml) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out = new ByteArrayOutputStream();
			ITextRenderer renderer = new ITextRenderer(40.0f, 20);
			renderer.getFontResolver().addFont("/fonts/LiberationSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("/fonts/LiberationSans-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("/fonts/LiberationSans-BoldItalic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("/fonts/LiberationSans-Italic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

			// Create the target PDF
			renderer.setDocument(xhtml, null);
			renderer.layout();
			renderer.createPDF(out, false);

			// Complete the PDF
			renderer.finishPDF();

			return out.toByteArray();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
				out = null;
			} catch (IOException e) {/* nothing */
			}
		}
	}

	// We generate the canonical content
	private String prepocessMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {

		TrustExMessageHeader header = message.getHeader();
		eu.europa.ec.etrustex.domain.Message internalMessage = messageService.retrieveMessage(header.getMessageId());

		try {
			eu.europa.ec.etrustex.domain.Message parentMessage = null;
			if (header.getParentMessageId() != null) {
				parentMessage = messageService.retrieveMessage(header.getParentMessageId());
			}
			return cannonicalService.createCanonical(message, internalMessage,parentMessage);
		} catch (Exception e) {
			logger.error("Error Creating Canonical", e);
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, internalMessage.getTransaction().getDocument()
					.getDocumentTypeCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}

	}

	public void setXmlService(IXmlService xmlService) {
		this.xmlService = xmlService;
	}

	public void setHumanReadableTemplateService(IHumanReadableTemplateService humanReadableTemplateService) {
		this.humanReadableTemplateService = humanReadableTemplateService;
	}

}