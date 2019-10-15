package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.BusinessDomainElement;
import eu.europa.ec.etrustex.admin.old.web.dto.LogsSearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.validators.LogsSearchValidator;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.dao.dto.LogDTO.LOG_OPERATION;
import eu.europa.ec.etrustex.dao.dto.LogDTO.LOG_TYPE;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.log.Log;
import eu.europa.ec.etrustex.domain.policy.Policy;
import eu.europa.ec.etrustex.domain.routing.Endpoint;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.types.LogModuleEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/logs")
@PreAuthorize("hasAnyRole('ADM', 'SUP','CBO')")
public class LogsController {
    private static final String ROLE_CBO = "CBO";
    private static final String PAGE_LOGS = "page.logs";
    private static final String FRAG_LOGS = "frag.logs";
    private static final String FRAG_LOGS_RESULTS_CIPADMIN = "frag.logs.results.cipadmin";
    private static final String FRAG_LOGS_RESULTS_ETRUSTEX = "frag.logs.results.etrustex";
    private static final String FRAG_LOGS_DETAILS_CIPADMIN = "frag.logs.details.cipadmin";
    private static final String FRAG_LOGS_DETAILS_ETRUSTEX = "frag.logs.details.etrustex";

    @Autowired
    private ILogService logService;
    @Autowired
    private IPartyService partyService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LogsSearchValidator logsSearchValidator;


    /*-----------------------------------------------------------------*/
    /*---------------------------- Binding ----------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @InitBinder("logsSearchForm")
    public void initPartyFormBinder(WebDataBinder binder) {
        binder.setValidator(logsSearchValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("logsSearchForm")
    public LogsSearchForm logsSearchForm() {
        return new LogsSearchForm();
    }

    @ModelAttribute("entities")
    public List<String> entities() {
        List<String> entityNames = new ArrayList<String>();

        entityNames.add(Endpoint.class.getSimpleName());
        entityNames.add(InterchangeAgreement.class.getSimpleName());
        entityNames.add(Party.class.getSimpleName());
        entityNames.add(PartyAgreement.class.getSimpleName());
        entityNames.add(User.class.getSimpleName());
        entityNames.add(Document.class.getSimpleName());
        entityNames.add(Message.class.getSimpleName());
        entityNames.add(MetaDataItem.class.getSimpleName());
        entityNames.add(Profile.class.getSimpleName());
        entityNames.add(Policy.class.getSimpleName());
        entityNames.add(Role.class.getSimpleName());
        entityNames.add(Transaction.class.getSimpleName());
        entityNames.add(Log.class.getSimpleName());

        Collections.sort(entityNames);
        return entityNames;
    }

    @ModelAttribute("cboEntities")
    public List<String> cboEntities() {
        List<String> entityNames = new ArrayList<String>();

        entityNames.add(Endpoint.class.getSimpleName());
        entityNames.add(InterchangeAgreement.class.getSimpleName());
        entityNames.add(Party.class.getSimpleName());
        entityNames.add(PartyAgreement.class.getSimpleName());
        entityNames.add(User.class.getSimpleName());

        Collections.sort(entityNames);
        return entityNames;
    }

    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("module") LogModuleEnum module,
                         @ModelAttribute("logsSearchForm") LogsSearchForm logsSearchForm, Model model) {
        logsSearchForm.setModule_search(module);
        populateSearchForm(model, logsSearchForm, "");

        return PAGE_LOGS;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@ModelAttribute("logsSearchForm") LogsSearchForm logsSearchForm, Model model, SessionStatus sessionStatus, @ActiveUser SessionUserInformation userInfo) {
        BusinessDomain bd = new BusinessDomain();
        bd.setId(userInfo.getBusinessDomain().getId());
        logsSearchForm.setBusinessDomain_search(bd);
        populateSearchForm(model, logsSearchForm, "");
        sessionStatus.setComplete();
        return FRAG_LOGS;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = Log.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size,
                                @ModelAttribute("logsSearchForm") @Valid LogsSearchForm logsSearchForm, BindingResult result,
                                Model model, SessionStatus sessionStatus, HttpServletRequest request, @ActiveUser SessionUserInformation userInfo) {
        sessionStatus.setComplete();

        if (result.hasErrors()) {
            populateSearchForm(model, logsSearchForm, "");

            return logsSearchForm.getModule_search().name().equalsIgnoreCase(LogModuleEnum.ETXADMIN.name()) ? FRAG_LOGS_RESULTS_CIPADMIN : FRAG_LOGS_RESULTS_ETRUSTEX;
        }

        int sizeNo = size == null ? 50 : size.intValue();
        final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;

        List<Log> logs;
        long total;

        BusinessDomain businessDomain = null;
        Set<Long> bDomains = new HashSet<Long>();
        if (logsSearchForm.getBusinessDomain_search() != null && !logsSearchForm.getBusinessDomain_search().getId().equals(Long.valueOf("-1"))) {
            businessDomain = logsSearchForm.getBusinessDomain_search();
        }else{
            for (BusinessDomainElement el :userInfo.getBusinessDomains()) {
                bDomains.add(el.getId());
            }
        }

        LogDTO.LOG_TYPE logType = !StringUtils.isEmpty(logsSearchForm.getLogType_search()) ? LogDTO.LOG_TYPE.valueOf(logsSearchForm.getLogType_search()) : null;
        LogDTO.LOG_OPERATION logOperation = !StringUtils.isEmpty(logsSearchForm.getOperation_search()) ? LogDTO.LOG_OPERATION.valueOf(logsSearchForm.getOperation_search()) : null;

        // Only for Etrustex logs
        Long issuerPartyId = null;
        Long senderPartyId = null;
        Long receiverPartyId = null;
        Party issuerParty = null;
        Party senderParty = null;
        Party receiverParty = null;

        if (logsSearchForm.getModule_search().name().equalsIgnoreCase(LogModuleEnum.ETRUSTEX.name())) {
            issuerPartyId = logsSearchForm.getIssuerParty_search() != null ? logsSearchForm.getIssuerParty_search().getId() : null;
            senderPartyId = logsSearchForm.getSenderParty_search() != null ? logsSearchForm.getSenderParty_search().getId() : null;
            receiverPartyId = logsSearchForm.getReceiverParty_search() != null ? logsSearchForm.getReceiverParty_search().getId() : null;

            issuerParty = issuerPartyId != null ? partyService.getParty(issuerPartyId) : null;
            senderParty = senderPartyId != null ? partyService.getParty(senderPartyId) : null;
            receiverParty = receiverPartyId != null ? partyService.getParty(receiverPartyId) : null;
        }

        List<String> entities = null;
        if(ROLE_CBO.equals(userInfo.getRole().getCode())){
            entities = cboEntities();
        }

        /*
         * In the eTrustEx logs search criteria a non existent party id can be input. In which case there are no results.
         */
        if (logsSearchForm.getModule_search().name().equalsIgnoreCase(LogModuleEnum.ETRUSTEX.name())
                && (issuerPartyId == null ^ issuerParty == null) || (senderPartyId == null ^ senderParty == null) || (receiverPartyId == null ^ receiverParty == null)) {
            logs = new ArrayList<>();
            total = 0L;
        } else {
            LogDTO logDTO = new LogDTO.LogDTOBuilder(logType, logOperation, this.getClass().getName())
                    .businessDomain(businessDomain)
                    .businessDomains(bDomains)
                    .module(logsSearchForm.getModule_search())
                    .username(logsSearchForm.getUsername_search())
                    .userRole(logsSearchForm.getUserRole_search())
                    .urlContext(logsSearchForm.getUrlContext_search())
                    .authIpAddress(logsSearchForm.getAuthIpAddress_search())
                    .correlationId(logsSearchForm.getCorrelationId_search())
                    .entity(logsSearchForm.getEntity_search())
                    .entityId(logsSearchForm.getEntityId_search())
                    .entities(entities)
                    .description(logsSearchForm.getDescription_search())
                    .largeValue(logsSearchForm.getLargeValue_search())
                    .documentTypeCode(logsSearchForm.getDocumentTypeCode_search())
                    .documentId(logsSearchForm.getDocumentId_search())
                    .businessCorrelationId(logsSearchForm.getBusinessCorrelationId_search())
                    .issuerParty(issuerParty)
                    .senderParty(senderParty)
                    .receiverParty(receiverParty)
                    .build();

            logs = logService.findLogsByCriteria(logDTO, logsSearchForm.getCreationDateFrom(), logsSearchForm.getCreationDateTo(), firstResult, sizeNo);

            total = logService.count(logDTO, logsSearchForm.getCreationDateFrom(), logsSearchForm.getCreationDateTo());
        }


