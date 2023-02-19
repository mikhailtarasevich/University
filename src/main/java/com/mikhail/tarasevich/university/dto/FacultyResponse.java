package com.mikhail.tarasevich.university.dto;

import java.util.Objects;

public class FacultyResponse extends DtoWithUniqueName {

    private String description;

    public FacultyResponse() {

    }

    public FacultyResponse(int id, String name, String description) {
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
        if (!(o instanceof FacultyResponse)) return false;
        if (!super.equals(o)) return false;
        FacultyResponse that = (FacultyResponse) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription());
    }

    @Override
    public String toString() {
        return "FacultyResponse{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
    }

}
