package eu.europa.ec.etrustex.dao.config;

import eu.europa.ec.etrustex.dao.impl.MessageBinaryDAO;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@ImportResource("classpath:dao-context.xml")
@Configuration
@ComponentScan({ "eu.europa.ec.etrustex.dao" })
@EnableJpaRepositories(basePackages = "eu.europa.ec.etrustex.dao.admin")
@PropertySources({
	@PropertySource(value="classpath:/etrustex-dao.properties")
})
public class EtrustexDataAccessConfig{
	
	@Autowired
    private Environment prop;
	
	@Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(eTrustExDS());
        emf.setPackagesToScan(new String[] { "eu.europa.ec.etrustex.domain" });
        emf.setPersistenceUnitName("etrustex");
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        emf.setJpaProperties(hibernateProperties());
        emf.afterPropertiesSet();
        return emf;
    }
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter()
	{
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(false);
		adapter.setGenerateDdl(false);
		//adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");				
		return adapter;
	}
	
	private final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.transaction.jta.platform", prop.getProperty("transactionmanager.name"));
        hibernateProperties.setProperty("hibernate.dialect", prop.getProperty("dialect.value"));
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", "20");
        hibernateProperties.setProperty("jboss.as.jpa.providerModule", "application");
        hibernateProperties.setProperty("javax.persistence.transactionType", "JTA");
        hibernateProperties.setProperty("hibernate.id.new_generator_mappings", "false");
        return hibernateProperties;
    }

	@Bean(name="eTrustExDS", destroyMethod="")
	public DataSource eTrustExDS(){
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		DataSource dataSource = dsLookup.getDataSource(prop.getProperty("datasource.name"));
      	return dataSource;
	}

	// Liquibase
//	@Bean("liquibase")
//	public SpringLiquibase liquibase(){
//		SpringLiquibase liquibase = new SpringLiquibase();
//		liquibase.setDataSource(eTrustExDS());
//		liquibase.setChangeLog("classpath:liquibase/db.changelog-master.xml");
//		liquibase.setDefaultSchema(prop.getProperty("liquibase.default.schema"));
//
//		return liquibase;
//	}


	@Bean("exceptionTranslation")
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

	@Bean("messageBinaryDAO")
    public MessageBinaryDAO messageBinaryDAO() {
		MessageBinaryDAO messageBinaryDAO = new MessageBinaryDAO();
		messageBinaryDAO.setRetrieveBinaryQuery("SELECT binval.MSG_BIN_VAL_VALUE "
				+ "FROM ETR_TB_MSG_BIN_VALUE binval "
				+ "join ETR_TB_MESSAGE_BINARY bin on binval.MSG_BIN_VAL_ID = bin.MSG_BIN_MSG_BIN_VAL_ID "
				+ "where bin.MSG_BIN_MSG_ID=? and bin.MSG_BIN_TYPE=?");
		messageBinaryDAO.setRetrieveBinaryStreamQuery("SELECT binval.MSG_BIN_VAL_VALUE "
				+ "FROM ETR_TB_MSG_BIN_VALUE binval "
				+ "join ETR_TB_MESSAGE_BINARY bin on binval.MSG_BIN_VAL_ID = bin.MSG_BIN_MSG_BIN_VAL_ID "
				+ "where bin.MSG_BIN_ID=?");
		messageBinaryDAO.setEncryptionKey(prop.getProperty("encryptionKey.value"));
		messageBinaryDAO.setKeySize("128");
        return messageBinaryDAO;
    }


    @Bean("soapErrorMessages")
    public PropertiesFactoryBean mailProperties() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("soapErrorMessages.properties"));
        return bean;
    }
}
