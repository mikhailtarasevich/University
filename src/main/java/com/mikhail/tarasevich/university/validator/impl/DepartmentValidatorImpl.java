package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.validator.DepartmentValidator;
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
            throw new IncorrectRequestData("The department with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(DepartmentRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestData("The department name can't be null or empty.");
    }

}
