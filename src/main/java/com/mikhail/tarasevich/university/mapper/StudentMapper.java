package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Student;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(config = UserMapper.class, componentModel = MappingConstants.ComponentModel.SPRING, imports = Group.class)
public interface StudentMapper extends UserMapper<StudentRequest, StudentResponse, Student> {

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "withGroup",
            expression = "java(Group.builder().withId(request.getGroupId()).build())")
    Student toEntity(StudentRequest request);

    StudentResponse toResponse(Student entity);

}
