package com.mikhail.tarasevich.university.mapper;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Department;

public interface DepartmentMapper extends Mapper<DepartmentRequest, DepartmentResponse, Department> {
}
