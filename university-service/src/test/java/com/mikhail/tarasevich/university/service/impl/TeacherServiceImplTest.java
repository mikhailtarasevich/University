package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.repository.CourseRepository;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.RoleRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.TeacherMapper;
import com.mikhail.tarasevich.university.service.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    TeacherRepository teacherRepository;
    @Mock
    LessonRepository lessonRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    GroupRepository groupRepository;
    @Mock
    RoleRepository roleRepository;
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
        when(teacherRepository.save(TEACHER_ENTITY_1)).thenReturn(TEACHER_ENTITY_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
        doNothing().when(roleRepository).addRoleForUser(1, 2);

        TeacherResponse teacherResponse = teacherService.register(TEACHER_REQUEST_1);

        assertEquals(TEACHER_RESPONSE_WITH_ID_1, teacherResponse);
        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_1);
        verify(teacherRepository, times(1)).save(TEACHER_ENTITY_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
        verify(roleRepository, times(1)).addRoleForUser(1, 2);
    }

    @Test
    void register_inputTeacherRequestWithExistingEmailInDB_expectedException() {
        when(teacherRepository.findByEmail(TEACHER_REQUEST_1.getEmail())).thenReturn(Optional.of(Teacher.builder().build()));

        assertThrows(EmailAlreadyExistsException.class, () -> teacherService.register(TEACHER_REQUEST_1));

        verify(mapper, times(0)).toEntity(TEACHER_REQUEST_1);
        verify(teacherRepository, times(0)).save(TEACHER_ENTITY_1);
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

        when(teacherRepository.findByEmail(TEACHER_REQUEST_1.getEmail())).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail(TEACHER_REQUEST_2.getEmail())).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail(TeacherWithInvalidLastName.getEmail())).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail(TeacherWithExistingEmail.getEmail())).thenReturn(Optional.of(Teacher.builder().build()));
        when(mapper.toEntity(TEACHER_REQUEST_1)).thenReturn(TEACHER_ENTITY_1);
        when(mapper.toEntity(TEACHER_REQUEST_2)).thenReturn(TEACHER_ENTITY_2);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_2);
        doThrow(IncorrectRequestDataException.class).when(validator).validateUserNameNotNullOrEmpty(TeacherWithInvalidLastName);

        teacherService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_1);
        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_2);
        verify(teacherRepository, times(1)).saveAll(teacherEntities);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_2);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TeacherWithInvalidLastName);
        verify(validator, times(0)).validateUserNameNotNullOrEmpty(TeacherWithExistingEmail);
    }

    @Test
    void findById_inputIntId_expectedFoundTeacher() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(TEACHER_ENTITY_WITH_ID_1));
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);

        TeacherResponse TeacherResponse = teacherService.findById(1);

        assertEquals(TEACHER_RESPONSE_WITH_ID_1, TeacherResponse);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(teacherRepository, times(1)).findById(1);
    }

    @Test
    void findById_inputNonExistentId_expectedException() {
        when(teacherRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> teacherService.findById(100));

        verify(teacherRepository, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllStudents() {
        when(teacherRepository.findAll()).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findAll();

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundTeachersFromPageOne() {
        Page<Teacher> pageOfTeacherEntitiesWithId = new PageImpl<>(teacherEntitiesWithId);

        when(teacherRepository
                .findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id"))))
                .thenReturn(pageOfTeacherEntitiesWithId);
        when(teacherRepository.count()).thenReturn(2L);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findAll("1");

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherRepository, times(1)).findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id")));
        verify(teacherRepository, times(1)).count();
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
                        .withGender(Gender.MALE)
                        .withEmail("update1@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setLastName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setGender(Gender.MALE);
        TEACHER_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");

        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_1);

        teacherService.edit(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateEmail(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validatePassword(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(teacherRepository, times(1)).save(TEACHER_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editGeneralUserInfo_inputTeacherRequest_expectedNothing() {
        final Teacher TEACHER_ENTITY_FOR_UPDATE_1 =
                Teacher.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withGender(Gender.MALE)
                        .withEmail("update1@email.com")
                        .build();
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setLastName("update1");
        TEACHER_REQUEST_FOR_UPDATE_1.setGender(Gender.MALE);
        TEACHER_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");

        doNothing().when(teacherRepository).updateGeneralInfo(TEACHER_ENTITY_FOR_UPDATE_1.getId(),
                TEACHER_ENTITY_FOR_UPDATE_1.getFirstName(), TEACHER_ENTITY_FOR_UPDATE_1.getLastName(),
                TEACHER_ENTITY_FOR_UPDATE_1.getGender(), TEACHER_ENTITY_FOR_UPDATE_1.getEmail());
        when(mapper.toEntity(TEACHER_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_ENTITY_FOR_UPDATE_1);

        teacherService.editGeneralUserInfo(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateEmail(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(teacherRepository, times(1)).updateGeneralInfo(TEACHER_ENTITY_FOR_UPDATE_1.getId(),
                TEACHER_ENTITY_FOR_UPDATE_1.getFirstName(), TEACHER_ENTITY_FOR_UPDATE_1.getLastName(),
                TEACHER_ENTITY_FOR_UPDATE_1.getGender(), TEACHER_ENTITY_FOR_UPDATE_1.getEmail());
    }

    @Test
    void editPassword_inputStudentRequest_expectedNothing() {
        final TeacherRequest TEACHER_REQUEST_FOR_UPDATE_1 = new TeacherRequest();
        TEACHER_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_REQUEST_FOR_UPDATE_1.setPassword("0000");
        TEACHER_REQUEST_FOR_UPDATE_1.setConfirmPassword("0000");

        when(passwordEncoder.encode("0000")).thenReturn("asdferergergefsv1234");
        doNothing().when(teacherRepository).updateUserPassword(1, "asdferergergefsv1234");

        teacherService.editPassword(TEACHER_REQUEST_FOR_UPDATE_1);

        verify(teacherRepository, times(1)).updateUserPassword(1, "asdferergergefsv1234");
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

        teacherService.editAll(inputList);

        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(TEACHER_REQUEST_FOR_UPDATE_2);
        verify(teacherRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateUserNameNotNullOrEmpty(TEACHER_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputTeacherId_expectedSuccessDelete() {
        int id = 1;

        when(teacherRepository.findById(id)).thenReturn(Optional.of(TEACHER_ENTITY_1));
        doNothing().when(groupRepository).unbindGroupsFromTeacher(id);
        doNothing().when(lessonRepository).unbindLessonsFromTeacher(id);
        doNothing().when(courseRepository).unbindCoursesFromTeacher(id);
        doNothing().when(roleRepository).unbindRoleFromUser(id);
        doNothing().when(teacherRepository).deleteById(id);

        boolean result = teacherService.deleteById(1);

        assertTrue(result);
        verify(teacherRepository, times(1)).findById(id);
        verify(teacherRepository, times(1)).deleteById(id);
        verify(groupRepository, times(1)).unbindGroupsFromTeacher(id);
        verify(lessonRepository, times(1)).unbindLessonsFromTeacher(id);
        verify(courseRepository, times(1)).unbindCoursesFromTeacher(id);
        verify(roleRepository, times(1)).unbindRoleFromUser(id);
    }

    @Test
    void deleteById_inputTeacherId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = teacherService.deleteById(1);

        assertFalse(result);
        verify(teacherRepository, times(1)).findById(id);
        verifyNoInteractions(groupRepository);
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(courseRepository);
        verify(teacherRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputTeachersIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupRepository).unbindGroupsFromTeacher(1);
        doNothing().when(lessonRepository).unbindLessonsFromTeacher(1);
        doNothing().when(courseRepository).unbindCoursesFromTeacher(1);
        doNothing().when(roleRepository).unbindRoleFromUser(1);
        doNothing().when(groupRepository).unbindGroupsFromTeacher(2);
        doNothing().when(lessonRepository).unbindLessonsFromTeacher(2);
        doNothing().when(courseRepository).unbindCoursesFromTeacher(2);
        doNothing().when(roleRepository).unbindRoleFromUser(2);
        doNothing().when(teacherRepository).deleteAllByIdInBatch(ids);

        boolean result = teacherService.deleteByIds(ids);

        assertTrue(result);
        verify(teacherRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(groupRepository, times(1)).unbindGroupsFromTeacher(1);
        verify(lessonRepository, times(1)).unbindLessonsFromTeacher(1);
        verify(courseRepository, times(1)).unbindCoursesFromTeacher(1);
        verify(roleRepository, times(1)).unbindRoleFromUser(1);
        verify(groupRepository, times(1)).unbindGroupsFromTeacher(2);
        verify(lessonRepository, times(1)).unbindLessonsFromTeacher(2);
        verify(courseRepository, times(1)).unbindCoursesFromTeacher(2);
        verify(roleRepository, times(1)).unbindRoleFromUser(2);
    }

    @Test
    void subscribeUserToGroup_inputTeacherIdGroupId_expectedNothing() {
        doNothing().when(teacherRepository).addUserToGroup(1, 1);

        teacherService.subscribeUserToGroup(1, 1);

        verify(teacherRepository, times(1)).addUserToGroup(1, 1);
    }

    @Test
    void subscribeTeacherToCourse_inputTeacherIdCourseId_expectedNothing() {
        doNothing().when(teacherRepository).addTeacherToCourse(1, 1);

        teacherService.subscribeTeacherToCourse(1, 1);

        verify(teacherRepository, times(1)).addTeacherToCourse(1, 1);
    }

    @Test
    void unsubscribeTeacherFromCourse_inputTeacherIdCourseId_expectedNothing() {
        doNothing().when(teacherRepository).deleteTeacherFromCourse(1, 1);

        teacherService.unsubscribeTeacherFromCourse(1, 1);

        verify(teacherRepository, times(1)).deleteTeacherFromCourse(1, 1);
    }

    @Test
    void findTeachersRelateToGroup_inputGroupId_expectedTeachersList() {
        int groupId = 1;

        when(teacherRepository.findTeachersByGroupsId(groupId)).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findTeachersRelateToGroup(groupId);

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherRepository, times(1)).findTeachersByGroupsId(groupId);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void findTeachersRelateToCourse_inputCourseId_expectedTeachersList() {
        int courseId = 1;

        when(teacherRepository.findTeachersByCoursesId(courseId)).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findTeachersRelateToCourse(courseId);

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherRepository, times(1)).findTeachersByCoursesId(courseId);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void findTeachersRelateToDepartment_inputDepartmentId_expectedTeachersList() {
        int departmentId = 1;

        when(teacherRepository.findTeachersByDepartmentId(departmentId)).thenReturn(teacherEntitiesWithId);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_2)).thenReturn(TEACHER_RESPONSE_WITH_ID_2);

        List<TeacherResponse> foundTeachers = teacherService.findTeachersRelateToDepartment(departmentId);

        assertEquals(teacherResponses, foundTeachers);
        verify(teacherRepository, times(1)).findTeachersByDepartmentId(departmentId);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_2);
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(teacherRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, teacherService.lastPageNumber());

        verify(teacherRepository, times(1)).count();
    }

    @Test
    void login_inputEmailPassword_expectedTrue() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Teacher teacher = Teacher.builder().withEmail("email@mail.com").withPassword("1111").build();

        when(teacherRepository.findByEmail(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(correctPassword, teacher.getPassword())).thenReturn(true);

        assertTrue(teacherService.login(email, correctPassword));

        verify(passwordEncoder, times(1)).matches(correctPassword, teacher.getPassword());
        verify(teacherRepository, times(1)).findByEmail(email);
    }

    @Test
    void login_inputEmailPassword_expectedFalse() {
        final String email = "email@mail.com";
        final String nonCorrectPassword = "1111";
        final Teacher teacher = Teacher.builder().withEmail("email@mail.com").withPassword("1234").build();

        when(teacherRepository.findByEmail(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(nonCorrectPassword, teacher.getPassword())).thenReturn(false);

        assertFalse(teacherService.login(email, nonCorrectPassword));

        verify(passwordEncoder, times(1)).matches(nonCorrectPassword, teacher.getPassword());
        verify(teacherRepository, times(1)).findByEmail(email);
    }

    @Test
    void login_inputNonExistentEmail_expectedFalse() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";

        when(teacherRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertFalse(teacherService.login(email, correctPassword));

        verify(teacherRepository, times(1)).findByEmail(email);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void changeTeacherTeacherTitle_inputTeacherIdTeacherTitleId_expectedNothing() {
        final int teacherId = 1;
        final int teacherTitleId = 1;

        doNothing().when(teacherRepository).changeTeacherTitle(teacherId, teacherTitleId);

        assertDoesNotThrow(() -> teacherService.changeTeacherTeacherTitle(teacherId, teacherTitleId));

        verify(teacherRepository, times(1)).changeTeacherTitle(teacherId, teacherTitleId);
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(groupRepository);
    }

    @Test
    void changeTeacherDepartment_inputTeacherIdTeacherTitleId_expectedNothing() {
        final int teacherId = 1;
        final int departmentId = 1;

        doNothing().when(teacherRepository).changeDepartment(teacherId, departmentId);

        assertDoesNotThrow(() -> teacherService.changeTeacherDepartment(teacherId, departmentId));

        verify(teacherRepository, times(1)).changeDepartment(teacherId, departmentId);
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(groupRepository);
    }

    @Test
    void subscribeTeacherToGroups_inputTeacherIdGroupIds_expectedNothing() {
        final int teacherId = 1;
        final List<Integer> groupIds = new ArrayList<>();
        groupIds.add(1);
        groupIds.add(2);

        doNothing().when(teacherRepository).addUserToGroup(teacherId, groupIds.get(0));
        doNothing().when(teacherRepository).addUserToGroup(teacherId, groupIds.get(1));

        assertDoesNotThrow(() -> teacherService.subscribeTeacherToGroups(teacherId, groupIds));

        verify(teacherRepository, times(1)).addUserToGroup(teacherId, groupIds.get(0));
        verify(teacherRepository, times(1)).addUserToGroup(teacherId, groupIds.get(1));
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(groupRepository);
    }

    @Test
    void unsubscribeTeacherFromGroups_inputTeacherIdGroupIds_expectedNothing() {
        final int teacherId = 1;
        final List<Integer> groupIds = new ArrayList<>();
        groupIds.add(1);
        groupIds.add(2);

        doNothing().when(teacherRepository).deleteTeacherFromGroup(teacherId, groupIds.get(0));
        doNothing().when(teacherRepository).deleteTeacherFromGroup(teacherId, groupIds.get(1));

        assertDoesNotThrow(() -> teacherService.unsubscribeTeacherFromGroups(teacherId, groupIds));

        verify(teacherRepository, times(1)).deleteTeacherFromGroup(teacherId, groupIds.get(0));
        verify(teacherRepository, times(1)).deleteTeacherFromGroup(teacherId, groupIds.get(1));
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(groupRepository);
    }

    @Test
    void subscribeTeacherToCourses_inputTeacherIdCourseIds_expectedNothing() {
        final int teacherId = 1;
        final List<Integer> courseIds = new ArrayList<>();
        courseIds.add(1);
        courseIds.add(2);

        doNothing().when(teacherRepository).addTeacherToCourse(teacherId, courseIds.get(0));
        doNothing().when(teacherRepository).addTeacherToCourse(teacherId, courseIds.get(1));

        assertDoesNotThrow(() -> teacherService.subscribeTeacherToCourses(teacherId, courseIds));

        verify(teacherRepository, times(1)).addTeacherToCourse(teacherId, courseIds.get(0));
        verify(teacherRepository, times(1)).addTeacherToCourse(teacherId, courseIds.get(1));
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(groupRepository);
    }

    @Test
    void unsubscribeTeacherToCourses_inputTeacherIdCourseIds_expectedNothing() {
        final int teacherId = 1;
        final List<Integer> courseIds = new ArrayList<>();
        courseIds.add(1);
        courseIds.add(2);

        doNothing().when(teacherRepository).deleteTeacherFromCourse(teacherId, courseIds.get(0));
        doNothing().when(teacherRepository).deleteTeacherFromCourse(teacherId, courseIds.get(1));

        assertDoesNotThrow(() -> teacherService.unsubscribeTeacherFromCourses(teacherId, courseIds));

        verify(teacherRepository, times(1)).deleteTeacherFromCourse(teacherId, courseIds.get(0));
        verify(teacherRepository, times(1)).deleteTeacherFromCourse(teacherId, courseIds.get(1));
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(groupRepository);
    }

    @Test
    void findTeachersRelateToTeacherTitle_inputTeacherTitleId_expectedGroupList() {
        final Teacher TEACHER_ENTITY_WITH_ID_1 =
                Teacher.builder()
                        .withId(1)
                        .withFirstName("firstName1")
                        .withLastName("lastName1")
                        .withEmail("1@email.com")
                        .withTeacherTitle(TeacherTitle.builder().withId(1).build())
                        .build();
        final Teacher TEACHER_ENTITY_WITH_ID_2 =
                Teacher.builder()
                        .withId(2)
                        .withFirstName("firstName2")
                        .withLastName("lastName2")
                        .withEmail("2@email.com")
                        .withTeacherTitle(TeacherTitle.builder().withId(2).build())
                        .build();

        final List<Teacher> teachers = new ArrayList<>();
        teachers.add(TEACHER_ENTITY_WITH_ID_1);
        teachers.add(TEACHER_ENTITY_WITH_ID_2);

        final List<TeacherResponse> expected = new ArrayList<>();
        expected.add(TEACHER_RESPONSE_WITH_ID_1);

        when(teacherRepository.findAll()).thenReturn(teachers);
        when(mapper.toResponse(TEACHER_ENTITY_WITH_ID_1)).thenReturn(TEACHER_RESPONSE_WITH_ID_1);

        List<TeacherResponse> found = teacherService.findTeachersRelateToTeacherTitle(1);

        assertEquals(expected, found);
        verify(mapper, times(1)).toResponse(TEACHER_ENTITY_WITH_ID_1);
        verify(teacherRepository, times(1)).findAll();
    }

}
