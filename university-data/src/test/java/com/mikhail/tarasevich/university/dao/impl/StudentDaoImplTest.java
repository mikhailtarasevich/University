package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dao.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Student;
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
class StudentDaoImplTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Student student1 = Student.builder()
            .withId(1)
            .withFirstName("Tom")
            .withLastName("Robinson")
            .withGender(Gender.MALE)
            .withEmail("tomrobinson@gmail.com")
            .withPassword("1234")
            .withGroup(Group.builder()
                    .withId(1)
                    .withName("g1")
                    .withFaculty(Faculty.builder().withId(1).build())
                    .withHeadStudent(
                            Student.builder().withId(1).build()
                    )
                    .withEducationForm(
                            EducationForm.builder().withId(2)
                                    .build()
                    )
                    .build())
            .build();

    private static final Student student2 = Student.builder()
            .withId(2)
            .withFirstName("Rory")
            .withLastName("McDonald")
            .withGender(Gender.MALE)
            .withEmail("mcdonald@yandex.ru")
            .withPassword("1111")
            .withGroup(Group.builder()
                    .withId(2)
                    .withName("g2")
                    .withFaculty(Faculty.builder().withId(2).build())
                    .withHeadStudent(
                            Student.builder().withId(2).build()
                    )
                    .withEducationForm(
                            EducationForm.builder().withId(2)
                                    .build()
                    )
                    .build())
            .build();

    private static final Student student3 = Student.builder()
            .withId(3)
            .withFirstName("Kate")
            .withLastName("Austin")
            .withGender(Gender.FEMALE)
            .withEmail("kitty@yahoo.com")
            .withPassword("kate12#")
            .withGroup(Group.builder()
                    .withId(3)
                    .withName("g3")
                    .withFaculty(Faculty.builder().withId(3).build())
                    .withHeadStudent(
                            Student.builder().withId(3).build()
                    )
                    .withEducationForm(
                            EducationForm.builder().withId(1)
                                    .build()
                    )
                    .build())
            .build();

    private static final Student student4 = Student.builder()
            .withId(4)
            .withFirstName("Amanda")
            .withLastName("Johnson")
            .withGender(Gender.FEMALE)
            .withEmail("amandaj@gmail.com")
            .withPassword("0000")
            .withGroup(Group.builder()
                    .withId(4)
                    .withName("g4")
                    .withFaculty(Faculty.builder().withId(4).build())
                    .withHeadStudent(
                            Student.builder().withId(4).build()
                    )
                    .withEducationForm(
                            EducationForm.builder().withId(1)
                                    .build()
                    )
                    .build())
            .build();

    private static final Student student5 = Student.builder()
            .withId(7)
            .withFirstName("testName1")
            .withLastName("testLastName1")
            .withGender(Gender.MALE)
            .withEmail("test1@gmail.com")
            .withPassword("test1")
            .withGroup(Group.builder()
                    .withId(1)
                    .withName("g1")
                    .withFaculty(Faculty.builder().withId(1).build())
                    .withHeadStudent(Student.builder().withId(1).build())
                    .withEducationForm(EducationForm.builder().withId(2).build())
                    .build())
            .build();

    private static final Student student6 = Student.builder()
            .withId(8)
            .withFirstName("testName2")
            .withLastName("testLastName2")
            .withGender(Gender.FEMALE)
            .withEmail("test2@gmail.com")
            .withPassword("test2")
            .withGroup(Group.builder()
                    .withId(1)
                    .withName("g1")
                    .withFaculty(Faculty.builder().withId(1).build())
                    .withHeadStudent(Student.builder().withId(1).build())
                    .withEducationForm(EducationForm.builder().withId(2).build())
                    .build())
            .build();

    List<Student> students = new ArrayList<>();

    {
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        final Student entityForSave = Student.builder()
                .withFirstName("Tom")
                .withLastName("Robinson")
                .withGender(Gender.MALE)
                .withEmail("tomrobinson@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder()
                        .withId(1)
                        .withName("g1")
                        .withFaculty(Faculty.builder().withId(1).build())
                        .withHeadStudent(
                                Student.builder().withId(1).build()
                        )
                        .withEducationForm(
                                EducationForm.builder().withId(2)
                                        .build()
                        )
                        .build())
                .build();

        final Student expectedEntity = Student.builder()
                .withId(11)
                .withFirstName("Tom")
                .withLastName("Robinson")
                .withGender(Gender.MALE)
                .withEmail("tomrobinson@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder()
                        .withId(1)
                        .withName("g1")
                        .withFaculty(Faculty.builder().withId(1).build())
                        .withHeadStudent(
                                Student.builder().withId(1).build()
                        )
                        .withEducationForm(
                                EducationForm.builder().withId(2)
                                        .build()
                        )
                        .build())
                .build();

        Student savedEntity = studentDao.save(entityForSave);

        assertEquals(expectedEntity.getId(), savedEntity.getId());
        assertEquals(expectedEntity.getFirstName(), savedEntity.getFirstName());
        assertEquals(expectedEntity.getLastName(), savedEntity.getLastName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "(id = 11 OR id = 12) AND user_type = 'student'"));

        final Student studentForSave1 = Student.builder()
                .withFirstName("studentForSave1")
                .withLastName("studentForSave1")
                .withGender(Gender.MALE)
                .withPassword("studentForSave1")
                .withEmail("studentForSave1")
                .build();

        final Student studentForSave2 = Student.builder()
                .withFirstName("studentForSave2")
                .withLastName("studentForSave2")
                .withGender(Gender.MALE)
                .withPassword("studentForSave2")
                .withEmail("studentForSave2")
                .build();

        List<Student> entitiesForSave = new ArrayList<>();
        entitiesForSave.add(studentForSave1);
        entitiesForSave.add(studentForSave2);

        studentDao.saveAll(entitiesForSave);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 11 AND user_type = 'student' AND first_name = 'studentForSave1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 12 AND user_type = 'student' AND first_name = 'studentForSave2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Student> foundEntity = studentDao.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(student1.getId(), foundEntity.get().getId());
        assertEquals(student1.getFirstName(), foundEntity.get().getFirstName());
        assertEquals(student1.getLastName(), foundEntity.get().getLastName());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Student> foundEntity = studentDao.findByName("tomrobinson@gmail.com");

        assertTrue(foundEntity.isPresent());
        assertEquals(student1.getId(), foundEntity.get().getId());
        assertEquals(student1.getFirstName(), foundEntity.get().getFirstName());
        assertEquals(student1.getLastName(), foundEntity.get().getLastName());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Student> foundEntities = studentDao.findAll();

        assertEquals(students.size(), foundEntities.size());
        assertEquals(students.get(0).getId(), foundEntities.get(0).getId());
        assertEquals(students.get(1).getId(), foundEntities.get(1).getId());
        assertEquals(students.get(2).getId(), foundEntities.get(2).getId());
        assertEquals(students.get(3).getId(), foundEntities.get(3).getId());
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Student> foundEntities = studentDao.findAll(2, 2);

        List<Student> expectedEntities = new ArrayList<>();
        expectedEntities.add(student3);
        expectedEntities.add(student4);

        assertEquals(expectedEntities.size(), foundEntities.size());
        assertEquals(expectedEntities.get(0).getId(), foundEntities.get(0).getId());
        assertEquals(expectedEntities.get(1).getId(), foundEntities.get(1).getId());
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = studentDao.count();

        long expectedRows = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'student'");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated'"));

        final Student entityForUpdate = Student.builder()
                .withId(1)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withPassword("Updated")
                .build();

        studentDao.update(entityForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateGeneralUserInfo_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated'"));

        final Student entityForUpdate = Student.builder()
                .withId(1)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withGroup(Group.builder()
                        .withId(5)
                        .build())
                .build();

        studentDao.updateGeneralUserInfo(entityForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated'"));
    }

    @Test
    void updatePassword_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND password = 'hello12'"));

        studentDao.updateUserPassword(1, "hello12");

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND password = 'hello12'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 2 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated'"));

        final Student entityForUpdate1 = Student.builder()
                .withId(1)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withPassword("Updated")
                .withGroup(Group.builder()
                        .withId(5)
                        .build())
                .build();

        final Student entityForUpdate2 = Student.builder()
                .withId(2)
                .withFirstName("Updated")
                .withLastName("Updated")
                .withGender(Gender.MALE)
                .withEmail("Updated")
                .withPassword("Updated")
                .withGroup(Group.builder()
                        .withId(5)
                        .build())
                .build();

        List<Student> entitiesForUpdate = new ArrayList<>();
        entitiesForUpdate.add(entityForUpdate1);
        entitiesForUpdate.add(entityForUpdate2);

        studentDao.updateAll(entitiesForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 2 AND first_name = 'Updated' AND last_name = 'Updated' AND email = 'Updated' AND " +
                        "password = 'Updated'"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 7"));

        studentDao.deleteById(7);

        int expectedSize = 5;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'student'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 7"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addUserToGroup_inputUserIdGroupId_expectedUserHasAGroup() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND group_id = 1"));

        studentDao.addUserToGroup(1, 4);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND group_id = 1"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND group_id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 7 OR id = 8"));

        Set<Integer> ids = new HashSet<>();
        ids.add(7);
        ids.add(8);

        studentDao.deleteByIds(ids);

        int expectedSize = 4;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "user_type = 'student'"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 7 OR id = 8"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteStudentFromGroup_inputStudentId_expectedStudentHasNoGroup() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND group_id = 1"));

        studentDao.deleteStudentFromGroup(1);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "id = 1 AND group_id IS NULL"));
    }

    @Test
    void findStudentsRelateToGroup_inputGroupId_expectedStudentsRelateToGroup() {
        assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "group_id = 1"));

        List<Student> foundStudents = studentDao.findStudentsRelateToGroup(1);

        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(student1);
        expectedStudents.add(student5);
        expectedStudents.add(student6);

        assertEquals(expectedStudents.size(), foundStudents.size());
        assertEquals(expectedStudents.get(0).getId(), foundStudents.get(0).getId());
        assertEquals(expectedStudents.get(1).getId(), foundStudents.get(1).getId());
        assertEquals(expectedStudents.get(2).getId(), foundStudents.get(2).getId());

        assertEquals(foundStudents.size(), JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "group_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindStudentsFromGroup_inputGroupId_expectedGroupsWereUnbound() {
        assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "group_id = 1"));

        studentDao.unbindStudentsFromGroup(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "users",
                "group_id = 1"));
    }

}
