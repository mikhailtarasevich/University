package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.TeacherMapper;
import com.mikhail.tarasevich.university.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @InjectMocks
    TeacherServiceImpl teacherService;
    @Mock
    TeacherDao teacherDao;
    @Mock
    LessonDao lessonDao;
    @Mock
    CourseDao courseDao;
    @Mock
    GroupDao groupDao;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TeacherMapper mapper;
    @Mock
    UserValidator<TeacherRequest> validator;

    private static final Teacher TEACHER_ENTITY_1 =
            Teacher.builder()
                    .withFirstName("firstName1")
                    .withLastName("lastName1")
                    .withEmail("1@email.com")
                    .build();
    private static final Teacher TEACHER_ENTITY_WITH_ID_1 =
            Teacher.builder()
                    .withId(1)
                    .withFirstName("firstName1")
                    .withLastName("lastName1")
                    .withEmail("1@email.com")
                    .build();
    private static final TeacherRequest TEACHER_REQUEST_1 =
            new TeacherRequest(0, "firstName1", "lastName1",
                    null, "1@email.com", null, null, null, null, null);
    private static final TeacherResponse TEACHER_RESPONSE_WITH_ID_1 =
            new TeacherResponse(1, "firstName1", "lastName1",
                    null, "1@email.com", null, null, null, null);

    private static final Teacher TEACHER_ENTITY_2 =
            Teacher.builder()
                    .withFirstName("firstName2")
                    .withLastName("lastName2")
                    .withEmail("2@email.com")
                    .build();
    private static final Teacher TEACHER_ENTITY_WITH_ID_2 =
            Teacher.builder()
                    .withId(2)
                    .withFirstName("firstName2")
                    .withLastName("lastName2")
                    .withEmail("2@email.com")
                    .build();
    private static final TeacherRequest TEACHER_REQUEST_2 =
            new TeacherRequest(0, "firstName2", "lastName2",
                    null, "2@email.com", null, null, null, null, null);
    private static final TeacherResponse TEACHER_RESPONSE_WITH_ID_2 =
            new TeacherResponse(2, "firstName2", "lastName2",
                    null, "2@email.com", null, null, null, null);

    private final List<Teacher> teacherEntities = new ArrayList<>();
    private final List<Teacher> teacherEntitiesWithId = new ArrayList<>();
    private final List<TeacherResponse> teacherResponses = new ArrayList<>();

    {
        teacherEntities.add(TEACHER_ENTITY_1);
        teacherEntities.add(TEACHER_ENTITY_2);

        teacherEntitiesWithId.add(TEACHER_ENTITY_WITH_ID_1);
        teacherEntitiesWithId.add(TEACHER_ENTITY_WITH_ID_2);

        teacherResponses.add(TEACHER_RESPONSE_WITH_ID_1);
        teacherResponses.add(TEACHER_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputTeacherRequest_expectedTeacherResponseWithId() {
        when(mapper.toEntity(TEACHER_REQUEST_1)).thenReturn(TEACHER_ENTITY_1);
        when(teacherDao.save(TEACHER_ENTITY_1)).thenReturn(TEACHER_ENTITY_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);

        TeacherResponse teacherResponse = teacherService.register(TEACHER_REQUEST_1);

        assertEquals(TEACHER_RESPONSE_WITH_ID_1, teacherResponse);
        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_1);
        verify(teacherDao, times(1)).save(TEACHER_ENTITY_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
    }

    @Test
    void register_inputTeacherRequestWithExistingEmailInDB_expectedException() {
        when(teacherDao.findByName(TEACHER_REQUEST_1.getEmail())).thenReturn(Optional.of(Teacher.builder().build()));

        assertThrows(EmailAlreadyExistsException.class, () -> teacherService.register(TEACHER_REQUEST_1));

        verify(mapper, times(0)).toEntity(TEACHER_REQUEST_1);
        verify(teacherDao, times(0)).save(TEACHER_ENTITY_1);
        verify(mapper, times(0)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(validator, times(0)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
    }

    @Test
    void registerAllTeachers_inputTeacherRequestListWithRepeatableTeacher_expectedNothing() {
        TeacherRequest TeacherWithExistingEmail = new TeacherRequest();
        TeacherWithExistingEmail.setEmail("exist@email.com");

        TeacherRequest TeacherWithInvalidLastName = new TeacherRequest();
        TeacherWithExistingEmail.setFirstName("Helga");
        TeacherWithExistingEmail.setLastName("  ");

        final List<TeacherRequest> listForRegister = new ArrayList<>();

        listForRegister.add(TEACHER_REQUEST_1);
        listForRegister.add(TEACHER_REQUEST_2);
        listForRegister.add(TeacherWithExistingEmail);
        listForRegister.add(TeacherWithInvalidLastName);

        when(teacherDao.findByName(TEACHER_REQUEST_1.getEmail())).thenReturn(Optional.empty());
        when(teacherDao.findByName(TEACHER_REQUEST_2.getEmail())).thenReturn(Optional.empty());
        when(teacherDao.findByName(TeacherWithInvalidLastName.getEmail())).thenReturn(Optional.empty());
        when(teacherDao.findByName(TeacherWithExistingEmail.getEmail())).thenReturn(Optional.of(Teacher.builder().build()));
        when(mapper.toEntity(TEACHER_REQUEST_1)).thenReturn(TEACHER_ENTITY_1);
        when(mapper.toEntity(TEACHER_REQUEST_2)).thenReturn(TEACHER_ENTITY_2);
        doNothing().when(teacherDao).saveAll(teacherEntities);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_2);
        doThrow(IncorrectRequestData.class).when(validator).validateUserNameNotNullOrEmpty(TeacherWithInvalidLastName);

        teacherService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_1);
        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_2);
        verify(teacherDao, times(1)).saveAll(teacherEntities);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_2);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TeacherWithInvalidLastName);
        verify(validator, times(0)).validateUserNameNotNullOrEmpty(TeacherWithExistingEmail);
    }

    @Test
    void findById_inputIntId_expectedFoundTeacher() {
        when(teacherDao.findById(1)).thenReturn(Optional.of(TEACHER_ENTITY_WITH_ID_1));
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);

        Optional<TeacherResponse> TeacherResponse = teacherService.findById(1);

        assertEquals(Optional.of(TEACHER_RESPONSE_WITH_ID_1), TeacherResponse);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(teacherDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundTeachersFromPageOne() {
        when(teacherDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(teacherEntitiesWithId);
        when(teacherDao.count()).thenReturn(2L);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findAll("1");

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(teacherDao, times(1)).count();
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputTeacherRequest_expectedNothing() {
        final Teacher TEACHER_ENTITY_FOR_UPDATE_1 =
                Teacher.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withEmail("update1@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 =
                new TeacherRequest(1, "update1", "update1",
                        null, "update1@email.com", null, null, null, null, null);

        doNothing().when(teacherDao).update(TEACHER_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_1);

        teacherService.edit(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(teacherDao, times(1)).update(TEACHER_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputTeacherRequestListWhereOneTeacherHasIncorrectName_expectedNothing() {
        final Teacher TEACHER_ENTITY_FOR_UPDATE_1 =
                Teacher.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withEmail("update1@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 =
                new TeacherRequest(1, "update1", "update1",
                        null, "update1@email.com", null, null, null, null, null);
        final Teacher TEACHER_ENTITY_FOR_UPDATE_2 =
                Teacher.builder()
                        .withId(2)
                        .withFirstName("update2")
                        .withLastName("update2")
                        .withEmail("update2@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_2 =
                new TeacherRequest(2, "update2", "update2",
                        null, "update2@email.com", null, null, null, null, null);
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_INCORRECT =
                new TeacherRequest(3, " ", " ",
                        null, " ", null, null, null, null, null);

        final List<TeacherRequest> inputList = new ArrayList<>();
        inputList.add(TEACHER_REQUEST_FOR_UPDATE_1);
        inputList.add(TEACHER_REQUEST_FOR_UPDATE_2);
        inputList.add(TEACHER_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Teacher> listForUpdate = new ArrayList<>();
        listForUpdate.add(TEACHER_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(TEACHER_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData())
                .when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_2)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_2);

        doNothing().when(teacherDao).updateAll(listForUpdate);

        teacherService.editAll(inputList);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_2);
        verify(teacherDao, times(1)).updateAll(listForUpdate);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputTeacherId_expectedSuccessDelete() {
        int id = 1;

        when(teacherDao.findById(id)).thenReturn(Optional.of(TEACHER_ENTITY_1));
        doNothing().when(groupDao).unbindGroupsFromTeacher(id);
        doNothing().when(lessonDao).unbindLessonsFromTeacher(id);
        doNothing().when(courseDao).unbindCoursesFromTeacher(id);
        when(teacherDao.deleteById(id)).thenReturn(true);

        boolean result = teacherService.deleteById(1);

        assertTrue(result);
        verify(teacherDao, times(1)).findById(id);
        verify(teacherDao, times(1)).deleteById(id);
        verify(groupDao, times(1)).unbindGroupsFromTeacher(id);
        verify(lessonDao, times(1)).unbindLessonsFromTeacher(id);
        verify(courseDao, times(1)).unbindCoursesFromTeacher(id);
    }

    @Test
    void deleteById_inputTeacherId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(teacherDao.findById(id)).thenReturn(Optional.empty());

        boolean result = teacherService.deleteById(1);

        assertFalse(result);
        verify(teacherDao, times(1)).findById(id);
        verifyNoInteractions(groupDao);
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(courseDao);
        verify(teacherDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputTeachersIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupDao).unbindGroupsFromTeacher(1);
        doNothing().when(lessonDao).unbindLessonsFromTeacher(1);
        doNothing().when(courseDao).unbindCoursesFromTeacher(1);
        doNothing().when(groupDao).unbindGroupsFromTeacher(2);
        doNothing().when(lessonDao).unbindLessonsFromTeacher(2);
        doNothing().when(courseDao).unbindCoursesFromTeacher(2);
        when(teacherDao.deleteByIds(ids)).thenReturn(true);

        boolean result = teacherService.deleteByIds(ids);

        assertTrue(result);
        verify(teacherDao, times(1)).deleteByIds(ids);
        verify(groupDao, times(1)).unbindGroupsFromTeacher(1);
        verify(lessonDao, times(1)).unbindLessonsFromTeacher(1);
        verify(courseDao, times(1)).unbindCoursesFromTeacher(1);
        verify(groupDao, times(1)).unbindGroupsFromTeacher(2);
        verify(lessonDao, times(1)).unbindLessonsFromTeacher(2);
        verify(courseDao, times(1)).unbindCoursesFromTeacher(2);
    }

    @Test
    void deleteByIds_inputTeachersIds_expectedUnsuccessfulDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupDao).unbindGroupsFromTeacher(1);
        doNothing().when(lessonDao).unbindLessonsFromTeacher(1);
        doNothing().when(courseDao).unbindCoursesFromTeacher(1);
        doNothing().when(groupDao).unbindGroupsFromTeacher(2);
        doNothing().when(lessonDao).unbindLessonsFromTeacher(2);
        doNothing().when(courseDao).unbindCoursesFromTeacher(2);
        when(teacherDao.deleteByIds(ids)).thenReturn(false);

        boolean result = teacherService.deleteByIds(ids);

        assertFalse(result);
        verify(groupDao, times(1)).unbindGroupsFromTeacher(1);
        verify(lessonDao, times(1)).unbindLessonsFromTeacher(1);
        verify(courseDao, times(1)).unbindCoursesFromTeacher(1);
        verify(groupDao, times(1)).unbindGroupsFromTeacher(2);
        verify(lessonDao, times(1)).unbindLessonsFromTeacher(2);
        verify(courseDao, times(1)).unbindCoursesFromTeacher(2);
    }

    @Test
    void subscribeUserToGroup_inputTeacherIdGroupId_expectedNothing() {
        doNothing().when(teacherDao).addUserToGroup(1, 1);

        teacherService.subscribeUserToGroup(1, 1);

        verify(teacherDao, times(1)).addUserToGroup(1, 1);
    }

    @Test
    void unsubscribeTeacherFromGroup_inputTeacherIdGroupId_expectedNothing() {
        doNothing().when(teacherDao).deleteTeacherFromGroup(1, 1);

        teacherService.unsubscribeTeacherFromGroup(1, 1);

        verify(teacherDao, times(1)).deleteTeacherFromGroup(1, 1);
    }

    @Test
    void subscribeTeacherToCourse_inputTeacherIdCourseId_expectedNothing() {
        doNothing().when(teacherDao).addTeacherToCourse(1, 1);

        teacherService.subscribeTeacherToCourse(1, 1);

        verify(teacherDao, times(1)).addTeacherToCourse(1, 1);
    }

    @Test
    void unsubscribeTeacherFromCourse_inputTeacherIdCourseId_expectedNothing() {
        doNothing().when(teacherDao).deleteTeacherFromCourse(1, 1);

        teacherService.unsubscribeTeacherFromCourse(1, 1);

        verify(teacherDao, times(1)).deleteTeacherFromCourse(1, 1);
    }

    @Test
    void findTeachersRelateToGroup_inputGroupId_expectedTeachersList() {
        int groupId = 1;

        when(teacherDao.findTeachersRelateToGroup(groupId)).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findTeachersRelateToGroup(groupId);

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherDao, times(1)).findTeachersRelateToGroup(groupId);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void findTeachersRelateToCourse_inputCourseId_expectedTeachersList() {
        int courseId = 1;

        when(teacherDao.findTeachersRelateToCourse(courseId)).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findTeachersRelateToCourse(courseId);

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherDao, times(1)).findTeachersRelateToCourse(courseId);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void findTeachersRelateToDepartment_inputDepartmentId_expectedTeachersList() {
        int departmentId = 1;

        when(teacherDao.findTeachersRelateToDepartment(departmentId)).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findTeachersRelateToDepartment(departmentId);

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherDao, times(1)).findTeachersRelateToDepartment(departmentId);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void login_inputEmailPassword_expectedTrue() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Teacher teacher = Teacher.builder().withEmail("email@mail.com").withPassword("1111").build();

        when(teacherDao.findByName(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(correctPassword, teacher.getPassword())).thenReturn(true);

        assertTrue(teacherService.login(email, correctPassword));
    }

    @Test
    void login_inputEmailPassword_expectedFalse() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Teacher teacher = Teacher.builder().withEmail("email@mail.com").withPassword("1234").build();

        when(teacherDao.findByName(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(correctPassword, teacher.getPassword())).thenReturn(false);

        assertFalse(teacherService.login(email, correctPassword));
    }

}
