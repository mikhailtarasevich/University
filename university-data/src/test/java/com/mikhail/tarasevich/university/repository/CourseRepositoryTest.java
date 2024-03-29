package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringTestConfig.class)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Course course1 = Course.builder()
            .withId(1)
            .withName("Mathematical Modeling and Artificial Intelligence")
            .withDescription("without description")
            .build();

    private static final Course course2 = Course.builder()
            .withId(2)
            .withName("Software Engineering")
            .withDescription("without description")
            .build();

    private static final Course course3 = Course.builder()
            .withId(3)
            .withName("Bioinformatics")
            .withDescription("without description")
            .build();

    private static final Course course4 = Course.builder()
            .withId(4)
            .withName("Data analysis in the economy")
            .withDescription("without description")
            .build();

    private static final Course course5 = Course.builder()
            .withId(5)
            .withName("Artificial intelligence systems and supercomputer technologies")
            .withDescription("without description")
            .build();

    private static final Course course6 = Course.builder()
            .withId(6)
            .withName("Intelligent information systems and data processing")
            .withDescription("without description")
            .build();

    private static final Course course7 = Course.builder()
            .withId(7)
            .withName("test course 1")
            .withDescription("without description")
            .build();

    private static final Course course8 = Course.builder()
            .withId(8)
            .withName("test course 2")
            .withDescription("without description")
            .build();

    List<Course> courses = new ArrayList<>();

    {
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);
        courses.add(course5);
        courses.add(course6);
        courses.add(course7);
        courses.add(course8);
    }

    @Test
//    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
//            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 9"));

        final Course entityForSave = Course.builder().withName("test").withDescription("test").build();
        final Course expectedEntity = Course.builder().withId(9).withName("test").withDescription("test").build();

        Course savedEntity = courseRepository.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 9"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedInDB() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 9 OR id = 10"));

        final Course test1 = Course.builder()
                .withId(9)
                .withName("test1")
                .withDescription("test1")
                .build();

        final Course test2 = Course.builder()
                .withId(10)
                .withName("test2")
                .withDescription("test2")
                .build();

        List<Course> entities = new ArrayList<>();
        entities.add(test1);
        entities.add(test2);

        courseRepository.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 9 AND name = 'test1' AND description = 'test1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 10 AND name = 'test2' AND description = 'test2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Course> foundEntity = courseRepository.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(course1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Course> foundEntity = courseRepository.findByName("Software Engineering");

        assertTrue(foundEntity.isPresent());
        assertEquals(course2, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Course> foundEntities = courseRepository.findAll();

        assertEquals(courses, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Course> foundEntities = courseRepository.findAll(PageRequest.of(1, 2)).toList();

        List<Course> expectedEntities = new ArrayList<>();
        expectedEntities.add(course3);
        expectedEntities.add(course4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = courseRepository.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));

        final Course updatedEntity = Course.builder()
                .withId(1)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        courseRepository.update(updatedEntity.getId(), updatedEntity.getName(), updatedEntity.getDescription());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 2 AND name = 'Updated' AND description = 'Updated'"));

        final Course updatedEntity1 = Course.builder()
                .withId(1)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        final Course updatedEntity2 = Course.builder()
                .withId(2)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        List<Course> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        courseRepository.saveAll(updatedEntities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 2 AND name = 'Updated' AND description = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 7"));

        courseRepository.deleteById(7);

        int expectedSize = 7;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 7"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 7 OR id = 8"));

        Set<Integer> ids = new HashSet<>();
        ids.add(7);
        ids.add(8);

        courseRepository.deleteAllByIdInBatch(ids);

        int expectedSize = 6;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "courses"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "courses",
                "id = 7 OR id = 8"));
    }

    @Test
    void findCoursesByDepartment_inputDepartmentId_expectedAllCoursesRelateToDepartment() {
        List<Course> foundEntities = courseRepository.findCoursesByDepartmentsId(1);

        List<Course> expectedEntities = new ArrayList<>();
        expectedEntities.add(course1);
        expectedEntities.add(course2);
        expectedEntities.add(course3);
        expectedEntities.add(course4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void findCoursesRelateToTeacher_inputTeacherId_expectedAllCoursesRelateToTeacher() {
        List<Course> foundEntities = courseRepository.findCoursesByTeachersId(6);

        List<Course> expectedEntities = new ArrayList<>();
        expectedEntities.add(course5);
        expectedEntities.add(course6);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindCoursesFromTeacher_inputTeacherId_expectedThereAreNoTeacher() {
        assertEquals(2L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user_courses", "user_id = 6"));

        courseRepository.unbindCoursesFromTeacher(6);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user_courses", "user_id = 6"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindCoursesFromDepartment_inputDepartmentId_expectedThereAreNoDepartment() {
        assertEquals(4L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 1"));

        courseRepository.unbindCoursesFromDepartment(1);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 1"));
    }

}
