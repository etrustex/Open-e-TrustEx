package eu.europa.ec.etrustex.services;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Application Lifecycle Listener implementation class CoreWebListenner
 *
 */
public class CoreWebListenner implements ServletContextListener {
	private static final Logger logger = LoggerFactory
			.getLogger(CoreWebListenner.class);
	private Timer timer =null;
	
    /**
     * Default constructor. 
     */
    public CoreWebListenner() {
    	timer = new Timer();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	System.setProperty("saaj.use.mimepull","true");
    	   	
    	timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				logger.debug("Start cleaning");
				try{					
					String tmpDir = System.getProperty("java.io.tmpdir");
					File root = new File(tmpDir);
					for (File f:root.listFiles()){
						try{
							if (f.isFile() && (f.lastModified() < new Date().getTime() -3600000)){ //file older than 1 hour
								logger.debug("Remove "+f.getName());
								boolean b =f.delete();
								logger.debug("Removal succeeded: "+b);
							}
						}catch(Exception e){
							//DO NOTHING
						}
					}
					
				}catch(Exception e){
					logger.error(e.getMessage(),e);
				}
				logger.debug("Stop cleaning");
			}
		}, 0, 3600000); //every 1 hour
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	try{
    		timer.cancel();
    	}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
    }
	
}
