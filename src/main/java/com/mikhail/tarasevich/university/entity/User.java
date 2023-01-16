package com.mikhail.tarasevich.university.entity;

import java.util.Objects;

public abstract class User {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected String email;
    protected String password;

    protected User(Builder<?> builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.email = builder.email;
        this.password = builder.password;
    }

    public int getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) && getGender() == user.getGender() &&
                Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getGender(), getEmail(), getPassword());
    }

    abstract static class Builder<T extends Builder> {

        protected int id;
        protected String firstName;
        protected String lastName;
        protected Gender gender;
        protected String email;
        protected String password;

        public T withId(final int id) {
            this.id = id;
            return self();
        }

        public T withFirstName(final String firstName) {
            this.firstName = firstName;
            return self();
        }

        public T withLastName(final String lastName) {
            this.lastName = lastName;
            return self();
        }

        public T withGender(final Gender gender) {
            this.gender = gender;
            return self();
        }

        public T withEmail(final String email) {
            this.email = email;
            return self();
        }

        public T withPassword(final String password) {
            this.password = password;
            return self();
        }

        public abstract User build();

        protected abstract T self();

    }

}
