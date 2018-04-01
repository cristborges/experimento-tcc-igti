package com.cristborges.experimento_camel_tcc.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = { "com.cristborges.experimento_camel_tcc" })
@EnableTransactionManagement
public class ConfigPersistencia {

	@Inject
	private Environment environment;

	@Inject
	private DataSource dataSource;

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		final HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabasePlatform(environment.getProperty("hibernate.dialect"));
		hibernateJpaVendorAdapter.setShowSql(false);

		final Map<String, String> jpaPropertyMap = new HashMap<String, String>();
		jpaPropertyMap.put("hibernate.jdbc.batch_size", environment.getProperty("hibernate.jdbcBatchSize"));
		jpaPropertyMap.put("hibernate.default_schema", environment.getProperty("hibernate.defaultSchema"));

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan("com.cristborges.experimento_camel_tcc");
		factory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		factory.setDataSource(dataSource);
		factory.setJpaPropertyMap(jpaPropertyMap);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
		return jpaTransactionManager;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Configuration
	@Profile("standard")
	static class StandardProfile {

		@Inject
		private Environment environment;

		@Bean
		public DataSource dataSource() {
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
			basicDataSource.setUrl(environment.getProperty("dataSource.url"));
			basicDataSource.setUsername(environment.getProperty("dataSource.username"));
			basicDataSource.setPassword(environment.getProperty("dataSource.password"));
			return basicDataSource;
		}
	}

	@Configuration
	@Profile("test")
	static class TestProfile {

		@Inject
		private Environment environment;

		@Bean
		public DataSource dataSource() {
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName(environment.getProperty("dataSource.driverClassName"));
			basicDataSource.setUrl(environment.getProperty("dataSource.url"));
			return basicDataSource;
		}

		@Bean
		public JdbcTemplate jdbcTemplate() {
			return new JdbcTemplate(dataSource());
		}

		@Bean(initMethod = "init", destroyMethod = "destroy")
		public DerbyDatabaseBean derbyDatabaseBean() {
			DerbyDatabaseBean derbyDatabaseBean = new DerbyDatabaseBean();
			derbyDatabaseBean.setJdbcTemplate(jdbcTemplate());
			return derbyDatabaseBean;
		}
	}
}
