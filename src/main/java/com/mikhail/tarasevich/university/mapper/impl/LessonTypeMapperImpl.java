package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.mapper.LessonTypeMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonTypeMapperImpl implements LessonTypeMapper {

    @Override
    public LessonType toEntity(LessonTypeRequest lt) {
        return LessonType.builder()
                .withId(lt.getId())
                .withName(lt.getName())
                .withDuration(lt.getDuration())
                .build();
    }

    @Override
    public LessonTypeResponse toResponse(LessonType lt) {
        LessonTypeResponse lessonTypeResponse = new LessonTypeResponse();
        lessonTypeResponse.setId(lt.getId());
        lessonTypeResponse.setName(lt.getName());
        lessonTypeResponse.setDuration(lt.getDuration());

        return lessonTypeResponse;
    }

}
