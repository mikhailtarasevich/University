package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.PrivilegeDao;
import com.mikhail.tarasevich.university.entity.Privilege;
import com.mikhail.tarasevich.university.entity.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PrivilegeDaoImpl extends AbstractPageableCrudDaoImpl<Privilege> implements PrivilegeDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";
    private static final String FIND_PRIVILEGES_RELATE_TO_ROLE_QUERY =
            "SELECT p FROM Privilege p LEFT JOIN p.roles r WHERE r.id = :roleId";
    private static final String FIND_PRIVILEGES_RELATE_TO_USER_QUERY =
            "SELECT p FROM Privilege p LEFT JOIN p.roles r LEFT JOIN r.users u WHERE u.email = :email";

    public PrivilegeDaoImpl(EntityManager entityManager) {
        super(entityManager, Privilege.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

    @Override
    public void addPrivilegeToRole(int roleId, int privilegeId) {
        Privilege privilege = entityManager.find(clazz, privilegeId);
        Role role = entityManager.find(Role.class, roleId);
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
        Query query = entityManager.createQuery(FIND_PRIVILEGES_RELATE_TO_ROLE_QUERY);
        query.setParameter("roleId", roleId);

        return query.getResultList();
    }

    @Override
    public List<Privilege> findPrivilegesRelateToUser(String email) {
        Query query = entityManager.createQuery(FIND_PRIVILEGES_RELATE_TO_USER_QUERY);
        query.setParameter("email", email);

        return query.getResultList();
    }

}
