package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.MetadataForm;
import eu.europa.ec.etrustex.admin.old.web.dto.MetadataSearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.BusinessDomainUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.Referer;
import eu.europa.ec.etrustex.admin.old.web.validators.MetadataSearchValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.MetadataValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.domain.util.MetaDataItem;
import eu.europa.ec.etrustex.services.*;
import eu.europa.ec.etrustex.types.MetaDataItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/metadata")
@SessionAttributes({"metadataForm", "metadataSearchForm"})
@PreAuthorize("hasAnyRole('ADM', 'SUP')")
public class MetadataController {
    private static final String PAGE_METADATA = "page.metadata";
    private static final String PAGE_METADATA_SEARCH = "page.metadataSearch";
    private static final String FRAG_METADATA_INNER = "frag.metadata.inner";
    private static final String FRAG_METADATA_DOCUMENT = "frag.metadata.document";
    private static final String FRAG_METADATA_ICA = "frag.metadata.ica";
    private static final String FRAG_METADATA_TRANSACTION = "frag.metadata.transaction";
    private static final String FRAG_METADATA_SEARCH = "frag.metadataSearch.inner";
    private static final String FRAG_METADATA_VIEW = "frag.metadataView.inner";
    private static final String FRAG_METADATA_PARTY = "frag.metadata.party";

    @Autowired
    private IMetadataService metadataService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private IDocumentService documentService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IPartyService partyService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IProfileService profileService;
    @Autowired
    private MetadataValidator metadataValidator;
    @Autowired
    private MetadataSearchValidator metadataSearchValidator;
    @Autowired
    private ValidationHelper validationHelper;
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

