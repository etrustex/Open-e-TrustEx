package eu.europa.ec.eprocurement.integration.business;

import java.io.StringReader;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.dto.MessageQueryDTO;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

public abstract class SubmitCreditNoteBase extends TrustExBusinessService implements IASynchBusinessService {

	private static final Logger logger = LoggerFactory.getLogger(SubmitCreditNoteBase.class);
	public static final String INVOICE_TYPECODE = "380";

	public SubmitCreditNoteBase() {
		super();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message) throws BusinessException, TechnicalErrorException {
			try {
				
				final Configuration config = new Configuration();
				DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
				
				List<String> res = queryFor("//*:CreditNote/*:BillingReference/*:InvoiceDocumentReference/*:ID",docInfo, config);
				String parentDocId = null;
				if(res.size() == 1){
					parentDocId = res.get(0);
				}
				
				res = queryFor("//*:CreditNote/*:BillingReference/*:InvoiceDocumentReference/*:DocumentTypeCode",docInfo, config);
				String parentTypeCode = null;
				if(res.size() == 1){
					parentTypeCode = res.get(0);
				}
	
				if(parentDocId != null && parentDocId.length() > 0 && parentTypeCode != null && INVOICE_TYPECODE.equals(parentTypeCode)){
					MessageQueryDTO mq = new MessageQueryDTO();					
					mq.setMessageDocumentId(parentDocId);
					mq.setDocumentTypeCode(parentTypeCode);
					List<Message> msgs = messageService.retrieveMessages(mq);
					if (msgs.size() == 1){
						Message mainMessage = messageService.retrieveMessage(message.getHeader().getMessageId());

                        Message parentMessage = msgs.get(0);
                        parentMessage = messageService.retrieveMessage(parentMessage.getId());

                        if(mainMessage.getSender().getId().equals(parentMessage.getSender().getId())
                                && mainMessage.getReceiver().getId().equals(parentMessage.getReceiver().getId())) {
                            mainMessage.addParentMessage(parentMessage);
                            messageService.updateMessage(mainMessage);
                        }
					}
				}
			} catch (XPathException e) {
				logger.error(e.getMessage(), e);
				throw new TechnicalErrorException("soapenv:Server",
						"Unable to retrieve information with query ", null,
						"301", "Technical Error Occured");
			} catch (MessageUpdateException e) {
				logger.error(e.getMessage(), e);
				throw new TechnicalErrorException("soapenv:Server",
						"Unable to attach parent document", null,
						"301", "Technical Error Occured");
			}
			return message;
		}

}