package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.mapper.TeacherMapper;
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
                .withTeacherTitle(TeacherTitle.builder().withId(t.getTeacherTitleId()).build())
                .withDepartment(Department.builder().withId(t.getDepartmentId()).build())
                .build();
    }

    @Override
    public TeacherResponse toResponse(Teacher t) {
        TeacherResponse teacherResponse = new TeacherResponse();
        teacherResponse.setId(t.getId());
        teacherResponse.setFirstName(t.getFirstName());
        teacherResponse.setLastName(t.getLastName());
        teacherResponse.setGender(t.getGender());
        teacherResponse.setEmail(t.getEmail());
        teacherResponse.setTeacherTitle(t.getTeacherTitle());
        teacherResponse.setDepartment(t.getDepartment());

        return teacherResponse;
    }

}
