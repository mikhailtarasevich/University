package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.DepartmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepartmentValidatorImpl implements DepartmentValidator {

    private final DepartmentDao dao;

    @Autowired
    public DepartmentValidatorImpl(DepartmentDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(DepartmentRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The department with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(DepartmentRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The department name can't be null or empty.");
    }

}
