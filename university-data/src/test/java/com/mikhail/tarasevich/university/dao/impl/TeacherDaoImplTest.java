package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dao.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
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
class TeacherDaoImplTest {

    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Teacher teacher1 = Teacher.builder()
            .withId(5)
            .withFirstName("Robert")
            .withLastName("Sapolski")
            .withGender(Gender.MALE)
            .withEmail("rsapolski@gmail.com")
            .withPassword("3245")
            .withTeacherTitle(
                    TeacherTitle.builder()
                            .withId(1)
                            .withName("Professor")
                            .build()
            )
            .withDepartment(Department.builder()
                    .withId(1)
                    .withName("Mathematics and mechanics")
                    .withDescription("without description")
                    .build())
            .build();

    private static final Teacher teacher2 = Teacher.builder()
            .withId(6)
            .withFirstName("Kristina")
            .withLastName("Drugova")
            .withGender(Gender.FEMALE)
            .withEmail("drugova@gmail.com")
            .withPassword("drdrug18")
            .withTeacherTitle(
                    TeacherTitle.builder()
                            .withId(2)
                            .withName("Lecturer")
                            .build()
            )
            .withDepartment(Department.builder()
                    .withId(2)
                    .withName("Computer and information sciences")
                    .withDescription("without description")
                    .build())
            .build();

    private static final Teacher teacher3 = Teacher.builder()
            .withId(9)
            .withFirstName("testName3")
            .withLastName("testLastName3")
            .withGender(Gender.MALE)
            .withEmail("test3@gmail.com")
            .withPassword("test3")
            .withTeacherTitle(
                    TeacherTitle.builder()
                            .withId(1)
                            .withName("Professor")
                            .build()
            )
            .withDepartment(Department.builder()
                    .withId(1)
                    .withName("Mathematics and mechanics")
                    .withDescription("without description")
                    .build())
            .build();

    private static final Teacher teacher4 = Teacher.builder()
            .withId(10)
            .withFirstName("testName4")
            .withLastName("testLastName4")
            .withGender(Gender.FEMALE)
            .withEmail("test4@gmail.com")
            .withPassword("test4")
            .withTeacherTitle(
                    TeacherTitle.builder()
                            .withId(2)
                            .withName("Lecturer")
                            .build()
            )
            .withDepartment(Department.builder()
                    .withId(2)
                    .withName("Computer and information sciences")
                    .withDescription("without description")
                    .build())
            .build();

    List<Teacher> teachers = new ArrayList<>();

