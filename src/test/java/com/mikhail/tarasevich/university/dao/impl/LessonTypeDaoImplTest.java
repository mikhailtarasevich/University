package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.config.SpringConfigTest;
import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LessonTypeDaoImplTest {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
    private final LessonTypeDao lessonTypeDao = context.getBean("lessonTypeDao", LessonTypeDaoImpl.class);
    private final JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

    private static final LessonType lessonType1 = LessonType.builder()
            .withId(1)
            .withName("Lecture")
            .withDuration(Duration.ofMinutes(90))
            .build();

    private static final LessonType lessonType2 = LessonType.builder()
            .withId(2)
            .withName("Practice")
            .withDuration(Duration.ofMinutes(45))
            .build();

    private static final LessonType lessonType3 = LessonType.builder()
            .withId(3)
            .withName("test LT 1")
            .withDuration(Duration.ofMinutes(60))
            .build();

    private static final LessonType lessonType4 = LessonType.builder()
            .withId(4)
            .withName("test LT 2")
            .withDuration(Duration.ofMinutes(120))
            .build();

    List<LessonType> lessonTypes = new ArrayList<>();

    {
        lessonTypes.add(lessonType1);
        lessonTypes.add(lessonType2);
        lessonTypes.add(lessonType3);
        lessonTypes.add(lessonType4);
    }

    @Test
    void save_inputEntity_expectedEntityWithId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5"));

        LessonType savedEntity = lessonTypeDao.save(lessonType1);

        assertEquals(5, savedEntity.getId());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5 AND name = 'Lecture' AND duration = '90'"));
    }

    @Test
    void saveAll_inputEntities_expectedEntitiesAddedInDB() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5 OR id = 6"));

        List<LessonType> entities = new ArrayList<>();
        entities.add(lessonType1);
        entities.add(lessonType2);

        lessonTypeDao.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5 AND name = 'Lecture' AND duration = '90'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 6 AND name = 'Practice' AND duration = '45'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<LessonType> foundEntity = lessonTypeDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(lessonType1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<LessonType> foundEntity = lessonTypeDao.findByName("Lecture");

        assertTrue(foundEntity.isPresent());
        assertEquals(lessonType1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<LessonType> foundEntities = lessonTypeDao.findAll();

        assertEquals(lessonTypes, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<LessonType> foundEntities = lessonTypeDao.findAll(1, 2);

        List<LessonType> expectedEntities = new ArrayList<>();
        expectedEntities.add(lessonType1);
        expectedEntities.add(lessonType2);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = lessonTypeDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_types");

        assertEquals(expectedRows, rows);
    }

    @Test
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated' AND duration = '200'"));

        final LessonType updatedEntity = LessonType.builder()
                .withId(1)
                .withName("Updated")
                .withDuration(Duration.ofMinutes(200))
                .build();

        lessonTypeDao.update(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated' AND duration = '200'"));
    }


    @Test
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated1' AND duration = '200'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 2 AND name = 'Updated2' AND duration = '100'"));

        final LessonType updatedEntity1 = LessonType.builder()
                .withId(1)
                .withName("Updated1")
                .withDuration(Duration.ofMinutes(200))
                .build();

        final LessonType updatedEntity2 = LessonType.builder()
                .withId(2)
                .withName("Updated2")
                .withDuration(Duration.ofMinutes(100))
                .build();

        List<LessonType> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        lessonTypeDao.updateAll(updatedEntities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated1' AND duration = '200'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 2 AND name = 'Updated2' AND duration = '100'"));
    }

    @Test
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        lessonTypeDao.deleteById(4);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_types"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 4"));
    }

    @Test
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        Set<Integer> ids = new HashSet<>();
        ids.add(3);
        ids.add(4);

        lessonTypeDao.deleteByIds(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_types"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 3 OR id = 4"));
    }

    @Test
    void changeDuration_inputLessonTypeIdAndNewDuration_expectedLessonTypeHasNewDuration() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Lecture' AND duration = '90'"));

        lessonTypeDao.changeDuration(1, Duration.ofMinutes(1000));

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Lecture' AND duration = '1000'"));
    }

}
