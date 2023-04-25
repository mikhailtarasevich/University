package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.entity.Faculty;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class FacultyDaoImpl extends AbstractPageableCrudDaoImpl<Faculty> implements FacultyDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";

    public FacultyDaoImpl(EntityManager entityManager) {
        super(entityManager, Faculty.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

}
