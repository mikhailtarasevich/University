package com.mikhail.tarasevich.university.dto;

import lombok.Data;

import java.time.Duration;

@Data
public class LessonTypeRequest {

    private int id;
    private String name;
    private Duration duration;

}
