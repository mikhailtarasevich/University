package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.validator.GroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupValidatorImpl implements GroupValidator {

    private final GroupDao dao;

    @Autowired
    public GroupValidatorImpl(GroupDao dao) {
        this.dao = dao;
    }

    @Override
    public void validateUniqueNameInDB(GroupRequest request) {
        if (dao.findByName(request.getName()).isPresent())
            throw new IncorrectRequestDataException("The group with specified name already exists in the database.");
    }

    @Override
    public void validateNameNotNullOrEmpty(GroupRequest request) {
        String name = request.getName();

        if (name == null || name.replaceAll("\\s", "").equals(""))
            throw new IncorrectRequestDataException("The group name can't be null or empty.");
    }

}
