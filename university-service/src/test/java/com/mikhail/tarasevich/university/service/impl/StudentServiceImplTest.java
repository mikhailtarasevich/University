package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.RoleDao;
import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.mapper.StudentMapper;
import com.mikhail.tarasevich.university.service.validator.UserValidator;
import com.mikhail.tarasevich.university.service.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @InjectMocks
    StudentServiceImpl studentService;
    @Mock
    StudentDao studentDao;
    @Mock
    GroupDao groupDao;
    @Mock
    RoleDao roleDao;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    StudentMapper mapper;
    @Mock
    UserValidator<StudentRequest> validator;

    private static final Student STUDENT_ENTITY_1 =
            Student.builder()
                    .withFirstName("firstName1")
                    .withLastName("lastName1")
                    .withEmail("1@email.com")
                    .build();
    private static final Student STUDENT_ENTITY_WITH_ID_1 =
            Student.builder()
                    .withId(1)
                    .withFirstName("firstName1")
                    .withLastName("lastName1")
                    .withEmail("1@email.com")
                    .build();
    private static final StudentRequest STUDENT_REQUEST_1 = new StudentRequest();
    private static final StudentResponse STUDENT_RESPONSE_WITH_ID_1 = new StudentResponse();

    private static final Student STUDENT_ENTITY_2 =
            Student.builder()
                    .withFirstName("firstName2")
                    .withLastName("lastName2")
                    .withEmail("2@email.com")
                    .build();
    private static final Student STUDENT_ENTITY_WITH_ID_2 =
            Student.builder()
                    .withId(2)
                    .withFirstName("firstName2")
                    .withLastName("lastName2")
                    .withEmail("2@email.com")
                    .build();
    private static final StudentRequest STUDENT_REQUEST_2 = new StudentRequest();
    private static final StudentResponse STUDENT_RESPONSE_WITH_ID_2 = new StudentResponse();

    private static final List<Student> studentEntities = new ArrayList<>();
    private static final List<Student> studentEntitiesWithId = new ArrayList<>();
    private static final List<StudentResponse> studentResponses = new ArrayList<>();

    static {
        STUDENT_REQUEST_1.setId(0);
        STUDENT_REQUEST_1.setFirstName("firstName1");
        STUDENT_REQUEST_1.setLastName("lastName1");
        STUDENT_REQUEST_1.setEmail("1@email.com");
        STUDENT_REQUEST_1.setPassword("1111");

        STUDENT_RESPONSE_WITH_ID_1.setId(1);
        STUDENT_RESPONSE_WITH_ID_1.setFirstName("firstName1");
        STUDENT_RESPONSE_WITH_ID_1.setLastName("lastName1");
        STUDENT_RESPONSE_WITH_ID_1.setEmail("1@email.com");

        STUDENT_REQUEST_2.setId(0);
        STUDENT_REQUEST_2.setFirstName("firstName2");
        STUDENT_REQUEST_2.setLastName("lastName2");
        STUDENT_REQUEST_2.setEmail("2@email.com");
        STUDENT_REQUEST_2.setPassword("1234");

        STUDENT_RESPONSE_WITH_ID_2.setId(2);
        STUDENT_RESPONSE_WITH_ID_2.setFirstName("firstName2");
        STUDENT_RESPONSE_WITH_ID_2.setLastName("lastName2");
        STUDENT_RESPONSE_WITH_ID_2.setEmail("2@email.com");

        studentEntities.add(STUDENT_ENTITY_1);
        studentEntities.add(STUDENT_ENTITY_2);

        studentEntitiesWithId.add(STUDENT_ENTITY_WITH_ID_1);
        studentEntitiesWithId.add(STUDENT_ENTITY_WITH_ID_2);

        studentResponses.add(STUDENT_RESPONSE_WITH_ID_1);
        studentResponses.add(STUDENT_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputStudentRequest_expectedStudentResponseWithId() {
        when(mapper.toEntity(STUDENT_REQUEST_1)).thenReturn(STUDENT_ENTITY_1);
        when(studentDao.save(STUDENT_ENTITY_1)).thenReturn(STUDENT_ENTITY_WITH_ID_1);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
        doNothing().when(roleDao).addRoleForUser(1, 3);

        StudentResponse StudentResponse = studentService.register(STUDENT_REQUEST_1);

        assertEquals(STUDENT_RESPONSE_WITH_ID_1, StudentResponse);
        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_1);
        verify(studentDao, times(1)).save(STUDENT_ENTITY_1);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
        verify(roleDao, times(1)).addRoleForUser(1,3);
    }

    @Test
    void register_inputStudentRequestWithExistingEmailInDB_expectedException() {
        when(studentDao.findByName(STUDENT_REQUEST_1.getEmail())).thenReturn(Optional.of(Student.builder().build()));

        assertThrows(EmailAlreadyExistsException.class, () -> studentService.register(STUDENT_REQUEST_1));

        verify(mapper, times(0)).toEntity(STUDENT_REQUEST_1);
        verify(studentDao, times(0)).save(STUDENT_ENTITY_1);
        verify(mapper, times(0)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(validator, times(0)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
    }

    @Test
    void registerAllStudents_inputStudentRequestListWithRepeatableStudent_expectedNothing() {
        StudentRequest studentWithExistingEmail = new StudentRequest();
        studentWithExistingEmail.setEmail("exist@email.com");

        StudentRequest studentWithInvalidLastName = new StudentRequest();
        studentWithExistingEmail.setFirstName("Helga");
        studentWithExistingEmail.setLastName("  ");

        final List<StudentRequest> listForRegister = new ArrayList<>();

        listForRegister.add(STUDENT_REQUEST_1);
        listForRegister.add(STUDENT_REQUEST_2);
        listForRegister.add(studentWithExistingEmail);
        listForRegister.add(studentWithInvalidLastName);

        when(studentDao.findByName(STUDENT_REQUEST_1.getEmail())).thenReturn(Optional.empty());
        when(studentDao.findByName(STUDENT_REQUEST_2.getEmail())).thenReturn(Optional.empty());
        when(studentDao.findByName(studentWithInvalidLastName.getEmail())).thenReturn(Optional.empty());
        when(studentDao.findByName(studentWithExistingEmail.getEmail()))
                .thenReturn(Optional.of(Student.builder().build()));
        when(mapper.toEntity(STUDENT_REQUEST_1)).thenReturn(STUDENT_ENTITY_1);
        when(mapper.toEntity(STUDENT_REQUEST_2)).thenReturn(STUDENT_ENTITY_2);
        doNothing().when(studentDao).saveAll(studentEntities);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_2);
        doThrow(IncorrectRequestDataException.class).when(validator).validateUserNameNotNullOrEmpty(studentWithInvalidLastName);

        studentService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_1);
        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_2);
        verify(studentDao, times(1)).saveAll(studentEntities);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_2);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(studentWithInvalidLastName);
        verify(validator, times(0)).validateUserNameNotNullOrEmpty(studentWithExistingEmail);
    }

    @Test
    void findById_inputIntId_expectedFoundStudent() {
        when(studentDao.findById(1)).thenReturn(Optional.of(STUDENT_ENTITY_WITH_ID_1));
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);

        StudentResponse studentResponse = studentService.findById(1);

        assertEquals(STUDENT_RESPONSE_WITH_ID_1, studentResponse);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(studentDao, times(1)).findById(1);
    }

    @Test
    void findById_inputNonExistentId_expectedException() {
        when(studentDao.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> studentService.findById(100));

        verify(studentDao, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllStudents() {
        when(studentDao.findAll()).thenReturn(studentEntitiesWithId);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_2)).thenReturn(STUDENT_RESPONSE_WITH_ID_2);

        List<StudentResponse> foundStudents = studentService.findAll();

        assertEquals(studentResponses, foundStudents);
        verify(studentDao, times(1)).findAll();
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundStudentsFromPageOne() {
        when(studentDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(studentEntitiesWithId);
        when(studentDao.count()).thenReturn(2L);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_2)).thenReturn(STUDENT_RESPONSE_WITH_ID_2);

        List<StudentResponse> foundStudents = studentService.findAll("1");

        assertEquals(studentResponses, foundStudents);
        verify(studentDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(studentDao, times(1)).count();
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputStudentRequest_expectedNothing() {
        final Student STUDENT_ENTITY_FOR_UPDATE_1 =
                Student.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withEmail("update1@email.com")
                        .build();
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_1 = new StudentRequest();
        STUDENT_REQUEST_FOR_UPDATE_1.setId(1);
        STUDENT_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        STUDENT_REQUEST_FOR_UPDATE_1.setLastName("update1");
        STUDENT_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        STUDENT_REQUEST_FOR_UPDATE_1.setPassword("0000");


        doNothing().when(studentDao).update(STUDENT_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_1)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_1);

        studentService.edit(STUDENT_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateEmail(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validatePassword(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(studentDao, times(1)).update(STUDENT_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editGeneralUserInfo_inputStudentRequest_expectedNothing() {
        final Student STUDENT_ENTITY_FOR_UPDATE_1 =
                Student.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withEmail("update1@email.com")
                        .build();
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_1 = new StudentRequest();
        STUDENT_REQUEST_FOR_UPDATE_1.setId(1);
        STUDENT_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        STUDENT_REQUEST_FOR_UPDATE_1.setLastName("update1");
        STUDENT_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        STUDENT_REQUEST_FOR_UPDATE_1.setPassword("0000");


        doNothing().when(studentDao).updateGeneralUserInfo(STUDENT_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_1)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_1);

        studentService.editGeneralUserInfo(STUDENT_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateEmail(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(studentDao, times(1)).updateGeneralUserInfo(STUDENT_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editPassword_inputStudentRequest_expectedNothing() {
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_1 = new StudentRequest();
        STUDENT_REQUEST_FOR_UPDATE_1.setId(1);
        STUDENT_REQUEST_FOR_UPDATE_1.setPassword("0000");
        STUDENT_REQUEST_FOR_UPDATE_1.setConfirmPassword("0000");

        when(passwordEncoder.encode("0000")).thenReturn("asdferergergefsv1234");
        doNothing().when(studentDao).updateUserPassword(1, "asdferergergefsv1234");

        studentService.editPassword(STUDENT_REQUEST_FOR_UPDATE_1);

        verify(studentDao, times(1)).updateUserPassword(1, "asdferergergefsv1234");
        verify(passwordEncoder, times(1)).encode("0000");
    }

    @Test
    void editAll_inputStudentRequestListWhereOneStudentHasIncorrectName_expectedNothing() {
        final Student STUDENT_ENTITY_FOR_UPDATE_1 =
                Student.builder()
                        .withId(1)
                        .withFirstName("update1")
                        .withLastName("update1")
                        .withEmail("update1@email.com")
                        .build();
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_1 = new StudentRequest();
        STUDENT_REQUEST_FOR_UPDATE_1.setId(1);
        STUDENT_REQUEST_FOR_UPDATE_1.setFirstName("update1");
        STUDENT_REQUEST_FOR_UPDATE_1.setLastName("update1");
        STUDENT_REQUEST_FOR_UPDATE_1.setEmail("update1@email.com");
        STUDENT_REQUEST_FOR_UPDATE_1.setPassword("0000");

        final Student STUDENT_ENTITY_FOR_UPDATE_2 =
                Student.builder()
                        .withId(2)
                        .withFirstName("update2")
                        .withLastName("update2")
                        .withEmail("update2@email.com")
                        .build();
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_2 = new StudentRequest();
        STUDENT_REQUEST_FOR_UPDATE_2.setId(2);
        STUDENT_REQUEST_FOR_UPDATE_2.setFirstName("update2");
        STUDENT_REQUEST_FOR_UPDATE_2.setLastName("update2");
        STUDENT_REQUEST_FOR_UPDATE_2.setEmail("update2@email.com");
        STUDENT_REQUEST_FOR_UPDATE_2.setPassword("4321");

        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_INCORRECT = new StudentRequest();
        STUDENT_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        STUDENT_REQUEST_FOR_UPDATE_INCORRECT.setFirstName("  ");
        STUDENT_REQUEST_FOR_UPDATE_INCORRECT.setLastName(" ");
        STUDENT_REQUEST_FOR_UPDATE_INCORRECT.setEmail("   ");
        STUDENT_REQUEST_FOR_UPDATE_INCORRECT.setPassword("  ");

        final List<StudentRequest> inputList = new ArrayList<>();
        inputList.add(STUDENT_REQUEST_FOR_UPDATE_1);
        inputList.add(STUDENT_REQUEST_FOR_UPDATE_2);
        inputList.add(STUDENT_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Student> listForUpdate = new ArrayList<>();
        listForUpdate.add(STUDENT_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(STUDENT_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException())
                .when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_1)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_2)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_2);

        doNothing().when(studentDao).updateAll(listForUpdate);

        studentService.editAll(inputList);

        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_2);
        verify(studentDao, times(1)).updateAll(listForUpdate);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputStudentId_expectedSuccessDelete() {
        int id = 1;

        when(studentDao.findById(id)).thenReturn(Optional.of(STUDENT_ENTITY_1));
        doNothing().when(groupDao).unbindGroupsFromStudent(id);
        doNothing().when(roleDao).unbindRoleFromUser(id);
        when(studentDao.deleteById(id)).thenReturn(true);

        boolean result = studentService.deleteById(1);

        assertTrue(result);
        verify(studentDao, times(1)).findById(id);
        verify(studentDao, times(1)).deleteById(id);
        verify(groupDao, times(1)).unbindGroupsFromStudent(id);
        verify(roleDao, times(1)).unbindRoleFromUser(id);
    }

    @Test
    void deleteById_inputStudentId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(studentDao.findById(id)).thenReturn(Optional.empty());

        boolean result = studentService.deleteById(1);

        assertFalse(result);
        verify(studentDao, times(1)).findById(id);
        verifyNoInteractions(groupDao);
        verify(studentDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputStudentsIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupDao).unbindGroupsFromStudent(1);
        doNothing().when(groupDao).unbindGroupsFromStudent(2);
        doNothing().when(roleDao).unbindRoleFromUser(1);
        doNothing().when(roleDao).unbindRoleFromUser(2);
        when(studentDao.deleteByIds(ids)).thenReturn(true);

        boolean result = studentService.deleteByIds(ids);

        assertTrue(result);
        verify(studentDao, times(1)).deleteByIds(ids);
        verify(groupDao, times(1)).unbindGroupsFromStudent(1);
        verify(groupDao, times(1)).unbindGroupsFromStudent(2);
        verify(roleDao, times(1)).unbindRoleFromUser(1);
        verify(roleDao, times(1)).unbindRoleFromUser(2);
    }

    @Test
    void deleteByIds_inputStudentsIds_expectedUnsuccessfulDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupDao).unbindGroupsFromStudent(1);
        doNothing().when(groupDao).unbindGroupsFromStudent(2);
        doNothing().when(roleDao).unbindRoleFromUser(1);
        doNothing().when(roleDao).unbindRoleFromUser(2);
        when(studentDao.deleteByIds(ids)).thenReturn(false);

        boolean result = studentService.deleteByIds(ids);

        assertFalse(result);
        verify(studentDao, times(1)).deleteByIds(ids);
        verify(groupDao, times(1)).unbindGroupsFromStudent(1);
        verify(groupDao, times(1)).unbindGroupsFromStudent(2);
        verify(roleDao, times(1)).unbindRoleFromUser(1);
        verify(roleDao, times(1)).unbindRoleFromUser(2);
    }

    @Test
    void subscribeUserToGroup_inputStudentIdGroupId_expectedNothing() {
        doNothing().when(studentDao).addUserToGroup(1, 1);

        studentService.subscribeUserToGroup(1, 1);

        verify(studentDao, times(1)).addUserToGroup(1, 1);
    }

    @Test
    void unsubscribeStudentFromGroup_inputStudentIdGroupId_expectedNothing() {
        doNothing().when(studentDao).deleteStudentFromGroup(1);

        studentService.unsubscribeStudentFromGroup(1);

        verify(studentDao, times(1)).deleteStudentFromGroup(1);
    }

    @Test
    void findStudentsRelateToGroup_inputGroupId_expectedStudentsList() {
        int teacherId = 1;

        when(studentDao.findStudentsRelateToGroup(teacherId)).thenReturn(studentEntitiesWithId);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_2)).thenReturn(STUDENT_RESPONSE_WITH_ID_2);

        List<StudentResponse> foundStudents = studentService.findStudentsRelateToGroup(teacherId);

        assertEquals(studentResponses, foundStudents);
        verify(studentDao, times(1)).findStudentsRelateToGroup(teacherId);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_2);
    }

    @Test
    void findStudentsNotRelateToGroup_inputGroupId_expectedStudentList() {
        List<Student> studentsRelateToGroup = new ArrayList<>();
        studentsRelateToGroup.add(STUDENT_ENTITY_WITH_ID_2);

        List<StudentResponse> expected = new ArrayList<>();
        expected.add(STUDENT_RESPONSE_WITH_ID_1);

        int groupId = 1;

        when(studentDao.findAll()).thenReturn(studentEntitiesWithId);
        when(studentDao.findStudentsRelateToGroup(groupId)).thenReturn(studentsRelateToGroup);
        when(mapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);

        List<StudentResponse> students = studentService.findStudentsNotRelateToGroup(groupId);

        assertEquals(expected, students);
        verify(studentDao, times(1)).findAll();
        verify(studentDao, times(1)).findStudentsRelateToGroup(groupId);
        verify(mapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
    }

    @Test
    void lastPageNumber() {
        when(studentDao.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, studentService.lastPageNumber());
        verify(studentDao, times(1)).count();
    }

    @Test
    void login_inputEmailPassword_expectedTrue() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Student student = Student.builder().withEmail("email@mail.com").withPassword("1111").build();

        when(studentDao.findByName(email)).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(correctPassword, student.getPassword())).thenReturn(true);

        assertTrue(studentService.login(email, correctPassword));

        verify(passwordEncoder, times(1)).matches(correctPassword, student.getPassword());
        verify(studentDao, times(1)).findByName(email);
    }

    @Test
    void login_inputEmailPassword_expectedFalse() {
        final String email = "email@mail.com";
        final String nonCorrectPassword = "1111";
        final Student student = Student.builder().withEmail("email@mail.com").withPassword("1234").build();

        when(studentDao.findByName(email)).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(nonCorrectPassword, student.getPassword())).thenReturn(false);

        assertFalse(studentService.login(email, nonCorrectPassword));

        verify(passwordEncoder, times(1)).matches(nonCorrectPassword, student.getPassword());
        verify(studentDao, times(1)).findByName(email);
    }

    @Test
    void login_inputNonExistentEmail_expectedFalse() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";

        when(studentDao.findByName(email)).thenReturn(Optional.empty());

        assertFalse(studentService.login(email, correctPassword));

        verify(studentDao, times(1)).findByName(email);
        verifyNoInteractions(passwordEncoder);
    }

}
