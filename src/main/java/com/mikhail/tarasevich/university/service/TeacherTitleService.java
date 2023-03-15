package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;

public interface TeacherTitleService extends CrudService<TeacherTitleRequest, TeacherTitleResponse> {
    int lastPageNumber();
}
