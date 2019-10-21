package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.BusinessDomainElement;
import eu.europa.ec.etrustex.admin.old.web.dto.ProfileForm;
import eu.europa.ec.etrustex.admin.old.web.dto.ProfileSearchForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.BusinessDomainUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.Referer;
import eu.europa.ec.etrustex.admin.old.web.validators.ProfileSearchValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ProfileValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.IProfileService;
import eu.europa.ec.etrustex.services.ITransactionService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/profile")
@SessionAttributes({"profileSearchForm"})
@PreAuthorize("hasAnyRole('ADM', 'SUP')")
public class ProfileController {
    private static final String PAGE_PROFILE = "page.profile";
    private static final String PAGE_PROFILE_SEARCH = "page.profileSearch";
    private static final String FRAG_PROFILE_INNER = "frag.profile.inner";
    private static final String FRAG_PROFILE_SEARCH = "frag.profileSearch.inner";
    private static final String FRAG_PROFILE_VIEW = "frag.profileView.inner";

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProfileValidator profileValidator;
    @Autowired
    private ProfileSearchValidator profileSearchValidator;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private IProfileService profileService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private BusinessDomainUtil businessDomainUtil;
    @Autowired
    private Referer refererPage;

    private Map<String, String> confidentialityLevels;
    private Map<String, String> integrityLevels;
    private Map<String, String> availabilityLevels;

    @PostConstruct
    public void setCiaLevels() {
        confidentialityLevels = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            confidentialityLevels.put("" + i, messageSource.getMessage("cia.level.confidentiality." + i, null, LocaleContextHolder.getLocale()));
        }

