package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.entity.Course;

public interface CourseMapper extends Mapper<CourseRequest, CourseResponse, Course> {
}
