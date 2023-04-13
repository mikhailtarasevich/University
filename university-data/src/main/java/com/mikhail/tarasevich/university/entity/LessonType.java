package com.mikhail.tarasevich.university.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "lesson_types")
public class LessonType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "duration")
    Duration duration;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "lessonType")
    List<Lesson> lessons;

}
