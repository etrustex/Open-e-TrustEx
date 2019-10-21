package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.DocumentForm;
import eu.europa.ec.etrustex.admin.old.web.dto.DocumentSearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.Referer;
import eu.europa.ec.etrustex.admin.old.web.validators.DocumentSearchValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.DocumentValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Document;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IDocumentService;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping(value = "/document")
@SessionAttributes({"documentSearchForm", "metadataForm", "transactionForm"})
@PreAuthorize("hasAnyRole('ADM', 'SUP')")
public class DocumentController {
    private static final String PAGE_DOCUMENT = "page.document";
    private static final String FRAG_DOCUMENT_INNER = "page.document.inner";
    private static final String PAGE_DOCUMENT_SEARCH = "page.documentSearch";
    private static final String FRAG_DOCUMENT_SEARCH_POPUP = "page.documentSearchPopup.inner";
    private static final String FRAG_DOCUMENT_SEARCH = "page.documentSearch.inner";
    private static final String FRAG_DOCUMENT_VIEW = "page.documentView.inner";
    private static final String FRAG_DOCUMENT_VIEW_POPUP = "page.documentViewPopup.inner";
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IDocumentService documentService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private DocumentValidator documentValidator;
    @Autowired
    private DocumentSearchValidator documentSearchValidator;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private Referer refererPage;


