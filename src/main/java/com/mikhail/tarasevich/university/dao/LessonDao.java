package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Lesson;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonDao extends CrudPageableDao<Lesson>{

    List<Lesson> findLessonsRelateToGroup(int groupId);
    void changeGroup(int lessonId, int newGroupId);
    void changeTeacher(int lessonId, int newTeacherId);
    void changeCourse(int lessonId, int newCourseId);
    void changeLessonType(int lessonId, int newLessonTypeId);
    void changeStartTime(int lessonId, LocalDateTime newStartTime);
    void unbindLessonsFromCourse(int courseId);
    void unbindLessonsFromTeacher(int teacherId);
    void unbindLessonsFromGroup(int groupId);
    void unbindLessonsFromLessonType(int lessonTypeId);

}
