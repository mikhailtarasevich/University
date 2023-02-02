package com.mikhail.tarasevich.university.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

public class Lesson {

    private static final Logger LOG = LoggerFactory.getLogger(Lesson.class);
    private final int id;
    private final String name;
    private final Group group;
    private final Teacher teacher;
    private final Course course;
    private final LessonType lessonType;
    private final LocalDateTime startTime;

    private Lesson(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.group = builder.group;
        this.teacher = builder.teacher;
        this.course = builder.course;
        this.lessonType = builder.lessonType;
        this.startTime = builder.startTime;
        LOG.info("Lesson was made with parameters: id = {}, name = {}, group = {}, teacher = {}, " +
                "course = {}, lesson type = {}, start time = {}.",
                id, name, group, teacher, course, lessonType, startTime);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return getId() == lesson.getId() && Objects.equals(getName(), lesson.getName()) &&
                Objects.equals(getGroup(), lesson.getGroup()) &&
                Objects.equals(getTeacher(), lesson.getTeacher()) &&
                Objects.equals(getCourse(), lesson.getCourse()) &&
                Objects.equals(getLessonType(), lesson.getLessonType()) &&
                Objects.equals(getStartTime(), lesson.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getGroup(), getTeacher(), getCourse(), getLessonType(), getStartTime());
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group +
                ", teacher=" + teacher +
                ", course=" + course +
                ", lessonType=" + lessonType +
                ", startTime=" + startTime +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String name;
        private Group group;
        private Teacher teacher;
        private Course course;
        private LessonType lessonType;
        private LocalDateTime startTime;

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

        public Builder withGroup(final Group group) {
            this.group = group;
            return this;
        }

        public Builder withTeacher(final Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder withCourse(final Course course) {
            this.course = course;
            return this;
        }

        public Builder withLessonType(final LessonType lessonType) {
            this.lessonType = lessonType;
            return this;
        }

        public Builder withStartTime(final LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Lesson build() {
            return new Lesson(this);
        }

    }

}
