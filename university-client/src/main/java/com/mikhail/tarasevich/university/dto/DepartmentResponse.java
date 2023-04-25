package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Teacher;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentResponse {

    private int id;
    private String name;
    private String description;
    private List<Course> courses;
    private List<Teacher> teachers;

}
