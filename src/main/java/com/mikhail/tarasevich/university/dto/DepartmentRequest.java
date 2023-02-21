package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Teacher;
import lombok.*;

import java.util.List;

@Data
public class DepartmentRequest {

    private int id;
    private String name;
    private String description;
    private List<Course> courses;
    private List<Teacher> teachers;

}
