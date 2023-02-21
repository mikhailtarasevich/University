package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.entity.Teacher;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class LessonRequest {

    private int id;
    private String name;
    private Group group;
    private Teacher teacher;
    private Course course;
    private LessonType lessonType;
    private LocalDateTime startTime;

}
