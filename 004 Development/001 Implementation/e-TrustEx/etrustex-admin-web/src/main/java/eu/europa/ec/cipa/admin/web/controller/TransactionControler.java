package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.dto.TransactionForm;
import eu.europa.ec.cipa.admin.web.dto.TransactionSearchForm;
import eu.europa.ec.cipa.admin.web.logs.UserActionsLog;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.utils.*;
import eu.europa.ec.cipa.admin.web.validators.TransactionSearchValidator;
import eu.europa.ec.cipa.admin.web.validators.TransactionValidator;
import eu.europa.ec.cipa.admin.web.validators.ValidationHelper;
import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Role;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Controller
@RequestMapping("/transaction")
@SessionAttributes({"transactionSearchForm", "metadataForm"})
@PreAuthorize("hasAnyRole('ADM', 'CBO', 'SUP')")
public class TransactionControler {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String PAGE_TRANSACTION = "page.transaction";
	private static final String PAGE_TRANSACTION_SEARCH = "page.transactionSearch";
	private static final String FRAG_TRANSACTION_INNER = "frag.transaction.inner";
	private static final String FRAG_TRANSACTION_SEARCH_POPUP = "frag.transactionSearchPopup.inner";
	private static final String FRAG_TRANSACTION_SEARCH = "frag.transactionSearch.inner";
	private static final String FRAG_TRANSACTION_VIEW = "frag.transactionView.inner";
	private static final String FRAG_TRANSACTION_DOCUMENT = "frag.transaction.document";
	private static final String FRAG_TRANSACTION_VIEW_POPUP = "page.transactionViewPopup.inner";
	
	@Autowired private ITransactionService transactionService;
	@Autowired private MessageSource messageSource;
	@Autowired private TransactionValidator transactionValidator;
	@Autowired private TransactionSearchValidator transactionSearchValidator;
	@Autowired private ValidationHelper validationHelper;
	@Autowired private IBusinessDomainService businessDomainService;
	@Autowired private IRoleService roleService;
	@Autowired private IDocumentService documentService;
	@Autowired private IProfileService profileService;
	@Autowired private IMessageService messageService;
    @Autowired private BusinessDomainUtil businessDomainUtil;
	@Autowired private Referer refererPage;
	
	private Map<String,String> confidentialityLevels;
	private Map<String,String> integrityLevels;
	private Map<String,String> availabilityLevels;
	
	@PostConstruct
	public void setCiaLevels() {
			confidentialityLevels = new LinkedHashMap<>();
			for(int i=0 ; i<3 ; i++) {
				confidentialityLevels.put(""+i, messageSource.getMessage("cia.level.confidentiality."+i, null, LocaleContextHolder.getLocale()));
			}
			
			integrityLevels = new LinkedHashMap<>();
			for(int i=0 ; i<3 ; i++) {
				integrityLevels.put(""+i, messageSource.getMessage("cia.level.integrity."+i, null, LocaleContextHolder.getLocale()));
			}
		
			availabilityLevels = new LinkedHashMap<>();
			for(int i=0 ; i<3 ; i++) {
				availabilityLevels.put(""+i, messageSource.getMessage("cia.level.availability."+i, null, LocaleContextHolder.getLocale()));
			}
	}
	
	/*-----------------------------------------------------------------*/
	/*---------------------------- Bindings ---------------------------*/
	/*-----------------------------------------------------------------*/
	@InitBinder("transactionForm")
	public void initFormBinder(WebDataBinder binder) {
		binder.setValidator(transactionValidator);
		binder.registerCustomEditor(Set.class, "profiles",  new CustomCollectionEditor(Set.class) {
			@Override
			protected Object convertElement(Object element) {
				Long profileId = new Long((String) element);
		        return profileId.equals(new Long(-1)) ? getTransientProfile(profileId) : profileService.getProfile(profileId);
			}
		});
	}

