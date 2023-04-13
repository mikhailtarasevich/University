package com.mikhail.tarasevich.university.config;

import com.mikhail.tarasevich.university.dao.*;
import com.mikhail.tarasevich.university.dao.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import java.util.Properties;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan(basePackages = "com.mikhail.tarasevich.university",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {PersistenceConfig.class}))
@EnableTransactionManagement
public class SpringConfigTest {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript("classpath:sql/schema.sql")
                .addScripts("classpath:sql/data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.mikhail.tarasevich.university.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
        properties.put("hibernate.show_sql", true);

        return properties;
    }

    @Bean
    CourseDao courseDaoTest() {
        return new CourseDaoImpl(sessionFactory().getObject());
    }

    @Bean
    DepartmentDao departmentDaoTest() {
        return new DepartmentDaoImpl(sessionFactory().getObject());
    }

    @Bean
    EducationFormDao educationFormDaoTest() {
        return new EducationFormDaoImpl(sessionFactory().getObject());
    }

    @Bean
    FacultyDao facultyDaoTest() {
        return new FacultyDaoImpl(sessionFactory().getObject());
    }

    @Bean
    GroupDao groupDaoTest() {
        return new GroupDaoImpl(sessionFactory().getObject());
    }

    @Bean
    LessonDao lessonDaoTest() {
        return new LessonDaoImpl(sessionFactory().getObject());
    }

    @Bean
    LessonTypeDao lessonTypeDaoTest() {
        return new LessonTypeDaoImpl(sessionFactory().getObject());
    }

    @Bean
    TeacherTitleDao teacherTitleDaoTest() {
        return new TeacherTitleDaoImpl(sessionFactory().getObject());
    }

    @Bean
    TeacherDao teacherDaoTest() {
        return new TeacherDaoImpl(sessionFactory().getObject());
    }

    @Bean
    StudentDao studentDaoTest() {
        return new StudentDaoImpl(sessionFactory().getObject());
    }

}
