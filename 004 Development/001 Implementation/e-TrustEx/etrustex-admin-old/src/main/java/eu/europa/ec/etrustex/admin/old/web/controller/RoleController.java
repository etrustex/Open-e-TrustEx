package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.RoleForm;
import eu.europa.ec.etrustex.admin.old.web.dto.SelectElement;
import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.logs.UserActionsLog;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.admin.old.web.utils.AjaxResult;
import eu.europa.ec.etrustex.admin.old.web.utils.ICAUtil;
import eu.europa.ec.etrustex.admin.old.web.utils.UserRoleEnum;
import eu.europa.ec.etrustex.admin.old.web.validators.RoleValidator;
import eu.europa.ec.etrustex.admin.old.web.validators.ValidationHelper;
import eu.europa.ec.etrustex.domain.Role;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.IInterchangeAgreementService;
import eu.europa.ec.etrustex.services.IProfileService;
import eu.europa.ec.etrustex.services.IRoleService;
import org.apache.commons.beanutils.PropertyUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
    private static final String PAGE_ROLE = "page.role";
    private static final String PAGE_ROLE_INNER = "page.role.inner";
    private static final String FRAG_ROLE_CREATION = "frag.role.creation";
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IInterchangeAgreementService interchangeAgreementService;
    @Autowired
    private IProfileService profileService;
    @Autowired
    private IBusinessDomainService businessDomainService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RoleValidator roleValidator;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private ICAUtil icaUtil;

    /*--------------------------------------------------------------------*/
    /*----------------------------- Bindings -----------------------------*/
    /*--------------------------------------------------------------------*/
    @InitBinder("roleForm")
    public void initUserFormBinder(WebDataBinder binder) {
        binder.setValidator(roleValidator);
    }


    /*-----------------------------------------------------------------*/
    /*-------------------- Handler methods ----------------------------*/
    /*-----------------------------------------------------------------*/
    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model) {
        return PAGE_ROLE;
    }

    @PreAuthorize("hasAnyRole('ADM', 'SUP')")
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public String load(Model model) {

        RoleForm role = new RoleForm();
        role.setTechnicalFlag(false);
        role.setBidirectionalFlag(false);

        populateForm(model, role, "new");

        return PAGE_ROLE_INNER;
    }


    /*
     * Important! If arguments are modified, their order must correspond to the following enumeration:
     * @Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.*.save(..)) && args(form, .., request)")
     */
    @PreAuthorize("hasAnyRole('ADM', 'SUP')")
    @UserActionsLog(entity = Role.class)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("roleForm") @Valid RoleForm form, BindingResult result,
                           @ActiveUser SessionUserInformation userInfo, Model model, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (result.hasErrors()) {
            return new AjaxResult(false,
                    messageSource.getMessage("error.role.save", new Object[]{validationHelper.getSaveErrorsMsg(result)}, LocaleContextHolder.getLocale()), form.getId());
        }

        Long roleId = null;

        Role role = toDomain(form);

        role.setCode(role.getCode().trim());
        role.setName(role.getName().trim());

        if (role.getId() == null) {//creation of a new role
            EntityAccessInfo eai = new EntityAccessInfo();
            eai.setCreationId(userInfo.getUsername());
            role.setAccessInfo(eai);

            roleId = roleService.createRole(role).getId();

        } else {//update of an existing role
            EntityAccessInfo eai = new EntityAccessInfo();
            eai.setModificationId(userInfo.getUsername());
            role.setAccessInfo(eai);

            roleId = roleService.updateRole(role).getId();

        }

        return new AjaxResult(true, messageSource.getMessage("role.success.save", new Object[]{roleId}, LocaleContextHolder.getLocale()), roleId);
    }

    @PreAuthorize("hasAnyRole('ADM', 'SUP')")
    @RequestMapping(value = "/{roleId}/edit", method = RequestMethod.GET)
    public String editRoleView(@PathVariable("roleId") Long roleId, Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Role role = roleService.getRole(roleId);
        populateForm(model, toForm(role), "edit");

        model.addAttribute("existICAwithSameRoleForParties", interchangeAgreementService.existICAwithSameRoleForParties(role));

        return FRAG_ROLE_CREATION;
    }


    @PreAuthorize("hasAnyRole('ADM', 'SUP')")
    @UserActionsLog(entity = Role.class)
    @RequestMapping(value = "/{roleId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("roleId") Long roleId, HttpServletRequest request) {
        Role role = roleService.getRole(roleId);

        if (role == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), roleId);
        }

        if (roleService.isInUse(role)) {
            return new AjaxResult(false, messageSource.getMessage("error.role.inUse", null, LocaleContextHolder.getLocale()), roleId);
        }

        roleService.deleteRole(role.getId());
        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), roleId);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public String cancel(Model model) {
        populateForm(model, new RoleForm(), "new");

        return FRAG_ROLE_CREATION;
    }

    @ResponseBody
    @RequestMapping(value = "/forProfile", method = RequestMethod.GET)
    public List<SelectElement> reloadRolesForProfile(@ActiveUser SessionUserInformation userInfo, @RequestParam Long profileId) {

        if (profileId.equals(Long.valueOf(-1))) {
            return new ArrayList<>();
        }

        if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())
                && !profileService.getProfile(profileId).getBusinessDomains().contains(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()))) {
            throw new AccessDeniedException("User not allowed to access this resource");
        }

        return icaUtil.getRolesForProfile(profileId, userInfo.getRole().getCode());
    }


    private RoleForm toForm(Role role) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RoleForm form = new RoleForm();
        PropertyUtils.copyProperties(form, role);
        return form;
    }

    private Role toDomain(RoleForm form) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Role role = new Role();
        PropertyUtils.copyProperties(role, form);

        return role;
    }

    private void populateForm(Model model, RoleForm form, String mode) {
        model.addAttribute("roleForm", form);
        model.addAttribute("pageMode", mode);

        // Only the list of available roles in the Role screen is ordered by code. In the rest of the screens the selects are ordered by name
        List<Role> roleList = roleService.getAll();
        Collections.sort(roleList, new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                return o1.getCode().compareToIgnoreCase(o2.getCode());
            }
        });
        model.addAttribute("roleList", roleList);
    }
}
