package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EducationFormMapper {

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    EducationForm toEntity(EducationFormRequest request);

    EducationFormResponse toResponse(EducationForm entity);

}
