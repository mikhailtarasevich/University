package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Teacher;

public interface TeacherMapper extends UserMapper<TeacherRequest, TeacherResponse, Teacher> {
}
