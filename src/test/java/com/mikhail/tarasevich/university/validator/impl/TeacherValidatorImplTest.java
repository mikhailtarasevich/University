package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TeacherValidatorImplTest {

    private static final UserValidatorImpl<TeacherRequest> teacherValidator = new UserValidatorImpl<>();

    @Test
    void validateUserNameNotNullOrEmpty_inputCorrectNames_expectedNothing() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setId(1);
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName("lastName1");
        teacherRequest.setEmail("1@email.com");
        teacherRequest.setPassword("1111");

        assertDoesNotThrow(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputEmptyFirstName_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setId(1);
        teacherRequest.setFirstName("   ");
        teacherRequest.setLastName("lastName1");
        teacherRequest.setEmail("1@email.com");
        teacherRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameEmpty_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setId(1);
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName("   ");
        teacherRequest.setEmail("1@email.com");
        teacherRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputFirstNameNull_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setId(1);
        teacherRequest.setFirstName(null);
        teacherRequest.setLastName("lastName1");
        teacherRequest.setEmail("1@email.com");
        teacherRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameNull_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setId(1);
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName(null);
        teacherRequest.setEmail("1@email.com");
        teacherRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));
    }

}
