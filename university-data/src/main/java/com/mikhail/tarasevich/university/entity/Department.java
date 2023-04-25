package com.mikhail.tarasevich.university.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "departments")
    List<Course> courses;

}
