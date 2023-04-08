package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dto.UserRequest;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.UserValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidatorImpl<R extends UserRequest> implements UserValidator<R> {

    private final static String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final static Pattern emailPattern = Pattern.compile(EMAIL_REGEX);


    @Override
    public void validateUserNameNotNullOrEmpty(R request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        if (firstName == null || firstName.replaceAll("\\s", "").equals("")) {
            throw new IncorrectRequestDataException("User's first name can't be null or empty.");
        }

        if (lastName == null || lastName.replaceAll("\\s", "").equals("")) {
            throw new IncorrectRequestDataException("User's last name can't be null or empty.");
        }

        if (firstName.length() < 2 || firstName.length() >= 30) {
            throw new IncorrectRequestDataException("User's first name should be between 1 and 30 characters.");
        }

        if (lastName.length() < 2 || lastName.length() >= 30) {
            throw new IncorrectRequestDataException("User's last name should be between 1 and 30 characters.");
        }
    }

    @Override
    public void validateEmail(R request){
        String email = request.getEmail();
        Matcher matcher = emailPattern.matcher(email);

        if (!matcher.matches()) {
            throw new IncorrectRequestDataException("Email is not valid.");
        }
    }

    @Override
    public void validatePassword(R request){
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            throw new IncorrectRequestDataException("Entered passwords are different.");
        }

        if (password.replaceAll("\\s", "").equals("")) {
            throw new IncorrectRequestDataException("Password should not be empty.");
        }

        if (password.length() < 4) {
            throw new IncorrectRequestDataException("Password should be equal or greater than 4 symbols.");
        }
    }

}
