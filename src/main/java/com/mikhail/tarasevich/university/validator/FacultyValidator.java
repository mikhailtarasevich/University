package com.mikhail.tarasevich.university.validator;

import com.mikhail.tarasevich.university.dto.FacultyRequest;

public interface FacultyValidator {

    void validateUniqueNameInDB(FacultyRequest request);
    void validateNameNotNullOrEmpty(FacultyRequest request);

}
