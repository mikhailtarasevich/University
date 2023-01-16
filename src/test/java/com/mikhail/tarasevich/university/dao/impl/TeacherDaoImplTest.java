package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.config.SpringConfigTest;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeacherDaoImplTest {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
    private final TeacherDao teacherDao = context.getBean("teacherDao", TeacherDaoImpl.class);
    private final JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

    private static final Teacher teacher1 = Teacher.builder()
            .withId(5)
            .withFirstName("Robert")
            .withLastName("Sapolski")
            .withGender(Gender.MALE)
            .withEmail("rsapolski@gmail.com")
            .withPassword("3245")
            .withTeacherTitle(
                    TeacherTitle.builder().withId(1).build()
            )
            .withDepartment(Department.builder().withId(1).build())
            .build();

    private static final Teacher teacher2 = Teacher.builder()
            .withId(6)
            .withFirstName("Kristina")
            .withLastName("Drugova")
            .withGender(Gender.FEMALE)
            .withEmail("drugova@gmail.com")
            .withPassword("drdrug18")
            .withTeacherTitle(
                    TeacherTitle.builder().withId(2).build()
            )
            .withDepartment(Department.builder().withId(2).build())
            .build();

    private static final Teacher teacher3 = Teacher.builder()
            .withId(9)
            .withFirstName("testName3")
            .withLastName("testLastName3")
            .withGender(Gender.MALE)
            .withEmail("test3@gmail.com")
            .withPassword("test3")
            .withTeacherTitle(
                    TeacherTitle.builder().withId(1).build()
            )
            .withDepartment(Department.builder().withId(1).build())
            .build();

    private static final Teacher teacher4 = Teacher.builder()
            .withId(10)
            .withFirstName("testName4")
            .withLastName("testLastName4")
            .withGender(Gender.FEMALE)
            .withEmail("test4@gmail.com")
            .withPassword("test4")
            .withTeacherTitle(
                    TeacherTitle.builder().withId(2).build()
            )
            .withDepartment(Department.builder().withId(2).build())
            .build();

    List<Teacher> teachers = new ArrayList<>();

    {
        teachers.add(teacher1);
        teachers.add(teacher2);
        teachers.add(teacher3);
        teachers.add(teacher4);
    }

    @Test
    void save_inputEntity_expectedEntityWithId() {
        final Teacher entityForSave = Teacher.builder()
                .withFirstName("testName3")
                .withLastName("testLastName3")
                .withGender(Gender.MALE)
                .withEmail("test3@gmail.com")
                .withPassword("test3")
                .withTeacherTitle(
                        TeacherTitle.builder().withId(1).build()
                )
                .withDepartment(Department.builder().withId(1).build())
                .build();

        final Teacher expectedEntity = Teacher.builder()
                .withId(11)
                .withFirstName("testName3")
                .withLastName("testLastName3")
                .withGender(Gender.MALE)
                .withEmail("test3@gmail.com")
                .withPassword("test3")
                .withTeacherTitle(
                        TeacherTitle.builder().withId(1).build()
                )
                .withDepartment(Department.builder().withId(1).build())
                .build();


        Teacher savedEntity = teacherDao.save(entityForSave);

        assertEquals(expectedEntity, savedEntity);
    }

    @Test
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "(id = 11 OR id = 12) AND user_type = 'teacher'"));

        List<Teacher> entitiesForSave = new ArrayList<>();
        entitiesForSave.add(teacher3);
        entitiesForSave.add(teacher4);
        teacherDao.saveAll(entitiesForSave);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 11 AND user_type = 'teacher' AND first_name = 'testName3'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 12 AND user_type = 'teacher' AND first_name = 'testName4'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Teacher> foundEntity = teacherDao.findById(5);

        assertTrue(foundEntity.isPresent());
        assertEquals(teacher1, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Teacher> foundEntities = teacherDao.findAll();

        assertEquals(teachers, foundEntities);
    }

    @Test
    void findAllPageable_inputNothing_expectedEntitiesFromThePage() {
        List<Teacher> foundEntities = teacherDao.findAll(1, 2);

        List<Teacher> expectedEntities = new ArrayList<>();
        expectedEntities.add(teacher3);
        expectedEntities.add(teacher4);

        assertEquals(expectedEntities, foundEntities);
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = teacherDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'teacher'");

        assertEquals(expectedRows, rows);
    }

    @Test
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated' AND department_id = 4 AND teacher_title_id = 1"));

        final Teacher entityForUpdate = Teacher.builder()
                .withId(5)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withPassword("Updated")
                .withDepartment(Department.builder().withId(4).build())
                .withTeacherTitle(TeacherTitle.builder().withId(1).build())
                .build();

        teacherDao.update(entityForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated' AND department_id = 4 AND teacher_title_id = 1"));
    }

    @Test
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated' AND department_id = 4 AND teacher_title_id = 1"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 6 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated' AND department_id = 4 AND teacher_title_id = 1"));

        final Teacher entityForUpdate1 = Teacher.builder()
                .withId(5)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withPassword("Updated")
                .withDepartment(Department.builder().withId(4).build())
                .withTeacherTitle(TeacherTitle.builder().withId(1).build())
                .build();

        final Teacher entityForUpdate2 = Teacher.builder()
                .withId(6)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withPassword("Updated")
                .withDepartment(Department.builder().withId(4).build())
                .withTeacherTitle(TeacherTitle.builder().withId(1).build())
                .build();

        List<Teacher> entitiesForUpdate = new ArrayList<>();
        entitiesForUpdate.add(entityForUpdate1);
        entitiesForUpdate.add(entityForUpdate2);

        teacherDao.updateAll(entitiesForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated' AND department_id = 4 AND teacher_title_id = 1"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 6 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated' AND department_id = 4 AND teacher_title_id = 1"));
    }

    @Test
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 9"));

        teacherDao.deleteById(9);

        int expectedSize = 3;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'teacher'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 9"));
    }

    @Test
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 9 OR id = 10"));

        Set<Integer> ids = new HashSet<>();
        ids.add(9);
        ids.add(10);

        teacherDao.deleteByIds(ids);

        int expectedSize = 2;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'teacher'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 9 OR id = 10"));
    }

    @Test
    void addUserToGroup_inputUserIdGroupId_expectedRowWasAddedToUserGroupsTable() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 5 AND group_id = 4"));

        teacherDao.addUserToGroup(5, 4);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 5 AND group_id = 4"));
    }

    @Test
    void addTeacherToCourse_inputTeacherIdCourseId_expectedRowAddedToUserCoursesTable() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 5"));

        teacherDao.addTeacherToCourse(5, 5);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 5"));
    }

    @Test
    void deleteTeacherFromCourse_inputTeacherIdCourseId_expectedRowDeletedFromUserCoursesTable() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 1"));

        teacherDao.deleteTeacherFromCourse(5, 1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 1"));
    }

    @Test
    void changeDepartment_inputTeacherIdDepartmentId_expectedDepartmentIdWasChanged() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND department_id = 1"));

        teacherDao.changeDepartment(5, 3);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND department_id = 3"));
    }

    @Test
    void changeTeacherTitle_inputTeacherIdTeacherTitleId_expectedTeacherTitleIdWasChanged() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND teacher_title_id = 1"));

        teacherDao.changeTeacherTitle(5, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND teacher_title_id = 2"));
    }

    @Test
    void deleteTeacherFromGroup_inputTeacherIdGroupId_expectedRowDeletedFromUserGroupsTable() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 5 AND group_id = 1"));

        teacherDao.deleteTeacherFromGroup(5, 1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 5 AND group_id = 1"));
    }

    @Test
    void findTeachersRelateToGroup_inputGroupId_expectedTeachersRelateToGroup() {

        List<Teacher> foundStudents = teacherDao.findTeachersRelateToGroup(1);

        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(teacher1);

        assertEquals(expectedTeachers, foundStudents);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "group_id = 1"), foundStudents.size());
    }

    @Test
    void findTeachersRelateToCourse_inputCourseId_expectedTeachersRelateToCourse() {

        List<Teacher> foundStudents = teacherDao.findTeachersRelateToCourse(1);

        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(teacher1);

        assertEquals(expectedTeachers, foundStudents);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "course_id = 1"), foundStudents.size());
    }

    @Test
    void findTeachersRelateToDepartment_inputDepartmentId_expectedTeachersRelateToDepartment() {

        List<Teacher> foundStudents = teacherDao.findTeachersRelateToDepartment(1);

        List<Teacher> expectedTeachers = new ArrayList<>();
        expectedTeachers.add(teacher1);
        expectedTeachers.add(teacher3);

        assertEquals(expectedTeachers, foundStudents);
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "department_id = 1"), foundStudents.size());
    }

}
