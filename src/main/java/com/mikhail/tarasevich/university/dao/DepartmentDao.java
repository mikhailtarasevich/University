package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Department;

public interface DepartmentDao extends CrudPageableDao<Department>{

    void addCourseToDepartment(int departmentId, Integer courseId);
    void deleteCourseFromDepartment(int departmentId, Integer courseId);
    void unbindDepartmentsFromCourse(int courseId);

}
