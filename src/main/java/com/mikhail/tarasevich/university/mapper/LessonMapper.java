package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.*;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {Group.class, Teacher.class, Course.class, LessonType.class})
public interface LessonMapper {

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    @Mapping(target = "withGroup",
            expression = "java(Group.builder().withId(request.getGroupId()).build())")
    @Mapping(target = "withTeacher",
            expression = "java(Teacher.builder().withId(request.getTeacherId()).build())")
    @Mapping(target = "withCourse",
            expression = "java(Course.builder().withId(request.getCourseId()).build())")
    @Mapping(target = "withLessonType",
            expression = "java(LessonType.builder().withId(request.getLessonTypeId()).build())")
    @Mapping(target = "withStartTime", source = "startTime")
    Lesson toEntity(LessonRequest request);

    LessonResponse toResponse(Lesson entity);

}
