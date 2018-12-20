package eu.europa.ec.etrustex.admin.old.web.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import eu.europa.ec.etrustex.admin.old.web.dto.BusinessDomainElement;
import eu.europa.ec.etrustex.admin.old.web.dto.CertificateWrapper;
import eu.europa.ec.etrustex.admin.old.web.dto.CertificatesForm;
import eu.europa.ec.etrustex.admin.old.web.dto.CredentialsDTO;
import eu.europa.ec.etrustex.admin.old.web.dto.PartyIdentifierDTO;
import eu.europa.ec.etrustex.admin.old.web.dto.PartySearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.PartyView;
import eu.europa.ec.etrustex.admin.old.web.dto.SearchPartyElement;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.services.IPartyNotificationService;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.PartyUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.Referer;
import eu.europa.ec.etrustex.admin.old.web.utils.UserRoleEnum;
import eu.europa.ec.etrustex.admin.old.web.validators.CertificateValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.PartySearchValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.PartyValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.dao.exception.RecordInUseException;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Certificate;
import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Message;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyCredentials;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.ICertificateService;
import eu.europa.ec.etrustex.services.ICredentialsService;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.IMessageRoutingService;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.IPartyAgreementService;
import eu.europa.ec.etrustex.services.IPartyIdentifierService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.services.admin.IUserService;
import eu.europa.ec.etrustex.types.IdentifierIssuingAgency;

@Controller
@RequestMapping("/party")
@SessionAttributes({"identifierList", "partySearchForm", "interchangeAgreement", "partyForm", "certificatesForm"})
@PreAuthorize("isFullyAuthenticated()")
public class PartyController {
    private static final String PAGE_PARTY = "page.party";
    private static final String FRAG_PARTY = "page.party.inner";
    private static final String FRAG_PARTY_VIEW = "page.partyView.inner";
    private static final String FRAG_CREDENTIALS = "page.party.credentials";
    private static final String FRAG_IDENTIFIERS = "page.party.identifiers";
    private static final String PAGE_PARTY_SEARCH = "page.partySearch";
    private static final String FRAG_PARTY_SEARCH = "page.partySearch.inner";
    private static final String FRAG_PARTY_SEARCH_POPUP = "page.partySearchPopup.inner";
    private static final String FRAG_CERTIFICATE_VIEW = "page.certificates.view";
    private static final String FRAG_PARTY_VIEW_POPUP = "page.partyViewPopup.inner";
    IdentifierIssuingAgency[] identifiers;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IPartyService partyService;
    @Autowired
    private IPartyNotificationService partyNotificationService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPartyIdentifierService partyIdentifierService;
    @Autowired
    private ICertificateService certificateService;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private ICredentialsService credentialsService;
    @Autowired
    private IPartyAgreementService partyAgreementService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IMessageRoutingService messageRoutingService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CertificateValidator certificateValidator;
    @Autowired
    private PartyValidator partyValidator;
    @Autowired
    private PartySearchValidator partySearchValidator;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private Referer refererPage;

