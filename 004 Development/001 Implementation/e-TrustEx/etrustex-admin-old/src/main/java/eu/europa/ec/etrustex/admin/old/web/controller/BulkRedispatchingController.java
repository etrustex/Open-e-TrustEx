package eu.europa.ec.etrustex.admin.old.web.controller;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import eu.europa.ec.etrustex.admin.old.web.dto.BulkRedispatchForm;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.etrustex.integration.util.LogServiceHelper;
import eu.europa.ec.etrustex.integration.util.RedispatchingBean;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.types.LogModuleEnum;

@Controller
@RequestMapping(value = "/bulkredispatch")
@PreAuthorize("hasAnyRole('ADM', 'SUP')")
public class BulkRedispatchingController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired private RedispatchingBean redispatcher;
    @Autowired private MessageSource messageSource;
    @Autowired private LogServiceHelper logServiceHelper;
    @Autowired private ILogService logService;
    
	
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model) {
    	model.addAttribute("bulkRedispatchForm", new BulkRedispatchForm());
    	model.addAttribute("pageMode", "new");
    	return "page.bulkRedispatch";
    }
    
    
    @RequestMapping(value = "/search/load", method = RequestMethod.POST)
    public String searchLoad(SessionStatus sessionStatus) {
    	 sessionStatus.setComplete();
        return "frag.bulkRedispatch.inner";
    }
    
    
    
    @PreAuthorize("hasAnyRole('ADM', 'CBO')")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String redispatch(@RequestParam("file") MultipartFile csv, Model model) {
    	String result = "success";
    	Scanner scanner = null;
    	try {
    		byte[] bytes = csv.getBytes();
    		scanner = new Scanner(new ByteArrayInputStream(bytes));
            scanner.useDelimiter(",");
            TrustExMessage<String> trustExMessage;
            while(scanner.hasNext()){
            	trustExMessage = redispatcher.redispatch(Long.parseLong(scanner.next()));
            	saveLog(trustExMessage, LogDTO.LOG_OPERATION.REDISPATCH);
            }
    	} catch (NumberFormatException e) {
        	log.error(e.getMessage(), e);
        	result = "Error reading the file";
    	} catch (Exception e) {
        	log.error(e.getMessage(), e);
        	result = messageSource.getMessage("error.message.redispatch", null, LocaleContextHolder.getLocale());
        }finally{
        	if(scanner != null){
        		scanner.close();
        	}
        }
    	
    	model.addAttribute("errorMsg", result);

        return result;    
    }

    private void saveLog(TrustExMessage<String> trustExMessage, LogDTO.LOG_OPERATION operation) {
        LogDTO cipAdminlogDTO = logServiceHelper.createLog(trustExMessage, LogDTO.LOG_TYPE.INFO, operation, "Inside BulkRedispatchingController#resubmit", this.getClass().getName());
        cipAdminlogDTO.setModule(LogModuleEnum.ETXADMIN);
        logService.saveLog(cipAdminlogDTO);
    }
}
