package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.mapper.TeacherTitleMapper;
import org.springframework.stereotype.Component;

@Component
public class TeacherTitleMapperImpl implements TeacherTitleMapper {

    @Override
    public TeacherTitle toEntity(TeacherTitleRequest t) {
        return TeacherTitle.builder()
                .withId(t.getId())
                .withName(t.getName())
                .build();
    }

    @Override
    public TeacherTitleResponse toResponse(TeacherTitle t) {
        return new TeacherTitleResponse(t.getId(), t.getName());
    }

}
