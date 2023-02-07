package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.*;

import java.util.List;
import java.util.Objects;

public class TeacherRequest extends UserRequest {

    private List<Group> groups;
    private List<Course> courses;
    private TeacherTitle teacherTitle;
    private Department department;

    public TeacherRequest() {
    }

    public TeacherRequest(int id, String firstName, String lastName, Gender gender, String email, String password, List<Group> groups, List<Course> courses, TeacherTitle teacherTitle, Department department) {
        super(id, firstName, lastName, gender, email, password);
        this.groups = groups;
        this.courses = courses;
        this.teacherTitle = teacherTitle;
        this.department = department;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public TeacherTitle getTeacherTitle() {
        return teacherTitle;
    }

    public void setTeacherTitle(TeacherTitle teacherTitle) {
        this.teacherTitle = teacherTitle;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherRequest)) return false;
        if (!super.equals(o)) return false;
        TeacherRequest that = (TeacherRequest) o;
        return Objects.equals(getGroups(), that.getGroups()) && Objects.equals(getCourses(), that.getCourses()) && Objects.equals(getTeacherTitle(), that.getTeacherTitle()) && Objects.equals(getDepartment(), that.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGroups(), getCourses(), getTeacherTitle(), getDepartment());
    }

    @Override
    public String toString() {
        return "TeacherRequest{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", gender=" + gender +
                ", email=" + email +
                ", password=" + password +
                ", groups=" + groups +
                ", courses=" + courses +
                ", teacherTitle=" + teacherTitle +
                ", department=" + department +
                '}';
    }

}
