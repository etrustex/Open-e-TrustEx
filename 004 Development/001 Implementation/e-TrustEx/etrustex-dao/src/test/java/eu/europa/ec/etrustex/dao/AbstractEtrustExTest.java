package eu.europa.ec.etrustex.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.etrustex.dao.config.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@Rollback
@Transactional
public abstract class AbstractEtrustExTest {
	
	protected Properties prop = new Properties();
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@PostConstruct
	protected void loadPropertiesFile(){
		InputStream input = null;
		try {
			String fileName = "testEtrustex.properties";		
			input = getClass().getClassLoader().getResourceAsStream(fileName);
			if (input == null) {
				System.out.println("Unable to find " + fileName);
				return;
			}			
			prop.load(input);			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(input != null){
				try{
					input.close();
				}catch(Exception e){}
			}
		}
	}
	
	protected void flush(){
		entityManager.flush();
	}


}
