package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.*;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonMapperImpl implements LessonMapper {

    @Override
    public Lesson toEntity(LessonRequest l) {
        return Lesson.builder()
                .withId(l.getId())
                .withName(l.getName())
                .withGroup(l.getGroup())
                .withTeacher(l.getTeacher())
                .withCourse(l.getCourse())
                .withLessonType(l.getLessonType())
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
