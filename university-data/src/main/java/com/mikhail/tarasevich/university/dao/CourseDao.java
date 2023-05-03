package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Course;

import java.util.List;

public interface CourseDao extends CrudPageableDao<Course> {

    List<Course> findCoursesRelateToDepartment(int departmentId);
    List<Course> findCoursesRelateToTeacher(int teacherId);
    void unbindCoursesFromTeacher(int teacherId);
    void unbindCoursesFromDepartment(int departmentId);

}
