package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.UserDao;
import com.mikhail.tarasevich.university.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractUserDaoImpl<E extends User> extends AbstractPageableCrudDaoImpl<E>
        implements UserDao<E> {

    private static final String UNIQUE_NAME_PARAMETER = "email";

    protected AbstractUserDaoImpl(SessionFactory sessionFactory, Class<E> clazz) {
        super(sessionFactory, clazz, UNIQUE_NAME_PARAMETER);
    }


    @Override
    public void updateGeneralUserInfo(E entity) {
        Session session = sessionFactory.getCurrentSession();
        User userForUpdate = session.get(clazz, entity.getId());
        userForUpdate.setFirstName(entity.getFirstName());
        userForUpdate.setLastName(entity.getLastName());
        userForUpdate.setGender(entity.getGender());
        userForUpdate.setEmail(entity.getEmail());
    }

    @Override
    public void updateUserPassword(int id, String password) {
        Session session = sessionFactory.getCurrentSession();
        User userForUpdate = session.get(clazz, id);
        userForUpdate.setPassword(password);
    }

}
