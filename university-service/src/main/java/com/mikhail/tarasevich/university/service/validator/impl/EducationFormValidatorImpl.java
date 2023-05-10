package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.repository.EducationFormRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.EducationFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EducationFormValidatorImpl  implements EducationFormValidator {

    private final EducationFormRepository dao;

    @Autowired
    public EducationFormValidatorImpl(EducationFormRepository dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(EducationFormRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The education form with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(EducationFormRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The education form name can't be null or empty.");
    }

}
