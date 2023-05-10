package com.mikhail.tarasevich.university.service.validator.impl;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.GroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupValidatorImpl implements GroupValidator {

    private final GroupRepository dao;

    @Autowired
    public GroupValidatorImpl(GroupRepository dao) {
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
