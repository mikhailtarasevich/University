package com.mikhail.tarasevich.university.dao.config;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.dao.impl.CourseDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.DepartmentDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.EducationFormDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.FacultyDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.GroupDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.LessonDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.LessonTypeDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.StudentDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.TeacherDaoImpl;
import com.mikhail.tarasevich.university.dao.impl.TeacherTitleDaoImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAutoConfiguration
public class SpringTestConfig {

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:sql/schema.sql")
                .addScripts("classpath:sql/data.sql")
                .build();
    }

    @Bean
    @Profile("test")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", String.valueOf(true));
        props.setProperty("hibernate.connection.autocommit", String.valueOf(true));

        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.mikhail.tarasevich.university");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(props);
        factoryBean.afterPropertiesSet();

        return factoryBean.getNativeEntityManagerFactory();
    }

    @Bean
    @Profile("test")
    public CourseDao courseDao(EntityManager entityManager) {
        return new CourseDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public DepartmentDao departmentDao(EntityManager entityManager) {
        return new DepartmentDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public TeacherTitleDao teacherTitleDao(EntityManager entityManager) {
        return new TeacherTitleDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public EducationFormDao educationFormDao(EntityManager entityManager) {
        return new EducationFormDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public FacultyDao facultyDao(EntityManager entityManager) {
        return new FacultyDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public GroupDao groupDao(EntityManager entityManager) {
        return new GroupDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public LessonDao lessonDao(EntityManager entityManager) {
        return new LessonDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public LessonTypeDao lessonTypeDao(EntityManager entityManager) {
        return new LessonTypeDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public StudentDao studentDao(EntityManager entityManager) {
        return new StudentDaoImpl(entityManager);
    }

    @Bean
    @Profile("test")
    public TeacherDao teacherDao(EntityManager entityManager) {
        return new TeacherDaoImpl(entityManager);
    }

}
