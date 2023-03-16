package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.*;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LessonMapperImpl implements LessonMapper {

    @Override
    public Lesson toEntity(LessonRequest l) {
        return Lesson.builder()
                .withId(l.getId())
                .withName(l.getName())
                .withGroup(Group.builder().withId(l.getGroupId()).build())
                .withTeacher(Teacher.builder().withId(l.getTeacherId()).build())
                .withCourse(Course.builder().withId(l.getCourseId()).build())
                .withLessonType(LessonType.builder().withId(l.getLessonTypeId()).build())
                .withStartTime(l.getStartTime())
                .build();
    }

    @Override
    public LessonResponse toResponse(Lesson l) {
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId(l.getId());
        lessonResponse.setName(l.getName());
        lessonResponse.setGroup(l.getGroup());
        lessonResponse.setTeacher(l.getTeacher());
        lessonResponse.setCourse(l.getCourse());
        lessonResponse.setLessonType(l.getLessonType());
        lessonResponse.setStartTime(l.getStartTime());

        return lessonResponse;
    }

}
