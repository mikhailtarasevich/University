package com.mikhail.tarasevich.university.validator;

import com.mikhail.tarasevich.university.dto.LessonTypeRequest;

public interface LessonTypeValidator {

    void validateUniqueNameInDB(LessonTypeRequest request);
    void validateNameNotNullOrEmpty(LessonTypeRequest request);

}
