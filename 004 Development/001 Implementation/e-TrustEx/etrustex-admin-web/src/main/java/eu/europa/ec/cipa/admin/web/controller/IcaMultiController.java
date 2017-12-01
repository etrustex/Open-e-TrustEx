package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.*;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.services.IICANotificationService;
import eu.europa.ec.cipa.admin.web.utils.BusinessDomainUtil;
import eu.europa.ec.cipa.admin.web.utils.FormUtil;
import eu.europa.ec.cipa.admin.web.utils.ICAUtil;
import eu.europa.ec.cipa.admin.web.utils.Referer;
import eu.europa.ec.cipa.admin.web.validators.ICACompositeValidator;
import eu.europa.ec.cipa.admin.web.validators.InterchangeAgreementSearchValidator;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.services.*;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;
import eu.europa.ec.cipa.etrustex.services.dto.PartyListItemDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ica/multi")
@SessionAttributes({"interchangeAgreement"})
@PreAuthorize("hasAnyRole('ADM', 'CBO')")
public class IcaMultiController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final String PAGE_ICA = "page.interchangeAgreement.multi";
	private static final String FRAG_ICA_INNER = "page.ica.inner";
    private static final String FRAG_ICA_MULTIPLE_SECOND_PARTIES = "page.ica.multiple.second.parties";
    private static final String FRAG_ICA_PARTY = "fragment.ica.party";

	@Autowired private ILogService logService;
	@Autowired private IPartyService partyService;
	@Autowired private IProfileService profileService;
	@Autowired private IRoleService roleService;
	@Autowired private IBusinessDomainService businessDomainService;
	@Autowired private IInterchangeAgreementService interchangeAgreementService;
	@Autowired private IICANotificationService icaNotificationService;
	@Autowired private IUserService userService;
	@Autowired private IPartyRoleService partyRoleService;
	@Autowired private MessageSource  messageSource;
	@Autowired private InterchangeAgreementSearchValidator icaSearchValidator; 
	@Autowired private ICACompositeValidator icaValidator;
    @Autowired private BusinessDomainUtil businessDomainUtil;
    @Autowired private ICAUtil icaUtil;
	@Autowired private Referer refererPage;
	
	
	/*-----------------------------------------------------------------*/
	/*---------------------------- Bindings ---------------------------*/
	/*-----------------------------------------------------------------*/
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/*
			To prevent java.lang.IndexOutOfBoundsException: Index: 256, Size: 256.
			autoGrowCollectionLimit property in WebDataBinder is 256 by default.
			The problem is that partyListItems is a property of InterchangeAgreementMultiForm form.
			TODO evaluate handling the list of second parties as model attribute and not a property.
		 */
		binder.setAutoGrowCollectionLimit(2000);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@InitBinder("interchangeAgreement")
    public void initFormBinder(WebDataBinder binder) {  
        binder.setValidator(icaValidator);
    }

    /*-----------------------------------------------------------------*/
	/*------------------------ Model Attributes -----------------------*/
	/*-----------------------------------------------------------------*/
	// Populates interchangeAgreement if it is null
	@ModelAttribute("interchangeAgreement")
    public InterchangeAgreementMultiForm getInterchangeAgreement() {
		return new InterchangeAgreementMultiForm();
    }
	

	/*-----------------------------------------------------------------*/
	/*------------------------ Exception Handlers ---------------------*/
	/*-----------------------------------------------------------------*/
	@ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException iae) {
        log.error(iae.getMessage(), iae);
        req.getSession().setAttribute("interchangeAgreement", new InterchangeAgreementMultiForm());
    }
	
	
	
	/*-----------------------------------------------------------------*/
	/*-------------------- Handler methods ----------------------------*/
	/*-----------------------------------------------------------------*/
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(@RequestParam(value="partyId", required=false) Long partyId, @ModelAttribute("interchangeAgreement") InterchangeAgreementMultiForm interchangeAgreement, Model model, HttpSession session) {
		if(partyId != null) {
			interchangeAgreement.setFirstParty(new PartyForm(partyService.getParty(partyId)));
			refererPage.setController(null); // At this point it's value is IcaMultiController. We clear it, otherwise remains in session
		}
		
		if(interchangeAgreement.getIcaValidityStartDate() == null) {
			interchangeAgreement.setIcaValidityStartDate(new Date());
		}
		
		model.addAttribute("pageMode", "new");
		model.addAttribute("isMultiple", true);
		model.addAttribute("interchangeAgreement", interchangeAgreement);
		refererPage.setAction("create");
		
		return PAGE_ICA;
	}

	@RequestMapping(value = "/create/load", method = RequestMethod.POST)
	public String createLoad(@ActiveUser SessionUserInformation userInfo, @ModelAttribute("interchangeAgreement") InterchangeAgreementMultiForm interchangeAgreement, 
			Model model, SessionStatus sessionStatus) {
//		interchangeAgreement.setPartyListItems(getPartiesFilteredByRoleForUser(null, userInfo));
		
		if (interchangeAgreement != null && interchangeAgreement.getProfile() != null){
            model.addAttribute("roleList", icaUtil.getRolesForProfile(interchangeAgreement.getProfile().getId(), userInfo.getRole().getCode()));
    	}
		
		populateForm(model, interchangeAgreement, "new", userInfo);
		
		sessionStatus.setComplete();
		
		return FRAG_ICA_INNER;
	}
	
	
	/*
	 * Saving logs is handled here. Not by UserActionsLogAspect.java
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("interchangeAgreement") InterchangeAgreementMultiForm interchangeAgreement, BindingResult result, 
			@ActiveUser SessionUserInformation userInfo, SessionStatus sessionStatus, Model model, HttpServletRequest request){
    	if (interchangeAgreement != null && interchangeAgreement.getProfile() != null && interchangeAgreement.getProfile().getId() != null) {
            model.addAttribute("roleList", icaUtil.getRolesForProfile(interchangeAgreement.getProfile().getId(), userInfo.getRole().getCode()));
    	}
    	
		if (result.hasErrors()) {
			log.debug("Binding errors");
			
			StringBuilder message = new StringBuilder("<br/>There are validation errors: <br/><ul>");
			
			for(ObjectError e : result.getAllErrors()) {
				String resolvedMsg = messageSource.getMessage(e.getCode(), e.getArguments(), LocaleContextHolder.getLocale());
				log.debug(e.getObjectName() + " - " + resolvedMsg + " - " + e.toString());
				message.append("<li type='disc'>").append(resolvedMsg).append("</li>");
			}
			message.append("</ul>");
			
			model.addAttribute("successMsg", "error" + message);
			populateForm(model, interchangeAgreement, "new", userInfo);

            if (result.getFieldError("partyListItems") != null) {
                model.addAttribute("secondParties", getSecondParties(interchangeAgreement.getProfile().getId(),
                        interchangeAgreement.getFirstParty().getId(),
                        interchangeAgreement.getFirstParty().getRoleId(),
                        interchangeAgreement.getSecondPartyRoleId(),
                        userInfo.getBusinessDomain().getId()));
            }

			sessionStatus.setComplete();			
			
			return FRAG_ICA_INNER;
		}
		
		log.debug("validateSaveAndRefreshMultipleCreationBusinessRules: validation ended with success" );

		Map<String, List<Long>> partyIdsSuccessAndError = createMultipleInterchangeAgreements(interchangeAgreement, userInfo, request);

        int successful = partyIdsSuccessAndError.get("success").size();
        int inError = partyIdsSuccessAndError.get("inError").size();
        int total = successful + inError;

		if( inError > 0 ) {
            model.addAttribute("successMsg", "warning" + messageSource.getMessage("ica.multiple.error.save", new String[]{"" + total, "" + successful, "" + inError}, LocaleContextHolder.getLocale()));
            model.addAttribute("icasInErrorCaptionMsg", messageSource.getMessage("ica.multiple.error.caption", new String[]{"" + total, "" + inError}, LocaleContextHolder.getLocale()));
        } else {
            model.addAttribute("successMsg", "success" + messageSource.getMessage("ica.multiple.success.save", new String[]{"" + total, "" + successful}, LocaleContextHolder.getLocale()));
        }

        if( successful > 0) {
            model.addAttribute("icasInSuccessCaptionMsg", messageSource.getMessage("ica.multiple.success.save", new String[]{"" + total, "" + successful}, LocaleContextHolder.getLocale()));
        }

        List<PartyListItem> secondParties = getSecondParties(interchangeAgreement.getProfile().getId(),
                interchangeAgreement.getFirstParty().getId(),
                interchangeAgreement.getFirstParty().getRoleId(),
                interchangeAgreement.getSecondPartyRoleId(),
                null);

        // Merge second parties and ICA parties
        secondParties.removeAll(interchangeAgreement.getPartyListItems());
        secondParties.addAll(interchangeAgreement.getPartyListItems());

        Collections.sort(secondParties, new Comparator<PartyListItem>() {
            @Override
            public int compare(PartyListItem o1, PartyListItem o2) {
                return o1.getPartyName().compareToIgnoreCase(o2.getPartyName());
            }
        });

        model.addAttribute("secondParties", secondParties);

        interchangeAgreement.setPartyListItems(null);
        populateForm(model, interchangeAgreement, "new", userInfo);

		sessionStatus.setComplete();
		
		return FRAG_ICA_INNER;
	}
	
	
	@RequestMapping(value = "/refreshSecondParties", method = RequestMethod.POST)
	public String refreshSecondParties(@ActiveUser SessionUserInformation userInfo,
                                       @RequestParam Long profileId,
                                       @RequestParam Long firstPartyId,
                                       @RequestParam Long firstPartyRoleId,
                                       @RequestParam Long secondPartyRoleId, Model model) {


        if( FormUtil.convertDefaultOptionToNull(profileId) != null
                && firstPartyId != null
                && FormUtil.convertDefaultOptionToNull(firstPartyRoleId) != null
                && FormUtil.convertDefaultOptionToNull(secondPartyRoleId) != null ) {
            model.addAttribute("secondParties", getSecondParties(profileId, firstPartyId, firstPartyRoleId, secondPartyRoleId, userInfo.getBusinessDomain().getId()));
        }

    	return FRAG_ICA_MULTIPLE_SECOND_PARTIES;
	}
	
	@RequestMapping(value = "/party", method = RequestMethod.POST)
	@ResponseBody
	public String party(@ModelAttribute("interchangeAgreement") InterchangeAgreementMultiForm interchangeAgreement, Model model) {
		refererPage.setController(this.getClass().getSimpleName());
    	return "success";
	}

    @RequestMapping(value = "/add/party/{id}", method = RequestMethod.GET)
    public String addParty(@PathVariable("id") Long partyId, @RequestParam String partyType, @ModelAttribute("interchangeAgreement") InterchangeAgreementForm interchangeAgreement,
                           @ActiveUser SessionUserInformation userInfo, Model model, HttpServletRequest request){
        Party party = partyService.getParty(partyId);
        PartyForm partyForm = new PartyForm(party);

        switch(partyType) {
            case "first":
                interchangeAgreement.setFirstParty(partyForm);
                break;
            case "first3rd":
                interchangeAgreement.setFirst3rdParty(partyForm);
                break;
        }

        model.addAttribute("interchangeAgreement", interchangeAgreement);
        model.addAttribute("pageMode", interchangeAgreement.getId() == null ? "new" : "edit");
        request.setAttribute("partyType", partyType);

        return FRAG_ICA_PARTY;
    }
	

	/*-----------------------------------------------------------------*/
	/*------------------------ Utility methods ------------------------*/
	/*-----------------------------------------------------------------*/
    private List<PartyListItem> getSecondParties(Long profileId, Long firstPartyId, Long firstPartyRoleId, Long secondPartyRoleId, Long businessDomainId) {
        List<PartyListItem> secondParties = new ArrayList<PartyListItem>();
        List<PartyListItemDTO> parties = partyRoleService.getPartiesByRoleAndDomain(secondPartyRoleId,
                                                    businessDomainId != null ? businessDomainId : partyService.getParty(firstPartyId).getBusinessDomain().getId());
        List<Long> partiesAlreadyInAgreement = interchangeAgreementService.getPartiesInInterchangeAgreementFor(profileId, firstPartyRoleId, firstPartyId, secondPartyRoleId);
        boolean existsPartiesInAgreement = CollectionUtils.isNotEmpty(partiesAlreadyInAgreement);

        for (PartyListItemDTO party : parties) {
            if( !party.getId().equals(firstPartyId) ){ // Do not add first party
                PartyListItem partyItem = new PartyListItem(partyService.getParty(party.getId()), partyService.getAllThirdPartiesFor(party.getId()));

                if( existsPartiesInAgreement && partiesAlreadyInAgreement.contains(partyItem.getId()) ){
                    partyItem.setExists(true);
                }

                secondParties.add(partyItem);
            }
        }

        return secondParties;
    }
	
	private Map<String, List<Long>> createMultipleInterchangeAgreements(InterchangeAgreementMultiForm interchangeAgreement, SessionUserInformation userInfo, HttpServletRequest request){
		Map<String, List<Long>> result = new HashMap<>();
		result.put("success", new ArrayList<Long>());
		result.put("inError", new ArrayList<Long>());
		
		Date currentDate = Calendar.getInstance().getTime();
		String currentUser = userInfo.getUsername();
		
		EntityAccessInfo eai = new EntityAccessInfo();
		eai.setCreationId(currentUser);
		
		InterchangeAgreementForm form = new InterchangeAgreementForm();
		form.setFirstParty(interchangeAgreement.getFirstParty());
		form.setProfile(interchangeAgreement.getProfile());
		form.setIcaValidityStartDate(interchangeAgreement.getIcaValidityStartDate());
		
		form.setCiaLevels(interchangeAgreement.getCiaLevels());
		form.setIcaValidityStartDate(interchangeAgreement.getIcaValidityStartDate());

		PartyAgreement firstDelegation = null;
        if (interchangeAgreement.getFirst3rdParty() != null && FormUtil.convertDefaultOptionToNull(interchangeAgreement.getFirst3rdParty().getId()) != null){
			log.debug("First party delegates to third party.");
			firstDelegation = new PartyAgreement();
			firstDelegation.setAccessInfo(eai);
			firstDelegation.getAccessInfo().setCreationDate(currentDate);
			firstDelegation.getAccessInfo().setCreationId(currentUser);
			firstDelegation.setAuthorizingParty(partyService.getParty(interchangeAgreement.getFirstParty().getId()));
            firstDelegation.setDelegateParty(partyService.getParty(interchangeAgreement.getFirst3rdParty().getId()));
		}
		
		if (CollectionUtils.isNotEmpty(interchangeAgreement.getPartyListItems())) {
			for (PartyListItem party : interchangeAgreement.getPartyListItems()) {
                if (party.getIsValid() != null && !party.getIsValid()){
                    result.get("inError").add(party.getId());

                    saveLog(userInfo, request, null, LogDTO.LOG_TYPE.ERROR,
                            "Business error saving " + InterchangeAgreement.class + ": " + party.getMessage(), null);
                } else {
                    try{
                        PartyForm secondPartyForm = new PartyForm();
                        secondPartyForm.setId(party.getId());
                        secondPartyForm.setBusinessDomainId(party.getBusinessDomainId());
                        secondPartyForm.setRoleId(interchangeAgreement.getSecondPartyRoleId());
                        secondPartyForm.setDelegatedParties(party.getThirdParties());
                        form.setSecondParty(secondPartyForm);
                        InterchangeAgreement ica = toDomain(form);
                        ica.setAccessInfo(eai);
//                        InterchangeAgreement createdIca = interchangeAgreementService.createInterchangeAgreement(ica, firstDelegation, null);
                        InterchangeAgreement createdIca = icaNotificationService.createAndSendNotification(ica, firstDelegation, null);
                        result.get("success").add(party.getId());
                        party.setMessage(messageSource.getMessage("multiple.ica.success.save", new Object[] { createdIca.getId() }, LocaleContextHolder.getLocale()));
                        party.setStatusColor("green");
                        party.setExists(true);

                        saveLog(userInfo, request, createdIca.getId(), LogDTO.LOG_TYPE.SUCCESS,
                                "Saved " + InterchangeAgreement.class + " with id: " + createdIca.getId(), null);
                    } catch(Exception e) {
                        saveLog(userInfo, request, null, LogDTO.LOG_TYPE.ERROR,
                                "Exception saving" + InterchangeAgreement.class + ": " + e.getClass(),
                                ExceptionUtils.getStackTrace(e));
                    }
                }
			}
		}

		return result;
	}
	
	
	private void saveLog(SessionUserInformation userInfo, HttpServletRequest request, Long entityId, LogDTO.LOG_TYPE logType, String description, String largeValue) {
		LogDTO logDTO = new LogDTO.LogDTOBuilder(logType, LogDTO.LOG_OPERATION.CREATE, this.getClass().getName())
			.businessDomain(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()))
			.module("CIPADMIN")
			.username(userInfo.getUsername())
			.userRole(userInfo.getRole().getCode())
			.urlContext(request.getRequestURI())
			.authIpAddress(request.getRemoteAddr())
			.correlationId(request.getSession().getId())
			.entity(InterchangeAgreement.class.getName())
			.entityId(entityId)
			.description(description)
			.largeValue(largeValue)
			.build();
		
		logService.saveLog(logDTO);
		
		printLog(logDTO);
	}
	
	private void printLog(LogDTO logDTO) {
		log.info("Log saved.");
		log.info("	businessDomain: " + logDTO.getBusinessDomain());
		log.info("	correlationId: " + logDTO.getCorrelationId());
		log.info("	urlContext: " + logDTO.getUrlContext());
		log.info("	operation: " + logDTO.getOperation());
		log.info("	username: " + logDTO.getUsername());
		log.info("	userRole: " + logDTO.getUserRole());
		log.info("	entity: " + logDTO.getEntity());
		log.info("	entityId: " + logDTO.getEntityId());
		log.info("	ipAddress: " + logDTO.getAuthIpAddress());
		log.info("	logType: " + logDTO.getLogType());
		log.info("	description: " + logDTO.getDescription());
		log.info("	largValue: " + logDTO.getLargeValue());
	}

	private InterchangeAgreement toDomain(InterchangeAgreementForm form) {
		InterchangeAgreement ica;
		boolean isUpdate = false;
		if (FormUtil.convertDefaultOptionToNull(form.getId()) == null) {
			ica = new InterchangeAgreement();
		} else {
			isUpdate = true;
			ica = interchangeAgreementService.getInterchangeArgreement(form
					.getId());
		}

		log.debug("toDomain - form.getId() = " + form.getId());

		ica.setProfile(profileService.getProfile(form.getProfile() == null ? null : form.getProfile().getId()));
		log.debug(ica.getProfile().toString());

		ica.setValidityStartDate(form.getIcaValidityStartDate());
		ica.setPartyRoles(new HashSet<PartyRole>(2));
		PartyRole partyRole = new PartyRole();

		partyRole.setParty(partyService.getParty(form.getFirstParty()
				.getId()));
		log.debug(partyRole.getParty().toString());

		partyRole
				.setRole(roleService.getRole(form.getFirstParty().getRoleId()));
		log.debug(partyRole.getRole().toString());

		ica.getPartyRoles().add(partyRole); 

		partyRole = new PartyRole();

		partyRole.setParty(partyService.getParty(form.getSecondParty()
				.getId()));
		log.debug(partyRole.getParty().getId() + " - "
				+ partyRole.getParty().toString());

		partyRole.setRole(roleService
				.getRole(form.getSecondParty().getRoleId()));
		log.debug(partyRole.getRole().getId() + " - "
				+ partyRole.getRole().toString());

		ica.getPartyRoles().add(partyRole);

		if (isUpdate && ica.getCiaLevel() != null && !ica.getCiaLevel().equals(form.getCiaLevels())){
			form.getCiaLevels().setId(null); // to create a new CIA record and link it to the modified ICA,
												// instead of updating the existing CIA level 
												// that might be used by other ICAs
		}
		
		ica.setCiaLevel(form.getCiaLevels());
		
		log.debug(ica.toString());

		return ica;
	}
	

	private void populateForm(Model model, InterchangeAgreementForm form, String pageMode, SessionUserInformation userInfo) {
		if(pageMode != null) {
			model.addAttribute("pageMode", pageMode);
		}

		/* @deleted by batrian
		List<Profile> profiles = profileService.getAllProfilesForUserDomain(userInfo.getBusinessDomain().getId());
		*/
		Set<Profile> profiles = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()).getProfiles();

		model.addAttribute("profileList", businessDomainUtil.getProfilesElementsForUser(profiles, userInfo));
		model.addAttribute("thirdPartyList", partyService.getAllThirdPartiesForBusinessDomain(userInfo.getBusinessDomain().getId()));
		model.addAttribute("confidentialityLevels", confidentialityLevels());
		model.addAttribute("integrityLevels", integrityLevels());
		model.addAttribute("availabilityLevels", availabilityLevels());
		model.addAttribute("interchangeAgreement", form);
		model.addAttribute("isMultiple", true);
	}
	
	private Map<String,String> confidentialityLevels() {
		Map<String,String> confidentialityLevels = new LinkedHashMap<>();
		for(int i=0 ; i<3 ; i++) {
			confidentialityLevels.put(""+i, messageSource.getMessage("cia.level.confidentiality."+i, null, LocaleContextHolder.getLocale()));
		}
		return confidentialityLevels;
    }
	
	private Map<String,String> integrityLevels() {
		Map<String,String> integrityLevels = new LinkedHashMap<>();
		for(int i=0 ; i<3 ; i++) {
			integrityLevels.put(""+i, messageSource.getMessage("cia.level.integrity."+i, null, LocaleContextHolder.getLocale()));
		}
		return integrityLevels;
    }
	
	private Map<String,String> availabilityLevels() {
		Map<String,String> availabilityLevels = new LinkedHashMap<>();
		for(int i=0 ; i<3 ; i++) {
			availabilityLevels.put(""+i, messageSource.getMessage("cia.level.availability."+i, null, LocaleContextHolder.getLocale()));
		}
		return availabilityLevels;
    }
}