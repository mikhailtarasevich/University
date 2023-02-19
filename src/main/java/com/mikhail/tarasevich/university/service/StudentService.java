package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Student;

import java.util.List;

public interface StudentService extends UserService<StudentRequest, StudentResponse> {

    void unsubscribeStudentFromGroup(int userId);
    List<StudentResponse> findStudentsRelateToGroup(int groupId);

}
