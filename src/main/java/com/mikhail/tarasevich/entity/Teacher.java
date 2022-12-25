package com.mikhail.tarasevich.entity;

import java.util.List;
import java.util.Objects;

public class Teacher extends User {
    private final List<Group> groups;
    private final List<Course> courses;
    private final TeacherTitle teacherTitle;
    private final Department department;

    private Teacher(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.email = builder.email;
        this.password = builder.password;
        this.groups = builder.groups;
        this.courses = builder.courses;
        this.teacherTitle = builder.teacherTitle;
        this.department = builder.department;
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
        Teacher teacher = (Teacher) o;
        return getId() == teacher.getId() && getFirstName().equals(teacher.getFirstName()) &&
                getLastName().equals(teacher.getLastName()) && getGender() == teacher.getGender() &&
                getEmail().equals(teacher.getEmail()) && getPassword().equals(teacher.getPassword()) &&
                getGroups().equals(teacher.getGroups()) && getCourses().equals(teacher.getCourses()) &&
                getTeacherTitle().equals(teacher.getTeacherTitle()) && getDepartment().equals(teacher.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getGender(), getEmail(), getPassword(),
                getGroups(), getCourses(), getTeacherTitle(), getDepartment());
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

    public static class Builder {
        private int id;
        private String firstName;
        private String lastName;
        private Gender gender;
        private String email;
        private String password;
        private List<Group> groups;
        private List<Course> courses;
        private TeacherTitle teacherTitle;
        private Department department;

        private Builder() {
        }

        public Builder withId(final int id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withGender(final Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(final String password) {
            this.password = password;
            return this;
        }

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

        public Teacher build() {
            return new Teacher(this);
        }
    }

}
