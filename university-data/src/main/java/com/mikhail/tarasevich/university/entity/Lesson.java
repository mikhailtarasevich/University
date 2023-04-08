package com.mikhail.tarasevich.university.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
@Value
public class Lesson {

    int id;
    String name;
    Group group;
    Teacher teacher;
    Course course;
    LessonType lessonType;
    LocalDateTime startTime;

}
