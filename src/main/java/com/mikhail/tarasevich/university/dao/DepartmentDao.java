package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.entity.Teacher;

import java.util.List;

public interface DepartmentDao extends CrudPageableDao<Department>{

    void addCourseToDepartment(int departmentId, Integer courseId);
    void deleteCourseFromDepartment(int departmentId, Integer courseId);

}
