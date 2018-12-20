/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.dto.UserAccessRightsElement;
import eu.europa.ec.etrustex.admin.old.web.dto.UserForm;
import eu.europa.ec.etrustex.admin.old.web.dto.UserSearchForm;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.utils.*;
import eu.europa.ec.etrustex.admin.old.web.validators.UserConfigurationValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.UserSearchValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.UserValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.dao.exception.RecordNotFoundException;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.admin.User;
import eu.europa.ec.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.IPartyService;
import eu.europa.ec.etrustex.services.admin.IUserRoleService;
import eu.europa.ec.etrustex.services.admin.IUserService;
import eu.europa.ec.etrustex.services.util.EncryptionService;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author batrian
 */
@Controller
@SessionAttributes({"userSearchForm"})
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ADM', 'CBO', 'SUP')")
public class UserController {
    private static final String PAGE_USER = "page.user";
    private static final String FRAG_USER_INNER = "page.user.inner";
    private static final String PAGE_USER_SEARCH = "page.userSearch";
    private static final String FRAG_USER_SEARCH = "page.userSearch.inner";
    private static final String PAGE_USER_VIEW_FRAGMENT = "page.userView.inner";
    private static final String FRAG_USER_CONFIGURATIONS = "page.user.inner.configurations";
    private static final String FRAG_USER_PARTY = "frag.user.party";
    private static final String FRAG_USER_CHANGE_PWD = "page.user.inner.changePwd";
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private IPartyService partyService;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserConfigurationValidator userConfigurationValidator;
    @Autowired
    private UserSearchValidator userSearchValidator;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private BusinessDomainUtil businessDomainUtil;
    @Autowired
    private Referer refererPage;


