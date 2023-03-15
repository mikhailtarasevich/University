package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;

import java.util.List;

public interface CourseService extends CrudService<CourseRequest, CourseResponse> {

    List<CourseResponse> findCoursesRelateToDepartment(int departmentId);
    List<CourseResponse> findCoursesRelateToTeacher(int teacherId);
    List<CourseResponse> findCoursesRelateToDepartmentNotRelateToTeacher(int departmentId, int teacherId);
    void subscribeCourseToDepartment(int departmentId, int courseId);
    void unsubscribeCourseFromDepartment(int departmentId, int courseId);

}
