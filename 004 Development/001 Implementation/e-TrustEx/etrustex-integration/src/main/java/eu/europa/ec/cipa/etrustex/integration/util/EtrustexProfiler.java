package eu.europa.ec.cipa.etrustex.integration.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.integration.Message;

import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessage;


public class EtrustexProfiler implements MethodInterceptor
{
    public Object invoke(MethodInvocation methodInvocation) throws Throwable
    {
    	Object output = null;
    	long start;
    	long elapsedTimeInfo;
    	String threadInfo 		= Thread.currentThread().getId()+"";
    	String classInfo = methodInvocation.getThis().getClass().toString(); 
    	String methodInfo 		= classInfo+"."+methodInvocation.getMethod().getName();
    	try {
			start = System.currentTimeMillis();
			//long memUsedBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			output = methodInvocation.proceed();
			//long memUsedAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			elapsedTimeInfo 	= System.currentTimeMillis() - start;
			 
			String suffix = "";
			if(elapsedTimeInfo < 500){
				suffix = "[SUB500ms]";
			}else if(elapsedTimeInfo > 5000)
				{suffix = "[OVER3S]";}
			logger.error("---->"+"T-Id:"+threadInfo+"--Target:"+methodInfo+"--Time:"+elapsedTimeInfo+"(ms)"+suffix);
			
		} catch (Exception e) {
			for (Object o : methodInvocation.getArguments()) {
				if("transformIncomingMessage".equals(methodInvocation.getMethod().getName())){
					Message msg = (Message)o;
					logger.error("\n---->BOOM::"+"T-Id:"+threadInfo+"--Target:"+methodInfo+"--Arguments::\n"+
							msg.getPayload());
				}if(classInfo.endsWith("Activator")){
					Message<TrustExMessage<String>> msg = (Message<TrustExMessage<String>>)o;
					logger.error("\n---->BOOM::"+"T-Id:"+threadInfo+"--Target:"+methodInfo+"--Arguments::\n"+
							msg.getPayload().getPayload()+"----\n");
				}
					logger.error("\n---->BOOM::"+"T-Id:"+threadInfo+"--Target:"+methodInfo+"--Arguments::\n"+o);
			}
			throw e;
		}
        return output;
    }
		
	private static final Logger logger = LoggerFactory
			.getLogger(EtrustexProfiler.class);
	


}
