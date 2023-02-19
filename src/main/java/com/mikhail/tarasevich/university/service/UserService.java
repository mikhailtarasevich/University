package com.mikhail.tarasevich.university.service;

public interface UserService<REQUEST, RESPONSE> extends CrudService<REQUEST, RESPONSE> {

    boolean login(String email, String password);
    void subscribeUserToGroup(int userId, int groupId);

}
