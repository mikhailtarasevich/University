package com.mikhail.tarasevich.university.entity;

import lombok.Builder;
import lombok.Value;

@Builder(setterPrefix = "with")
@Value
public class Role {

    int id;
    String name;

}
