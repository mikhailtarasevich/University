package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;

import java.util.List;

public interface TeacherService extends UserService<TeacherRequest, TeacherResponse> {

    void unsubscribeTeacherFromGroup(int userId, int groupId);
    void subscribeTeacherToCourse(int teacherId, int courseId);
    void unsubscribeTeacherFromCourse(int teacherId, int courseId);
    List<TeacherResponse> findTeachersRelateToGroup(int groupId);
    List<TeacherResponse> findTeachersRelateToCourse(int courseId);
    List<TeacherResponse> findTeachersRelateToDepartment(int departmentId);

}
