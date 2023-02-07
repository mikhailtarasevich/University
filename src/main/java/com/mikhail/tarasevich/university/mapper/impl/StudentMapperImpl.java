package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.*;
import com.mikhail.tarasevich.university.mapper.StudentMapper;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student toEntity(StudentRequest s) {
        return Student.builder()
                .withId(s.getId())
                .withFirstName(s.getFirstName())
                .withLastName(s.getLastName())
                .withGender(s.getGender())
                .withEmail(s.getEmail())
                .withPassword(s.getPassword())
                .withGroup(s.getGroup())
                .build();
    }

    @Override
    public StudentResponse toResponse(Student s) {
        return new StudentResponse(s.getId(), s.getFirstName(), s.getLastName(),
                s.getGender(), s.getEmail(), s.getGroup());
    }

}
