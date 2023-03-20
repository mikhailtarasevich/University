package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeacherTitleMapper {

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    TeacherTitle toEntity(TeacherTitleRequest request);

    TeacherTitleResponse toResponse(TeacherTitle entity);

}
