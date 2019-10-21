package eu.europa.ec.etrustex.integration.util;

import org.springframework.core.env.Environment;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadPoolExecutor used by the Apache CXF SOAP/JMS endpoint.
 * It attaches a JNDI context to each thread in the pool, otherwise the MessageListenerContainer cannot authenticate on the JMS server
 */
public class CxfThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CxfThreadPoolExecutor.class);

    private static Properties properties;
    private static CxfThreadPoolExecutor INSTANCE;
    private static ThreadLocal<Context> jndiContext = new ThreadLocal<>();

    private static int POOL_SIZE = 1;
    private static int MAX_POOL_SIZE = 5;
    private static int THREAD_KEEP_ALIVE_SECONDS = 10;
    private static int WORK_QUEUE_CAPACITY = 10;

    public static synchronized CxfThreadPoolExecutor getInstance(Environment prop) {
        if (properties == null) {
            properties = new Properties();
            properties.put(InitialContext.INITIAL_CONTEXT_FACTORY, prop.getProperty(InitialContext.INITIAL_CONTEXT_FACTORY));
            properties.put(InitialContext.PROVIDER_URL, prop.getProperty(InitialContext.PROVIDER_URL));
            properties.put(InitialContext.SECURITY_PRINCIPAL, prop.getProperty(InitialContext.SECURITY_PRINCIPAL));
            properties.put(InitialContext.SECURITY_CREDENTIALS, prop.getProperty(InitialContext.SECURITY_CREDENTIALS));
        }

        if (INSTANCE == null) {
            synchronized (CxfThreadPoolExecutor.class) {
                INSTANCE = new CxfThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, THREAD_KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_CAPACITY));
            }
        }
        return INSTANCE;
    }

    private CxfThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread thread, Runnable runnable) {
        try {
            jndiContext.set(new InitialContext(properties));
        } catch (NamingException e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        try {
            jndiContext.get().close();
        } catch (NamingException e) {
            logger.error(e.getMessage(),e);
        }

    }
}
