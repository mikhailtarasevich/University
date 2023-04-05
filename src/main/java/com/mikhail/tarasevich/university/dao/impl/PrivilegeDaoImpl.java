package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.PrivilegeDao;
import com.mikhail.tarasevich.university.entity.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PrivilegeDaoImpl extends AbstractPageableCrudDaoImpl<Privilege> implements PrivilegeDao {

    private static final String SAVE_QUERY = "INSERT INTO privileges (name) VALUES(?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM privileges ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM privileges WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM privileges WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM privileges ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE privileges SET name = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM privileges WHERE id = ?";
    private static final String ADD_PRIVILEGE_TO_ROLE_QUERY = "INSERT INTO role_privileges (role_id, privilege_id) VALUES (?, ?)";
    private static final String UNBIND_PRIVILEGE_FROM_ROLE_QUERY = "DELETE FROM role_privileges WHERE role_id = ?";
    private static final String FIND_PRIVILEGES_RELATE_TO_ROLE_QUERY = "SELECT privileges.id, privileges.name " +
            "FROM roles " +
            "LEFT JOIN role_privileges on roles.id = role_id " +
            "LEFT JOIN privileges on privilege_id = privileges.id " +
            "WHERE role_id = ?";
    private static final String FIND_PRIVILEGES_RELATE_TO_USER_QUERY = "SELECT privileges.id, privileges.name " +
            "FROM users " +
            "JOIN user_roles ON users.id = user_id " +
            "JOIN roles ON role_id = roles.id " +
            "JOIN role_privileges ON roles.id = role_privileges.role_id " +
            "JOIN privileges ON privilege_id = privileges.id " +
            "WHERE email = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM privileges";
    private static final RowMapper<Privilege> ROW_MAPPER = (resultSet, rowNum) ->
            Privilege.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .build();

    @Autowired
    public PrivilegeDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public void addPrivilegeToRole(int roleId, int privilegeId) {
        jdbcTemplate.update(ADD_PRIVILEGE_TO_ROLE_QUERY, roleId, privilegeId);
    }

    @Override
    public void unbindPrivilegeFromRole(int roleId) {
        jdbcTemplate.update(UNBIND_PRIVILEGE_FROM_ROLE_QUERY, roleId);
    }

    @Override
    public List<Privilege> findPrivilegesRelateToRole(int roleId) {
        return jdbcTemplate.query(FIND_PRIVILEGES_RELATE_TO_ROLE_QUERY, mapper, roleId);
    }

    @Override
    public List<Privilege> findPrivilegesRelateToUser(String email) {
        return jdbcTemplate.query(FIND_PRIVILEGES_RELATE_TO_USER_QUERY, mapper, email);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Privilege entity) throws SQLException {
        ps.setString(1, entity.getName());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Privilege entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getId());
    }

    @Override
    protected Privilege makeEntityWithId(Privilege role, int id) {
        return Privilege.builder()
                .withId(id)
                .withName(role.getName())
                .build();
    }

}
