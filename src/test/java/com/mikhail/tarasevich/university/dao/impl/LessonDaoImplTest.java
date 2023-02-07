package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.config.SpringConfigTest;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LessonDaoImplTest {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
    private final LessonDao lessonDao = context.getBean("lessonDao", LessonDaoImpl.class);
    private final JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

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
            .withStartTime(LocalDateTime.parse("2023-06-22 12:00:00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss")))
            .build();

    List<Lesson> lessons = new ArrayList<>();

    {
        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @Test
    void save_inputEntity_expectedEntityWithId() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3"));

        Lesson savedEntity = lessonDao.save(lesson2);

        assertEquals(3, savedEntity.getId());
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3 AND name = 'lesson 2' AND group_id = 2 AND user_id = 6 AND " +
                        "course_id = 5 AND lesson_type_id = 2"));
    }

    @Test
    void saveAll_inputEntities_expectedEntitiesAddedInDB() {
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3 OR id = 4"));

        List<Lesson> entities = new ArrayList<>();
        entities.add(lesson1);
        entities.add(lesson2);

        lessonDao.saveAll(entities);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 3 AND name = 'lesson 1' AND group_id = 1 AND user_id = 5 AND " +
                        "course_id = 1 AND lesson_type_id = 1"));
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 4 AND name = 'lesson 2' AND group_id = 2 AND user_id = 6 AND " +
                        "course_id = 5 AND lesson_type_id = 2"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Lesson> foundEntity = lessonDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(lesson1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Lesson> foundEntity = lessonDao.findByName("lesson 1");

        assertTrue(foundEntity.isPresent());
        assertEquals(lesson1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Lesson> foundEntities = lessonDao.findAll();

        assertEquals(lessons, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Lesson> foundEntities = lessonDao.findAll(1, 2);

        assertEquals(lessons, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = lessonDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons");

        assertEquals(expectedRows, rows);
    }

    @Test
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

        lessonDao.update(updatedEntity);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND name = 'Updated' AND group_id = 2 AND user_id = 6 AND " +
                        "course_id = 5 AND lesson_type_id = 2"));
    }


    @Test
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        final Lesson updatedEntity1 = Lesson.builder()
                .withId(1)
                .withName("Updated1")
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

        final Lesson updatedEntity2 = Lesson.builder()
                .withId(2)
                .withName("Updated2")
                .withGroup(Group.builder()
                        .withId(3)
                        .build())
                .withTeacher(Teacher.builder()
                        .withId(3)
                        .build())
                .withCourse(Course.builder()
                        .withId(3)
                        .build())
                .withLessonType(LessonType.builder()
                        .withId(2)
                        .build())
                .build();

        List<Lesson> updatedEntities = new ArrayList<>();
        updatedEntities.add(updatedEntity1);
        updatedEntities.add(updatedEntity2);

        lessonDao.updateAll(updatedEntities);

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND name = 'Updated1' AND group_id = 2 AND user_id = 6 AND " +
                        "course_id = 5 AND lesson_type_id = 2"));

        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 2 AND name = 'Updated2' AND group_id = 3 AND user_id = 3 AND " +
                        "course_id = 3 AND lesson_type_id = 2"));
    }

    @Test
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        lessonDao.deleteById(1);

        int expectedSize = 1;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1"));
    }

    @Test
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        lessonDao.deleteByIds(ids);

        int expectedSize = 0;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "lessons"));
        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 OR id = 2"));
    }

    @Test
    void findLessonsRelateToGroup_inputGroupId_expectedAllLessonsRelateToGroup() {
        List<Lesson> foundEntities = lessonDao.findLessonsRelateToGroup(1);

        assertEquals(1, foundEntities.size());
        assertEquals(lesson1, foundEntities.get(0));
    }

    @Test
    void changeGroup_inputLessonIdGroupId_expectedLessonHasNewGroupId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND group_id = 2"));

        lessonDao.changeGroup(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND group_id = 2"));
    }

    @Test
    void changeTeacher_inputLessonIdTeacherId_expectedLessonHasNewTeacherId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND user_id = 2"));

        lessonDao.changeTeacher(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND user_id = 2"));
    }

    @Test
    void changeCourse_inputLessonIdCourseId_expectedLessonHasNewCourseId() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND course_id = 2"));

        lessonDao.changeCourse(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND course_id = 2"));
    }

    @Test
    void changeLessonType_inputLessonIdLessonTypeId_expectedLessonHasNewLessonType() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND lesson_type_id = 2"));

        lessonDao.changeLessonType(1, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND lesson_type_id = 2"));
    }

    @Test
    void changeStartTime_inputLessonIdStartTime_expectedLessonHasNewStartTime() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND start_time = '2033-11-11 14:00:00'"));

        LocalDateTime newStartTime = LocalDateTime.parse("2033-11-11 14:00:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));

        lessonDao.changeStartTime(1, newStartTime);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "id = 1 AND start_time = '2033-11-11 14:00:00'"));
    }

    @Test
    void unbindLessonsFromCourse_inputCourseId_expectedCourseWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "course_id = 1"));

        lessonDao.unbindLessonsFromCourse(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "course_id = 1"));
    }

    @Test
    void unbindLessonsFromTeacher_inputTeacherId_expectedTeacherWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "user_id = 6"));

        lessonDao.unbindLessonsFromTeacher(6);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "user_id = 6"));
    }

    @Test
    void unbindLessonsFromGroup_inputGroupId_expectedGroupWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "group_id = 1"));

        lessonDao.unbindLessonsFromGroup(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "group_id = 1"));
    }

    @Test
    void unbindLessonsFromLessonType_inputLessonTypeId_expectedLessonTypeWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "lesson_type_id = 1"));

        lessonDao.unbindLessonsFromLessonType(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "lessons",
                "lesson_type_id = 1"));
    }

}
