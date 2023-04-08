package com.mikhail.tarasevich.university.entity;

import lombok.*;

@Builder(setterPrefix = "with")
@Value
public class Faculty {

    int id;
    String name;
    String description;

}
