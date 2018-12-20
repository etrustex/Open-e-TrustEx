package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.*;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.services.IICANotificationService;
import eu.europa.ec.etrustex.admin.old.web.utils.*;
import eu.europa.ec.etrustex.admin.old.web.validators.ICACompositeValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.InterchangeAgreementSearchValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.dao.dto.ICASearchDTO;
import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.*;
import eu.europa.ec.etrustex.services.admin.IUserService;
import eu.europa.ec.etrustex.services.report.xls.ICAResultXlsExportService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ica")
@SessionAttributes({"interchangeAgreement", "interchangeAgreementSearchForm", "metadataForm"})
@PreAuthorize("isFullyAuthenticated()")
public class IcaController {
    private static final String PAGE_ICA = "page.interchangeAgreement";
    private static final String FRAG_ICA_INNER = "page.ica.inner";
    private static final String PAGE_ICA_SEARCH = "page.icaSearch";
    private static final String FRAG_ICA_SEARCH = "page.icaSearch.inner";
    private static final String FRAG_ICA_SEARCH_POPUP = "page.icaSearchPopup.inner";
    private static final String FRAG_ICA_VIEW = "page.icaView.inner";
    private static final String FRAG_ICA_VIEW_POPUP = "page.icaViewPopup.inner";
    private static final String FRAG_ICA_PARTY = "fragment.ica.party";
    private static final String FRAG_ICA_PARTY_DELEGATES = "fragment.ica.partyDelegates";
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IPartyService partyService;
    @Autowired
    private IProfileService profileService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IICANotificationService icaNotificationService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICAResultXlsExportService xlsReport;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private InterchangeAgreementSearchValidator icaSearchValidator;
    @Autowired
    private ICACompositeValidator icaValidator;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private ICAUtil icaUtil;
    @Autowired
    private BusinessDomainUtil businessDomainUtil;


    @Autowired
    private Referer refererPage;


