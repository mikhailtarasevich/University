package com.mikhail.tarasevich.university.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;


@SuperBuilder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("teacher")
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "teacher_title_id", referencedColumnName = "id")
    TeacherTitle teacherTitle;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    Department department;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    List<Group> groups;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "teachers")
    List<Course> courses;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "teacher")
    List<Lesson> lessons;

}
