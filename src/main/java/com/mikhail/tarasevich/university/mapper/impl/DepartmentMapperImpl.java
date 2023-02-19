package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.mapper.DepartmentMapper;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public Department toEntity(DepartmentRequest r) {
        return Department.builder()
                .withId(r.getId())
                .withName(r.getName())
                .withDescription(r.getDescription())
                .build();
    }

    @Override
    public DepartmentResponse toResponse(Department d) {
        return new DepartmentResponse(d.getId(), d.getName(), d.getDescription(), d.getCourses(), d.getTeachers());
    }

}
