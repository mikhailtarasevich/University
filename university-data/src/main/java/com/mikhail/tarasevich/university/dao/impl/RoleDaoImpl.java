package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.RoleDao;
import com.mikhail.tarasevich.university.entity.Role;
import com.mikhail.tarasevich.university.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;


@Repository
public class RoleDaoImpl extends AbstractPageableCrudDaoImpl<Role> implements RoleDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";
    private static final String FIND_PRIVILEGES_RELATE_TO_USER_QUERY =
            "SELECT r FROM Role r LEFT JOIN r.users u WHERE u.id = :userId";

    public RoleDaoImpl(EntityManager entityManager) {
        super(entityManager, Role.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

    @Override
    public void addRoleForUser(int userId, int roleId) {
        Role role = entityManager.find(clazz, roleId);
        User user = entityManager.find(User.class, userId);
        role.getUsers().add(user);
    }

    @Override
    public void unbindRoleFromUser(int userId) {
        Query query = entityManager.createQuery(FIND_PRIVILEGES_RELATE_TO_USER_QUERY);
        query.setParameter("userId", userId);
        Role role = (Role) query.getSingleResult();
        role.getUsers().removeIf(u -> u.getId() == userId);
    }

}
