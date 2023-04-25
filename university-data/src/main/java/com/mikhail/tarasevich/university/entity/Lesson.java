package com.mikhail.tarasevich.university.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    Course course;

    @ManyToOne
    @JoinColumn(name = "lesson_type_id", referencedColumnName = "id")
    LessonType lessonType;

    @Column(name = "start_time")
    LocalDateTime startTime;

}
