package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Group;

import java.util.Objects;

public class StudentRequest extends UserRequest {

    private Group group;

    public StudentRequest() {
    }

    public StudentRequest(int id, String firstName, String lastName, Gender gender, String email, String password, Group group) {
        super(id, firstName, lastName, gender, email, password);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentRequest)) return false;
        if (!super.equals(o)) return false;
        StudentRequest that = (StudentRequest) o;
        return Objects.equals(getGroup(), that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGroup());
    }

    @Override
    public String toString() {
        return "StudentRequest{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", gender=" + gender +
                ", email=" + email +
                ", password=" + password +
                ", group=" + group +
                '}';
    }

}
