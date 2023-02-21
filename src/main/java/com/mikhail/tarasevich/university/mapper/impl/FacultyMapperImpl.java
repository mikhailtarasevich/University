package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.dto.FacultyResponse;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.mapper.FacultyMapper;
import org.springframework.stereotype.Component;

@Component
public class FacultyMapperImpl implements FacultyMapper {

    @Override
    public Faculty toEntity(FacultyRequest facultyRequest) {
        return Faculty.builder()
                .withId(facultyRequest.getId())
                .withName(facultyRequest.getName())
                .withDescription(facultyRequest.getDescription())
                .build();
    }

    @Override
    public FacultyResponse toResponse(Faculty f) {
        FacultyResponse facultyResponse = new FacultyResponse();
        facultyResponse.setId(f.getId());
        facultyResponse.setName(f.getName());
        facultyResponse.setDescription(f.getDescription());

        return facultyResponse;
    }

}
