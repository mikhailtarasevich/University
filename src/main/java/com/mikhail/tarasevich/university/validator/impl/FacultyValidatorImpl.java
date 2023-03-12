package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.validator.FacultyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacultyValidatorImpl implements FacultyValidator {

    private final FacultyDao dao;

    @Autowired
    public FacultyValidatorImpl(FacultyDao dao) {
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
