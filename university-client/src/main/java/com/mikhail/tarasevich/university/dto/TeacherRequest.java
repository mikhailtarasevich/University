package com.mikhail.tarasevich.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TeacherRequest extends UserRequest {

    private List<Integer> groupIds;
    private List<Integer> courseIds;
    private int teacherTitleId;
    private int departmentId;

}
