package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.entity.Group;

public interface GroupMapper extends Mapper<GroupRequest, GroupResponse, Group> {
}
