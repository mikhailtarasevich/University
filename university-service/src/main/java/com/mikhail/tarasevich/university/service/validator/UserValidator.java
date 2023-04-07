package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.UserRequest;

public interface UserValidator<R extends UserRequest> {

    void validateUserNameNotNullOrEmpty(R request);
    void validateEmail(R request);
    void validatePassword(R request);

}
