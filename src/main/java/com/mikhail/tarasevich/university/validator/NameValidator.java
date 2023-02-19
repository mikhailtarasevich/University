package com.mikhail.tarasevich.university.validator;

import com.mikhail.tarasevich.university.dto.DtoWithUniqueName;

public interface NameValidator<R extends DtoWithUniqueName> {

    void validateUniqueNameInDB(R request);
    void validateNameNotNullOrEmpty(R request);

}
