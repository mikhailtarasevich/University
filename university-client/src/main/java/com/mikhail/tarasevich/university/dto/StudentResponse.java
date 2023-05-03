package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentResponse extends UserResponse {

    private Group group;

}
