package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class Department {

    private static final Logger LOG = LoggerFactory.getLogger(Department.class);
    private final int id;
    private final String name;
    private final String description;
    private final List<Course> courses;
    private final List<Teacher> teachers;

    private Department(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.courses = builder.courses;
        this.teachers = builder.teachers;
        LOG.info("Department was made with parameters: id = {}, name = {}, description = {}, courses = {}, " +
                        "teachers = {}.", id, name, description , courses, teachers);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department department = (Department) o;
        return getId() == department.getId() && Objects.equals(getName(), department.getName()) &&
                Objects.equals(getDescription(), department.getDescription()) &&
                Objects.equals(getCourses(), department.getCourses()) &&
                Objects.equals(getTeachers(), department.getTeachers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getCourses(), getTeachers());
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", courses=" + courses +
                ", teachers=" + teachers +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int id;
        private String name;
        private String description;
        private List<Course> courses;
        private List<Teacher> teachers;

        private Builder() {
        }

        public Builder withId(final int id) {
            this.id = id;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withCourses(final List<Course> courses) {
            this.courses = courses;
            return this;
        }

        public Builder withTeachers(final List<Teacher> teachers) {
            this.teachers = teachers;
            return this;
        }

        public Department build() {
            return new Department(this);
        }

    }

}
