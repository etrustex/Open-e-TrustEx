/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.integration.exception.MessageProcessingException;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageBinary;
import eu.europa.ec.cipa.etrustex.services.IAuthorisationService;
import eu.europa.ec.cipa.etrustex.services.ILogService;
import eu.europa.ec.cipa.etrustex.services.dto.LogDTO;

/**
 * @author chiricr
 *
 */
@Aspect
public class LoggingInterceptor {
	
	@Autowired
	private ILogService logService;
	
	@Autowired
	private LogServiceHelper logServiceHelper;
	
	@Autowired
	private IAuthorisationService authorisationService;
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
	
	@AfterThrowing(pointcut="execution(public * eu.europa.ec.cipa.etrustex.integration.service.*.*(..)) "
			+ "&& !execution(public * eu.europa.ec.cipa.etrustex.integration.service.MessageBinaryServiceActivator..*(..))", throwing="ex")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void logException(JoinPoint joinPoint, Throwable ex) {
		
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length == 0) {
			return;
		}
		
		Message<TrustExMessage<String>> message = null;		
		for (Object arg : args) {
			if (arg instanceof Message) {
				message = (Message)arg;
				break;
			}
		}		
		if (message == null) {
			return;
		}
		
		String exceptionDescription = "";
		if (ex instanceof MessageProcessingException) {
			MessageProcessingException mpe = (MessageProcessingException)ex;
			exceptionDescription = mpe.getDescription();
		}
			
		TrustExMessage<String> trustexMessage = message.getPayload();
		LogDTO logDTO = logServiceHelper.createLog(
				trustexMessage, 
				LogDTO.LOG_TYPE.ERROR, LogDTO.LOG_OPERATION.PROCESS_MSG, 
				"!!Inside " + joinPoint.getTarget().getClass().getSimpleName() + " "
						+ ex.getClass().getName() + ": " + exceptionDescription + " " + ex.getMessage(),
				joinPoint.getTarget().getClass().getName());
					
		logService.saveLog(logDTO);		

	}	
	
//	@Around(value="execution(public * eu.europa.ec.cipa.etrustex.integration.service.*.*(..)) ") 
	public Object logExecutionTimeInServiceActivators(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed(joinPoint.getArgs());
		long elapsed = System.currentTimeMillis() - startTime;
		
		Message message = null;	
		Object[] args = joinPoint.getArgs();
		if (args != null) {
			for (Object arg : args) {
				if (arg instanceof Message) {
					message = (Message)arg;
					break;
				}
			}	
		}
		TrustExMessage trustexMessage = null;
		TrustExMessageBinary trustexMessageBinary = null;
		if (message != null && message.getPayload() != null) {
			if (message.getPayload() instanceof TrustExMessage) {
				trustexMessage = (TrustExMessage)message.getPayload();
			} else if (message.getPayload() instanceof TrustExMessageBinary){
				trustexMessageBinary = (TrustExMessageBinary)message.getPayload();
			}
		}
		
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
		logger.debug("Processing time in " + className + "." + methodName + " for message "	
				+ (trustexMessage != null ? trustexMessage.getHeader().getMessageDocumentId() 
				: (trustexMessageBinary != null ? trustexMessageBinary.getMessageDocumentId() : "unknown")) + ": " + elapsed + " ms");	
		
		return result;
	}
	
//	@Around(value="execution(public * eu.europa.ec.cipa.etrustex.services.dao.*.*(..)) ") 
	public Object logExecutionTimeInDAO(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed(joinPoint.getArgs());
		long elapsed = System.currentTimeMillis() - startTime;
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
		logger.debug("Processing time in " + className + "." + methodName + ": " + elapsed + " ms");	
		return result;
	}	
	
	

}
