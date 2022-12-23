package com.mikhail.tarasevich.entity;

import java.util.Objects;

public class Group {
    private final int id;
    private final String name;
    private final Faculty faculty;
    private final Student headStudent;
    private final EducationForm educationForm;

    private Group(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.faculty = builder.faculty;
        this.headStudent = builder.headStudent;
        this.educationForm = builder.educationForm;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Student getHeadStudent() {
        return headStudent;
    }

    public EducationForm getEducationForm() {
        return educationForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return getId() == group.getId() && Objects.equals(getName(), group.getName()) &&
                Objects.equals(getFaculty(), group.getFaculty()) &&
                Objects.equals(getHeadStudent(), group.getHeadStudent()) &&
                Objects.equals(getEducationForm(), group.getEducationForm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getFaculty(), getHeadStudent(), getEducationForm());
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", faculty=" + faculty +
                ", headStudent=" + headStudent +
                ", educationForm=" + educationForm +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;
        private Faculty faculty;
        private Student headStudent;
        private EducationForm educationForm;

        private Builder() {
        }

        public Builder withId(final int id) {
            this.id = id;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withFaculty(final Faculty faculty) {
            this.faculty = faculty;
            return this;
        }

        public Builder withHeadStudent(final Student headStudent) {
            this.headStudent = headStudent;
            return this;
        }

        public Builder withEducationForm(final EducationForm educationForm) {
            this.educationForm = educationForm;
            return this;
        }

        public Group build() {
            return new Group(this);
        }
    }

}
