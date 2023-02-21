package com.mikhail.tarasevich.university.validator;

import com.mikhail.tarasevich.university.dto.LessonRequest;

public interface LessonValidator {

    void validateUniqueNameInDB(LessonRequest request);
    void validateNameNotNullOrEmpty(LessonRequest request);

}
