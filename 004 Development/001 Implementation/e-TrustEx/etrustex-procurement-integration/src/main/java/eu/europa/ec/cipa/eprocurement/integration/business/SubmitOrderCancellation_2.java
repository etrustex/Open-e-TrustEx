package eu.europa.ec.cipa.eprocurement.integration.business;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.IStateMachineService;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import eu.europa.ec.cipa.etrustex.services.exception.StateMachineException;
import eu.europa.ec.cipa.etrustex.types.DocumentStatusCode;
import eu.europa.ec.cipa.etrustex.types.ErrorResponseCode;

public class SubmitOrderCancellation_2 extends TrustExBusinessService implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(SubmitOrderCancellation_2.class);

	private static final String PARENT_DOC_ID_XPATH = "/*:OrderCancellation/*:OrderReference/*:ID";
	private static final String ORDER_TYPE_CODE = "220";
	private static final String STATE_REJECTED_EVENT = "REJECT";

	@Autowired
	private IStateMachineService smService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {

		try {
			Message mainMessage = messageService.retrieveMessage(message.getHeader().getMessageId());
			String doctypeCode = mainMessage.getTransaction().getDocument().getDocumentTypeCode();

			final Configuration config = new Configuration();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));

			String parentId = queryForSingle(PARENT_DOC_ID_XPATH, docInfo, config);

			// Get the Parent Message: Order
			Message parent = messageService.retrieveMessage(parentId, ORDER_TYPE_CODE, message.getHeader().getSenderPartyId(), message.getHeader()
					.getReceiverPartyId(), false, null);

			// Check if the order exists and has the right state
			if (parent == null
					|| !(DocumentStatusCode.ACCEPTED.name().equals(parent.getStatusCode()) || DocumentStatusCode.PROCESSED.name().equals(
							parent.getStatusCode()))) {
				throw new BusinessException("soapenv:Client", "Parent Document ID does not exist or has a wrong status", null,
						ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST, doctypeCode, "Parent Document ID does not exist or has a wrong status");
			}

			String newStatus = smService.getNextState(parent.getTransaction().getDocument(), parent.getStatusCode(), STATE_REJECTED_EVENT);

			messageService.updateMessageStatus(parent.getId(), newStatus);

			mainMessage.addParentMessage(parent);
			messageService.updateMessage(mainMessage);

		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Unable to retrieve information with query ", null, "301", "Technical Error Occured");
		} catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Could not attach the parent document", null, "301", "Technical Error Occured");
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (StateMachineException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server", "Could not get the next document State", null, "301", "Technical Error Occured");
		}

		return message;
	}

	public IStateMachineService getSmService() {
		return smService;
	}

	public void setSmService(IStateMachineService smService) {
		this.smService = smService;
	}
}
