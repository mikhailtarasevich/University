package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Gender;
import lombok.*;

@Data
public abstract class UserResponse {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected String email;

}