    /*-----------------------------------------------------------------*/
    /*---------------------------- Binding ----------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @InitBinder("interchangeAgreementSearchForm")
    public void initSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(icaSearchValidator);
    }

    @InitBinder("interchangeAgreement")
    public void initFormBinder(WebDataBinder binder) {
        binder.setValidator(icaValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("interchangeAgreementSearchForm")
    public InterchangeAgreementSearchForm getInterchangeAgreementSearchForm() {
        return new InterchangeAgreementSearchForm();
    }

    // Populates interchangeAgreement if it is null
    @ModelAttribute("interchangeAgreement")
    public InterchangeAgreementForm getInterchangeAgreement() {
        return new InterchangeAgreementForm();
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Exception Handlers ---------------------*/
    /*-----------------------------------------------------------------*/
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException iae) {
        log.error(iae.getMessage(), iae);
        req.getSession().setAttribute("interchangeAgreement", new InterchangeAgreementForm());
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam(value = "partyId", required = false) Long partyId, @ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement, Model model) {
        if (partyId != null) {
            interchangeAgreement.setFirstParty(new PartyForm(partyService.getParty(partyId)));
            refererPage.setController(null); // At this point it's value is IcaController. We clear it, otherwise remains in session
        } else {
            interchangeAgreement = new InterchangeAgreementForm();
        }

        if (interchangeAgreement.getIcaValidityStartDate() == null) {
            interchangeAgreement.setIcaValidityStartDate(new Date());
        }

        model.addAttribute("interchangeAgreement", interchangeAgreement);
        model.addAttribute("pageMode", "new");
        model.addAttribute("isMultiple", false);
        refererPage.setAction("create");

        return PAGE_ICA;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(@ActiveUser SessionUserInformation userInfo, @ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement,
                             Model model, SessionStatus sessionStatus) {
        populateForm(model, interchangeAgreement, "new", userInfo);

        sessionStatus.setComplete();

        return FRAG_ICA_INNER;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @UserActionsLog(entity = InterchangeAgreement.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@Valid @ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement, BindingResult result,
                           @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm interchangeAgreementSearchForm,
                           @ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, HttpServletRequest request) throws Exception {
        Long icaId;

        if (result.hasErrors()) {
            sessionStatus.setComplete();
            return new AjaxResult(false, messageSource.getMessage("error.ica.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), interchangeAgreement.getId());
        }

        log.debug("NO binding errors");

        Date currentDate = Calendar.getInstance().getTime();
        String currentUser = userInfo.getUsername();

        EntityAccessInfo eai = new EntityAccessInfo();

        PartyAgreement firstDelegation = null;
        if (interchangeAgreement.getFirst3rdParty() != null && FormUtil.convertDefaultOptionToNull(interchangeAgreement.getFirst3rdParty().getId()) != null) {
            log.debug("First party delegates to third party.");
            firstDelegation = new PartyAgreement();
            firstDelegation.setAccessInfo(eai);
            firstDelegation.getAccessInfo().setCreationDate(currentDate);
            firstDelegation.getAccessInfo().setCreationId(currentUser);
            firstDelegation.setAuthorizingParty(partyService.getParty(interchangeAgreement.getFirstParty().getId()));
            firstDelegation.setDelegateParty(partyService.getParty(interchangeAgreement.getFirst3rdParty().getId()));
        }

        PartyAgreement secondDelegation = null;
        if (interchangeAgreement.getSecond3rdParty() != null && FormUtil.convertDefaultOptionToNull(interchangeAgreement.getSecond3rdParty().getId()) != null) {
            log.debug("Second party delegates to third party.");
            secondDelegation = new PartyAgreement();
            secondDelegation.setAccessInfo(eai);
            secondDelegation.getAccessInfo().setCreationDate(currentDate);
            secondDelegation.getAccessInfo().setCreationId(currentUser);
            secondDelegation.setAuthorizingParty(partyService.getParty(interchangeAgreement.getSecondParty().getId()));
            secondDelegation.setDelegateParty(partyService.getParty(interchangeAgreement.getSecond3rdParty().getId()));
        }

        if (FormUtil.convertDefaultOptionToNull(interchangeAgreement.getId()) == null) { // create
            eai.setCreationId(currentUser);

            InterchangeAgreement newIca = toDomain(interchangeAgreement);

            for (PartyRole partyRole : newIca.getPartyRoles()) {
                partyRole.setAccessInfo(eai);
            }

            newIca.setAccessInfo(eai);

            if (newIca.getCiaLevel() != null) {
                newIca.getCiaLevel().setAccessInfo(eai);
            }

//			InterchangeAgreement ica = interchangeAgreementService.createInterchangeAgreement(newIca, firstDelegation, secondDelegation);
            InterchangeAgreement ica = icaNotificationService.createAndSendNotification(newIca, firstDelegation, secondDelegation);
            icaId = ica.getId();
        } else { //update
            eai = interchangeAgreementService.getInterchangeArgreement(interchangeAgreement.getId()).getAccessInfo();
            eai.setModificationId(currentUser);

            InterchangeAgreement modifiedIca = toDomain(interchangeAgreement);
            modifiedIca.setAccessInfo(eai);

            /* CIA records are not updatable via the console, if one exists for
             *  the given values, then it will be used, otherwise a new one will be created
             */
            EntityAccessInfo ciaEai = new EntityAccessInfo();
            ciaEai.setCreationId(eai.getModificationId());
            if (modifiedIca.getCiaLevel() != null) {
                modifiedIca.getCiaLevel().setAccessInfo(ciaEai);
            }

//			InterchangeAgreement savedIca = interchangeAgreementService.updateInterchangeAgreement(modifiedIca, firstDelegation, secondDelegation);
            InterchangeAgreement savedIca = icaNotificationService.updateAndSendNotification(modifiedIca, firstDelegation, secondDelegation);
            icaId = savedIca.getId();
        }

        sessionStatus.setComplete();
        return new AjaxResult(true, messageSource.getMessage("ica.success.save", new Object[]{icaId}, LocaleContextHolder.getLocale()), icaId);
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "fromView", required = false) boolean fromView, @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm interchangeAgreementSearchForm, Model model) {
        if (fromView) { // Run search if coming back from view.
            interchangeAgreementSearchForm.setSearchOnLoad(true);
        } else {
            interchangeAgreementSearchForm = new InterchangeAgreementSearchForm();
            interchangeAgreementSearchForm.setSearchOnLoad(false);
        }

        model.addAttribute("interchangeAgreementSearchForm", interchangeAgreementSearchForm);
        refererPage.setAction("search");

        return PAGE_ICA_SEARCH;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@RequestParam(value = "isSearchDialog", required = false) Boolean isSearchDialog,
                             @ActiveUser SessionUserInformation userInfo,
                             @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm interchangeAgreementSearchForm, Model model, SessionStatus sessionStatus) {
        if (isSearchDialog != null && isSearchDialog) {

            // ETRUSTEX-1498 Routing Endpoints: search for ICA should filter the results by the selected business domain
            Long businessDomainId = interchangeAgreementSearchForm.getBusinessDomainId();
            interchangeAgreementSearchForm = new InterchangeAgreementSearchForm();
            interchangeAgreementSearchForm.setBusinessDomainId(businessDomainId);
        }

        populateSearchForm(model, interchangeAgreementSearchForm, null, userInfo, isSearchDialog);
        sessionStatus.setComplete();

        if (isSearchDialog != null && isSearchDialog) {
            log.debug("IcaController - search popup loaded, returning {}", FRAG_ICA_SEARCH_POPUP);
            return FRAG_ICA_SEARCH_POPUP;
        }

        return FRAG_ICA_SEARCH;
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = InterchangeAgreement.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@RequestParam(value = "isSearchDialog", required = false) Boolean isSearchDialog,
                                @ModelAttribute("interchangeAgreementSearchForm") @Valid InterchangeAgreementSearchForm searchForm, BindingResult result,
                                @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) {

        searchForm.setSearchOnLoad(false);

        sessionStatus.setComplete();

        if (result.hasErrors()) {
            populateSearchForm(model, searchForm, null, userInfo, isSearchDialog);
            return isSearchDialog != null && isSearchDialog ? FRAG_ICA_SEARCH_POPUP : FRAG_ICA_SEARCH;
        }


        ICASearchDTO searchDTO = getSearchDTO(searchForm, userInfo);

        List<InterchangeAgreement> icas = interchangeAgreementService.findInterchangeAgreementsByCriteria(searchDTO);

        log.debug("Found {} interchange agreements for the given criteria.", icas == null ? null : icas.size());

        if (CollectionUtils.isNotEmpty(icas)) {
            Collections.sort(icas, new Comparator<InterchangeAgreement>() {
                @Override
                public int compare(InterchangeAgreement o1, InterchangeAgreement o2) {
                    return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
                }
            });
        }

        populateSearchForm(model, searchForm, null, userInfo, isSearchDialog);
        model.addAttribute("icas", icas);

        return isSearchDialog != null && isSearchDialog ? FRAG_ICA_SEARCH_POPUP : FRAG_ICA_SEARCH;
    }


    @UserActionsLog(entity = InterchangeAgreement.class)
    @RequestMapping(value = "/search/result/xls", method = RequestMethod.GET)
    public void searchResultsExport(@ModelAttribute("interchangeAgreementSearchForm") @Valid InterchangeAgreementSearchForm searchForm, BindingResult result,
                                    @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request, HttpServletResponse response) {


        sessionStatus.setComplete();

        ICASearchDTO searchDTO = getSearchDTO(searchForm, userInfo);

        List<InterchangeAgreement> icas = interchangeAgreementService.findInterchangeAgreementsByCriteria(searchDTO);

        if (CollectionUtils.isNotEmpty(icas)) {
            Collections.sort(icas, new Comparator<InterchangeAgreement>() {
                @Override
                public int compare(InterchangeAgreement o1, InterchangeAgreement o2) {
                    return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
                }
            });
        }

        Map<String, Object> viewModel = new HashMap<String, Object>();

        viewModel.put("bd", userInfo.getBusinessDomain().getName());
        viewModel.put("ICAS", icas);

        try {
            HSSFSheet s = xlsReport.buildExcelDocument(viewModel);

            String fileName = "ICAResult.xls";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/vnd.ms-excel");
            write(response, s);
        } catch (Exception e) {
            log.error("Error during report Generation" + e.getMessage());
        }
    }

    @UserActionsLog(entity = InterchangeAgreement.class)
    @RequestMapping(value = "/{icaId}/view", method = RequestMethod.GET)
    public String view(@PathVariable("icaId") Long id,
                       @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm criteria,
                       @ActiveUser SessionUserInformation userInfo, Model model, HttpServletRequest request) {
        populateSearchForm(model, criteria, "view", userInfo, false);
        model.addAttribute("icaId", id);

        return PAGE_ICA;
    }

    @UserActionsLog(entity = InterchangeAgreement.class)
    @RequestMapping(value = "/{icaId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("icaId") Long id,
                           @RequestParam(value = "isViewDialog", required = false) Boolean isViewDialog,
                           @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm criteria,
                           @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(id);

        if (ica == null) {
            throw new RecordNotFoundException(InterchangeAgreement.class.getSimpleName(), id);
        }

        populateForm(model, toForm(ica), "view", userInfo);

        if (isViewDialog != null && isViewDialog) {
            log.debug("IcaController - view popup loaded, returning {}", FRAG_ICA_VIEW_POPUP);
            return FRAG_ICA_VIEW_POPUP;
        }

        populateSearchForm(model, criteria, "view", userInfo, false);

        if (!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return FRAG_ICA_VIEW;
    }


    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @RequestMapping(value = "/{icaId}/edit", method = RequestMethod.GET)
    public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("icaId") Long icaId,
                       @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm criteria, Model model) {
        InterchangeAgreement interchangeAgreement = interchangeAgreementService.getInterchangeArgreement(icaId);
        populateSearchForm(model, criteria, "edit", userInfo, false);
        populateForm(model, toForm(interchangeAgreement), "edit", userInfo);
        model.addAttribute("icaId", icaId);


        if (!icaUtil.hasAccessRights(userInfo, interchangeAgreement, userService.getUser(interchangeAgreement.getAccessInfo().getCreationId()))) {
            populateSearchForm(model, criteria, "view", userInfo, false);
            model.addAttribute("accessRightsError", messageSource.getMessage("common.error.delete.modify.noRights", null, LocaleContextHolder.getLocale()));

            return PAGE_ICA;
        }

        return PAGE_ICA;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @RequestMapping(value = "/{icaId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@PathVariable("icaId") Long icaId, @ActiveUser SessionUserInformation userInfo,
                           @ModelAttribute("interchangeAgreementSearchForm") InterchangeAgreementSearchForm criteria,
                           Model model, SessionStatus sessionStatus) {
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);
        InterchangeAgreementForm interchangeAgreement = toForm(ica);

        // Check if the user's business domain is the same of the ICA.
        Long icaBusinessDomainId = ica.getPartyRoles().iterator().next().getParty().getBusinessDomain().getId();
        populateSearchForm(model, criteria, "edit", userInfo, false);
        populateForm(model, interchangeAgreement, "edit", userInfo);

        if (!userInfo.getBusinessDomain().getId().equals(icaBusinessDomainId)) {
            model.addAttribute("pageMode", "view");
            model.addAttribute("icaId", icaId);
            model.addAttribute("businessDomainErrorMessage", messageSource.getMessage("ica.businessDomain.different.message", null, LocaleContextHolder.getLocale()));

            return FRAG_ICA_VIEW;
        }

        sessionStatus.setComplete();

        return FRAG_ICA_INNER;
    }


    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @UserActionsLog(entity = InterchangeAgreement.class)
    @RequestMapping(value = "/{icaId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("icaId") Long icaId, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) throws Exception {
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);

        if (ica == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), icaId);
        }

        if (!icaUtil.hasAccessRights(userInfo, ica, userService.getUser(ica.getAccessInfo().getCreationId()))) {
            return new AjaxResult(false, messageSource.getMessage("common.error.delete.modify.noRights", null, LocaleContextHolder.getLocale()), icaId);
        }

        try {
//			interchangeAgreementService.deleteInterchangeAgreementById(Long.valueOf(icaId));
            icaNotificationService.deleteAndSendNotification(icaId);
        } catch (RecordInUseException riue) {
            return new AjaxResult(false, messageSource.getMessage("common.error.delete.recordInUse", null, LocaleContextHolder.getLocale()), icaId);
        }

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), icaId);
    }


    @RequestMapping(value = "/party", method = RequestMethod.POST)
    @ResponseBody
    public String party(@ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement, Model model) {
        refererPage.setController(this.getClass().getSimpleName());
        return "success";
    }

    @RequestMapping(value = "/add/party/{id}", method = RequestMethod.GET)
    public String addParty(@PathVariable("id") Long partyId, @RequestParam String partyType, @ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement,
                           @ActiveUser SessionUserInformation userInfo, Model model, HttpServletRequest request) {
        Party party = partyService.getParty(partyId);
        PartyForm partyForm = new PartyForm(party);

        switch (partyType) {
            case "first":
                interchangeAgreement.setFirstParty(partyForm);
                break;
            case "second":
                interchangeAgreement.setSecondParty(partyForm);
                break;
            case "first3rd":
                interchangeAgreement.setFirst3rdParty(partyForm);
                break;
            case "second3rd":
                interchangeAgreement.setSecond3rdParty(partyForm);
                break;
            default:
                // Should not happen
        }

        model.addAttribute("interchangeAgreement", interchangeAgreement);
        model.addAttribute("pageMode", interchangeAgreement.getId() == null ? "new" : "edit");
        request.setAttribute("partyType", partyType);

        return FRAG_ICA_PARTY;
    }

    @RequestMapping(value = "/partyDelegates/{partyId}", method = RequestMethod.GET)
    public String partyDelegates(@PathVariable Long partyId, @RequestParam String partyType, @ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement,
                                 Model model, HttpServletRequest request) {
        if (partyType.equals("first")) {
            interchangeAgreement.setFirstPartyDelegates(icaUtil.getPartyDelegates(partyId));
        } else {
            interchangeAgreement.setSecondPartyDelegates(icaUtil.getPartyDelegates(partyId));
        }

        request.setAttribute("partyType", partyType);
        model.addAttribute("interchangeAgreement", interchangeAgreement);

        return FRAG_ICA_PARTY_DELEGATES;
    }


    private void write(HttpServletResponse response, HSSFSheet worksheet) {
        log.debug("Writing report to the stream");
        try {
            // Retrieve the output stream
            ServletOutputStream outputStream = response.getOutputStream();
            // Write to the output stream
            worksheet.getWorkbook().write(outputStream);
            // Flush the stream
            outputStream.flush();
        } catch (Exception e) {
            log.error("Unable to write report to the output stream");
        }
    }

    private InterchangeAgreement toDomain(InterchangeAgreementForm form) {
        InterchangeAgreement ica;
        boolean isUpdate = false;
        if (FormUtil.convertDefaultOptionToNull(form.getId()) == null) {
            ica = new InterchangeAgreement();
        } else {
            isUpdate = true;
            ica = interchangeAgreementService.getInterchangeArgreement(form.getId());
        }

        log.debug("toDomain - form.getId() = " + form.getId());

        ica.setProfile(profileService.getProfile(form.getProfile() == null ? null : form.getProfile().getId()));
        log.debug(ica.getProfile().toString());

        ica.setValidityStartDate(form.getIcaValidityStartDate());
        ica.setPartyRoles(new HashSet<PartyRole>(2));
        PartyRole partyRole = new PartyRole();

        partyRole.setParty(partyService.getParty(form.getFirstParty()
                .getId()));
        log.debug(partyRole.getParty().toString());

        partyRole
                .setRole(roleService.getRole(form.getFirstParty().getRoleId()));
        log.debug(partyRole.getRole().toString());

        ica.getPartyRoles().add(partyRole);

        partyRole = new PartyRole();

        partyRole.setParty(partyService.getParty(form.getSecondParty()
                .getId()));
        log.debug(partyRole.getParty().getId() + " - "
                + partyRole.getParty().toString());

        partyRole.setRole(roleService
                .getRole(form.getSecondParty().getRoleId()));
        log.debug(partyRole.getRole().getId() + " - "
                + partyRole.getRole().toString());

        ica.getPartyRoles().add(partyRole);

        if (isUpdate && ica.getCiaLevel() != null && !ica.getCiaLevel().equals(form.getCiaLevels())) {
            form.getCiaLevels().setId(null); // to create a new CIA record and link it to the modified ICA,
            // instead of updating the existing CIA level
            // that might be used by other ICAs
        }

        ica.setCiaLevel(form.getCiaLevels());

        log.debug(ica.toString());

        return ica;
    }

    private InterchangeAgreementForm toForm(InterchangeAgreement ica) {
        log.debug("toIcaForm({})", ica);

        InterchangeAgreementForm form = new InterchangeAgreementForm();

        if (ica == null) {
            return form;
        }

        ProfileElement p = new ProfileElement(ica.getProfile().getId(), ica.getProfile().getName(), null);

        form.setProfile(p);
        form.setIcaValidityStartDate(ica.getValidityStartDate());

        Iterator<PartyRole> it = ica.getPartyRoles().iterator();
        PartyRole partyRole1 = it.next();
        PartyRole partyRole2 = it.next();

        PartyForm partyForm = new PartyForm();
        partyForm.setId(partyRole1.getParty().getId());
        partyForm.setPartyName(partyRole1.getParty().getName());
        partyForm.setRoleId(partyRole1.getRole().getId());
        partyForm.setRoleName(partyRole1.getRole().getName());
        partyForm.setIdentifiers(new ArrayList<>(partyRole1.getParty().getIdentifiers()));
//		partyForm.setVia3rdPartyId(); //TODO: batrian - check out what to do in case of parties using multiple third parties, the party agreements are not linked to an ICA in any way
//		partyForm.setVia3rdPartyName(thirdPartyName);
        form.setFirstParty(partyForm);

        partyForm = new PartyForm();
        partyForm.setId(partyRole2.getParty().getId());
        partyForm.setPartyName(partyRole2.getParty().getName());
        partyForm.setRoleId(partyRole2.getRole().getId());
        partyForm.setRoleName(partyRole2.getRole().getName());
        partyForm.setIdentifiers(new ArrayList<>(partyRole2.getParty().getIdentifiers()));
        // partyForm.setVia3rdPartyId(); //TODO: batrian - check out what to do in case of parties using multiple third parties, the party agreements are not linked to an ICA in any way
//		partyForm.setVia3rdPartyName(thirdPartyName);
        form.setSecondParty(partyForm);

        form.setCiaLevels(ica.getCiaLevel());

        form.setId(ica.getId());

        form.setFirstPartyDelegates(icaUtil.getPartyDelegates(form.getFirstParty().getId()));
        form.setSecondPartyDelegates(icaUtil.getPartyDelegates(form.getSecondParty().getId()));

        return form;
    }


    private void populateForm(Model model, InterchangeAgreementForm form, String pageMode, SessionUserInformation userInfo) {
        if (pageMode != null) {
            model.addAttribute("pageMode", pageMode);
        }

        if (form.getProfile() != null && FormUtil.convertDefaultOptionToNull(form.getProfile().getId()) != null) {
            model.addAttribute("roleList", icaUtil.getRolesForProfile(form.getProfile().getId(), userInfo.getRole().getCode()));
        }

        Set<Profile> profiles = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()).getProfiles();

        model.addAttribute("profileList", businessDomainUtil.getProfilesElementsForUser(profiles, userInfo));
        model.addAttribute("confidentialityLevels", confidentialityLevels());
        model.addAttribute("integrityLevels", integrityLevels());
        model.addAttribute("availabilityLevels", availabilityLevels());
        model.addAttribute("interchangeAgreement", form);
        model.addAttribute("icaId", form.getId());
        model.addAttribute("isMultiple", false);
    }

    private void populateSearchForm(Model model, InterchangeAgreementSearchForm form, String pageMode, SessionUserInformation userInfo, Boolean isSearchDialog) {
        // ETRUSTEX-1498 Routing Endpoints: search for ICA should filter the results by the selected business domain
        Long businessDomainId = FormUtil.convertDefaultOptionToNull(form.getBusinessDomainId()) != null ? FormUtil.convertDefaultOptionToNull(form.getBusinessDomainId()) : userInfo.getBusinessDomain().getId();
        Collection<Role> roleList;
        Set<Profile> profiles;

        // FILTER partyroles list ETRUSTEX-2382
        if (isSearchDialog != null && isSearchDialog) {
            roleList = roleService.getRolesForBusinessDomain(businessDomainId);
        } else {
            if (StringUtils.equalsIgnoreCase(userInfo.getRole().getCode(), UserRoleEnum.ADM.name())) {
                roleList = roleService.getAll();
            } else {
                roleList = roleService.getAllNonTechnical();
            }
        }

        profiles = businessDomainService.getBusinessDomain(businessDomainId).getProfiles();

        if (pageMode != null) {
            model.addAttribute("pageMode", pageMode);
        }

        model.addAttribute("profileList", businessDomainUtil.getProfilesElementsForUser(profiles, userInfo));
        model.addAttribute("roleList", roleList);
        model.addAttribute("interchangeAgreementSearchForm", form);
        model.addAttribute("isSearchDialog", isSearchDialog);
    }

    private Map<String, String> confidentialityLevels() {
        Map<String, String> confidentialityLevels = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            confidentialityLevels.put("" + i, messageSource.getMessage("cia.level.confidentiality." + i, null, LocaleContextHolder.getLocale()));
        }
        return confidentialityLevels;
    }

    private Map<String, String> integrityLevels() {
        Map<String, String> integrityLevels = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            integrityLevels.put("" + i, messageSource.getMessage("cia.level.integrity." + i, null, LocaleContextHolder.getLocale()));
        }
        return integrityLevels;
    }

    private Map<String, String> availabilityLevels() {
        Map<String, String> availabilityLevels = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            availabilityLevels.put("" + i, messageSource.getMessage("cia.level.availability." + i, null, LocaleContextHolder.getLocale()));
        }
        return availabilityLevels;
    }

    private ICASearchDTO getSearchDTO(InterchangeAgreementSearchForm searchForm, SessionUserInformation userInfo) {
        String partyName = searchForm.getParty_search() != null ? searchForm.getParty_search().getPartyName() : null;
        String identifierValue = searchForm.getIdentifier_search();
        Long roleId = FormUtil.convertDefaultOptionToNull(searchForm.getPartyRoleId());
        Long profileId = FormUtil.convertDefaultOptionToNull(searchForm.getProfileId());
        Long businessDomainId = FormUtil.convertDefaultOptionToNull(searchForm.getBusinessDomainId()) != null ? searchForm.getBusinessDomainId() : userInfo.getBusinessDomain().getId();

        return new ICASearchDTO(partyName, identifierValue, roleId, profileId, businessDomainId);
    }
}