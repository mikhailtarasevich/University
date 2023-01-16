package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Lesson;

import java.time.LocalDateTime;

public interface LessonDao extends CrudPageableDao<Lesson>{

    void changeGroup(int lessonId, int newGroupId);
    void changeTeacher(int lessonId, int newTeacherId);
    void changeCourse(int lessonId, int newCourseId);
    void changeLessonType(int lessonId, int newLessonTypeId);
    void changeStartTime(int lessonId, LocalDateTime newStartTime);

}
