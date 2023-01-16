package com.mikhail.tarasevich.university.config;

import com.mikhail.tarasevich.university.dao.impl.CourseDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
@ComponentScan("com.mikhail.tarasevich.university")
public class SpringConfig {

    private static final String DB_PROPERTIES_FILE_PATH = "database";

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
