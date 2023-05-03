package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.LessonType;

import java.time.Duration;

public interface LessonTypeDao extends CrudPageableDao<LessonType> {

    void changeDuration (int id, Duration newDuration);

}
