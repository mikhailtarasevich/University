package com.mikhail.tarasevich.university.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class LessonRequest {

    private int id;
    private String name;
    private int groupId;
    private int teacherId;
    private int courseId;
    private int lessonTypeId;
    private LocalDateTime startTime;

}
