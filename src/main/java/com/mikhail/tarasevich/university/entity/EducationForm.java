package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class EducationForm {

    private static final Logger LOG = LoggerFactory.getLogger(EducationForm.class);
    private final int id;
    private final String name;

    private EducationForm(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        LOG.info("Education form entity was made with parameters: id = {}, name = {}.", id, name);
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
        if (!(o instanceof EducationForm)) return false;
        EducationForm educationForm = (EducationForm) o;
        return getId() == educationForm.getId() && Objects.equals(getName(), educationForm.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "EducationForm{" +
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

        public EducationForm build() {
            return new EducationForm(this);
        }

    }

}
