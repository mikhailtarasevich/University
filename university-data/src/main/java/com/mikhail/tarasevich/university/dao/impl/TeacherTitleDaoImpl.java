package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherTitleDaoImpl extends AbstractPageableCrudDaoImpl<TeacherTitle>
        implements TeacherTitleDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";

    @Autowired
    public TeacherTitleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, TeacherTitle.class, UNIQUE_NAME_PARAMETER);
    }

}