        integrityLevels = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            integrityLevels.put("" + i, messageSource.getMessage("cia.level.integrity." + i, null, LocaleContextHolder.getLocale()));
        }

        availabilityLevels = new LinkedHashMap<>();
        for (int i = 0; i < 3; i++) {
            availabilityLevels.put("" + i, messageSource.getMessage("cia.level.availability." + i, null, LocaleContextHolder.getLocale()));
        }
    }

    /*-----------------------------------------------------------------*/
    /*---------------------------- Bindings ---------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder("profileForm")
    public void initFormBinder(WebDataBinder binder) {
        binder.setValidator(profileValidator);

        binder.registerCustomEditor(List.class, "businessDomains", new CustomCollectionEditor(List.class) {
            @Override
            public Object convertElement(Object element) {
                Long id;
                if (element instanceof BusinessDomainElement) {
                    id = ((BusinessDomainElement) element).getId();
                } else if (element instanceof String) {
                    String idStr = (String) element;
                    if (StringUtils.isEmpty(idStr)) {
                        return null;
                    }
                    id = Long.valueOf(idStr);
                } else {
                    id = (Long) element;
                }

                return id.equals(Long.valueOf(-1)) ? businessDomainUtil.getTransientBD(id, true) : businessDomainService.getBusinessDomain(id);
            }
        });

        binder.registerCustomEditor(BusinessDomain.class, new PropertyEditorSupport() {
            Object value;

            @Override
            public void setAsText(String text) {
                value = StringUtils.isEmpty(text) || text.equals("-1") ?
                        businessDomainUtil.getTransientBD(Long.valueOf("-1"), false) :
                        businessDomainService.getBusinessDomain(Long.valueOf(text));
            }

            @Override
            public Object getValue() {
                return value;
            }
        });

        binder.registerCustomEditor(Set.class, "transactions", new CustomCollectionEditor(Set.class) {
            @Override
            public Object convertElement(Object element) {
                Long id;
                if (element instanceof Transaction) {
                    id = ((Transaction) element).getId();
                } else if (element instanceof String) {
                    String idStr = (String) element;
                    if (StringUtils.isEmpty(idStr)) {
                        return null;
                    }
                    id = Long.valueOf(idStr);
                } else {
                    id = (Long) element;
                }

                return transactionService.getTransaction(id);
            }
        });

        binder.registerCustomEditor(Transaction.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (getValue() == null) {
                    return null;
                } else {
                    Transaction obj = (Transaction) getValue();
                    return "" + obj.getId();
                }
            }
        });
    }

    @InitBinder("profileSearchForm")
    public void initSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(profileSearchValidator);

        binder.registerCustomEditor(BusinessDomain.class, "businessDomain", new PropertyEditorSupport() {
            Object value;

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public void setAsText(String text) {
                if (StringUtils.isEmpty(text) || text.equals("-1")) {
                    value = businessDomainUtil.getTransientBD(Long.valueOf("-1"), false);
                } else {
                    Long id = Long.valueOf(text);
                    value = businessDomainService.getBusinessDomain(id);
                }
            }

            @Override
            public String getAsText() {
                BusinessDomain obj = (BusinessDomain) getValue();
                return obj == null ? "-1" : "" + obj.getId();
            }


        });
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Integer.class, "cia.confidentiality", new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                String obj = (String) getValue();
                return null == obj ? "" : obj + " - " + confidentialityLevels.get(obj);
            }
        });

        binder.registerCustomEditor(Integer.class, "cia.integrity", new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                String obj = (String) getValue();
                return null == obj ? "" : obj + " - " + integrityLevels.get(obj);
            }
        });

        binder.registerCustomEditor(Integer.class, "cia.availability", new PropertyEditorSupport() {
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
    @ModelAttribute("profileForm")
    public ProfileForm docvumentForm(@RequestParam(required = false) Long id) {
        return new ProfileForm();
    }

    @ModelAttribute("profileSearchForm")
    public ProfileSearchForm profileSearchForm() {
        ProfileSearchForm profileSearchForm = new ProfileSearchForm();
        profileSearchForm.setBusinessDomain(businessDomainUtil.getTransientBD(Long.valueOf("-1"), false));
        profileSearchForm.setSearchOnLoad(false);
        return profileSearchForm;
    }

    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("pageMode", "new");
        refererPage.setAction("create");

        return PAGE_PROFILE;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(@ActiveUser SessionUserInformation userInfo, Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        populateForm(model, new Profile(), "new", userInfo);

        return FRAG_PROFILE_INNER;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasRole('ADM')")
    @UserActionsLog(entity = Profile.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("profileForm") @Valid ProfileForm form, BindingResult result,
                           @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.profile.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), form.getId());
        }

        Profile profile = toDomain(form);

        if (form.getBusinessDomains() != null && !form.getBusinessDomains().isEmpty()) {
            for (Iterator<BusinessDomain> it = form.getBusinessDomains().iterator(); it.hasNext(); ) {
                BusinessDomain bd = it.next();
                if (bd == null || bd.getId().equals(Long.valueOf(-1))) {
                    it.remove();
                }
            }

            profile.setBusinessDomains(form.getBusinessDomains());
        }

        Long profileId = null;
        EntityAccessInfo eai = new EntityAccessInfo();
        if (profile.getId() == null) {// creation of a new profile
            eai.setCreationId(userInfo.getUsername());
            profile.setAccessInfo(eai);
            profileId = profileService.create(profile).getId();
        } else {// update of an existing profile
            if (!interchangeAgreementService.getInterchangeAgreementsByProfile(profileId).isEmpty()) {
                return new AjaxResult(false, messageSource.getMessage("error.profile.used.by.ica", null, LocaleContextHolder.getLocale()), form.getId());
            }
            eai.setModificationId(userInfo.getUsername());
            profile.setAccessInfo(eai);
            profileId = profileService.update(profile).getId();
        }


        return new AjaxResult(true, messageSource.getMessage("profile.success.save", new Object[]{profileId}, LocaleContextHolder.getLocale()), profileId);
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@ActiveUser SessionUserInformation userInfo, @RequestParam(value = "fromView", required = false) boolean fromView,
                         @ModelAttribute("profileSearchForm") ProfileSearchForm profileSearchForm, Model model) {
        if (fromView) { // Run search if coming back from view.
            profileSearchForm.setSearchOnLoad(true);
        }

        refererPage.setAction("search");

        return PAGE_PROFILE_SEARCH;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@ActiveUser SessionUserInformation userInfo,
                             @ModelAttribute("profileSearchForm") ProfileSearchForm profileSearchForm, Model model, SessionStatus sessionStatus) {
        populateSearchForm(model, profileSearchForm, null, userInfo);
        sessionStatus.setComplete();

        return FRAG_PROFILE_SEARCH;
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = Profile.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@ActiveUser SessionUserInformation userInfo,
                                @ModelAttribute("profileSearchForm") @Valid ProfileSearchForm profileSearchForm, BindingResult result,
                                Model model, SessionStatus sessionStatus, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        profileSearchForm.setSearchOnLoad(false);

        sessionStatus.setComplete();

        if (result.hasErrors()) {
            populateSearchForm(model, profileSearchForm, null, userInfo);
            return FRAG_PROFILE_SEARCH;
        }

        List<Profile> profiles = profileService.findProfilesByCriteria(profileSearchForm.getName_search(), profileSearchForm.getTransactionName(),
                profileSearchForm.getBusinessDomain() != null && !Long.valueOf("-1").equals(profileSearchForm.getBusinessDomain().getId()) ? profileSearchForm.getBusinessDomain() : null);

        model.addAttribute("profiles", profiles);
        populateSearchForm(model, profileSearchForm, null, userInfo);

        return FRAG_PROFILE_SEARCH;
    }

    @UserActionsLog(entity = Profile.class)
    @RequestMapping(value = "/{profileId}/view", method = RequestMethod.GET)
    public String view(@ActiveUser SessionUserInformation userInfo, @PathVariable("profileId") Long id,
                       @ModelAttribute("profileSearchForm") ProfileSearchForm profileSearchForm,
                       Model model, HttpServletRequest request) {
        populateSearchForm(model, profileSearchForm, "view", userInfo);
        model.addAttribute("profileId", id);

        return PAGE_PROFILE;
    }

    @UserActionsLog(entity = Profile.class)
    @RequestMapping(value = "/{profileId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@ActiveUser SessionUserInformation userInfo, @PathVariable("profileId") Long id, @ModelAttribute("profileSearchForm") ProfileSearchForm profileSearchForm,
                           Model model, SessionStatus sessionStatus, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Profile profile = profileService.getProfile(id);

        if (profile == null) {
            throw new RecordNotFoundException(Profile.class.getSimpleName(), id);
        }

        populateForm(model, profile, "view", userInfo);
        model.addAttribute("profileSearchForm", profileSearchForm);

        if (!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return FRAG_PROFILE_VIEW;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/{profileId}/edit", method = RequestMethod.GET)
    public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("profileId") Long profileId,
                       @ModelAttribute("profileSearchForm") ProfileSearchForm profileSearchForm, Model model) {

        populateSearchForm(model, profileSearchForm, "edit", userInfo);
        model.addAttribute("profileId", profileId);

        return PAGE_PROFILE;
    }

    @PreAuthorize("hasRole('ADM')")
    @RequestMapping(value = "/{profileId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@ActiveUser SessionUserInformation userInfo, @PathVariable("profileId") Long profileId,
                           @ModelAttribute("profileSearchForm") ProfileSearchForm profileSearchForm, Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Profile profile = profileService.getProfile(profileId);

		/*
		 * UC40_BR12	All fields can be modified if no interchange agreement using this profile exists in the System. 
		 * If an interchangte agreement exists for the profile, then the selections of business domains and transactions associated to the profile cannot be changed. 
		 * This check shall be done twice:
		 •		First, when accessing the modification page, which will make the lists protected 
		 •		Second, when trying to save a modified transaction (in order to ensure that no ICA using this profile has been created in the meanwhile)
		 */
        if (!interchangeAgreementService.getInterchangeAgreementsByProfile(profileId).isEmpty()) {
            model.addAttribute("inUseByICA", true);
        }

        populateForm(model, profile, "edit", userInfo);
        model.addAttribute("profileSearchForm", profileSearchForm);

        return FRAG_PROFILE_INNER;
    }


    @PreAuthorize("hasRole('ADM')")
    @UserActionsLog(entity = Profile.class)
    @RequestMapping(value = "/{profileId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("profileId") Long profileId, HttpServletRequest request) {
        Profile profile = profileService.getProfile(profileId);

        if (profile == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), profileId);
        }

        if (profileService.isInUse(profile)) {
            return new AjaxResult(false, messageSource.getMessage("error.profile.inUse", null, LocaleContextHolder.getLocale()), profileId);
        }

        profileService.delete(profileId);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), profileId);
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Private methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    private Profile toDomain(ProfileForm form) {
        Profile profile = new Profile();
        profile.setId(form.getId());
        profile.setName(form.getName());
        profile.setNamespace(form.getNamespace());
        profile.setCiaLevel(form.getCiaLevel());
        profile.setTransactions(form.getTransactions());
        profile.setBusinessDomains(form.getBusinessDomains());

        return profile;
    }

    private ProfileForm toForm(Profile profile, String pageMode) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        ProfileForm form = new ProfileForm();
        PropertyUtils.copyProperties(form, profile);
        return form;
    }


    private void populateForm(Model model, Profile profile, String pageMode, SessionUserInformation userInfo) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        model.addAttribute("profileForm", toForm(profile, pageMode));
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("confidentialityLevels", confidentialityLevels);
        model.addAttribute("integrityLevels", integrityLevels);
        model.addAttribute("availabilityLevels", availabilityLevels);

        List<BusinessDomain> businessDomains = new ArrayList<>();
        for (BusinessDomainElement bde : userInfo.getBusinessDomains()) {
            businessDomains.add(businessDomainService.getBusinessDomain(bde.getId()));
        }
        businessDomains.add(0, businessDomainUtil.getTransientBD(Long.valueOf("-1"), true));
        model.addAttribute("businessDomains", businessDomains);

        if (pageMode.equals("new") || pageMode.equals("edit")) {
            model.addAttribute("transactions", transactionService.getAll());
        }
    }

    private void populateSearchForm(Model model, ProfileSearchForm form, String pageMode, SessionUserInformation userInfo) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("profileSearchForm", form);

        List<BusinessDomainElement> bdes = new ArrayList<>(userInfo.getBusinessDomains());
        bdes.add(0, new BusinessDomainElement(businessDomainUtil.getTransientBD(Long.valueOf("-1"), false)));
        model.addAttribute("businessDomains", bdes);
    }


}
