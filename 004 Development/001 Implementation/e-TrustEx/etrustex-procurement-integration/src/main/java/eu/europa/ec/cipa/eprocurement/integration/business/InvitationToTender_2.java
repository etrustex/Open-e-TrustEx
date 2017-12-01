package eu.europa.ec.cipa.eprocurement.integration.business;

import java.io.StringReader;
import java.util.List;

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
import eu.europa.ec.cipa.etrustex.services.exception.UndefinedIdentifierException;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class InvitationToTender_2 extends TrustExBusinessService implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(InvitationToTender_2.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {

		try {
			Message mainMessage = messageService.retrieveMessage(message.getHeader().getMessageId());
			String doctypeCode = mainMessage.getTransaction().getDocument().getDocumentTypeCode();

			final Configuration config = new Configuration();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));

			String senderPID = queryForSingle("//*:Shortlist/*:SenderParty/*:EndpointID", docInfo, config);
			String receiverPID = queryForSingle("//*:Shortlist/*:ReceiverParty/*:EndpointID", docInfo, config);

			// messageService.retrieveMessage(bundleId,
			// parentMessage.getReceiver().getId(),
			// authorisationService.getTransactionsByDocumentTypeCd(BUNDLE_TYPE_CODE).get(0).getId());

			// Hard Business Rules
			if (message.getHeader().getReceiverPartyId() == null) {
				throw new BusinessException("soapenv:Client", "Non existing Receiver Party in the header", null,
						ErrorResponseCode.DOCUMENT_VALIDATION_FAILED, doctypeCode, "Non existing Receiver Party in the header");
			}
			Party issuer = message.getHeader().getIssuer();
			if (!authorisationService.getParty(senderPID, issuer.getBusinessDomain()).getId().equals(mainMessage.getSender().getId())
					|| !authorisationService.getParty(receiverPID, issuer.getBusinessDomain()).getId().equals(mainMessage.getReceiver().getId())) {
				throw new BusinessException("soapenv:Client", "Sender and Receiver in Header and Body are not the same", null,
						ErrorResponseCode.DOCUMENT_VALIDATION_FAILED, doctypeCode, ErrorResponseCode.DOCUMENT_VALIDATION_FAILED.getDescription()
								+ " - Sender and Receiver in Header and Body are not the same");
			}

			List<String> shortlisted = queryFor("//*:Shortlist/*:EconomicOperatorShortList/*:ShortlistedParty/*:PreSelectedParty/*:EndpointID",
					docInfo, config);
			for (String pre : shortlisted) {
				try {
					authorisationService.getParty(pre, issuer.getBusinessDomain());
				} catch (UndefinedIdentifierException e) {
					throw new BusinessException("soapenv:Client", "Preselected Party does not exist", null,
							ErrorResponseCode.DOCUMENT_VALIDATION_FAILED, doctypeCode, ErrorResponseCode.DOCUMENT_VALIDATION_FAILED.getDescription()
									+ " - Preselected Party does not exist");
				}
			}

			shortlisted = queryFor("//*:Shortlist/*:EconomicOperatorShortList/*:ShortlistedParty/*:ForwardedParty/*:EndpointID", docInfo, config);
			for (String pre : shortlisted) {
				try {
					authorisationService.getParty(pre, issuer.getBusinessDomain());
				} catch (UndefinedIdentifierException e) {
					throw new BusinessException("soapenv:Client", "Forwarded Party does not exist", null,
							ErrorResponseCode.DOCUMENT_VALIDATION_FAILED, doctypeCode, ErrorResponseCode.DOCUMENT_VALIDATION_FAILED.getDescription()
									+ " - Forwarded Party does not exist");
				}
			}

		} catch (UndefinedIdentifierException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Unable to retrieve the Sender or Receiver Party", null, "301",
					"Technical Error Occured");
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Unable to retrieve information with query ", null, "301", "Technical Error Occured");
		}
		return message;
	}
}
