package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.exception.EmailAlreadyExistsException;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.StudentMapper;
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

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @InjectMocks
    StudentServiceImpl studentService;
    @Mock
    StudentDao studentDao;
    @Mock
    GroupDao groupDao;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    StudentMapper studentMapper;
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
    private static final StudentRequest STUDENT_REQUEST_1 =
            new StudentRequest(0, "firstName1", "lastName1",
                    null, "1@email.com", null, null);
    private static final StudentResponse STUDENT_RESPONSE_WITH_ID_1 =
            new StudentResponse(1, "firstName1", "lastName1",
                    null, "1@email.com", null);

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
    private static final StudentRequest STUDENT_REQUEST_2 =
            new StudentRequest(0, "firstName2", "lastName2",
                    null, "2@email.com", null, null);
    private static final StudentResponse STUDENT_RESPONSE_WITH_ID_2 =
            new StudentResponse(2, "firstName2", "lastName2",
                    null, "2@email.com", null);

    private final List<Student> studentEntities = new ArrayList<>();
    private final List<Student> studentEntitiesWithId = new ArrayList<>();
    private final List<StudentResponse> studentResponses = new ArrayList<>();

    {
        studentEntities.add(STUDENT_ENTITY_1);
        studentEntities.add(STUDENT_ENTITY_2);

        studentEntitiesWithId.add(STUDENT_ENTITY_WITH_ID_1);
        studentEntitiesWithId.add(STUDENT_ENTITY_WITH_ID_2);

        studentResponses.add(STUDENT_RESPONSE_WITH_ID_1);
        studentResponses.add(STUDENT_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputStudentRequest_expectedStudentResponseWithId() {
        when(studentMapper.toEntity(STUDENT_REQUEST_1)).thenReturn(STUDENT_ENTITY_1);
        when(studentDao.save(STUDENT_ENTITY_1)).thenReturn(STUDENT_ENTITY_WITH_ID_1);
        when(studentMapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);

        StudentResponse StudentResponse = studentService.register(STUDENT_REQUEST_1);

        assertEquals(STUDENT_RESPONSE_WITH_ID_1, StudentResponse);
        verify(studentMapper, times(1)).toEntity(STUDENT_REQUEST_1);
        verify(studentDao, times(1)).save(STUDENT_ENTITY_1);
        verify(studentMapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
    }

    @Test
    void register_inputStudentRequestWithExistingEmailInDB_expectedException() {
        when(studentDao.findByName(STUDENT_REQUEST_1.getEmail())).thenReturn(Optional.of(Student.builder().build()));

        assertThrows(EmailAlreadyExistsException.class, () -> studentService.register(STUDENT_REQUEST_1));

        verify(studentMapper, times(0)).toEntity(STUDENT_REQUEST_1);
        verify(studentDao, times(0)).save(STUDENT_ENTITY_1);
        verify(studentMapper, times(0)).toResponse(STUDENT_ENTITY_WITH_ID_1);
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
        when(studentDao.findByName(studentWithExistingEmail.getEmail())).thenReturn(Optional.of(Student.builder().build()));
        when(studentMapper.toEntity(STUDENT_REQUEST_1)).thenReturn(STUDENT_ENTITY_1);
        when(studentMapper.toEntity(STUDENT_REQUEST_2)).thenReturn(STUDENT_ENTITY_2);
        doNothing().when(studentDao).saveAll(studentEntities);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_2);
        doThrow(IncorrectRequestData.class).when(validator).validateUserNameNotNullOrEmpty(studentWithInvalidLastName);

        studentService.registerAll(listForRegister);

        verify(studentMapper, times(1)).toEntity(STUDENT_REQUEST_1);
        verify(studentMapper, times(1)).toEntity(STUDENT_REQUEST_2);
        verify(studentDao, times(1)).saveAll(studentEntities);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_1);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_2);
        verify(validator, times(1)).validateUserNameNotNullOrEmpty(studentWithInvalidLastName);
        verify(validator, times(0)).validateUserNameNotNullOrEmpty(studentWithExistingEmail);
    }

    @Test
    void findById_inputIntId_expectedFoundStudent() {
        when(studentDao.findById(1)).thenReturn(Optional.of(STUDENT_ENTITY_WITH_ID_1));
        when(studentMapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);

        Optional<StudentResponse> studentResponse = studentService.findById(1);

        assertEquals(Optional.of(STUDENT_RESPONSE_WITH_ID_1), studentResponse);
        verify(studentMapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(studentDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundStudentsFromPageOne() {
        when(studentDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(studentEntitiesWithId);
        when(studentDao.count()).thenReturn(2L);
        when(studentMapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        when(studentMapper.toResponse(STUDENT_ENTITY_WITH_ID_2)).thenReturn(STUDENT_RESPONSE_WITH_ID_2);

        List<StudentResponse> foundStudents = studentService.findAll("1");

        assertEquals(studentResponses, foundStudents);
        verify(studentDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(studentDao, times(1)).count();
        verify(studentMapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(studentMapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_2);
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
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_1 =
                new StudentRequest(1, "update1", "update1",
                        null, "update1@email.com", null, null);

        doNothing().when(studentDao).update(STUDENT_ENTITY_FOR_UPDATE_1);
        when(studentMapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_1)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_1);

        studentService.edit(STUDENT_REQUEST_FOR_UPDATE_1);

        verify(studentMapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(studentDao, times(1)).update(STUDENT_ENTITY_FOR_UPDATE_1);
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
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_1 =
                new StudentRequest(1, "update1", "update1",
                        null, "update1@email.com", null, null);
        final Student STUDENT_ENTITY_FOR_UPDATE_2 =
                Student.builder()
                        .withId(2)
                        .withFirstName("update2")
                        .withLastName("update2")
                        .withEmail("update2@email.com")
                        .build();
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_2 =
                new StudentRequest(2, "update2", "update2",
                        null, "update2@email.com", null, null);
        final StudentRequest STUDENT_REQUEST_FOR_UPDATE_INCORRECT =
                new StudentRequest(3, " ", " ",
                        null, " ", null, null);

        final List<StudentRequest> inputList = new ArrayList<>();
        inputList.add(STUDENT_REQUEST_FOR_UPDATE_1);
        inputList.add(STUDENT_REQUEST_FOR_UPDATE_2);
        inputList.add(STUDENT_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Student> listForUpdate = new ArrayList<>();
        listForUpdate.add(STUDENT_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(STUDENT_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData())
                .when(validator).validateUserNameNotNullOrEmpty(STUDENT_REQUEST_FOR_UPDATE_INCORRECT);

        when(studentMapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_1)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_1);
        when(studentMapper.toEntity(STUDENT_REQUEST_FOR_UPDATE_2)).thenReturn(STUDENT_ENTITY_FOR_UPDATE_2);

        doNothing().when(studentDao).updateAll(listForUpdate);

        studentService.editAll(inputList);

        verify(studentMapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_1);
        verify(studentMapper, times(1)).toEntity(STUDENT_REQUEST_FOR_UPDATE_2);
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
        when(studentDao.deleteById(id)).thenReturn(true);

        boolean result = studentService.deleteById(1);

        assertTrue(result);
        verify(studentDao, times(1)).findById(id);
        verify(studentDao, times(1)).deleteById(id);
        verify(groupDao, times(1)).unbindGroupsFromStudent(id);
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
        when(studentDao.deleteByIds(ids)).thenReturn(true);

        boolean result = studentService.deleteByIds(ids);

        assertTrue(result);
        verify(studentDao, times(1)).deleteByIds(ids);
        verify(groupDao, times(1)).unbindGroupsFromStudent(1);
        verify(groupDao, times(1)).unbindGroupsFromStudent(2);
    }

    @Test
    void deleteByIds_inputStudentsIds_expectedUnsuccessfulDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupDao).unbindGroupsFromStudent(1);
        doNothing().when(groupDao).unbindGroupsFromStudent(2);
        when(studentDao.deleteByIds(ids)).thenReturn(false);

        boolean result = studentService.deleteByIds(ids);

        assertFalse(result);
        verify(studentDao, times(1)).deleteByIds(ids);
        verify(groupDao, times(1)).unbindGroupsFromStudent(1);
        verify(groupDao, times(1)).unbindGroupsFromStudent(2);
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
        when(studentMapper.toResponse(STUDENT_ENTITY_WITH_ID_1)).thenReturn(STUDENT_RESPONSE_WITH_ID_1);
        when(studentMapper.toResponse(STUDENT_ENTITY_WITH_ID_2)).thenReturn(STUDENT_RESPONSE_WITH_ID_2);

        List<StudentResponse> foundStudents = studentService.findStudentsRelateToGroup(teacherId);

        assertEquals(studentResponses, foundStudents);
        verify(studentDao, times(1)).findStudentsRelateToGroup(teacherId);
        verify(studentMapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_1);
        verify(studentMapper, times(1)).toResponse(STUDENT_ENTITY_WITH_ID_2);
    }

    @Test
    void login_inputEmailPassword_expectedTrue() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Student student = Student.builder().withEmail("email@mail.com").withPassword("1111").build();

        when(studentDao.findByName(email)).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(correctPassword, student.getPassword())).thenReturn(true);

        assertTrue(studentService.login(email, correctPassword));
    }

    @Test
    void login_inputEmailPassword_expectedFalse() {
        final String email = "email@mail.com";
        final String correctPassword = "1111";
        final Student student = Student.builder().withEmail("email@mail.com").withPassword("1234").build();

        when(studentDao.findByName(email)).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(correctPassword, student.getPassword())).thenReturn(false);

        assertFalse(studentService.login(email, correctPassword));
    }

}
