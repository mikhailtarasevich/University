package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.Group;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentResponse extends UserResponse {

    private Group group;

}
