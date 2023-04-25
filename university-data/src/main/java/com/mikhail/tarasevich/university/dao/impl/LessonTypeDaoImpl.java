package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.entity.LessonType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class LessonTypeDaoImpl extends AbstractPageableCrudDaoImpl<LessonType> implements LessonTypeDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";

    @Autowired
    public LessonTypeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, LessonType.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    public void changeDuration(int id, Duration newDuration) {
        Session session = sessionFactory.getCurrentSession();
        LessonType lessonType = session.get(clazz, id);
        lessonType.setDuration(newDuration);
    }

}
