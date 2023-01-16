package com.mikhail.tarasevich.university.entity;

import java.util.Arrays;

public enum Gender {

    MALE(0), FEMALE(1);

    private int id;

    Gender(int id){this.id = id;}

    public static Gender getById(int id){
        return Arrays.stream(Gender.values())
                .filter(gender-> gender.id == id)
                .findFirst().orElse(null);
    }

    public int getId(){return id;}

}
