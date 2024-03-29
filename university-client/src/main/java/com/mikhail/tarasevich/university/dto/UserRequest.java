package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Gender;
import lombok.Data;

@Data
public abstract class UserRequest {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected String email;
    protected String password;
    protected String confirmPassword;

}
