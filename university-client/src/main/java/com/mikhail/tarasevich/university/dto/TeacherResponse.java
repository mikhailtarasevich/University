package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TeacherResponse extends UserResponse {

    private List<Group> groups;
    private List<Course> courses;
    private TeacherTitle teacherTitle;
    private Department department;

}
