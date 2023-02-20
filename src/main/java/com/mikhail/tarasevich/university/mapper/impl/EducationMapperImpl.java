package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.mapper.EducationFormMapper;
import org.springframework.stereotype.Component;

@Component
public class EducationMapperImpl implements EducationFormMapper {

    @Override
    public EducationForm toEntity(EducationFormRequest e) {
        return EducationForm.builder()
                .withId(e.getId())
                .withName(e.getName())
                .build();
    }

    @Override
    public EducationFormResponse toResponse(EducationForm e) {
        EducationFormResponse educationFormResponse = new EducationFormResponse();
        educationFormResponse.setId(e.getId());
        educationFormResponse.setName(e.getName());

        return educationFormResponse;
    }

}
