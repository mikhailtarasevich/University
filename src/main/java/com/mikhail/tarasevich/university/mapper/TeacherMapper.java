package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;

import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(config = UserMapper.class, componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {TeacherTitle.class, Department.class})
public interface TeacherMapper extends UserMapper<TeacherRequest, TeacherResponse, Teacher> {

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "withTeacherTitle",
            expression = "java(TeacherTitle.builder().withId(request.getTeacherTitleId()).build())")
    @Mapping(target = "withDepartment",
            expression = "java(Department.builder().withId(request.getDepartmentId()).build())")
    @Mapping(target = "withCourses", ignore = true)
    @Mapping(target = "withGroups", ignore = true)
    Teacher toEntity(TeacherRequest request);

    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "courses", ignore = true)
    TeacherResponse toResponse(Teacher entity);

}
