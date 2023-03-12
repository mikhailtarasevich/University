package com.mikhail.tarasevich.university.controller;

import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.service.GroupService;
import com.mikhail.tarasevich.university.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    private static final int STUDENT_ID = 1;
    private static final Group GROUP = Group.builder().withId(1).withName("g1").build();

    @InjectMocks
    private StudentsController studentsController;
    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;

    private MockMvc mockMvc;

    private static final List<StudentResponse> foundStudents = new ArrayList<>();
    private static final List<GroupResponse> groups = new ArrayList<>();
    private static final StudentResponse foundStudent = new StudentResponse();
    private static final GroupResponse groupResponse = new GroupResponse();

    static {
        foundStudent.setId(1);
        foundStudent.setFirstName("Miki");
        foundStudent.setLastName("Ronald");
        foundStudent.setGender(Gender.MALE);
        foundStudent.setEmail("mr@gmail.com");
        foundStudent.setGroup(GROUP);

        foundStudents.add(foundStudent);

        groups.add(groupResponse);
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentsController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void showAllStudents_inputValidPageNumber_expectedSecondStudentsPage() throws Exception {
        final int CURRENT_PAGE = 2;
        final int LAST_PAGE = 3;

        when(studentService.findAll("2")).thenReturn(foundStudents);
        when(studentService.lastPageNumber()).thenReturn(3);

        mockMvc.perform(get("/students").param("page", "2"))
                .andExpectAll(status().isOk(),
                        model().attribute("students", foundStudents),
                        model().attribute("lastPage", LAST_PAGE),
                        model().attribute("currentPage", CURRENT_PAGE),
                        view().name("students/all-students"));

        verify(studentService, times(1)).findAll("2");
        verify(studentService, times(1)).lastPageNumber();
        verifyNoInteractions(groupService);
    }

    @Test
    void showStudent_inputValidIdInPathVariable_expectedShowStudentPage() throws Exception {
        when(studentService.findById(STUDENT_ID)).thenReturn(foundStudent);

        mockMvc.perform(get("/students/{id}", STUDENT_ID))
                .andExpectAll(status().isOk(),
                        model().attribute("student", foundStudent),
                        view().name("students/show-student"));

        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void newStudent_inputNothing_expectedNewStudentPage() throws Exception {
        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/students/new"))
                .andExpectAll(status().isOk(),
                        model().attribute("student", new StudentRequest()),
                        model().attribute("groups", groups),
                        view().name("students/new"));

        verify(groupService, times(1)).findAll();
        verifyNoInteractions(studentService);
    }

    @Test
    void register_inputValidStudentRequest_expectedIsOk() throws Exception {
        final StudentRequest studentRequest = new StudentRequest();
        final StudentResponse studentResponse = new StudentResponse();

        when(studentService.register(studentRequest)).thenReturn(studentResponse);

        mockMvc.perform(post("/students").flashAttr("student", studentRequest))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/students"));

        verify(studentService, times(1)).register(studentRequest);
        verifyNoInteractions(groupService);
    }

    @Test
    void edit_inputValidStudentIdInPathVariable_expectedEditPage() throws Exception {

        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(foundStudent.getId());
        studentRequest.setFirstName(foundStudent.getFirstName());
        studentRequest.setLastName(foundStudent.getLastName());
        studentRequest.setGender(foundStudent.getGender());
        studentRequest.setEmail(foundStudent.getEmail());
        studentRequest.setGroupId(foundStudent.getGroup().getId());

        when(studentService.findById(STUDENT_ID)).thenReturn(foundStudent);
        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/students/{id}/edit", STUDENT_ID))
                .andExpectAll(status().isOk(),
                        model().attribute("student", studentRequest),
                        model().attribute("groups", groups),
                        view().name("students/edit"));

        verify(studentService, times(1)).findById(STUDENT_ID);
        verify(groupService, times(1)).findAll();
    }

    @Test
    void update_inputUpdatedStudentRequestInModel_expectedShowStudentPageWithUpdatedStudentResponse() throws Exception {
        StudentResponse studentResponse = new StudentResponse();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setGroupId(0);

        doNothing().when(studentService).editGeneralUserInfo(studentRequest);
        when(studentService.findById(STUDENT_ID)).thenReturn(studentResponse);

        mockMvc.perform(patch("/students/{id}", STUDENT_ID).flashAttr("student", studentRequest))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("student", studentResponse),
                        view().name("students/show-student")
                );

        verify(studentService, times(1)).editGeneralUserInfo(studentRequest);
        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void updatePassword_inputStudentRequestWithPasswords_expectedShowStudentPage() throws Exception {
        StudentResponse studentResponse = new StudentResponse();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setPassword("1111");
        studentRequest.setConfirmPassword("1111");

        doNothing().when(studentService).editPassword(studentRequest);
        when(studentService.findById(STUDENT_ID)).thenReturn(studentResponse);

        mockMvc.perform(patch("/students/{id}/edit-password", STUDENT_ID)
                        .flashAttr("student", studentRequest))
                .andExpectAll(status().isOk(),
                        model().attribute("student", studentResponse),
                        view().name("students/show-student")
                );

        verify(studentService, times(1)).editPassword(studentRequest);
        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void updateGroup_inputStudentRequestWithUpdatedGroup_expectedShowStudentPage() throws Exception {
        final int UPDATED_GROUP_ID = 1;

        StudentResponse studentResponse = new StudentResponse();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(STUDENT_ID);
        studentRequest.setGroupId(UPDATED_GROUP_ID);

        doNothing().when(studentService).subscribeUserToGroup(studentRequest.getId(), studentRequest.getGroupId());
        when(studentService.findById(STUDENT_ID)).thenReturn(studentResponse);

        mockMvc.perform(patch("/students/{id}/edit-group", STUDENT_ID)
                        .flashAttr("student", studentRequest))
                .andExpectAll(status().isOk(),
                        model().attribute("student", studentResponse),
                        view().name("students/show-student")
                );

        verify(studentService, times(1))
                .subscribeUserToGroup(studentRequest.getId(), studentRequest.getGroupId());
        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void leaveGroup_inputStudentRequest_expectedShowStudentPage() throws Exception {
        StudentResponse studentResponse = new StudentResponse();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(STUDENT_ID);

        doNothing().when(studentService).unsubscribeStudentFromGroup(studentRequest.getId());
        when(studentService.findById(STUDENT_ID)).thenReturn(studentResponse);

        mockMvc.perform(patch("/students/{id}/leave-group", STUDENT_ID)
                        .flashAttr("student", studentRequest))
                .andExpectAll(status().isOk(),
                        model().attribute("student", studentResponse),
                        view().name("students/show-student")
                );

        verify(studentService, times(1)).unsubscribeStudentFromGroup(STUDENT_ID);
        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void delete_inputStudentId_expectedRedirectToStudents() throws Exception {
        when(studentService.deleteById(STUDENT_ID)).thenReturn(true);

        mockMvc.perform(delete("/students/{id}", STUDENT_ID))
                .andExpectAll(status().is3xxRedirection(),
                        view().name("redirect:/students")
                );

        verify(studentService, times(1)).deleteById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void showStudent_inputIncorrectIdInPathVariable_expectedErrorPage() throws Exception {
        doThrow(ObjectWithSpecifiedIdNotFoundException.class).when(studentService).findById(STUDENT_ID);

        mockMvc.perform(get("/students/{id}", STUDENT_ID))
                .andExpectAll(status().isOk(),
                        view().name("globalExceptionHandler/error"));

        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

    @Test
    void showStudent_inputException_expectedCommonErrorPage() throws Exception {
        doThrow(RuntimeException.class).when(studentService).findById(STUDENT_ID);

        mockMvc.perform(get("/students/{id}", STUDENT_ID))
                .andExpectAll(status().isOk(),
                        view().name("globalExceptionHandler/common-error"));

        verify(studentService, times(1)).findById(STUDENT_ID);
        verifyNoInteractions(groupService);
    }

}
