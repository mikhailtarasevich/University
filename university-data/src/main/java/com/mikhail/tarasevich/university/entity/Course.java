package com.mikhail.tarasevich.university.entity;

import lombok.*;

@Builder(setterPrefix = "with")
@Value
public class Course {

    int id;
    String name;
    String description;

}
