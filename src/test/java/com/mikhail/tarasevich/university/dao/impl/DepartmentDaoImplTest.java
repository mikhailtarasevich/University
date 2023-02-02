package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.config.SpringConfigTest;
import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.entity.Department;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDaoImplTest {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
    private final DepartmentDao departmentDao = context.getBean("departmentDao", DepartmentDaoImpl.class);
    private final JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

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
    void save_inputEntity_expectedEntityWithId() {
        final Department entityForSave = Department.builder().withName("test").withDescription("test").build();
        final Department expectedEntity = Department.builder().withId(5).withName("test").withDescription("test").build();

        Department savedEntity = departmentDao.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
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
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Department> foundEntities = departmentDao.findAll();

        assertEquals(departments, foundEntities);
    }

    @Test
    void findAllPageable_inputNothing_expectedEntitiesFromThePage() {
        List<Department> foundEntities = departmentDao.findAll(1, 2);

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
    void addCourseToDepartment_inputCourseIdDepartmentId_expectedCourseAddedToDepartment(){
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 3 AND course_id = 1"));

        departmentDao.addCourseToDepartment(3, 1);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 3 AND course_id = 1"));
    }

    @Test
    void deleteCourseToDepartment_inputCourseIdDepartmentId_expectedCourseDeletedFromDepartment(){
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 1 AND course_id = 1"));

        departmentDao.deleteCourseFromDepartment(1,1);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "department_courses", "department_id = 1 AND course_id = 1"));
    }

}
