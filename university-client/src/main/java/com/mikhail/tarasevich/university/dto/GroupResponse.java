package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.entity.Student;
import lombok.*;

@Data
public class GroupResponse {

    private int id;
    private String name;
    private Faculty faculty;
    private Student headStudent;
    private EducationForm educationForm;

}
