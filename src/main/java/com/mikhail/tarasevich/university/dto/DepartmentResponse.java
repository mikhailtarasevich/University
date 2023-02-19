package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Teacher;

import java.util.List;
import java.util.Objects;

public class DepartmentResponse {

    private int id;
    private String name;
    private String description;
    private List<Course> courses;
    private List<Teacher> teachers;

    public DepartmentResponse() {
    }

    public DepartmentResponse(int id, String name, String description, List<Course> courses, List<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courses = courses;
        this.teachers = teachers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof DepartmentResponse)) return false;
        DepartmentResponse that = (DepartmentResponse) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(),
                that.getDescription()) && Objects.equals(getCourses(),
                that.getCourses()) && Objects.equals(getTeachers(), that.getTeachers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getCourses(), getTeachers());
    }

    @Override
    public String toString() {
        return "DepartmentResponse{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", courses=" + courses +
                ", teachers=" + teachers +
                '}';
    }

}
