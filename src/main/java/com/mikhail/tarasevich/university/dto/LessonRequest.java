package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.entity.Teacher;

import java.time.LocalDateTime;
import java.util.Objects;

public class LessonRequest extends DtoWithUniqueName {

    private Group group;
    private Teacher teacher;
    private Course course;
    private LessonType lessonType;
    private LocalDateTime startTime;

    public LessonRequest() {
    }

    public LessonRequest(int id, String name, Group group, Teacher teacher, Course course, LessonType lessonType,
                         LocalDateTime startTime) {
        super(id, name);
        this.group = group;
        this.teacher = teacher;
        this.course = course;
        this.lessonType = lessonType;
        this.startTime = startTime;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessonRequest)) return false;
        if (!super.equals(o)) return false;
        LessonRequest that = (LessonRequest) o;
        return Objects.equals(getGroup(), that.getGroup()) && Objects.equals(getTeacher(),
                that.getTeacher()) && Objects.equals(getCourse(), that.getCourse()) && Objects.equals(getLessonType(),
                that.getLessonType()) && Objects.equals(getStartTime(), that.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGroup(), getTeacher(), getCourse(), getLessonType(), getStartTime());
    }

    @Override
    public String toString() {
        return "LessonRequest{" +
                "id=" + id +
                ", name=" + name +
                ", group=" + group +
                ", teacher=" + teacher +
                ", course=" + course +
                ", lessonType=" + lessonType +
                ", startTime=" + startTime +
                '}';
    }

}
