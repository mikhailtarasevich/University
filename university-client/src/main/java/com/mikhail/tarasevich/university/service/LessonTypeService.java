package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;

public interface LessonTypeService extends CrudService<LessonTypeRequest, LessonTypeResponse> {
    int lastPageNumber();
}
