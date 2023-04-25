package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.config.SpringConfigTest;
import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = SpringConfigTest.class)
class TeacherTitleDaoImplTest {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
    private final TeacherTitleDao teacherTitleDao = context.getBean("teacherTitleDaoTest", TeacherTitleDao.class);
    private final JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

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
    void save_inputEntity_expectedEntityWithId() {
        final TeacherTitle entityForSave = TeacherTitle.builder().withName("test").build();
        final TeacherTitle expectedEntity = TeacherTitle.builder().withId(5).withName("test").build();

        TeacherTitle savedEntity = teacherTitleDao.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
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

        teacherTitleDao.saveAll(entities);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 5 AND name = 'test1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 6 AND name = 'test2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<TeacherTitle> foundEntity = teacherTitleDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(teacherTitle1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<TeacherTitle> foundEntity = teacherTitleDao.findByName("Professor");

        assertTrue(foundEntity.isPresent());
        assertEquals(teacherTitle1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<TeacherTitle> foundEntities = teacherTitleDao.findAll();

        assertEquals(teacherTitles, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<TeacherTitle> foundEntities = teacherTitleDao.findAll(2, 2);

        List<TeacherTitle> expectedEntities = new ArrayList<>();
        expectedEntities.add(teacherTitle3);
        expectedEntities.add(teacherTitle4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = teacherTitleDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "teacher_titles");

        assertEquals(expectedRows, rows);
    }

    @Test
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 1 AND name = 'Updated'"));

        final TeacherTitle updatedEntity = TeacherTitle.builder()
                .withId(1)
                .withName("Updated")
                .build();

        teacherTitleDao.update(updatedEntity);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 1 AND name = 'Updated'"));
    }

    @Test
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

        teacherTitleDao.updateAll(updatedEntities);

        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "(id = 1 OR id = 2) AND name = 'Updated'"));
    }

    @Test
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 4"));

        teacherTitleDao.deleteById(4);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teacher_titles"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 4"));
    }

    @Test
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 3 OR id = 4"));

        Set<Integer> ids = new HashSet<>();
        ids.add(3);
        ids.add(4);

        teacherTitleDao.deleteByIds(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teacher_titles"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "teacher_titles",
                "id = 3 OR id = 4"));
    }

}
