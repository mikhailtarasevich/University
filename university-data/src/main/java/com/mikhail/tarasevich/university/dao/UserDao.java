package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.User;

public interface UserDao<E extends User> extends CrudPageableDao<E> {

    void updateGeneralUserInfo (E entity);
    void updateUserPassword (int id, String password);
    void addUserToGroup(int userId, int groupId);

}
