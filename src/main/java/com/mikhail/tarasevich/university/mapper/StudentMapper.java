package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Student;

public interface StudentMapper extends UserMapper<StudentRequest, StudentResponse, Student> {
}
