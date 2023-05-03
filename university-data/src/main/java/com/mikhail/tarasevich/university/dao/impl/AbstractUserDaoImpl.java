package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.UserDao;
import com.mikhail.tarasevich.university.entity.User;

import javax.persistence.EntityManager;

public abstract class AbstractUserDaoImpl<E extends User> extends AbstractPageableCrudDaoImpl<E>
        implements UserDao<E> {

    private static final String UNIQUE_NAME_PARAMETER = "email";
    private static final String ORDER_BY_QUERY = "id";

    protected AbstractUserDaoImpl(EntityManager entityManager, Class<E> clazz) {
        super(entityManager, clazz, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }


    @Override
    public void updateGeneralUserInfo(E entity) {
        User userForUpdate = entityManager.find(clazz, entity.getId());
        userForUpdate.setFirstName(entity.getFirstName());
        userForUpdate.setLastName(entity.getLastName());
        userForUpdate.setGender(entity.getGender());
        userForUpdate.setEmail(entity.getEmail());
    }

    @Override
    public void updateUserPassword(int id, String password) {
        User userForUpdate = entityManager.find(clazz, id);
        userForUpdate.setPassword(password);
    }

}
