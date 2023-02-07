package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.validator.LessonTypeValidator;
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
            throw new IncorrectRequestData("The lesson type with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(LessonTypeRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestData("The lesson type name can't be null or empty.");
    }

}
