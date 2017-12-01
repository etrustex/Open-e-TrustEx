package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.RetentionPolicySearchForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.dto.SlaPolicyForm;
import eu.europa.ec.cipa.admin.web.logs.UserActionsLog;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.utils.*;
import eu.europa.ec.cipa.admin.web.validators.RetentionPolicySearchValidator;
import eu.europa.ec.cipa.admin.web.validators.RetentionPolicyValidator;
import eu.europa.ec.cipa.admin.web.validators.ValidationHelper;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.domain.sla.SlaPolicy;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.integration.task.ISlaPolicyService;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.ITransactionService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import eu.europa.ec.cipa.etrustex.types.SlaType;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by guerrpa on 23/05/2016.
 */

@Controller
@RequestMapping(value = "/retentionPolicy")
@SessionAttributes({"retentionPolicySearchForm", "retentionPolicyForm"})
//@PreAuthorize("hasAnyRole('ADM', 'SUP', 'CBO')")
@PreAuthorize("hasRole('NO_ROLE_FOR_THE_MOMENT')")

public class RetentionPolicyController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired private ISlaPolicyService slaPolicyService;
    @Autowired private IBusinessDomainService businessDomainService;
    @Autowired private ITransactionService transactionService;
    @Autowired private MessageSource messageSource;
    @Autowired private RetentionPolicyValidator retentionPolicyValidator;
    @Autowired private RetentionPolicySearchValidator retentionPolicySearchValidator;
    @Autowired private ValidationHelper validationHelper;
    @Autowired private BusinessDomainUtil businessDomainUtil;
    @Autowired private IUserService userService;

    @Autowired private Referer refererPage;

    /*-----------------------------------------------------------------*/
	/*---------------------------- Binding ----------------------------*/
	/*-----------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(BusinessDomain.class, new PropertyEditorSupport() {
            Object value;

            @Override
            public void setAsText(String text) {
                value = StringUtils.isEmpty(text) || text.equals("-1") ?
                        businessDomainUtil.getTransientBD(new Long("-1"), false) :
                        businessDomainService.getBusinessDomain(new Long(text));
            }

            @Override
            public Object getValue() {
                return value;
            }
        });
    }

    @InitBinder("retentionPolicyForm")
    public void initFormBinder(WebDataBinder binder) {
        binder.setValidator(retentionPolicyValidator);
    }

    @InitBinder("retentionPolicySearchForm")
    public void initUserFormBinder(WebDataBinder binder) {
        binder.setValidator(retentionPolicySearchValidator);
    }




    /*-----------------------------------------------------------------*/
	/*------------------------ Model Attributes -----------------------*/
	/*-----------------------------------------------------------------*/
    @ModelAttribute("retentionPolicySearchForm")
    public RetentionPolicySearchForm searchForm() {
        return new RetentionPolicySearchForm();
    }

    @ModelAttribute("retentionPolicyForm")
    public SlaPolicyForm form() {
        return new SlaPolicyForm();
    }

    @ModelAttribute("businessDomains")
    public List<BusinessDomain> businessDomains() {
        return businessDomainUtil.getUserBusinessDomains(true);
    }


    /*-----------------------------------------------------------------*/
	/*-------------------- Handler methods ----------------------------*/
	/*-----------------------------------------------------------------*/
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, HttpSession session) {
        model.addAttribute("pageMode", "new");
        refererPage.setAction("create");

        return "page.retentionPolicy";
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(Model model, @ActiveUser SessionUserInformation userInfo, HttpSession session) {
        /*
         * UC0030_ BR07	"Active" flag is mandatory (true or false)
         * By default, the flag is set to 'true' in the screen
         */
        SlaPolicyForm form = new SlaPolicyForm();
        form.setActiveFlag(true);
        form.setBusinessDomain(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()));

        populateForm(model, form, "new");

        return "frag.retentionPolicy.inner";
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasAnyRole('ADM', 'CBO')") //  and (#form.businessDomain.id == principal.businessDomain.id)
    @UserActionsLog(entity = SlaPolicy.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("retentionPolicyForm") @Valid SlaPolicyForm form, BindingResult result, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.retentionPolicy.save", new Object[] { validationHelper.getSaveErrorsMsg(result) }, LocaleContextHolder.getLocale()), form.getId());
        }

        SlaPolicy slaPolicy = new SlaPolicy();
        EntityAccessInfo eai = new EntityAccessInfo();
        /*
         * UC0030_BR16	The retention type is automatically set to RETENTION by the System.
         */
        slaPolicy.setType(SlaType.RETENTION);

        try{
            PropertyUtils.copyProperties(slaPolicy, form);
            if (form.getId() == null) { //create
                eai.setCreationId(userInfo.getUsername());
                slaPolicy.setAccessInfo(eai);
                slaPolicy = slaPolicyService.create(slaPolicy);
            } else { //update
                eai.setModificationId(userInfo.getUsername());
                slaPolicy.setAccessInfo(eai);
                slaPolicy = slaPolicyService.update(slaPolicy);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new AjaxResult(false, messageSource.getMessage("common.system.error", null, LocaleContextHolder.getLocale()), slaPolicy.getId());
        }

        return new AjaxResult(true, messageSource.getMessage("retentionPolicy.success.save", new Object[] { slaPolicy.getId() }, LocaleContextHolder.getLocale()), slaPolicy.getId());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value="fromView", required=false) boolean fromView, @ModelAttribute("retentionPolicySearchForm") RetentionPolicySearchForm form, Model model) {
        // Run search if coming back from view.
        if (fromView) {
            form.setSearchOnLoad(true);
        } else {
            form = new RetentionPolicySearchForm();
            form.setSearchOnLoad(false);
        }

        populateSearchForm(model, form, "");
        refererPage.setAction("search");

        return "page.retentionPolicySearch";
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@RequestParam(value="businessDomain", required=false) Long businessDomainId,
                             @ModelAttribute("retentionPolicySearchForm") RetentionPolicySearchForm form, Model model, SessionStatus sessionStatus) {
        populateSearchForm(model, form, "");
        sessionStatus.setComplete();


        return "frag.retentionPolicySearch.inner";
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = SlaPolicy.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@ModelAttribute("retentionPolicySearchForm") @Valid RetentionPolicySearchForm form, BindingResult result, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        form.setSearchOnLoad(false);
        sessionStatus.setComplete();

        if (result.hasErrors()){
            populateSearchForm(model, form, "");

            return "frag.retentionPolicySearch.inner";
        }

/*
(1)	The User enters one or more search criteria.  A search criterion left empty means the concerned field will not be taken into account. The user can search by:
a.	Business domain
-	No selection = system will search for retention policies pertaining to any of the business domains the user is configured with
b.	Transaction name
c.	Active flag
-	No selection = both active and inactive retention policies are searched for
-	Yes = only active retention policies are searched for
-	No = only inactive retention policies are searched for

 */
        List<BusinessDomain> businessDomains = new ArrayList<>();
        if (FormUtil.convertDefaultOptionToNull(form.getBusinessDomain_search().getId()) != null) {
            businessDomains.add(form.getBusinessDomain_search());
        } else {
            businessDomains = businessDomainUtil.getUserBusinessDomains(false);
        }

        SlaPolicy slaPolicyExample = new SlaPolicy();
        slaPolicyExample.setType(SlaType.RETENTION);
        slaPolicyExample.setTransaction(new Transaction());
        slaPolicyExample.getTransaction().setName(form.getTransaction_search());
        slaPolicyExample.setActiveFlag(form.getActiveFlag_search());

        List<SlaPolicy> slaPolicies = slaPolicyService.findPoliciesByCriteriaForView(slaPolicyExample, businessDomains);



        model.addAttribute("slaPolicies", slaPolicies);
        populateSearchForm(model, form, "");

        return "frag.retentionPolicySearch.inner";
    }


    @UserActionsLog(entity = SlaPolicy.class)
    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String view(@PathVariable Long id, @ModelAttribute("retentionPolicySearchForm") RetentionPolicySearchForm form, Model model, HttpServletRequest request) {
        populateSearchForm(model, form, "view");
        model.addAttribute("id", id);

        return "page.retentionPolicy";
    }

    @UserActionsLog(entity = SlaPolicy.class)
    @RequestMapping(value = "/{id}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable Long id, @ModelAttribute("retentionPolicySearchForm") RetentionPolicySearchForm searchForm, @ActiveUser SessionUserInformation userInfo,
                           SessionStatus sessionStatus, Model model, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        SlaPolicy slaPolicy = slaPolicyService.findById(id);

        if(slaPolicy == null) {
            throw new AccessDeniedException(messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()));
        } else {
            // TODO REFACTOR UserAccessRights --> E.g. Populate and use GrantedAuthorities in CustomUserDetailsService
            boolean isAuthorized = false;
            Set<UserAccessRights> userAccessRightses = userService.getUser(userInfo.getUserId()).getAccessRights();
            for(UserAccessRights uar : userAccessRightses) {
                if (uar.getBusinessDomain().equals(slaPolicy.getBusinessDomain())
                        && ( uar.getRole().getCode().equals(UserRoleEnum.ADM.name())
                                || uar.getRole().getCode().equals(UserRoleEnum.CBO.name())
                                || uar.getRole().getCode().equals(UserRoleEnum.SUP.name()))) {
                    isAuthorized = true;
                }
            }

            if(!isAuthorized) {
                throw new AccessDeniedException("User not allowed to access this resource");
            }
        }

        SlaPolicyForm form = new SlaPolicyForm();
        PropertyUtils.copyProperties(form, slaPolicy);
        populateForm(model, form, "view");

        populateSearchForm(model, searchForm, "view");

        if(!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return "frag.retentionPolicyView.inner";
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, @ActiveUser SessionUserInformation userInfo,
                       @ModelAttribute("retentionPolicySearchForm") RetentionPolicySearchForm form, Model model) {

        model.addAttribute("id", id);

        populateSearchForm(model, form, "edit");

        return "page.retentionPolicy";
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{id}/edit/load", method = RequestMethod.POST)
    public String editLoad(@PathVariable Long id, @ModelAttribute("retentionPolicySearchForm") RetentionPolicySearchForm searchForm, @ActiveUser SessionUserInformation userInfo, Model model) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        SlaPolicy slaPolicy = slaPolicyService.findById(id);

        if(slaPolicy == null || !slaPolicy.getBusinessDomain().getId().equals(userInfo.getBusinessDomain().getId())) {
            throw new AccessDeniedException("User not allowed to access this resource");
        }

        SlaPolicyForm form = new SlaPolicyForm();
        PropertyUtils.copyProperties(form, slaPolicy);

        populateForm(model, form, "edit");
        populateSearchForm(model, searchForm, "edit");

        return "frag.retentionPolicy.inner";
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @UserActionsLog(entity = SlaPolicy.class)
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable Long id,
                             @ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, HttpServletRequest request) {
        SlaPolicy slaPolicy = slaPolicyService.findById(id);

        if (slaPolicy == null || !slaPolicy.getType().equals(SlaType.RETENTION) || !slaPolicy.getBusinessDomain().getId().equals(userInfo.getBusinessDomain().getId())) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), null);
        }

        slaPolicyService.delete(id);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), slaPolicy.getId());
    }

    @RequestMapping(value = "/add/transaction/{id}", method = RequestMethod.GET)
    public String addTransaction(@PathVariable("id") Long transactionId, @ModelAttribute("retentionPolicyForm") SlaPolicyForm form, @ActiveUser SessionUserInformation userInfo, Model model){
        Transaction transaction = transactionService.getTransaction(transactionId);
        form.setTransaction(transaction);
        populateForm(model, form, form.getId() == null ? "new" : "edit");

        return "frag.retentionPolicy.transaction";
    }

    private void populateForm(Model model, SlaPolicyForm form, String mode) {
        model.addAttribute("retentionPolicyForm", form);
        model.addAttribute("pageMode", mode);
    }

    private void populateSearchForm(Model model, RetentionPolicySearchForm form, String pageMode) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("retentionPolicySearchForm", form);
    }
}
