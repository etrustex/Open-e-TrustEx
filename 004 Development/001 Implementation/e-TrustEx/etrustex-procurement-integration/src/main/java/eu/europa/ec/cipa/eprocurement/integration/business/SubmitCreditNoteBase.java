package eu.europa.ec.cipa.eprocurement.integration.business;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.cipa.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;
import eu.europa.ec.cipa.etrustex.integration.exception.TechnicalErrorException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.services.exception.MessageUpdateException;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.List;

public abstract class SubmitCreditNoteBase extends TrustExBusinessService implements IASynchBusinessService {

	public static final Logger logger = LoggerFactory.getLogger(SubmitCreditNoteBase.class);
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
					List<Message> msgs = messageService.retrieveMessages(null, null, null, null, null, null, parentDocId, parentTypeCode, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
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



	//				else{
	//					throw new BusinessException(
	//							"soapenv:Client",
	//							"Parent Document ID does not exist",
	//							null,ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST,doctypeCode,
	//							"Parent Document ID does not exist");
	//				}
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