package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.mapper.TeacherMapper;
import com.mikhail.tarasevich.university.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapperImpl implements TeacherMapper {

    @Override
    public Teacher toEntity(TeacherRequest t) {
        return Teacher.builder()
                .withId(t.getId())
                .withFirstName(t.getFirstName())
                .withLastName(t.getLastName())
                .withGender(t.getGender())
                .withEmail(t.getEmail())
                .withPassword(t.getPassword())
                .withTeacherTitle(t.getTeacherTitle())
                .withDepartment(t.getDepartment())
                .build();
    }

    @Override
    public TeacherResponse toResponse(Teacher t) {
        return new TeacherResponse(t.getId(), t.getFirstName(), t.getLastName(), t.getGender(), t.getEmail(),
                t.getGroups(), t.getCourses(), t.getTeacherTitle(), t.getDepartment());
    }

}
