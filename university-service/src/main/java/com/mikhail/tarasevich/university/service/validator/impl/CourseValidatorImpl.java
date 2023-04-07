package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.CourseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseValidatorImpl implements CourseValidator {

    private final CourseDao dao;

    @Autowired
    public CourseValidatorImpl(CourseDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(CourseRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The course with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(CourseRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The course name can't be null or empty.");
    }

}
