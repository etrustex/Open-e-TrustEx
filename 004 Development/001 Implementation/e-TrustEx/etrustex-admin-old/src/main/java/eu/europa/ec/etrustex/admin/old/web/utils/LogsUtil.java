package eu.europa.ec.etrustex.admin.old.web.utils;

import eu.europa.ec.etrustex.admin.old.web.dto.SessionUserInformation;
import eu.europa.ec.etrustex.dao.dto.LogDTO;
import eu.europa.ec.etrustex.services.IBusinessDomainService;
import eu.europa.ec.etrustex.services.ILogService;
import eu.europa.ec.etrustex.types.LogModuleEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by guerrpa on 13/07/2016.
 */
@Component
public class LogsUtil {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ILogService logService;
    @Autowired
    private IBusinessDomainService businessDomainService;


    public LogDTO getLog(LogDTO.LOG_OPERATION operation, LogDTO.LOG_TYPE logType, Class entity, Long entityId, String className) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SessionUserInformation userInfo = (SessionUserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LogDTO logDTO = new LogDTO.LogDTOBuilder(logType, operation, className)
                .businessDomain(businessDomainService.getBusinessDomain(userInfo.getBusinessDomain().getId()))
                .module(LogModuleEnum.ETXADMIN)
                .username(userInfo.getUsername())
                .userRole(userInfo.getRole().getCode())
                .urlContext(request.getRequestURI())
                .authIpAddress(request.getRemoteAddr())
                .correlationId(request.getSession().getId())
                .entity(entity != null ? entity.getSimpleName() : null)
                .entityId(entityId)
                .build();

        return logDTO;
    }

    public LogDTO.LOG_OPERATION getLogOperation(String methodName) {
        LogDTO.LOG_OPERATION operation = null;

        if (StringUtils.isNotBlank(methodName)) {
            switch (methodName) {
                case "create":
                case "createLoad":
                    operation = LogDTO.LOG_OPERATION.CREATE;
                    break;
                case "edit":
                case "editLoad":
                    operation = LogDTO.LOG_OPERATION.UPDATE;
                    break;
                case "search":
                case "searchLoad":
                case "searchResults":
                    operation = LogDTO.LOG_OPERATION.SEARCH;
                    break;
                case "view":
                case "viewLoad":
                    operation = LogDTO.LOG_OPERATION.VIEW;
                    break;
                case "delete":
                    operation = LogDTO.LOG_OPERATION.DELETE;
                    break;
                default:
                    operation = LogDTO.LOG_OPERATION.OTHER;
            }
        }

        return operation;
    }

    public void printLog(LogDTO logDTO) {
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
}
