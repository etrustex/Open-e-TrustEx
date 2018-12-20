package eu.europa.ec.eprocurement.integration.business;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.common.exception.TechnicalErrorException;
import eu.europa.ec.etrustex.dao.exception.MessageUpdateException;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.integration.api.IASynchBusinessService;
import eu.europa.ec.etrustex.integration.business.TrustExBusinessService;
import eu.europa.ec.etrustex.integration.exception.BusinessException;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.types.ErrorResponseCode;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;

@Component("SubmitReminderRequest-2.0")
public class SubmitReminderRequest_2  extends TrustExBusinessService implements IASynchBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(SubmitReminderRequest_2.class);

	private static final String PARENT_DOC_ID_XPATH    = "/*:Reminder/*:ReminderLine/*:BillingReference/*:InvoiceDocumentReference/*:ID";
	private static final String PARENT_DOCTYPE_XPATH   = "/*:Reminder/*:ReminderLine/*:BillingReference/*:InvoiceDocumentReference/*:DocumentTypeCode";
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TrustExMessage<String> processMessage(TrustExMessage<String> message)
			throws BusinessException, TechnicalErrorException {
				
		try {			
			Set<String> states = new HashSet<String>(Arrays.asList("ERROR", "SUBMITTED"));
			
			Message mainMessage = messageService.retrieveMessage(message.getHeader().getMessageId());			
			String doctypeCode = mainMessage.getTransaction().getDocument().getDocumentTypeCode();
			
			final Configuration config = new Configuration();
			DocumentInfo docInfo = config.buildDocument(new StreamSource(new StringReader(message.getPayload())));
			
			List<String> parentIdList = queryFor(PARENT_DOC_ID_XPATH,docInfo, config);
			List<String> parentDocTypeList = queryFor(PARENT_DOCTYPE_XPATH,docInfo, config);
			
			Message parent = null;
			if(parentIdList.size()  > 0){
				parent = messageService.retrieveMessage(parentIdList.get(0).trim(), parentDocTypeList.get(0), message.getHeader().getSenderPartyId(), message.getHeader().getReceiverPartyId(), false, states); 
				if (parent == null){
					throw new BusinessException(
							"soapenv:Client",
							"Parent Document ID does not exist",
							null,ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST,doctypeCode,
							"Parent Document ID does not exist");
				}
				for (Message child : parent.getChildMessages()) {
					if(doctypeCode.equals(child.getTransaction().getDocument().getDocumentTypeCode()) && DateUtils.isSameDay(child.getIssueDate(), mainMessage.getIssueDate())){
						throw new BusinessException(
								"soapenv:Client",
								ErrorResponseCode.INVOICE_REMINDER_ALREADY_EXISTS.getDescription(),
								null,ErrorResponseCode.INVOICE_REMINDER_ALREADY_EXISTS,doctypeCode,
								ErrorResponseCode.INVOICE_REMINDER_ALREADY_EXISTS.getDescription());
					}
				}
				mainMessage.addParentMessage(parent);
				messageService.updateMessage(mainMessage);
			}else{
				throw new BusinessException(
						"soapenv:Client",
						"Parent Document ID does not exist",
						null,ErrorResponseCode.DOCUMENT_PARENT_ID_NOT_EXIST,doctypeCode,
						"Parent Document ID does not exist");
			}			
		} catch (XPathException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Unable to retrieve information with query ", null,
					"301", "Technical Error Occured");
		} 
		catch (MessageUpdateException e) {
			logger.error(e.getMessage(), e);
			throw new TechnicalErrorException("soapenv:Server",
					"Could not attach the parent document", null,
					"301", "Technical Error Occured");
		}catch(BusinessException e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		return message;
	}
	

}
