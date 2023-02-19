package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Teacher;

import java.util.List;
import java.util.Objects;

public class DepartmentRequest extends DtoWithUniqueName {

    private String description;
    private List<Course> courses;
    private List<Teacher> teachers;

    public DepartmentRequest() {
    }

    public DepartmentRequest(int id, String name, String description, List<Course> courses, List<Teacher> teachers) {
        super(id, name);
        this.description = description;
        this.courses = courses;
        this.teachers = teachers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentRequest)) return false;
        if (!super.equals(o)) return false;
        DepartmentRequest that = (DepartmentRequest) o;
        return Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getCourses(), that.getCourses()) && Objects.equals(getTeachers(), that.getTeachers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription(), getCourses(), getTeachers());
    }

    @Override
    public String toString() {
        return "DepartmentRequest{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", courses=" + courses +
                ", teachers=" + teachers +
                '}';
    }

}
