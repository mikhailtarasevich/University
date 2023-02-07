package com.mikhail.tarasevich.university.service;

import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.dto.LessonResponse;

import java.util.List;

public interface LessonService extends CrudService<LessonRequest, LessonResponse> {

    List<LessonResponse> findLessonsRelateToGroup(int groupId);

}
