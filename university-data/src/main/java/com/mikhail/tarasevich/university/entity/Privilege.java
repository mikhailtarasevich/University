package com.mikhail.tarasevich.university.entity;

import lombok.Builder;
import lombok.Value;

@Builder(setterPrefix = "with")
@Value
public class Privilege {

    int id;
    String name;

}
