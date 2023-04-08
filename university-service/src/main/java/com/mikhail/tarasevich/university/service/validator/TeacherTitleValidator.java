package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;

public interface TeacherTitleValidator {

    void validateUniqueNameInDB(TeacherTitleRequest request);
    void validateNameNotNullOrEmpty(TeacherTitleRequest request);

}
