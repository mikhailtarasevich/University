package com.mikhail.tarasevich.university.dto;

import java.util.Objects;

public class CourseRequest extends DtoWithUniqueName {

    private String description;

    public CourseRequest() {
    }

    public CourseRequest(int id, String name, String description) {
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
        if (!(o instanceof CourseRequest)) return false;
        if (!super.equals(o)) return false;
        CourseRequest that = (CourseRequest) o;
        return Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescription());
    }

    @Override
    public String toString() {
        return "CourseRequest{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
    }

}
