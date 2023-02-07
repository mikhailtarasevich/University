package com.mikhail.tarasevich.university.dto;

public class TeacherTitleRequest extends DtoWithUniqueName {

    public TeacherTitleRequest() {
    }

    public TeacherTitleRequest(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "TeacherTitleRequest{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
