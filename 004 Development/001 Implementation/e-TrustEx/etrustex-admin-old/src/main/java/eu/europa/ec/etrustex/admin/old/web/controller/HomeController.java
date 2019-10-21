package eu.europa.ec.etrustex.admin.old.web.controller;

import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.admin.old.web.security.ActiveUser;
import eu.europa.ec.etrustex.services.admin.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class.getName());
    @Autowired
    private IUserService userService;

    @Autowired
    private MessageSource messageSource;


    @RequestMapping("/")
    public String home(HttpServletRequest request, HttpSession session) {
        log.debug("HomeController - home(): ");
//		log.debug("Session id: " + session.getId());
//		log.debug("request.isRequestedSessionIdValid(): " + request.isRequestedSessionIdValid());

//		log.debug("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
//		if(SecurityContextHolder.getContext().getAuthentication() != null)
//			log.debug("Authenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

//		return "redirect";
        return "redirect:/home.do";
//		return "page.home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String init(HttpServletRequest request, HttpSession session) {
        log.debug("HomeController - init(): returning 'page.home'");
//		log.debug("Session id: " + session.getId());
//		log.debug("request.isRequestedSessionIdValid(): " + request.isRequestedSessionIdValid());

//		log.debug("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
//		if(SecurityContextHolder.getContext().getAuthentication() != null)
//			log.debug("Authenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());


        return "page.home";
    }

    @RequestMapping(value = "/home/load", method = RequestMethod.POST)
    public String load(@ActiveUser SessionUserInformation userInfo, Model model, HttpServletRequest request, HttpSession session) {
        log.info("HomeController - load(): returning 'page.home.inner'");
//    	log.debug("Session id: " + session.getId());
//    	log.debug("request.isRequestedSessionIdValid(): " + request.isRequestedSessionIdValid());
//		
//    	log.debug("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
//		if(SecurityContextHolder.getContext().getAuthentication() != null)
//			log.debug("Authenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());


        model.addAttribute("welcomeMessage", messageSource.getMessage("home.welcome.message." + userInfo.getRole().getCode().toLowerCase(), new Object[]{userInfo.getRole().getDescription()}, LocaleContextHolder.getLocale()));
        return "page.home.inner";
    }
    
    @RequestMapping("/logoutPage")
    public String logoutPage(HttpServletRequest request, HttpSession session) {
    	SecurityContextHolder.clearContext();
        request.getSession().invalidate();
		return "logout";
    }
}
