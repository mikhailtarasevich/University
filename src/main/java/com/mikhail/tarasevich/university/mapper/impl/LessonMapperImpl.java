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
        return new LessonResponse(l.getId(), l.getName(), l.getGroup(), l.getTeacher(), l.getCourse(),
                l.getLessonType(), l.getStartTime());
    }

}
