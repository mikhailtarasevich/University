package com.mikhail.tarasevich.university.entity;

import lombok.*;

import java.time.Duration;

@Builder(setterPrefix = "with")
@Value
public class LessonType {

    int id;
    String name;
    Duration duration;

}
