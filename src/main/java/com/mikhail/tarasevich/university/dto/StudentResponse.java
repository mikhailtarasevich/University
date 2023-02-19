package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.Group;

import java.util.Objects;

public class StudentResponse extends UserResponse {

    private Group group;

    public StudentResponse() {
    }

    public StudentResponse(int id, String firstName, String lastName, Gender gender, String email, Group group) {
        super(id, firstName, lastName, gender, email);
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
        if (!(o instanceof StudentResponse)) return false;
        if (!super.equals(o)) return false;
        StudentResponse that = (StudentResponse) o;
        return Objects.equals(getGroup(), that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGroup());
    }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", gender=" + gender +
                ", email=" + email +
                ", group=" + group +
                '}';
    }

}
