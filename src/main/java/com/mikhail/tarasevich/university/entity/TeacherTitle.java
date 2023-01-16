package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TeacherTitle {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherTitle.class);
    private final int id;
    private final String name;

    private TeacherTitle(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        LOG.info("Teacher Title was made with parameters: id = {}, name = {}.", id, name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherTitle)) return false;
        TeacherTitle that = (TeacherTitle) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "TeacherTitle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;

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

        public TeacherTitle build() {
            return new TeacherTitle(this);
        }

    }

}