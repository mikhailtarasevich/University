package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.entity.EducationForm;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class EducationFormDaoImpl extends AbstractPageableCrudDaoImpl<EducationForm>
        implements EducationFormDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";

    public EducationFormDaoImpl(EntityManager entityManager) {
        super(entityManager, EducationForm.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

}
