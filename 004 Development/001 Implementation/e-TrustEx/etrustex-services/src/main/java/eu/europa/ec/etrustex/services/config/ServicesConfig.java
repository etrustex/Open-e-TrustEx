package eu.europa.ec.etrustex.services.config;

import java.security.KeyStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import eu.europa.ec.etrustex.dao.config.EtrustexDataAccessConfig;
import eu.europa.ec.etrustex.services.IMessageService;
import eu.europa.ec.etrustex.services.impl.MessageService;
import eu.europa.ec.etrustex.services.util.EncryptionService;

@EnableAspectJAutoProxy
@PropertySources({
	@PropertySource(value="classpath:/etrustex-services.properties"),
	@PropertySource(value="file:///${prop.etx.config.location}")
})
@Configuration
@ComponentScan(basePackages = "eu.europa.ec.etrustex.services")
@EnableCaching
@Import(EtrustexDataAccessConfig.class)
@EnableTransactionManagement
public class ServicesConfig {

	@Autowired
	private Environment prop;
	
	@Autowired
	private KeyStore keyStore;
	
	@Autowired
	private KeyStore aesKeyStore;
	
	@Bean("messageService")
	public IMessageService messageService(){
		MessageService ms = new MessageService();
		ms.setLocalFileStorePath(prop.getProperty("localFileStorePath"));
		ms.setLocalUsefilestore("true".equals(prop.getProperty("localUsefilestore"))?Boolean.TRUE:Boolean.FALSE);
		ms.setUsefilestoreMetadata("false".equals(prop.getProperty("usefilestoreMetadata"))?Boolean.FALSE:Boolean.TRUE);
		return ms;
	}
	
	@Bean("encryptionService")
	public EncryptionService encryptionService(){
		EncryptionService es = new EncryptionService();
		es.setAddAlgorithmPrefix("true".equals(prop.getProperty("addAlgorithmPrefix"))?Boolean.TRUE:Boolean.FALSE);
		es.setCertificateAlias("etrustex");
		es.setPassword(prop.getProperty("keyStore.password"));
		es.setKeyStore(keyStore);
		es.setAesAlias("endpointkey");
		es.setAesPassword(prop.getProperty("aes.keyStore.password"));
		es.setAesKeyStore(aesKeyStore);
		return es;
	}
	
	
	@Bean("ehCacheCacheManager")
    public EhCacheCacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }
	
	@Bean("ehCacheManagerFactoryBean")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);

        return cacheManagerFactoryBean;
    }

}
