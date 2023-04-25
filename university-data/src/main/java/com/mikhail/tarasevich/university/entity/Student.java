package com.mikhail.tarasevich.university.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("student")
public class Student extends User {

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    Group group;

}
