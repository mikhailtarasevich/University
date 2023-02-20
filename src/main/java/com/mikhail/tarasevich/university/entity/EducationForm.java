package com.mikhail.tarasevich.university.entity;

import lombok.*;

@Builder(setterPrefix = "with")
@Value
public class EducationForm {

    int id;
    String name;

}
