package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.BusinessDomainElement;
import eu.europa.ec.cipa.admin.web.dto.MessageForm;
import eu.europa.ec.cipa.admin.web.dto.MessageSearchForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.logs.UserActionsLog;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.utils.AjaxResult;
import eu.europa.ec.cipa.admin.web.utils.Referer;
import eu.europa.ec.cipa.admin.web.validators.MessageSearchValidator;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.log.Log;
import eu.europa.ec.cipa.etrustex.domain.routing.MessageRouting;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import eu.europa.ec.cipa.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.cipa.etrustex.services.*;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.util.DispatchEnum;
import eu.europa.ec.cipa.etrustex.types.DocumentStatusCode;
import eu.europa.ec.cipa.etrustex.types.MessageBinaryType;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.Item;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping(value = "/message")
@SessionAttributes({"messageForm", "messageSearchForm"})
@PreAuthorize("hasAnyRole('ADM', 'SUP')")
public class MessageController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Autowired private IMessageService messageService;
	@Autowired private MessageSource  messageSource;
    @Autowired private IPartyService partyService;
    @Autowired private IInterchangeAgreementService interchangeAgreementService;
    @Autowired private ITransactionService transactionService;
    @Autowired private IMessageRoutingService messageRoutingService;
    @Autowired private IDocumentService documentService;
    @Autowired private ILogService logService;
    @Autowired private MessageSearchValidator messageSearchValidator;
    @Autowired private JmsTemplate toRoutingQueueTemplate;
    @Autowired private IMetadataService metadataService;
    @Autowired private LogServiceHelper logServiceHelper;
	@Autowired private Referer refererPage;

	/*--------------------------------------------------------------------*/
	/*----------------------------- Bindings -----------------------------*/
	/*--------------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true){
            /*
             *  TODO. find out why after party search dates, if not picked, arrive as ","
             */
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.hasText(text) && text.equals(",")) {
                    setValue(null);
                } else{
                    super.setAsText(text);
                }
            }
        });
    }

    @InitBinder("messageSearchForm")
    public void initSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(messageSearchValidator);
    }


    /*-----------------------------------------------------------------*/
	/*------------------------ Model Attributes -----------------------*/
	/*-----------------------------------------------------------------*/
    @ModelAttribute("messageSearchForm")
    public MessageSearchForm messageSearchForm() {
        MessageSearchForm messageSearchForm = new MessageSearchForm();
        messageSearchForm.setCreationDateFrom(null);
        messageSearchForm.setCreationDateTo(null);
        return messageSearchForm;
    }

    @ModelAttribute("documentTypeCodes")
    public List<String> documentTypeCodes() {
        return messageService.getDocumentTypeCodes();
    }

    @ModelAttribute("statusCodes")
    public List<String> statusCodes() {
        return messageService.getStatusCodes();
    }


    /*-----------------------------------------------------------------*/
	/*-------------------- Handler methods ----------------------------*/
	/*-----------------------------------------------------------------*/
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@RequestParam(value="fromView", required=false) boolean fromView, @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm, Model model) {
		// Run search if coming back from view.
		if (fromView) {
			messageSearchForm.setSearchOnLoad(true);
		} else {
			messageSearchForm = new MessageSearchForm();
			messageSearchForm.setSearchOnLoad(false);
		}

		populateSearchForm(model, messageSearchForm, "");
		refererPage.setAction("search");

		return "page.messageSearch";
	}

	@RequestMapping(value = "/search/load", method = RequestMethod.POST)
	public String searchLoad(@RequestParam(value="businessDomain", required=false) Long businessDomainId,
							 @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm, Model model, SessionStatus sessionStatus) {

		populateSearchForm(model, messageSearchForm, "");
		sessionStatus.setComplete();

		return "frag.messageSearch.inner";
	}

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = Message.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size,
                                @ModelAttribute("messageSearchForm") @Valid MessageSearchForm messageSearchForm, BindingResult result,
                                @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        messageSearchForm.setSearchOnLoad(false);

        if (result.hasErrors()){
            populateSearchForm(model, messageSearchForm, "");

            return "frag.messageSearch.inner";
        }

        int sizeNo = size == null ? 50 : size.intValue();
        final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;

        Message message = new Message();
        message.setAccessInfo(new EntityAccessInfo());


        List<Long> businessDomainIds = new ArrayList<>();
        if(messageSearchForm.getBusinessDomain().getId().equals(new Long("-1"))) {
            for (BusinessDomainElement bd :  userInfo.getBusinessDomains()) {
                businessDomainIds.add(bd.getId());
            }
        } else {
            businessDomainIds.add(messageSearchForm.getBusinessDomain().getId());
        }

        if(StringUtils.hasText(messageSearchForm.getCreationId())) {
            message.getAccessInfo().setCreationId(messageSearchForm.getCreationId().trim());
        }

        if(StringUtils.hasText(messageSearchForm.getCorrelationId())) {
            message.setCorrelationId(messageSearchForm.getCorrelationId().trim());
        }

        if(StringUtils.hasText(messageSearchForm.getDocumentId())) {
            message.setDocumentId(messageSearchForm.getDocumentId().trim());
        }

        if(StringUtils.hasText(messageSearchForm.getMessageDocumentTypeCode())) {
            message.setMessageDocumentTypeCode(messageSearchForm.getMessageDocumentTypeCode());
        }

        if(messageSearchForm.getRetrieveIndicator() != null) {
            message.setRetrieveIndicator(messageSearchForm.getRetrieveIndicator());
        }

        if(messageSearchForm.getIssuer().getId() != null) {
            message.setIssuer(messageSearchForm.getIssuer());
        }

        if(messageSearchForm.getSender().getId() != null) {
            message.setSender(messageSearchForm.getSender());
        }

        if(messageSearchForm.getReceiver().getId() != null) {
            message.setReceiver(messageSearchForm.getReceiver());
        }

        if(messageSearchForm.getAgreement().getId() != null) {
            message.setAgreement(messageSearchForm.getAgreement());
        }

        if(messageSearchForm.getTransaction().getId() != null) {
            message.setTransaction(messageSearchForm.getTransaction());
        }

        if(StringUtils.hasText(messageSearchForm.getStatusCode())) {
            message.setStatusCode(messageSearchForm.getStatusCode());
        }



        List<Message> messages = messageService.findMessagesByCriteria(message, messageSearchForm.getCreationDateFrom(), messageSearchForm.getCreationDateTo(), firstResult, sizeNo, businessDomainIds, messageSearchForm.getDispatched());

        long total = messageService.countMessagesByCriteria(message, messageSearchForm.getCreationDateFrom(), messageSearchForm.getCreationDateTo(), businessDomainIds, messageSearchForm.getDispatched());
        float nrOfPages = (float) total / sizeNo;

        model.addAttribute("messages", messages);
        model.addAttribute("total", total);
        model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));

        populateSearchForm(model, messageSearchForm, "");

        return "frag.messageSearch.inner";
    }

    @RequestMapping(value = "/search/add/party/{id}", method = RequestMethod.GET)
    public String searchAddParty(@PathVariable("id") Long partyId, @RequestParam(value = "partyType", required = false) String partyType,
                                 @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm, @ActiveUser SessionUserInformation userInfo, Model model){
        Party party = partyService.getParty(partyId);

        switch (partyType) {
            case "issuer":
                messageSearchForm.setIssuer(party);
                break;

            case "sender":
                messageSearchForm.setSender(party);
                break;

            case "receiver":
                messageSearchForm.setReceiver(party);
                break;

            default: // Should not happen
                break;
        }

        populateSearchForm(model, messageSearchForm, "");

        return "frag.message." + partyType;
    }

    @RequestMapping(value = "/search/add/ica/{id}", method = RequestMethod.GET)
    public String searchAddIca(@PathVariable("id") Long icaId, @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm, @ActiveUser SessionUserInformation userInfo, Model model){
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);
        messageSearchForm.setAgreement(ica);
        populateSearchForm(model, messageSearchForm, "");

        return "frag.message.ica";
    }

    @RequestMapping(value = "/search/add/transaction/{id}", method = RequestMethod.GET)
    public String searchAddTransaction(@PathVariable("id") Long transactionId, @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm, @ActiveUser SessionUserInformation userInfo, Model model){
        Transaction transaction = transactionService.getTransaction(transactionId);
        messageSearchForm.setTransaction(transaction);
        populateSearchForm(model, messageSearchForm, "");

        return "frag.message.transaction";
    }

    @RequestMapping(value = "/{messageId}/view", method = RequestMethod.GET)
	public String view(@PathVariable("messageId") Long id, @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm, Model model, HttpServletRequest request) {
    	populateSearchForm(model, messageSearchForm, "view");
		model.addAttribute("messageId", id);

		return "page.message";
	}

    @RequestMapping(value = "/{messageId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("messageId") Long id, @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm,
                           SessionStatus sessionStatus, Model model) {
        try{
            Message message = messageService.retrieveMessageWithInitializedProperties(id);
            List<MessageRouting> messageRoutings = messageRoutingService.findByMessageId(id);
            DispatchEnum dispatched = DispatchEnum.NO;

            /*
             * Agreed by the team: (JIRA ETRUSTEX-1746)
             * -	Yes = The message has been dispatched to all active endpoints
             * -	No = The message has not been dispatched to any active endpoint
             * -	Partial = The message has been dispatched to some of the active endpoints, but not to all
             */
            for (MessageRouting mr : messageRoutings) {
                if (mr.getEndpoint().getIsActive()) {
                    if(BooleanUtils.isTrue(mr.getProcessed())) {
                        dispatched = DispatchEnum.YES;
                    } else if (dispatched.equals(DispatchEnum.YES)) {
                        dispatched = DispatchEnum.PARTIAL;
                        break;
                    }
                }
            }

            populateForm(model, new MessageForm(message, dispatched), "view");
        } catch (Exception e) {
            populateForm(model, new MessageForm(), "view");
        }

        populateSearchForm(model, messageSearchForm, "view");

        return "frag.messageView.inner";
    }

    @RequestMapping(value = "/{messageId}/show/{stuff}", method = RequestMethod.POST)
    public String showMessageStuff(@PathVariable("messageId") Long id, @PathVariable("stuff") String stuff,
                                   @ModelAttribute("messageSearchForm") MessageSearchForm messageSearchForm,
                           SessionStatus sessionStatus, Model model) {
        Message message = messageService.retrieveMessageWithInitializedProperties(id);

        /*
         * stuff can be [binaries, routing, parent, child]
         */
        switch(stuff){
            case "binaries" :
                model.addAttribute("binaries", message.getBinaries());
                break;
            case "routing" :
                model.addAttribute("isInReceivedState", message.getStatusCode().equals(DocumentStatusCode.RECEIVED.name()));
                model.addAttribute("routing", messageRoutingService.findByMessageId(id));
                break;
            case "parent" :
                Set<Message> parents = new HashSet<>();
                for(Message m : message.getParentMessages()) {
                    parents.add(messageService.retrieveMessageWithInitializedProperties(m.getId()));
                }
                model.addAttribute("parents", parents);

                break;
            case "child" :
                Set<Message> children = new HashSet<>();
                for(Message m : message.getChildMessages()) {
                    children.add(messageService.retrieveMessageWithInitializedProperties(m.getId()));
                }
                model.addAttribute("children", children);

                break;
        }

        populateSearchForm(model, messageSearchForm, "view");

        return "frag.messageShow." + stuff + ".dialog";
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{msgRoutingId}/redispatch", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult redispatch(@PathVariable Long msgRoutingId, SessionStatus sessionStatus, Model model) {
        try{
            MessageRouting messageRouting = messageRoutingService.findById(msgRoutingId);
            Message message = messageService.retrieveMessageWithInitializedProperties(messageRouting.getMessage().getId());
            TrustExMessage<String> trustExMessage = getTrustExMessage(message);
            trustExMessage.getHeader().setMessageRoutingId(String.valueOf(msgRoutingId));
            toRoutingQueueTemplate.convertAndSend(trustExMessage);
            saveLog(trustExMessage, LogDTO.LOG_OPERATION.REDISPATCH);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new AjaxResult(false, messageSource.getMessage("error.message.redispatch", null, LocaleContextHolder.getLocale()), msgRoutingId);
        }

        return new AjaxResult(true, messageSource.getMessage("message.redispatch.success", null, LocaleContextHolder.getLocale()), msgRoutingId);
    }

    private TrustExMessage<String> getTrustExMessage(Message message) throws XPathException {
        TrustExMessage<String> trustExMessage = new TrustExMessage<>(null);
        TrustExMessageHeader header = new TrustExMessageHeader();
        Transaction transaction = message.getTransaction();
        Document document = documentService.getDocument(transaction.getDocument().getId());
        LogDTO logDTO = new LogDTO.LogDTOBuilder(null, LogDTO.LOG_OPERATION.DISPATCHING, null)
                .messageId(message.getId())
                .build();
        Log etrustexLog = logService.findLogsByCriteria(logDTO, null, null, 0, 1).get(0);
        Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData((Long) null, null, null, null);
        String separator = metadata.get(MetaDataItemType.SCHEME_ID_SEPARATOR).getValue();
        PartyIdentifier senderIdentifier = message.getSender().getIdentifiers().iterator().next();
        PartyIdentifier receiverIdentifier = message.getReceiver().getIdentifiers().iterator().next();

        header.setMessageId(message.getId());
        header.setAuthenticatedUser(message.getAccessInfo().getCreationId());
        header.setMessageDocumentId(message.getDocumentId());
        header.setCorrelationId(message.getCorrelationId());
        header.setMessageDocumentVersion(document.getVersion());
        header.setSenderIdWithScheme(senderIdentifier.getSchemeId() + separator + senderIdentifier.getValue()); // identifierSchemeId : identifierValue
        header.setReceiverIdWithScheme(receiverIdentifier.getSchemeId() + separator + receiverIdentifier.getValue()); // identifierSchemeId : identifierValue
        header.setTransactionTypeId(message.getTransaction().getId());
        header.setIssuerPartyId(message.getIssuer() != null ? message.getIssuer().getId() : null);
        header.setSenderPartyId(message.getSender().getId());
        header.setReceiverPartyId(message.getReceiver().getId());
        header.setAvailableNotification(null);
        header.setTransactionNamespace(message.getTransaction().getNamespace());
        header.setTransactionRequestLocalName(message.getTransaction().getRequestLocalName());
        header.setInterchangeAgreementId(message.getAgreement().getId());
        header.setLogCorrelationId(etrustexLog.getCorrelationId());

        // Set parent document id and type code
        setParentDocumentInfo(message, header);

        trustExMessage.setHeader(header);

        return trustExMessage;
    }

    private void setParentDocumentInfo(Message message, TrustExMessageHeader header) throws XPathException {
        Transaction t = message.getTransaction();
        Map<MetaDataItemType, MetaDataItem> metadata = metadataService.retrieveMetaData(null, t, t.getDocument(), t.getProfiles());

        // get raw data
        MessageBinary messageBinary = message.getMessageBinaryByBinaryType(MessageBinaryType.RAW_MESSAGE);

        // find parent document id
        Configuration config = new Configuration();
        DocumentInfo docInfo = config.buildDocument(new StreamSource(new ByteArrayInputStream(messageBinary.getBinary())));
        DynamicQueryContext dynamicContext = new DynamicQueryContext(config);
        dynamicContext.setContextItem(docInfo);

        final StaticQueryContext sqc = config.newStaticQueryContext();

        MetaDataItem parentIdQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_ID_XQUERY);

        // If parent id metadata is not present, means that there is no pareent document
        if (parentIdQueryItem != null){
            Item it ;
            final XQueryExpression parentIdQuery = sqc.compileQuery(parentIdQueryItem.getValue());
            it = (Item)parentIdQuery.evaluateSingle(dynamicContext);
            if (it != null){
                header.setMessageParentDocumentId(StringUtils.trimWhitespace(it.getStringValue()));
            }

            MetaDataItem parentTCQueryItem = metadata.get(MetaDataItemType.PARENT_DOCUMENT_TYPECODE_XQUERY);
            if (parentTCQueryItem != null){
                final XQueryExpression parentTCQuery = sqc.compileQuery(parentTCQueryItem.getValue());
                it = (Item)parentTCQuery.evaluateSingle(dynamicContext);
                if (it != null){
                    header.setMessageParentDocumentTypeCode(StringUtils.trimWhitespace(it.getStringValue()));
                }
            }
        }
    }


    private void saveLog(TrustExMessage<String> trustExMessage, LogDTO.LOG_OPERATION operation) {
        LogDTO cipAdminlogDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, operation, "Inside MessageController#resubmit", this.getClass().getName());
        cipAdminlogDTO.setModule("CIPADMIN");
        logService.saveLog(cipAdminlogDTO);
    }

    private void populateForm(Model model, MessageForm messageForm,  String pageMode) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("messageForm", messageForm);
        model.addAttribute("messageId", messageForm.getId());
    }

	private void populateSearchForm(Model model, MessageSearchForm messageSearchForm,  String pageMode) {
		model.addAttribute("pageMode", pageMode);
		model.addAttribute("messageSearchForm", messageSearchForm);
        model.addAttribute("dispatchedValues", DispatchEnum.values());
	}
}
