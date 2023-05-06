package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    Optional<Lesson> findByName(String name);

    List<Lesson> findLessonByGroupId(int groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.group.id = :newGroupId WHERE l.id = :lessonId")
    void changeGroup(@Param("lessonId") int lessonId, @Param("newGroupId") int newGroupId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.teacher.id = :newTeacherId WHERE l.id = :lessonId")
    void changeTeacher(@Param("lessonId") int lessonId, @Param("newTeacherId") int newTeacherId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.course.id = :newCourseId WHERE l.id = :lessonId")
    void changeCourse(@Param("lessonId") int lessonId, @Param("newCourseId") int newCourseId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.lessonType.id = :newLessonTypeId WHERE l.id = :lessonId")
    void changeLessonType(@Param("lessonId") int lessonId, @Param("newLessonTypeId") int newLessonTypeId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.startTime = :newStartTime WHERE l.id = :lessonId")
    void changeStartTime(@Param("lessonId") int lessonId, @Param("newStartTime") LocalDateTime newStartTime);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.course = NULL WHERE l.course.id = :courseId")
    void unbindLessonsFromCourse(@Param("courseId") int courseId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.teacher = NULL WHERE l.teacher.id = :teacherId")
    void unbindLessonsFromTeacher(@Param("teacherId") int teacherId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.group = NULL WHERE l.group.id = :groupId")
    void unbindLessonsFromGroup(@Param("groupId") int groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Lesson l SET l.lessonType = NULL WHERE l.lessonType.id = :lessonTypeId")
    void unbindLessonsFromLessonType(@Param("lessonTypeId") int lessonTypeId);

}
