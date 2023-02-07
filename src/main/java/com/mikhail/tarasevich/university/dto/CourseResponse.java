package com.mikhail.tarasevich.university.dto;

import java.util.Objects;

public class CourseResponse {

    private int id;
    private String name;
    private String description;

    public CourseResponse() {
    }

    public CourseResponse(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof CourseResponse)) return false;
        CourseResponse that = (CourseResponse) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "CourseResponse{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
    }

}
