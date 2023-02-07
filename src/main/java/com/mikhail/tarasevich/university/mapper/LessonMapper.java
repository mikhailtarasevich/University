package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;
import com.mikhail.tarasevich.university.entity.Lesson;

public interface LessonMapper extends Mapper<LessonRequest, LessonResponse, Lesson> {
}
