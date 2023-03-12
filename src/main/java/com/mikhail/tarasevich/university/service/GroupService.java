package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;

import java.util.List;

public interface GroupService extends CrudService<GroupRequest, GroupResponse> {

    List<GroupResponse> findGroupsRelateToTeacher (int teacherId);
    List<GroupResponse> findGroupsNotRelateToTeacher (int teacherId);

}