    @PostConstruct
    public void initFoo() {
        identifiers = IdentifierIssuingAgency.values();
        Arrays.sort(identifiers, new Comparator<IdentifierIssuingAgency>() {
            @Override
            public int compare(IdentifierIssuingAgency o1, IdentifierIssuingAgency o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    /*-----------------------------------------------------------------*/
    /*---------------------------- Binding ----------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

        binder.registerCustomEditor(IdentifierIssuingAgency.class, new PropertyEditorSupport() {
            Object value;

            @Override
            public String getAsText() {
                IdentifierIssuingAgency type = (IdentifierIssuingAgency) getValue();
                return type.getSchemeID();
            }

            @Override
            public void setAsText(String text) {
                value = StringUtils.isEmpty(text) || text.equals("-1") ? null : IdentifierIssuingAgency.valueOf(text);
                setValue(value);
            }
        });
    }

    @InitBinder("partyForm")
    public void initPartyFormBinder(WebDataBinder binder) {
        binder.setValidator(partyValidator);
    }

    @InitBinder("partySearchForm")
    public void initUserFormBinder(WebDataBinder binder) {
        binder.setValidator(partySearchValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("partyForm")
    public PartyView partyForm(@ActiveUser SessionUserInformation userInfo) {
        PartyView pv = new PartyView();

        // UC10_BR03	"Is 3rd Party" is set to "No" by default.
        pv.setIsThirdParty(false);

        // UC10_BR58	"Is natural person" is set to "No" by default.
        pv.setNaturalPersonFlag(false);

        CredentialsDTO partyCredentials = new CredentialsDTO();
        partyCredentials.setIsPasswordEncrypted(true);
        pv.setCredentials(partyCredentials);

        return pv;
    }

    @ModelAttribute("partySearchForm")
    public PartySearchForm partySearchForm() {
        return new PartySearchForm();
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, HttpSession session) {
        model.addAttribute("pageMode", "new");
        refererPage.setAction("create");

        return PAGE_PARTY;
    }

    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(@ActiveUser SessionUserInformation userInfo, Model model, HttpSession session) {
        populateForm(model, partyForm(userInfo), "new");

        return FRAG_PARTY;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @UserActionsLog(entity = Party.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("partyForm") @Valid PartyView partyView, BindingResult result,
                           @ModelAttribute("partySearchForm") PartySearchForm partySearchForm,
                           @ActiveUser SessionUserInformation userInfo, HttpSession session, SessionStatus sessionStatus, HttpServletRequest request) throws Exception {
        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.party.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), partyView.getId());
        }

        Long partyId = null;

        /*
         * If not null or emtpy, add certificates to partyView.
         * Removal of deleted certificates is managed in partyNotificationService.updateParty()
         */
        CertificatesForm certificatesForm = (CertificatesForm) session.getAttribute("certificatesForm");

        if (certificatesForm != null && CollectionUtils.isNotEmpty(certificatesForm.getCertificates())) {
            partyView.setCertificates(new ArrayList<Certificate>());

            for (CertificateWrapper certificateWrapper : certificatesForm.getCertificates()) {
                if (certificateWrapper.getAddToParty()) {
                    partyView.getCertificates().add(certificateWrapper.getCertificate());
                }
            }
        }

        session.removeAttribute("certificatesForm");

        if (partyView.getId() == null) {//creation of a new party
            log.debug("PartyController - save - creating new party...");
            partyId = createParty(partyView, userInfo);
        } else {//update of an existing party
            log.debug("PartyController - save - updating party with id {}...", partyView.getId());
            partyId = modifyParty(partyView, userInfo);
        }

        // Set sessionStatus complete only if it is not coming from ICA creation. Otherwise ICA form fields filled before creating new party will be reset...
        if (refererPage.getController() == null) {
            sessionStatus.setComplete();
        }

        String successMsg = messageSource.getMessage("party.success.save", new Object[]{partyId}, LocaleContextHolder.getLocale());
        return new AjaxResult(true, successMsg, partyId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "fromView", required = false) boolean fromView, @ModelAttribute("partySearchForm") PartySearchForm partySearchForm, Model model) {
        // Run search if coming back from view.
        if (fromView) {
            partySearchForm.setSearchOnLoad(true);
        } else {
            partySearchForm = new PartySearchForm();
            partySearchForm.setSearchOnLoad(false);
        }

        populateSearchForm(model, partySearchForm, "", false);
        refererPage.setAction("search");

        return PAGE_PARTY_SEARCH;
    }

    /**
     * @param isSearchDialog
     * @param partyType       can be any of:
     *                        [first, first3rd, second, second3rd, secondMulti] if coming from ICA screens,
     *                        [issuer, sender, receiver] if coming from Inspect Messages screen,
     *                        null otherwise
     * @param userInfo
     * @param partySearchForm
     * @param model
     * @param sessionStatus
     * @return
     */
    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@RequestParam(value = "isSearchDialog", required = false) Boolean isSearchDialog,
                             @RequestParam(value = "partyType", required = false) String partyType,
                             @ActiveUser SessionUserInformation userInfo, @ModelAttribute("partySearchForm") PartySearchForm partySearchForm, Model model, SessionStatus sessionStatus) {
        /* business domain should be prefilled in search party popup when coming from the user screen with the business domain selected */
        if (isSearchDialog != null && isSearchDialog) {
            // ETRUSTEX-1498 Routing Endpoints: search for ICA should filter the results by the selected business domain
            Long businessDomainId = partySearchForm.getBusinessDomainId();
            partySearchForm = new PartySearchForm();
            partySearchForm.setBusinessDomainId(businessDomainId);

            /*
             * ETRUSTEX-1407 CIPAdmin shall allow CBO users to configure routing endpoints
             */
            if ((userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name()) && businessDomainId != null)
                    || (Objects.equals(partyType, "authorizing") || Objects.equals(partyType, "delegate") || Objects.equals(partyType, "sender")) // "sender" is from metadata pages
                    ) {
                // CBO searching party to create Routing Endpoint Configuration
                model.addAttribute("disabledBD", true);
            }

            /*
             * Subflow 2.8.1 : User searches for a party
             */
            // (b)	the business domain criteria will be defaulted to the one form the application header and read only
            if (StringUtils.isNotBlank(partyType)) {
                switch (partyType) {
                    case "first":
                    case "second":
                    case "authorizing":
                        // (c)	if triggered from the button associated to a first or second party, the is third party criteria will be defaulted to No and read only
                        partySearchForm.setThirdPartyFlag_search(false);
                        break;
                    case "first3rd":
                    case "second3rd":
                    case "delegate":
                        // (d)	if triggered from the button associated to a third party field, the is third party criteria will be defaulted to Yes and read only
                        partySearchForm.setThirdPartyFlag_search(true);
                        break;
                    default:
                        // do nothing
                }


                partySearchForm.setBusinessDomainId(userInfo.getBusinessDomain().getId());
            }

            populateSearchForm(model, partySearchForm, "", isSearchDialog);

            return FRAG_PARTY_SEARCH_POPUP;
        } else {
            populateSearchForm(model, partySearchForm, "", isSearchDialog);

            sessionStatus.setComplete();

            return FRAG_PARTY_SEARCH;
        }
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = Party.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@ModelAttribute("partySearchForm") @Valid PartySearchForm partySearchForm, BindingResult result,
                                @RequestParam(value = "isSearchDialog", required = false) Boolean isSearchDialog,
                                @RequestParam(value = "partyType", required = false) String partyType,
                                @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        log.debug("Search party with criteria: partyName[" + partySearchForm.getName_search()
                + "], partyBusinessDomain[" + partySearchForm.getBusinessDomainId()
                + "], partyUsername[" + partySearchForm.getUsername_search()
                + "], partyIdentifier[" + partySearchForm.getIdentifier_search() + "]");

        partySearchForm.setSearchOnLoad(false);

        sessionStatus.setComplete();

        if (result.hasErrors()) {
            populateSearchForm(model, partySearchForm, "", isSearchDialog);

            return isSearchDialog != null && isSearchDialog ? FRAG_PARTY_SEARCH_POPUP : FRAG_PARTY_SEARCH;
        }

        List<Long> businessDomainIds = new ArrayList<Long>();
        /*
         * ETRUSTEX-1407 CIPAdmin shall allow CBO users to configure routing endpoints
         */
        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name()) && isSearchDialog != null && isSearchDialog) {
            businessDomainIds.add(userInfo.getBusinessDomain().getId());
        } else if (partySearchForm.getBusinessDomainId() == null || partySearchForm.getBusinessDomainId() == -1) {
            List<BusinessDomainElement> bds = userInfo.getBusinessDomains();
            for (BusinessDomainElement bd : bds) {
                businessDomainIds.add(bd.getId());
            }
        } else {
            businessDomainIds.add(partySearchForm.getBusinessDomainId());
        }

