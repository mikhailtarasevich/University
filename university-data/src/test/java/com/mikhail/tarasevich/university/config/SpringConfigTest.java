package com.mikhail.tarasevich.university.config;

import com.mikhail.tarasevich.university.dao.*;
import com.mikhail.tarasevich.university.dao.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class SpringConfigTest {

    @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:sql/schema.sql")
                .addScripts("classpath:sql/data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CourseDao courseDao(JdbcTemplate jdbcTemplate){
        return new CourseDaoImpl(jdbcTemplate);
    }

    @Bean
    public DepartmentDao departmentDao(JdbcTemplate jdbcTemplate){
        return new DepartmentDaoImpl(jdbcTemplate);
    }

    @Bean
    public EducationFormDao educationFormDao(JdbcTemplate jdbcTemplate){
        return new EducationFormDaoImpl(jdbcTemplate);
    }

    @Bean
    public FacultyDao facultyDao(JdbcTemplate jdbcTemplate){
        return new FacultyDaoImpl(jdbcTemplate);
    }

    @Bean
    public GroupDao groupDao(JdbcTemplate jdbcTemplate){
        return new GroupDaoImpl(jdbcTemplate);
    }

    @Bean
    public LessonDao lessonDao(JdbcTemplate jdbcTemplate){
        return new LessonDaoImpl(jdbcTemplate);
    }

    @Bean
    public LessonTypeDao lessonTypeDao(JdbcTemplate jdbcTemplate){
        return new LessonTypeDaoImpl(jdbcTemplate);
    }

    @Bean
    public StudentDao studentDao(JdbcTemplate jdbcTemplate){
        return new StudentDaoImpl(jdbcTemplate);
    }

    @Bean
    public TeacherDao teacherDao(JdbcTemplate jdbcTemplate){
        return new TeacherDaoImpl(jdbcTemplate);
    }

    @Bean
    public TeacherTitleDao teacherTitleDao(JdbcTemplate jdbcTemplate){
        return new TeacherTitleDaoImpl(jdbcTemplate);
    }

}