	@InitBinder("transactionSearchForm")
	public void initSearchFormBinder(WebDataBinder binder) {
		binder.setValidator(transactionSearchValidator);
		
		binder.registerCustomEditor(Set.class, "profiles_search",  new CustomCollectionEditor(Set.class) {
		    @Override
		    protected Object convertElement(Object element) {
		        Long profileId = element instanceof Long ? (Long) element : new Long((String) element);
		        
		        return profileId.equals(new Long(-1)) || profileId.equals(new Long(-2)) ? getTransientProfile(profileId) : profileService.getProfile(profileId);
		    }
		});
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		
		binder.registerCustomEditor(Integer.class, "cia.confidentiality",  new PropertyEditorSupport() {
		    @Override
		    public String getAsText() {
		    	String obj = (String) getValue();
		        return null == obj ? "" : obj + " - " + confidentialityLevels.get(obj);
		    }
		});
		
		binder.registerCustomEditor(Integer.class, "cia.integrity",  new PropertyEditorSupport() {
		    @Override
		    public String getAsText() {
		    	String obj = (String) getValue();
		        return null == obj ? "" : obj + " - " + integrityLevels.get(obj);
		    }
		});
		
		binder.registerCustomEditor(Integer.class, "cia.availability",  new PropertyEditorSupport() {
		    @Override
		    public String getAsText() {
		    	String obj = (String) getValue();
		        return null == obj ? "" : obj + " - " + availabilityLevels.get(obj);
		    }
		});
	}
	
	/*-----------------------------------------------------------------*/
	/*------------------------ Model Attributes -----------------------*/
	/*-----------------------------------------------------------------*/
	@ModelAttribute("transactionForm")
	public TransactionForm transactionForm(@RequestParam(required = false) Long id) {
		return new TransactionForm();
	}
	
	@ModelAttribute("transactionSearchForm")
    public TransactionSearchForm transactionSearchForm() {
		TransactionSearchForm transactionSearchForm = new TransactionSearchForm();
		transactionSearchForm.setProfiles_search(new HashSet<Profile>());
		transactionSearchForm.getProfiles_search().add(getTransientProfile(new Long(-2)));
		return transactionSearchForm;
    }

	
	/*-----------------------------------------------------------------*/
	/*-------------------- Handler methods ----------------------------*/
	/*-----------------------------------------------------------------*/
	@PreAuthorize("hasRole('ADM')")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("pageMode", "new");
		refererPage.setAction("create");
		
