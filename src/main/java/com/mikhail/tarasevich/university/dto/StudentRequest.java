package com.mikhail.tarasevich.university.dto;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentRequest extends UserRequest {

    private int groupId;

}
