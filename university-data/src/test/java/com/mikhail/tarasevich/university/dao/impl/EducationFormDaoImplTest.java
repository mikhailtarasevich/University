package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dao.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.EducationForm;
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
class EducationFormDaoImplTest {

    @Autowired
    private EducationFormDao educationFormDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final EducationForm educationForm1 = EducationForm.builder()
            .withId(1)
            .withName("Full-time education")
            .build();

    private static final EducationForm educationForm2 = EducationForm.builder()
            .withId(2)
            .withName("Distance education")
            .build();

    private static final EducationForm educationForm3 = EducationForm.builder()
            .withId(3)
            .withName("test EF 1")
            .build();

    private static final EducationForm educationForm4 = EducationForm.builder()
            .withId(4)
            .withName("test EF 2")
            .build();

    List<EducationForm> educationForms = new ArrayList<>();

    {
        educationForms.add(educationForm1);
        educationForms.add(educationForm2);
        educationForms.add(educationForm3);
        educationForms.add(educationForm4);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        final EducationForm entityForSave = EducationForm.builder().withName("test").build();
        final EducationForm expectedEntity = EducationForm.builder().withId(5).withName("test").build();

        EducationForm savedEntity = educationFormDao.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 5 OR id = 6"));

        final EducationForm test1 = EducationForm.builder()
                .withId(5)
                .withName("test1")
                .build();

        final EducationForm test2 = EducationForm.builder()
                .withId(6)
                .withName("test2")
                .build();

        List<EducationForm> entities = new ArrayList<>();
        entities.add(test1);
        entities.add(test2);

        educationFormDao.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 5 AND name = 'test1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 6 AND name = 'test2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<EducationForm> foundEntity = educationFormDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(educationForm1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<EducationForm> foundEntity = educationFormDao.findByName("Full-time education");

        assertTrue(foundEntity.isPresent());
        assertEquals(educationForm1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<EducationForm> foundEntities = educationFormDao.findAll();

        assertEquals(educationForms, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<EducationForm> foundEntities = educationFormDao.findAll(2, 2);

        List<EducationForm> expectedEntities = new ArrayList<>();
        expectedEntities.add(educationForm3);
        expectedEntities.add(educationForm4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = educationFormDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "education_forms");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 1 AND name = 'Updated'"));

        final EducationForm updatedEntity = EducationForm.builder()
                .withId(1)
                .withName("Updated")
                .build();

        educationFormDao.update(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 1 AND name = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 1 AND name = 'Updated'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 2 AND name = 'Updated'"));

        final EducationForm updatedEntity1 = EducationForm.builder()
                .withId(1)
                .withName("Updated")
                .build();

        final EducationForm updatedEntity2 = EducationForm.builder()
                .withId(2)
                .withName("Updated")
                .build();

        List<EducationForm> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        educationFormDao.updateAll(updatedEntities);

        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "(id = 1 OR id = 2) AND name = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 4"));

        educationFormDao.deleteById(4);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "education_forms"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 3 OR id = 4"));

        Set<Integer> ids = new HashSet<>();
        ids.add(3);
        ids.add(4);

        educationFormDao.deleteByIds(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "education_forms"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "education_forms",
                "id = 3 OR id = 4"));
    }

}
