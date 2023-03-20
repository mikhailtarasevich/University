package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Student;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import com.mikhail.tarasevich.university.entity.Faculty;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {Faculty.class, Student.class, EducationForm.class})
public interface GroupMapper {

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    @Mapping(target = "withFaculty",
            expression = "java(Faculty.builder().withId(request.getFacultyId()).build())")
    @Mapping(target = "withHeadStudent",
            expression = "java(Student.builder().withId(request.getHeadStudentId()).build())")
    @Mapping(target = "withEducationForm",
            expression = "java(EducationForm.builder().withId(request.getEducationFormId()).build())")
    Group toEntity(GroupRequest request);

    GroupResponse toResponse(Group entity);

}
