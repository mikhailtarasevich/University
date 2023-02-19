package com.mikhail.tarasevich.university.dto;

import java.util.Objects;

public class FacultyRequest extends DtoWithUniqueName {

    private String description;

    public FacultyRequest() {

    }

    public FacultyRequest(int id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacultyRequest)) return false;
        if (!super.equals(o)) return false;
        FacultyRequest that = (FacultyRequest) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription());
    }

    @Override
    public String toString() {
        return "FacultyRequest{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
    }

}
