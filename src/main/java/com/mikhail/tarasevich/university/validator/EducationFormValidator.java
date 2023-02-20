package com.mikhail.tarasevich.university.validator;

import com.mikhail.tarasevich.university.dto.EducationFormRequest;

public interface EducationFormValidator {

    void validateUniqueNameInDB(EducationFormRequest request);
    void validateNameNotNullOrEmpty(EducationFormRequest request);

}
