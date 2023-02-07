package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.mapper.CourseMapper;
import org.springframework.stereotype.Component;

@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public Course toEntity(CourseRequest courseRequest) {
        return Course.builder()
                .withId(courseRequest.getId())
                .withName(courseRequest.getName())
                .withDescription(courseRequest.getDescription())
                .build();
    }

    @Override
    public CourseResponse toResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getDescription());
    }

}
