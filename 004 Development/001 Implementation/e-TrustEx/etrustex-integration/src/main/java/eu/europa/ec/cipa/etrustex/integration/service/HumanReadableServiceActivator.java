package eu.europa.ec.cipa.etrustex.integration.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.query.StaticQueryContext;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.lowagie.text.pdf.BaseFont;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.MessageBinary;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.IXmlService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class HumanReadableServiceActivator extends XmlProcessingServiceActivator implements IASynchBusinessService {


//	final Configuration config = new Configuration();
	private static final Logger logger = LoggerFactory.getLogger(HumanReadableServiceActivator.class);
//	final StaticQueryContext sqc = config.newStaticQueryContext();

	// Used for the HumanReadable Xslt-transforamtion
	private IXmlService xmlService;
	
	@Autowired
	private LogServiceHelper logServiceHelper;
	
	@Autowired
	private CannonicalServiceActivator cannonicalService;
	
	@Autowired
	private ILogService logService;

	public IXmlService getXmlService() {
		return xmlService;
	}

	public void setXmlService(IXmlService xmlService) {
		this.xmlService = xmlService;
	}

	@Override
	@ServiceActivator
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {
		TrustExMessageHeader header = message.getHeader();
		Message internalMessage = messageService.retrieveMessage(header.getMessageId());
		String documentTypeCode = internalMessage.getTransaction().getDocument().getDocumentTypeCode();
		try {
			String canonical = message.getPayload();
			Map<MetaDataItemType, MetaDataItem> metadata = message.getHeader().getMetadata();
			if (metadata.get(MetaDataItemType.DOCUMENT_HUMAN_READABLE_PREPROC) != null && Boolean.parseBoolean(metadata.get(MetaDataItemType.DOCUMENT_HUMAN_READABLE_PREPROC).getValue()) == true) {
				canonical = prepocessMessage(message);
			}

			MetaDataItem xsltSourceItem = metadata.get(MetaDataItemType.DOCUMENT_HUMAN_READABLE_XSLT);
			//ETRUSTEX-1076
			if (xsltSourceItem == null) {
				logger.warn("HR template not found for message with id " + header.getMessageDocumentId());
				LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.WARN, LogDTO.LOG_OPERATION.OTHER, 
						"HR template not found for message with id " + header.getMessageDocumentId(), 
						this.getClass().getName());
				logService.saveLog(logDTO);
				return message;
			}
			Source xsltSource = new StreamSource(new ByteArrayInputStream(xsltSourceItem.getValue().getBytes(Charsets.UTF_8)));

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
			bin.setBinaryType(MessageBinaryType.HUMAN_READABLE_MESSAGE.name());
			bin.setMimeCode("application/pdf");
			bin.setMessage(internalMessage);
			messageService.updateMessageBinary(bin);

			message.setPayload(html);
		} catch (Exception e) {
			LogDTO logDTO = logServiceHelper.createLog(message, LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.OTHER, 
					e.getMessage(), this.getClass().getName());
			logService.saveLog(logDTO);			
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, documentTypeCode,
					ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}
		return message;
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
		eu.europa.ec.cipa.etrustex.domain.Message internalMessage = messageService.retrieveMessage(header.getMessageId());
		
		try {
			eu.europa.ec.cipa.etrustex.domain.Message parentMessage =null;
			if (header.getParentMessageId() != null) {
				parentMessage = messageService.retrieveMessage(header.getParentMessageId());
			}
			return cannonicalService.createCanonical(message, internalMessage,parentMessage);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TechnicalErrorException(ErrorResponseCode.TECHNICAL_ERROR.getCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription(), null, internalMessage.getTransaction().getDocument()
					.getDocumentTypeCode(), ErrorResponseCode.TECHNICAL_ERROR.getDescription());
		}		
		
	}

	
}