    /*-----------------------------------------------------------------*/
    /*---------------------------- Bindings ---------------------------*/
    /*-----------------------------------------------------------------*/
    @InitBinder("userForm")
    public void initUserFormBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }

    @InitBinder("userSearchForm")
    public void initUserSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(userSearchValidator);
    }

    @InitBinder("newUserConfiguration")
    public void initUserConfigurationBinder(WebDataBinder binder) {
        binder.setValidator(userConfigurationValidator);
    }


    /*-----------------------------------------------------------------*/
    /*------------------------ Model Attributes -----------------------*/
    /*-----------------------------------------------------------------*/
    @ModelAttribute("userSearchForm")
    public UserSearchForm getUserSearchForm() {
        return new UserSearchForm();
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("pageMode", "new");
        refererPage.setAction("create");

        return PAGE_USER;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/create/load", method = RequestMethod.POST)
    public String createLoad(Model model, @ActiveUser SessionUserInformation userInfo) {
        populateUserForm(model, new UserForm(new User()), "new", userInfo);

        return FRAG_USER_INNER;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @UserActionsLog(entity = User.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult result,
                           @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) throws NoSuchAlgorithmException {
        User user = userForm.getUser();

        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.user.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), user.getId());
        }

        String message;

        List<UserAccessRightsElement> userAccessRights = userForm.getUserAccessRights();

        /*
         * ETRUSTEX-2584 CIPAdmin User Creation - CBO can create global ADM
         */
        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            for (UserAccessRightsElement uare : userAccessRights) {
                if (uare.getUar().getRole().getCode().equals(UserRoleEnum.ADM.name())) {
                    throw new AccessDeniedException("User not allowed to access this resource");
                }
            }
        }

        if (user.getId() == null) {//creation
            if (StringUtils.isNotEmpty(userForm.getUser().getPassword())) {
                user.setPassword(encryptionService.encryptPassword(userForm.getUser().getPassword()));
            }

            if (user.getAccessInfo() == null) {
                user.setAccessInfo(new EntityAccessInfo());
            }

            user.getAccessInfo().setCreationId(userInfo.getUsername());

            if (CollectionUtils.isNotEmpty(userAccessRights)) {
                fillInAccessRigthsDetails(userAccessRights, userInfo.getUsername());

                Set<UserAccessRights> uarSet = new HashSet<>();
                for (UserAccessRightsElement userAccessRightsElement : userAccessRights) {
                    uarSet.add(userAccessRightsElement.getUar());
                }
                user.setAccessRights(uarSet);
            }

            userService.create(user);

            message = messageSource.getMessage("user.success.create", new Object[]{userForm.getUser().getName(), user.getId()}, LocaleContextHolder.getLocale());


        } else {//modification
            EntityAccessInfo userEai = new EntityAccessInfo();
            userEai.setModificationId(userInfo.getUsername());
            user.setAccessInfo(userEai);

            List<Long> uarIdsToDelete = new ArrayList<Long>();
            Set<UserAccessRights> uars = new HashSet<>();
            if (CollectionUtils.isNotEmpty(userAccessRights)) {
                fillInAccessRigthsDetails(userAccessRights, userInfo.getUsername());
                Iterator<UserAccessRightsElement> uarIterator = userAccessRights.iterator();
                while (uarIterator.hasNext()) {
                    UserAccessRightsElement uarElem = uarIterator.next();
                    if (uarElem.getUar().getId() != null && uarElem.getIsMarkedForDeletion()) {
                        uarIdsToDelete.add(uarElem.getUar().getId());
                    } else {
                        uars.add(uarElem.getUar());
                    }
                }
            }
            user.setAccessRights(uars);

            if (StringUtils.isNotBlank(user.getPassword())) {
                String encryptedPwd = encryptionService.encryptPassword(user.getPassword());
                user.setPassword(encryptedPwd);
            }
            try {
                user = userService.update(user, uarIdsToDelete);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            }


            userForm.setChangePasswordFlag(false);
            userForm.setUser(user);

            message = messageSource.getMessage("user.success.edit",
                    new Object[]{user.getName(), user.getId()},
                    LocaleContextHolder.getLocale());

        }


        return new AjaxResult(true, message, user.getId());
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @UserActionsLog(entity = User.class)
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult changePwd(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult result,
                                @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) throws NoSuchAlgorithmException {
        User user = userForm.getUser();

        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.user.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), user.getId());
        }

        EntityAccessInfo userEai = new EntityAccessInfo();
        userEai.setModificationId(userInfo.getUsername());
        user.setAccessInfo(userEai);

        if (StringUtils.isNotBlank(user.getPassword())) {
            String encryptedPwd = encryptionService.encryptPassword(user.getPassword());
            user.setPassword(encryptedPwd);
        }

        try {
            user = userService.changePwd(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        String message = messageSource.getMessage("user.success.edit", new Object[]{user.getName(), user.getId()}, LocaleContextHolder.getLocale());


        return new AjaxResult(true, message, user.getId());
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value = "fromView", required = false) boolean fromView, @ModelAttribute("userSearchForm") UserSearchForm criteria, Model model, @ActiveUser SessionUserInformation userInfo) {
        if (fromView) { // Run search if coming back from user view.
            criteria.setSearchOnLoad(true);
        } else {
            criteria = new UserSearchForm();
            criteria.setSearchOnLoad(false);
        }
        model.addAttribute("userSearchForm", criteria);

        refererPage.setAction("search");

        return PAGE_USER_SEARCH;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@ModelAttribute("userSearchForm") UserSearchForm criteria, Model model, SessionStatus sessionStatus, @ActiveUser SessionUserInformation userInfo) {
        populateSearchForm(model, criteria, null, userInfo);

        sessionStatus.setComplete();

        return FRAG_USER_SEARCH;
    }

    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.searchResults(..)) && args(*, result, .., request)")
     */
    @UserActionsLog(entity = User.class)
    @RequestMapping(value = "/search/results", method = RequestMethod.POST)
    public String searchResults(@ModelAttribute("userSearchForm") @Valid UserSearchForm criteria, BindingResult result,
                                Model model, @ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, HttpServletRequest request) {
        criteria.setSearchOnLoad(false);

        sessionStatus.setComplete();

        if (result.hasErrors()) {
            model.addAttribute("userSearchForm", criteria);
            populateSearchForm(model, criteria, null, userInfo);

            return FRAG_USER_SEARCH;
        }


        List<User> users = userService.getUsers(criteria.getUsernameSearch(),
                FormUtil.convertDefaultOptionToNull(criteria.getUserRoleIdSearch()),
                FormUtil.convertDefaultOptionToNull(criteria.getBusinessDomainIdSearch()),
                FormUtil.convertDefaultOptionToNull(criteria.getPartyIdSearch()));

        populateSearchForm(model, criteria, "", userInfo);
        model.addAttribute("users", users);

        return FRAG_USER_SEARCH;
    }


    @UserActionsLog(entity = User.class)
    @RequestMapping(value = "/{userId}/view", method = RequestMethod.GET)
    public String view(@PathVariable("userId") Long id,
                       @ModelAttribute("userSearchForm") UserSearchForm criteria, Model model, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        populateSearchForm(model, criteria, "view", userInfo);
        model.addAttribute("userId", id);

        return PAGE_USER;
    }

    @UserActionsLog(entity = User.class)
    @RequestMapping(value = "/{userId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("userId") Long userId, @ActiveUser SessionUserInformation userInfo,
                           @ModelAttribute("userSearchForm") UserSearchForm criteria, Model model, SessionStatus sessionStatus, HttpServletRequest request) {
        User user = userService.getUser(userId);

        if (user == null) {
            throw new RecordNotFoundException(User.class.getSimpleName(), userId);
        }

        UserForm userForm = new UserForm(user);

        populateSearchForm(model, criteria, "view", userInfo);
        model.addAttribute("userForm", userForm);

        sessionStatus.setComplete();

        /*
         * UC80_BR46	User cannot modify his/her own user configuration.
         * However, user can send a modification request to a more powerful user containing the details to modify and the reason.
         * Note that user can change his/her own password via User / Change password menu option.
         */
        if (refererPage.getAction().equals("edit")) {
            if (userInfo.getUserId().equals(userId)) {
                model.addAttribute("validationMsg", getIsSelfMessage(userInfo, user));
            }

            /*
             *  ETRUSTEX-2533 CBO can only modify user configurations associated to his business domain and using CBO user role
             */
            else if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
                model.addAttribute("validationMsg", messageSource.getMessage("error.user.delete.modify.role.adm", new Object[]{UserRoleEnum.ADM.name()}, LocaleContextHolder.getLocale()));
            }

        }

        return PAGE_USER_VIEW_FRAGMENT;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{userId}/edit", method = RequestMethod.GET)
    public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("userId") Long userId,
                       @ModelAttribute("userSearchForm") UserSearchForm criteria, Model model) {
        String pageMode;
        boolean forbidden = false;

        /*
         * UC80_BR46 User cannot modify his/her own user configuration.
         * However, user can send a modification request to a more powerful user containing the details to modify and the reason.
         * Note that user can change his/her own password via User / Change password menu option.
         */
        if (userInfo.getUserId().equals(userId)) {
            forbidden = true;
        } else {
            /*
             *  ETRUSTEX-2533 CBO can only modify user configurations associated to his business domain and using CBO user role
             */
            User user = userService.getUser(userId);
            if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
                for (UserAccessRights uar : user.getAccessRights()) {
                    if (uar.getRole().getCode().equals(UserRoleEnum.ADM.name()) || !Objects.equals(uar.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId())) {
                        forbidden = true;
                        break;
                    }
                }
            }
        }

        if (forbidden) {
            refererPage.setAction("edit");
            pageMode = "view";
        } else {
            pageMode = "edit";
        }

        model.addAttribute("pageMode", pageMode);
        model.addAttribute("userId", userId);

        return PAGE_USER;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{userId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@PathVariable("userId") Long userId, @ModelAttribute("userSearchForm") UserSearchForm criteria, @ActiveUser SessionUserInformation userInfo, Model model) {
        User user = userService.getUser(userId);
        UserForm userForm = new UserForm(user);
        Set<UserAccessRightsElement> uarCollection = new HashSet<>();

        if (user.getAccessRights() != null && CollectionUtils.isNotEmpty(user.getAccessRights())) {
            for (UserAccessRights uar : user.getAccessRights()) {
                uarCollection.add(new UserAccessRightsElement(uar));
            }
        }

        userForm.setUserAccessRights(uarCollection);
        userForm.getUser().setPassword(null);

        populateUserForm(model, userForm, "edit", userInfo);
        model.addAttribute("userSearchForm", criteria);

        return FRAG_USER_INNER;
    }


    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @UserActionsLog(entity = User.class)
    @RequestMapping(value = "/{userId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("userId") Long userId, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        User user = userService.getUser(userId);

        if (user == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), userId);
        }

        /*
         * UC80_BR47	User cannot delete his/her own user configuration.
         * However, user can send a removal request to a more powerful user explaining the reason for deleting his/her account.
         */
        if (userInfo.getUserId().equals(userId)) {
            return new AjaxResult(false, getIsSelfMessage(userInfo, user), userId);
        }

        /*
         *  UC80_BR38	CBO can only delete users pertaining to their business domain and having only
         *  CBO role in the context of this business domain. If the user is also configured with another business domain,
         *  or has also and ADM or CBO right, the CBO is not allowed to delete the user.
         *  Instead he/she can edit the user and remove the specific CBO configuration pertaining to his domain if one exists.
         */
        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            for (UserAccessRights uar : user.getAccessRights()) {
                if (!uar.getBusinessDomain().getId().equals(userInfo.getBusinessDomain().getId()) || uar.getRole().getCode().equals(UserRoleEnum.ADM.name())) {
                    return new AjaxResult(false, messageSource.getMessage("error.user.delete.modify.role.adm", new Object[]{UserRoleEnum.ADM.name()}, LocaleContextHolder.getLocale()), userId);
                }
            }
        }

        userService.delete(user);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), userId);
    }


    @RequestMapping(value = "/search/refreshParties", method = RequestMethod.POST)
    public String refreshParties(@ModelAttribute("userSearchForm") UserSearchForm criteria, Model model, @ActiveUser SessionUserInformation userInfo) {
        populateSearchForm(model, criteria, "", userInfo);

        return FRAG_USER_SEARCH;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/add/party/{id}", method = RequestMethod.GET)
    public String addUserPartyToCurrentConfigurationAndRefresh(@PathVariable("id") Long partyId, @ModelAttribute("userForm") UserForm userForm,
                                                               @ActiveUser SessionUserInformation userInfo, Model model) {
        Party party = partyService.getParty(partyId);
        userForm.getNewUserConfiguration().setParty(party);
        populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

        return FRAG_USER_PARTY;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/addUserConfiguration", method = RequestMethod.POST)
    public String addUserConfiguration(@ModelAttribute("userForm") UserForm userForm, BindingResult result, @ActiveUser SessionUserInformation userInfo, Model model) {
        try {
            result.pushNestedPath("newUserConfiguration");
            ValidationUtils.invokeValidator(userConfigurationValidator, userForm.getNewUserConfiguration(), result);
        } finally {
            result.popNestedPath();
        }

        if (result.hasErrors()) {
            log.debug("UserController -  addUserConfiguration - there are validation errors!");
            populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

            return FRAG_USER_CONFIGURATIONS;
        } else {
            userValidator.validateNewConfigurationAgainstExisting(userForm, result);
            if (result.hasErrors()) {
                populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

                return FRAG_USER_CONFIGURATIONS;
            }
        }

        if (userForm.getUserAccessRights() == null) {
            userForm.setUserAccessRights(new ArrayList<UserAccessRightsElement>());
        }

        // Populate UserAccessRights
        UserAccessRights userAccessRights = userForm.getNewUserConfiguration();
        userAccessRights.setBusinessDomain(businessDomainService.getBusinessDomain(userAccessRights.getBusinessDomain().getId()));
        userAccessRights.setRole(userRoleService.getRole(userAccessRights.getRole().getId()));
        if (userAccessRights.getParty() != null && userAccessRights.getParty().getId() != null) {
            userAccessRights.setParty(partyService.getParty(userAccessRights.getParty().getId()));
        }

        userForm.getUserAccessRights().add(new UserAccessRightsElement(userAccessRights));
        //reset the configuration to be added
        userForm.setNewUserConfiguration(new UserAccessRights());

        populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);
        model.addAttribute("pageMode", userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit");

        return FRAG_USER_CONFIGURATIONS;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/removeUserConfiguration", method = RequestMethod.POST)
    public String removeUserConfiguration(@ModelAttribute("userForm") UserForm userForm, @RequestParam int userAccessRightsIdx,
                                          @ActiveUser SessionUserInformation userInfo, Model model) {
        List<UserAccessRightsElement> userAccessRightsList = userForm.getUserAccessRights();
        UserAccessRightsElement userAccessRightsElement = userAccessRightsList.get(userAccessRightsIdx);

        if (userAccessRightsElement.getUar().getId() == null) {
            // non existing UAR. Just remove them from the list
            userAccessRightsList.remove(userAccessRightsElement);
        } else {
            // existing UAR. Mark them for deletion
            userAccessRightsElement.setIsMarkedForDeletion(true);
        }

        userForm.setUserAccessRights(userAccessRightsList);
        populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

        return FRAG_USER_CONFIGURATIONS;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/editUserConfiguration", method = RequestMethod.POST)
    public String editUserConfiguration(@ModelAttribute("userForm") UserForm userForm, @RequestParam int userAccessRightsIdx, @ActiveUser SessionUserInformation userInfo, Model model) {
        List<UserAccessRightsElement> userAccessRightsList = userForm.getUserAccessRights();
        UserAccessRightsElement userAccessRightsElement = userAccessRightsList.remove(userAccessRightsIdx);
        UserAccessRights userAccessRights = userAccessRightsElement.getUar();
        userForm.setNewUserConfiguration(userAccessRights);
        userForm.setOriginalUserConfiguration(userAccessRights);

        model.addAttribute("editConfigurationMode", true);

        populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

        return FRAG_USER_CONFIGURATIONS;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/refreshConfigurations", method = RequestMethod.POST)
    public String refreshConfiguration(@ModelAttribute("userForm") UserForm userForm,
                                       @ActiveUser SessionUserInformation userInfo, Model model) {
        userForm.setNewUserConfiguration(new UserAccessRights());
        userForm.getUserAccessRights().add(new UserAccessRightsElement(userForm.getOriginalUserConfiguration()));

        populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

        return FRAG_USER_CONFIGURATIONS;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/updateUserConfiguration", method = RequestMethod.POST)
    public String updateUserConfiguration(@ModelAttribute("userForm") UserForm userForm,
                                          BindingResult result, @ActiveUser SessionUserInformation userInfo, Model model) {
        if (result.hasErrors()) {
            log.debug("UserController -  updateUserConfiguration - there are validation errors!");

            return FRAG_USER_CONFIGURATIONS;
        } else {
            userValidator.validateNewConfigurationAgainstExisting(userForm, result);
            if (result.hasErrors()) {
                log.debug("UserController -  updateUserConfiguration - New Configuration invalid!");

                return FRAG_USER_CONFIGURATIONS;
            }
        }

        setNewConfigurationData(userForm.getNewUserConfiguration());
        userForm.getNewUserConfiguration().setUser(userForm.getUser());

        if (userForm.getNewUserConfiguration().getId() == null) {
            userForm.addAccessRights(new UserAccessRightsElement(userForm.getNewUserConfiguration()));
        } else {
            userForm.findAndUpdateConfiguration(userForm.getNewUserConfiguration().getId());
        }


        //reset the configuration to be added
        userForm.setNewUserConfiguration(new UserAccessRights());

        populateUserForm(model, userForm, userForm.getUser() == null || userForm.getUser().getId() == null ? "new" : "edit", userInfo);

        return FRAG_USER_CONFIGURATIONS;
    }


    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @RequestMapping(value = "/changePwd", method = RequestMethod.GET)
    public String changePwd(@ActiveUser SessionUserInformation userInfo,
                            @ModelAttribute("userSearchForm") UserSearchForm criteria, Model model) {
        model.addAttribute("userId", userInfo.getUserId());
        model.addAttribute("pageMode", "changePwd");
        refererPage.setAction("changePwd");

        return PAGE_USER;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO', 'LBO')")
    @RequestMapping(value = "/changePwd/load", method = RequestMethod.POST)
    public String changePwdLoad(@ActiveUser SessionUserInformation userInfo, Model model) {
        User user = userService.getUser(userInfo.getUserId());

        UserForm userForm = new UserForm(user);
        Set<UserAccessRightsElement> uarCollection = new HashSet<UserAccessRightsElement>();

        if (user.getAccessRights() != null && CollectionUtils.isNotEmpty(user.getAccessRights())) {
            for (UserAccessRights uar : user.getAccessRights()) {
                uarCollection.add(new UserAccessRightsElement(uar));
            }
        }

        userForm.setUserAccessRights(uarCollection);
        userForm.getUser().setPassword(null);
        userForm.setIsChangePwdMode(true);

        populateUserForm(model, userForm, "changePwd", userInfo);

        return FRAG_USER_CHANGE_PWD;
    }

    /*-----------------------------------------------------------------*/
    /*-------------------- Private methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    private void fillInAccessRigthsDetails(List<UserAccessRightsElement> accessRights, String username) {
        for (UserAccessRightsElement uar : accessRights) {
            if (uar.getUar().getBusinessDomain() != null
                    && uar.getUar().getBusinessDomain().getId() != null && uar
                    .getUar().getBusinessDomain().getId() != -1) {
                uar.getUar().setBusinessDomain(businessDomainService.getBusinessDomain(uar.getUar().getBusinessDomain().getId()));
            } else {
                uar.getUar().setBusinessDomain(null);
            }
            if (uar.getUar().getRole() != null
                    && uar.getUar().getRole().getId() != null && uar
                    .getUar().getRole().getId() != -1) {
                uar.getUar().setRole(userRoleService.getRole(uar.getUar().getRole().getId()));
            } else {
                uar.getUar().setRole(null);
            }
            if (uar.getUar().getParty() != null
                    && uar.getUar().getParty().getId() != null && uar
                    .getUar().getParty().getId() != -1) {
                uar.getUar().setParty(partyService.getParty(uar.getUar().getParty().getId()));
            } else {
                uar.getUar().setParty(null);
            }

            if (uar.getUar().getAccessInfo() == null) {
                uar.getUar().setAccessInfo(new EntityAccessInfo());
            }

            if (uar.getUar().getId() == null) {
                uar.getUar().getAccessInfo().setCreationId(username);
            } else {
                uar.getUar().getAccessInfo().setModificationId(username);
            }
        }
    }


    /*
     * User cannot delete or modify himself
     */
    private String getIsSelfMessage(SessionUserInformation userInfo, User user) {

        if (StringUtils.equalsIgnoreCase(userInfo.getUsername(), user.getName())) {
            String actionUser;

            if (StringUtils.equalsIgnoreCase(userInfo.getRole().getCode(), UserRoleEnum.LBO.name())) {
                actionUser = UserRoleEnum.CBO.name();
            } else {
                actionUser = UserRoleEnum.ADM.name();
            }

            return messageSource.getMessage("error.user.delete.modify.self", new Object[]{actionUser}, LocaleContextHolder.getLocale());
        }

        return null;
    }

    private void setNewConfigurationData(UserAccessRights newCfg) {
        UserRole newRole = newCfg.getRole();//null;
        if (newRole == null || newRole.getId() == null || newRole.getId() == -1) {
            newCfg.setRole(null);
        } else {
            newCfg.setRole(userRoleService.getRole(newRole.getId()));
        }

        BusinessDomain newBusinessDomain = newCfg.getBusinessDomain();
        if (newBusinessDomain == null || newBusinessDomain.getId() == null || newBusinessDomain.getId() == -1) {
            newCfg.setBusinessDomain(null);
        } else {
            newCfg.setBusinessDomain(businessDomainService.getBusinessDomain(newBusinessDomain.getId()));
        }

        Party newParty = newCfg.getParty();
        if (newParty == null || newParty.getId() == null || newParty.getId() == -1) {
            newCfg.setParty(null);
        } else {
            newCfg.setParty(partyService.getParty(newParty.getId()));
        }
    }

    private List<UserRole> getUserRoles(SessionUserInformation userInfo) {
        log.debug("@ModelAttribute('userRoleList')");

        List<UserRole> userRoles = userRoleService.getAll();
        ;

        /*
         * ETRUSTEX-2584 CIPAdmin User Creation - CBO can create global ADM
         */
        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            UserRole admRole = new UserRole();
            admRole.setCode(UserRoleEnum.ADM.name());
            userRoles.remove(admRole);
        }

        Collections.sort(userRoles, new Comparator<UserRole>() {
            @Override
            public int compare(UserRole o1, UserRole o2) {
                return o1.getDescription().compareToIgnoreCase(o2.getDescription());
            }
        });

        UserRole userRole = new UserRole();
        userRole.setId(Long.valueOf(-1));
        userRole.setDescription(messageSource.getMessage("choose.please", null, LocaleContextHolder.getLocale()));

        userRoles.add(0, userRole);

        return userRoles;
    }

    private void populateUserForm(Model model, UserForm userForm, String pageMode, SessionUserInformation userInfo) {
        /*
         * CIPADMIN-194. Password for user temporarily shown (hashed and starred) when saving
         * Empty password field
         */
        userForm.getUser().setPassword(null);
        userForm.setPasswordCheck(null);

        model.addAttribute("userForm", userForm);

        if (!model.containsAttribute("userRoleList")) {
            model.addAttribute("userRoleList", getUserRoles(userInfo));
        }

        if (!model.containsAttribute("businessDomainList")) {
            /*
             * UC80_BR03 For CBO, the selected business domain is defaulted to their business domain and not modifiable.
             */
            List<BusinessDomain> businessDomains;
            if (userInfo.getRole().getCode().equals(UserRoleEnum.ADM.name())) {
                businessDomains = businessDomainUtil.getUserBusinessDomains(true);
            } else {
                businessDomains = new ArrayList<>();
                businessDomains.add(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()));
            }

            model.addAttribute("businessDomainList", businessDomains);
        }

        model.addAttribute("pageMode", pageMode);
    }

    private void populateSearchForm(Model model, UserSearchForm criteria, String pageMode, SessionUserInformation userInfo) {
        if (criteria != null && criteria.getBusinessDomainIdSearch() != null) {
            model.addAttribute("partyList", partyService.getAllPartiesForBusinessDomain(criteria.getBusinessDomainIdSearch()));
        } else {
            model.addAttribute("partyList", userService.getUserParties(userInfo.getUsername(), userInfo.getBusinessDomain().getId()));
        }

        if (!model.containsAttribute("userRoleList")) {
            model.addAttribute("userRoleList", getUserRoles(userInfo));
        }

        if (!model.containsAttribute("businessDomainList")) {
            model.addAttribute("businessDomainList", businessDomainUtil.getUserBusinessDomains(true));
        }

        model.addAttribute("userSearchForm", criteria);
        model.addAttribute("pageMode", pageMode);
    }
}
