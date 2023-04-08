package com.mikhail.tarasevich.university.service.validator;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;

public interface DepartmentValidator {

    void validateUniqueNameInDB(DepartmentRequest request);
    void validateNameNotNullOrEmpty(DepartmentRequest request);

}
