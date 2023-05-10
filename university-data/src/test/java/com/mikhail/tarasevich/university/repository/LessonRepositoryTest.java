package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringTestConfig.class)
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Lesson lesson1 = Lesson.builder()
            .withId(1)
            .withName("lesson 1")
            .withGroup(Group.builder()
                    .withId(1)
                    .withName("g1")
                    .build())
            .withTeacher(Teacher.builder()
                    .withId(5)
                    .withFirstName("Robert")
                    .withLastName("Sapolski")
                    .withGender(Gender.MALE)
                    .withEmail("rsapolski@gmail.com")
                    .withPassword("3245")
                    .withTeacherTitle(TeacherTitle.builder()
                            .withId(1)
                            .build())
                    .withDepartment(Department.builder()
                            .withId(1)
                            .build())
                    .build())
            .withCourse(Course.builder()
                    .withId(1)
                    .withName("Mathematical Modeling and Artificial Intelligence")
                    .withDescription("without description")
                    .build())
            .withLessonType(LessonType.builder()
                    .withId(1)
                    .withName("Lecture")
                    .withDuration(Duration.ofMinutes(90))
                    .build())
            .withStartTime(LocalDateTime.parse("2023-06-22 12:00:00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss")))
            .build();

    private static final Lesson lesson2 = Lesson.builder()
            .withId(2)
            .withName("lesson 2")
            .withGroup(Group.builder()
                    .withId(2)
                    .withName("g2")
                    .build())
            .withTeacher(Teacher.builder()
                    .withId(6)
                    .withFirstName("Kristina")
                    .withLastName("Drugova")
                    .withGender(Gender.FEMALE)
                    .withEmail("drugova@gmail.com")
                    .withPassword("drdrug18")
                    .withTeacherTitle(TeacherTitle.builder()
                            .withId(2)
                            .build())
                    .withDepartment(Department.builder()
                            .withId(2)
                            .build())
                    .build())
            .withCourse(Course.builder()
                    .withId(5)
                    .withName("Artificial intelligence systems and supercomputer technologies")
                    .withDescription("without description")
                    .build())
            .withLessonType(LessonType.builder()
                    .withId(2)
                    .withName("Practice")
                    .withDuration(Duration.ofMinutes(45))
                    .build())
            .withStartTime(LocalDateTime.parse("2023-06-22 13:00:00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss")))
            .build();

    List<Lesson> lessons = new ArrayList<>();

    {
        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3"));

        final Lesson lesson3 = Lesson.builder().withName("lesson 3").build();

        Lesson savedEntity = lessonRepository.save(lesson3);

        assertEquals(3, savedEntity.getId());
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3 AND name = 'lesson 3'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedInDB() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3 OR id = 4"));

        final Lesson lesson3 = Lesson.builder().withName("lesson 3").build();
        final Lesson lesson4 = Lesson.builder().withName("lesson 4").build();

        List<Lesson> entities = new ArrayList<>();
        entities.add(lesson3);
        entities.add(lesson4);

        lessonRepository.saveAll(entities);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3 AND name = 'lesson 3'"));
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 4 AND name = 'lesson 4'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Lesson> foundEntity = lessonRepository.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(1, foundEntity.get().getId());
        assertEquals("lesson 1", foundEntity.get().getName());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Lesson> foundEntity = lessonRepository.findByName("lesson 1");

        assertTrue(foundEntity.isPresent());
        assertEquals(lesson1.getId(), foundEntity.get().getId());
        assertEquals(lesson1.getName(), foundEntity.get().getName());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Lesson> foundEntities = lessonRepository.findAll();

        assertEquals(lessons.size(), foundEntities.size());
        assertEquals(1, foundEntities.get(0).getId());
        assertEquals("lesson 1", foundEntities.get(0).getName());
        assertEquals(2, foundEntities.get(1).getId());
        assertEquals("lesson 2", foundEntities.get(1).getName());
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Lesson> foundEntities = lessonRepository
                .findAll(PageRequest.of(0, 2, Sort.by("startTime"))).toList();

        assertEquals(lessons.size(), foundEntities.size());
        assertEquals(1, foundEntities.get(0).getId());
        assertEquals("lesson 1", foundEntities.get(0).getName());
        assertEquals(2, foundEntities.get(1).getId());
        assertEquals("lesson 2", foundEntities.get(1).getName());
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = lessonRepository.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND name = 'Updated' AND group_id = 2 AND user_id = 6 AND " +
                        "course_id = 5 AND lesson_type_id = 2"));

        final Lesson updatedEntity = Lesson.builder()
                .withId(1)
                .withName("Updated")
                .withGroup(Group.builder()
                        .withId(2)
                        .build())
                .withTeacher(Teacher.builder()
                        .withId(6)
                        .build())
                .withCourse(Course.builder()
                        .withId(5)
                        .build())
                .withLessonType(LessonType.builder()
                        .withId(2)
                        .build())
                .build();

        lessonRepository.save(updatedEntity);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND name = 'Updated' AND group_id = 2 AND user_id = 6 AND " +
                        "course_id = 5 AND lesson_type_id = 2"));
    }


    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        final Lesson updatedEntity1 = Lesson.builder()
                .withId(1)
                .withName("Updated1")
                .build();

        final Lesson updatedEntity2 = Lesson.builder()
                .withId(2)
                .withName("Updated2")
                .build();

        List<Lesson> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND name = 'Updated1'"));

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 2 AND name = 'Updated2'"));

        lessonRepository.saveAll(updatedEntities);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND name = 'Updated1'"));

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 2 AND name = 'Updated2'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        lessonRepository.deleteById(1);

        int expectedSize = 1;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        lessonRepository.deleteAllByIdInBatch(ids);

        int expectedSize = 0;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 OR id = 2"));
    }

    @Test
    void findLessonByGroupId_inputGroupId_expectedAllLessonsRelateToGroup() {
        List<Lesson> foundEntities = lessonRepository.findLessonByGroupId(1);

        assertEquals(1, foundEntities.size());
        assertEquals(lesson1.getId(), foundEntities.get(0).getId());
        assertEquals(lesson1.getName(), foundEntities.get(0).getName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeGroup_inputLessonIdGroupId_expectedLessonHasNewGroupId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND group_id = 2"));

        lessonRepository.changeGroup(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND group_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeTeacher_inputLessonIdTeacherId_expectedLessonHasNewTeacherId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND user_id = 2"));

        lessonRepository.changeTeacher(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND user_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeCourse_inputLessonIdCourseId_expectedLessonHasNewCourseId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND course_id = 2"));

        lessonRepository.changeCourse(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND course_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeLessonType_inputLessonIdLessonTypeId_expectedLessonHasNewLessonType() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND lesson_type_id = 2"));

        lessonRepository.changeLessonType(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND lesson_type_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeStartTime_inputLessonIdStartTime_expectedLessonHasNewStartTime() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND start_time = '2033-11-11 14:00:00'"));

        LocalDateTime newStartTime = LocalDateTime.parse("2033-11-11 14:00:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));

        lessonRepository.changeStartTime(1, newStartTime);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND start_time = '2033-11-11 14:00:00'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindLessonsFromCourse_inputCourseId_expectedCourseWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "course_id = 1"));

        lessonRepository.unbindLessonsFromCourse(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "course_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindLessonsFromTeacher_inputTeacherId_expectedTeacherWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "user_id = 6"));

        lessonRepository.unbindLessonsFromTeacher(6);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "user_id = 6"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindLessonsFromGroup_inputGroupId_expectedGroupWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "group_id = 1"));

        lessonRepository.unbindLessonsFromGroup(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "group_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindLessonsFromLessonType_inputLessonTypeId_expectedLessonTypeWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "lesson_type_id = 1"));

        lessonRepository.unbindLessonsFromLessonType(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "lesson_type_id = 1"));
    }

}
