package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Faculty {

    private static final Logger LOG = LoggerFactory.getLogger(Faculty.class);
    private final int id;
    private final String name;
    private final String description;

    private Faculty(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        LOG.info("Education form entity was made with parameters: id = {}, name = {}, description = {}.",
                id, name, description);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty)) return false;
        Faculty faculty = (Faculty) o;
        return getId() == faculty.getId() && Objects.equals(getName(), faculty.getName()) &&
                Objects.equals(getDescription(), faculty.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription());
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;
        private String description;

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

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Faculty build() {
            return new Faculty(this);
        }

    }

}
