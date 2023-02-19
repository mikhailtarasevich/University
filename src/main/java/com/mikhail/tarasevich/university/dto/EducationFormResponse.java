package com.mikhail.tarasevich.university.dto;

public class EducationFormResponse extends DtoWithUniqueName {

    public EducationFormResponse() {
    }

    public EducationFormResponse(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "EducationFormResponse{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