        List<Party> parties = partyService.findPartiesByCriteria(partySearchForm.getName_search(),
                partySearchForm.getUsername_search(), partySearchForm.getIdentifier_search(), businessDomainIds, partySearchForm.getThirdPartyFlag_search());

        log.debug("Found {} parties for the given criteria.", parties == null ? null : parties.size());

        List<SearchPartyElement> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(parties)) {
            for (Party party : parties) {
                /*
                 * (e) if triggered by an LBO user from the button associated to a second party, an extra filter shall apply so that only the parties
                 * the user is authorized for (as configured in "Configure User" use case) will be searched for, applying the remaining criteria.
                 */
                if (party != null && !(userInfo.getRole().getCode().equals(UserRoleEnum.LBO.name()) && StringUtils.isNotBlank(partyType) && partyType.equals("second"))) {
                    results.add(new SearchPartyElement(party));
                }
            }

            Collections.sort(results, new Comparator<SearchPartyElement>() {
                @Override
                public int compare(SearchPartyElement o1, SearchPartyElement o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
        }

        model.addAttribute("parties", results);
        populateSearchForm(model, partySearchForm, "", isSearchDialog);


        return isSearchDialog != null && isSearchDialog ? FRAG_PARTY_SEARCH_POPUP : FRAG_PARTY_SEARCH;
    }


    @UserActionsLog(entity = Party.class)
    @RequestMapping(value = "/{partyId}/view", method = RequestMethod.GET)
    public String view(@PathVariable("partyId") Long id, @ModelAttribute("partySearchForm") PartySearchForm partySearchForm, Model model, HttpServletRequest request) {
        populateSearchForm(model, partySearchForm, "view", false);
        model.addAttribute("partyId", id);

        return PAGE_PARTY;
    }

    @UserActionsLog(entity = Party.class)
    @RequestMapping(value = "/{partyId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("partyId") Long id, @ModelAttribute("partySearchForm") PartySearchForm partySearchForm,
                           @RequestParam(value = "isViewDialog", required = false) Boolean isViewDialog,
                           SessionStatus sessionStatus, Model model, HttpServletRequest request) {
        Party party = partyService.getParty(id);

        if (party == null) {
            throw new RecordNotFoundException(Party.class.getSimpleName(), id);
        }

        populateForm(model, new PartyView(party), "view");

        if (isViewDialog != null && isViewDialog) {
            return FRAG_PARTY_VIEW_POPUP;
        }

        populateSearchForm(model, partySearchForm, "view", false);

        if (!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return FRAG_PARTY_VIEW;
    }


    @RequestMapping(value = "/{partyId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("partyId") String partyId, @ActiveUser SessionUserInformation userInfo,
                       @ModelAttribute("partySearchForm") PartySearchForm partySearchForm, Model model) {

        model.addAttribute("partyId", partyId);

        Party party = partyService.getParty(Long.parseLong(partyId));

        if (!hasAccessRights(userInfo, party.getAccessInfo(), party.getBusinessDomain().getId())) {
            populateSearchForm(model, partySearchForm, "view", false);
            model.addAttribute("accessRightsError", messageSource.getMessage("common.error.delete.modify.noRights", null, LocaleContextHolder.getLocale()));

            return PAGE_PARTY;
        }

        populateSearchForm(model, partySearchForm, "edit", false);

        return PAGE_PARTY;
    }

    @RequestMapping(value = "/{partyId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@PathVariable("partyId") String partyId, @ModelAttribute("partySearchForm") PartySearchForm partySearchForm, Model model) {
        Party party = partyService.getParty(Long.parseLong(partyId));
        PartyView partyForm = new PartyView(party);

        if (party != null && party.getCredentials() == null) {
            Credentials cred = new PartyCredentials();
            cred.setPasswordEncrypted(true);
            partyForm.setCredentials(new CredentialsDTO(cred));
        }

        partyForm.getCredentials().setPassword(null);
        partyForm.getCredentials().setPassword2(null);

        populateForm(model, partyForm, "edit");
        populateSearchForm(model, partySearchForm, "edit", false);
        model.addAttribute("existsAgreementForParty", partyAgreementService.existsAgreementForParty(party));
        model.addAttribute("isHiddenPassword", partyForm.getCredentials() != null && partyForm.getCredentials().getId() != null);

        return FRAG_PARTY;
    }

    @UserActionsLog(entity = Party.class)
    @RequestMapping(value = "/{partyId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("partyId") Long partyId,
                             @ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, HttpServletRequest request) throws Exception {
        Party party = partyService.getParty(partyId);

        if (party == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), null);
        }

        if (!hasAccessRights(userInfo, party.getAccessInfo(), party.getBusinessDomain().getId())) {
            return new AjaxResult(false, messageSource.getMessage("common.error.delete.modify.noRights", null, LocaleContextHolder.getLocale()), party.getId());
        }


        boolean inUse = false;

        /*
         * UC10_BR60 Parties that are used in an Endpoint Configuration (ETR_TB_ENDPOINT) for which there is a message routing record (ETR_TB_MESSAGE_ROUTING) cannot be deleted.
         */
        if (CollectionUtils.isNotEmpty(messageRoutingService.findByEndpointPartyId(partyId))) {
            inUse = true;
        } else {
            /*
             * UC10_BR36 If there is an Interchange Agreement involving the party, and based on which messages were exchanged, the party cannot be deleted. User is notified if he/she attempts to delete such party.
             */
            for (InterchangeAgreement ica : interchangeAgreementService.findByParty(party)) {
                if (messageService.isInterchangeAgreementUsedToSendMessages(ica)) {
                    inUse = true;
                    break;
                }
            }
        }
        
        Message mCriteria = new Message();
        mCriteria.setSender(party);
        if(messageService.countMessagesByCriteria(mCriteria, null, null, null, null) != 0){
        	inUse = true;
        }
        
        if (inUse) {
            return new AjaxResult(false, messageSource.getMessage("error.party.in.use", null, LocaleContextHolder.getLocale()), party.getId());
        }

        try{
        	partyNotificationService.deleteAndSendNotification(partyId, userInfo.getBusinessDomain().getId());
        }catch(JAXBException | RecordInUseException  e){
        	return new AjaxResult(false, messageSource.getMessage("error.party.in.use", null, LocaleContextHolder.getLocale()), party.getId());
        }
        

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), party.getId());
    }

    /*
     * View party's certificates
     */
    @RequestMapping(value = "/{partyId}/certificates", method = RequestMethod.POST)
    public String certificates(@PathVariable("partyId") Long partyId, Model model) {
        Party party = partyService.getParty(partyId);
        model.addAttribute("certificates", party.getCertificates());
        model.addAttribute("popupMode", "view");

        return FRAG_CERTIFICATE_VIEW;
    }



    /* ----------------------------------------------------------------- */
    /* -------------------- Fragments Refreshing ----------------------- */
    /* ----------------------------------------------------------------- */

    /**
     * Used for refreshing the credentials fragment when user changes the value of thirdPartyFlag.
     *
     * @param partyView
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/credentials", method = RequestMethod.GET)
    public String refreshCredentials(@ModelAttribute("partyForm") PartyView partyView,
                                     BindingResult result, Model model) {

        log.debug("PartyController - refreshing Credentials...");

        if (partyView.getId() == null) {
            model.addAttribute("pageMode", "new");
        } else {
            model.addAttribute("pageMode", "edit");
        }
        model.addAttribute("partyForm", partyView);

        return FRAG_CREDENTIALS;
    }

    /**
     * Used for refreshing the certificate in the main-info fragment when user adds/changes a certificate.
     * @param partyView
     * @param result
     * @param model
     * @return
     */
