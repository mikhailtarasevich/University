package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.TeacherTitleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeacherTitleValidatorImpl implements TeacherTitleValidator {

    private final TeacherTitleDao dao;

    @Autowired
    public TeacherTitleValidatorImpl(TeacherTitleDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(TeacherTitleRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The teacher title with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(TeacherTitleRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The teacher title name can't be null or empty.");
    }

}
