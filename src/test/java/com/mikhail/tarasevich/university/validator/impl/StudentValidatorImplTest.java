package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class StudentValidatorImplTest {

    private static final UserValidatorImpl<StudentRequest> studentValidator = new UserValidatorImpl<>();

    @Test
    void validateUserNameNotNullOrEmpty_inputCorrectNames_expectedNothing() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName("lastName1");

        assertDoesNotThrow(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputEmptyFirstName_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("   ");
        studentRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputFirstNameNull_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName(null);
        studentRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputShortFirstName_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("L");
        studentRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name should be between 1 and 30 characters.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLongFirstName_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        studentRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name should be between 1 and 30 characters.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameEmpty_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName("   ");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameNull_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName(null);

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputShortLastName_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName("E");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name should be between 1 and 30 characters.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLongLastName_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName("firstName1");
        studentRequest.setLastName("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

        final Throwable exception = catchException(() -> studentValidator.validateUserNameNotNullOrEmpty(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name should be between 1 and 30 characters.");
    }

    @Test
    void validateEmail_inputCorrectEmail_expectedNothing() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail("1@email.com");

        assertDoesNotThrow(() -> studentValidator.validateEmail(studentRequest));
    }

    @Test
    void validateEmail_inputIncorrectEmail_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail("incorrectEmail");

        final Throwable exception = catchException(() -> studentValidator.validateEmail(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Email is not valid.");
    }

    @Test
    void validatePassword_inputCorrectPassword_expectedNothing() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setPassword("1111");
        studentRequest.setConfirmPassword("1111");

        assertDoesNotThrow(() -> studentValidator.validatePassword(studentRequest));
    }

    @Test
    void validateEmail_inputPasswordNotMatch_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setPassword("1111");
        studentRequest.setConfirmPassword("2222");

        final Throwable exception = catchException(() -> studentValidator.validatePassword(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Entered passwords are different.");
    }

    @Test
    void validateEmail_inputEmptyPasswords_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setPassword("    ");
        studentRequest.setConfirmPassword("    ");

        final Throwable exception = catchException(() -> studentValidator.validatePassword(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Password should not be empty.");
    }

    @Test
    void validateEmail_inputShortPasswords_expectedException() {
        final StudentRequest studentRequest = new StudentRequest();
        studentRequest.setPassword("111");
        studentRequest.setConfirmPassword("111");

        final Throwable exception = catchException(() -> studentValidator.validatePassword(studentRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Password should be equal or greater than 4 symbols.");
    }

}
