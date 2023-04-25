package com.mikhail.tarasevich.university.config;

import com.mikhail.tarasevich.university.util.JndiProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("com.mikhail.tarasevich.university")
@EnableTransactionManagement
public class PersistenceConfig {

    @Bean
    public DataSource dataSource() throws NamingException {
        JndiTemplate jndiTemplate = new JndiTemplate();

        return (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/db");
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.mikhail.tarasevich.university.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws NamingException {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", JndiProperty.getProperty("java:comp/env/hibernate/dialect", "org.hibernate.dialect.PostgreSQL82Dialect"));
        properties.put("hibernate.show_sql", JndiProperty.getProperty("java:comp/env/hibernate/show_sql", "true"));

        return properties;
    }

}
