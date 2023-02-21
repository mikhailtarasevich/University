package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StudentValidatorImplTest {

    private static final UserValidatorImpl<StudentRequest> studentValidator = new UserValidatorImpl<>();

    @Test
    void validateUserNameNotNullOrEmpty_inputCorrectNames_expectedNothing() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1);
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName("lastName1");
        studentRequest.setEmail("1@email.com");
        studentRequest.setPassword("1111");

        assertDoesNotThrow(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputEmptyFirstName_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1);
        studentRequest.setFirstName("   ");
        studentRequest.setLastName("lastName1");
        studentRequest.setEmail("1@email.com");
        studentRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameEmpty_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1);
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName("   ");
        studentRequest.setEmail("1@email.com");
        studentRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputFirstNameNull_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1);
        studentRequest.setFirstName(null);
        studentRequest.setLastName("lastName1");
        studentRequest.setEmail("1@email.com");
        studentRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameNull_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1);
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName(null);
        studentRequest.setEmail("1@email.com");
        studentRequest.setPassword("1111");

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

}
