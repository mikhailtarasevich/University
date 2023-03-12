package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;

import java.util.List;

public interface TeacherService extends UserService<TeacherRequest, TeacherResponse> {

    void changeTeacherTeacherTitle(int teacherId, int teacherTitleId);
    void changeTeacherDepartment(int teacherId, int departmentId);
    void subscribeTeacherToCourse(int teacherId, int courseId);
    void unsubscribeTeacherFromCourse(int teacherId, int courseId);
    void subscribeTeacherToGroups(int teacherId, List<Integer> groupIds);
    void unsubscribeTeacherFromGroups(int teacherId, List<Integer> groupIds);
    void subscribeTeacherToCourses(int teacherId, List<Integer> courseIds);
    void unsubscribeTeacherFromCourses(int teacherId, List<Integer> courseIds);
    List<TeacherResponse> findTeachersRelateToGroup(int groupId);
    List<TeacherResponse> findTeachersRelateToCourse(int courseId);
    List<TeacherResponse> findTeachersRelateToDepartment(int departmentId);

}