//	@RequestMapping(value = "/refreshCertificate", method = RequestMethod.GET)
//	public String refreshCertificate(@ModelAttribute("partyForm") PartyView partyView,
//			BindingResult result, Model model) {
//		
//		log.debug("PartyController - refreshing Certificate...");
//		
//		model.addAttribute("partyForm", partyView);
//		
//		return FRAG_MAIN_INFO;
//	}	

    /**
     * Used for refreshing the identifiers fragment when user clicks on "Add an identifier" button.
     *
     * @param partyView
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/addIdentifier", method = RequestMethod.POST)
    public String addIdentifier(@ActiveUser SessionUserInformation userInfo, @ModelAttribute("partyForm") PartyView partyView,
                                BindingResult result, Model model) {
        String validationMsg = validateNewIdentifier(userInfo, partyView);

        if (validationMsg != null) {
            model.addAttribute("validationMsg", validationMsg);
        } else {
            addNewIdentifierToParty(partyView);
            clearNewIdentifierFields(partyView);
        }

        return FRAG_IDENTIFIERS;
    }

    @RequestMapping(value = "/identifier/delete", method = RequestMethod.POST)
    public String deleteIdentifier(@ActiveUser SessionUserInformation userInfo,
                                   @ModelAttribute("partyForm") PartyView partyView,
                                   @RequestParam("identifierId") String id,
                                   @RequestParam("identifierType") String type,
                                   @RequestParam("identifierValue") String value,
                                   BindingResult result,
                                   Model model) {

        List<PartyIdentifierDTO> identifiers = partyView.getIdentifiers();
        if (CollectionUtils.isNotEmpty(identifiers)) {

            int i = 0;
            for (Iterator<PartyIdentifierDTO> it = identifiers.iterator(); it.hasNext(); i++) {
                PartyIdentifierDTO identifier = it.next();

                if (!identifier.getIsMarkedForDeletion()) {
                    continue;
                }

                if (identifier.getId() == null) {
                    it.remove();
                    break;
                } else {
                    if (identifier.getId() != null
                            && StringUtils.isNotBlank(id)
                            && identifier.getId().equals(Long.parseLong(id))) {


                        if (!hasAccessRights(userInfo, partyIdentifierService.getPartyIdentifier(identifier.getId()).getAccessInfo(), partyView.getBusinessDomain().getId())) {
                            model.addAttribute("accessRightsMessage", messageSource.getMessage("common.error.delete.modify.noRights", null, LocaleContextHolder.getLocale()));
                            result.rejectValue("identifiers[" + i + "].type", "NOT AUTHORIZED");
                        }

                        break;
                    }
                }

            }

        }

        populateForm(model, partyView, partyView.getId() == null ? "new" : "edit");

        return FRAG_IDENTIFIERS;
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Private methods ----------------------------*/
    /*-----------------------------------------------------------------*/

    /**
     * Used to validate access rights on deletion and modification.
     *
     * @param userInfo
     * @param accessInfo
     * @return
     */
    private boolean hasAccessRights(SessionUserInformation userInfo, EntityAccessInfo accessInfo, Long businessDomainId) {
        String userRole = userInfo.getRole().getCode();

        /*
         *	UC10_BR23
         *	LBO can modify only parties pertaining to his/her business domain and that have been created
         *	by himself/herself or another LBO acting in behalf of the same party as the user.
         *
         * 	UC10_BR24
         * 	CBO can modify only parties pertaining to his/her business domain.
         *
         * 	UC10_BR35
         * 	CBO can delete only parties pertaining to his/her business domain.
         */

        // Actually no one can modify/delete a Party that does not pertain to his BD
        if (!userInfo.getBusinessDomain().getId().equals(businessDomainId)) {
            return false;
        }

        if (StringUtils.equalsIgnoreCase(userRole, UserRoleEnum.LBO.name())) {
            User creationUser = userService.getUser(accessInfo.getCreationId());
            boolean createdByUser = creationUser == null ? false : StringUtils.equalsIgnoreCase(userInfo.getUsername(), creationUser.getName());
            boolean createdByLBOOnBehalf = PartyUtil.isCreatedByLBOOnBehalf(creationUser, userInfo, businessDomainId);

            if (!(createdByUser || createdByLBOOnBehalf)) {
                return false;
            }
        }

        return true;
    }

    /* ----------------------------------------------------------------- */
    /* -------------------------- Validation ----------------------------- */
    /* ----------------------------------------------------------------- */
    private String validateNewIdentifier(SessionUserInformation userInfo, PartyView partyView) {

        if (StringUtils.isBlank(partyView.getNewIdentifierType())
                || StringUtils.equals(partyView.getNewIdentifierType(), "-1")
                || StringUtils.isBlank(partyView.getNewIdentifierValue())) {
            return messageSource.getMessage("error.party.identifiers.not.completely.filled", null, LocaleContextHolder.getLocale());
        }

        if (StringUtils.isNotBlank(partyView.getNewIdentifierType())
                && !StringUtils.equals(partyView.getNewIdentifierType(), "-1")
                && StringUtils.isNotBlank(partyView.getNewIdentifierValue())) {

            if (partyView.getNewIdentifierValue().trim().length() > ValidationHelper.MAX_LENGHT) {
                return messageSource.getMessage("error.party.identifier.value.tooLong", new Object[]{partyView.getNewIdentifierType()}, LocaleContextHolder.getLocale());
            } else {
                if (partyView.getNewIdentifierValue().trim().length() < ValidationHelper.MIN_LENGHT) {
                    return messageSource.getMessage("error.party.identifier.value.tooShort", new Object[]{partyView.getNewIdentifierType()}, LocaleContextHolder.getLocale());
                }
            }

            List<PartyIdentifierDTO> identifiers = partyView.getIdentifiers();

            if (identifiers != null) {
                boolean isUniquePerParty = true;
                for (PartyIdentifierDTO identifier : identifiers) {
                    if (identifier.getType().compareTo(IdentifierIssuingAgency.valueOf(partyView.getNewIdentifierType())) == 0
                            && !identifier.getIsMarkedForDeletion()) {
                        isUniquePerParty = false;
                    }
                }
                if (!isUniquePerParty) {
                    return messageSource.getMessage("error.party.identifiers.type.unique", null, LocaleContextHolder.getLocale());
                }
            }

            PartyIdentifier pi = new PartyIdentifier();
            pi.setSchemeId(IdentifierIssuingAgency.valueOf(partyView.getNewIdentifierType()));
            pi.setValue(partyView.getNewIdentifierValue().trim());

            if (partyView.getId() == null
                    || (partyView.getId() != null
                    && isNewIdentifierDifferentThanDeletedOnes(new PartyIdentifierDTO(pi), identifiers))) {

                if (partyIdentifierService.existsPartyIdentifierInBusinessDomain(pi, userInfo.getBusinessDomain().getId())) {
                    return messageSource.getMessage("error.party.identifier.not.unique", null, LocaleContextHolder.getLocale());
                }
            }
        }

        return null;
    }

    private boolean isNewIdentifierDifferentThanDeletedOnes(PartyIdentifierDTO newPartyIdentifier,
                                                            List<PartyIdentifierDTO> partyIdentifiersFromView) {

        if (CollectionUtils.isEmpty(partyIdentifiersFromView)) {
            return true;
        }

        for (PartyIdentifierDTO partyIdentifier : partyIdentifiersFromView) {
            if (partyIdentifier.getType().compareTo(newPartyIdentifier.getType()) == 0
                    && partyIdentifier.getIsMarkedForDeletion()) {
                return false;
            }
        }

        return true;

    }


    /**
     * Enforce default values where it is the case
     *
     * @param partyView
     * @param userInfo
     */
    private void enforceDefaultValues(PartyView partyView, SessionUserInformation userInfo) {
        //for LBO 3rd party flag must be NO/false - if not, do not show error but enforce the correct value
        if (StringUtils.equals(userInfo.getRole().getCode(), UserRoleEnum.LBO.name())) {
            if (partyView.getIsThirdParty() == null || partyView.getIsThirdParty()) {
                partyView.setIsThirdParty(false);
            }
        }


        CredentialsDTO credentials = partyView.getCredentials();

//		if no username or password filled in, do not set any credentials
        if (credentials != null && (StringUtils.isNotBlank(credentials.getUser()) ||
                StringUtils.isNotBlank(credentials.getPassword()))) {
            // If isPasswordEncrypted and signatureRequired flags are null,
            // force them to the default values without displaying an error.

            if (credentials.getIsPasswordEncrypted() == null) {
                credentials.setIsPasswordEncrypted(true);
            }

            if (credentials.getIsSignatureRequired() == null) {
                credentials.setIsSignatureRequired(false);
            }
        } else {
            partyView.setCredentials(null);
        }
    }

    private Long createParty(PartyView partyView, SessionUserInformation userInfo) throws Exception {
        String username = userInfo.getUsername();

        enforceDefaultValues(partyView, userInfo);

        if (partyView.getNewIdentifierType() != null
                && !StringUtils.equals(partyView.getNewIdentifierType(), "-1")
                && partyView.getNewIdentifierValue() != null) {
            addNewIdentifierToParty(partyView);
            clearNewIdentifierFields(partyView);
        }

        Party newParty = partyView.toDomain();

        EntityAccessInfo eai = new EntityAccessInfo();
        eai.setCreationId(username);
        newParty.setAccessInfo(eai);

        Party savedParty = partyNotificationService.createAndSendNotification(newParty, userInfo.getBusinessDomain().getId(), partyView.getIdentifiers().get(0));

        return savedParty.getId();
    }


    private Long modifyParty(PartyView partyView, SessionUserInformation userInfo) throws Exception {

        String username = userInfo.getUsername();

        enforceDefaultValues(partyView, userInfo);

        if (partyView.getNewIdentifierType() != null
                && !StringUtils.equals(partyView.getNewIdentifierType(), "-1")
                && partyView.getNewIdentifierValue() != null) {
            addNewIdentifierToParty(partyView);
            clearNewIdentifierFields(partyView);
        }

        Party modifiedParty = partyView.toDomain();
        EntityAccessInfo eai = new EntityAccessInfo();
        eai.setModificationId(username);
        modifiedParty.setAccessInfo(eai);

        if (modifiedParty.getCredentials() != null) {

            if (modifiedParty.getCredentials().getUser() != null) {
                modifiedParty.getCredentials().setUser(modifiedParty.getCredentials().getUser().trim());
            }

            if (modifiedParty.getCredentials().getId() == null) { // new credentials
            } else { // existing credentials
                if (partyView.getChangePasswordFlag()) {
                } else {
                    modifiedParty.getCredentials().setPassword(null);
                }
            }

            if (modifiedParty.getCredentials().getId() != null && !partyView.getChangePasswordFlag()) {
                modifiedParty.getCredentials().setPassword(null);
            }
        }

        Set<PartyIdentifier> identifiers = modifiedParty.getIdentifiers();
        for (PartyIdentifier identifier : identifiers) {
            identifier.setValue(identifier.getValue().trim());
        }

        //build list with party identifiers to delete
        List<PartyIdentifierDTO> partyIdentifiersFromView = partyView.getIdentifiers();
        List<Long> partyIdentifiersToDelete = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(partyIdentifiersFromView)) {
            Iterator<PartyIdentifierDTO> partyIdentifierIterator = partyIdentifiersFromView.iterator();
            while (partyIdentifierIterator.hasNext()) {
                PartyIdentifierDTO partyIdentifierDTO = partyIdentifierIterator.next();
                if (partyIdentifierDTO.getId() != null && partyIdentifierDTO.getIsMarkedForDeletion()) {
                    partyIdentifiersToDelete.add(partyIdentifierDTO.getId());
                    partyIdentifierIterator.remove();
                }
            }
        }

        Party savedParty = partyNotificationService.updateAndSendNotification(modifiedParty, partyIdentifiersToDelete, userInfo.getBusinessDomain().getId(), partyView.getIdentifiers().get(0));

        return savedParty.getId();
    }

    private void addNewIdentifierToParty(PartyView partyView) {
        PartyIdentifierDTO newPartyIdentifier = new PartyIdentifierDTO();
        newPartyIdentifier.setType(IdentifierIssuingAgency.valueOf(partyView.getNewIdentifierType()));
        newPartyIdentifier.setValue(partyView.getNewIdentifierValue().trim());
        newPartyIdentifier.setIsMarkedForDeletion(false);

        List<PartyIdentifierDTO> partyIdentifiers = partyView.getIdentifiers();
        if (partyIdentifiers == null) {
            partyIdentifiers = new ArrayList<PartyIdentifierDTO>();
            partyView.setIdentifiers(partyIdentifiers);
        }

        boolean found = false;

        if (partyView.getId() != null) {
            if (CollectionUtils.isNotEmpty(partyIdentifiers)) {
                for (PartyIdentifierDTO partyIdentifierDTO : partyIdentifiers) {
                    if (partyIdentifierDTO.getId() != null
                            && partyIdentifierDTO.getType().equals(newPartyIdentifier.getType())
                            && partyIdentifierDTO.getValue().trim().equals(newPartyIdentifier.getValue())
                            && partyIdentifierDTO.getIsMarkedForDeletion()) {
                        partyIdentifierDTO.setIsMarkedForDeletion(false);
                        found = true;
                    }
                }
            }
        }

        if (!found) {
            partyView.getIdentifiers().add(newPartyIdentifier);
        }
    }

    public void clearNewIdentifierFields(PartyView partyView) {
        partyView.setNewIdentifierType(null);
        partyView.setNewIdentifierValue(null);
    }

    /*
    private void fillInCertificate(PartyView partyView, HttpSession session, Boolean isCreation){

        String certificateSessionId = partyView.getCertificateInSession();
        if (StringUtils.isNotBlank(certificateSessionId)){
            log.debug("Attaching certificate from session, identified by {}", certificateSessionId);
            partyView.setCertificate((Certificate)session.getAttribute(certificateSessionId));
        } else {
            if (isCreation) {
                partyView.setCertificate(null);
            } else {
                if (partyView.getCertificates() == null || partyView.getCertificates().getId() == null){
                    //certificate comes from view with all fields null in case of not existing certificate
                    partyView.setCertificate(null);
                } else {
                    partyView.setCertificate(certificateService.getCertificate(partyView.getCertificates().getId()));
                }
            }
        }

        log.debug("Party certificate = " + partyView.getCertificates());

    }
*/
    private void populateForm(Model model, PartyView partyForm, String pageMode) {
        model.addAttribute("identifierList", this.identifiers);
        model.addAttribute("partyForm", partyForm);
        model.addAttribute("pageMode", pageMode);
    }

    private void populateSearchForm(Model model, PartySearchForm partySearchForm, String pageMode, Boolean isSearchDialog) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("isSearchDialog", isSearchDialog);
        model.addAttribute("partySearchForm", partySearchForm);
    }

}