    /*-----------------------------------------------------------------*/
    /*---------------------------- Bindings ---------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder("documentForm")
    public void initUserFormBinder(WebDataBinder binder) {
        binder.setValidator(documentValidator);
    }

    @InitBinder("documentSearchForm")
    public void initUserSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(documentSearchValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("documentForm")
    public DocumentForm docvumentForm(@RequestParam(required = false) Long id) {
        return new DocumentForm();
    }

    @ModelAttribute("documentSearchForm")
    public DocumentSearchForm documentSearchForm() {
        return new DocumentSearchForm();
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("pageMode", "new");
        refererPage.setAction("create");

        return PAGE_DOCUMENT;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(Model model) {
        model.addAttribute("pageMode", "new");

        return FRAG_DOCUMENT_INNER;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasRole('ADM')")
    @UserActionsLog(entity = Document.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("documentForm") @Valid DocumentForm form, BindingResult result,
                           @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.document.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), form.getId());
        }

        Document document = toDomain(form);
        Long documentId = null;
        EntityAccessInfo eai = new EntityAccessInfo();
        if (document.getId() == null) {// creation of a new document
            eai.setCreationId(userInfo.getUsername());
            document.setAccessInfo(eai);
            documentId = documentService.createDocument(document).getId();
        } else {// update of an existing document
            eai.setModificationId(userInfo.getUsername());
            document.setAccessInfo(eai);
            documentId = documentService.updateDocument(document).getId();
        }


        return new AjaxResult(true, messageSource.getMessage("document.success.save", new Object[]{documentId}, LocaleContextHolder.getLocale()), documentId);
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "fromView", required = false) boolean fromView, @ModelAttribute("documentSearchForm") DocumentSearchForm documentSearchForm, Model model) {
        if (fromView) { // Run search if coming back from view.
            documentSearchForm.setSearchOnLoad(true);
        } else {
            documentSearchForm = new DocumentSearchForm();
            documentSearchForm.setSearchOnLoad(false);
        }

        populateSearchForm(model, documentSearchForm, null, false);
        refererPage.setAction("search");

        return PAGE_DOCUMENT_SEARCH;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@RequestParam(value = "isSearchDialog", required = false) Boolean isSearchDialog, @ModelAttribute("documentSearchForm") DocumentSearchForm documentSearchForm, Model model, SessionStatus sessionStatus) {
        if (isSearchDialog != null && isSearchDialog) {
            documentSearchForm = new DocumentSearchForm();
        }

        populateSearchForm(model, documentSearchForm, null, isSearchDialog);
        sessionStatus.setComplete();

        if (isSearchDialog != null && isSearchDialog) {
            log.debug("DocumentController - search popup loaded, returning {}", FRAG_DOCUMENT_SEARCH_POPUP);
            return FRAG_DOCUMENT_SEARCH_POPUP;
        }

        return FRAG_DOCUMENT_SEARCH;
    }

    @UserActionsLog(entity = Document.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@RequestParam(value = "isSearchDialog", required = false) Boolean isSearchDialog, @ModelAttribute("documentSearchForm") @Valid DocumentSearchForm documentSearchForm, BindingResult result,
                                Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        log.debug("Search document with criteria: name_search[" + documentSearchForm.getName_search()
                + "], localName_search[" + documentSearchForm.getLocalName_search()
                + "], typeCode_search[" + documentSearchForm.getTypeCode_search() + "]");

        documentSearchForm.setSearchOnLoad(false);

        sessionStatus.setComplete();

        if (result.hasErrors()) {
            populateSearchForm(model, documentSearchForm, null, isSearchDialog);
            return isSearchDialog != null && isSearchDialog ? FRAG_DOCUMENT_SEARCH_POPUP : FRAG_DOCUMENT_SEARCH;
        }

        List<Document> docs = documentService.getDocuments(documentSearchForm.getName_search(), documentSearchForm.getLocalName_search(), documentSearchForm.getTypeCode_search());

//		log.info("Found {} documents for criteria: name_search[{}], localName_search[{}], typeCode_search[{}].", docs.size(), documentSearchForm.getName_search(), documentSearchForm.getLocalName_search(), documentSearchForm.getTypeCode_search());

        model.addAttribute("documents", docs);
        populateSearchForm(model, documentSearchForm, null, isSearchDialog);

        return isSearchDialog != null && isSearchDialog ? FRAG_DOCUMENT_SEARCH_POPUP : FRAG_DOCUMENT_SEARCH;
    }

    @UserActionsLog(entity = Document.class)
    @RequestMapping(value = "/{documentId}/view", method = RequestMethod.GET)
    public String view(@PathVariable("documentId") Long id,
                       @ModelAttribute("documentSearchForm") DocumentSearchForm documentSearchForm,
                       Model model, HttpServletRequest request) {
        populateSearchForm(model, documentSearchForm, "view", false);
        model.addAttribute("documentId", id);

        return PAGE_DOCUMENT;
    }

    @UserActionsLog(entity = Document.class)
    @RequestMapping(value = "/{documentId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("documentId") Long id, @RequestParam(value = "isViewDialog", required = false) Boolean isViewDialog, @ModelAttribute("documentSearchForm") DocumentSearchForm documentSearchForm,
                           Model model, SessionStatus sessionStatus, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Document doc = documentService.getDocument(id);

        if (doc == null) {
            throw new RecordNotFoundException(Document.class.getSimpleName(), id);
        }

        populateForm(model, toForm(doc), "view");

        if (isViewDialog != null && isViewDialog) {
            log.debug("DocumentController - view popup loaded, returning {}", FRAG_DOCUMENT_VIEW_POPUP);
            return FRAG_DOCUMENT_VIEW_POPUP;
        }

        populateSearchForm(model, documentSearchForm, "view", false);

        if (!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return FRAG_DOCUMENT_VIEW;
    }


    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/{documentId}/edit", method = RequestMethod.GET)
    public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("documentId") String documentId,
                       @ModelAttribute("documentSearchForm") DocumentSearchForm documentSearchForm, Model model) {
        populateSearchForm(model, documentSearchForm, "edit", false);
        model.addAttribute("documentId", documentId);

        return PAGE_DOCUMENT;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/{documentId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@PathVariable("documentId") Long documentId, @ModelAttribute("documentSearchForm") DocumentSearchForm documentSearchForm, Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Document doc = documentService.getDocument(documentId);

        populateForm(model, toForm(doc), "edit");
        populateSearchForm(model, documentSearchForm, "edit", false);

        return FRAG_DOCUMENT_INNER;
    }


    @PreAuthorize("hasRole('ADM')")
    @UserActionsLog(entity = Document.class)
    @RequestMapping(value = "/{documentId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("documentId") Long documentId, HttpServletRequest request) {
        Document doc = documentService.getDocument(documentId);

        if (doc == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), documentId);
        }

        if (documentService.isInUse(doc)) {
            return new AjaxResult(false, messageSource.getMessage("error.document.inUse", null, LocaleContextHolder.getLocale()), documentId);
        }

        documentService.deleteDocument(documentId);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), documentId);
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Private methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    private DocumentForm toForm(Document doc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        DocumentForm form = new DocumentForm();
        PropertyUtils.copyProperties(form, doc);
        return form;
    }

    private Document toDomain(DocumentForm form) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Document doc = new Document();

        doc.setId(form.getId());
        doc.setName(form.getName().trim());
        doc.setLocalName(form.getLocalName().trim());
        doc.setNamespace(form.getNamespace().trim());
        doc.setDocumentTypeCode(form.getDocumentTypeCode().trim());
        doc.setVersion(form.getVersion().trim());

        return doc;
    }

    private void populateForm(Model model, DocumentForm doc, String pageMode) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("documentForm", doc);
        model.addAttribute("documentId", doc.getId());
    }

    private void populateSearchForm(Model model, DocumentSearchForm documentSearchForm, String pageMode, Boolean isSearchDialog) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("isSearchDialog", isSearchDialog);
        model.addAttribute("documentSearchForm", documentSearchForm);
    }
}
