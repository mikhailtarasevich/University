package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Student extends User {

    private static final Logger LOG = LoggerFactory.getLogger(Student.class);
    private final Group group;

    private Student(Builder builder) {
        super(builder);
        this.group = builder.group;
        LOG.info("Student was made with parameters: id = {}, firstName = {}, lastName = {}, gender = {}, email = {}," +
                "password = {}, group = {}", id, firstName, lastName, gender, email, password, group);
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(getGroup(), student.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGroup());
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

    public static class Builder extends User.Builder<Builder> {

        private Group group;

        public Builder withGroup(final Group group) {
            this.group = group;
            return this;
        }

        @Override
        public Student build() {
            return new Student(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

    }

}
