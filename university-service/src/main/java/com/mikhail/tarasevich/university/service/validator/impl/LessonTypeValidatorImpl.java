package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.LessonTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonTypeValidatorImpl implements LessonTypeValidator {

    private final LessonTypeDao dao;

    @Autowired
    public LessonTypeValidatorImpl(LessonTypeDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(LessonTypeRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The lesson type with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(LessonTypeRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The lesson type name can't be null or empty.");
    }

}
