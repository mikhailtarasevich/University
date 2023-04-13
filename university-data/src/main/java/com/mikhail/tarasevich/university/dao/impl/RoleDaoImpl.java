package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.RoleDao;
import com.mikhail.tarasevich.university.entity.Role;
import com.mikhail.tarasevich.university.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;


@Repository
public class RoleDaoImpl extends AbstractPageableCrudDaoImpl<Role> implements RoleDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String FIND_PRIVILEGES_RELATE_TO_USER_QUERY =
            "SELECT r FROM Role r LEFT JOIN r.users u WHERE u.id = :userId";

    @Autowired
    public RoleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Role.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    public void addRoleForUser(int userId, int roleId) {
        Session session = sessionFactory.getCurrentSession();
        Role role = session.get(clazz, roleId);
        User user = session.get(User.class, userId);
        role.getUsers().add(user);
    }

    @Override
    public void unbindRoleFromUser(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_PRIVILEGES_RELATE_TO_USER_QUERY);
        query.setParameter("userId", userId);
        Role role = (Role) query.getSingleResult();
        role.getUsers().removeIf(u -> u.getId() == userId);
    }

}
