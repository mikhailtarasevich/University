package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.UserValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class TeacherValidatorImplTest {

    private static final UserValidatorImpl<TeacherRequest> teacherValidator = new UserValidatorImpl<>();

    @Test
    void validateUserNameNotNullOrEmpty_inputCorrectNames_expectedNothing() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName("lastName1");

        assertDoesNotThrow(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputEmptyFirstName_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("   ");
        teacherRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputFirstNameNull_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName(null);
        teacherRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputShortFirstName_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("L");
        teacherRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name should be between 1 and 30 characters.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLongFirstName_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        teacherRequest.setLastName("lastName1");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's first name should be between 1 and 30 characters.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameEmpty_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName("   ");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLastNameNull_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName(null);

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name can't be null or empty.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputShortLastName_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName("E");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name should be between 1 and 30 characters.");
    }

    @Test
    void validateUserNameNotNullOrEmpty_inputLongLastName_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setFirstName("firstName1");
        teacherRequest.setLastName("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

        final Throwable exception = catchException(() -> teacherValidator.validateUserNameNotNullOrEmpty(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("User's last name should be between 1 and 30 characters.");
    }

    @Test
    void validateEmail_inputCorrectEmail_expectedNothing() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setEmail("1@email.com");

        assertDoesNotThrow(() -> teacherValidator.validateEmail(teacherRequest));
    }

    @Test
    void validateEmail_inputIncorrectEmail_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setEmail("incorrectEmail");

        final Throwable exception = catchException(() -> teacherValidator.validateEmail(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Email is not valid.");
    }

    @Test
    void validatePassword_inputCorrectPassword_expectedNothing() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setPassword("1111");
        teacherRequest.setConfirmPassword("1111");

        assertDoesNotThrow(() -> teacherValidator.validatePassword(teacherRequest));
    }

    @Test
    void validateEmail_inputPasswordNotMatch_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setPassword("1111");
        teacherRequest.setConfirmPassword("2222");

        final Throwable exception = catchException(() -> teacherValidator.validatePassword(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Entered passwords are different.");
    }

    @Test
    void validateEmail_inputEmptyPasswords_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setPassword("    ");
        teacherRequest.setConfirmPassword("    ");

        final Throwable exception = catchException(() -> teacherValidator.validatePassword(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Password should not be empty.");
    }

    @Test
    void validateEmail_inputShortPasswords_expectedException() {
        final TeacherRequest teacherRequest = new TeacherRequest();
        teacherRequest.setPassword("111");
        teacherRequest.setConfirmPassword("111");

        final Throwable exception = catchException(() -> teacherValidator.validatePassword(teacherRequest));

        assertThat(exception).isInstanceOf(IncorrectRequestDataException.class)
                .hasMessageContaining("Password should be equal or greater than 4 symbols.");
    }

}
