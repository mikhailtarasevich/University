package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.LessonType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringTestConfig.class)
class LessonTypeRepositoryTest {

    @Autowired
    private LessonTypeRepository lessonTypeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5"));

        final LessonType entityForSave = LessonType.builder()
                .withName("Lecture")
                .withDuration(Duration.ofMinutes(90))
                .build();

        LessonType savedEntity = lessonTypeRepository.save(entityForSave);

        assertEquals(5, savedEntity.getId());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5 AND name = 'Lecture' AND duration = '90'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedInDB() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5 OR id = 6"));

        final LessonType entityForSave1 = LessonType.builder()
                .withName("Lecture")
                .withDuration(Duration.ofMinutes(90))
                .build();

        final LessonType entityForSave2 = LessonType.builder()
                .withName("Practice")
                .withDuration(Duration.ofMinutes(45))
                .build();

        List<LessonType> entities = new ArrayList<>();
        entities.add(entityForSave1);
        entities.add(entityForSave2);

        lessonTypeRepository.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 5 AND name = 'Lecture' AND duration = '90'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 6 AND name = 'Practice' AND duration = '45'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<LessonType> foundEntity = lessonTypeRepository.findById(1);

        System.out.println(foundEntity);

        assertTrue(foundEntity.isPresent());
        assertEquals(lessonType1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<LessonType> foundEntity = lessonTypeRepository.findByName("Lecture");

        assertTrue(foundEntity.isPresent());
        assertEquals(lessonType1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<LessonType> foundEntities = lessonTypeRepository.findAll();

        assertEquals(lessonTypes, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<LessonType> foundEntities = lessonTypeRepository.findAll(PageRequest.of(0, 2)).toList();

        List<LessonType> expectedEntities = new ArrayList<>();
        expectedEntities.add(lessonType1);
        expectedEntities.add(lessonType2);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = lessonTypeRepository.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_types");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated' AND duration = '200'"));

        final LessonType updatedEntity = LessonType.builder()
                .withId(1)
                .withName("Updated")
                .withDuration(Duration.ofMinutes(200))
                .build();

        lessonTypeRepository.save(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated' AND duration = '200'"));
    }


    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        lessonTypeRepository.saveAll(updatedEntities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Updated1' AND duration = '200'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 2 AND name = 'Updated2' AND duration = '100'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        lessonTypeRepository.deleteById(4);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_types"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        Set<Integer> ids = new HashSet<>();
        ids.add(3);
        ids.add(4);

        lessonTypeRepository.deleteAllByIdInBatch(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lesson_types"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 3 OR id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeDuration_inputLessonTypeIdAndNewDuration_expectedLessonTypeHasNewDuration() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Lecture' AND duration = '90'"));

        lessonTypeRepository.changeDuration(1, Duration.ofMinutes(1000));

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lesson_types",
                "id = 1 AND name = 'Lecture' AND duration = '1000'"));
    }

}
