package com.mikhail.tarasevich.university.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder(setterPrefix = "with")
@Getter
@EqualsAndHashCode
@ToString
public abstract class User {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected String email;
    protected String password;

}
