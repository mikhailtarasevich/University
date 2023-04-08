package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.LessonRequest;

public interface LessonValidator {

    void validateUniqueNameInDB(LessonRequest request);
    void validateNameNotNullOrEmpty(LessonRequest request);
    void validateStartTimeNotNull(LessonRequest request);

}
