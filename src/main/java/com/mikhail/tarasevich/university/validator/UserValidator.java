package com.mikhail.tarasevich.university.validator;

import com.mikhail.tarasevich.university.dto.UserRequest;

public interface UserValidator<R extends UserRequest> {

    void validateUserNameNotNullOrEmpty(R request);

}
