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

    UserValidatorImpl<StudentRequest> studentValidator = new UserValidatorImpl<>();

    @Test
    void validateUserNameNotNullOrEmpty_inputCorrectNames_expectedNothing() {
        StudentRequest studentRequest =
                new StudentRequest(0, "firstName1", "lastName1",
                        null, "1@email.com", null, null);

        assertDoesNotThrow(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputEmptyFirstName_expectedException() {
        final StudentRequest studentRequest =
                new StudentRequest(0, "  ", "lastName1",
                        null, "1@email.com", null, null);

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameEmpty_expectedException() {
        final StudentRequest studentRequest =
                new StudentRequest(0, "firstName1", "  ",
                        null, "1@email.com", null, null);

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputFirstNameNull_expectedException() {
        final StudentRequest studentRequest =
                new StudentRequest(0, null, "lastName1",
                        null, "1@email.com", null, null);

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameNull_expectedException() {
        final StudentRequest studentRequest =
                new StudentRequest(0, "firstName1", null,
                        null, "1@email.com", null, null);

        assertThrows(IncorrectRequestData.class, () -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

}
