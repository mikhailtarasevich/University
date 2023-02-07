package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dto.UserRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.validator.UserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl<R extends UserRequest> implements UserValidator<R> {

    @Override
    public void validateUserNameNotNullOrEmpty(R request) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();

        if (firstName == null || firstName.replaceAll("\\s", "").equals("")) {
            throw new IncorrectRequestData("User's first name can't be null or empty.");
        }

        if (lastName == null || lastName.replaceAll("\\s", "").equals("")) {
            throw new IncorrectRequestData("User's last name can't be null or empty.");
        }
    }

}
