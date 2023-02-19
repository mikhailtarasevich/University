package com.mikhail.tarasevich.university.dto;

import java.time.Duration;
import java.util.Objects;

public class LessonTypeRequest extends DtoWithUniqueName {

    private Duration duration;

    public LessonTypeRequest() {
    }

    public LessonTypeRequest(int id, String name, Duration duration) {
        super(id, name);
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonTypeRequest)) return false;
        if (!super.equals(o)) return false;
        LessonTypeRequest that = (LessonTypeRequest) o;
        return Objects.equals(getDuration(), that.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDuration());
    }

    @Override
    public String toString() {
        return "LessonTypeRequest{" +
                "id=" + id +
                ", name=" + name +
                ", duration=" + duration +
                '}';
    }

}
