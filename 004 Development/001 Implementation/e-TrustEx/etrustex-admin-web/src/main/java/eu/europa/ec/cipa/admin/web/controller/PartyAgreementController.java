package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.PartyAgreementForm;
import eu.europa.ec.cipa.admin.web.dto.PartyAgreementSearchForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.logs.UserActionsLog;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.utils.AjaxResult;
import eu.europa.ec.cipa.admin.web.utils.Referer;
import eu.europa.ec.cipa.admin.web.validators.PartyAgreementSearchValidator;
import eu.europa.ec.cipa.admin.web.validators.PartyAgreementValidator;
import eu.europa.ec.cipa.admin.web.validators.ValidationHelper;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyAgreement;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.cipa.etrustex.services.IPartyAgreementService;
import eu.europa.ec.cipa.etrustex.services.IPartyService;
import eu.europa.ec.cipa.etrustex.services.ITransactionService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import eu.europa.ec.cipa.etrustex.services.exception.RecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/partyAgreement")
@SessionAttributes({"partyAgreementSearchForm", "partyAgreementForm"})
@PreAuthorize("hasAnyRole('ADM', 'CBO')")
public class PartyAgreementController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private IPartyService partyService;
	@Autowired private IUserService userService;
	@Autowired private IPartyAgreementService partyAgreementService;
    @Autowired private ITransactionService transactionService;
    @Autowired private IInterchangeAgreementService interchangeAgreementService;
    @Autowired private ValidationHelper validationHelper;
	@Autowired private MessageSource  messageSource;
	@Autowired private PartyAgreementValidator partyAgreementValidator;
	@Autowired private PartyAgreementSearchValidator partyAgreementSearchValidator;
	@Autowired private Referer refererPage;


	/*-----------------------------------------------------------------*/
	/*---------------------------- Binding ----------------------------*/
	/*-----------------------------------------------------------------*/
    @InitBinder("partyAgreementForm")
    public void initPartyAgreementFormBinder(WebDataBinder binder) {
        binder.setValidator(partyAgreementValidator);
    } 
	
    @InitBinder("partyAgreementSearchForm")
    public void initUserFormBinder(WebDataBinder binder) {  
        binder.setValidator(partyAgreementSearchValidator);
    } 
	

	
	
	/*-----------------------------------------------------------------*/
	/*------------------------ Model Attributes -----------------------*/
	/*-----------------------------------------------------------------*/
    @ModelAttribute("partyAgreementForm")
    public PartyAgreementForm form() {
		return new PartyAgreementForm();
    }
    
    @ModelAttribute("partyAgreementSearchForm")
    public PartyAgreementSearchForm searchForm() {
		return new PartyAgreementSearchForm();
    }

   
    
    /*-----------------------------------------------------------------*/
	/*-------------------- Handler methods ----------------------------*/
	/*-----------------------------------------------------------------*/
	@RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
		model.addAttribute("pageMode", "new");
		refererPage.setAction("create");

		return "page.partyAgreement";
    }

    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(Model model) {
    	populateForm(model, new PartyAgreementForm(), "new");
		
    	return "fragment.partyAgreement.inner";
    }
    
    
    /*
	 * Important! If arguments are modified, their order must correspond to the following enumeration:
	 * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.save(..)) && args(form, .., request)")
	 */
    @UserActionsLog(entity = PartyAgreement.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public AjaxResult save(@ModelAttribute("partyAgreementForm") @Valid PartyAgreementForm form, BindingResult result,
			@ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm sarchForm,
			@ActiveUser SessionUserInformation userInfo, HttpSession session, SessionStatus sessionStatus, HttpServletRequest request) {
		if (result.hasErrors()) {
			return new AjaxResult(false, messageSource.getMessage("error.party.save", new Object[] { validationHelper.getSaveErrorsMsg(result) }, LocaleContextHolder.getLocale()), form.getId());
		}
		
        EntityAccessInfo eai = new EntityAccessInfo();
        String username = userInfo.getUsername();
        PartyAgreement partyAgreement = toDomain(form);

//		try{
			if (form.getId() == null) {//creation of a new party Agreement
                eai.setCreationId(username);
                partyAgreement.setAccessInfo(eai);
                partyAgreement = partyAgreementService.createPartyAgreement(partyAgreement);
			} else {//update of an existing party Agreement
                eai.setModificationId(username);
                partyAgreement.setAccessInfo(eai);
                partyAgreement = partyAgreementService.updatePartyAgreement(partyAgreement);
            }
//		} catch (Exception e) {
//            log.error(e.getMessage(), e);
//			return new AjaxResult(false, messageSource.getMessage("common.system.error", null, LocaleContextHolder.getLocale()), form.getId());
//		}

		// Set sessionStatus complete only if it is not coming from ICA creation. Otherwise ICA form fields filled before creating new party will be reset...
		if(refererPage.getController() == null) {
			sessionStatus.setComplete();
		}
		
		return new AjaxResult(true, messageSource.getMessage("partyAgreement.success.save", new Object[] { partyAgreement.getId() }, LocaleContextHolder.getLocale()), partyAgreement.getId());
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value="fromView", required=false) boolean fromView, @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm, Model model) {
		// Run search if coming back from view.
		if (fromView) {
			searchForm.setSearchOnLoad(true);
		} else {
			searchForm = new PartyAgreementSearchForm();
			searchForm.setSearchOnLoad(false);
		}
		
		populateSearchForm(model, searchForm, "", false);
		refererPage.setAction("search");
		
        return "page.partyAgreementSearch";
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@RequestParam(value="isSearchDialog", required = false) Boolean isSearchDialog,
                             @ActiveUser SessionUserInformation userInfo, @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm, Model model, SessionStatus sessionStatus) {
        if(isSearchDialog != null && isSearchDialog) {
            // Not yet
            return null;
    	} else {
            populateSearchForm(model, searchForm, "", isSearchDialog);
            sessionStatus.setComplete();
            return "fragment.partyAgreementSearch.inner";
        }
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = PartyAgreement.class)
	@RequestMapping(value = "/search/results", method = RequestMethod.POST)
	public String searchResults(@ModelAttribute("partyAgreementSearchForm") @Valid PartyAgreementSearchForm searchForm, BindingResult result,
			                    @RequestParam(value="isSearchDialog", required = false) Boolean isSearchDialog,
                                @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
		searchForm.setSearchOnLoad(false);
		sessionStatus.setComplete();

		if (result.hasErrors()){
			populateSearchForm(model, searchForm, "", isSearchDialog);
            return isSearchDialog != null && isSearchDialog ? null : "fragment.partyAgreementSearch.inner";
		}

		List<PartyAgreement> partyAgreements = partyAgreementService.findPartyAgreementsByCriteria(searchForm.getAuthorizingParty_search(), searchForm.getDelegateParty_search(), searchForm.getTransaction_search(), userInfo.getBusinessDomain().getId());

		model.addAttribute("partyAgreements", partyAgreements);
		populateSearchForm(model, searchForm, "", isSearchDialog);


        return isSearchDialog != null && isSearchDialog ? null : "fragment.partyAgreementSearch.inner";
	}

    
    @UserActionsLog(entity = PartyAgreement.class)
    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String view(@PathVariable Long id, @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm, Model model, HttpServletRequest request) {
    	populateSearchForm(model, searchForm, "view", false);
		model.addAttribute("id", id);
		
		return "page.partyAgreement";
	}
    @UserActionsLog(entity = PartyAgreement.class)
	@RequestMapping(value = "/{id}/view/load", method = RequestMethod.POST)
	public String viewLoad(@PathVariable Long id, @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm,
                           @RequestParam(value="isViewDialog", required = false) Boolean isViewDialog, @ActiveUser SessionUserInformation userInfo,
                           SessionStatus sessionStatus, Model model, HttpServletRequest request) {
		PartyAgreement partyAgreement = partyAgreementService.findPartyAgreementById(id);

		if (partyAgreement == null) {
            throw new RecordNotFoundException(PartyAgreement.class.getSimpleName(), id);
        } else if (!partyAgreement.getAuthorizingParty().getBusinessDomain().getId().equals(userInfo.getBusinessDomain().getId()) ) {
            throw new AccessDeniedException("User not allowed to access this resource");
        }

        model.addAttribute("partyAgreement", partyAgreement);

        if (isViewDialog != null && isViewDialog) {
            // Not yet
            // view = FRAG_PARTY_VIEW_POPUP;
        }

        populateSearchForm(model, searchForm, "view", false);

        if(!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }
		
		return "fragment.partyAgreementView.inner";
	}
	

	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, @ActiveUser SessionUserInformation userInfo,
			@ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm, Model model) {
		model.addAttribute("id", id);
		populateSearchForm(model, searchForm, "edit", false);

		return "page.partyAgreement";
	}
	
	@RequestMapping(value = "/{id}/edit/load", method = RequestMethod.POST)
	public String editLoad(@PathVariable Long id, @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm,
                           @ActiveUser SessionUserInformation userInfo, Model model) throws InvocationTargetException, IllegalAccessException {
        PartyAgreement partyAgreement = partyAgreementService.findPartyAgreementById(id);

        if (partyAgreement == null) {
            throw new RecordNotFoundException(PartyAgreement.class.getSimpleName(), id);
        } else if (!partyAgreement.getAuthorizingParty().getBusinessDomain().getId().equals(userInfo.getBusinessDomain().getId()) ) {
            throw new AccessDeniedException("User not allowed to access this resource");
        }

        model.addAttribute("partyAgreementForm", toForm(partyAgreement));
        populateSearchForm(model, searchForm, "edit", false);
		
		return "fragment.partyAgreement.inner";
	}

	@UserActionsLog(entity = PartyAgreement.class)
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(@PathVariable Long id,
			@ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, HttpServletRequest request) {
        PartyAgreement partyAgreement = partyAgreementService.findPartyAgreementById(id);

        if (partyAgreement == null) {
            throw new RecordNotFoundException(PartyAgreement.class.getSimpleName(), id);
        } else if (!partyAgreement.getAuthorizingParty().getBusinessDomain().getId().equals(userInfo.getBusinessDomain().getId()) ) {
            throw new AccessDeniedException("User not allowed to access this resource");
        }

        Party authorizingParty = partyAgreement.getAuthorizingParty();
        Party delegateParty = partyAgreement.getDelegateParty();

        // UC160_BR12	There shall be no messages in the system having the authorized party as sender and the delegated party as issuer.
        if(partyAgreementValidator.existingMessagesForParties(authorizingParty, delegateParty)){
            return new AjaxResult(false, messageSource.getMessage("error.partyAgreement.inUse", null, LocaleContextHolder.getLocale()), null);
        }

        // UC160_BR13	If this is the only party agreement for the authorizing party and the authorizing party has not credentials, there shall be no interchange agreement configured for the authorizing party (such interchange agreement will become useless as the sender has no means to connect and send messages).
        if(partyAgreementService.findPartyAgreementsForAuthorizingParty(authorizingParty).size() == 1
                && authorizingParty.getCredentials() == null
                && interchangeAgreementService.containsParty(authorizingParty)){
            return new AjaxResult(false, messageSource.getMessage("error.partyAgreement.noCredentials", null, LocaleContextHolder.getLocale()), null);
        }

        partyAgreementService.deletePartyAgreement(partyAgreement);

		return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), id);
	}


    @RequestMapping(value = "/add/party/{partyId}", method = RequestMethod.GET)
    public String addParty(@PathVariable Long partyId, @RequestParam(value = "partyType", required = false) String partyType,
                           @ModelAttribute("partyAgreementForm") PartyAgreementForm form, Model model, HttpServletRequest request){
        Party party = partyService.getParty(partyId);

        switch (partyType) {
            case "authorizing":
                form.setAuthorizingParty(party);
                break;

            case "delegate":
                form.setDelegateParty(party);
                break;

            default: // Should not happen
                break;
        }

        populateForm(model, form, form.getId() == null ? "new" : "edit");
        request.setAttribute("partyType", partyType);

        return "fragment.partyAgreement.party";
    }

    @RequestMapping(value = "/search/add/party/{partyId}", method = RequestMethod.GET)
    public String searchAddParty(@PathVariable Long partyId, @RequestParam(value = "partyType", required = false) String partyType,
                                 @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm, Model model, HttpServletRequest request){
        Party party = partyService.getParty(partyId);

        switch (partyType) {
            case "authorizing":
                searchForm.setAuthorizingParty_search(party);
                break;

            case "delegate":
                searchForm.setDelegateParty_search(party);
                break;

            default: // Should not happen
                break;
        }

        populateSearchForm(model, searchForm, "", false);
        request.setAttribute("partyType", partyType);

        return "fragment.partyAgreement.party";
    }

    @RequestMapping(value = "/add/transaction/{txId}", method = RequestMethod.GET)
    public String addTransaction(@PathVariable("txId") Long transactionId, @ModelAttribute("partyAgreementForm") PartyAgreementForm form, @ActiveUser SessionUserInformation userInfo, Model model){
        Transaction transaction = transactionService.getTransaction(transactionId);

        if(!form.getTransactions().contains(transaction)) {
            form.getTransactions().add(transaction);
        }

        populateForm(model, form, form.getId() == null ? "new" : "edit");

        return "fragment.partyAgreement.transactions";
    }

    @RequestMapping(value = "/search/add/transaction/{txId}", method = RequestMethod.GET)
    public String searchAddTransaction(@PathVariable Long txId, @ModelAttribute("partyAgreementSearchForm") PartyAgreementSearchForm searchForm, Model model){
        Transaction transaction = transactionService.getTransaction(txId);
        searchForm.setTransaction_search(transaction);
        populateSearchForm(model, searchForm, "", false);

        return "fragment.partyAgreement.transaction";
    }

    @RequestMapping(value = "/tx/remove", method = RequestMethod.POST)
    public String removeTx(@ModelAttribute("partyAgreementForm") PartyAgreementForm form, @RequestParam Long txId, Model model){
        boolean valid = true;
        Transaction tx = transactionService.getTransaction(txId);

        // UC160_BR11	Authorized transactions cannot be deleted if there are messages in the system having the authorized party as sender and the delegated party as issuer.
        if (form.getId() != null && partyAgreementValidator.existingMessagesForParties(form.getAuthorizingParty(), form.getDelegateParty())) {
            PartyAgreement existingPA = partyAgreementService.findPartyAgreementById(form.getId());
            if(existingPA.getTransactions().contains(tx)){
                valid = false;
                model.addAttribute("txCannotBeRemoved",  messageSource.getMessage("error.partyAgreement.transactions.inUse", null, LocaleContextHolder.getLocale()));
            }
        }

        if (valid) {
            form.getTransactions().remove(tx);
        }

        populateForm(model, form, form.getId() == null ? "new" : "edit");

        return "fragment.partyAgreement.transactions";
    }



    /*-----------------------------------------------------------------*/
	/*-------------------- Private methods ----------------------------*/
	/*-----------------------------------------------------------------*/
	private void populateForm(Model model, PartyAgreementForm form, String pageMode) {
		model.addAttribute("partyAgreementForm", form);
		model.addAttribute("pageMode", pageMode);
	}
	
	private void populateSearchForm(Model model, PartyAgreementSearchForm searchForm,  String pageMode, Boolean isSearchDialog) {
		model.addAttribute("pageMode", pageMode);
		model.addAttribute("isSearchDialog", isSearchDialog);
		model.addAttribute("partyAgreementSearchForm", searchForm);
	}

	private PartyAgreement toDomain(PartyAgreementForm form) {
        PartyAgreement partyAgreement;

        if (form.getId() == null) {//creation of a new party Agreement
            partyAgreement = new PartyAgreement();
            partyAgreement.setAuthorizingParty(form.getAuthorizingParty());
            partyAgreement.setTransactions(new HashSet<>(form.getTransactions()));
        } else {//update of an existing party Agreement
            partyAgreement = partyAgreementService.findPartyAgreementById(form.getId());

            if(form.getAllTransactions()){
                // if the PA has been set to authorized on all txs
                partyAgreement.setTransactions(new HashSet<Transaction>());
            } else {
                // remove transactions if they have been removed
                for (Iterator<Transaction> it = partyAgreement.getTransactions().iterator(); it.hasNext();){
                    Transaction tx = it.next();
                    if(!form.getTransactions().contains(tx)) {
                        it.remove();
                    }
                }

                // add transactions
                for(Transaction tx : form.getTransactions()) {
                    partyAgreement.getTransactions().add(tx);
                }
            }
        }

        partyAgreement.setDelegateParty(form.getDelegateParty());

        return partyAgreement;
    }

    private PartyAgreementForm toForm(PartyAgreement partyAgreement) {
        PartyAgreementForm form = new PartyAgreementForm();

        if(partyAgreement != null) {
            form.setId(partyAgreement.getId());
            form.setAuthorizingParty(partyAgreement.getAuthorizingParty());
            form.setDelegateParty(partyAgreement.getDelegateParty());
            if(!CollectionUtils.isEmpty(partyAgreement.getTransactions())){
                form.setTransactions(new ArrayList<>(partyAgreement.getTransactions()));
                form.setAllTransactions(Boolean.FALSE);
            }
        }

        return form;
    }
}