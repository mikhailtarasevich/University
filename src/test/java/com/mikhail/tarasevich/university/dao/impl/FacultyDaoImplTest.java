package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.config.SpringConfigTest;
import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.entity.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FacultyDaoImplTest {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
    private final FacultyDao facultyDao = context.getBean("facultyDao", FacultyDaoImpl.class);
    private final JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

    private static final Faculty faculty1 = Faculty.builder()
            .withId(1)
            .withName("Applied Mathematics and Informatics")
            .withDescription("without description")
            .build();

    private static final Faculty faculty2 = Faculty.builder()
            .withId(2)
            .withName("Mechanics and Mathematical Modeling")
            .withDescription("without description")
            .build();

    private static final Faculty faculty3 = Faculty.builder()
            .withId(3)
            .withName("Statistics")
            .withDescription("without description")
            .build();

    private static final Faculty faculty4 = Faculty.builder()
            .withId(4)
            .withName("Mathematics and Computer Sciences")
            .withDescription("without description")
            .build();

    private static final Faculty faculty5 = Faculty.builder()
            .withId(5)
            .withName("Mathematical Software and Information Systems Administration")
            .withDescription("without description")
            .build();

    private static final Faculty faculty6 = Faculty.builder()
            .withId(6)
            .withName("test faculty 1")
            .withDescription("without description")
            .build();

    private static final Faculty faculty7 = Faculty.builder()
            .withId(7)
            .withName("test faculty 2")
            .withDescription("without description")
            .build();

    List<Faculty> faculties = new ArrayList<>();

    {
        faculties.add(faculty1);
        faculties.add(faculty2);
        faculties.add(faculty3);
        faculties.add(faculty4);
        faculties.add(faculty5);
        faculties.add(faculty6);
        faculties.add(faculty7);
    }

    @Test
    void save_inputEntity_expectedEntityWithId() {
        final Faculty entityForSave = Faculty.builder().withName("test").withDescription("test").build();
        final Faculty expectedEntity = Faculty.builder().withId(8).withName("test").withDescription("test").build();

        Faculty savedEntity = facultyDao.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 8 OR id = 9"));

        final Faculty test1 = Faculty.builder()
                .withId(8)
                .withName("test1")
                .withDescription("test1")
                .build();

        final Faculty test2 = Faculty.builder()
                .withId(9)
                .withName("test2")
                .withDescription("test2")
                .build();

        List<Faculty> entities = new ArrayList<>();
        entities.add(test1);
        entities.add(test2);

        facultyDao.saveAll(entities);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 8 AND name = 'test1' AND description = 'test1'"));
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 9 AND name = 'test2' AND description = 'test2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Faculty> foundEntity = facultyDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(faculty1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Faculty> foundEntity = facultyDao.findByName("Statistics");

        assertTrue(foundEntity.isPresent());
        assertEquals(faculty3, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Faculty> foundEntities = facultyDao.findAll();

        assertEquals(faculties, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Faculty> foundEntities = facultyDao.findAll(2, 2);

        List<Faculty> expectedEntities = new ArrayList<>();
        expectedEntities.add(faculty3);
        expectedEntities.add(faculty4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = facultyDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        assertEquals(expectedRows, rows);
    }

    @Test
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));

        final Faculty updatedEntity = Faculty.builder()
                .withId(1)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        facultyDao.update(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 1 AND name = 'Updated' AND description = 'Updated'"));
    }

    @Test
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "(id = 1 OR id = 2) AND name = 'Updated' AND description = 'Updated'"));

        final Faculty updatedEntity1 = Faculty.builder()
                .withId(1)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        final Faculty updatedEntity2 = Faculty.builder()
                .withId(2)
                .withName("Updated")
                .withDescription("Updated")
                .build();

        List<Faculty> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        facultyDao.updateAll(updatedEntities);

        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "(id = 1 OR id = 2) AND name = 'Updated' AND description = 'Updated'"));
    }

    @Test
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 7"));

        facultyDao.deleteById(7);

        int expectedSize = 6;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 7"));
    }

    @Test
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 1 OR id = 2"));

        Set<Integer> ids = new HashSet<>();
        ids.add(6);
        ids.add(7);

        facultyDao.deleteByIds(ids);

        int expectedSize = 5;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties"));
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "faculties",
                "id = 1 OR id = 2"));
    }

}