		return PAGE_TRANSACTION;
	}

	@PreAuthorize("hasRole('ADM')")
	@RequestMapping(value = "/create/load", method = RequestMethod.POST)
	public String createLoad(@ActiveUser SessionUserInformation userInfo, Model model) {
		populateForm(model, new TransactionForm(), "new", userInfo);

		return FRAG_TRANSACTION_INNER;
	}
	
	
	/*
	 * Important! If arguments are modified, their order must correspond to the following enumeration:
	 * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.save(..)) && args(form, .., request)")
	 */
	@PreAuthorize("hasRole('ADM')")
	@UserActionsLog(entity = Transaction.class)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(@ModelAttribute("transactionForm") @Valid TransactionForm form, BindingResult result, 
			@ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {

    	if (result.hasErrors()) {
			return new AjaxResult(false, messageSource.getMessage("error.transaction.save", new Object[] { validationHelper.getSaveErrorsMsg(result) }, LocaleContextHolder.getLocale()), form.getId());
		}

    	Transaction transaction = new Transaction();
    	transaction.setId(form.getId());
    	transaction.setName(form.getName().trim());
    	transaction.setVersion(form.getVersion().trim());
    	transaction.setNamespace(form.getNamespace().trim());
    	transaction.setRequestLocalName(form.getRequestLocalName().trim());
    	transaction.setResponseLocalName(form.getResponseLocalName().trim());
    	transaction.setCiaLevel(form.getCiaLevel());
    	
    	if(form.getProfiles() != null && !form.getProfiles().isEmpty()) {
    		for(Iterator<Profile> it = form.getProfiles().iterator(); it.hasNext();) {
    			Profile p = it.next();
    			if(p == null || p.getId().equals(new Long(-1))) { // Profile "none"
    				it.remove();
    			}
    		}
    		
    		transaction.setProfiles(form.getProfiles());
    	}
    	
    	if(form.getDocument() != null && form.getDocument().getId() != null) {
//    		transaction.setDocument(documentService.getDocument(form.getDocument().getId()));
    		transaction.setDocument(form.getDocument());
    	}
    	
    	if(form.getSenderRole() != null && form.getSenderRole().getId() != null) {
    		transaction.setSenderRole(form.getSenderRole());
    	}
    	
    	if(form.getReceiverRole() != null && form.getReceiverRole().getId() != null) {
    		transaction.setReceiverRole(form.getReceiverRole());
    	}
    	
		Long transactionId = null;
		EntityAccessInfo eai = new EntityAccessInfo();
		if (transaction.getId() == null) {// creation of a new transaction
			eai.setCreationId(userInfo.getUsername());
			transaction.setAccessInfo(eai);
			transactionId = transactionService.create(transaction).getId();
		} else {// update of an existing transaction
			eai.setModificationId(userInfo.getUsername());
			transaction.setAccessInfo(eai);
			transactionId = transactionService.update(transaction).getId();
		}
		
			
		return new AjaxResult(true, messageSource.getMessage("transaction.success.save", new Object[] { transactionId }, LocaleContextHolder.getLocale()), transactionId);
	}


	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(@ActiveUser SessionUserInformation userInfo, @RequestParam(value="fromView", required=false) boolean fromView, 
			@ModelAttribute("transactionSearchForm") TransactionSearchForm transactionSearchForm, Model model) {
		if (fromView) { // Run search if coming back from view.
			transactionSearchForm.setSearchOnLoad(true);
		} else {
			transactionSearchForm = new TransactionSearchForm();
			transactionSearchForm.setSearchOnLoad(false);
			transactionSearchForm.setProfiles_search(new HashSet<Profile>());
			transactionSearchForm.getProfiles_search().add(getTransientProfile(new Long(-1)));
		}
		
//		populateSearchForm(model, transactionSearchForm, null, false, userInfo);
		refererPage.setAction("search");
		
		return PAGE_TRANSACTION_SEARCH;
	}

	@RequestMapping(value = "/search/load", method = RequestMethod.POST)
	public String searchLoad(@ActiveUser SessionUserInformation userInfo, @RequestParam(value="isSearchDialog", required = false) Boolean isSearchDialog, 
			@ModelAttribute("transactionSearchForm") TransactionSearchForm transactionSearchForm, Model model, SessionStatus sessionStatus) {
		if(isSearchDialog != null && isSearchDialog) {
            // ETRUSTEX-1498 Routing Endpoints: search for ICA should filter the results by the selected business domain
            Long businessDomainId = transactionSearchForm.getBusinessDomainId();
            transactionSearchForm = new TransactionSearchForm();
            transactionSearchForm.setBusinessDomainId(businessDomainId);
            transactionSearchForm.setProfiles_search(new HashSet<Profile>());
			transactionSearchForm.getProfiles_search().add(getTransientProfile(new Long(-2)));
    	}
		
		populateSearchForm(model, transactionSearchForm, null, isSearchDialog, userInfo);
		sessionStatus.setComplete();
		
		if (isSearchDialog != null && isSearchDialog){
			log.debug("TransactionController - search popup loaded, returning {}", FRAG_TRANSACTION_SEARCH_POPUP);
			return FRAG_TRANSACTION_SEARCH_POPUP;
		}

		return FRAG_TRANSACTION_SEARCH;
	}

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
	@UserActionsLog(entity = Transaction.class)
	@RequestMapping(value = "/search/results", method = RequestMethod.POST)
	public String searchResults(@ActiveUser SessionUserInformation userInfo, @RequestParam(value="isSearchDialog", required = false) Boolean isSearchDialog, 
			@ModelAttribute("transactionSearchForm") @Valid TransactionSearchForm transactionSearchForm, BindingResult result, 
			Model model, SessionStatus sessionStatus, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		transactionSearchForm.setSearchOnLoad(false);
		
    	sessionStatus.setComplete();
    	
		if (result.hasErrors()){
			populateSearchForm(model, transactionSearchForm, null, isSearchDialog, userInfo);
			return isSearchDialog != null && isSearchDialog ? FRAG_TRANSACTION_SEARCH_POPUP : FRAG_TRANSACTION_SEARCH;
		}
		
        Transaction tx = toDomain(transactionSearchForm);
		
		List<Transaction> transactions = transactionService.getTransactionsByCriteria(tx);

		/*
         * ETRUSTEX-1407 CIPAdmin shall allow CBO users to configure routing endpoints
         * I.e. only transactions associated to profiles of the current user's business domain if a CBO user selects profile "Any"
         */
        if ( userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name()) && transactionSearchForm.getProfiles_search().iterator().next().getId().equals(Long.valueOf(-2)) ) {
            Collection<Profile> currentBDprofiles = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()).getProfiles();
            for (Iterator<Transaction> it = transactions.iterator(); it.hasNext();) {
                Transaction t  = it.next();
                if (!CollectionUtils.containsAny(t.getProfiles(), currentBDprofiles)) {
                    it.remove();
                }
            }
        }
		
		model.addAttribute("transactions", transactions);
		populateSearchForm(model, transactionSearchForm, null, isSearchDialog, userInfo);

		return isSearchDialog != null && isSearchDialog ? FRAG_TRANSACTION_SEARCH_POPUP : FRAG_TRANSACTION_SEARCH;
	}

	@UserActionsLog(entity = Transaction.class)
	@RequestMapping(value = "/{transactionId}/view", method = RequestMethod.GET)
	public String view(@ActiveUser SessionUserInformation userInfo, @PathVariable("transactionId") Long id,
			@ModelAttribute("transactionSearchForm") TransactionSearchForm transactionSearchForm, 
			Model model, HttpServletRequest request) {
		populateSearchForm(model, transactionSearchForm, "view", false, userInfo);
		model.addAttribute("transactionId", id);
		
		return PAGE_TRANSACTION;
	}

	@UserActionsLog(entity = Transaction.class)
	@RequestMapping(value = "/{transactionId}/view/load", method = RequestMethod.POST)
	public String viewLoad(@ActiveUser SessionUserInformation userInfo, @PathVariable("transactionId") Long id,
						   @RequestParam(value="isViewDialog", required = false) Boolean isViewDialog,
						   @ModelAttribute("transactionSearchForm") TransactionSearchForm transactionSearchForm,
						   Model model, SessionStatus sessionStatus, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Transaction tx = transactionService.getTransaction(id);

		if(tx != null) {
			populateForm(model, toForm(tx), "view", userInfo);
		} else {
			populateForm(model, new TransactionForm(), "view", userInfo);
		}

		if (isViewDialog != null && isViewDialog) {
			log.debug("TransactionController - view popup loaded, returning {}", FRAG_TRANSACTION_VIEW_POPUP);
			return FRAG_TRANSACTION_VIEW_POPUP;
		}

		model.addAttribute("transactionSearchForm", transactionSearchForm);
		
		if(!refererPage.getAction().equals("search")) {
			sessionStatus.setComplete();
		}
		
		return FRAG_TRANSACTION_VIEW;
	}

	@PreAuthorize("hasRole('ADM')")
	@RequestMapping(value = "/{transactionId}/edit", method = RequestMethod.GET)
	public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("transactionId") Long transactionId,
			@ModelAttribute("transactionSearchForm") TransactionSearchForm transactionSearchForm, Model model) {
		String pageMode;
		/*
		 * UC70_BR24	Transaction modification is not authorized if there are messages in the System referring to the transaction. This check shall be done twice:
		 •	Second, when trying to save a modified transaction (in order to ensure that no message using this transaction has been received in the meanwhile)
		 •	First, when trying to acces the modification page
		 */
		if(!messageService.findMessagesByTransaction(transactionId).isEmpty()) {
			pageMode = "view";
			model.addAttribute("updateNotAuthorizedMsg", messageSource.getMessage("transaction.edit.messages.notAuthorized", null, LocaleContextHolder.getLocale()));
		} else {
			pageMode = "edit";
		}
		
		populateSearchForm(model, transactionSearchForm, pageMode, false, userInfo);
		model.addAttribute("transactionId", transactionId);
		
		return PAGE_TRANSACTION;
	}

	@PreAuthorize("hasRole('ADM')")
	@RequestMapping(value = "/{transactionId}/edit/load", method = RequestMethod.POST)
	public String editLoad(@ActiveUser SessionUserInformation userInfo, @PathVariable("transactionId") Long transactionId, 
			@ModelAttribute("transactionSearchForm") TransactionSearchForm transactionSearchForm, Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Transaction tx = transactionService.getTransaction(transactionId);

		populateForm(model, toForm(tx), "edit", userInfo);
		model.addAttribute("isSearchDialog", false);
		model.addAttribute("transactionSearchForm", transactionSearchForm);

		return FRAG_TRANSACTION_INNER;
	}


	@PreAuthorize("hasRole('ADM')")
	@UserActionsLog(entity = Transaction.class)
	@RequestMapping(value = "/{transactionId}/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(@PathVariable("transactionId") Long transactionId, HttpServletRequest request) {
		Transaction tx = transactionService.getTransaction(transactionId);

		if (tx == null) {
			return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), transactionId);
		}

		if (transactionService.isInUse(tx)) {
			return new AjaxResult(false, messageSource.getMessage("error.transaction.inUse", null, LocaleContextHolder.getLocale()), transactionId);
		}

		transactionService.delete(transactionId);

		return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), transactionId);
	}
	
    @PreAuthorize("hasRole('ADM')")
	@RequestMapping(value = "/add/document/{id}", method = RequestMethod.GET)
	public String addDocument(@ActiveUser SessionUserInformation userInfo, @PathVariable("id") Long documentId, @ModelAttribute("transactionForm") TransactionForm transactionForm, Model model){
		Document document = documentService.getDocument(documentId);
		transactionForm.setDocument(document);
		populateForm(model, transactionForm, transactionForm.getId() == null ? "new" : "edit", userInfo);
		
		return FRAG_TRANSACTION_DOCUMENT;
	}
	
	
	
	/*-----------------------------------------------------------------*/
	/*-------------------- Private methods ----------------------------*/
	/*-----------------------------------------------------------------*/
	private TransactionForm toForm(Transaction tx) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TransactionForm form = new TransactionForm();
		PropertyUtils.copyProperties(form, tx);
		return form;
	}
	
	private Transaction toDomain(TransactionSearchForm form) {
		Transaction tx = new Transaction();
		
		tx.setName(form.getName_search());
		
		if(StringUtils.isNotEmpty(form.getDocument_search())) {
			Document d = new Document();
			d.setName(form.getDocument_search());
			tx.setDocument(d);
		}
		
		if(form.getProfiles_search() != null) {
			boolean profileAnyOrNone = false;
			/*
			 * If selected profile is "Any", profiles = null
			 * If selected profile is "None", profiles = empty
			 * See TransactionDAO#getTransactionsByCriteria(transaction) 
			 */
			if(form.getProfiles_search().isEmpty()) {
				tx.setProfiles(null);
			} else {
				for(Profile p : form.getProfiles_search()) {
					if(p.getId().equals(new Long(-2))) { // Any
						tx.setProfiles(null);
						profileAnyOrNone = true;
						break;
					}
					
					if(p.getId().equals(new Long(-1))) { // None
						tx.setProfiles(new HashSet<Profile>());
						profileAnyOrNone = true;
						break;
					}
				}
				
				// If selected profiles aren't "Any" or "None"
				if(!profileAnyOrNone) {
					tx.setProfiles(form.getProfiles_search());
				}
			}
		} else {
			tx.setProfiles(null);
		}
		
		
		if(form.getReceiverRole_search() != null && form.getReceiverRole_search().getId() != null) {
			tx.setReceiverRole(form.getReceiverRole_search());
		}
		
		if(form.getSenderRole_search() != null && form.getSenderRole_search().getId() != null) {
			tx.setSenderRole(form.getSenderRole_search());
		}
		
		return tx;
	}
	
	private void populateForm(Model model, TransactionForm form, String pageMode, SessionUserInformation userInfo) {
		model.addAttribute("pageMode", pageMode);
		model.addAttribute("transactionForm", form);
		model.addAttribute("transactionId", form.getId());
		model.addAttribute("profiles", businessDomainUtil.getAllProfilesForUser(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()).getProfiles(),
				userInfo.getRole().getCode(), messageSource.getMessage("common.none", null, LocaleContextHolder.getLocale())));
		model.addAttribute("confidentialityLevels", confidentialityLevels);
		model.addAttribute("integrityLevels", integrityLevels);
		model.addAttribute("availabilityLevels", availabilityLevels);
		model.addAttribute("roleList", roleService.getAll());
	}
	
	private void populateSearchForm(Model model, TransactionSearchForm form, String pageMode, Boolean isSearchDialog, SessionUserInformation userInfo) {
        // ETRUSTEX-1498 Routing Endpoints: search for ICA should filter the results by the selected business domain
        Long businessDomainId;
        Set<Role> roles;

        if(FormUtil.convertDefaultOptionToNull(form.getBusinessDomainId()) != null) {
            businessDomainId = form.getBusinessDomainId();
            roles = roleService.getRolesForBusinessDomain(businessDomainId);
        } else {
            businessDomainId = userInfo.getBusinessDomain().getId();
            roles =  new HashSet<>(roleService.getAll());
        }

        model.addAttribute("pageMode", pageMode);
        model.addAttribute("isSearchDialog", isSearchDialog);
        model.addAttribute("transactionSearchForm", form);
        model.addAttribute("roleList", roles);

		List<Profile> profiles = businessDomainUtil.getAllProfilesForUser(businessDomainService.getBusinessDomain(businessDomainId).getProfiles(),
				userInfo.getRole().getCode(), messageSource.getMessage("common.none", null, LocaleContextHolder.getLocale()));
		
		Profile none = new Profile();
		none.setId(new Long(-2));
		none.setName(messageSource.getMessage("choose.please", null, LocaleContextHolder.getLocale()));
		profiles.add(0, none);
		
		model.addAttribute("profiles", profiles);
	}
	
	private Profile getTransientProfile(Long profileId) {
		Profile p = new Profile();
		p.setId(profileId);
		
		if(profileId.equals(new Long(-2))) {
        	p.setName(messageSource.getMessage("choose.please", null, LocaleContextHolder.getLocale()));
        } else {
        	p.setName(messageSource.getMessage("common.none", null, LocaleContextHolder.getLocale()));
        }
		
		return p;
	}
}