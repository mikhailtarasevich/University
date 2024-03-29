package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.config.SpringTestConfig;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Student;
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
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Group group1 = Group.builder()
            .withId(1)
            .withName("g1")
            .withFaculty(Faculty.builder()
                    .withId(1)
                    .withName("Applied Mathematics and Informatics")
                    .withDescription("without description")
                    .build())
            .withHeadStudent(Student.builder()
                    .withId(1)
                    .withFirstName("Tom")
                    .withLastName("Robinson")
                    .withGender(Gender.MALE)
                    .withEmail("tomrobinson@gmail.com")
                    .withPassword("1234")
                    .withGroup(Group.builder().withId(1).build())
                    .build())
            .withEducationForm(EducationForm.builder()
                    .withId(2)
                    .withName("Distance education")
                    .build())
            .build();

    private static final Group group2 = Group.builder()
            .withId(2)
            .withName("g2")
            .withFaculty(Faculty.builder()
                    .withId(2)
                    .withName("Mechanics and Mathematical Modeling")
                    .withDescription("without description")
                    .build())
            .withHeadStudent(Student.builder()
                    .withId(2)
                    .withFirstName("Rory")
                    .withLastName("McDonald")
                    .withGender(Gender.MALE)
                    .withEmail("mcdonald@yandex.ru")
                    .withPassword("1111")
                    .withGroup(Group.builder().withId(2).build())
                    .build())
            .withEducationForm(EducationForm.builder()
                    .withId(2)
                    .withName("Distance education")
                    .build())
            .build();

    private static final Group group3 = Group.builder()
            .withId(3)
            .withName("g3")
            .withFaculty(Faculty.builder()
                    .withId(3)
                    .withName("Statistics")
                    .withDescription("without description")
                    .build())
            .withHeadStudent(Student.builder()
                    .withId(3)
                    .withFirstName("Kate")
                    .withLastName("Austin")
                    .withGender(Gender.FEMALE)
                    .withEmail("kitty@yahoo.com")
                    .withPassword("kate12#")
                    .withGroup(Group.builder().withId(3).build())
                    .build())
            .withEducationForm(EducationForm.builder()
                    .withId(1)
                    .withName("Full-time education")
                    .build())
            .build();

    private static final Group group4 = Group.builder()
            .withId(4)
            .withName("g4")
            .withFaculty(Faculty.builder()
                    .withId(4)
                    .withName("Mathematics and Computer Sciences")
                    .withDescription("without description")
                    .build())
            .withHeadStudent(Student.builder()
                    .withId(4)
                    .withFirstName("Amanda")
                    .withLastName("Johnson")
                    .withGender(Gender.FEMALE)
                    .withEmail("amandaj@gmail.com")
                    .withPassword("0000")
                    .withGroup(Group.builder().withId(4).build())
                    .build())
            .withEducationForm(EducationForm.builder()
                    .withId(1)
                    .withName("Full-time education")
                    .build())
            .build();

    private static final Group group5 = Group.builder()
            .withId(5)
            .withName("test group 1")
            .withFaculty(Faculty.builder()
                    .withId(0)
                    .withName(null)
                    .withDescription(null)
                    .build())
            .withHeadStudent(Student.builder()
                    .withId(0)
                    .withFirstName(null)
                    .withLastName(null)
                    .withGender(Gender.MALE)
                    .withEmail(null)
                    .withPassword(null)
                    .withGroup(Group.builder()
                            .withId(0)
                            .withName(null)
                            .withFaculty(null)
                            .withEducationForm(null)
                            .build())
                    .build())
            .withEducationForm(EducationForm.builder()
                    .withId(0)
                    .withName(null)
                    .build())
            .build();

    private static final Group group6 = Group.builder()
            .withId(6)
            .withName("test group 2")
            .withFaculty(Faculty.builder()
                    .withId(0)
                    .withName(null)
                    .withDescription(null)
                    .build())
            .withHeadStudent(Student.builder()
                    .withId(0)
                    .withFirstName(null)
                    .withLastName(null)
                    .withGender(Gender.MALE)
                    .withEmail(null)
                    .withPassword(null)
                    .withGroup(Group.builder()
                            .withId(0)
                            .withName(null)
                            .withFaculty(null)
                            .withEducationForm(null)
                            .build())
                    .build())
            .withEducationForm(EducationForm.builder()
                    .withId(0)
                    .withName(null)
                    .build())
            .build();

    List<Group> groups = new ArrayList<>();

    {
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);
        groups.add(group5);
        groups.add(group6);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_inputEntity_expectedEntityWithId() {
        final Group entityForSave = Group.builder()
                .withName("test")
                .withFaculty(Faculty.builder()
                        .withId(1)
                        .build())
                .withHeadStudent(Student.builder()
                        .withId(1)
                        .build())
                .withEducationForm(EducationForm.builder()
                        .withId(2)
                        .build())
                .build();

        final Group expectedEntity = Group.builder()
                .withId(7)
                .withName("test")
                .withFaculty(Faculty.builder()
                        .withId(1)
                        .build())
                .withHeadStudent(Student.builder()
                        .withId(1)
                        .build())
                .withEducationForm(EducationForm.builder()
                        .withId(2)
                        .build())
                .build();

        Group savedEntity = groupRepository.save(entityForSave);

        assertEquals(expectedEntity.getId(), savedEntity.getId());
        assertEquals(expectedEntity.getName(), savedEntity.getName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void saveAll_inputEntities_expectedEntitiesAddedToDb() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 7 OR id = 8"));

        Group groupForSave1 = Group.builder().withName("groupForSave1").build();
        Group groupForSave2 = Group.builder().withName("groupForSave2").build();

        List<Group> entitiesForSave = new ArrayList<>();
        entitiesForSave.add(groupForSave1);
        entitiesForSave.add(groupForSave2);
        groupRepository.saveAll(entitiesForSave);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 7 AND name = 'groupForSave1'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 8 AND name = 'groupForSave2'"));
    }

    @Test
    void findById_inputEntityId_expectedEntityReturnedFromDB() {
        Optional<Group> foundEntity = groupRepository.findById(1);

        assertTrue(foundEntity.isPresent());
        assertEquals(group1.getId(), foundEntity.get().getId());
        assertEquals(group1.getName(), foundEntity.get().getName());
    }

    @Test
    void findByName_inputEntityName_expectedEntitiesListReturnedFromDB() {
        Optional<Group> foundEntity = groupRepository.findByName("g1");

        assertTrue(foundEntity.isPresent());
        assertEquals(group1.getId(), foundEntity.get().getId());
        assertEquals(group1.getName(), foundEntity.get().getName());
    }

    @Test
    void findAll_inputNothing_expectedAllEntitiesFromDB() {
        List<Group> foundEntities = groupRepository.findAll();

        assertEquals(groups.size(), foundEntities.size());
        assertEquals(groups.get(0).getId(), foundEntities.get(0).getId());
        assertEquals(groups.get(0).getName(), foundEntities.get(0).getName());
        assertEquals(groups.get(1).getId(), foundEntities.get(1).getId());
        assertEquals(groups.get(1).getName(), foundEntities.get(1).getName());
        assertEquals(groups.get(2).getId(), foundEntities.get(2).getId());
        assertEquals(groups.get(2).getName(), foundEntities.get(2).getName());
        assertEquals(groups.get(3).getId(), foundEntities.get(3).getId());
        assertEquals(groups.get(3).getName(), foundEntities.get(3).getName());
        assertEquals(groups.get(4).getId(), foundEntities.get(4).getId());
        assertEquals(groups.get(4).getName(), foundEntities.get(4).getName());
        assertEquals(groups.get(5).getId(), foundEntities.get(5).getId());
        assertEquals(groups.get(5).getName(), foundEntities.get(5).getName());
    }

    @Test
    void findAllPageable_inputPageNumber_expectedEntitiesFromThePage() {
        List<Group> foundEntities = groupRepository.findAll(PageRequest.of(1, 2)).toList();
        List<Group> expectedEntities = new ArrayList<>();
        expectedEntities.add(group3);
        expectedEntities.add(group4);

        assertEquals(foundEntities.size(), expectedEntities.size());
        assertEquals(foundEntities.get(0).getId(), expectedEntities.get(0).getId());
        assertEquals(foundEntities.get(0).getName(), expectedEntities.get(0).getName());
        assertEquals(foundEntities.get(1).getId(), expectedEntities.get(1).getId());
        assertEquals(foundEntities.get(1).getName(), expectedEntities.get(1).getName());
    }

    @Test
    void count_inputNothing_expectedQuantityOfRowsInTable() {
        long rows = groupRepository.count();

        long expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        assertEquals(expectedRows, rows);
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_inputUpdatedEntity_expectedEntityInDBWasUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND name = 'Updated' AND faculty_id = 2 AND head_user_id = 2 AND " +
                        "education_form_id = 2"));

        Group e = Group.builder()
                .withId(1)
                .withName("Updated")
                .withFaculty(Faculty.builder()
                        .withId(2)
                        .build())
                .withHeadStudent(Student.builder()
                        .withId(2)
                        .build())
                .withEducationForm(EducationForm.builder()
                        .withId(2)
                        .build())
                .build();

        groupRepository.update(e.getId(), e.getName(), e.getFaculty().getId(), e.getHeadStudent().getId(),
                e.getEducationForm().getId());

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND name = 'Updated' AND faculty_id = 2 AND head_user_id = 2 AND " +
                        "education_form_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAll_inputUpdatedEntities_expectedEntitiesInDBWereUpdated() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND name = 'Updated' AND faculty_id = 2 AND head_user_id = 2 AND " +
                        "education_form_id = 2"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 2 AND name = 'Updated' AND faculty_id = 1 AND head_user_id = 1 AND " +
                        "education_form_id = 1"));

        final Group entityForUpdate1 = Group.builder()
                .withId(1)
                .withName("Updated")
                .withFaculty(Faculty.builder()
                        .withId(2)
                        .build())
                .withHeadStudent(Student.builder()
                        .withId(2)
                        .build())
                .withEducationForm(EducationForm.builder()
                        .withId(2)
                        .build())
                .build();

        final Group entityForUpdate2 = Group.builder()
                .withId(2)
                .withName("Updated")
                .withFaculty(Faculty.builder()
                        .withId(1)
                        .build())
                .withHeadStudent(Student.builder()
                        .withId(1)
                        .build())
                .withEducationForm(EducationForm.builder()
                        .withId(1)
                        .build())
                .build();

        List<Group> groupsForUpdate = new ArrayList<>();
        groupsForUpdate.add(entityForUpdate1);
        groupsForUpdate.add(entityForUpdate2);

        groupRepository.saveAll(groupsForUpdate);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND name = 'Updated' AND faculty_id = 2 AND head_user_id = 2 AND " +
                        "education_form_id = 2"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 2 AND name = 'Updated' AND faculty_id = 1 AND head_user_id = 1 AND " +
                        "education_form_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_inputIdOfDeletedEntity_expectedDeletedEntityIsAbsentInDB() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 6"));

        groupRepository.deleteById(6);

        int expectedSize = 5;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 6"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIds_inputIdsOfDeletedEntities_expectedDeletedEntitiesAreAbsentInDB() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 5 OR id = 6"));

        Set<Integer> ids = new HashSet<>();
        ids.add(5);
        ids.add(6);

        groupRepository.deleteAllByIdInBatch(ids);

        int expectedSize = 4;

        assertEquals(expectedSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 6 OR id = 7"));
    }

    @Test
    void findGroupsRelateToTeacher_inputTeacherId_expectedGroupsRelateToTeacher() {
        List<Group> foundEntities = groupRepository.findGroupByTeachersId(5);
        System.out.println(foundEntities.size());
        assertEquals(group1.getId(), foundEntities.get(0).getId());
        assertEquals(group1.getName(), foundEntities.get(0).getName());
        assertEquals(group2.getId(), foundEntities.get(1).getId());
        assertEquals(group2.getName(), foundEntities.get(1).getName());
        assertEquals(2, foundEntities.size());
    }

    @Test
    void findGroupsNotRelateToTeacher_inputTeacherId_expectedEntitiesFromDB() {
        List<Group> foundEntities = groupRepository.findGroupsNotRelateToTeacher(5);

        List<Group> expectedEntities = new ArrayList<>();
        expectedEntities.add(group3);
        expectedEntities.add(group4);
        expectedEntities.add(group5);
        expectedEntities.add(group6);

        assertEquals(foundEntities.size(), expectedEntities.size());
        assertEquals(foundEntities.get(0).getId(), expectedEntities.get(0).getId());
        assertEquals(foundEntities.get(0).getName(), expectedEntities.get(0).getName());
        assertEquals(foundEntities.get(1).getId(), expectedEntities.get(1).getId());
        assertEquals(foundEntities.get(1).getName(), expectedEntities.get(1).getName());
        assertEquals(foundEntities.get(2).getId(), expectedEntities.get(2).getId());
        assertEquals(foundEntities.get(2).getName(), expectedEntities.get(2).getName());
        assertEquals(foundEntities.get(3).getId(), expectedEntities.get(3).getId());
        assertEquals(foundEntities.get(3).getName(), expectedEntities.get(3).getName());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeFaculty_inputGroupIdAndFacultyId_expectedFacultyWasChangedInDB() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND faculty_id = 4"));

        groupRepository.changeFaculty(1, 4);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND faculty_id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeHeadUser_inputGroupIdAndHeadUSerId_expectedHeadUserIdWasChangedInDB() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND head_user_id = 5"));

        groupRepository.changeHeadUser(1, 5);


        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND head_user_id = 5"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void changeEducationForm_inputGroupIdAndEducationFormId_expectedEducationFormWasChangedInDB() {
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND education_form_id = 4"));

        groupRepository.changeEducationForm(1, 4);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "id = 1 AND education_form_id = 4"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindGroupsFromStudent_inputStudentId_expectedGroupsWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "head_user_id = 1"));

        groupRepository.unbindGroupsFromStudent(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "head_user_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindGroupsFromTeacher_inputTeacherId_expectedGroupsWereUnbound() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 6"));

        groupRepository.unbindGroupsFromTeacher(6);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_groups",
                "user_id = 6"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindGroupsFromEducationForm_inputEducationFormId_expectedGroupsWereUnbound() {
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "education_form_id = 1"));

        groupRepository.unbindGroupsFromEducationForm(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "education_form_id = 1"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unbindGroupsFromFaculty_inputFacultyId_expectedGroupsWereUnbound() {
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "faculty_id = 1"));

        groupRepository.unbindGroupsFromFaculty(1);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups",
                "faculty_id = 1"));
    }

}
