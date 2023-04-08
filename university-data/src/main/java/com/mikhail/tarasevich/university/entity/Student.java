package com.mikhail.tarasevich.university.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder(setterPrefix = "with")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Student extends User {

    private final Group group;

}
