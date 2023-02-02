package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Student;

import java.util.List;

public interface StudentDao extends UserDao<Student> {

    void deleteStudentFromGroup(int userId);
    List<Student> findStudentsRelateToGroup(int groupId);

}
