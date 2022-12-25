package com.mikhail.tarasevich.entity;

import java.util.Objects;

public class Student extends User {
    private final Group group;

    private Student(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.email = builder.email;
        this.password = builder.password;
        this.group = builder.group;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return getId() == student.getId() && getFirstName().equals(student.getFirstName()) &&
                getLastName().equals(student.getLastName()) && getGender() == student.getGender() &&
                getEmail().equals(student.getEmail()) && getPassword().equals(student.getPassword()) &&
                getGroup().equals(student.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getGender(), getEmail(), getPassword(), getGroup());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", group='" + group + '\'' +
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
        private Group group;

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

        public Builder withGroup(final Group group) {
            this.group = group;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

}