        float nrOfPages = (float) total / sizeNo;

        model.addAttribute("logs", logs);
        model.addAttribute("total", total);
        model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));

        populateSearchForm(model, logsSearchForm, "");

        return logsSearchForm.getModule_search().name().equalsIgnoreCase(LogModuleEnum.ETXADMIN.name()) ? FRAG_LOGS_RESULTS_CIPADMIN : FRAG_LOGS_RESULTS_ETRUSTEX;
    }

    @UserActionsLog(entity = Log.class)
    @RequestMapping(value = "/{logId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("logId") Long id,
                           @RequestParam("module") String module, Model model) {
        model.addAttribute("log", logService.getLog(id));

        return module.equalsIgnoreCase(LogModuleEnum.ETXADMIN.name()) ? FRAG_LOGS_DETAILS_CIPADMIN : FRAG_LOGS_DETAILS_ETRUSTEX;
    }


    private void populateSearchForm(Model model, LogsSearchForm logsSearchForm, String pageMode) {
        List<LOG_TYPE> logTypes = new ArrayList<>();
        List<LOG_OPERATION> logOperations;

        String currentModule = logsSearchForm.getModule_search().name();

        if (currentModule.equalsIgnoreCase(LogModuleEnum.ETXADMIN.name())) {
            logTypes.add(LOG_TYPE.ERROR);
            logTypes.add(LOG_TYPE.SUCCESS);

            logOperations = LOG_OPERATION.etxadminOperations();

        } else {
            logTypes = Arrays.asList(LogDTO.LOG_TYPE.values());

            logOperations = LOG_OPERATION.eTrustexOperations();
        }

        Collections.sort(logTypes);

        model.addAttribute("logTypes", logTypes);
        model.addAttribute("operations", logOperations);
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("logsSearchForm", logsSearchForm);
    }
}