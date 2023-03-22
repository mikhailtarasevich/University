package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.RoleDao;
import com.mikhail.tarasevich.university.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class RoleDaoImpl extends AbstractPageableCrudDaoImpl<Role> implements RoleDao {

    private static final String SAVE_QUERY = "INSERT INTO roles (name) VALUES(?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM roles ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM roles WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM roles WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM roles ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE roles SET name = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM roles WHERE id = ?";
    private static final String ADD_ROLE_FOR_USER_QUERY = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
    private static final String UNBIND_ROLE_FROM_USER_QUERY = "DELETE FROM user_roles WHERE user_id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM roles";
    private static final RowMapper<Role> ROW_MAPPER = (resultSet, rowNum) ->
            Role.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .build();

    @Autowired
    public RoleDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public void addRoleForUser(int userId, int roleId) {
        jdbcTemplate.update(ADD_ROLE_FOR_USER_QUERY, userId, roleId);
    }

    @Override
    public void unbindRoleFromUser(int userId) {
        jdbcTemplate.update(UNBIND_ROLE_FROM_USER_QUERY, userId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Role entity) throws SQLException {
        ps.setString(1, entity.getName());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Role entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getId());
    }

    @Override
    protected Role makeEntityWithId(Role role, int id) {
        return Role.builder()
                .withId(id)
                .withName(role.getName())
                .build();
    }

}
