package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.User;

public interface UserDao<E extends User> extends CrudPageableDao<E> {

    void addUserToGroup(int userId, Integer groupId);

}
