package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Group;

import java.util.List;

public interface GroupDao extends CrudPageableDao<Group>{

    void changeFaculty(int groupId, int newFacultyId);
    void changeHeadUser(int groupId, int newUserId);
    void changeEducationForm(int groupId, int newEducationFormId);
    List<Group> findGroupsRelateToTeacher (int teacherId);
    List<Group> findGroupsNotRelateToTeacher (int teacherId);
    void unbindGroupsFromStudent(int studentId);
    void unbindGroupsFromTeacher(int teacherId);
    void unbindGroupsFromEducationForm(int educationFormId);
    void unbindGroupsFromFaculty(int facultyId);

}
