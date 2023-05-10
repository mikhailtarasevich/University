package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.LessonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonValidatorImpl implements LessonValidator {

    private final LessonRepository dao;

    @Autowired
    public LessonValidatorImpl(LessonRepository dao) {
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
