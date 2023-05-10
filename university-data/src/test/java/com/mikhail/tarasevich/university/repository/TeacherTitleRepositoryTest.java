package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
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
class TeacherTitleRepositoryTest {

    @Autowired
    private TeacherTitleRepository teacherTitleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final TeacherTitle teacherTitle1 = TeacherTitle.builder()
            .withId(1)
            .withName("Professor")
            .build();
    private static final TeacherTitle teacherTitle2 = TeacherTitle.builder()
            .withId(2)
            .withName("Lecturer")
            .build();
    private static final TeacherTitle teacherTitle3 = TeacherTitle.builder()
            .withId(3)
            .withName("test TT 1")
            .build();
    private static final TeacherTitle teacherTitle4 = TeacherTitle.builder()
            .withId(4)
            .withName("test TT 2")
            .build();

    List<TeacherTitle> teacherTitles = new ArrayList<>();

    {
        teacherTitles.add(teacherTitle1);
        teacherTitles.add(teacherTitle2);
        teacherTitles.add(teacherTitle3);
        teacherTitles.add(teacherTitle4);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        final TeacherTitle entityForSave = TeacherTitle.builder().withName("test").build();
        final TeacherTitle expectedEntity = TeacherTitle.builder().withId(5).withName("test").build();

        TeacherTitle savedEntity = teacherTitleRepository.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 5 OR id = 6"));

        final TeacherTitle test1 = TeacherTitle.builder()
                .withName("test1")
                .build();

        final TeacherTitle test2 = TeacherTitle.builder()
                .withName("test2")
                .build();

        List<TeacherTitle> entities = new ArrayList<>();
        entities.add(test1);
        entities.add(test2);

        teacherTitleRepository.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 5 AND name = 'test1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 6 AND name = 'test2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<TeacherTitle> foundEntity = teacherTitleRepository.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(teacherTitle1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<TeacherTitle> foundEntity = teacherTitleRepository.findByName("Professor");

        assertTrue(foundEntity.isPresent());
        assertEquals(teacherTitle1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<TeacherTitle> foundEntities = teacherTitleRepository.findAll();

        assertEquals(teacherTitles, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<TeacherTitle> foundEntities = teacherTitleRepository.findAll(PageRequest.of(1,2)).toList();

        List<TeacherTitle> expectedEntities = new ArrayList<>();
        expectedEntities.add(teacherTitle3);
        expectedEntities.add(teacherTitle4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = teacherTitleRepository.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teacher_titles");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 1 AND name = 'Updated'"));

        final TeacherTitle updatedEntity = TeacherTitle.builder()
                .withId(1)
                .withName("Updated")
                .build();

        teacherTitleRepository.save(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 1 AND name = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "(id = 1 OR id = 2) AND name = 'Updated'"));

        final TeacherTitle updatedEntity1 = TeacherTitle.builder()
                .withId(1)
                .withName("Updated")
                .build();

        final TeacherTitle updatedEntity2 = TeacherTitle.builder()
                .withId(2)
                .withName("Updated")
                .build();

        List<TeacherTitle> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        teacherTitleRepository.saveAll(updatedEntities);

        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "(id = 1 OR id = 2) AND name = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 4"));

        teacherTitleRepository.deleteById(4);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teacher_titles"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 3 OR id = 4"));

        Set<Integer> ids = new HashSet<>();
        ids.add(3);
        ids.add(4);

        teacherTitleRepository.deleteAllByIdInBatch(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teacher_titles"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 3 OR id = 4"));
    }

}
