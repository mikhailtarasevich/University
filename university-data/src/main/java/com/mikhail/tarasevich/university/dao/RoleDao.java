package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Role;

public interface RoleDao extends CrudPageableDao<Role> {

    void addRoleForUser(int userId, int roleId);
    void unbindRoleFromUser(int userId);

}
