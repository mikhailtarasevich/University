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
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setId(d.getId());
        departmentResponse.setName(d.getName());
        departmentResponse.setDescription(d.getDescription());
        departmentResponse.setCourses(d.getCourses());
        departmentResponse.setTeachers(d.getTeachers());

        return departmentResponse;
    }

}
