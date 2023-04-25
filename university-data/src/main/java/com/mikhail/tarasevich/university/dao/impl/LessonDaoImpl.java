package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LessonDaoImpl extends AbstractPageableCrudDaoImpl<Lesson> implements LessonDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String FIND_LESSONS_RELATE_TO_GROUP_QUERY =
            "SELECT l FROM Lesson l LEFT JOIN l.group g WHERE g.id = :groupId";
    private static final String UPDATE_GROUP_QUERY =
            "UPDATE lessons SET group_id = :groupId WHERE id = :lessonId";
    private static final String UPDATE_TEACHER_QUERY =
            "UPDATE lessons SET user_id = :teacherId WHERE id = :lessonId";
    private static final String UPDATE_COURSE_QUERY =
            "UPDATE lessons SET course_id = :courseId WHERE id = :lessonId";
    private static final String UPDATE_LESSON_TYPE_QUERY =
            "UPDATE lessons SET lesson_type_id = :lessonTypeId WHERE id = :lessonId";
    private static final String UNBIND_GROUP_QUERY =
            "UPDATE lessons SET group_id = NULL WHERE group_id = :groupId";
    private static final String UNBIND_TEACHER_QUERY =
            "UPDATE lessons SET user_id = NULL WHERE user_id = :teacherId";
    private static final String UNBIND_COURSE_QUERY =
            "UPDATE lessons SET course_id = NULL WHERE course_id = :courseId";
    private static final String UNBIND_LESSON_TYPE_QUERY =
            "UPDATE lessons SET lesson_type_id = NULL WHERE lesson_type_id = :lessonTypeId";

    @Autowired
    public LessonDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Lesson.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    public List<Lesson> findLessonsRelateToGroup(int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_LESSONS_RELATE_TO_GROUP_QUERY);
        query.setParameter("groupId", groupId);

        return query.getResultList();
    }

    @Override
    public void changeGroup(int lessonId, int newGroupId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_GROUP_QUERY)
                .setParameter("groupId", newGroupId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeTeacher(int lessonId, int newTeacherId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_TEACHER_QUERY)
                .setParameter("teacherId", newTeacherId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeCourse(int lessonId, int newCourseId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_COURSE_QUERY)
                .setParameter("courseId", newCourseId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeLessonType(int lessonId, int newLessonTypeId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_LESSON_TYPE_QUERY)
                .setParameter("lessonTypeId", newLessonTypeId)
                .setParameter("lessonId", lessonId)
                .executeUpdate();
    }

    @Override
    public void changeStartTime(int lessonId, LocalDateTime newStartTime) {
        Session session = sessionFactory.getCurrentSession();
        Lesson lesson = session.get(clazz, lessonId);
        lesson.setStartTime(newStartTime);
    }

    @Override
    public void unbindLessonsFromCourse(int courseId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_COURSE_QUERY)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public void unbindLessonsFromTeacher(int teacherId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_TEACHER_QUERY)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
    }

    @Override
    public void unbindLessonsFromGroup(int groupId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_GROUP_QUERY)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void unbindLessonsFromLessonType(int lessonTypeId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_LESSON_TYPE_QUERY)
                .setParameter("lessonTypeId", lessonTypeId)
                .executeUpdate();
    }

}
