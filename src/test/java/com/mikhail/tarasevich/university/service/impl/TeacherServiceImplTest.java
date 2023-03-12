package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.exception.ObjectWithSpecifiedIdNotFoundException;
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
    private static final TeacherRequest TEACHER_REQUEST_1 = new TeacherRequest();
    private static final TeacherResponse TEACHER_RESPONSE_WITH_ID_1 = new TeacherResponse();

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
    private static final TeacherRequest TEACHER_REQUEST_2 = new TeacherRequest();
    private static final TeacherResponse TEACHER_RESPONSE_WITH_ID_2 = new TeacherResponse();

    private static final List<Teacher> teacherEntities = new ArrayList<>();
    private static final List<Teacher> teacherEntitiesWithId = new ArrayList<>();
    private static final List<TeacherResponse> teacherResponses = new ArrayList<>();

    static {
        TEACHER_REQUEST_1.setId(0);
        TEACHER_REQUEST_1.setFirstName("firstName1");
        TEACHER_REQUEST_1.setLastName("lastName1");
        TEACHER_REQUEST_1.setEmail("1@email.com");
        TEACHER_REQUEST_1.setPassword("1111");

        TEACHER_RESPONSE_WITH_ID_1.setId(1);
        TEACHER_RESPONSE_WITH_ID_1.setFirstName("firstName1");
        TEACHER_RESPONSE_WITH_ID_1.setLastName("lastName1");
        TEACHER_RESPONSE_WITH_ID_1.setEmail("1@email.com");

        TEACHER_REQUEST_2.setId(0);
        TEACHER_REQUEST_2.setFirstName("firstName2");
        TEACHER_REQUEST_2.setLastName("lastName2");
        TEACHER_REQUEST_2.setEmail("2@email.com");
        TEACHER_REQUEST_2.setPassword("1234");

        TEACHER_RESPONSE_WITH_ID_2.setId(2);
        TEACHER_RESPONSE_WITH_ID_2.setFirstName("firstName2");
        TEACHER_RESPONSE_WITH_ID_2.setLastName("lastName2");
        TEACHER_RESPONSE_WITH_ID_2.setEmail("2@email.com");

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
        doThrow(IncorrectRequestDataException.class).when(validator).validateUserNameNotNullOrEmpty(TeacherWithInvalidLastName);

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

        TeacherResponse TeacherResponse = teacherService.findById(1);

        assertEquals(TEACHER_RESPONSE_WITH_ID_1, TeacherResponse);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(teacherDao, times(1)).findById(1);
    }

    @Test
    void findById_inputNonExistentId_expectedException() {
        when(teacherDao.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> teacherService.findById(100));

        verify(teacherDao, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllStudents() {
        when(teacherDao.findAll()).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findAll();

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherDao, times(1)).findAll();
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
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
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setLastName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");

        doNothing().when(teacherDao).update(TEACHER_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_1);

        teacherService.edit(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateEmail(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validatePassword(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(teacherDao, times(1)).update(TEACHER_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editGeneralUserInfo_inputTeacherRequest_expectedNothing() {
        final Teacher TEACHER_ENTITY_FOR_UPDATE_1 =
                Teacher.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withEmail("update1@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setLastName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");

        doNothing().when(teacherDao).updateGeneralUserInfo(TEACHER_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_1);

        teacherService.editGeneralUserInfo(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateEmail(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(teacherDao, times(1)).updateGeneralUserInfo(TEACHER_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editPassword_inputStudentRequest_expectedNothing() {
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");
        TEACHER_REQUEST_FOR_UPDATE_1.setConfirmPassword("0000");

        when(passwordEncoder.encode("0000")).thenReturn("asdferergergefsv1234");
        doNothing().when(teacherDao).updateUserPassword(1, "asdferergergefsv1234");

        teacherService.editPassword(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(teacherDao, times(1)).updateUserPassword(1, "asdferergergefsv1234");
        verify(passwordEncoder, times(1)).encode("0000");
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
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setLastName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");

        final Teacher TEACHER_ENTITY_FOR_UPDATE_2 =
                Teacher.builder()
                        .withId(2)
                        .withFirstName("update2")
                        .withLastName("update2")
                        .withEmail("update2@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_2 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_2.setId(2);
        TEACHER_REQUEST_FOR_UPDATE_2.setFirstName("update2");
        TEACHER_REQUEST_FOR_UPDATE_2.setLastName("update2");
        TEACHER_REQUEST_FOR_UPDATE_2.setEmail("update2@email.com");
        TEACHER_REQUEST_FOR_UPDATE_2.setPassword("4321");

        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_INCORRECT = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        TEACHER_REQUEST_FOR_UPDATE_INCORRECT.setFirstName("  ");
        TEACHER_REQUEST_FOR_UPDATE_INCORRECT.setLastName("  ");
        TEACHER_REQUEST_FOR_UPDATE_INCORRECT.setEmail("  ");
        TEACHER_REQUEST_FOR_UPDATE_INCORRECT.setPassword("  ");

        final List<TeacherRequest> inputList = new ArrayList<>();
        inputList.add(TEACHER_REQUEST_FOR_UPDATE_1);
        inputList.add(TEACHER_REQUEST_FOR_UPDATE_2);
        inputList.add(TEACHER_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Teacher> listForUpdate = new ArrayList<>();
        listForUpdate.add(TEACHER_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(TEACHER_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException())
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
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(teacherDao.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, teacherService.lastPageNumber());

        verify(teacherDao, times(1)).count();
    }

    @Test
    void login_inputEmailPassword_expectedTrue() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Teacher teacher = Teacher.builder().withEmail("email@mail.com").withPassword("1111").build();

        when(teacherDao.findByName(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(correctPassword, teacher.getPassword())).thenReturn(true);

        assertTrue(teacherService.login(email, correctPassword));

        verify(passwordEncoder, times(1)).matches(correctPassword, teacher.getPassword());
        verify(teacherDao, times(1)).findByName(email);
    }

    @Test
    void login_inputEmailPassword_expectedFalse() {
        final String email = "email@mail.com";
        final String nonCorrectPassword = "1111";
        final Teacher teacher = Teacher.builder().withEmail("email@mail.com").withPassword("1234").build();

        when(teacherDao.findByName(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(nonCorrectPassword, teacher.getPassword())).thenReturn(false);

        assertFalse(teacherService.login(email, nonCorrectPassword));

        verify(passwordEncoder, times(1)).matches(nonCorrectPassword, teacher.getPassword());
        verify(teacherDao, times(1)).findByName(email);
    }

    @Test
    void login_inputNonExistentEmail_expectedFalse() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";

        when(teacherDao.findByName(email)).thenReturn(Optional.empty());

        assertFalse(teacherService.login(email, correctPassword));

        verify(teacherDao, times(1)).findByName(email);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void changeTeacherTeacherTitle_inputTeacherIdTeacherTitleId_expectedNothing() {
        final int teacherId = 1;
        final int teacherTitleId = 1;

        doNothing().when(teacherDao).changeTeacherTitle(teacherId, teacherTitleId);

        assertDoesNotThrow(() -> teacherService.changeTeacherTeacherTitle(teacherId, teacherTitleId));

        verify(teacherDao, times(1)).changeTeacherTitle(teacherId, teacherTitleId);
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void changeTeacherDepartment_inputTeacherIdTeacherTitleId_expectedNothing() {
        final int teacherId = 1;
        final int departmentId = 1;

        doNothing().when(teacherDao).changeDepartment(teacherId, departmentId);

        assertDoesNotThrow(() -> teacherService.changeTeacherDepartment(teacherId, departmentId));

        verify(teacherDao, times(1)).changeDepartment(teacherId, departmentId);
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void subscribeTeacherToGroups_inputTeacherIdGroupIds_expectedNothing() {
        final int teacherId = 1;
        final List <Integer> groupIds = new ArrayList<>();
        groupIds.add(1);
        groupIds.add(2);

        doNothing().when(teacherDao).addUserToGroup(teacherId, groupIds.get(0));
        doNothing().when(teacherDao).addUserToGroup(teacherId, groupIds.get(1));

        assertDoesNotThrow(() -> teacherService.subscribeTeacherToGroups(teacherId, groupIds));

        verify(teacherDao, times(1)).addUserToGroup(teacherId, groupIds.get(0));
        verify(teacherDao, times(1)).addUserToGroup(teacherId, groupIds.get(1));
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void unsubscribeTeacherFromGroups_inputTeacherIdGroupIds_expectedNothing() {
        final int teacherId = 1;
        final List <Integer> groupIds = new ArrayList<>();
        groupIds.add(1);
        groupIds.add(2);

        doNothing().when(teacherDao).deleteTeacherFromGroup(teacherId, groupIds.get(0));
        doNothing().when(teacherDao).deleteTeacherFromGroup(teacherId, groupIds.get(1));

        assertDoesNotThrow(() -> teacherService.unsubscribeTeacherFromGroups(teacherId, groupIds));

        verify(teacherDao, times(1)).deleteTeacherFromGroup(teacherId, groupIds.get(0));
        verify(teacherDao, times(1)).deleteTeacherFromGroup(teacherId, groupIds.get(1));
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void subscribeTeacherToCourses_inputTeacherIdCourseIds_expectedNothing() {
        final int teacherId = 1;
        final List <Integer> courseIds = new ArrayList<>();
        courseIds.add(1);
        courseIds.add(2);

        doNothing().when(teacherDao).addTeacherToCourse(teacherId, courseIds.get(0));
        doNothing().when(teacherDao).addTeacherToCourse(teacherId, courseIds.get(1));

        assertDoesNotThrow(() -> teacherService.subscribeTeacherToCourses(teacherId, courseIds));

        verify(teacherDao, times(1)).addTeacherToCourse(teacherId, courseIds.get(0));
        verify(teacherDao, times(1)).addTeacherToCourse(teacherId, courseIds.get(1));
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(groupDao);
    }

    @Test
    void unsubscribeTeacherToCourses_inputTeacherIdCourseIds_expectedNothing() {
        final int teacherId = 1;
        final List <Integer> courseIds = new ArrayList<>();
        courseIds.add(1);
        courseIds.add(2);

        doNothing().when(teacherDao).deleteTeacherFromCourse(teacherId, courseIds.get(0));
        doNothing().when(teacherDao).deleteTeacherFromCourse(teacherId, courseIds.get(1));

        assertDoesNotThrow(() -> teacherService.unsubscribeTeacherFromCourses(teacherId, courseIds));

        verify(teacherDao, times(1)).deleteTeacherFromCourse(teacherId, courseIds.get(0));
        verify(teacherDao, times(1)).deleteTeacherFromCourse(teacherId, courseIds.get(1));
        verifyNoInteractions(lessonDao);
        verifyNoInteractions(groupDao);
    }

}
