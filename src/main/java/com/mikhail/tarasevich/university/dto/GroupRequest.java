package com.mikhail.tarasevich.university.dto;

import lombok.*;

@Data
public class GroupRequest {

    private int id;
    private String name;
    private int facultyId;
    private int headStudentId;
    private int educationFormId;

}
