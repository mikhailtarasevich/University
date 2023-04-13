package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.PrivilegeDao;
import com.mikhail.tarasevich.university.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class PrivilegeDaoImpl extends AbstractPageableCrudDaoImpl<Privilege> implements PrivilegeDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String FIND_PRIVILEGES_RELATE_TO_ROLE_QUERY =
            "SELECT p FROM Privilege p LEFT JOIN p.roles r WHERE r.id = :roleId";
    private static final String FIND_PRIVILEGES_RELATE_TO_USER_QUERY =
            "SELECT p FROM Privilege p LEFT JOIN p.roles r LEFT JOIN r.users u WHERE u.email = :email";

    @Autowired
    public PrivilegeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Privilege.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    public void addPrivilegeToRole(int roleId, int privilegeId) {
        Session session = sessionFactory.getCurrentSession();
        Privilege privilege = session.get(clazz, privilegeId);
        Role role = session.get(Role.class, roleId);
        privilege.getRoles().add(role);
    }

    @Override
    public void unbindPrivilegeFromRole(int roleId) {
        List<Privilege> privilegesRelateToRole = findPrivilegesRelateToRole(roleId);
        privilegesRelateToRole.forEach(
                p -> p.getRoles().removeIf(r -> r.getId() == roleId)
        );
    }

    @Override
    public List<Privilege> findPrivilegesRelateToRole(int roleId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_PRIVILEGES_RELATE_TO_ROLE_QUERY);
        query.setParameter("roleId", roleId);

        return query.getResultList();
    }

    @Override
    public List<Privilege> findPrivilegesRelateToUser(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_PRIVILEGES_RELATE_TO_USER_QUERY);
        query.setParameter("email", email);

        return query.getResultList();
    }

}
