package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.entity.LessonType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.Duration;

@Repository
public class LessonTypeDaoImpl extends AbstractPageableCrudDaoImpl<LessonType> implements LessonTypeDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";

    public LessonTypeDaoImpl(EntityManager entityManager) {
        super(entityManager, LessonType.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

    @Override
    public void changeDuration(int id, Duration newDuration) {
        LessonType lessonType = entityManager.find(clazz, id);
        lessonType.setDuration(newDuration);
    }

}
