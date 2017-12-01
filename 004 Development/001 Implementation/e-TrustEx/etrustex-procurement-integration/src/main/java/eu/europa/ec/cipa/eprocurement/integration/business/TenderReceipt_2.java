package eu.europa.ec.cipa.eprocurement.integration.business;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class TenderReceipt_2 extends TrustExBusinessService implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(TenderReceipt_2.class);

	private static final String BUNDLE_TYPE_CODE = "BDL";

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {

		String doctypeCode = "";
		try {
			Message mainMessage = messageService.retrieveMessage(message.getHeader().getMessageId());
			doctypeCode = mainMessage.getTransaction().getDocument().getDocumentTypeCode();
			final Configuration config = new Configuration();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));

			String bundleId = queryForSingle("/*:TenderReceipt/*:TenderDocumentReference[*:DocumentTypeCode = '" + BUNDLE_TYPE_CODE + "']/*:ID",
					docInfo, config);
			// String bundleTypeCode =
			// queryForSingle("/*:TenderReceipt/*:TenderDocumentReference/*:DocumentTypeCode",docInfo,
			// config);

			String senderPID = queryForSingle("/*:TenderReceipt/*:SenderParty/*:EndpointID", docInfo, config);
			String receiverPID = queryForSingle("/*:TenderReceipt/*:ReceiverParty/*:PartyIdentification/*:ID", docInfo, config);

			Message bundleMessage = messageService.retrieveMessage(bundleId, BUNDLE_TYPE_CODE, mainMessage.getReceiver().getId(), mainMessage
					.getSender().getId());

			if (bundleMessage == null) {
				mainMessage.setParentMessages(null);
				messageService.updateMessage(mainMessage);
				throw new BusinessException("soapenv:Client", "Non existing Bundle Message", null, ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST,
						doctypeCode, "Non existing Bundle Message");
			}

			Party plSender = null;
			Party plReceiver = null;

			try {
				plSender = authorisationService.getParty(senderPID, message.getHeader().getIssuer().getBusinessDomain());
			} catch (UndefinedIdentifierException e) {
				mainMessage.setParentMessages(null);
				messageService.updateMessage(mainMessage);
				throw new BusinessException("soapenv:Client", "Non Existing Sender in the body", null, ErrorResponseCode.DOCUMENT_VALIDATION_FAILED,
						doctypeCode, "Non Existing Sender in the body");
			}

			try {
				plReceiver = authorisationService.getParty(receiverPID, message.getHeader().getIssuer().getBusinessDomain());
			} catch (UndefinedIdentifierException e) {
				mainMessage.setParentMessages(null);
				messageService.updateMessage(mainMessage);
				throw new BusinessException("soapenv:Client", "Non Existing Receiver in the body", null,
						ErrorResponseCode.DOCUMENT_VALIDATION_FAILED, doctypeCode, "Non Existing Receiver in the body");
			}

			if ((plSender == null) || (plReceiver == null) || (!plSender.getId().equals(mainMessage.getSender().getId()))
					|| (!plReceiver.getId().equals(mainMessage.getReceiver().getId()))) {
				mainMessage.setParentMessages(null);
				messageService.updateMessage(mainMessage);
				throw new BusinessException("soapenv:Client", "Sender and Receiver in Header and Body are not the same", null,
						ErrorResponseCode.DOCUMENT_VALIDATION_FAILED, doctypeCode, "Sender and Receiver in Header and Body are not the same");
			}
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Unable to retrieve information with query ", null, "301", "Technical Error Occured");
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Could not attach the parent document", null, "301", "Technical Error Occured");
		}
		return message;
	}
}
