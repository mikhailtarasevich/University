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
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(g.getId());
        groupResponse.setName(g.getName());
        groupResponse.setFaculty(g.getFaculty());
        groupResponse.setHeadStudent(g.getHeadStudent());
        groupResponse.setEducationForm(g.getEducationForm());

        return groupResponse;
    }

}
