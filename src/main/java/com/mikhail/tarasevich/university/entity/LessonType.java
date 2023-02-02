package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

public class LessonType {

    private static final Logger LOG = LoggerFactory.getLogger(LessonType.class);
    private final int id;
    private final String name;
    private final Duration duration;

    private LessonType(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.duration = builder.duration;
        LOG.info("Lesson type was made with parameters: id = {}, name = {}, duration = {}",
                id, name, duration);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonType)) return false;
        LessonType that = (LessonType) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDuration(), that.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDuration());
    }

    @Override
    public String toString() {
        return "LessonType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;
        private Duration duration;

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

        public Builder withDuration(final Duration duration) {
            this.duration = duration;
            return this;
        }

        public LessonType build() {
            return new LessonType(this);
        }

    }

}
