package com.mikhail.tarasevich.university.mapper.impl;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.entity.*;
import com.mikhail.tarasevich.university.mapper.GroupMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupMapperImpl implements GroupMapper {

    @Override
    public Group toEntity(GroupRequest g) {
        return Group.builder()
                .withId(g.getId())
                .withName(g.getName())
                .withFaculty(g.getFaculty())
                .withHeadStudent(g.getHeadStudent())
                .withEducationForm(g.getEducationForm())
                .build();
    }

    @Override
    public GroupResponse toResponse(Group g) {
        return new GroupResponse(g.getId(), g.getName(), g.getFaculty(), g.getHeadStudent(), g.getEducationForm());
    }

}
