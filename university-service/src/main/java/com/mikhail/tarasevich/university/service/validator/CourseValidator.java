package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.CourseRequest;

public interface CourseValidator {

    void validateUniqueNameInDB(CourseRequest request);
    void validateNameNotNullOrEmpty(CourseRequest request);

}
