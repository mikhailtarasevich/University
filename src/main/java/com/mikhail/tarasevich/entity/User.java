package com.mikhail.tarasevich.entity;

public abstract class User {
    protected long id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected String email;
    protected String password;

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