    {
        teachers.add(teacher1);
        teachers.add(teacher2);
        teachers.add(teacher3);
        teachers.add(teacher4);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        assertEquals(expectedEntity.getId(), savedEntity.getId());
        assertEquals(expectedEntity.getFirstName(), savedEntity.getFirstName());
        assertEquals(expectedEntity.getLastName(), savedEntity.getLastName());
        assertEquals(expectedEntity.getEmail(), savedEntity.getEmail());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "(id = 11 OR id = 12) AND user_type = 'teacher'"));

        final Teacher entityForSave1 = Teacher.builder()
                .withFirstName("entityForSave1")
                .withLastName("entityForSave1")
                .withGender(Gender.MALE)
                .withEmail("entityForSave1")
                .withPassword("entityForSave1")
                .withTeacherTitle(
                        TeacherTitle.builder().withId(1).build()
                )
                .withDepartment(Department.builder().withId(1).build())
                .build();

        final Teacher entityForSave2 = Teacher.builder()
                .withFirstName("entityForSave2")
                .withLastName("entityForSave2")
                .withGender(Gender.MALE)
                .withEmail("entityForSave2")
                .withPassword("entityForSave2")
                .withTeacherTitle(
                        TeacherTitle.builder().withId(1).build()
                )
                .withDepartment(Department.builder().withId(1).build())
                .build();

        List<Teacher> entitiesForSave = new ArrayList<>();
        entitiesForSave.add(entityForSave1);
        entitiesForSave.add(entityForSave2);
        teacherDao.saveAll(entitiesForSave);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 11 AND user_type = 'teacher' AND first_name = 'entityForSave1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 12 AND user_type = 'teacher' AND first_name = 'entityForSave2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Teacher> foundEntity = teacherDao.findById(5);

        assertTrue(foundEntity.isPresent());
        assertEquals(teacher1, foundEntity.get());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Teacher> foundEntity = teacherDao.findByName("drugova@gmail.com");

        assertTrue(foundEntity.isPresent());
        assertEquals(teacher2, foundEntity.get());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Teacher> foundEntities = teacherDao.findAll();

        assertEquals(teachers, foundEntities);
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Teacher> foundEntities = teacherDao.findAll(2, 2);

        List<Teacher> expectedEntities = new ArrayList<>();
        expectedEntities.add(teacher3);
        expectedEntities.add(teacher4);

        assertEquals(expectedEntities.size(), foundEntities.size());
        assertEquals(expectedEntities.get(0).getId(), foundEntities.get(0).getId());
        assertEquals(expectedEntities.get(0).getEmail(), foundEntities.get(0).getEmail());
        assertEquals(expectedEntities.get(1).getId(), foundEntities.get(1).getId());
        assertEquals(expectedEntities.get(1).getEmail(), foundEntities.get(1).getEmail());
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = teacherDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'teacher'");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateGeneralUserInfo_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated'"));

        final Teacher entityForUpdate = Teacher.builder()
                .withId(5)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .build();

        teacherDao.updateGeneralUserInfo(entityForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated'"));
    }

    @Test
    void updatePassword_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND password = 'hello12'"));

        teacherDao.updateUserPassword(5, "hello12");

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND password = 'hello12'"));
    }


    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserToGroup_inputUserIdGroupId_expectedRowWasAddedToUserGroupsTable() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 5 AND group_id = 4"));

        teacherDao.addUserToGroup(5, 4);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 5 AND group_id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addTeacherToCourse_inputTeacherIdCourseId_expectedRowAddedToUserCoursesTable() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 5"));

        teacherDao.addTeacherToCourse(5, 5);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 5"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteTeacherFromCourse_inputTeacherIdCourseId_expectedRowDeletedFromUserCoursesTable() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 1"));

        teacherDao.deleteTeacherFromCourse(5, 1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_courses",
                "user_id = 5 AND course_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeDepartment_inputTeacherIdDepartmentId_expectedDepartmentIdWasChanged() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND department_id = 1"));

        teacherDao.changeDepartment(5, 3);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND department_id = 3"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeTeacherTitle_inputTeacherIdTeacherTitleId_expectedTeacherTitleIdWasChanged() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND teacher_title_id = 1"));

        teacherDao.changeTeacherTitle(5, 2);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 5 AND teacher_title_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindTeachersFromCourse_inputCourseId_expectedThereAreNoCourse() {
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user_courses", "course_id = 3"));

        teacherDao.unbindTeachersFromCourse(3);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user_courses", "course_id = 3"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindTeachersFromDepartment_inputDepartmentId_expectedThereAreNoDepartment() {
        assertEquals(2L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "users", "department_id = 1"));

        teacherDao.unbindTeachersFromDepartment(1);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "users", "department_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindTeachersFromGroup_inputGroupId_expectedTeacherWereUnbound() {
        assertEquals(1L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user_groups", "group_id = 1"));

        teacherDao.unbindTeachersFromGroup(1);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user_groups", "group_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindTeachersFromTeacherTitle_inputTeacherTitleId_expectedTeacherTitleWereUnbound() {
        assertEquals(2L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "users", "teacher_title_id = 1"));

        teacherDao.unbindTeachersFromTeacherTitle(1);

        assertEquals(0L, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "users", "teacher_title_id = 1"));
    }

}
