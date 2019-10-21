package eu.europa.ec.etrustex.dao.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:testEtrustex.properties" })
@ComponentScan(basePackages = "eu.europa.ec.etrustex.dao",
	excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {EtrustexDataAccessConfig.class})
})
public class PersistenceConfig {
	
	@Autowired
    private Environment env;

	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(eTrustExDS());
        emf.setPackagesToScan(new String[] { "eu.europa.ec.etrustex.domain" });
        emf.setPersistenceUnitName("etrustex");
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(hibernateProperties());
        return emf;
    }
	
	@Bean
    public DataSource eTrustExDS() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("eTrustExDS.driverClassName"));
        dataSource.setUrl(env.getProperty("eTrustExDS.url"));
        dataSource.setUsername(env.getProperty("eTrustExDS.username"));
        dataSource.setPassword(env.getProperty("eTrustExDS.password"));
        return dataSource;
    }
	
	@Bean("transactionManager")
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
        
	private final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect"		 , "org.hibernate.dialect.Oracle10gDialect");
        hibernateProperties.setProperty("org.hibernate.flushMode", FlushMode.ALWAYS.name());
        //hibernateProperties.setProperty("hibernate.show_sql", "true");
        return hibernateProperties;
    }

}
