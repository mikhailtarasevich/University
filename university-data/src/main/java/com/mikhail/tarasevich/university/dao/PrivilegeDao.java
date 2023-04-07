package com.mikhail.tarasevich.university.dao;

import com.mikhail.tarasevich.university.entity.Privilege;

import java.util.List;

public interface PrivilegeDao extends CrudPageableDao<Privilege> {

    void addPrivilegeToRole(int roleId, int privilegeId);
    void unbindPrivilegeFromRole(int roleId);
    List<Privilege> findPrivilegesRelateToRole(int roleId);
    List<Privilege> findPrivilegesRelateToUser(String email);

}
