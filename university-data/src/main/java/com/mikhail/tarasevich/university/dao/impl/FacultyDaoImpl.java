package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.entity.Faculty;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FacultyDaoImpl extends AbstractPageableCrudDaoImpl<Faculty> implements FacultyDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";

    @Autowired
    public FacultyDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Faculty.class, UNIQUE_NAME_PARAMETER);
    }

}
