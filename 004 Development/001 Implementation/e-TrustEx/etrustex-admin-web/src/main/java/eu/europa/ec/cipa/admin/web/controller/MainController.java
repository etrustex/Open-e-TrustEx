/**
 * 
 */
package eu.europa.ec.cipa.admin.web.controller;

import eu.europa.ec.cipa.admin.web.dto.BusinessDomainElement;
import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.security.ActiveUser;
import eu.europa.ec.cipa.admin.web.utils.UserRoleEnum;
import eu.europa.ec.cipa.etrustex.domain.admin.UserRole;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author batrian
 *
 */
@Controller
public class MainController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired protected IUserService userService;
	@Autowired protected IBusinessDomainService businessDomainService;
	@Autowired private MessageSource  messageSource;


	@RequestMapping(value = "/login/authorise/domain/{id}", method = RequestMethod.POST)
	@ResponseBody
	public List<String> authorizeUserOnSelectedDomain(@PathVariable("id") Long domainId, @ActiveUser SessionUserInformation userInfo) {
		log.debug("MainController - authorizeUserOnSelectedDomain({})...", domainId);
		
		List<String> messages = new ArrayList<String>();
		Locale locale = LocaleContextHolder.getLocale();
		
		String username = userInfo.getUsername();
		
		if(convertDefaultOptionToNull(domainId) != null) {
			userInfo.setBusinessDomain(new BusinessDomainElement(businessDomainService.getBusinessDomain(domainId)));
			
			UserRole userRole = userService.getUserRole(username, domainId);
			userInfo.setRole(userRole);
			
			if (StringUtils.equalsIgnoreCase(UserRoleEnum.LBO.name(), userRole.getCode())){
				userInfo.setParties(userService.getUserParties(username, domainId));
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Authentication authentication = new PreAuthenticatedAuthenticationToken(userInfo, auth.getCredentials(), userInfo.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			messages.add("success" + messageSource.getMessage("user.welcome", new Object[]{username, userInfo.getRole().getDescription()}, locale));
		} else {
			messages.add(messageSource.getMessage("login.businessDomain.required", null, locale));
		}
		
		return messages;
	}

	private Long convertDefaultOptionToNull(Long value) {
		if (value == null || value == -1) {// Default option is hint
			return null;
		} else { // Default option is the element's id
			return value;
		}
	}
}
