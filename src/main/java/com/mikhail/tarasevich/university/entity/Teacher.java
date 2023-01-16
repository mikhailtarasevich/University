package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class Teacher extends User {

    private static final Logger LOG = LoggerFactory.getLogger(Teacher.class);
    private final List<Group> groups;
    private final List<Course> courses;
    private final TeacherTitle teacherTitle;
    private final Department department;

    private Teacher(Builder builder) {
        super(builder);
        this.groups = builder.groups;
        this.courses = builder.courses;
        this.teacherTitle = builder.teacherTitle;
        this.department = builder.department;
        LOG.info("Teacher was made with parameters: id = {}, firstName = {}, lastName = {}, gender = {}, email = {}," +
                        "password = {}, groups = {}, courses = {}, teacherTitle = {}, department = {}.",
                id, firstName, lastName, gender, email, password, groups, courses, teacherTitle, department);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public TeacherTitle getTeacherTitle() {
        return teacherTitle;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(getGroups(), teacher.getGroups()) &&
                Objects.equals(getCourses(), teacher.getCourses()) &&
                Objects.equals(getTeacherTitle(), teacher.getTeacherTitle()) &&
                Objects.equals(getDepartment(), teacher.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGroups(), getCourses(), getTeacherTitle(), getDepartment());
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", groups='" + groups + '\'' +
                ", courses='" + courses + '\'' +
                ", teacherTitle='" + teacherTitle + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends User.Builder<Builder> {

        private List<Group> groups;
        private List<Course> courses;
        private TeacherTitle teacherTitle;
        private Department department;

        public Builder withGroups(final List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder withCourses(final List<Course> courses) {
            this.courses = courses;
            return this;
        }

        public Builder withTeacherTitle(final TeacherTitle teacherTitle) {
            this.teacherTitle = teacherTitle;
            return this;
        }

        public Builder withDepartment(final Department department) {
            this.department = department;
            return this;
        }

        @Override
        public Teacher build() {
            return new Teacher(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

    }

}
