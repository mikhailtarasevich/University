package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.GroupRequest;

public interface GroupValidator {

    void validateUniqueNameInDB(GroupRequest request);
    void validateNameNotNullOrEmpty(GroupRequest request);

}
