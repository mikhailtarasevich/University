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
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(s.getId());
        studentResponse.setFirstName(s.getFirstName());
        studentResponse.setLastName(s.getLastName());
        studentResponse.setGender(s.getGender());
        studentResponse.setEmail(s.getEmail());
        studentResponse.setGroup(s.getGroup());

        return studentResponse;
    }

}
