package com.mikhail.tarasevich.university.dto;

import java.util.Objects;

public abstract class DtoWithUniqueName {

    protected int id;
    protected String name;

    public DtoWithUniqueName(){}

    public DtoWithUniqueName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoWithUniqueName)) return false;
        DtoWithUniqueName that = (DtoWithUniqueName) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

}