        binder.registerCustomEditor(Set.class, "profiles", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long profileId = Long.valueOf((String) element);
                return profileId.equals(Long.valueOf(-1)) ? null : profileService.getProfile(profileId);
            }
        });
    }

    @InitBinder("metadataForm")
    public void initFormBinder(WebDataBinder binder) {
        binder.setValidator(metadataValidator);
    }

    @InitBinder("metadataSearchForm")
    public void initSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(metadataSearchValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("metadataForm")
    public MetadataForm metadataForm() {
        return new MetadataForm();
    }

    @ModelAttribute("metadataSearchForm")
    public MetadataSearchForm metadataSearchForm() {
        return new MetadataSearchForm();
    }


    @ModelAttribute("metaDataItemTypes")
    public MetaDataItemType[] metaDataItemTypes() {
        MetaDataItemType[] types = MetaDataItemType.values();
        Arrays.sort(types, new Comparator<MetaDataItemType>() {
            @Override
            public int compare(MetaDataItemType o1, MetaDataItemType o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        return types;
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("pageMode", "new");
        refererPage.setAction("create");

        return PAGE_METADATA;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(@RequestParam(value = "documentId", required = false) Long documentId, @ModelAttribute("metadataForm") MetadataForm metadataForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        if (documentId != null && documentId != -1) {
            metadataForm.setDocument(documentService.getDocument(documentId));
        } else {
            populateForm(model, new MetadataForm(), "new", userInfo);
        }

        return FRAG_METADATA_INNER;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasRole('ADM')")
    @UserActionsLog(entity = MetaDataItem.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("metadataForm") @Valid MetadataForm metadataForm, BindingResult result,
                           @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.metadata.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), metadataForm.getId());
        }

        String message;

        MetaDataItem metadataItem = toDomain(metadataForm);
        EntityAccessInfo eai = new EntityAccessInfo();

        if (metadataItem.getId() == null) { //creation
            eai.setCreationId(userInfo.getUsername());
            metadataItem.setAccessInfo(eai);
            metadataService.create(metadataItem);
        } else { //modification
            eai.setModificationId(userInfo.getUsername());
            metadataItem.setAccessInfo(eai);
            metadataService.update(metadataItem);
        }
        message = messageSource.getMessage("metadata.success.save", new Object[]{metadataItem.getId()}, LocaleContextHolder.getLocale());

        return new AjaxResult(true, message, metadataItem.getId());
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "fromView", required = false) boolean fromView, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        if (fromView) { // Run search if coming back from view.
            metadataSearchForm.setSearchOnLoad(true);
        } else {
            metadataSearchForm = new MetadataSearchForm();
            metadataSearchForm.setSearchOnLoad(false);
        }

        populateSearchForm(model, metadataSearchForm, "", userInfo);
        refererPage.setAction("search");

        return PAGE_METADATA_SEARCH;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus) {
        populateSearchForm(model, metadataSearchForm, "", userInfo);
        sessionStatus.setComplete();

        return FRAG_METADATA_SEARCH;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = MetaDataItem.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@ModelAttribute("metadataSearchForm") @Valid MetadataSearchForm metadataSearchForm, BindingResult result,
                                @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        metadataSearchForm.setSearchOnLoad(false);
        sessionStatus.setComplete();

        if (result.hasErrors()) {
            populateSearchForm(model, metadataSearchForm, "", userInfo);

            return FRAG_METADATA_SEARCH;
        }


        List<MetaDataItem> metadataResults = metadataService.getMetaDataItemsByCriteria(toDomain(metadataSearchForm));


        model.addAttribute("metadataResults", metadataResults);
        populateSearchForm(model, metadataSearchForm, "", userInfo);

        return FRAG_METADATA_SEARCH;
    }

    @UserActionsLog(entity = MetaDataItem.class)
    @RequestMapping(value = "/{metadataItemId}/view", method = RequestMethod.GET)
    public String view(@PathVariable("metadataItemId") Long metadataItemId, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, Model model, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        populateSearchForm(model, metadataSearchForm, "view", userInfo);
        model.addAttribute("metadataItemId", metadataItemId);

        return PAGE_METADATA;
    }

    @UserActionsLog(entity = MetaDataItem.class)
    @RequestMapping(value = "/{metadataItemId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("metadataItemId") Long metadataItemId, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm,
                           SessionStatus sessionStatus, Model model, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        MetaDataItem metadataItem = metadataService.getMetadata(metadataItemId);

        if (metadataItem == null) {
            throw new RecordNotFoundException(MetaDataItem.class.getSimpleName(), metadataItemId);
        }

        populateForm(model, toForm(metadataItem), "view", userInfo);

        populateSearchForm(model, metadataSearchForm, "view", userInfo);

        if (!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return FRAG_METADATA_VIEW;
    }


    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/{metadataItemId}/edit", method = RequestMethod.GET)
    public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("metadataItemId") Long metadataItemId,
                       @ModelAttribute("metadataForm") MetadataForm metadataForm, Model model) {
        populateForm(model, metadataForm, "edit", userInfo);
        model.addAttribute("metadataItemId", metadataItemId);

        return PAGE_METADATA;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/{metadataItemId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@ActiveUser SessionUserInformation userInfo, @PathVariable("metadataItemId") Long metadataItemId, @ModelAttribute("metadataForm") MetadataForm metadataForm, Model model) {
        MetaDataItem metadataItem = metadataService.getMetadata(metadataItemId);

        populateForm(model, toForm(metadataItem), "edit", userInfo);
//		populateSearchForm(model, metadataForm, "edit");

        return FRAG_METADATA_INNER;
    }

    @PreAuthorize("hasRole('ADM')")
    @UserActionsLog(entity = MetaDataItem.class)
    @RequestMapping(value = "/{metadataItemId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("metadataItemId") Long metadataItemId, HttpServletRequest request) {
        MetaDataItem metadataItem = metadataService.getMetadata(metadataItemId);

        if (metadataItem == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), metadataItemId);
        }

        metadataService.delete(metadataItem);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), metadataItemId);
    }


    @RequestMapping(value = "/add/document/{id}", method = RequestMethod.GET)
    public String addDocument(@PathVariable("id") Long documentId, @ModelAttribute("metadataForm") MetadataForm metadataForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        Document document = documentService.getDocument(documentId);
        metadataForm.setDocument(document);
        populateForm(model, metadataForm, metadataForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_METADATA_DOCUMENT;
    }

    @RequestMapping(value = "/add/ica/{id}", method = RequestMethod.GET)
    public String addIca(@PathVariable("id") Long icaId, @ModelAttribute("metadataForm") MetadataForm metadataForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);
        metadataForm.setInterchangeAgreement(ica);
        populateForm(model, metadataForm, metadataForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_METADATA_ICA;
    }

    @RequestMapping(value = "/add/party/{id}", method = RequestMethod.GET)
    public String addParty(@PathVariable("id") Long partyId, @ModelAttribute("metadataForm") MetadataForm metadataForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        Party party = partyService.getParty(partyId);
        metadataForm.setParty(party);
        populateForm(model, metadataForm, metadataForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_METADATA_PARTY;
    }

    @RequestMapping(value = "/add/transaction/{id}", method = RequestMethod.GET)
    public String addTransaction(@PathVariable("id") Long transactionId, @ModelAttribute("metadataForm") MetadataForm metadataForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        metadataForm.setTansaction(transaction);
        populateForm(model, metadataForm, metadataForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_METADATA_TRANSACTION;
    }

    @RequestMapping(value = "/search/add/document/{id}", method = RequestMethod.GET)
    public String searchAddDocument(@PathVariable("id") Long documentId, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        Document document = documentService.getDocument(documentId);
        metadataSearchForm.setDocument_search(document);
        populateSearchForm(model, metadataSearchForm, "", userInfo);

        return FRAG_METADATA_DOCUMENT;
    }

    @RequestMapping(value = "/search/add/ica/{id}", method = RequestMethod.GET)
    public String searchAddIca(@PathVariable("id") Long icaId, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);
        metadataSearchForm.setInterchangeAgreement_search(ica);
        populateSearchForm(model, metadataSearchForm, "", userInfo);

        return FRAG_METADATA_ICA;
    }

    @RequestMapping(value = "/search/add/party/{id}", method = RequestMethod.GET)
    public String searchAddParty(@PathVariable("id") Long partyId, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        Party party = partyService.getParty(partyId);
        metadataSearchForm.setParty_search(party);
        populateSearchForm(model, metadataSearchForm, "", userInfo);

        return FRAG_METADATA_PARTY;
    }

    @RequestMapping(value = "/search/add/transaction/{id}", method = RequestMethod.GET)
    public String searchAddTransaction(@PathVariable("id") Long transactionId, @ModelAttribute("metadataSearchForm") MetadataSearchForm metadataSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        metadataSearchForm.setTansaction_search(transaction);
        populateSearchForm(model, metadataSearchForm, "", userInfo);

        return FRAG_METADATA_TRANSACTION;
    }


    private void populateForm(Model model, MetadataForm metadataForm, String pageMode, SessionUserInformation userInfo) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("metadataItemId", metadataForm.getId());
        model.addAttribute("metadataForm", metadataForm);
        model.addAttribute("profiles", businessDomainUtil.getAllProfilesForUser(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()).getProfiles(),
                userInfo.getRole().getCode(), messageSource.getMessage("common.none", null, LocaleContextHolder.getLocale())));
    }

    private void populateSearchForm(Model model, MetadataSearchForm metadataSearchForm, String pageMode, SessionUserInformation userInfo) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("metadataSearchForm", metadataSearchForm);
        model.addAttribute("profiles", businessDomainUtil.getAllProfilesForUser(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()).getProfiles(),
                userInfo.getRole().getCode(), messageSource.getMessage("choose.please", null, LocaleContextHolder.getLocale())));
    }

    private MetadataForm toForm(MetaDataItem metadataItem) {
        MetadataForm metadataForm = new MetadataForm();
        metadataForm.setId(metadataItem.getId());
        metadataForm.setDocument(metadataItem.getDocument());
        metadataForm.setInterchangeAgreement(metadataItem.getInterchangeAgreement());
        metadataForm.setMetaDataItemType(metadataItem.getItemType());
        metadataForm.setProfile(metadataItem.getProfile());
        metadataForm.setTansaction(metadataItem.getTansaction());
        metadataForm.setValue(metadataItem.getValue());
        metadataForm.setParty(metadataItem.getSender());

        return metadataForm;
    }

    private MetaDataItem toDomain(MetadataForm metadataForm) {
        MetaDataItem metadataItem = new MetaDataItem();
        metadataItem.setId(metadataForm.getId());
        metadataItem.setRawItemType(metadataForm.getMetaDataItemType().name());
        metadataItem.setValue(metadataForm.getValue().trim());
        metadataItem.setDocument(metadataForm.getDocument());
        metadataItem.setInterchangeAgreement(metadataForm.getInterchangeAgreement());
        Profile profile = metadataForm.getProfile() != null && metadataForm.getProfile().getId() != null && !metadataForm.getProfile().getId().equals(Long.valueOf(-1))
                ? metadataForm.getProfile() : null;
        metadataItem.setProfile(profile);
        metadataItem.setTansaction(metadataForm.getTansaction());
        metadataItem.setSender(metadataForm.getParty());

        return metadataItem;
    }

    private MetaDataItem toDomain(MetadataSearchForm metadataSearchForm) {
        MetaDataItem metaDataItem = new MetaDataItem();
        metaDataItem.setRawItemType(metadataSearchForm.getMetaDataItemType_search() != null ? metadataSearchForm.getMetaDataItemType_search().name() : null);
        metaDataItem.setDocument(metadataSearchForm.getDocument_search());
        metaDataItem.setInterchangeAgreement(metadataSearchForm.getInterchangeAgreement_search());
        Profile profile = metadataSearchForm.getProfile_search() != null && metadataSearchForm.getProfile_search().getId() != null && !metadataSearchForm.getProfile_search().getId().equals(Long.valueOf(-1))
                ? metadataSearchForm.getProfile_search() : null;
        metaDataItem.setProfile(profile);
        metaDataItem.setTansaction(metadataSearchForm.getTansaction_search());
        metaDataItem.setSender(metadataSearchForm.getParty_search());

        return metaDataItem;
    }
}