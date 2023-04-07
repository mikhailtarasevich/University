package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService extends CrudService<DepartmentRequest, DepartmentResponse> {
    void addCoursesToDepartment(int departmentId, List<Integer> courseIds);
    void deleteCoursesFromDepartment(int departmentId, List<Integer> courseIds);
    int lastPageNumber();
}
