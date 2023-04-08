package com.mikhail.tarasevich.university.dto;

import lombok.*;

import java.util.List;

@Data
public class DepartmentRequest {

    private int id;
    private String name;
    private String description;
    private List<Integer> courseIds;
    private List<Integer> teacherIds;

}
