package com.mikhail.tarasevich.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentRequest extends UserRequest {

    private int groupId;

}
