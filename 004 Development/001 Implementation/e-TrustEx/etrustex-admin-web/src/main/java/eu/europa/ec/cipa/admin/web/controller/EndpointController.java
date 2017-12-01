package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.CredentialsForm;
import eu.europa.ec.cipa.admin.web.dto.EndpointForm;
import eu.europa.ec.cipa.admin.web.dto.EndpointSearchForm;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.logs.UserActionsLog;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.utils.*;
import eu.europa.ec.cipa.admin.web.validators.EndpointSearchValidator;
import eu.europa.ec.cipa.admin.web.validators.EndpointValidator;
import eu.europa.ec.cipa.admin.web.validators.ValidationHelper;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.routing.AMQPEndpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.Endpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.JMSEndpoint;
import eu.europa.ec.cipa.etrustex.domain.routing.WSEndpoint;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.*;
import eu.europa.ec.cipa.etrustex.services.dto.CredentialsDTO;
import eu.europa.ec.cipa.etrustex.services.dto.EndpointDTO;
import eu.europa.ec.cipa.etrustex.services.exception.MissingImplementationException;
import eu.europa.ec.cipa.etrustex.services.exception.RecordNotFoundException;
import eu.europa.ec.cipa.etrustex.types.EndpointMessageType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static eu.europa.ec.cipa.admin.web.utils.ConfigurationTypeEnum.JMS;

@Controller
@RequestMapping("/endpoint")
@SessionAttributes({"endpointSearchForm", "endpointForm"})
@PreAuthorize("hasAnyRole('ADM', 'CBO', 'SUP')")
public class EndpointController {
    private static final String PAGE_ENDPOINT = "page.endpoint";
    private static final String PAGE_ENDPOINT_SEARCH = "page.endpointSearch";
    private static final String FRAG_ENDPOINT_INNER = "frag.endpoint.inner";
    private static final String FRAG_ENDPOINT_SEARCH = "frag.endpointSearch.inner";
    private static final String FRAG_ENDPOINT_VIEW = "frag.endpointView.inner";
    private static final String FRAG_ENDPOINT_VIEW_DIALOG = "frag.endpointView.dialog";
    private static final String FRAG_ENDPOINT_ICA = "frag.endpoint.ica";
    private static final String FRAG_ENDPOINT_TRANSACTION = "frag.endpoint.transaction";
    private static final String FRAG_ENDPOINT_PARTY = "frag.endpoint.party";
    private static final String FRAG_ENDPOINT_PROFILES = "frag.endpoint.profiles";
    private static final String FRAG_CREDENTIALS_DIALOG = "frag.endpoint.credentials.dialog";
    private static final String FRAG_CREDENTIALS = "frag.endpoint.credentials";
    private static final String FRAG_PROXY_CREDENTIALS = "frag.endpoint.proxyCredentials";
    private static final String FRAG_CREDENTIALS_RESULTS = "frag.endpoint.credentials.results";

    @Autowired private IEndpointService endpointService;
    @Autowired private MessageSource  messageSource;
    @Autowired private IBusinessDomainService businessDomainService;
    @Autowired private IPartyService partyService;
    @Autowired private IInterchangeAgreementService interchangeAgreementService;
    @Autowired private ITransactionService transactionService;
    @Autowired private IProfileService profileService;
    @Autowired private IMessageRoutingService messageRoutingService;
    @Autowired private ICredentialsService credentialsService;
    @Autowired private EndpointValidator endpointValidator;
    @Autowired private EndpointSearchValidator endpointSearchValidator;
    @Autowired private ValidationHelper validationHelper;
    @Autowired private BusinessDomainUtil businessDomainUtil;
    @Autowired private Referer refererPage;

    /*-----------------------------------------------------------------*/
	/*---------------------------- Binding ----------------------------*/
	/*-----------------------------------------------------------------*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

        binder.registerCustomEditor(Set.class, "profiles",  new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Long profileId = Long.valueOf((String) element);
                return profileId.equals(Long.valueOf(-1)) ? null : profileService.getProfile(profileId);
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

        binder.registerCustomEditor(Profile.class, new PropertyEditorSupport() {
            Object value;

            @Override
            public void setAsText(String text) {
                value = StringUtils.isEmpty(text) || text.equals("-1") ?
                        getTransientProfile(Long.valueOf("-1")) :
                        profileService.getProfile(Long.valueOf(text));
            }

            @Override
            public Object getValue() {
                return value;
            }
        });
    }

    @InitBinder("endpointForm")
    public void initFormBinder(WebDataBinder binder) {
        binder.setValidator(endpointValidator);
    }

    @InitBinder("endpointSearchForm")
    public void initSearchFormBinder(WebDataBinder binder) {
        binder.setValidator(endpointSearchValidator);
    }


    /*-----------------------------------------------------------------*/
	/*------------------------ Model Attributes -----------------------*/
	/*-----------------------------------------------------------------*/
    @ModelAttribute("endpointForm")
    public EndpointForm endpointForm() {
        return new EndpointForm();
    }

