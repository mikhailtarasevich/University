package com.mikhail.tarasevich.university.entity;

import lombok.*;

import java.util.List;

@Builder(setterPrefix = "with")
@Value
public class Department {

    int id;
    String name;
    String description;
    List<Course> courses;
    List<Teacher> teachers;

}
