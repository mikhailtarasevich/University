package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class TeacherTitleDaoImpl extends AbstractPageableCrudDaoImpl<TeacherTitle>
        implements TeacherTitleDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";

    public TeacherTitleDaoImpl(EntityManager entityManager) {
        super(entityManager, TeacherTitle.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

}
