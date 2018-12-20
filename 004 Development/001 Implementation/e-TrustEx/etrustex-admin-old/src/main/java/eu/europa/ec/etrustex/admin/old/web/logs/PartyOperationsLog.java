package eu.europa.ec.etrustex.admin.old.web.logs;


//@Aspect
//@Component
public class PartyOperationsLog {
/*	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String MODULE = "CIPAdmin";
	private static final String OPERATION_SAVE = "SAVE";
	private static final String OPERATION_CREATE = "CREATE";
	private static final String OPERATION_UPDATE = "UPDATE";
	private static final String OPERATION_DELETE = "DELETE";
	private static final String OPERATION_VIEW = "VIEW";
	private static final String OPERATION_SEARCH = "SEARCH";
	private static final String ENTITY = "Party";
	
	@Autowired private ILogService logService;
	@Autowired private IBusinessDomainService businessDomainService;
	@Autowired private MessageSource messageSource;

	@Pointcut(value = "@annotation(userActionsLog)", argNames = "userActionsLog")
	public void beanAnnotatedWithUserActionsLog(UserActionsLog userActionsLog) {}
	
	@Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.PartyController.save(.., javax.servlet.http.HttpServletRequest)) && args(.., request)")
    public void saveOperation(HttpServletRequest request) {}
	
	@Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.PartyController.delete(.., javax.servlet.http.HttpServletRequest)) && args(.., request)")
    public void deleteOperation(HttpServletRequest request) {}
	
	@Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.PartyController.searchResults(.., org.springframework.validation.BindingResult)) "
			+ "&& args(.., result)")
	public void searchOperation1(BindingResult result) {}
	
	@Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.PartyController.searchResults(.., javax.servlet.http.HttpServletRequest)) "
			+ "&& args(.., request)")
	public void searchOperation2(HttpServletRequest request) {}
	
	@Pointcut("execution(* eu.europa.ec.etrustex.admin.old.web.controller.PartyController.view(.., javax.servlet.http.HttpServletRequest)) && args(.., request)")
    public void viewOperation(HttpServletRequest request) {}
	
	
	 SAVE 
	@AfterReturning(pointcut="saveOperation(request)", returning="ajaxResult")
    public void saveReturned(JoinPoint joinPoint, HttpServletRequest request, AjaxResult ajaxResult) {
		String logType, description;

		LogDTO logDTO = getLog(joinPoint, request, OPERATION_SAVE); // Check for update is performed within getSaveLog()
		
		if(ajaxResult.getSuccess()) {
			logType = LogDTO.LOG_TYPE.SUCCESS.name();
			description = "Saved " + ENTITY + " with id: " + logDTO.getEntityId();
		} else {
			logType = LogDTO.LOG_TYPE.ERROR.name();
			description = "Business error saving " + ENTITY + ": " + ajaxResult.getMessage();
		}
		
		logDTO.setLogType(logType);
		logDTO.setDescription(description);
		
		logService.saveLog(logDTO);
		

		log.debug("businessDomain: " + logDTO.getBusinessDomain());
		log.debug("correlationId: " + logDTO.getCorrelationId());
		log.debug("urlContext: " + logDTO.getUrlContext());
		log.debug("operation: " + logDTO.getOperation());
		log.debug("username: " + logDTO.getUsername());
		log.debug("userRole: " + logDTO.getUserRole());
		log.debug("entity: " + logDTO.getEntity());
		log.debug("entityId: " + logDTO.getEntityId());
		log.debug("ipAddress: " + logDTO.getAuthIpAddress());
		log.debug("logType: " + logDTO.getLogType());
		log.debug("description: " + logDTO.getDescription());
    }
	
	@AfterThrowing(pointcut="saveOperation(request)", throwing="e")
    public void saveThrowing(JoinPoint joinPoint, HttpServletRequest request, Throwable e) {
		String logType, description;
		LogDTO logDTO = getLog(joinPoint, request, OPERATION_SAVE); 
		
		logType = LogDTO.LOG_TYPE.EXCEPTION.name();
		description = "Exception saving " + ENTITY + ": " + e.getClass();
		
		logDTO.setLogType(logType);
		logDTO.setDescription(description);
		logDTO.setLargeValue(ExceptionUtils.getStackTrace(e));
		
		logService.saveLog(logDTO);
		

		log.debug("businessDomain: " + logDTO.getBusinessDomain());
		log.debug("correlationId: " + logDTO.getCorrelationId());
		log.debug("urlContext: " + logDTO.getUrlContext());
		log.debug("operation: " + logDTO.getOperation());
		log.debug("username: " + logDTO.getUsername());
		log.debug("userRole: " + logDTO.getUserRole());
		log.debug("entity: " + logDTO.getEntity());
		log.debug("entityId: " + logDTO.getEntityId());
		log.debug("ipAddress: " + logDTO.getAuthIpAddress());
		log.debug("logType: " + logDTO.getLogType());
		log.debug("description: " + logDTO.getDescription());
    }
	
	
	 DELETE 
	@AfterReturning(pointcut="beanAnnotatedWithUserActionsLog(userActionsLog) && deleteOperation(request)", returning="ajaxResult")
    public void deleteReturning(JoinPoint joinPoint, UserActionsLog userActionsLog, HttpServletRequest request, AjaxResult ajaxResult) {
		String logType, description;

		LogDTO logDTO = getLog(joinPoint, request, OPERATION_DELETE);
		
		if(ajaxResult.getSuccess()) {
			logType = LogDTO.LOG_TYPE.SUCCESS.name();
			description = "Deleted " + ENTITY + " with id: " + logDTO.getEntityId();
		} else {
			logType = LogDTO.LOG_TYPE.ERROR.name();
			description = "Business error deleting " + ENTITY + " with id: " + logDTO.getEntityId() + ": " + ajaxResult.getMessage();
		}
		
		logDTO.setLogType(logType);
		logDTO.setDescription(description);
		
		logService.saveLog(logDTO);

		
		log.debug("businessDomain: " + logDTO.getBusinessDomain());
		log.debug("correlationId: " + logDTO.getCorrelationId());
		log.debug("urlContext: " + logDTO.getUrlContext());
		log.debug("operation: " + logDTO.getOperation());
		log.debug("username: " + logDTO.getUsername());
		log.debug("userRole: " + logDTO.getUserRole());
		log.debug("entity: " + logDTO.getEntity());
		log.debug("entityId: " + logDTO.getEntityId());
		log.debug("ipAddress: " + logDTO.getAuthIpAddress());
		log.debug("logType: " + logDTO.getLogType());
		log.debug("description: " + logDTO.getDescription());
    }
	
	@AfterThrowing(pointcut="deleteOperation(request)", throwing="e")
    public void deleteThrowing(JoinPoint joinPoint, HttpServletRequest request, Throwable e) {
		String logType, description;
		
		LogDTO logDTO = getLog(joinPoint, request, OPERATION_DELETE); 
		
		
		logType = LogDTO.LOG_TYPE.EXCEPTION.name();
		description = "Exception deleting " + ENTITY + " with id: " + logDTO.getEntityId() + ": " + e.getClass();
		
		logDTO.setLogType(logType);
		logDTO.setDescription(description);
		logDTO.setLargeValue(ExceptionUtils.getStackTrace(e));
		
		logService.saveLog(logDTO);
		

		log.debug("businessDomain: " + logDTO.getBusinessDomain());
		log.debug("correlationId: " + logDTO.getCorrelationId());
		log.debug("urlContext: " + logDTO.getUrlContext());
		log.debug("operation: " + logDTO.getOperation());
		log.debug("username: " + logDTO.getUsername());
		log.debug("userRole: " + logDTO.getUserRole());
		log.debug("entity: " + logDTO.getEntity());
		log.debug("entityId: " + logDTO.getEntityId());
		log.debug("ipAddress: " + logDTO.getAuthIpAddress());
		log.debug("logType: " + logDTO.getLogType());
		log.debug("description: " + logDTO.getDescription());
    }
	
	
	 SEARCH 
	@AfterReturning(pointcut="beanAnnotatedWithUserActionsLog(userActionsLog) && searchOperation1(result) && searchOperation2(request)")
    public void searchReturning(JoinPoint joinPoint, UserActionsLog userActionsLog, BindingResult result, HttpServletRequest request) {
//		if(result.hasErrors()) {
			LogDTO logDTO = getLog(joinPoint, null, OPERATION_SEARCH);
			
			StringBuilder message = new StringBuilder("Business error searching. There are validation errors: \n");
			for(ObjectError e : result.getAllErrors()) {
				String resolvedMsg = messageSource.getMessage(e.getCode(), e.getArguments(), LocaleContextHolder.getLocale());
				message.append(resolvedMsg).append("\n");
			}
			
			logDTO.setLogType(LogDTO.LOG_TYPE.ERROR.name());
			logDTO.setDescription(message.toString());
			
			logService.saveLog(logDTO);
			

			log.debug("businessDomain: " + logDTO.getBusinessDomain());
			log.debug("correlationId: " + logDTO.getCorrelationId());
			log.debug("urlContext: " + logDTO.getUrlContext());
			log.debug("operation: " + logDTO.getOperation());
			log.debug("username: " + logDTO.getUsername());
			log.debug("userRole: " + logDTO.getUserRole());
			log.debug("entity: " + logDTO.getEntity());
			log.debug("entityId: " + logDTO.getEntityId());
			log.debug("ipAddress: " + logDTO.getAuthIpAddress());
			log.debug("logType: " + logDTO.getLogType());
			log.debug("description: " + logDTO.getDescription());
//		}
    }
	
	@AfterThrowing(pointcut="searchOperation(result, request)", throwing="e")
    public void searchThrowing(JoinPoint joinPoint, BindingResult result, HttpServletRequest request,  Throwable e) {
		String logType, description;
		
		LogDTO logDTO = getLog(joinPoint, request, OPERATION_SEARCH); 
		
		
		logType = LogDTO.LOG_TYPE.EXCEPTION.name();
		description = "Exception searching " + ENTITY + ": " + e.getClass();
		
		logDTO.setLogType(logType);
		logDTO.setDescription(description);
		logDTO.setLargeValue(ExceptionUtils.getStackTrace(e));
		
		logService.saveLog(logDTO);
		

		log.debug("businessDomain: " + logDTO.getBusinessDomain());
		log.debug("correlationId: " + logDTO.getCorrelationId());
		log.debug("urlContext: " + logDTO.getUrlContext());
		log.debug("operation: " + logDTO.getOperation());
		log.debug("username: " + logDTO.getUsername());
		log.debug("userRole: " + logDTO.getUserRole());
		log.debug("entity: " + logDTO.getEntity());
		log.debug("entityId: " + logDTO.getEntityId());
		log.debug("ipAddress: " + logDTO.getAuthIpAddress());
		log.debug("logType: " + logDTO.getLogType());
		log.debug("description: " + logDTO.getDescription());
    }
	
	
	 VIEW 
	@AfterThrowing(pointcut="beanAnnotatedWithUserActionsLog(userActionsLog) && viewOperation(request)", throwing="e")
    public void viewThrowing(JoinPoint joinPoint, UserActionsLog userActionsLog, HttpServletRequest request, Throwable e) {
		String logType, description;

		LogDTO logDTO = getLog(joinPoint, request, OPERATION_VIEW); 
		
		logType = LogDTO.LOG_TYPE.EXCEPTION.name();
		description = "Exception viewing " + ENTITY + " with id: " + logDTO.getEntityId() + ": " + ": " + e.getClass();
		
		logDTO.setLogType(logType);
		logDTO.setDescription(description);
		logDTO.setLargeValue(ExceptionUtils.getStackTrace(e));
		
		logService.saveLog(logDTO);
		

		log.debug("businessDomain: " + logDTO.getBusinessDomain());
		log.debug("correlationId: " + logDTO.getCorrelationId());
		log.debug("urlContext: " + logDTO.getUrlContext());
		log.debug("operation: " + logDTO.getOperation());
		log.debug("username: " + logDTO.getUsername());
		log.debug("userRole: " + logDTO.getUserRole());
		log.debug("entity: " + logDTO.getEntity());
		log.debug("entityId: " + logDTO.getEntityId());
		log.debug("ipAddress: " + logDTO.getAuthIpAddress());
		log.debug("logType: " + logDTO.getLogType());
		log.debug("description: " + logDTO.getDescription());
    }

	
	private LogDTO getLog(JoinPoint joinPoint, HttpServletRequest request, String operation) {
		String username = null;
		String userRole = null;
		String ipAddress = null;
		String urlContext = null;
		String correlationId = null;
		Long entityId = null;
		BusinessDomain businessDomain = null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SessionUserInformation userInfo = (SessionUserInformation) auth.getPrincipal();
        
        username = userInfo.getUsername();
        userRole = userInfo.getRole().getCode();
        businessDomain = businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId());
        ipAddress = request.getRemoteAddr();
        urlContext = request.getRequestURI();
        correlationId = request.getSession().getId();
        
		Object[] args = joinPoint.getArgs();
		
		switch (operation) {
			case OPERATION_SAVE:
				for (int i = 0; i < args.length; i++) {
					if(args[i] instanceof PartyView){
						PartyView form = (PartyView) args[i];
						entityId = form.getId();
						operation = entityId != null ? OPERATION_UPDATE : OPERATION_CREATE;
					}
				}
				
				break;
				
			case OPERATION_DELETE:
				for (int i = 0; i < args.length; i++) {
					if(args[i] instanceof Long) {
						entityId = (Long) args[i];
					}
				}
				
				break;
				
			case OPERATION_SEARCH:
				
				break;
				
			case OPERATION_VIEW:
				break;
				
			default:
				break;
		}
		
		LogDTO logDTO = new LogDTO.LogDTOBuilder("")
		.businessDomain(businessDomain)
		.correlationId(correlationId)
		.urlContext(urlContext)
		.operation(operation)
		.module(MODULE)
		.username(username)
		.userRole(userRole)
		.authIpAddress(ipAddress)
		.entity(ENTITY)
		.entityId(entityId)
		
		// TODO remaining properties and module
		.build();
		
		return logDTO;
	}*/
}