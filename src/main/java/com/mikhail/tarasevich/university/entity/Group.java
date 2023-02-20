package com.mikhail.tarasevich.university.entity;

import lombok.*;

@Builder(setterPrefix = "with")
@Value
public class Group {

    int id;
    String name;
    Faculty faculty;
    Student headStudent;
    EducationForm educationForm;

}
