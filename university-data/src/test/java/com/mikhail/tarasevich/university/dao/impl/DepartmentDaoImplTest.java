package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dao.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = SpringTestConfig.class)
@ActiveProfiles("test")
class DepartmentDaoImplTest {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Department department1 = Department.builder()
            .withId(1)
            .withName("Mathematics and mechanics")
            .withDescription("without description")
            .build();

    private static final Department department2 = Department.builder()
            .withId(2)
            .withName("Computer and information sciences")
            .withDescription("without description")
            .build();

    private static final Department department3 = Department.builder()
            .withId(3)
            .withName("test department 1")
            .withDescription("without description")
            .build();

    private static final Department department4 = Department.builder()
            .withId(4)
            .withName("test department 2")
            .withDescription("without description")
            .build();

    List<Department> departments = new ArrayList<>();

    {
        departments.add(department1);
        departments.add(department2);
        departments.add(department3);
        departments.add(department4);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        final Department entityForSave = Department.builder().withName("test").withDescription("test").build();
        final Department expectedEntity = Department.builder().withId(5).withName("test").withDescription("test").build();

        Department savedEntity = departmentDao.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAllAndFindById_inputEntities_expectedEntitiesAddedInDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 5 OR id = 6"));

        final Department test1 = Department.builder()
                .withId(5)
                .withName("test1")
                .withDescription("test1")
                .build();

        final Department test2 = Department.builder()
                .withId(6)
                .withName("test2")
                .withDescription("test2")
                .build();

        List<Department> entities = new ArrayList<>();
        entities.add(test1);
        entities.add(test2);

        departmentDao.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 5 AND name = 'test1' AND description = 'test1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 6 AND name = 'test2' AND description = 'test2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Department> foundEntity = departmentDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(department1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Department> foundEntity = departmentDao.findByName("Mathematics and mechanics");

        assertTrue(foundEntity.isPresent());
        assertEquals(department1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Department> foundEntities = departmentDao.findAll();

        assertEquals(departments, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Department> foundEntities = departmentDao.findAll(2, 2);

        List<Department> expectedEntities = new ArrayList<>();
        expectedEntities.add(department3);
        expectedEntities.add(department4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = departmentDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "departments");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));

        final Department updatedEntity = Department.builder()
                .withId(1)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        departmentDao.update(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 2 AND name = 'Updated' AND description = 'Updated'"));

        final Department updatedEntity1 = Department.builder()
                .withId(1)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        final Department updatedEntity2 = Department.builder()
                .withId(2)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        List<Department> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        departmentDao.updateAll(updatedEntities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 2 AND name = 'Updated' AND description = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 4"));

        departmentDao.deleteById(4);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "departments"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 3"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 4"));

        Set<Integer> ids = new HashSet<>();
        ids.add(3);
        ids.add(4);

        departmentDao.deleteByIds(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "departments"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 3"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "departments",
                "id = 4"));
    }

    @Test
    void addCourseToDepartment_inputCourseIdDepartmentId_expectedCourseAddedToDepartment() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 3 AND course_id = 1"));

        departmentDao.addCourseToDepartment(3, 1);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 3 AND course_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCourseFromDepartment_inputCourseIdDepartmentId_expectedCourseDeletedFromDepartment() {
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 1 AND course_id = 1"));

        departmentDao.deleteCourseFromDepartment(1, 1);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 1 AND course_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindDepartmentsFromCourse_inputCourseId_expectedDepartmentsFromCourse() {
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "course_id = 2"));

        departmentDao.unbindDepartmentsFromCourse(2);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "course_id = 2"));
    }

}
