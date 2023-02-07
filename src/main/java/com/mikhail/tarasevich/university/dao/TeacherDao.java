package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Teacher;

import java.util.List;

public interface TeacherDao extends UserDao<Teacher> {

    void addTeacherToCourse(int teacherId, Integer courseId);
    void deleteTeacherFromCourse(int teacherId, int courseId);
    void changeDepartment(int teacherId, Integer newDepartmentId);
    void changeTeacherTitle(int teacherId, Integer newTeacherTitleId);
    void deleteTeacherFromGroup(int userId, int groupId);
    List<Teacher> findTeachersRelateToGroup(int groupId);
    List<Teacher> findTeachersRelateToCourse(int courseId);
    List<Teacher> findTeachersRelateToDepartment(int departmentId);
    void unbindTeachersFromCourse(int courseId);
    void unbindTeachersFromDepartment(int departmentId);
    void unbindTeachersFromGroup(int groupId);
    void unbindTeachersFromTeacherTitle(int teacherTitleId);

}
