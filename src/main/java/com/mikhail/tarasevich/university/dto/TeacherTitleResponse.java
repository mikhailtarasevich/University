package com.mikhail.tarasevich.university.dto;

public class TeacherTitleResponse extends DtoWithUniqueName{

    public TeacherTitleResponse() {
    }

    public TeacherTitleResponse(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "TeacherTitleResponse{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }

}
