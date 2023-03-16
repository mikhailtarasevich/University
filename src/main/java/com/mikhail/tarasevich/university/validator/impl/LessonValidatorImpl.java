package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.validator.LessonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonValidatorImpl implements LessonValidator {

    private final LessonDao dao;

    @Autowired
    public LessonValidatorImpl(LessonDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(LessonRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The lesson with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(LessonRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The lesson name can't be null or empty.");
    }

    @Override
    public void validateStartTimeNotNull(LessonRequest request) {
        if (request.getStartTime() == null)
            throw new IncorrectRequestDataException("Please, set lesson start time.");
    }

}
