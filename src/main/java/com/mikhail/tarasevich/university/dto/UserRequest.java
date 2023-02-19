package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Gender;

import java.util.Objects;

public abstract class UserRequest {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected String email;
    protected String password;

    public UserRequest() {
    }

    public UserRequest(int id, String firstName, String lastName, Gender gender, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRequest)) return false;
        UserRequest that = (UserRequest) o;
        return getId() == that.getId() && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && getGender() == that.getGender() && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getGender(), getEmail(), getPassword());
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", gender=" + gender +
                ", email=" + email +
                ", password=" + password +
                '}';
    }

}
