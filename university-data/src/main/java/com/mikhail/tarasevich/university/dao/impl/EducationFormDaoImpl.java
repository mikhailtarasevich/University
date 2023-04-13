package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.entity.EducationForm;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EducationFormDaoImpl extends AbstractPageableCrudDaoImpl<EducationForm>
        implements EducationFormDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";

    @Autowired
    public EducationFormDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, EducationForm.class, UNIQUE_NAME_PARAMETER);
    }

}
