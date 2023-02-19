package com.mikhail.tarasevich.university.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
@ComponentScan("com.mikhail.tarasevich.university")
@Import(SecurityConfig.class)
@EnableTransactionManagement
public class PersistenceConfig {

    private static final String DB_PROPERTIES_FILE_PATH = "database";

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource(){
        ResourceBundle resource = ResourceBundle.getBundle(DB_PROPERTIES_FILE_PATH);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(resource.getString("db.url"));
        dataSource.setUsername(resource.getString("db.user"));
        dataSource.setPassword(resource.getString("db.password"));

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
