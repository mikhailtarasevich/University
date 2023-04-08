package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.LessonTypeRequest;

public interface LessonTypeValidator {

    void validateUniqueNameInDB(LessonTypeRequest request);
    void validateNameNotNullOrEmpty(LessonTypeRequest request);

}
