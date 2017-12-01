package eu.europa.ec.cipa.admin.web.security;

import eu.europa.ec.cipa.admin.web.dto.SessionUserInformation;
import eu.europa.ec.cipa.admin.web.utils.BusinessDomainUtil;
import eu.europa.ec.cipa.admin.web.utils.UserRoleEnum;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.admin.User;
import eu.europa.ec.cipa.etrustex.domain.admin.UserAccessRights;
import eu.europa.ec.cipa.etrustex.services.IBusinessDomainService;
import eu.europa.ec.cipa.etrustex.services.admin.IUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

import java.util.Collection;
import java.util.List;
import java.util.Locale;



public class CustomUserDetailsService extends PreAuthenticatedGrantedAuthoritiesUserDetailsService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired private MessageSource  messageSource;
	@Autowired private IUserService userService;
	@Autowired private IBusinessDomainService businessDomainService;
    @Autowired private BusinessDomainUtil businessDomainUtil;

    @Override
    public UserDetails createuserDetails(Authentication token, Collection<? extends GrantedAuthority> authorities) {
		String username = token.getName();
		
		log.info("authoriseEcasUser({})", username);
		
		Locale locale = LocaleContextHolder.getLocale();
		User user = userService.getUser(username);
		
		if (user == null){
			throw new UsernameNotFoundException(messageSource.getMessage("error.login.user.notConfigured", null, LocaleContextHolder.getLocale()));
		}
		
		
		SessionUserInformation userInfo = new SessionUserInformation();
		List<BusinessDomain> businessDomains = userService.getUserBusinessDomains(user);

		if (businessDomains == null || businessDomains.isEmpty()) {
			if (user.getAccessRights().size() != 1){ // TODO. It can be more than one?
				throw new UsernameNotFoundException(messageSource.getMessage("login.user.businessDomain.noRights", null, locale));
			}

			UserAccessRights uar = user.getAccessRights().iterator().next();

			if (!StringUtils.equalsIgnoreCase(uar.getRole().getCode(), UserRoleEnum.ADM.name())){
				throw new UsernameNotFoundException(messageSource.getMessage("login.user.businessDomain.noRights", null, locale));
			}

			businessDomains = businessDomainService.getAllBusinessDomains();
			log.info("User role is: {}, having access to all business domains. ", uar.getRole().getCode());
		}

		userInfo.setUserId(user.getId());
		userInfo.setUsername(user.getName());
		userInfo.setBusinessDomains(businessDomainUtil.domainToElement(businessDomains));
		userInfo.setBusinessDomain(userInfo.getBusinessDomains().get(0));
		userInfo.setRole(userService.getUserRole(user.getName(), userInfo.getBusinessDomain().getId()));
		
		if (StringUtils.equalsIgnoreCase(UserRoleEnum.LBO.name(), userInfo.getRole().getCode())){
			userInfo.setParties(userService.getUserParties(user.getName(), userInfo.getBusinessDomain().getId()));
		}
		
		return userInfo;
    }
}