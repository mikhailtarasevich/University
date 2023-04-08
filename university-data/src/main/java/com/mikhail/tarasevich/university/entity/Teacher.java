package com.mikhail.tarasevich.university.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(setterPrefix = "with")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Teacher extends User {

    private final List<Group> groups;
    private final List<Course> courses;
    private final TeacherTitle teacherTitle;
    private final Department department;

}
