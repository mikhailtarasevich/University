package com.mikhail.tarasevich.university.dto;

public class EducationFormRequest extends DtoWithUniqueName {

    public EducationFormRequest() {
    }

    public EducationFormRequest(int id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "EducationFormRequest{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
