package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.repository.FacultyRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.FacultyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacultyValidatorImpl implements FacultyValidator {

    private final FacultyRepository dao;

    @Autowired
    public FacultyValidatorImpl(FacultyRepository dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(FacultyRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The faculty with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(FacultyRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The faculty name can't be null or empty.");
    }

}