    @ModelAttribute("endpointSearchForm")
    public EndpointSearchForm endpointSearchForm() {
        return new EndpointSearchForm();
    }


    @ModelAttribute("endointMessageTypes")
    public EndpointMessageType[] endointMessageTypes() {
        EndpointMessageType[] types = EndpointMessageType.values();
        Arrays.sort(types, new Comparator<EndpointMessageType>() {
            @Override
            public int compare(EndpointMessageType o1, EndpointMessageType o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        return types;
    }

    @ModelAttribute("msgConverterClasses")
    public MsgConverterClassEnum[] msgConverterClasses() {
        MsgConverterClassEnum[] msgConverterClasses = MsgConverterClassEnum.values();
        Arrays.sort(msgConverterClasses, new Comparator<MsgConverterClassEnum>() {
            @Override
            public int compare(MsgConverterClassEnum o1, MsgConverterClassEnum o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return msgConverterClasses;
    }


    /*-----------------------------------------------------------------*/
	/*-------------------- Handler methods ----------------------------*/
	/*-----------------------------------------------------------------*/
    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{configurationType}create", method = RequestMethod.GET)
    public String create(@PathVariable("configurationType") String configurationType, Model model) {
        model.addAttribute("pageMode", "new");
        model.addAttribute("configurationType", configurationType);
        refererPage.setAction("create");

        return PAGE_ENDPOINT;
    }

    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/{configurationType}create/load", method = RequestMethod.POST)
    public String createLoad(@PathVariable("configurationType") ConfigurationTypeEnum configurationType, @ModelAttribute("endpointForm") EndpointForm endpointForm, @ActiveUser SessionUserInformation userInfo, Model model) throws MissingImplementationException {
        endpointForm = new EndpointForm();
        endpointForm.setConfigurationType(configurationType);
        Credentials credentials;
        switch (configurationType) {
            case WS:
                credentials = new WSCredentials();
                break;
            case AMQP:
                credentials = new AMQPCredentials();
                break;
            case JMS:
                credentials = new JMSCredentials();
                break;
            default:
                throw new MissingImplementationException();
        }
        endpointForm.setCredentials(credentials);
        endpointForm.setIsAuthenticationRequired(true);
        endpointForm.setIsActive(true);
        populateForm(model, endpointForm, "new", userInfo);

        return FRAG_ENDPOINT_INNER;
    }


    /*
	 * Important! If arguments are modified, their order must correspond to the following enumeration:
	 * @Pointcut("execution(* eu.europa.ec.cipa.admin.web.controller.*.save(..)) && args(form, .., request)")
	 */
    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @UserActionsLog(entity = Endpoint.class)
    @RequestMapping(value = "/{configurationType}/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult save(@ModelAttribute("endpointForm") @Valid EndpointForm form, BindingResult result, @PathVariable("configurationType") String configurationType,
                           @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) throws NoSuchAlgorithmException {

        if (result.hasErrors()) {
            return new AjaxResult(false, messageSource.getMessage("error.endpoint.save", new Object[] { validationHelper.getSaveErrorsMsg(result) }, LocaleContextHolder.getLocale()), form.getId());
        }

        String message;
        Endpoint endpoint = getEndpointInstance(ConfigurationTypeEnum.valueOf(configurationType));

        if(endpoint != null) {
            // UC130_BR13	When saving the configuration, the leading and trailing spaces of the fields are ignored.
            form.setProxyHost(StringUtils.trim(form.getProxyHost()));
            form.setProviderUrl(StringUtils.trim(form.getProviderUrl()));
            form.setInitalContextFactory(StringUtils.trim(form.getInitalContextFactory()));
            form.setDestinationJndiName(StringUtils.trim(form.getDestinationJndiName()));
            form.setConnectionFactoryJndiName(StringUtils.trim(form.getConnectionFactoryJndiName()));
            form.setMessageConverterClass(StringUtils.trim(form.getMessageConverterClass()));
            form.setWsEndpointURL(StringUtils.trim(form.getWsEndpointURL()));
            form.getCredentials().setUser(StringUtils.trim(form.getCredentials().getUser()));
            form.getProxyCredential().setUser(StringUtils.trim(form.getProxyCredential().getUser()));

            BeanUtils.copyProperties(form, endpoint);

            EntityAccessInfo eai = new EntityAccessInfo();
            String username = userInfo.getUsername();

        /*
         * ETRUSTEX-1407 CIPAdmin shall allow CBO users to configure routing endpoints
         */
            if (userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
                endpoint.setBusinessDomain(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()));
            }

            if (endpoint.getId() == null) { //creation
                eai.setCreationId(username);
                endpoint.setAccessInfo(eai);

                setCredentialsAccessInfo(endpoint.getCredentials(), eai, username);
                setCredentialsAccessInfo(endpoint.getProxyCredential(), eai, username);

                endpointService.create(endpoint, username);
            } else { //modification
                eai.setModificationId(username);
                endpoint.setAccessInfo(eai);

                // Authentication credentials
                setCredentialsAccessInfo(endpoint.getCredentials(), eai, username);

                // Proxy credentials
                setCredentialsAccessInfo(endpoint.getProxyCredential(), eai, username);

                endpointService.update(endpoint, username);
            }
            message = messageSource.getMessage("endpoint.success.save", new Object[] { endpoint.getId() }, LocaleContextHolder.getLocale());

            return new AjaxResult(true, message, endpoint.getId());
        } else {
            return new AjaxResult(false, messageSource.getMessage("error.endpoint.save", new Object[] { validationHelper.getSaveErrorsMsg(result) }, LocaleContextHolder.getLocale()), form.getId());
        }
    }



    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(value="fromView", required=false) boolean fromView, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        if (fromView) { // Run search if coming back from view.
            endpointSearchForm.setSearchOnLoad(true);
        } else {
            endpointSearchForm = new EndpointSearchForm();
            endpointSearchForm.setSearchOnLoad(false);
        }

        populateSearchForm(model, endpointSearchForm, "", userInfo);
        refererPage.setAction("search");

        return PAGE_ENDPOINT_SEARCH;
    }

    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(@ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus) {
        populateSearchForm(model, endpointSearchForm, "", userInfo);
        sessionStatus.setComplete();

        return FRAG_ENDPOINT_SEARCH;
    }



    @UserActionsLog(entity = Endpoint.class)
	@RequestMapping(value = "/search/results", method = RequestMethod.POST)
	public String searchResults(@ModelAttribute("endpointSearchForm") @Valid EndpointSearchForm endpointSearchForm, BindingResult result,
			@ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus, HttpServletRequest request) throws MissingImplementationException {
    	endpointSearchForm.setSearchOnLoad(false);
		sessionStatus.setComplete();

        if (result.hasErrors()){
            populateSearchForm(model, endpointSearchForm, "", userInfo);

            return FRAG_ENDPOINT_SEARCH;
        }

        Endpoint endpoint;
        Credentials credentials;
        if(endpointSearchForm.getConfigurationType() != null) {
            endpoint = getEndpointInstance(endpointSearchForm.getConfigurationType());
            credentials = getCredentialsInstance(endpointSearchForm.getConfigurationType());
        } else {
            endpoint = new EndpointDTO();
            credentials = new CredentialsDTO();
        }


        endpoint.setCredentials(credentials);
        endpoint.setProxyCredential(new ProxyCredentials());
        endpoint.setEndointMessageType(endpointSearchForm.getEndointMessageType_search());
        endpoint.setIsActive(endpointSearchForm.getIsActive_search());
        endpoint.getCredentials().setUser(endpointSearchForm.getUsername());
        endpoint.getProxyCredential().setUser(endpointSearchForm.getProxyUsername());
        if(!endpointSearchForm.getBusinessDomain_search().getId().equals(Long.valueOf(-1))) {
            endpoint.setBusinessDomain(endpointSearchForm.getBusinessDomain_search());
        }
        if(endpointSearchForm.getProfile_search() != null && !endpointSearchForm.getProfile_search().getId().equals(Long.valueOf(-1))) {
            endpoint.setProfile(endpointSearchForm.getProfile_search());
        }
        endpoint.setTansaction(endpointSearchForm.getTansaction_search());
        endpoint.setInterchangeAgreement(endpointSearchForm.getInterchangeAgreement_search());
        endpoint.setParty(endpointSearchForm.getParty_search());

        List<? extends Endpoint> endpointResults = endpointService.findEndpointsByCriteria(endpoint);


        model.addAttribute("endpointResults", endpointResults);
        populateSearchForm(model, endpointSearchForm, "", userInfo);

        return FRAG_ENDPOINT_SEARCH;
    }


    @RequestMapping(value = "/{endpointId}/view", method = RequestMethod.GET)
    public String view(@PathVariable("endpointId") Long endpointId, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model, HttpServletRequest request) {
        populateSearchForm(model, endpointSearchForm, "view", userInfo);
        Endpoint endpoint = endpointService.findById(endpointId);
        String configurationType = getConfigurationTypeAsString(endpoint);
        model.addAttribute("configurationType", configurationType);
        model.addAttribute("endpointId", endpointId);

        return PAGE_ENDPOINT;
    }

    @RequestMapping(value = "/{endpointId}/view/load", method = RequestMethod.POST)
    public String viewLoad(@PathVariable("endpointId") Long endpointId, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm,
                           @RequestParam(value="isViewDialog", required = false) Boolean isViewDialog,
                           @ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, Model model, HttpServletRequest request) {
        Endpoint endpoint = endpointService.findById(endpointId);

        if (endpoint == null) {
            throw new RecordNotFoundException(Endpoint.class.getSimpleName(), endpointId);
        }

        checkAccess(endpoint.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId());


        EndpointForm form = new EndpointForm();
        BeanUtils.copyProperties(endpoint, form);
        form.setConfigurationType(getConfigurationType(endpoint));
        model.addAttribute("configurationType", form.getConfigurationType().toString());
        populateForm(model, form, "view", userInfo);

        if (isViewDialog != null && isViewDialog) {
            return FRAG_ENDPOINT_VIEW_DIALOG;
        }

        populateSearchForm(model, endpointSearchForm, "view", userInfo);

        if(!refererPage.getAction().equals("search")) {
            sessionStatus.setComplete();
        }

        return FRAG_ENDPOINT_VIEW;
    }


    @RequestMapping(value = "/{endpointId}/edit", method = RequestMethod.GET)
    public String edit(@ActiveUser SessionUserInformation userInfo, @PathVariable("endpointId") Long endpointId,
                       @ModelAttribute("endpointForm") EndpointForm endpointForm, Model model) {
        populateForm(model, endpointForm, "edit", userInfo);
        String configurationType = getConfigurationTypeAsString(endpointService.findById(endpointId));
        model.addAttribute("configurationType", configurationType);
        model.addAttribute("endpointId", endpointId);

        return PAGE_ENDPOINT;
    }

    @RequestMapping(value = "/{endpointId}/edit/load", method = RequestMethod.POST)
    public String editLoad(@PathVariable("endpointId") Long endpointId, @ModelAttribute("endpointForm") EndpointForm endpointForm, @ActiveUser SessionUserInformation userInfo, Model model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, MissingImplementationException {
        Endpoint endpoint = endpointService.findById(endpointId);

        checkAccess(endpoint.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId());

        EndpointForm form = new EndpointForm();

        // To prevent "Could not instantiate property" error on jsp compilation (TODO batrian: delete this)

		/* batrian: Credentials properties are referred to in the jspf pages.
		 * Thus we need to instantiate Credentials with the correct type
		 * (depending on the endpoint configuration type).
		 * This will save us from having null.id or similar references
		 * when the credentials fragments are loaded. */

        // String[] ignoredProps = new String[2]; // TODO batrian: delete this
        if(endpoint.getCredentials() == null) {
            //ignoredProps[0] = "credentials";
            // TODO batrian: delete above row
            Credentials credentials;
            switch (getConfigurationType(endpoint)) {
                case JMS:
                    credentials = new JMSCredentials();
                    break;
                case WS:
                    credentials = new WSCredentials();
                    break;
                case AMQP:
                    credentials = new AMQPCredentials();
                    break;
                default:
                    throw new MissingImplementationException();
            }
            endpoint.setCredentials(credentials);
        }
        if(endpoint.getProxyCredential() == null) {
            //ignoredProps[1] = "proxyCredential";
            // TODO batrian: delete above row
            endpoint.setProxyCredential(new ProxyCredentials());
        }

//		BeanUtils.copyProperties(endpoint, form, ignoredProps);
        // TODO batrian: delete above row
        BeanUtils.copyProperties(endpoint, form);
        form.setConfigurationType(getConfigurationType(endpoint));
        populateForm(model, form, "edit", userInfo);

        if(endpoint.getBusinessDomain() != null && endpoint.getBusinessDomain().getId() != null) {
            model.addAttribute("profiles", getProfilesForBD(endpoint.getBusinessDomain().getId()));
        }

        return FRAG_ENDPOINT_INNER;
    }


    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @UserActionsLog(entity = Endpoint.class)
    @RequestMapping(value = "/{endpointId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult delete(@PathVariable("endpointId") Long endpointId, @ActiveUser SessionUserInformation userInfo, HttpServletRequest request) {
        Endpoint endpoint = endpointService.findById(endpointId);

        if (endpoint == null) {
            return new AjaxResult(false, messageSource.getMessage("common.error.recordNotFound", null, LocaleContextHolder.getLocale()), endpointId);
        }

        checkAccess(endpoint.getBusinessDomain().getId(), userInfo.getBusinessDomain().getId());

        /*
         * UC130_BR24	If the user tries to delete a routing endpoint (be it JMS, WS or AMQP), and this routing endpoint is referred to in the Message_Routing table, then an error message will be shown to the user
         */
        if(CollectionUtils.isNotEmpty(messageRoutingService.findByEndpointId(endpointId))) {
            return new AjaxResult(false, messageSource.getMessage("error.endpoint.messageRouting", null, LocaleContextHolder.getLocale()), endpointId);
        }

        endpointService.delete(endpoint);

        return new AjaxResult(true, messageSource.getMessage("common.success.delete", null, LocaleContextHolder.getLocale()), endpointId);
    }




    @RequestMapping(value = "/add/party/{id}", method = RequestMethod.GET)
    public String addParty(@PathVariable("id") Long partyId, @ModelAttribute("endpointForm") EndpointForm endpointForm, @ActiveUser SessionUserInformation userInfo, Model model){
        Party party = partyService.getParty(partyId);
        endpointForm.setParty(party);
        populateForm(model, endpointForm, endpointForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_ENDPOINT_PARTY;
    }

    @RequestMapping(value = "/add/ica/{id}", method = RequestMethod.GET)
    public String addIca(@PathVariable("id") Long icaId, @ModelAttribute("endpointForm") EndpointForm endpointForm, @ActiveUser SessionUserInformation userInfo, Model model){
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);
        endpointForm.setInterchangeAgreement(ica);
        populateForm(model, endpointForm, endpointForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_ENDPOINT_ICA;
    }

    @RequestMapping(value = "/add/transaction/{id}", method = RequestMethod.GET)
    public String addTransaction(@PathVariable("id") Long transactionId, @ModelAttribute("endpointForm") EndpointForm endpointForm, @ActiveUser SessionUserInformation userInfo, Model model){
        Transaction transaction = transactionService.getTransaction(transactionId);
        endpointForm.setTansaction(transaction);
        populateForm(model, endpointForm, endpointForm.getId() == null ? "new" : "edit", userInfo);

        return FRAG_ENDPOINT_TRANSACTION;
    }

    @RequestMapping(value = "/search/add/party/{id}", method = RequestMethod.GET)
    public String searchAddParty(@PathVariable("id") Long partyId, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model){
        Party party = partyService.getParty(partyId);
        endpointSearchForm.setParty_search(party);
        populateSearchForm(model, endpointSearchForm, "", userInfo);

        return FRAG_ENDPOINT_PARTY;
    }

    @RequestMapping(value = "/search/add/ica/{id}", method = RequestMethod.GET)
    public String searchAddIca(@PathVariable("id") Long icaId, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model){
        InterchangeAgreement ica = interchangeAgreementService.getInterchangeArgreement(icaId);
        endpointSearchForm.setInterchangeAgreement_search(ica);
        populateSearchForm(model, endpointSearchForm, "", userInfo);

        return FRAG_ENDPOINT_ICA;
    }

    @RequestMapping(value = "/search/add/transaction/{id}", method = RequestMethod.GET)
    public String searchAddTransaction(@PathVariable("id") Long transactionId, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model){
        Transaction transaction = transactionService.getTransaction(transactionId);
        endpointSearchForm.setTansaction_search(transaction);
        populateSearchForm(model, endpointSearchForm, "", userInfo);

        return FRAG_ENDPOINT_TRANSACTION;
    }

    @RequestMapping(value = "/profiles", method = RequestMethod.POST)
    public String loadProfiles(@RequestParam Long bdId, @RequestParam String pageMode, Model model) {
        model.addAttribute("profiles", getProfilesForBD(bdId));
        model.addAttribute("pageMode", pageMode);

        return FRAG_ENDPOINT_PROFILES;
    }

    @RequestMapping(value = "/search/profiles", method = RequestMethod.POST)
    public String searchLoadProfiles(@RequestParam("bdId") Long bdId, @ModelAttribute("endpointSearchForm") EndpointSearchForm endpointSearchForm, @ActiveUser SessionUserInformation userInfo, Model model) {
        endpointSearchForm.setBusinessDomain_search(businessDomainService.getBusinessDomain(bdId));
        populateSearchForm(model, endpointSearchForm, "", userInfo);

        return FRAG_ENDPOINT_PROFILES;
    }

    @RequestMapping(value = "/credentials/dialog", method = RequestMethod.POST)
    public String credentialsDialog(@RequestParam(value = "isSearch", required = false) boolean isSearch, @RequestParam boolean isProxyCredentials, @RequestParam(value = "credentialsId", required = false) Long credentialsId,
                                    @ActiveUser SessionUserInformation userInfo, @ModelAttribute("endpointForm") EndpointForm endpointForm, Model model, SessionStatus sessionStatus) {

        CredentialsForm credentialsForm = new CredentialsForm();

        if(credentialsId != null) {
            Credentials credentials = credentialsService.get(credentialsId);
            credentialsForm.setUsername(credentials.getUser());
            credentialsForm.setPassword(credentials.getPassword());
            credentialsForm.setPassword2(credentials.getPassword());
        }

        model.addAttribute("isSearch", isSearch);
        model.addAttribute("credentialsForm", credentialsForm);
        model.addAttribute("isProxyCredentials", isProxyCredentials);

        if(!isProxyCredentials) {
            model.addAttribute("credentialsType", endpointForm.getConfigurationType().name());
        }

        return FRAG_CREDENTIALS_DIALOG;
    }

    @RequestMapping(value = "/credentials/load", method = RequestMethod.GET)
    public String credentialsLoad(@RequestParam boolean isProxyCredentials, @RequestParam(value = "credentialsId", required = false) Long credentialsId,
                                  @ModelAttribute("endpointForm") EndpointForm endpointForm, @ModelAttribute("credentialsForm") CredentialsForm credentialsForm,
                                  @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus) {
        String user, password;

        if(credentialsId != null) {
            Credentials credentials = credentialsService.get(credentialsId);
            user = credentials.getUser();
            password = credentials.getPassword();
        } else {
            user = credentialsForm.getUsername();
            password = credentialsForm.getPassword();
        }

        if(isProxyCredentials) {
            endpointForm.getProxyCredential().setId(credentialsId);
            endpointForm.getProxyCredential().setUser(user);
            endpointForm.getProxyCredential().setPassword(password);
        } else {
            endpointForm.getCredentials().setId(credentialsId);
            endpointForm.getCredentials().setUser(user);
            endpointForm.getCredentials().setPassword(password);
        }

        populateForm(model, endpointForm, "", userInfo);

        return isProxyCredentials ? FRAG_PROXY_CREDENTIALS : FRAG_CREDENTIALS;
    }

    @RequestMapping(value = "/credentials/results", method = RequestMethod.POST)
    public String credentialsResults(@ModelAttribute("credentialsForm") CredentialsForm credentialsForm,
                                     @RequestParam boolean isProxyCredentials, @RequestParam("t") ConfigurationTypeEnum credentialsType,
                                @ActiveUser SessionUserInformation userInfo, Model model) throws MissingImplementationException {

        Credentials credentials;
        Class endpointClass;

        if (isProxyCredentials) {
            credentials = new ProxyCredentials();
            endpointClass = Endpoint.class;
        } else {
            switch (credentialsType) {
                case WS:
                    credentials = new WSCredentials();
                    endpointClass = WSEndpoint.class;
                    break;
                case AMQP:
                    credentials = new AMQPCredentials();
                    endpointClass = AMQPEndpoint.class;
                    break;
                case JMS:
                    credentials = new JMSCredentials();
                    endpointClass = JMSEndpoint.class;
                    break;
                default:
                    throw new MissingImplementationException();
            }
        }

        credentials.setUser(credentialsForm.getUsername());

        List<? extends Endpoint> endpointResults = endpointService.findByCredentialsTypeAndUsernameLike(credentials, userInfo.getBusinessDomain().getId(), endpointClass);


        /* TODO Temporary fix for ETRUSTEX-2318. Until change request for normalizing Endpoints and credentials is approved */
        List<Endpoint> endpointResultsTmp = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(endpointResults) && !isProxyCredentials) {
            Map<String, Endpoint> endpointMap = new HashMap<>();

            switch (credentialsType) {
                case JMS:
                    for (Iterator<JMSEndpoint> it = (Iterator<JMSEndpoint>) endpointResults.iterator(); it.hasNext();) {
                        JMSEndpoint endpoint = it.next();
                        endpointMap.put(endpoint.getProviderUrl() + "-" + endpoint.getDestinationJndiName(), endpoint);
                    }

                    break;
                case WS:
                    for (Iterator<WSEndpoint> it = (Iterator<WSEndpoint>) endpointResults.iterator(); it.hasNext();) {
                        WSEndpoint endpoint = it.next();
                        endpointMap.put(endpoint.getWsEndpointURL(), endpoint);
                    }

                    break;
                case AMQP:
                    for (Iterator<AMQPEndpoint> it = (Iterator<AMQPEndpoint>) endpointResults.iterator(); it.hasNext();) {
                        AMQPEndpoint endpoint = it.next();
                        endpointMap.put(endpoint.getProviderUrl(), endpoint);
                    }

                    break;
                default:
            }

            endpointResultsTmp.addAll(endpointMap.values()) ;
        }
        /* TODO End temporary fix */


        model.addAttribute("credentialsType", credentialsType);
        model.addAttribute("endpointResults", isProxyCredentials ? endpointResults : endpointResultsTmp);

        return FRAG_CREDENTIALS_RESULTS;
    }


    @RequestMapping(value = "/credentials/reset", method = RequestMethod.GET)
	public String credentialsReset(@RequestParam boolean isProxyCredentials,  @ModelAttribute("endpointForm") EndpointForm endpointForm, @ModelAttribute("credentialsForm") CredentialsForm credentialsForm,
								  @ActiveUser SessionUserInformation userInfo, Model model, SessionStatus sessionStatus) throws MissingImplementationException {
        Credentials credentials;
        if(isProxyCredentials) {
            credentials = new ProxyCredentials();
            credentials.setPasswordEncrypted(false);
            credentials.setSignatureRequired(false);
            endpointForm.setProxyCredential(credentials);
        } else {
		    credentials = getCredentialsInstance(endpointForm.getConfigurationType());
            credentials.setPasswordEncrypted(false);
            credentials.setSignatureRequired(false);
            endpointForm.setCredentials(credentials);
        }

        populateForm(model, endpointForm, "", userInfo);

        return isProxyCredentials ? FRAG_PROXY_CREDENTIALS : FRAG_CREDENTIALS;
    }


    private void checkAccess(Long endpointBDId, Long userBDId) {
        // CBO shall not be able to consult, edit or delete endpoints outside his/her business domain
        if (!Objects.equals(endpointBDId, userBDId)) {
            throw new AccessDeniedException("User not allowed to access this resource");
        }
    }

    private void populateForm(Model model, EndpointForm endpointForm,  String pageMode, SessionUserInformation userInfo) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("endpointForm", endpointForm);

        if(userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            model.addAttribute("profiles", getProfilesForBD(userInfo.getBusinessDomain().getId()));
        }

        model.addAttribute("businessDomains", businessDomainUtil.getUserBusinessDomains(true));
    }

    private void populateSearchForm(Model model, EndpointSearchForm endpointSearchForm,  String pageMode, SessionUserInformation userInfo) {
        model.addAttribute("pageMode", pageMode);
        model.addAttribute("endpointSearchForm", endpointSearchForm);

        model.addAttribute("businessDomains", businessDomainUtil.getUserBusinessDomains(true));

        if(userInfo.getRole().getCode().equals(UserRoleEnum.CBO.name())) {
            model.addAttribute("profiles", getProfilesForBD(userInfo.getBusinessDomain().getId()));
        } else if (endpointSearchForm.getBusinessDomain_search() != null && !endpointSearchForm.getBusinessDomain_search().getId().equals(Long.valueOf(-1))) {
            model.addAttribute("profiles", getProfilesForBD(endpointSearchForm.getBusinessDomain_search().getId()));
        }

        model.addAttribute("configurationTypes", ConfigurationTypeEnum.values());
    }

    private List<Profile> getProfilesForBD(Long bdId) {
        if(bdId == null || bdId.equals(Long.valueOf(-1))) {
            return  null;
        }

        List<Profile> profiles = new ArrayList<>(businessDomainService.getBusinessDomain(bdId).getProfiles());
        Collections.sort(profiles, new Comparator<Profile>() {
            @Override
            public int compare(Profile o1, Profile o2) {
                return o1.getName().trim().compareToIgnoreCase(o2.getName().trim());
            }
        });

        profiles.add(0, getTransientProfile(Long.valueOf("-1")));

        return profiles;
    }

//    private EndpointForm toForm(Endpoint endpoint) {
//    	EndpointForm endpointForm = new EndpointForm();
//    	endpointForm.setId(endpoint.getId());
//    	endpointForm.setParty(endpoint.getParty());
//    	endpointForm.setInterchangeAgreement(endpoint.getInterchangeAgreement());
////    	endpointForm.setEndpointType(endpoint.getItemType());
//    	endpointForm.setProfile(endpoint.getProfile());
//    	endpointForm.setTansaction(endpoint.getTansaction());
////    	endpointForm.setValue(endpoint.getValue());
//
//    	return endpointForm;
//    }



    private Profile getTransientProfile(Long id) {
        Profile p = new Profile();
        p.setId(id);
        p.setName(messageSource.getMessage("choose.please", null, LocaleContextHolder.getLocale()));

        return p;
    }

    private void setCredentialsAccessInfo(Credentials credentials, EntityAccessInfo eai, String username) {
        if(credentials != null && credentials.getId() == null
                && StringUtils.isNotEmpty(credentials.getUser())
                && StringUtils.isNotEmpty(credentials.getPassword())) {
            // New Credentials added
            credentials.setAccessInfo(eai);
        } else if(credentials != null && credentials.getId() != null) {
            Credentials oldCredentials = credentialsService.get(credentials.getId());
            boolean credentialsChanged = !StringUtils.equals(oldCredentials.getUser(), credentials.getUser()) || !StringUtils.equals(oldCredentials.getPassword(), credentials.getPassword());
            if(credentialsChanged) {
                // Old Credentials updated
                credentials.setAccessInfo(eai);
            }
        }
    }

    private Endpoint getEndpointInstance(ConfigurationTypeEnum configurationType) {
        switch (configurationType) {
            case JMS:
                return new JMSEndpoint();
            case WS:
                return new WSEndpoint();
            case AMQP:
                return new AMQPEndpoint();
            default:
                return null;
        }
    }

    private Credentials getCredentialsInstance(ConfigurationTypeEnum configurationType) throws MissingImplementationException {
        switch (configurationType) {
            case WS:
                return new WSCredentials();
            case AMQP:
                return new AMQPCredentials();
            case JMS:
                return new JMSCredentials();
            default:
                throw new MissingImplementationException();
        }
    }

    private ConfigurationTypeEnum getConfigurationType(Endpoint endpoint) {
        if(endpoint instanceof JMSEndpoint) {
            return JMS;
        } else if(endpoint instanceof WSEndpoint) {
            return ConfigurationTypeEnum.WS;
        } else if(endpoint instanceof AMQPEndpoint) {
            return ConfigurationTypeEnum.AMQP;
        } else {
            return null;
        }
    }

    private String getConfigurationTypeAsString(Endpoint endpoint) {
        if(endpoint instanceof JMSEndpoint) {
            return JMS.toString();
        } else if(endpoint instanceof WSEndpoint) {
            return ConfigurationTypeEnum.WS.toString();
        } else if(endpoint instanceof AMQPEndpoint) {
            return ConfigurationTypeEnum.AMQP.toString();
        } else {
            return null;
        }
    }
}